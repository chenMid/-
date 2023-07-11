package wakoo.fun.controller.RoleController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.Vo.MsgVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.*;
import wakoo.fun.pojo.ButtonPermissions;
import wakoo.fun.pojo.Role;
import wakoo.fun.service.RoleService;
import wakoo.fun.utils.MenuTreeRole;
import wakoo.fun.utils.MsgUtils;

import javax.annotation.Resource;
import java.util.List;

@EnableTransactionManagement//数据库事务管理
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @ApiOperation(value = "角色")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("/SelectRole")
    public MsgVo SelectRole(String keyword, Integer pageSize, Integer pageNumber) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Role> allRole = roleService.getAllRole(keyword);
        PageInfo<Role> pageInfo = new PageInfo<>(allRole);
        return new MsgVo(MsgUtils.SUCCESS, pageInfo);
    }

    @ApiOperation(value = "权限操作")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("/getButton")
    public MsgVo getButton() {
        List<ButtonPermissions> button = roleService.getButton();
        List<ButtonPermissions> menuList = new MenuTreeRole(button).buildTree();
        return new MsgVo(200, "菜单列表", menuList);
    }

    @ApiOperation(value = "角色添加")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PostMapping("/addRole")
    public MsgVo addRole(@RequestBody RoleButtonDto role) {
        Boolean aBoolean = roleService.addRole(role);
        Boolean aBoolean1 = roleService.addPermission(role.getList(), role.getId());
        return new MsgVo(MsgUtils.SUCCESS, role);
    }

    @ApiOperation(value = "查询指定角色信息")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("/updgetRole")
    public MsgVo updgetRole(Integer id) {
        List<ButtonPermissions> buttonPermissions = roleService.updGetAllPermissions(id);
        List<ButtonPermissions> menuList = new MenuTreeRole(buttonPermissions).buildTree();
        return new MsgVo(200, "菜单列表", menuList);
    }


    @ApiOperation(value = "查询指定角色信息")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PutMapping("/udpRoleMess")
    public MsgVo udpRoleMess(@RequestBody UpdRoleDto updRoleDto) {
        try {
            Boolean aBoolean = roleService.updMess(updRoleDto);
            Boolean aBoolean1 = roleService.updMessRole(updRoleDto.getList());
            if (aBoolean && aBoolean1) {
                return new MsgVo(200, "修改成功", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MsgVo(403, "修改失败", false);
    }

    @ApiOperation(value = "修改角色状态")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PutMapping("/updRoleStatus")
    public MsgVo updRoleStatus(Integer id, Integer status) {
        Integer roleNum = roleService.getRoleNum(id);
        if (roleNum != 0) {
            return new MsgVo(200, "该角色下还有用户无法修改状态", false);
        } else {
            Boolean aBoolean = roleService.updStatusRole(id, status);
            if (aBoolean) {
                return new MsgVo(200, "修改成功", aBoolean);
            }
        }
        return new MsgVo(403, "修改失败", null);
    }
}

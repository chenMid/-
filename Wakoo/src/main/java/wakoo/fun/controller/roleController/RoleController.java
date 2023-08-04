package wakoo.fun.controller.roleController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.*;
import wakoo.fun.pojo.ButtonPermissions;
import wakoo.fun.pojo.Role;
import wakoo.fun.service.RoleService;
import wakoo.fun.utils.MenuTreeRole;
import wakoo.fun.utils.MsgUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    public MsgVo getButton(Integer roleId) {
        Integer byIdRoleName = roleService.getByIdRoleName(roleId);
        RoleIdRoleName twoRoleName = roleService.getTwoRoleName(roleId);
        List<RoleGetButonById> buttonById = roleService.getButtonById(roleId);
        List<ButtonPermissions> button = roleService.getButton(roleId);
        List<Integer> oneByid = roleService.getOneByid();
        for (ButtonPermissions me:button) {
            if (me.getType()==10){
                for (ButtonPermissions m:button) {
                    if (me.getPid().equals(m.getId())){
                        me.setPpid(m.getPid());
                    }
                }
            }
        }
        List<ButtonPermissions> menuList = new MenuTreeRole(button).buildTree();
        Map<String, Object> roleButton=new HashMap<>();
        roleButton.put("fRoleName", byIdRoleName);
        roleButton.put("roleName", twoRoleName.getRoleName());
        roleButton.put("status", twoRoleName.getStatus());
        roleButton.put("roleButton", menuList);
        roleButton.put("buttonById", buttonById);
        roleButton.put("oneByid", oneByid);
        return new MsgVo(200, "菜单列表", roleButton);
    }

    @ApiOperation(value = "角色添加")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PostMapping("/addRole")
    public MsgVo addRole(@RequestBody RoleButtonDto role) {
        try {
            if (role.getList().equals(null)){
                return new MsgVo(403,"请勾选权限",false);
            }
            // 创建一个Set集合，用于存储唯一的ID
            Set<Integer> uniqueIds = new HashSet<>();

            // 遍历子类ID数组
            for (Integer childId : role.getList()) {
                // 递归查找并添加父级和祖父级的ID
                findParentAndAncestorIds(childId, uniqueIds);
                // 添加子类ID
                uniqueIds.add(childId);
            }
            // 将Set集合转换为逗号分隔的字符串
            String ids = StringUtils.join(uniqueIds, ",");
//            Integer[] list = role.getList(); // 替换为实际的 Integer 数组
//            String dataStr = Arrays.stream(list)
//                    .map(String::valueOf)
//                    .collect(Collectors.joining(","));
            Boolean aBoolean1 = roleService.exampleQueryWhetherARoleIsDisplayed(role.getFId());
            if (aBoolean1){
                return new MsgVo(200,"角色权限重复,请重新添加",aBoolean1);
            }
            Boolean aBoolean = roleService.addRole(role, ids);
            return new MsgVo(200,"添加成功",aBoolean);
        } catch (Exception ex) {
            return new MsgVo(MsgUtils.FAILED);
        }
    }

    @ApiOperation(value = "查询指定角色信息")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("/updgetRole")
    public MsgVo updgetRole(Integer id) {
        System.out.println(id);
        List<ButtonPermissions> buttonPermissions = roleService.updGetAllPermissions(id);
        for (ButtonPermissions me:buttonPermissions) {
            if (me.getType()==10){
                for (ButtonPermissions m:buttonPermissions) {
                    if (me.getPid().equals(m.getId())){
                        me.setPpid(m.getPid());
                    }
                }
            }
        }
        List<ButtonPermissions> menuList = new MenuTreeRole(buttonPermissions).buildTree();
        return new MsgVo(200, "菜单列表", menuList);
    }


    @ApiOperation(value = "修改角色信息")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PutMapping("/udpRoleMess")
    public MsgVo udpRoleMess(@RequestBody UpdRoleDto updRoleDto) {
            // 修改角色信息
            if (updRoleDto != null) {
                Boolean aBoolean = roleService.updMess(updRoleDto);
                Boolean aBoolean2 = roleService.UpdRoleStatus(updRoleDto.getId());
                Boolean aBoolean1 = roleService.updMessRole(updRoleDto.getList());
                if (aBoolean && aBoolean1 && aBoolean2) {
                    return new MsgVo(200, "修改成功", true);
                }
            }
        return new MsgVo(200, "修改成功", true);
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

    private void findParentAndAncestorIds(Integer id, Set<Integer> uniqueIds) {
        // 根据子类的ID查询数据库获取父级ID
        String parentId = roleService.getParentIdById(id);
        if (parentId != null) {
            // 递归查找父级的父级ID
            findParentAndAncestorIds(Integer.parseInt(parentId), uniqueIds);
            // 添加父级ID
            uniqueIds.add(Integer.parseInt(parentId));
        }
    }


    @ApiOperation(value = "获取当前页面按钮状态")
    @UserLoginToken
    @GetMapping("/getButtonMenus")
    public ResponseEntity<MsgVo> getButtonMenus(HttpServletRequest request, Integer menuId) {
        try {
            Object userId = request.getAttribute("userId");
            List<Map<String, Boolean>> buttonRolea = roleService.getButtonRolea((Integer) userId, menuId);
            List<Map<String, Boolean>> towButton = roleService.getTowButton(menuId);

            for (Map<String, Boolean> map : buttonRolea) {
                String name = String.valueOf(map.get("name"));
                boolean hidden = map.get("hidden");

                for (Map<String, Boolean> stringBooleanMap : towButton) {
                    if (name.equals(String.valueOf(stringBooleanMap.get("name")))) {
                        boolean invertedHidden = !hidden;
                        stringBooleanMap.put("hidden", invertedHidden);
                    }
                }
            }
            return ResponseEntity.ok(new MsgVo(200, "请求成功", towButton));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new MsgVo(500, "服务器内部错误", null));
        }
    }
}

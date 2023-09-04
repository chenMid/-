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
import wakoo.fun.common.Log;
import wakoo.fun.log.Constants;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.utils.RoleUtils;
import wakoo.fun.vo.AgentIdrId;
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
import java.util.stream.Collectors;

@EnableTransactionManagement
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Role")
public class RoleController {
    @Resource
    private AdminAdministrationService adminAdministrationService;
    @Resource
    private RoleService roleService;

    @ApiOperation(value = "角色")
    @UserLoginToken
    @GetMapping("/SelectRole")
    public MsgVo selectRole(String keyword, Integer pageSize, Integer pageNumber, HttpServletRequest request) {
        pageNumber = Math.max(pageNumber, 1);

        Object userId = request.getAttribute("userId");
        System.out.println(userId);
        PageHelper.startPage(pageNumber, pageSize);
        List<Role> allRole = roleService.getAllRole(keyword, (Integer) userId);
        PageInfo<Role> pageInfo = new PageInfo<>(allRole);
        return new MsgVo(MsgUtils.SUCCESS, pageInfo);
    }

    @ApiOperation(value = "权限操作")
    @UserLoginToken
    @GetMapping("/getButton")
    public MsgVo getButton(Integer roleId) {
        Integer byIdRoleName = roleService.getByIdRoleName(roleId);
        RoleIdRoleName twoRoleName = roleService.getTwoRoleName(roleId);
        List<RoleGetButonById> buttonById = roleService.getButtonById(roleId);
        List<ButtonPermissions> button = roleService.getButton(roleId);
        List<Integer> oneByid = roleService.getOneByid();
        for (ButtonPermissions me : button) {
            if (me.getType() == 10) {
                for (ButtonPermissions m : button) {
                    if (me.getPid().equals(m.getId())) {
                        me.setPpid(m.getPid());
                    }
                }
            }
        }
        List<ButtonPermissions> menuList = new MenuTreeRole(button).buildTree();
        Map<String, Object> roleButton = new HashMap<>();
        roleButton.put("fRoleName", byIdRoleName);
        roleButton.put("roleName", twoRoleName.getRoleName());
        roleButton.put("status", twoRoleName.getStatus());
        roleButton.put("roleButton", menuList);
        roleButton.put("buttonById", buttonById);
        roleButton.put("oneByid", oneByid);
        return new MsgVo(200, "菜单列表", roleButton);
    }

    @ApiOperation(value = "角色添加")
    @Transactional(rollbackFor = Exception.class)
    @UserLoginToken
    @Log(modul = "角色页面-角色添加", type = Constants.INSERT, desc = "操作添加按钮")
    @PostMapping("/addRole")
    public MsgVo addRole(@RequestBody RoleButtonDto role) {
            // 调用adminAdministrationService的getRoles方法，获取角色列表，并赋值给roles变量
            List<AgentIdrId> roles = adminAdministrationService.getRoles();
            // 初始化一个整型变量a，并赋值为0
            int parentId=0;
            int a = 0;
            // 定义一个整型数组validIds，包含有效的角色ID
            int[] validIds = {0, 1, 2, 3};
            for (int b:validIds) {
                if (role.getFId().equals(b)){
                    parentId=role.getFId();
                    break;
                }else {
                    parentId = RoleUtils.findParent(roles, role.getFId(), validIds);
                }
            }
            // 调用RoleUtils的findParent方法，传入roles、id和validIds参数，获取父级ID，并赋值给parentId变量
            System.out.println(parentId);
            System.out.println(role.getFId());
            if (parentId==0){
                role.setRoleCode("admin");
            }else if (parentId==1){
                role.setRoleCode("总部");
            }else if (parentId==2){
                role.setRoleCode("总代");
            }else if (parentId==3){
                role.setRoleCode("代理");
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
            Boolean aBoolean = roleService.addRole(role, ids);
            return new MsgVo(200, "添加成功", aBoolean);
    }

    @ApiOperation(value = "查询添加权限")
    @UserLoginToken
    @GetMapping("/updgetRole")
    public MsgVo updgetRole(Integer id) {
        List<ButtonPermissions> buttonPermissions = roleService.updGetAllPermissions(id);
        for (ButtonPermissions me : buttonPermissions) {
            if (me.getType() == 10) {
                for (ButtonPermissions m : buttonPermissions) {
                    if (me.getPid().equals(m.getId())) {
                        me.setPpid(m.getPid());
                    }
                }
            }
        }
        List<ButtonPermissions> menuList = new MenuTreeRole(buttonPermissions).buildTree();
        return new MsgVo(200, "菜单列表", menuList);
    }


    @ApiOperation(value = "修改角色信息")
    @Transactional(rollbackFor = Exception.class)
    @UserLoginToken
    @Log(modul = "角色页面-角色修改", type = Constants.INSERT, desc = "操作修改按钮")
    @PutMapping("/udpRoleMess")
    public MsgVo udpRoleMess(@RequestBody UpdRoleDto updRoleDto) {
        StringBuilder number = new StringBuilder();
        for (Integer a : updRoleDto.getList()) {
            number.append(number.length() == 0 ? a : ("," + a));
        }
        Integer[] integers1 = roleService.addPermission(String.valueOf(number));
        Integer[] integers = roleService.getsTheParentMenu(String.valueOf(number));
        number.append(",");
        for (int i = 0; i < integers.length; i++) {
            if (i > 0) {
                number.append(",");
            }
            number.append(integers[i]);
        }
        number.append(",");
        for (int i = 0; i < integers1.length; i++) {
            if (i > 0) {
                number.append(",");
            }
            number.append(integers1[i]);
        }
        String[] strings = number.toString().split(",");
        Set<String> uniqueStrings = new HashSet<>(Arrays.asList(strings));
        updRoleDto.setMenus(String.join(",", uniqueStrings));
        Boolean aBoolean = roleService.updMess(updRoleDto);
        if (updRoleDto.getRid() <= 2) {
            String parentId1 = roleService.getParentIdByRealId(updRoleDto.getRid());
            String parentId2 = roleService.getParentIdByRealId(updRoleDto.getRid() + 1);
            Set<String> parentIdSet1 = new HashSet<>(Arrays.asList(parentId1.split(",")));
            String[] parentIdArray2 = parentId2.split(",");
            List<String> filteredParentIds = new ArrayList<>();
            for (String parentId : parentIdArray2) {
                if (parentIdSet1.contains(parentId.trim())) {
                    filteredParentIds.add(parentId.trim());
                }
            }
            String filteredParentId2 = String.join(",", filteredParentIds);
            UpdRoleDto updRoleDto1 = new UpdRoleDto(updRoleDto.getRid(), updRoleDto.getStatus(), filteredParentId2);
            Boolean aBoolean1 = roleService.modifyTheLowerLevelPermissionsConsistently(updRoleDto1);
            if (aBoolean1) {
                return new MsgVo(200, "修改成功", true);
            }
        }

        if (aBoolean) {
            return new MsgVo(200, "修改成功", true);
        }

        return new MsgVo(200, "修改成功", true);
    }

    @ApiOperation(value = "修改角色状态")
    @Transactional(rollbackFor = Exception.class)
    @UserLoginToken
    @Log(modul = "角色页面-修改角色状态", type = Constants.UPDATE, desc = "操作修改状态")
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
    public MsgVo getButtonMenus(HttpServletRequest request, Integer menuId) {
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

            return new MsgVo(200, "请求成功", towButton);
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgVo(500, "服务器内部错误", null);
        }
    }
}

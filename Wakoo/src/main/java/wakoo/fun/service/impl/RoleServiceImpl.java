package wakoo.fun.service.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.dto.RoleButtonDto;
import wakoo.fun.dto.RoleGetButonById;
import wakoo.fun.dto.RoleIdRoleName;
import wakoo.fun.dto.UpdRoleDto;
import wakoo.fun.mapper.RoleMapper;
import wakoo.fun.pojo.ButtonPermissions;
import wakoo.fun.pojo.Role;
import wakoo.fun.service.RoleService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> getAllRole(String keyword,Integer userId) {
        return roleMapper.getAllRole(keyword, userId);
    }

    @Override
    public List<ButtonPermissions> getButton(Integer roleId) {
        return roleMapper.getButton(roleId);
    }

    @Override
    public List<RoleGetButonById> getButtonById(Integer roleId) {
        return roleMapper.getButtonById(roleId);
    }

    @Override
    public List<Integer> getOneByid() {
        return roleMapper.getOneByid();
    }

    @Override
    public Boolean addRole(RoleButtonDto roleButtonDto,String list) {
        return roleMapper.addRole(roleButtonDto,list);
    }

    @Override
    public Integer[] addPermission(String menus) {
        return roleMapper.getPermission(menus);
    }

    @Override
    public List<ButtonPermissions> updGetAllPermissions(Integer id) {
        return roleMapper.updGetAllPermissions(id);
    }

    @Override
    public Boolean updMess(UpdRoleDto updRoleDto) {
        return roleMapper.updMess(updRoleDto);
    }

    @Override
    public Boolean updMessRole(Integer[] updRoleDto) {
        return roleMapper.updMessRole(updRoleDto);
    }

    @Override
    public Integer getRoleNum(Integer id) {
        return roleMapper.getRoleNum(id);
    }

    @Override
    public Boolean updStatusRole(Integer id, Integer status) {
        return roleMapper.updStatusRole(id, status);
    }

    @Override
    public Integer getByIdRoleName(Integer roleId) {
        return roleMapper.getByIdRoleName(roleId);
    }

    @Override
    public RoleIdRoleName getTwoRoleName(Integer roleId) {
        return roleMapper.getTwoRoleName(roleId);
    }

    @Override
    public Boolean UpdRoleStatus(Integer roleId) {
        return roleMapper.UpdRoleStatus(roleId);
    }

    @Override
    public Boolean delRole(Integer rid) {
        return roleMapper.delRole(rid);
    }

    @Override
    public String getParentIdById(Integer id) {
        return roleMapper.getParentIdById(id);
    }

    @Override
    public List<Map<String,Boolean>> getButtonRolea(Integer roleId, Integer menuId) {
        return roleMapper.getButtonRolea(roleId, menuId);
    }

    @Override
    public List<Map<String, Boolean>> getTowButton(Integer menuId) {
        return roleMapper.getTowButton(menuId);
    }

    @Override
    public Boolean exampleQueryWhetherARoleIsDisplayed(Integer id) {
        return roleMapper.exampleQueryWhetherARoleIsDisplayed(id) > 0;
    }

    @Override
    public Integer[] getsTheParentMenu(String menus) {
        return roleMapper.getsTheParentMenu(menus);
    }

    @Override
    public String getParentIdByRealId(Integer rid) {
        return roleMapper.getParentIdByRealId(rid);
    }

    @Override
    public Boolean modifyTheLowerLevelPermissionsConsistently(UpdRoleDto updRoleDto) {
        return roleMapper.modifyTheLowerLevelPermissionsConsistently(updRoleDto);
    }

    @Override
    public Integer exampleQueryWhetherTheParentExists(Integer rid) {
        return roleMapper.exampleQueryWhetherTheParentExists(rid);
    }

    @Override
    public Integer exampleQueryTheParentIdUnderId(Integer id) {
        return roleMapper.exampleQueryTheParentIdUnderId(id);
    }

}

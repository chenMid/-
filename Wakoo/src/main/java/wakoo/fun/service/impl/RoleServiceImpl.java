package wakoo.fun.service.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.dto.RoleButtonDto;
import wakoo.fun.Vo.RoleVo;
import wakoo.fun.dto.RoleIdRoleName;
import wakoo.fun.dto.UpdRoleDto;
import wakoo.fun.mapper.RoleMapper;
import wakoo.fun.pojo.ButtonPermissions;
import wakoo.fun.pojo.Role;
import wakoo.fun.service.RoleService;

import javax.annotation.Resource;
import java.util.List;
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> getAllRole(String keyword) {
        return roleMapper.getAllRole(keyword);
    }

    @Override
    public List<ButtonPermissions> getButton() {
        return roleMapper.getButton();
    }

    @Override
    public List<Integer> getButtonById() {
        return roleMapper.getButtonById();
    }

    @Override
    public List<Integer> getOneByid() {
        return roleMapper.getOneByid();
    }

    @Override
    public Boolean addRole(RoleButtonDto roleButtonDto) {
        return roleMapper.addRole(roleButtonDto);
    }

    @Override
    public Boolean addPermission(List<RoleVo> roleVos, Integer id) {
        return roleMapper.addPermission(roleVos, id);
    }

    @Override
    public List<ButtonPermissions> updGetAllPermissions() {
        return roleMapper.updGetAllPermissions();
    }

    @Override
    public Boolean updMess(UpdRoleDto updRoleDto) {
        return roleMapper.updMess(updRoleDto);
    }

    @Override
    public Boolean updMessRole(List<UpdRoleDto> updRoleDto) {
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

}

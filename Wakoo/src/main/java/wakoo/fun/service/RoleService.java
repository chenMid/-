package wakoo.fun.service;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.dto.RoleButtonDto;
import wakoo.fun.Vo.RoleVo;
import wakoo.fun.dto.UpdRoleDto;
import wakoo.fun.pojo.ButtonPermissions;
import wakoo.fun.pojo.Role;

import java.util.List;

public interface RoleService {
    /**
     * 查询角色信息
     * @return
     */
    List<Role> getAllRole(String keyword);
    /**
     * 查询按钮
     * @return
     */
    List<ButtonPermissions> getButton();

    /**
     * 添加角色
     * @return
     */
    Boolean addRole(RoleButtonDto roleButtonDto);
    /**
     * 插入多条语句
     * @param roleVos
     * @return
     */
    Boolean addPermission(@Param("roleVos") List<RoleVo> roleVos, @Param("id") Integer id);
    /**
     * 查询所有菜单并显示
     * @param id
     * @return
     */
    List<ButtonPermissions> updGetAllPermissions(@Param("id") Integer id);
    /**
     * 修改角色信息
     * @return
     */
    Boolean updMess(@Param("role") UpdRoleDto updRoleDto);
    /**
     * 修改角色信息
     * @param updRoleDto
     * @return
     */
    Boolean updMessRole(@Param("role") List<UpdRoleDto> updRoleDto);
    /**
     * 查询这个角色下有没有用户
     * @param id
     * @return
     */
    Integer getRoleNum(Integer id);
    /**
     * 修改角色状态
     * @return
     */
    Boolean updStatusRole(@Param("id") Integer id, @Param("status") Integer status);

}

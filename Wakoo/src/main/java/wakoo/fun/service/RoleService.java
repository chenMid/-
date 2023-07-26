package wakoo.fun.service;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import wakoo.fun.dto.RoleButtonDto;
import wakoo.fun.dto.RoleGetButonById;
import wakoo.fun.dto.RoleIdRoleName;
import wakoo.fun.dto.UpdRoleDto;
import wakoo.fun.pojo.ButtonPermissions;
import wakoo.fun.pojo.Role;

import java.util.List;
import java.util.Map;

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
    List<ButtonPermissions> getButton(Integer roleId);
    List<RoleGetButonById> getButtonById(Integer roleId);
    List<Integer> getOneByid();

    /**
     * 添加角色
     * @return
     */
    Boolean addRole(RoleButtonDto roleButtonDto,String list);
    /**
     * 插入多条语句
     * @param roleVos
     * @return
     */
    Boolean addPermission(@Param("roleVos") Integer[] roleVos, @Param("id") Integer id);
    /**
     * 查询所有菜单并显示
     * @return
     */
    List<ButtonPermissions> updGetAllPermissions(Integer id);
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
    Boolean updMessRole(@Param("role") Integer[] updRoleDto);
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
    /**
     * 查询一级角色名称
     * @param roleId
     * @return
     */
    Integer getByIdRoleName(@Param("roleId") Integer roleId);
    /**
     * 查询二级角色名称
     * @param roleId
     * @return
     */
    RoleIdRoleName getTwoRoleName(@Param("roleId") Integer roleId);
    /**
     * 修改状态
     * @return
     */
    Boolean UpdRoleStatus(Integer roleId);
    /**删除
     *
     */
    Boolean delRole(@Param("idList") Integer rid);
    /**
     * 通过子类id查询父类及子类
     */
    String getParentIdById(Integer id);
    /**
     * 查询按钮状态
     * @return
     */
    @MapKey("buttonName")
    List<Map<String,Boolean>> getButtonRolea(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId);
    @MapKey("twoName")
    List<Map<String,Boolean>> getTowButton(@Param("menuId") Integer menuId);
}

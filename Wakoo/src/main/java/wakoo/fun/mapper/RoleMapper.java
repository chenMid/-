package wakoo.fun.mapper;

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

public interface RoleMapper {
    /**
     * 查询角色信息
     * @return
     */
    List<Role> getAllRole(String keyword,Integer userId);
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
    Boolean addRole(@Param("role") RoleButtonDto roleButtonDto, @Param("aaa") String list);
    /**
     * 查询祖宗 菜单。
     * @return
     */
    Integer[] getPermission(String menus);
    /**
     * 查询所有菜单并显示
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
    Boolean UpdRoleStatus(@Param("roleId") Integer roleId);
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
    /**
     * 查询是否含有角色
     * @param id
     * @return
     */
    Integer exampleQueryWhetherARoleIsDisplayed(Integer id);

    /**获取角色下的菜单和高一级或底一级的菜单做比较
     * @return
     */
    String getParentIdByRealId(Integer rid);
    /**查询父级 菜单id
     *
     */
    Integer[] getsTheParentMenu(String menus);

    /**修改下级权限一致
     *
     * @param id
     * @return
     */
    Boolean modifyTheLowerLevelPermissionsConsistently(@Param("updRoleDto") UpdRoleDto updRoleDto);

    /**
     * 查询父级id
     *
     * @param rid 角色id
     * @return 返回父级id
     */
    Integer exampleQueryWhetherTheParentExists(Integer rid);
    /**
     * 查询父级id
     *
     * @param id 角色id
     * @return 返回父级id
     */
    Integer exampleQueryTheParentIdUnderId(Integer id);
}

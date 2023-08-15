package wakoo.fun.service;


import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import wakoo.fun.vo.AdminVo;
import wakoo.fun.vo.AllId;
import wakoo.fun.dto.*;
import wakoo.fun.pojo.FaAdmin;

import java.util.List;
import java.util.Map;

public interface AdminAdministrationService {
    /**
     * 查询管理员管理页面
     * @return
     */
    List<AdminAdministraltion> getAllAdministraltion(String keyword,Integer userId,Integer isnull);
    /**
     *  查询管理员管理页面的角色
     * @return
     */
    List<RoleDto> getRole(Integer userId);
    /**
     * 获取代理下拉框
     * @return
     */
    List<OrderQuantity> getOrderQ(Integer id,Integer role);

    /**
     * 添加管理用户
     * @return
     */
    Boolean isUserAdmin(AdmininistraltionDto adminAdministraltion);
    /**
     * 判断用户邮箱手机号是否重复
     * @param AdminDto
     * @return
     */
    List<AdminAdministraltion>isAdminEmailMobile(@Param("AdminDto") AdmininistraltionDto AdminDto);
    /**
     * 判断用户邮箱手机号是否重复
     * @param updAdminDto
     * @return
     */
    List<UpdAdminDto>isUpdAdminDto(@Param("UpdAdminDto") UpdAdminDto updAdminDto);


    /**
     * 添加关系表信息
     * @param uid
     * @param rid
     * @return
     */
    Boolean isUserRoleOrder(@Param("uid") Integer uid, @Param("rid") Integer rid);
    /**
     * 修改用户状态
     * @param id
     * @param status
     * @return
     */
    Boolean UpdStatus(@Param("id") Integer id, @Param("status") String status);
    /**
     * 查询指定用户信息
     * @param id
     * @return
     */
    AdminVo getAdminVo(@Param("id") Integer id);
    AdminVo getAdminVos(@Param("id") Integer id);
    AdminVo getAdminVoss(@Param("id") Integer id);
    /**
     * 修改组别门店
     * @return
     */
    Boolean updUserRole(@Param("uAdmin") UpdAdminDto updAdminDto);
    /**
     * 修改用户信息
     * @param updAdminDto
     * @return
     */
    Boolean updAdminUser(@Param("aUser") UpdAdminDto updAdminDto);
    /**
     * 获取指定用户
     * @param id
     * @return
     */
    FaAdmin getFaAdmin(Integer id);
    /**
     * 获取代理id
     * @return
     */
    AllId getAgentId(Integer id);
    /**
     * 修改campus_id
     * @param
     * @return
     */
    Boolean updAgentName(@Param("userId") Integer userId, @Param("campusId") Integer campusId);
    /**
     * 修改order——id
     */
    Boolean updAOder(@Param("id") Integer id, @Param("userId") Integer userId);

    /**
     * 获取门店
     * @return
     */
    List<Integer> getTheStoreUnderTheRole();
    /**
     * 获取角色的id
     * @param userId 指定user的id
     * @return 返回role-id
     */
    Integer getsTheIdOfTheRole(Integer userId);
    /**
     *  获取您的帐户ID
     * @param userId id
     * @return 返回字符串
     */
    String getYourAccountID(Integer userId);
    /**
     * 修改权限显示
     * @param userId id
     * @return 返回true
     */
    Boolean modifyDisplayUser(@Param("userId") Integer userId, @Param("affiliatedId") String affiliatedId);
    /**
     * 查询角色id权限
     * @param userId
     * @return
     */
    Integer exampleQueryTheIdPermissionOfARole(@Param("userId") Integer userId);
    /**
     * 过去角色id
     * @param userId 账号的id
     * @return 返回角色id
     */
    Integer getTheRoleId(Integer userId);
    /**
     *
     * @param userId
     * @return
     */
    Integer GetTheRoleId(Integer userId);
    /**
     * 查询指定用户的人
     * @param id
     * @return
     */
    Map<String,Map<Integer, String>> getAllAgent(Integer id);
    /**
     * 查询人员
     * @return
     */
    List<AdminAdministraltion> getsAllUsersWithSpecifiedPermissions(Integer role,Integer userId);
    /**
     * 如果是开发者或总部调用该方法
     * @return 返回查询到的普通人
     */
    List<Map<String,String>> getEveryoneWhoDoesnTHaveAnAccount();
    /**
     * 查询跟登录账号有关的未被分配的人
     * @param userId
     * @return
     */
    List<Map<String,String>> SearchrdinaryeoplAgent(Integer userId);
    /**
     * 查询没有账号的人包括自己
     */
    @MapKey("order")
    List<Map<String,String>>getNoUserperson(Integer id);

    /**
     * 修改回显
     * @param userId
     * @return
     */
    AdminAdministraltion getAll(Integer userId);
    /**
     * 获取 总部使用的EG
     * @return
     */
    String getCampusId(Integer userId);
}

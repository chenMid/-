package wakoo.fun.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import wakoo.fun.vo.*;
import wakoo.fun.dto.*;
import wakoo.fun.pojo.FaAdmin;

import java.util.List;
import java.util.Map;

public interface AdministrationMapper {
    /**
     * 查询管理员管理页面
     * @return
     */
    List<AdminAdministraltion> getAllAdministraltion(@Param("keyword") String keyword, @Param("userId") Integer userId, @Param("isnull") Integer isnull);
    /**
     *  查询管理员管理页面的角色
     * @return
     */
    List<RoleDto> getRole(Integer userId);
    /**
     * 获取代理下拉框
     * @return
     */
    List<OrderQuantity> getOrderQ(@Param("id") Integer id, @Param("role") Integer role);
    /**
     * 添加用户
     * @return
     */
    Boolean isUser(@Param("AdminDto") AdmininistraltionDto admininistraltionDto);

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
     * @param ids id
     * @return 提示的结果
     */
    Boolean UpdStatus(@Param("ids") Integer[] ids);

    /**
     * 查询指定用户信息
     * @param id
     * @return
     */
    AdminVo getAdminVo(@Param("id") Integer id);
    AdminVo getAdminVos(@Param("id") Integer id);
    AdminVo getAdminVoss(@Param("id") Integer id);
    AdminVo getAdminVosss(@Param("id") Integer id);
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
    AllId getAgentId(@Param("id") Integer id);

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
     * @param userId
     * @return
     */
    String getTheRoleId(Integer userId);

    /**
     * 验证角色id
     * @param userId
     * @return
     */
    Integer GetTheRoleId(Integer userId);

    /**
     * 查询指定用户的人
     * @param id
     * @return
     */
    @MapKey("key")
    Map<String,Map<Integer, String>> getAllAgent(Integer id);

    /**
     * 查询所有指定权限用户
     * @return
     */
    List<AdminAdministraltion> getsAllUsersWithSpecifiedPermissions(@Param("role") Integer role, @Param("userId") Integer userId);

    /**
     * 如果是开发者或总部调用该方法
     * @return 返回查询到的普通人
     */
    @MapKey("order")
    List<Map<String,String>> getEveryoneWhoDoesnTHaveAnAccount();

    /**
     * 查询跟登录账号有关的未被分配的人
     * @param userId
     * @return
     */
    @MapKey("order")
    List<Map<String,String>> SearchrdinaryeoplAgent(Integer userId);
    /**
     * 查询没有账号的人包括自己
     */
    @MapKey("order")
    List<Map<String,String>>getNoUserperson(Integer id,Integer role);
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
    /**
     *  查询所有的角色id
     * @return 返回角色id的list
     */
    List<AgentIdrId> getRoles();

    /**
     * 查询rid
     * @param rid rid
     * @return rid
     */
    Integer getRoleId(Integer rid);

    /**
     * 获取agent_id
     * @param userId id
     * @return 返回字符串
     */
    String getTheAgentId(Integer userId);

    /**
     * 查询agent_id
     * @param id id
     * @return 返回字符串
     */
    @MapKey("order")
    List<Map<String,String>>getAProxyRole(Integer id);

    /**
     * 查询agent_id
     * @param id id
     * @return 返回字符串
     */
    @MapKey("order")
    List<Map<String,String>>agency(Integer id);

    /**
     * 查询id
     * @return 返
     */
    List<Integer> getPro_In_Id(Integer id);

    /**
     * 查询agent_id
     * @return 返回字符串
     */
    @MapKey("order")
    List<Map<String,String>>all();

    /**
     *  销毁账号
     *  @param ids ids
     * @return 返回一个list
     */
    Boolean destroyAccount(@Param("ids") Integer[] ids);

    /**
     *      多条件查询的控制
     * @param username 用户名
     * @param roleName 角色名
     * @param name 姓名
     * @param email 邮箱
     * @param mobile 手机
     * @param status 可存
     * @param userId id
     * @return 返回一个list
     */
    List<AdminAdministraltion> multiConditionQuery(@Param("name") String name, @Param("nickname") String nickname, @Param("username") String username, @Param("roleName") String roleName, @Param("email") String email, @Param("mobile") String mobile, @Param("status") String status, @Param("userId") Integer userId);

    /**
     * 获取指定用户
     * @param id
     * @return
     */
    FadminVo personalInformation(Integer id);
}

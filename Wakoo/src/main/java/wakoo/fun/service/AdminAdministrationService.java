package wakoo.fun.service;


import org.apache.ibatis.annotations.Param;
import wakoo.fun.vo.AdminVo;
import wakoo.fun.vo.AllId;
import wakoo.fun.dto.*;
import wakoo.fun.pojo.FaAdmin;

import java.util.List;

public interface AdminAdministrationService {
    /**
     * 查询管理员管理页面
     * @return
     */
    List<AdminAdministraltion> getAllAdministraltion(String keyword);
    /**
     *  查询管理员管理页面的角色
     * @return
     */
    List<RoleDto> getRole();
    /**
     * 获取代理下拉框
     * @return
     */
    List<OrderQuantity> getOrderQ();

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
     * @param oid
     * @return
     */
    Boolean isUserRoleOrder(@Param("uid") Integer uid, @Param("rid") Integer rid, @Param("oid") Integer oid);
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
     * @param id
     * @return
     */
    Boolean getTheStoreUnderTheRole(Integer id);
}

package wakoo.fun.service;


import org.apache.ibatis.annotations.Param;
import wakoo.fun.dto.AdminAdministraltion;
import wakoo.fun.dto.AdmininistraltionDto;
import wakoo.fun.dto.OrderQuantity;
import wakoo.fun.dto.RoleDto;

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
     * 添加关系表信息
     * @param uid
     * @param rid
     * @param oid
     * @return
     */
    Boolean isUserRoleOrder(@Param("uid") Integer uid, @Param("rid") Integer rid, @Param("oid") String oid);
    /**
     * 修改用户状态
     * @param id
     * @param status
     * @return
     */
    Boolean UpdStatus(@Param("id") Integer id, @Param("status") String status);
}

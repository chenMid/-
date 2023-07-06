package wakoo.fun.service.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.dto.AdminAdministraltion;
import wakoo.fun.dto.AdmininistraltionDto;
import wakoo.fun.dto.OrderQuantity;
import wakoo.fun.dto.RoleDto;
import wakoo.fun.mapper.AdministrationMapper;
import wakoo.fun.service.AdminAdministrationService;

import javax.annotation.Resource;
import java.util.List;
@Service
public class adminAdministrationServiceImpl implements AdminAdministrationService {
    @Resource
    private AdministrationMapper adminAdministration;

    /**
     * 查询管理页面
     * @return
     */
    @Override
    public List<AdminAdministraltion> getAllAdministraltion(String keyword) {
        return adminAdministration.getAllAdministraltion(keyword);
    }

    /**
     * 查询管理员页面中模态框中的角色
     * @return
     */
    @Override
    public List<RoleDto> getRole() {
        return adminAdministration.getRole();
    }

    /**
     * 获取代理下拉框
     * @return
     */
    @Override
    public List<OrderQuantity> getOrderQ() {
        return adminAdministration.getOrderQ();
    }

    /**
     * 添加用户
     * @return
     */
    @Override
    public Boolean isUserAdmin(AdmininistraltionDto adminAdministraltion) {
        return adminAdministration.isUser(adminAdministraltion);
    }

    /**
     * 添加关系
     * @param uid
     * @param rid
     * @param oid
     * @return
     */
    @Override
    public Boolean isUserRoleOrder(Integer uid, Integer rid, String oid) {
        return adminAdministration.isUserRoleOrder(uid, rid, oid);
    }

    @Override
    public Boolean UpdStatus(Integer id, String status) {
        return adminAdministration.UpdStatus(id, status);
    }
}

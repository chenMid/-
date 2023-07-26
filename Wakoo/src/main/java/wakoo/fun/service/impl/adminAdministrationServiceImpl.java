package wakoo.fun.service.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.vo.AdminVo;
import wakoo.fun.vo.AllId;
import wakoo.fun.dto.*;
import wakoo.fun.mapper.AdministrationMapper;
import wakoo.fun.pojo.FaAdmin;
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

    @Override
    public List<AdminAdministraltion> isAdminEmailMobile(AdmininistraltionDto AdminDto) {
        return adminAdministration.isAdminEmailMobile(AdminDto);
    }

    @Override
    public List<UpdAdminDto> isUpdAdminDto(UpdAdminDto updAdminDto) {
        return adminAdministration.isUpdAdminDto(updAdminDto);
    }

    /**
     * 添加关系
     * @param uid
     * @param rid
     * @param oid
     * @return
     */
    @Override
    public Boolean isUserRoleOrder(Integer uid, Integer rid, Integer oid) {
        return adminAdministration.isUserRoleOrder(uid, rid, oid);
    }

    @Override
    public Boolean UpdStatus(Integer id, String status) {
        return adminAdministration.UpdStatus(id, status);
    }

    @Override
    public AdminVo getAdminVo(Integer id) {
        return adminAdministration.getAdminVo(id);
    }

    @Override
    public AdminVo getAdminVos(Integer id) {
        return adminAdministration.getAdminVos(id);
    }

    @Override
    public AdminVo getAdminVoss(Integer id) {
        AdminVo adminVoss = adminAdministration.getAdminVoss(id);
        if (adminVoss == null || adminVoss.getName() == null) {
            AdminVo adminVosss = adminAdministration.getAdminVosss(id);
            return adminVosss;
        }
        return adminVoss;
    }
    @Override
    public Boolean updUserRole(UpdAdminDto updAdminDto) {
        return adminAdministration.updUserRole(updAdminDto);
    }

    @Override
    public Boolean updAdminUser(UpdAdminDto updAdminDto) {
        return adminAdministration.updAdminUser(updAdminDto);
    }

    @Override
    public FaAdmin getFaAdmin(Integer id) {
        return adminAdministration.getFaAdmin(id);
    }

    @Override
    public AllId getAgentId(Integer id) {
        return adminAdministration.getAgentId(id);
    }

    @Override
    public Boolean updAgentName(Integer userId, Integer campusId) {
        return adminAdministration.updAgentName(userId, campusId);
    }

    @Override
    public Boolean updAOder(Integer id, Integer userId) {
        return adminAdministration.updAOder(id, userId);
    }

    @Override
    public Boolean getTheStoreUnderTheRole(Integer id) {
        Integer theStoreUnderTheRole = adminAdministration.getTheStoreUnderTheRole(id);
        return theStoreUnderTheRole != null && theStoreUnderTheRole > 0;
    }
}

package wakoo.fun.service.impl;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import wakoo.fun.vo.*;
import wakoo.fun.dto.*;
import wakoo.fun.mapper.AdministrationMapper;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.service.AdminAdministrationService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class adminAdministrationServiceImpl implements AdminAdministrationService {
    @Resource
    private AdministrationMapper adminAdministration;

    /**
     * 查询管理页面
     * @return
     */
    @Override
    public List<AdminAdministraltion> getAllAdministraltion(String keyword,Integer userId,Integer isnull) {
        return adminAdministration.getAllAdministraltion(keyword,userId,isnull);
    }

    /**
     * 查询管理员页面中模态框中的角色
     * @return
     */
    @Override
    public List<RoleDto> getRole(Integer userId) {
        return adminAdministration.getRole(userId);
    }

    /**
     * 获取代理下拉框
     * @return
     */
    @Override
    public List<OrderQuantity> getOrderQ(Integer id,Integer role) {
        return adminAdministration.getOrderQ(id,role);
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
     * @return
     */
    @Override
    public Boolean isUserRoleOrder(Integer uid, Integer rid) {
        return adminAdministration.isUserRoleOrder(uid, rid);
    }

    @Override
    public Boolean UpdStatus(Integer[] id, Integer status) {
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
    public List<Integer> getTheStoreUnderTheRole() {
        return adminAdministration.getTheStoreUnderTheRole();
    }

    @Override
    public Integer getsTheIdOfTheRole(Integer userId) {
        return adminAdministration.getsTheIdOfTheRole(userId);
    }

    @Override
    public String getYourAccountID(Integer userId) {
        return adminAdministration.getYourAccountID(userId);
    }

    @Override
    public Boolean modifyDisplayUser(Integer userId, String affiliatedId) {
        return adminAdministration.modifyDisplayUser(userId, affiliatedId);
    }

    @Override
    public Integer exampleQueryTheIdPermissionOfARole(Integer userId) {
        return adminAdministration.exampleQueryTheIdPermissionOfARole(userId);
    }

    @Override
    public String getTheRoleId(Integer userId) {
        return adminAdministration.getTheRoleId(userId);
    }

    @Override
    public Integer GetTheRoleId(Integer userId) {
        return adminAdministration.GetTheRoleId(userId);
    }

    @Override
    public  Map<String,Map<Integer, String>> getAllAgent(Integer id) {
        return adminAdministration.getAllAgent(id);
    }

    @Override
    public  List<AdminAdministraltion>getsAllUsersWithSpecifiedPermissions(Integer role,Integer userId) {
        return adminAdministration.getsAllUsersWithSpecifiedPermissions(role,userId);
    }

    @Override
    public List<Map<String, String>> getEveryoneWhoDoesnTHaveAnAccount() {
        return adminAdministration.getEveryoneWhoDoesnTHaveAnAccount();
    }

    @Override
    public List<Map<String, String>> SearchrdinaryeoplAgent(Integer userId) {
        return adminAdministration.SearchrdinaryeoplAgent(userId);
    }

    @Override
    public List<Map<String, String>> getNoUserperson(Integer id,Integer role) {
        return adminAdministration.getNoUserperson(id,role);
    }

    @Override
    public AdminAdministraltion getAll(Integer userId) {
        return adminAdministration.getAll(userId);
    }

    @Override
    public String getCampusId(Integer userId) {
        return adminAdministration.getCampusId(userId);
    }

    @Override
    public List<AgentIdrId> getRoles() {
        return adminAdministration.getRoles();
    }

    @Override
    public Integer getRoleId(Integer rid) {
        return adminAdministration.getRoleId(rid);
    }

    @Override
    public String getTheAgentId(Integer userId) {
        return adminAdministration.getTheAgentId(userId);
    }

    @Override
    public List<Map<String, String>> getAProxyRole(Integer id) {
        return adminAdministration.getAProxyRole(id);
    }

    @Override
    public List<Map<String, String>> agency(Integer id) {
        return adminAdministration.agency(id);
    }

    @Override
    public List<Integer> getPro_In_Id(Integer id) {
        return adminAdministration.getPro_In_Id(id);
    }

    @Override
    public List<Map<String, String>> all() {
        return adminAdministration.all();
    }

    @Override
    public Boolean destroyAccount(Integer[] ids) {
        return adminAdministration.destroyAccount(ids);
    }


}

package wakoo.fun.controller.AdministrationController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.Vo.AdminVo;
import wakoo.fun.Vo.MsgVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.*;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.service.FaAdminService;
import wakoo.fun.utils.MsgUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableTransactionManagement//数据库事务管理
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Administration")
public class AdministrationController {

    @Resource
    private AdminAdministrationService adminAdministrationService;
    @Resource
    private FaAdminService faAdminService;

    @ApiOperation(value = "管理员管理查询")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("Administration")
    public MsgVo Administration(String keyword, Integer pageSize, Integer pageNumber) {
        PageHelper.startPage(pageNumber, pageSize);
        List<AdminAdministraltion> allAdministraltion = adminAdministrationService.getAllAdministraltion(keyword);
        PageInfo<AdminAdministraltion> pageInfo = new PageInfo<>(allAdministraltion);
        return new MsgVo(200, "请求成功", pageInfo);
    }


    @ApiOperation(value = "管理员角色权限查询")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("getRole")
    public MsgVo getRole(String roleName) {
        Map<String, Object> map = new HashMap<>();
        map.put("role", adminAdministrationService.getRole());
        return new MsgVo(MsgUtils.SUCCESS, map);
    }

    @ApiOperation(value = "管理员所属代理")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("/getAgentManagement")
    public MsgVo getAgentManagement() {
        Map<String, Object> map = new HashMap<>();
        map.put("Order", adminAdministrationService.getOrderQ());
        return new MsgVo(MsgUtils.SUCCESS, map);
    }

    @ApiOperation(value = "管理员管理添加")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PostMapping("addAdminUser")
    public MsgVo addAdminUser(@RequestBody AdmininistraltionDto admininistraltionDto) {
        List<AdminAdministraltion> adminEmailMobile = adminAdministrationService.isAdminEmailMobile(admininistraltionDto);
        if (adminEmailMobile.size()!=0){
            for (AdminAdministraltion a:adminEmailMobile) {
                if (a.getUsername().equals(admininistraltionDto.getUsername())){
                    return new MsgVo(403,"用户名已存在",false);
                }else if (a.getMobile().equals(admininistraltionDto.getMobile())){
                    return new MsgVo(403,"手机号重复请重新输入",false);
                }else if (a.getEmail().equals(admininistraltionDto.getEmail())){
                    return new MsgVo(403,"邮箱重复请重新输入",false);
                }
            }
        }
        try {
            Boolean userAdmin = adminAdministrationService.isUserAdmin(admininistraltionDto);
            if (userAdmin) {
                List<FaAdmin> faAdmins = faAdminService.faAdmin(admininistraltionDto.getUsername());
                if (faAdmins.size() != 0) {
                    adminAdministrationService.isUserRoleOrder(faAdmins.get(0).getId(), admininistraltionDto.getRoleName(), admininistraltionDto.getName());
                    return new MsgVo(200,"添加成功",userAdmin);
                }
            }
            return new MsgVo(MsgUtils.FAILED);
        } catch (Exception e) {
            // 在发生异常时进行事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new MsgVo(MsgUtils.FAILED);
        }
    }

    @ApiOperation(value = "管理员管理修改状态")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @PutMapping("Updstatus")
    public MsgVo Updstatus(Integer id, String status) {
        Boolean aBoolean = adminAdministrationService.UpdStatus(id, status);
        return new MsgVo(MsgUtils.SUCCESS, aBoolean);
    }


    @ApiOperation(value = "获取指定用户信息")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("/getIsAdmin")
    public MsgVo getIsAdmin(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        AdminVo adminVoss = adminAdministrationService.getAdminVoss((Integer) userId);
        AdminVo adminVos = adminAdministrationService.getAdminVos((Integer) userId);
        AdminVo adminVo = adminAdministrationService.getAdminVo((Integer) userId);
        adminVo.setId(adminVos.getId());
        adminVo.setRoleName(adminVos.getRoleName());
        adminVo.setAgentId(adminVoss.getAgentId());
        adminVo.setName(adminVoss.getName());

        return new MsgVo(MsgUtils.SUCCESS, adminVo);
    }

    @ApiOperation(value = "管理员管理修改")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PutMapping("/updIpAdmin")
    public MsgVo updIpAdmin(@RequestBody UpdAdminDto updAdminDto) {
        Boolean aBoolean = adminAdministrationService.updUserRole(updAdminDto);
        Boolean aBoolean1 = adminAdministrationService.updAdminUser(updAdminDto);
        if (aBoolean && aBoolean1) {
            return new MsgVo(MsgUtils.SUCCESS, true);
        }
        return new MsgVo(MsgUtils.SUCCESS, false);
    }
}

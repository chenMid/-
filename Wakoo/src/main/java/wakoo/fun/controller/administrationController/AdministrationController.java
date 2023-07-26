package wakoo.fun.controller.administrationController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.vo.AdminVo;
import wakoo.fun.vo.AllId;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.*;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.utils.HashUtils;
import wakoo.fun.utils.MsgUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableTransactionManagement//数据库事务管理
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "管理员页面")
public class AdministrationController {

    @Resource
    private AdminAdministrationService adminAdministrationService;

    @ApiOperation(value = "管理员管理查询")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("Administration")
    public MsgVo Administration(String keyword, Integer pageSize, Integer pageNumber) {
        try {
            if (pageSize == null || pageSize <= 0) {
                pageSize = 10; // 默认每页显示10条数据
            }
            if (pageNumber == null || pageNumber <= 0) {
                pageNumber = 1; // 默认显示第一页
            }

            PageHelper.startPage(pageNumber, pageSize);
            List<AdminAdministraltion> allAdministraltion = adminAdministrationService.getAllAdministraltion(keyword);
            for (AdminAdministraltion a : allAdministraltion) {
                if (a.getName() == null) {
                    a.setName(a.getRoleName());
                }
            }
            PageInfo<AdminAdministraltion> pageInfo = new PageInfo<>(allAdministraltion);
            return new MsgVo(200, "请求成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgVo(500, "请求处理失败", null);
        }
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
//    @Transactional
    @UserLoginToken
    @PostMapping("addAdminUser")
    public MsgVo addAdminUser(@Validated @RequestBody AdmininistraltionDto admininistraltionDto, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return new MsgVo(403, errorMessage, false);
        }
        List<AdminAdministraltion> adminEmailMobile = adminAdministrationService.isAdminEmailMobile(admininistraltionDto);
        if (adminEmailMobile.stream().anyMatch(a -> a.getUsername().equals(admininistraltionDto.getUsername()))) {
            return new MsgVo(403, "用户名已存在", false);
        } else if (adminEmailMobile.stream().anyMatch(a -> a.getMobile().equals(admininistraltionDto.getMobile()))) {
            return new MsgVo(403, "手机号重复请重新输入", false);
        } else if (adminEmailMobile.stream().anyMatch(a -> a.getEmail().equals(admininistraltionDto.getEmail()))) {
            return new MsgVo(403, "邮箱重复请重新输入", false);
        }
        try {
            Boolean userAdmin = adminAdministrationService.isUserAdmin(admininistraltionDto);
            if (admininistraltionDto.getName() == null) {
                adminAdministrationService.isUserRoleOrder(admininistraltionDto.getId(), admininistraltionDto.getRoleName(), admininistraltionDto.getName());
                AllId id = adminAdministrationService.getAgentId(admininistraltionDto.getId());
                admininistraltionDto.setName(id.getId());
                adminAdministrationService.updAgentName(admininistraltionDto.getId(), admininistraltionDto.getName());
                Boolean aBoolean = adminAdministrationService.updAOder(admininistraltionDto.getName(),admininistraltionDto.getId());
                userAdmin=false;
            }
            if (userAdmin) {
                adminAdministrationService.isUserRoleOrder(admininistraltionDto.getId(), admininistraltionDto.getRoleName(), admininistraltionDto.getName());
                return new MsgVo(200, "添加成功", userAdmin);
            }
            return new MsgVo(200, "添加成功", true);
        } catch (Exception e) {
             //在发生异常时进行事务回滚
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
    public MsgVo getIsAdmin(Integer userId) {
        AdminVo adminVoss = adminAdministrationService.getAdminVoss(userId);
        AdminVo adminVos = adminAdministrationService.getAdminVos(userId);
        AdminVo adminVo = adminAdministrationService.getAdminVo(userId);

        Integer roleName = adminVos != null ? adminVos.getRoleName() : null;
        Integer name = adminVoss != null ? adminVoss.getName() : null;

        adminVo.setRoleName(roleName);
        adminVo.setName(name);

        return new MsgVo(MsgUtils.SUCCESS, adminVo);
    }

    @ApiOperation(value = "管理员管理修改")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PutMapping("/updIpAdmin")
    public ResponseEntity<MsgVo> updIpAdmin(@Validated @Valid @RequestBody UpdAdminDto updAdminDto,BindingResult result) {
        try {

            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
                MsgVo response = new MsgVo(403, errorMessage, false);
                return ResponseEntity.ok(response);
            }
            boolean success = false;

            // 检查是否存在重复的用户名、邮箱或手机号
            List<UpdAdminDto> updAdminDtoList = adminAdministrationService.isUpdAdminDto(updAdminDto);
            FaAdmin faAdmin = adminAdministrationService.getFaAdmin(updAdminDto.getUserId());

            String username = updAdminDto.getUsername();
            if (username != null && !username.equals(faAdmin.getUserName())) {
                // 检查是否修改了用户名，并检查是否重复
                for (UpdAdminDto u : updAdminDtoList) {
                    if (u.getUsername().equals(username)) {
                        // 用户名重复
                        MsgVo response = new MsgVo(403, "用户名重复", false);
                        return ResponseEntity.ok(response);
                    }
                }
            }

            String email = updAdminDto.getEmail();
            if (email != null && !email.equals(faAdmin.getEmail())) {
                // 检查是否修改了邮箱，并检查是否重复
                for (UpdAdminDto u : updAdminDtoList) {
                    if (u.getEmail().equals(email)) {
                        // 邮箱重复
                        MsgVo response = new MsgVo(403, "邮箱重复", false);
                        return ResponseEntity.ok(response);
                    }
                }
            }

            String mobile = updAdminDto.getMobile();
            if (mobile != null && !mobile.equals(faAdmin.getMobile())) {
                // 检查是否修改了手机号，并检查是否重复
                for (UpdAdminDto u : updAdminDtoList) {
                    if (u.getMobile().equals(mobile)) {
                        // 手机号重复
                        MsgVo response = new MsgVo(403, "手机号重复", false);
                        return ResponseEntity.ok(response);
                    }
                }
            }

            String password = updAdminDto.getPassword();
            if (password != null) {
                // 检查是否修改了密码
                // 更新密码为哈希值
                updAdminDto.setPassword(HashUtils.hash(password));
            } else {
                // 如果密码未被修改，则保持原密码不变
                updAdminDto.setPassword(faAdmin.getPassword());
            }

            // 更新用户角色
            success = adminAdministrationService.updUserRole(updAdminDto);

            // 更新用户信息
            if (success) {
                success = adminAdministrationService.updAdminUser(updAdminDto);
            }

            // 更新成功
            if (success) {
                MsgVo response = new MsgVo(200, "更新成功", true);
                return ResponseEntity.ok(response);
            } else {
                // 更新失败
                MsgVo response = new MsgVo(500, "更新失败", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            // 异常处理
            MsgVo response = new MsgVo(500, "服务器内部错误", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @ApiOperation(value = "管理员管理添加")
    @UserLoginToken
    @GetMapping("verifyTheGroupLinkageOfAllStores")
    public ResponseEntity<MsgVo> verifyTheGroupLinkageOfAllStores(Integer id){
        Boolean theStoreUnderTheRole = adminAdministrationService.getTheStoreUnderTheRole(id);
        return ResponseEntity.ok(new MsgVo(200,"请求成功",theStoreUnderTheRole));
    }
}

package wakoo.fun.controller.administrationController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.ibatis.reflection.MetaObject;
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
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import javax.validation.Valid;
import java.util.*;

/**
 * @author 管理员页面
 */
@EnableTransactionManagement
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
    public MsgVo administration(String keyword, Integer pageSize, Integer pageNumber, HttpServletRequest request) {
        try {
            Object userId = request.getAttribute("userId");
            PageHelper.startPage(pageNumber, pageSize);
            List<AdminAdministraltion> adminAdministrations = new ArrayList<>();
            List<AdminAdministraltion> allAdministration = adminAdministrationService.getAllAdministraltion(keyword, (Integer) userId,null);
            Integer userId1 = (Integer) request.getAttribute("userId");
            System.out.println(userId1);
            Integer role = adminAdministrationService.getsTheIdOfTheRole(userId1);
            for (AdminAdministraltion a : allAdministration) {
                if (role==2||role==4){
                    if ("1".equals(a.getAgentId())){
                        continue;
                    }else if ("3".equals(a.getAgentId())){
                        continue;
                    }
                }
                if (!a.getId().equals(userId1)) {
                    adminAdministrations.add(a);
                }
                if (a.getName() == null) {
                    a.setName(a.getRoleName());
                }
            }
            PageInfo<AdminAdministraltion> pageInfo = new PageInfo<>(adminAdministrations);
            pageInfo.setPageSize(pageSize);
            return new MsgVo(200, "请求成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgVo(500, "请求处理失败", null);
        }
    }
    @ApiOperation(value = "检查是否为管理员和总部")
    @UserLoginToken
    @GetMapping("headquarters")
    public MsgVo headquarters(HttpServletRequest request){
        Object userId = request.getAttribute("userId");
        Integer role = adminAdministrationService.getsTheIdOfTheRole((Integer) userId);
        boolean isShow = role == 1 || role == 2;
        Map<String, Boolean> map = Collections.singletonMap("isShow", isShow);
        return new MsgVo(200, "请求成功", map);
    }
    @ApiOperation(value = "管理员角色权限查询")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("getRole")
    public MsgVo getRole(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        Map<String, Object> map = new HashMap<>(50);
        map.put("role", adminAdministrationService.getRole((Integer) userId));
        return new MsgVo(MsgUtils.SUCCESS, map);
    }

    @ApiOperation(value = "管理员所属代理")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("/getAgentManagement")
    public MsgVo getAgentManagement(HttpServletRequest request,Integer id) {
        int ids=0;
        Map<String, Object> map = new HashMap<>(50);
        Object userId = request.getAttribute("userId");
        Integer role = adminAdministrationService.getsTheIdOfTheRole((Integer) userId);
        if (role.equals(1)||role.equals(2)){
            if (id==4){
                ids=3;
            }else {
                ids=id;
            }
            List<Map<String,String>> stringMapMap = adminAdministrationService.getsAllUsersWithSpecifiedPermissions(id,ids);
            map.put("Order", stringMapMap);
            return new MsgVo(MsgUtils.SUCCESS, map);
        }
        map.put("Order", adminAdministrationService.getOrderQ((Integer) userId,id));
        return new MsgVo(MsgUtils.SUCCESS, map);
    }
    @ApiOperation(value = "获取没有账号人")
    @UserLoginToken
    @GetMapping("/getPeopleWithoutAccounts")
    public MsgVo getPeopleWithoutAccounts(HttpServletRequest request,Integer roleId){
        Object userId = request.getAttribute("userId");
        Integer role = adminAdministrationService.getsTheIdOfTheRole((Integer) userId);

        if (role.equals(1)||role.equals(2)){
            if (roleId!=null){
                Integer role1 = adminAdministrationService.getsTheIdOfTheRole(roleId);
                if (role1==3||role1==4){
                    return new MsgVo(200, "请求成功", adminAdministrationService.getNoUserperson(roleId));
                }
            }
            return new MsgVo(200,"请求成功",adminAdministrationService.getEveryoneWhoDoesnTHaveAnAccount());
        }else {
            return new MsgVo(200,"请求成功", adminAdministrationService.SearchrdinaryeoplAgent((Integer) userId));
        }
    }

    @ApiOperation(value = "管理员管理添加")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PostMapping("addAdminUser")
    public MsgVo addAdminUser(@Validated @RequestBody AdmininistraltionDto admininistraltionDto, BindingResult result, HttpServletRequest request) {
        Boolean userAdmin=true;
        Object userId = request.getAttribute("userId");

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

        if (admininistraltionDto.getRoleName() == 2) {
            admininistraltionDto.setName("**");
            admininistraltionDto.setAgentId("**");
        } else if (admininistraltionDto.getRoleName() == 1) {
            admininistraltionDto.setName("*");
            admininistraltionDto.setAgentId("*");
        }
        try {
        Integer integer = adminAdministrationService.exampleQueryTheIdPermissionOfARole((Integer) userId);
        if (integer == 3) {
            Integer theRoleId = adminAdministrationService.getTheRoleId((Integer) userId);
            System.out.println(theRoleId);
            admininistraltionDto.setAgentId(String.valueOf(theRoleId));
            userAdmin = adminAdministrationService.isUserAdmin(admininistraltionDto);
        } else {
            Integer role = adminAdministrationService.getsTheIdOfTheRole((Integer) userId);
            if (role == 2 || role == 1) {
                if (admininistraltionDto.getName() == null || "".equals(admininistraltionDto.getName())) {
                    admininistraltionDto.setName(admininistraltionDto.getAgentId());
                    userAdmin = adminAdministrationService.isUserAdmin(admininistraltionDto);
                } else {
                    if (admininistraltionDto.getAgentId()==null || "".equals(admininistraltionDto.getAgentId())){
                        admininistraltionDto.setAgentId(String.valueOf(admininistraltionDto.getName()));                        userAdmin = adminAdministrationService.isUserAdmin(admininistraltionDto);
                    }else {
                        userAdmin = adminAdministrationService.isUserAdmin(admininistraltionDto);
                    }
                }
            }else {
                admininistraltionDto.setAgentId(String.valueOf(admininistraltionDto.getName()));
                userAdmin = adminAdministrationService.isUserAdmin(admininistraltionDto);
            }
        }

        if (userAdmin) {
            adminAdministrationService.isUserRoleOrder(admininistraltionDto.getId(), admininistraltionDto.getRoleName());
            return new MsgVo(200, "添加成功", true);
        }
        // 添加返回值，确保在未满足条件时有返回结果

        } catch (Exception e) {
            //在发生异常时进行事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new MsgVo(MsgUtils.FAILED);
        }
        return new MsgVo(403, "权限不足", false);
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
        AdminAdministraltion all = adminAdministrationService.getAll(userId);
        return new MsgVo(MsgUtils.SUCCESS, all);
    }

    @ApiOperation(value = "管理员管理修改")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PutMapping("/updIpAdmin")
    public ResponseEntity<MsgVo> updIpAdmin(@Validated @Valid @RequestBody UpdAdminDto updAdminDto, BindingResult result,HttpServletRequest request) {
//        try {

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
//            Object userId = request.getAttribute("userId");
//            Integer role = adminAdministrationService.getsTheIdOfTheRole((Integer) userId);
//            if (role==1||role==2){
//
//            }
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
//        } catch (Exception e) {
//            // 异常处理
//            MsgVo response = new MsgVo(500, "服务器内部错误", false);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
    }

    @ApiOperation(value = "下拉框权限")
    @UserLoginToken
    @GetMapping("verifyTheGroupLinkageOfAllStores")
    public ResponseEntity<MsgVo> verifyTheGroupLinkageOfAllStores(HttpServletRequest request) {
        String roleId = request.getParameter("id");
        int role = Integer.parseInt(roleId);
        Map<String,Boolean> map=new HashMap<>(50);
        if (role==1||role==2){
                map.put("key", true);
            }else {
            map.put("key", false);
        }

        return ResponseEntity.ok(new MsgVo(200, "请求成功",map));
    }
}


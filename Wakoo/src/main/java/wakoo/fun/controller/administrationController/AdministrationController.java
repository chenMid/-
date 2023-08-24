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
import wakoo.fun.utils.RoleUtils;
import wakoo.fun.vo.AgentIdrId;
import wakoo.fun.vo.DeleteVo;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.*;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.utils.HashUtils;
import wakoo.fun.utils.MsgUtils;
import wakoo.fun.vo.StatusVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @GetMapping("/Administration")
    public MsgVo administration(String keyword, Integer pageSize, Integer pageNumber, HttpServletRequest request) {
        try {
            // 初始化一个整型变量parentId，并赋值为0
            int parentId = 0;
            // 从请求中获取名为userId的属性，并赋值给变量userId
            Object userId = request.getAttribute("userId");
            // 使用PageHelper进行分页，设置页码和每页大小
            PageHelper.startPage(pageNumber, pageSize);
            // 创建一个空的AdminAdministration对象列表adminAdministrations
            List<AdminAdministraltion> adminAdministrations = new ArrayList<>();
            // 调用adminAdministrationService的getAllAdministration方法，传入keyword、userId和null参数，获取一个AdminAdministration对象列表，并赋值给allAdministration变量
            List<AdminAdministraltion> allAdministration = adminAdministrationService.getAllAdministraltion(keyword, (Integer) userId, null);
            // 从请求中获取名为userId的属性，并将其转换为Integer类型，赋值给userId1变量
            Integer userId1 = (Integer) request.getAttribute("userId");
            // 调用adminAdministrationService的getsTheIdOfTheRole方法，传入userId1参数，获取一个角色ID，并赋值给role变量
            Integer role = adminAdministrationService.getsTheIdOfTheRole(userId1);
            // 调用adminAdministrationService的getCampusId方法，传入userId1参数，获取一个campusId，并赋值给campusId变量
            String campusId = adminAdministrationService.getCampusId(userId1);
            // 调用getParentId方法，传入parentId和role参数，根据规则获取新的parentId值，并赋值给parentId变量
            parentId = getParentId(parentId, role);
            // 遍历allAdministration列表中的每个AdminAdministration对象，赋值给变量a
            for (AdminAdministraltion a : allAdministration) {
                // 如果parentId等于1或者等于3
                if (parentId == 1 || parentId == 3) {
                    // 如果a的代理ID等于"*"
                    if ("*".equals(a.getAgentId())) {
                        // 跳过当前循环，继续下一次循环
                        continue;
                        // 如果a的代理ID等于"3"
                    } else if ("3".equals(a.getAgentId())) {
                        // 跳过当前循环，继续下一次循环
                        continue;
                    }
                }
                // 如果parentId等于3
                if (parentId == 3) {
                    // 如果campusId的字符串值不等于a的代理ID
                    if (!String.valueOf(campusId).equals(a.getAgentId())) {
                        // 跳过当前循环，继续下一次循环
                        continue;
                    }
                }
                // 如果a的ID不等于userId1
                if (!a.getId().equals(userId1)) {
                    // 将a添加到adminAdministrations列表中
                    adminAdministrations.add(a);
                }
                // 如果a的名称为空
                if (a.getName() == null) {
                    // 将a的名称设置为a的角色名称
                    a.setName(a.getRoleName());
                }
            }
            // 使用adminAdministrations列表创建一个PageInfo对象，并赋值给pageInfo变量
            PageInfo<AdminAdministraltion> pageInfo = new PageInfo<>(adminAdministrations);
            // 设置pageInfo的每页大小为pageSize
            pageInfo.setPageSize(pageSize);
            // 返回一个包含请求成功信息和pageInfo的MsgVo对象
            return new MsgVo(200, "请求成功", pageInfo);
            // 捕捉异常
        } catch (Exception e) {
            // 打印异常堆栈信息
            e.printStackTrace();
            // 返回一个表示请求处理失败的MsgVo对象
            return new MsgVo(500, "请求处理失败", null);
        }
    }

    @ApiOperation(value = "检查是否为管理员和总部")
    @UserLoginToken
    @GetMapping("/headquarters")
    public MsgVo headquarters(HttpServletRequest request) {
        // 初始化一个整型变量parentId，并赋值为0
        int parentId = 0;
        // 从请求中获取名为userId的属性，并赋值给变量userId
        Object userId = request.getAttribute("userId");
        // 调用adminAdministrationService的getsTheIdOfTheRole方法，将userId强转为Integer类型后作为参数传入，获取一个角色ID，并赋值给role变量
        Integer role = adminAdministrationService.getsTheIdOfTheRole((Integer) userId);
        // 调用getParentId方法，传入parentId和role参数，根据规则获取新的parentId值，并赋值给parentId变量
        parentId = getParentId(parentId, role);
        // 判断parentId是否等于0或者等于1，将判断结果赋值给布尔型变量isShow
        boolean isShow = parentId == 0 || parentId == 1;
        // 将键名为"isShow"，值为isShow的键值对创建一个不可变的Map对象，并赋值给map变量
        Map<String, Boolean> map = Collections.singletonMap("isShow", isShow);
        // 返回一个包含请求成功信息和map的MsgVo对象
        return new MsgVo(200, "请求成功", map);
    }

    private int getParentId(int parentId, Integer role) {
        // 定义一个整型数组validIds，包含有效的角色ID
        int[] validIds = {0, 1, 2, 3};
        // 遍历validIds数组，将每个元素依次赋值给变量val
        for (int val : validIds) {
            // 判断role是否和val相等
            if (role.equals(val)) {
                // 如果相等，将parentId赋值为role
                parentId = role;
                // 跳出循环，结束遍历
                break;
            } else {
                // 调用adminAdministrationService的getRoles方法，获取角色列表，并赋值给roles变量
                List<AgentIdrId> roles = adminAdministrationService.getRoles();
                // 调用RoleUtils的findParent方法，传入roles、role和validIds参数，根据规则获取新的parentId值，并赋值给parentId变量
                parentId = RoleUtils.findParent(roles, role, validIds);
            }
        }
        // 返回parentId的值作为方法的返回值
        return parentId;
    }

    @ApiOperation(value = "管理员角色权限查询")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("getRole")
    public MsgVo getRole(HttpServletRequest request) {
        // 从请求中获取名为userId的属性，并赋值给变量userId
        Object userId = request.getAttribute("userId");
        // 创建一个容量为50的HashMap对象，用于存储数据
        Map<String, Object> map = new HashMap<>(50);
        // 将键名为"role"，值为adminAdministrationService.getRole((Integer) userId)的键值对添加到map中
        map.put("role", adminAdministrationService.getRole((Integer) userId));
        // 返回一个包含成功信息和map的MsgVo对象
        return new MsgVo(MsgUtils.SUCCESS, map);
    }

    @ApiOperation(value = "管理员所属总理")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("/getAgentManagement")
    public ResponseEntity<MsgVo> getAgentManagement(HttpServletRequest request, Integer id) {
        // 初始化一个整型变量parentId，并赋值为0
        int parentId = 0;
        // 从请求中获取名为userId的属性，并赋值给变量userId
        Object userId = request.getAttribute("userId");
        // 调用adminAdministrationService的getsTheIdOfTheRole方法，将userId强转为Integer类型后作为参数传入，获取一个角色ID，并赋值给role变量
        Integer role = adminAdministrationService.getsTheIdOfTheRole((Integer) userId);
        // 调用getParentId方法，传入parentId和role参数，根据规则获取新的parentId值，并赋值给parentId变量
        parentId = getParentId(parentId, role);
        // 创建一个ArrayList对象adminAdministrations，用于存储AdminAdministration类型的数据
        List<AdminAdministraltion> adminAdministrations = new ArrayList<>();
        // 创建一个容量为50的HashMap对象，用于存储数据
        Map<String, Object> map = new HashMap<>(50);
        System.out.println(parentId+"11111111111111");
        if (parentId==2){
            List<Map<String, String>> aProxyRole = adminAdministrationService.agency((Integer) userId);
            map.put("order", aProxyRole);
            return ResponseEntity.ok(new MsgVo(200, "查询成功", map));
        }
        adminAdministrations = adminAdministrationService.getsAllUsersWithSpecifiedPermissions(1, (Integer) userId);
        map.put("order", adminAdministrations);
        // 返回一个包含成功信息和map的MsgVo对象
        return ResponseEntity.ok(new MsgVo(200, "查询成功", map));
    }

    @ApiOperation(value = "管理员所属代理")
    @Transactional
    @UserLoginToken
    @GetMapping("/administratorOwnedAgent")
    public ResponseEntity<MsgVo> administratorOwnedAgent(HttpServletRequest request, String id) {
        Object userId = request.getAttribute("userId");
        Integer role = adminAdministrationService.getsTheIdOfTheRole((Integer) userId);
        // 调用adminAdministrationService的getRoles方法，获取角色列表，并赋值给roles变量
        Map<String, Object> map = new HashMap<>(50);
        List<AdminAdministraltion> adminAdministrations = new ArrayList<>();
        // 初始化一个整型变量a，并赋值为0
        int a = 0;
        a = getParentId(a, role);
        if (id == null || "".equals(id)) {
            if (a == 3) {
                    List<Map<String, String>> aProxyRole = adminAdministrationService.getAProxyRole((Integer) userId);
                    map.put("Order", aProxyRole);
                    return ResponseEntity.ok(new MsgVo(200, "查询成功", map));
            }
        } else {
            if (a==0 || a==1){
                List<Map<String, String>> noUserperson = adminAdministrationService.getNoUserperson(Integer.valueOf(id), 1);
                List<Map<String, String>> all = adminAdministrationService.all();
                Set<Map<String, String>> proInId = new HashSet<>();
                proInId.addAll(noUserperson);
                proInId.addAll(all);
                map.put("Order", proInId);
                return ResponseEntity.ok(new MsgVo(200, "查询成功", map));
            }else {
                List<Map<String, String>> noUserperson = adminAdministrationService.getNoUserperson(Integer.valueOf(id),2);
                map.put("Order", noUserperson);
                return ResponseEntity.ok(new MsgVo(200, "查询成功", map));
            }
        }
        return ResponseEntity.ok(new MsgVo(403, "查询失败", null));
    }

    @ApiOperation(value = "管理员管理添加")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PostMapping("addAdminUser")
    public MsgVo addAdminUser(@Validated @RequestBody AdmininistraltionDto admininistraltionDto, BindingResult result, HttpServletRequest request) {
        // 声明一个布尔型变量userAdmin，并初始化为true
        Boolean userAdmin = true;
        // 声明一个整型变量parentId，并初始化为0
        int parentId = 0;
        // 从请求中获取名为userId的属性，并将其赋值给变量userId
        Object userId = request.getAttribute("userId");
        String theAgentId = adminAdministrationService.getTheAgentId((Integer) userId);
        // 如果结果中存在错误
        if (result.hasErrors()) {
            // 获取第一个错误信息
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            // 返回一个包含错误信息的MsgVo对象
            return new MsgVo(403, errorMessage, false);
        }

        // 调用adminAdministrationService的isAdminEmailMobile方法，传入administrationDto参数，获取一个AdminAdministration对象列表，并赋值给adminEmailMobile变量
        List<AdminAdministraltion> adminEmailMobile = adminAdministrationService.isAdminEmailMobile(admininistraltionDto);
        // 如果adminEmailMobile列表中的任意一个对象的用户名与administrationDto的用户名相等
        if (adminEmailMobile.stream().anyMatch(a -> a.getUsername().equals(admininistraltionDto.getUsername()))) {
            // 返回一个包含错误信息的MsgVo对象
            return new MsgVo(403, "用户名已存在", false);
            // 如果adminEmailMobile列表中的任意一个对象的手机号与administrationDto的手机号相等
        } else if (adminEmailMobile.stream().anyMatch(a -> a.getMobile().equals(admininistraltionDto.getMobile()))) {
            // 返回一个包含错误信息的MsgVo对象
            return new MsgVo(403, "手机号重复请重新输入", false);
            // 如果adminEmailMobile列表中的任意一个对象的邮箱与administrationDto的邮箱相等
        } else if (adminEmailMobile.stream().anyMatch(a -> a.getEmail().equals(admininistraltionDto.getEmail()))) {
            // 返回一个包含错误信息的MsgVo对象
            return new MsgVo(403, "邮箱重复请重新输入", false);
        }

        // 调用adminAdministrationService的getRoleId方法，传入administrationDto的角色名参数，获取一个Integer类型的角色ID，并赋值给roleId变量
        Integer roleId = adminAdministrationService.getRoleId(admininistraltionDto.getRoleName());
        // 调用getParentId方法，传入parentId和roleId参数，根据规则获取新的parentId值，并赋值给parentId变量
        parentId = getParentId(parentId, roleId);
        // 如果parentId等于0
        if (parentId == 0) {
            // 将administrationDto的名称设置为"**"
            admininistraltionDto.setName("**");
            // 将administrationDto的代理ID设置为"**"
            admininistraltionDto.setAgentId("**");
            // 如果parentId等于1
        } else if (parentId == 1) {
            // 将administrationDto的名称设置为"*"
            admininistraltionDto.setName("*");
            // 将administrationDto的代理ID设置为"*"
            admininistraltionDto.setAgentId("*");
        }

        try {
            // 打印输出parentId是否等于2
            System.out.println(parentId == 2);

            // 如果parentId等于2
            if (parentId == 3) {
                // 如果administrationDto的代理ID为空或者等于空字符串
                if (admininistraltionDto.getName() == null || Objects.equals(admininistraltionDto.getName(), "")) {
                    admininistraltionDto.setName(theAgentId);
                }
            }
            admininistraltionDto.setPassword(HashUtils.hash(admininistraltionDto.getPassword()));
            // 调用adminAdministrationService的isUserAdmin方法，传入administrationDto参数，判断用户是否为管理员，并将结果赋值给userAdmin变量
            userAdmin = adminAdministrationService.isUserAdmin(admininistraltionDto);

            // 如果userAdmin为true
            if (userAdmin) {
                // 调用adminAdministrationService的isUserRoleOrder方法，传入administrationDto的ID和角色名参数，执行相关操作
                adminAdministrationService.isUserRoleOrder(admininistraltionDto.getId(), admininistraltionDto.getRoleName());
                // 返回一个包含成功信息的MsgVo对象
                return new MsgVo(200, "添加成功", true);
            }
            // 添加返回值，确保在未满足条件时有返回结果
            // 捕捉异常
        } catch (Exception e) {
            // 在发生异常时进行事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // 返回一个表示失败的MsgVo对象
            return new MsgVo(MsgUtils.FAILED);
        }
        // 返回一个表示权限不足的MsgVo对象
        return new MsgVo(403, "权限不足", false);
    }

    @ApiOperation(value = "管理员管理修改状态")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @PutMapping("status")
    public MsgVo status(@RequestBody StatusVo status) {
        Boolean aBoolean = adminAdministrationService.UpdStatus(status.getId(), status.getStatus());
        return new MsgVo(200,"删除成功", aBoolean);
    }


    @ApiOperation(value = "销毁账号")
    @UserLoginToken
    @DeleteMapping ("/destroyAccount/{ids}")
    public MsgVo destroyAccount(@PathVariable Integer[] ids) {
        Boolean aBoolean = adminAdministrationService.destroyAccount(ids);
        return new MsgVo(200,"销毁成功", aBoolean);
    }

    @ApiOperation(value = "获取指定用户信息")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @GetMapping("/getIsAdmin")
    public MsgVo getIsAdmin(Integer userId) {
        // 调用adminAdministrationService的getAll方法，传入userId作为参数，获取特定用户的详细信息，并赋值给all变量
        AdminAdministraltion all = adminAdministrationService.getAll(userId);
        // 返回一个包含成功信息和all的MsgVo对象
        return new MsgVo(MsgUtils.SUCCESS, all);
    }

    @ApiOperation(value = "管理员管理修改")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @Transactional
    @UserLoginToken
    @PutMapping("/updIpAdmin")
    public ResponseEntity<MsgVo> updIpAdmin(@Validated @Valid @RequestBody UpdAdminDto updAdminDto, BindingResult result, HttpServletRequest request) {
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

    @ApiOperation(value = "下拉框权限")
    @UserLoginToken
    @GetMapping("verifyTheGroupLinkageOfAllStores")
    public ResponseEntity<MsgVo> verifyTheGroupLinkageOfAllStores(HttpServletRequest request) {
        String roleId = request.getParameter("id");
        int role = Integer.parseInt(roleId);
        Map<String, Boolean> map = new HashMap<>(50);
        if (role == 0 || role == 1) {
            map.put("key", true);
        } else {
            map.put("key", false);
        }

        return ResponseEntity.ok(new MsgVo(200, "请求成功", map));
    }
}


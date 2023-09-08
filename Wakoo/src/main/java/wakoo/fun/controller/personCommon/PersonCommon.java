package wakoo.fun.controller.personCommon;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.common.Log;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.log.Constants;
import wakoo.fun.pojo.Agent;
import wakoo.fun.pojo.PersonUser;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.service.OrdersService.OrdersService;
import wakoo.fun.service.PersonUserService.PersonUserService;
import wakoo.fun.utils.RoleUtils;
import wakoo.fun.vo.AgentIdrId;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.vo.PersonUserVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 普通用户 处理
 *
 * @author HASEE
 */
@EnableTransactionManagement
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "普通用户接口")
public class PersonCommon {
    @Resource
    private PersonUserService personUserService;
    @Resource
    private OrdersService ordersService;
    @Resource
    private AdminAdministrationService adminAdministrationService;

    @ApiOperation(value = "查询普通用户")
    @UserLoginToken
    @GetMapping("/queryingCommonUsers")
    public MsgVo queryingCommonUsers(HttpServletRequest request, String keyword, Integer pageSize, Integer pageNumber, Integer status) {
        Object userId = request.getAttribute("userId");
        int parentId = 0;
        List<PersonUser> regularUsers;
        Integer rId = ordersService.returnsTheParentId((Integer) userId);
        RoleUtils roleUtils = new RoleUtils();
        List<AgentIdrId> roles = adminAdministrationService.getRoles();
        parentId = roleUtils.getParentId(parentId, rId, roles);
        PageHelper.startPage(pageNumber, pageSize);
        if (parentId == 3) {
            regularUsers = personUserService.getRegularUsers(keyword, (Integer) userId, status, 3);
        } else {
            regularUsers = personUserService.getRegularUsers(keyword, (Integer) userId, status, 1);
        }
        PageInfo<PersonUser> pageInfo = new PageInfo<>(regularUsers);
        pageInfo.setPageSize(pageSize);
        return new MsgVo(200, "查询成功", pageInfo);
    }

    @ApiOperation(value = "多条件查询普通用户")
    @UserLoginToken
    @GetMapping("/multipleCriteriaAreUsedToQueryCommonUsers")
    public MsgVo multipleCriteriaAreUsedToQueryCommonUsers(HttpServletRequest request, Integer pageNumber, Integer pageSize, String classname, String iphone, String agentName, String sex, Integer age, Integer status) {
        Object userId = request.getAttribute("userId");
        List<PersonUser> regularUsers;
        int parentId = 0;
        Integer rId = ordersService.returnsTheParentId((Integer) userId);
        RoleUtils roleUtils = new RoleUtils();
        List<AgentIdrId> roles = adminAdministrationService.getRoles();
        parentId = roleUtils.getParentId(parentId, rId, roles);
        PageHelper.startPage(pageNumber, pageSize);
        if (parentId == 3) {
            regularUsers = personUserService.queryUsersBasedOnMultipleCriteria(classname, iphone, agentName, sex, age, (Integer) userId, 3, status);
        } else {
            regularUsers = personUserService.queryUsersBasedOnMultipleCriteria(classname, iphone, agentName, sex, age, (Integer) userId, 1, status);
        }
        PageInfo<PersonUser> pageInfo = new PageInfo<>(regularUsers);
        pageInfo.setPageSize(pageSize);
        return new MsgVo(200, "查询成功", pageInfo);
    }


    @ApiOperation(value = "查询是否为代理")
    @UserLoginToken
    @GetMapping("/queryWhetherTheAgentIsUsed")
    public MsgVo queryWhetherTheAgentIsUsed(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        Integer rId = ordersService.returnsTheParentId((Integer) userId);
        if (rId == 3) {
            return new MsgVo(200, "代理", true);
        } else {
            return new MsgVo(403, "no代理", false);
        }
    }

    @ApiOperation(value = "添加普通用户")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "用户页面-普通用户添加", type = Constants.INSERT, desc = "操作添加按钮")
    @PostMapping("/addCommonUser")
    public CompletableFuture<MsgVo> addCommonUser(HttpServletRequest request, @Validated @RequestBody PersonUser personUser, BindingResult result) {
        if (result.hasErrors()) {
            // 如果校验出错
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            // 获取第一个错误的默认消息
            return CompletableFuture.completedFuture(new MsgVo(403, errorMessage, false));
            // 返回包含错误消息的 ResponseEntity
        }
        PersonUser personUser1 = personUserService.queryByMobilePhoneNumber(personUser.getIphone());
        String thePhoneNumber = personUserService.getThePhoneNumber(personUser.getIphone());
        // 获取给定手机号对应的数据库中的手机号
        if (thePhoneNumber != null && !thePhoneNumber.equals(personUser.getIphone())) {
            // 如果数据库中存在不同的手机号，则手机号重复
            return CompletableFuture.completedFuture(new MsgVo(403, "手机号重复", false));
            // 返回手机号重复的 ResponseEntity
        } else if (thePhoneNumber != null && thePhoneNumber.equals(personUser.getIphone())) {
            // 如果数据库中存在相同的手机号
            Boolean aBoolean1 = personUserService.addAgentsAndHumanRelationships(personUser1.getId(), Integer.parseInt(personUser.getAgentName()));
            // 添加用户与代理商之间的关系
            return CompletableFuture.completedFuture(new MsgVo(200, "添加成功", aBoolean1));
            // 返回成功添加用户关系的 ResponseEntity
        }

        Object userId = request.getAttribute("userId");
        // 获取请求的用户ID
        if (personUser.getAgentName() == null) {
            // 如果代理商名称为空
            Agent personUserByUserIdAnd = personUserService.getPersonUserByUserIdAnd((Integer) userId);
            // 根据用户ID获取代理商对象
            personUser.setAgentName(String.valueOf(personUserByUserIdAnd.getId()));
            // 设置代理商名称为代理商ID
        }

        personUser.setPassword(personUser.getIphone().substring(personUser.getIphone().length() - 4));
        // 设置用户的密码为手机号的后4位
        Boolean aBoolean = personUserService.addCommonUser(personUser);
        // 添加普通用户
        if (aBoolean) {
            Boolean aBoolean1 = personUserService.addAgentsAndHumanRelationships(personUser.getId(), Integer.parseInt(personUser.getAgentName()));
            // 添加用户与代理商之间的关系
            if (aBoolean1) {
                return CompletableFuture.completedFuture(new MsgVo(200, "添加成功", true));
                // 返回成功添加用户关系的 ResponseEntity
            }
        }
        return CompletableFuture.completedFuture(new MsgVo(403, "添加失败", false));
        // 返回添加失败的 ResponseEntity
    }

    @ApiOperation(value = "添加回显")
    @UserLoginToken
    @GetMapping("/addEcho")
    public MsgVo addEcho(String iphone) {
        PersonUser personUser1 = personUserService.queryByMobilePhoneNumber(iphone);
        if (personUser1 != null) {
            return new MsgVo(200, "查询成功", personUser1);
        } else {
            return new MsgVo(200, "查询成功", null);
        }
    }

    @ApiOperation(value = "人员下拉框")
    @UserLoginToken
    @GetMapping("/personnelDropDownBox")
    public MsgVo personnelDropDownBox(HttpServletRequest request, String iphone) {
        Object userId = request.getAttribute("userId");
        int parentId = 0;
        RoleUtils roleUtils = new RoleUtils();
        Integer rId = ordersService.returnsTheParentId((Integer) userId);
        List<AgentIdrId> roles = adminAdministrationService.getRoles();
        parentId = roleUtils.getParentId(parentId, rId, roles);
        List<Agent> agents;
        if (parentId == 0 || parentId == 1) {
            agents = personUserService.acquireOtherThanPersonnel((Integer) userId, 1, iphone);
        } else {
            agents = personUserService.acquireOtherThanPersonnel((Integer) userId, parentId, iphone);
        }
        return new MsgVo(200, "查询成功", agents);
    }


    @ApiOperation(value = "修改回显")
    @UserLoginToken
    @GetMapping("/modifyTheCommandOutputForCommonUsers")
    public MsgVo modifyTheCommandOutputForCommonUsers(@RequestParam Integer id) {
        PersonUser personUser = personUserService.theCommandOutputIsModified(id);
        return new MsgVo(200, "查询成功", personUser);
    }

    @ApiOperation(value = "修改普通用户")
    @UserLoginToken
    @Log(modul = "用户页面-普通用户修改", type = Constants.UPDATE, desc = "操作修改按钮")
    @PutMapping("/modifyingCommonUser")
    public MsgVo modifyingCommonUser(@RequestBody PersonUser personUser, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return new MsgVo(403, errorMessage, false);
        }



        Boolean aBoolean = personUserService.modifyingCommonUser(personUser);
        if (aBoolean) {
            return new MsgVo(200, "修改成功", true);
        } else {
            return new MsgVo(403, "修改失败", true);
        }
    }

    @ApiOperation(value = "购买所属人")
    @UserLoginToken
    @GetMapping("/purchaser")
    public MsgVo purchaser(Integer id) {
        List<Agent> purchaser = personUserService.purchaser(id);
        Map<String, List<Agent>> map = new HashMap<>(50);
        map.put("purchaser", purchaser);
        return new MsgVo(200, "查询成功", map);
    }


    @ApiOperation(value = "购买所属课程")
    @UserLoginToken
    @GetMapping("/purchaserCourse")
    public MsgVo purchaserCourse(Integer id, Integer cid) {
        List<PersonUserVo> personUserVos = personUserService.inquireAboutTheOwnersCourse(id, cid);
        Map<String, List<PersonUserVo>> map = new HashMap<>(50);
        map.put("person", personUserVos);
        return new MsgVo(200, "查询成功", map);
    }

    @ApiOperation(value = "已有课程")
    @UserLoginToken
    @GetMapping("/existingCourse")
    public MsgVo existingCourse(Integer id) {
        List<PersonUserVo> personUserVos = personUserService.accessExistingCourses(id);
        Map<String, List<PersonUserVo>> map = new HashMap<>(50);
        map.put("person", personUserVos);
        return new MsgVo(200, "查询成功", map);
    }

    @ApiOperation(value = "购买课程")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "用户页面-购买课程", type = Constants.INSERT, desc = "操作购买按钮")
    @PostMapping("/purchaseCourse")
    public MsgVo purchaseCourse(@RequestBody PersonUserVo personUserVo, HttpServletRequest request) {
        Boolean aBoolean = personUserService.addPurchaseCourse(personUserVo, request);
        PersonUser personUser = personUserService.theCommandOutputIsModified(personUserVo.getId());
        Object orderId = request.getAttribute("orderId");
        Boolean aBoolean1 = personUserService.addAudit(personUser.getId(),
                personUser.getSex(), personUser.getAge(), personUser.getIphone(), personUserVo.getAgentId(), Integer.parseInt(personUserVo.getSubclassName()), (Integer) orderId);
        if (aBoolean1 && aBoolean) {
            return new MsgVo(200, "购买成功", true);
        } else {
            return new MsgVo(403, "购买失败", false);
        }
    }

    @ApiOperation(value = "用户软删除")
    @UserLoginToken
    @SuppressWarnings("unchecked")
    @DeleteMapping("/userSoftDelete")
    public MsgVo userSoftDelete(@RequestBody Map<String, Object> requestBody) {

        List<Integer> idsList = (List<Integer>) requestBody.get("ids");
        Integer status = (Integer) requestBody.get("status");
        Integer[] ids = idsList.toArray(new Integer[0]);
        Boolean success = false;
        success = personUserService.softDeleteUser(ids, status);
        if (success) {
            return new MsgVo(200, "操作成功", true);
        } else {
            return new MsgVo(403, "操作失败", false);
        }
    }


}


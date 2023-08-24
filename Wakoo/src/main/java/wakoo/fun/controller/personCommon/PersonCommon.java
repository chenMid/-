package wakoo.fun.controller.personCommon;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.config.UserLoginToken;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<MsgVo> queryingCommonUsers(HttpServletRequest request, String keyword, Integer pageSize, Integer pageNumber) {
        Object userId = request.getAttribute("userId");
        PageHelper.startPage(pageNumber, pageSize);
        List<PersonUser> regularUsers = personUserService.getRegularUsers(keyword, (Integer) userId);
        PageInfo<PersonUser> pageInfo = new PageInfo<>(regularUsers);
        return ResponseEntity.ok(new MsgVo(200, "查询成功", pageInfo));
    }

    @ApiOperation(value = "查询是否为代理")
    @UserLoginToken
    @GetMapping("/queryWhetherTheAgentIsUsed")
    public ResponseEntity<MsgVo> queryWhetherTheAgentIsUsed(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        Integer rId = ordersService.returnsTheParentId((Integer) userId);
        if (rId == 3) {
            return ResponseEntity.ok(new MsgVo(200, "代理", true));
        } else {
            return ResponseEntity.ok(new MsgVo(200, "no代理", false));
        }
    }

    @ApiOperation(value = "添加普通用户")
    @UserLoginToken
    @Transactional
    @PostMapping("/addCommonUser")
    public ResponseEntity<MsgVo> addCommonUser(HttpServletRequest request, @Validated @RequestBody PersonUser personUser, BindingResult result) {
        if (result.hasErrors()) {
            // 如果校验出错
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            // 获取第一个错误的默认消息
            return ResponseEntity.ok(new MsgVo(403, errorMessage, false));
            // 返回包含错误消息的 ResponseEntity
        }

        try {
            String thePhoneNumber = personUserService.getThePhoneNumber(personUser.getIphone());
            // 获取给定手机号对应的数据库中的手机号
            if (thePhoneNumber != null && !thePhoneNumber.equals(personUser.getIphone())) {
                // 如果数据库中存在不同的手机号，则手机号重复
                return ResponseEntity.ok(new MsgVo(200, "手机号重复", false));
                // 返回手机号重复的 ResponseEntity
            } else if (thePhoneNumber != null && thePhoneNumber.equals(personUser.getIphone())) {
                // 如果数据库中存在相同的手机号
                Boolean aBoolean1 = personUserService.addAgentsAndHumanRelationships(personUser.getId(), Integer.parseInt(personUser.getAgentName()));
                // 添加用户与代理商之间的关系
                return ResponseEntity.ok(new MsgVo(200, "添加成功", aBoolean1));
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
                    return ResponseEntity.ok(new MsgVo(200, "添加成功", true));
                    // 返回成功添加用户关系的 ResponseEntity
                }
            }
            return ResponseEntity.ok(new MsgVo(403, "添加失败", false));
            // 返回添加失败的 ResponseEntity
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // 事务回滚
            return ResponseEntity.ok(new MsgVo(500, "添加异常", false));
            // 返回添加异常的 ResponseEntity
        }
    }

    @ApiOperation(value = "添加回显")
    @UserLoginToken
    @GetMapping("/addEcho")
    public ResponseEntity<MsgVo> addEcho(String iphone) {
        PersonUser personUser1 = personUserService.queryByMobilePhoneNumber(iphone);
        if (personUser1 != null) {
            return ResponseEntity.ok(new MsgVo(200, "查询成功", personUser1));
        } else {
            return ResponseEntity.ok(new MsgVo(200, "查询成功", null));
        }
    }

    @ApiOperation(value = "人员下拉框")
    @UserLoginToken
    @GetMapping("/personnelDropDownBox")
    public ResponseEntity<MsgVo> personnelDropDownBox(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        int parentId=0;
        RoleUtils roleUtils=new RoleUtils();
        Integer rId = ordersService.returnsTheParentId((Integer) userId);
        List<AgentIdrId> roles = adminAdministrationService.getRoles();
        parentId = roleUtils.getParentId(parentId, rId,roles);
        List<Agent> agents;
        if (parentId==0 || parentId==1){
            agents = personUserService.acquireOtherThanPersonnel((Integer) userId, 1);
        }else {
            agents=personUserService.acquireOtherThanPersonnel((Integer) userId, parentId);
        }
        return ResponseEntity.ok(new MsgVo(200, "查询成功", agents));
    }


    @ApiOperation(value = "修改回显")
    @UserLoginToken
    @GetMapping("/modifyTheCommandOutputForCommonUsers")
    public ResponseEntity<MsgVo> modifyTheCommandOutputForCommonUsers(@RequestParam Integer id) {
        PersonUser personUser = personUserService.theCommandOutputIsModified(id);
        return ResponseEntity.ok(new MsgVo(200, "查询成功", personUser));
    }

    @ApiOperation(value = "修改普通用户")
    @UserLoginToken
    @PutMapping("/modifyingCommonUser")
    public ResponseEntity<MsgVo> modifyingCommonUser(@RequestBody PersonUser personUser, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.ok(new MsgVo(403, errorMessage, false));
        }
        Boolean aBoolean = personUserService.modifyingCommonUser(personUser);
        if (aBoolean) {
            return ResponseEntity.ok(new MsgVo(200, "修改成功", true));
        } else {
            return ResponseEntity.ok(new MsgVo(403, "修改失败", true));
        }
    }

    @ApiOperation(value = "购买所属人")
    @UserLoginToken
    @GetMapping("/purchaser")
    public ResponseEntity<MsgVo> purchaser(Integer id) {
        List<Agent> purchaser = personUserService.purchaser(id);
        Map<String,List<Agent>> map=new HashMap<>(50);
        map.put("purchaser", purchaser);
        return ResponseEntity.ok(new MsgVo(200, "查询成功", map));
    }


    @ApiOperation(value = "购买所属课程")
    @UserLoginToken
    @GetMapping("/purchaserCourse")
    public ResponseEntity<MsgVo> purchaserCourse(Integer id) {
        List<PersonUserVo> personUserVos = personUserService.inquireAboutTheOwnersCourse(id);
        Map<String,List<PersonUserVo>> map=new HashMap<>(50);
        map.put("person", personUserVos);
        return ResponseEntity.ok(new MsgVo(200,"查询成功",map));
    }

    @ApiOperation(value = "已有课程")
    @UserLoginToken
    @GetMapping("/existingCourse")
    public ResponseEntity<MsgVo> existingCourse(Integer id) {
        List<PersonUserVo> personUserVos = personUserService.accessExistingCourses(id);
        Map<String,List<PersonUserVo>> map=new HashMap<>(50);
        map.put("person", personUserVos);
        return ResponseEntity.ok(new MsgVo(200,"查询成功",map));
    }



    @ApiOperation(value = "购买课程")
    @UserLoginToken
    @PostMapping("/purchaseCourse")
    public ResponseEntity<MsgVo> purchaseCourse(@RequestBody PersonUserVo personUserVo) {
        Boolean aBoolean = personUserService.addPurchaseCourse(personUserVo);
        if (aBoolean){
            return ResponseEntity.ok(new MsgVo(200,"购买成功",true));
        }else {
            return ResponseEntity.ok(new MsgVo(500,"购买失败",false));
        }
    }

    @ApiOperation(value = "用户详细信息")
    @UserLoginToken
    @PostMapping("/userDetails")
    public ResponseEntity<MsgVo> userDetails(@RequestBody PersonUser personUser) {
        return null;
    }


}


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
import wakoo.fun.service.OrdersService.OrdersService;
import wakoo.fun.service.PersonUserService.PersonUserService;
import wakoo.fun.vo.MsgVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 普通用户 处理
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
    public ResponseEntity<MsgVo> queryWhetherTheAgentIsUsed(HttpServletRequest request){
        Object userId = request.getAttribute("userId");
        Integer rId = ordersService.returnsTheParentId((Integer) userId);
        if (rId==3){
            return ResponseEntity.ok(new MsgVo(200, "代理", true));
        }else {
            return ResponseEntity.ok(new MsgVo(200, "no代理", false));
        }
    }

    @ApiOperation(value = "添加普通用户")
    @UserLoginToken
    @Transactional
    @PostMapping("/addCommonUser")
    public ResponseEntity<MsgVo> addCommonUser(HttpServletRequest request,@Validated @RequestBody PersonUser personUser, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.ok(new MsgVo(403, errorMessage, false));
        }
        try {

            String thePhoneNumber = personUserService.getThePhoneNumber(personUser.getIphone());
            if (thePhoneNumber != null && !thePhoneNumber.equals(personUser.getIphone())){
                return ResponseEntity.ok(new MsgVo(200, "手机号重复", false));
            }else if (thePhoneNumber != null &&thePhoneNumber.equals(personUser.getIphone())){
                Boolean aBoolean1 = personUserService.addAgentsAndHumanRelationships(personUser.getId(),Integer.parseInt(personUser.getAgentName()));
                return ResponseEntity.ok(new MsgVo(200, "添加成功", aBoolean1));
            }
            Object userId = request.getAttribute("userId");
            if (personUser.getAgentName()==null){
                Agent personUserByUserIdAnd = personUserService.getPersonUserByUserIdAnd((Integer) userId);
                personUser.setAgentName(String.valueOf(personUserByUserIdAnd.getId()));
            }
            personUser.setPassword(personUser.getIphone().substring(personUser.getIphone().length()-4));
            Boolean aBoolean = personUserService.addCommonUser(personUser);
            if (aBoolean){
                Boolean aBoolean1 = personUserService.addAgentsAndHumanRelationships(personUser.getId(),Integer.parseInt(personUser.getAgentName()));
                if (aBoolean1){
                    return ResponseEntity.ok(new MsgVo(200, "添加成功", true));
                }
            }
            return ResponseEntity.ok(new MsgVo(403, "添加失败", false));
        }catch (Exception e){
             e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.ok(new MsgVo(500, "添加异常", false));
        }
    }
    @ApiOperation(value = "添加回显")
    @UserLoginToken
    @GetMapping("/addEcho")
    public ResponseEntity<MsgVo> addEcho(String iphone){
        PersonUser personUser1 = personUserService.queryByMobilePhoneNumber(iphone);
        if (personUser1!=null){
            return ResponseEntity.ok(new MsgVo(200, "查询成功", personUser1));
        }else {
            return ResponseEntity.ok(new MsgVo(200, "查询成功", null));
        }
    }

    @ApiOperation(value = "人员下拉框")
    @UserLoginToken
    @GetMapping("/personnelDropDownBox")
    public ResponseEntity<MsgVo> personnelDropDownBox(String iphone,HttpServletRequest request){
        Object userId = request.getAttribute("userId");
        List<Agent> agents = personUserService.acquireOtherThanPersonnel((Integer) userId, iphone);
        return ResponseEntity.ok(new MsgVo(200, "查询成功", agents));
    }


    @ApiOperation(value = "修改回显")
    @UserLoginToken
    @GetMapping("/modifyTheCommandOutputForCommonUsers")    public ResponseEntity<MsgVo> modifyTheCommandOutputForCommonUsers(@RequestParam Integer id){
        PersonUser personUser = personUserService.theCommandOutputIsModified(id);
        return ResponseEntity.ok(new MsgVo(200, "查询成功", personUser));
    }
    @ApiOperation(value = "修改普通用户")
    @UserLoginToken
    @PutMapping("/modifyingCommonUser")
    public ResponseEntity<MsgVo> modifyingCommonUser(@RequestBody PersonUser personUser, BindingResult result){
        if (result.hasErrors()){
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.ok(new MsgVo(403,errorMessage,false));
        }
        Boolean aBoolean = personUserService.modifyingCommonUser(personUser);
        if (aBoolean){
            return ResponseEntity.ok(new MsgVo(200, "修改成功", true));
        }else {
            return ResponseEntity.ok(new MsgVo(403, "修改失败", true));
        }
    }
}

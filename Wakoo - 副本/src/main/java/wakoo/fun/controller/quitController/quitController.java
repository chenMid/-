package wakoo.fun.controller.quitController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.common.Log;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.log.Constants;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.service.FaAdminService;
import wakoo.fun.utils.TokenUtils;
import wakoo.fun.vo.FadminVo;
import wakoo.fun.vo.MsgVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**退出
 * @author HASEE
 */
@EnableTransactionManagement
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "退出")
public class quitController {
    @Resource
    private StringRedisTemplate stringRedisTemplate ;
    @Resource
    private AdminAdministrationService adminAdministrationService;
    @Resource
    private FaAdminService faAdminService;

    @ApiOperation(value = "退出")
    @UserLoginToken
    @GetMapping("/logOut")
    public MsgVo logOut(HttpServletRequest request){
        //获取token
        String token = request.getHeader("token");
        //解析token
        Object userInfo = TokenUtils.getUserInfo(token,"userId");
        assert userInfo != null;
        //删除redis中的token
        Boolean delete = stringRedisTemplate.delete(userInfo.toString());
        return new MsgVo(200,"退出成功",delete);
    }

    @ApiOperation(value = "查询个人信息")
    @UserLoginToken
    @GetMapping("/queryPersonalInformation")
    public MsgVo queryPersonalInformation(HttpServletRequest request){
        Object userId = request.getAttribute("userId");
        FadminVo fadminVo = adminAdministrationService.personalInformation((Integer) userId);
        return new MsgVo(200, "查询个人信息成功", fadminVo);
    }

    @ApiOperation(value = "修改个人信息")
    @UserLoginToken
    @Log(modul = "个人信息-修改个人信息", type = Constants.SPECIAL, desc = "操作修改信息")
    @PutMapping("/modifyPersonalInformation")
    public MsgVo modifyPersonalInformation(HttpServletRequest request,@RequestBody FadminVo fadminVo) {
        Object userId = request.getAttribute("userId");
        fadminVo.setId((Integer) userId);
        if (fadminVo.getPassword()==null || Objects.equals(fadminVo.getPassword(), "")){
            String password = faAdminService.getPassword(fadminVo);
            fadminVo.setPassword(password);
        }
        Boolean aBoolean = faAdminService.modifyPersonalInformation(fadminVo);
        if (aBoolean){
            return new MsgVo(200,"修改成功",true);
        }else {
            return new MsgVo(403,"修改失败",false);
        }
    }
}

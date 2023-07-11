package wakoo.fun.controller.FaAdminController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.controller.CaptchaController.getKaptchaImage;
import wakoo.fun.dto.User;
import wakoo.fun.pojo.FaAdminLogin;
import wakoo.fun.Vo.MsgVo;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.service.FaAdminService;
import wakoo.fun.utils.TokenUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Admin")
public class FaAdminController {

    @Resource
    private getKaptchaImage asd;
    @Resource
    private FaAdminService faAdminService;

    @ApiOperation(value = "登录")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @PostMapping("/login")
    public MsgVo login(@RequestBody User user, HttpServletRequest request) throws Exception {
        try {
            String captchaSession = asd.getCaptchaSession(request);
            System.out.println(captchaSession);
            if (!user.getCaptchaImage().equals(captchaSession)) {
                return new MsgVo(403, "验证码错误,请重新输入", null);
            }
        } catch (NullPointerException e) {
            return new MsgVo(500, "请携带参数", null);
        }
        MsgVo msg = null;
        List<FaAdmin> faAdmins = faAdminService.faAdmin(user.getUserName());
        if (faAdmins.size() != 0) {
            if (!faAdmins.get(0).getPassword().equals(user.getPassword())) {
                msg = new MsgVo(403, "密码错误", null);
            } else {
                /*登录成功*/
                String token = TokenUtils.token(faAdmins.get(0).getUserName(), faAdmins.get(0).getPassword(), faAdmins.get(0).getId());
                Boolean aBoolean = faAdminService.UpdToken(token, user.getUserName());
                if (aBoolean) {
                    FaAdminLogin faAdmin = faAdminService.UserNameFaAdmin(user.getUserName());
                    FaAdminLogin faAdminLogins = faAdminService.ListFadmin(user.getUserName());
                    msg = new MsgVo(200, "登录成功", faAdminLogins);
                }
            }
        } else {
            msg = new MsgVo(403, "用户名错误", null);
        }
        return msg;
    }


}

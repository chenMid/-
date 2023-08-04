package wakoo.fun.controller.faAdminController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.controller.kaptchaController.getKaptchaImage;
import wakoo.fun.dto.User;
import wakoo.fun.pojo.FaAdminLogin;
import wakoo.fun.vo.MsgVo;
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
    public MsgVo login(@RequestBody User user, HttpServletRequest request) {
        try {
            String captchaSession = asd.getCaptchaSession(request);
            System.out.println(captchaSession);
            if (!captchaSession.equalsIgnoreCase(user.getCaptchaImage())) {
                return new MsgVo(403, "验证码错误，请重新输入", null);
            }

            List<FaAdmin> faAdmins = faAdminService.faAdmin(user.getUserName());
            if (!faAdmins.isEmpty()) {
                if (!faAdmins.get(0).getPassword().equals(user.getPassword())) {
                    return new MsgVo(403, "密码错误", null);
                } else {
                    /* 登录成功 */
                    String token = TokenUtils.token(faAdmins.get(0).getUserName(), faAdmins.get(0).getPassword(), faAdmins.get(0).getId());
                    Boolean aBoolean = faAdminService.UpdToken(token, user.getUserName());
                    if (aBoolean) {
                        FaAdminLogin faAdmin = faAdminService.UserNameFaAdmin(user.getUserName());
                        FaAdminLogin faAdminLogins = faAdminService.ListFadmin(user.getUserName());
                        return new MsgVo(200, "登录成功", faAdminLogins);
                    }
                }
            } else {
                return new MsgVo(403, "用户名错误", null);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return new MsgVo(500, "请携带参数", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgVo(500, "请求处理失败", null);
        }
        return null;
    }


}

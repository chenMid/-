package wakoo.fun.controller.faAdminController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.controller.kaptchaController.getKaptchaImage;
import wakoo.fun.dto.User;
import wakoo.fun.pojo.FaAdminLogin;
import wakoo.fun.utils.HashUtils;
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
            // 调用asd的getCaptchaSession方法，获取验证码的session信息，并赋值给captchaSession变量
            String captchaSession = asd.getCaptchaSession(request);
            // 打印输出captchaSession的值
            System.out.println(captchaSession);
            // 判断captchaSession和用户传入的验证码是否相等，如果不相等
            if (!captchaSession.equalsIgnoreCase(user.getCaptchaImage())) {
                // 返回一个带有403状态码、错误提示信息和空数据的MsgVo对象
                return new MsgVo(403, "验证码错误，请重新输入", null);
            }

            // 调用faAdminService的faAdmin方法，传入用户名作为参数，查询用户信息，并赋值给faAdmins变量
            List<FaAdmin> faAdmins = faAdminService.faAdmin(user.getUserName());
            // 判断faAdmins列表是否为空
            if (!faAdmins.isEmpty()) {
                // 判断faAdmins列表中第一个元素的密码与用户传入的密码是否相等，如果不相等
                if (!faAdmins.get(0).getPassword().equals(user.getPassword())) {
                    // 返回一个带有403状态码、错误提示信息和空数据的MsgVo对象
                    return new MsgVo(403, "密码错误", null);
                } else {
                    /* 登录成功 */
                    // 调用TokenUtils的token方法，传入用户名、密码和用户ID，生成登录凭证token
                    String token = TokenUtils.token(faAdmins.get(0).getUserName(), faAdmins.get(0).getPassword(), faAdmins.get(0).getId());
                    // 调用faAdminService的UpdToken方法，更新用户的登录凭证token，并返回更新结果

                    Boolean aBoolean = faAdminService.UpdToken(token, user.getUserName());
                    // 判断更新结果是否为真
                    if (aBoolean) {
                        // 调用faAdminService的UserNameFaAdmin方法，传入用户名作为参数，获取用户的登录信息，并赋值给faAdmin变量
                        FaAdminLogin faAdmin = faAdminService.UserNameFaAdmin(user.getUserName());
                        // 调用faAdminService的List admin方法，传入用户名作为参数，获取用户的其他信息，并赋值给faAdminLogins变量
                        FaAdminLogin faAdminLogins = faAdminService.ListFadmin(user.getUserName());
                        // 返回一个带有200状态码、成功提示信息和faAdminLogins数据的MsgVo对象
                        return new MsgVo(200, "登录成功", faAdminLogins);
                    }
                }
            } else {
                // 返回一个带有403状态码、错误提示信息和空数据的MsgVo对象
                return new MsgVo(403, "用户名错误", null);
            }
        } catch (NullPointerException e) {
            // 打印异常跟踪栈信息
            e.printStackTrace();
            // 返回一个带有500状态码、错误提示信息和空数据的MsgVo对象
            return new MsgVo(500, "请携带参数", null);
        } catch (Exception e) {
            // 打印异常跟踪栈信息
            e.printStackTrace();
            // 返回一个带有500状态码、错误提示信息和空数据的MsgVo对象
            return new MsgVo(500, "请求处理失败", null);
        }
        // 返回空对象（实际上不会被执行到，只是为了保证代码的编译通过）
        return null;
    }
}

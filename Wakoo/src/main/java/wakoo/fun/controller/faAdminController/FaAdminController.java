package wakoo.fun.controller.faAdminController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Constants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.common.Log;
import wakoo.fun.config.PassToken;
import wakoo.fun.controller.kaptchaController.verifyCode;
import wakoo.fun.dto.User;
import wakoo.fun.pojo.FaAdminLogin;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.service.FaAdminService;
import wakoo.fun.utils.TokenUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author HASEE
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Admin")
public class FaAdminController {

    @Resource
    private FaAdminService faAdminService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "登录")
    @Log(modul = "登录", desc = "登录")
    @RequestMapping("/login")
    public MsgVo login(@RequestBody User user, HttpServletRequest request) throws JsonProcessingException {
        System.out.println(user.getKey());
        String s = stringRedisTemplate.opsForValue().get(user.getKey());
        // 判断captchaSession和用户传入的验证码是否相等，如果不相等
        assert s != null;
        if (!s.equalsIgnoreCase(user.getCaptchaImage())) {
                // 返回一个带有403状态码、错误提示信息和空数据的MsgVo对象
                return new MsgVo(403, "验证码错误，请重新输入", null);
            }
            // 调用faAdminService的faAdmin方法，传入用户名作为参数，查询用户信息，并赋值给faAdmins变量
            List<FaAdmin> faAdmins = faAdminService.faAdmin(user.getUserName());
            if ("0".equals(faAdmins.get(0).getStatus())){
                return new MsgVo(403, "这个用户处于禁用状态，请联系管理员", null);
            }
            // 判断faAdmins列表是否为空
            if (!faAdmins.isEmpty()) {
                // 判断faAdmins列表中第一个元素的密码与用户传入的密码是否相等，如果不相等
                if (!faAdmins.get(0).getPassword().equals(user.getPassword())) {
                    // 返回一个带有403状态码、错误提示信息和空数据的MsgVo对象
                    return new MsgVo(403, "密码错误", null);
                } else {
                    /* 登录成功 */
                    stringRedisTemplate.delete(user.getKey());
                    request.setAttribute("userName", user.getUserName());
                    // 调用TokenUtils的token方法，传入用户名、密码和用户ID，生成登录凭证token
                    String token = TokenUtils.token(faAdmins.get(0).getUserName(), faAdmins.get(0).getPassword(), faAdmins.get(0).getId());
                    JSONObject tokenJson = new JSONObject();
                    tokenJson.put("token", token);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonString = objectMapper.writeValueAsString(tokenJson);
                    assert token != null;
                    stringRedisTemplate.opsForValue().set(String.valueOf(faAdmins.get(0).getId()), jsonString, 7, TimeUnit.DAYS);
                    // 调用faAdminService的UpdToken方法，更新用户的登录凭证token，并返回更新结果
                    Boolean aBoolean = faAdminService.UpdToken(token, user.getUserName());
                    // 判断更新结果是否为真
                    if (aBoolean) {
                        // 调用faAdminService的UserNameFaAdmin方法，传入用户名作为参数，获取用户的登录信息，并赋值给faAdmin变量
                        faAdminService.UserNameFaAdmin(user.getUserName());
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
            return null;
    }
}

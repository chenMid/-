package wakoo.fun.config;

import com.auth0.jwt.exceptions.JWTDecodeException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.service.FaAdminService;
import wakoo.fun.utils.TokenUtils;
import wakoo.fun.vo.MsgVo;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author HASEE
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 目标方法执行前
     * 该方法在控制器处理请求方法前执行，其返回值表示是否中断后续操作
     * 返回 true 表示继续向下执行，返回 false 表示中断后续操作
     *
     * @return
     */
    @Resource
    private FaAdminService faAdminService;
    @Resource
    private StringRedisTemplate stringRedisTemplate ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        response.setCharacterEncoding("UTF-8");
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //检查方法是否有passtoken注解，有则跳过认证，直接通过
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 user id
                try {
                    Object obj = TokenUtils.getUserInfo(token, "userId");
                    request.setAttribute("userId",obj);
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("token不正确，请不要通过非法手段创建token");
                }
                //查询数据库，看看是否存在此用户，方法要自己写
                Object obj = TokenUtils.getUserInfo(token, "username");
                List<FaAdmin> userName = faAdminService.faAdmin((String) obj);
                if (userName == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }

                // 验证 token
                Object userInfo = TokenUtils.getUserInfo(token, "userId");
                assert userInfo != null;
                Boolean exists = stringRedisTemplate.hasKey(userInfo.toString());
                if (Boolean.TRUE.equals(exists)){
                    String s = stringRedisTemplate.opsForValue().get(userInfo.toString());
                    JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
                    String tokens = jsonObject.get("token").getAsString();
                    if (!tokens.equals(token)){
                        MsgVo msgVo = new MsgVo(401, "\u8D26\u53F7\u5DF2\u5728\u522B\u5904\u767B\u5F55\uFF0C\u8BF7\u91CD\u65B0\u767B\u5F55\uFF01", false);
                        Gson gson = new Gson();
                        String json = gson.toJson(msgVo);
                        response.getWriter().write(json);
                        return false;
                    }
                }
                if (Boolean.TRUE.equals(exists)) {
                    // Token 仍然有效，判断过期时间
                    boolean isExpired = false;
                    Long ttlInSeconds = stringRedisTemplate.getExpire(userInfo.toString(), TimeUnit.SECONDS);
                    if (ttlInSeconds != null) {
                        if (ttlInSeconds < 0) {
                            // Token 已过期
                            isExpired = true;
                        } else if (ttlInSeconds <= 300) {
                            // 剩余过期时间小于等于1800秒（30分钟），延长过期时间为30分钟
                            stringRedisTemplate.expire(userInfo.toString(), 1800, TimeUnit.SECONDS);
                        }
                    }

                    if (isExpired) {
                        // Token 已过期，执行自动退出操作
                        MsgVo msgVo = new MsgVo(401, "\u8EAB\u4EFD\u8FC7\u671F\uFF0C\u8BF7\u91CD\u65B0\u767B\u5F55\uFF01", false);
                        // 将 MsgVo 对象转换为 JSON 格式的字符串
                        Gson gson = new Gson();
                        String json = gson.toJson(msgVo);
                        response.getWriter().write(json);
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    // Token 不存在或已被删除，执行自动退出操作
                    response.setCharacterEncoding("UTF-8");
                    MsgVo msgVo = new MsgVo(401, "\u8EAB\u4EFD\u8FC7\u671F\uFF0C\u8BF7\u91CD\u65B0\u767B\u5F55\uFF01", false);
                    // 将 MsgVo 对象转换为 JSON 格式的字符串
                    Gson gson = new Gson();
                    String json = gson.toJson(msgVo);
                    response.getWriter().write(json);
                    return false;
                }
            }
        }
        throw new RuntimeException("没有权限注解一律不通过");
    }


    /**
     * 目标方法执行后
     * 该方法在控制器处理请求方法调用之后、解析视图之前执行
     * 可以通过此方法对请求域中的模型和视图做进一步修改
     */
    @Override
    public void postHandle (HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {

    }
    /**
     * 页面渲染后
     * 该方法在视图渲染结束后执行
     * 可以通过此方法实现资源清理、记录日志信息等工作
     */
    @Override
    public void afterCompletion (HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) throws Exception {
    }

}
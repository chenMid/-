package wakoo.fun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wakoo.fun.config.LoginInterceptor;

import javax.annotation.Resource;


import org.springframework.context.annotation.Configuration;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Resource
    private LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自己的拦截器,并设置拦截的请求路径
        //addPathPatterns为拦截此请求路径的请求
        //excludePathPatterns为不拦截此路径的请求
        registry.addInterceptor(loginInterceptor).addPathPatterns("/api/*").excludePathPatterns("/api/Captcha")
                .excludePathPatterns("/api/uploadAvatar").excludePathPatterns("/api/login");
    }
}
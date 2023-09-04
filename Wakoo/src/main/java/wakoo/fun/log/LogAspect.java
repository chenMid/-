package wakoo.fun.log;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import wakoo.fun.common.Log;
import wakoo.fun.dao.SysLogDao;
import wakoo.fun.dto.AdminAdministraltion;
import wakoo.fun.pojo.FaAdminLogin;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.service.FaAdminService;
import wakoo.fun.utils.IPUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author HASEE
 */
@Aspect
@Component
public class LogAspect {

    /**
     * 操作版本号
     * 项目启动时从命令行传入，例如：java -jar xxx.war --version=201902
     */
    @Value("${version}")
    private String version;

    /**
     * 统计请求的处理时间
     */
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Resource
    private SysLogDao sysLogService;

    @Resource
    private AdminAdministrationService adminAdministrationService;

    @Resource
    private FaAdminService faAdminService;

    @Pointcut("@annotation(wakoo.fun.common.Log)")
    public void logPoinCut() {
    }

    @Pointcut("execution(* wakoo.fun.controller..*.*(..))")
    public void exceptionLogPoinCut() {
    }

    @Before("logPoinCut()")
    public void doBefore() {
        // 接收到请求，记录请求开始时间
        startTime.set(System.currentTimeMillis());
    }


    @AfterReturning(value = "logPoinCut()", returning = "keys")
    public void doAfterReturning(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        LogInfo logInfo = LogInfo.builder().build();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            // 获取切入点所在的方法
            Method method = signature.getMethod();

            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();

            // 获取操作
            Log log = method.getAnnotation(Log.class);
            if (Objects.nonNull(log)) {
                logInfo.setModule(log.modul());
                logInfo.setType(log.type());
                logInfo.setMessage(log.desc());
            }
            Object userId = request.getAttribute("userId");
            AdminAdministraltion all = adminAdministrationService.getAll((Integer) userId);
            Object userName = request.getAttribute("userName");
            logInfo.setId(UUID.randomUUID().toString());
            logInfo.setMethod(className + "." + method.getName());
            if ("SELECT".equals(logInfo.getType())) {
                logInfo.setReqParam(JSON.toJSONString(converMapNo(request.getParameterMap())));
            } else {
                if ("SPECIAL".equals(logInfo.getType())){
                    logInfo.setReqParam(JSON.toJSONString(converMapNo(request.getParameterMap())));
                }else {
                    logInfo.setReqParam(JSON.toJSONString(converMap(joinPoint)));
                }
            }
            logInfo.setResParam(JSON.toJSONString(keys));

            if (userId != null) {
                logInfo.setUserId(String.valueOf(userId));
                logInfo.setUserName(all.getUsername());
            } else {
                FaAdminLogin faAdminLogin = faAdminService.ListFadmin((String) userName);
                if (faAdminLogin==null){
                    logInfo.setUserId(String.valueOf("null"));
                }else {
                    logInfo.setUserId(String.valueOf(faAdminLogin.getId()));
                }
                logInfo.setUserName((String) userName);
            }
            logInfo.setIp(IPUtils.getIpAddress(request));
            logInfo.setUri(request.getRequestURI());
            logInfo.setCreateTime(LocalDateTime.now());
            logInfo.setVersion(version);
            logInfo.setTakeUpTime(System.currentTimeMillis() - startTime.get());
            sysLogService.save(logInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterThrowing(pointcut = "exceptionLogPoinCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            // 获取切入点所在的方法
            Method method = signature.getMethod();
            Object userId = request.getAttribute("userId");
            AdminAdministraltion all = adminAdministrationService.getAll((Integer) userId);
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            sysLogService.error(
                    LogErrorInfo.builder()
                            .id(UUID.randomUUID().toString())
                            .reqParam(JSON.toJSONString(converMap(joinPoint)))
                            .method(className + "." + method.getName())
                            .name(e.getClass().getName())
                            .message(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace()))
                            .userId(String.valueOf(userId))
                            .userName(all.getUsername())
                            .uri(request.getRequestURI())
                            .ip(IPUtils.getIpAddress(request))
                            .version(version)
                            .createTime(LocalDateTime.now())
                            .build()
            );
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public String converMap(JoinPoint joinPoint) throws JsonProcessingException {
        Object videosVo=null;
        String name = joinPoint.getSignature().getName();
        if ("addCommonUser".equals(name)){
             videosVo = joinPoint.getArgs()[1];
        }else {
             videosVo = joinPoint.getArgs()[0];
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(videosVo);

    }

    public Map<String, String> converMapNo(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "<br/>");
        }
        String message = exceptionName + ":" + exceptionMessage + "<br/>" + strbuff.toString();
        return message;
    }
}

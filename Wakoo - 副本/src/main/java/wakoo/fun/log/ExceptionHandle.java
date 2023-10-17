package wakoo.fun.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wakoo.fun.vo.MsgVo;
import ws.schild.jave.EncoderException;

import java.net.MalformedURLException;
import java.util.Objects;

/**
 * @ClassName： ExceptionHandle.java
 * @ClassPath： com.tansci.common.exception.ExceptionHandle.java
 * @Description： 全局异常统一处理
 * @Author： tanyp
 * @Date： 2021/10/22 17:05
 **/
@Slf4j
@RestControllerAdvice
public class ExceptionHandle {

    /**
     * @MonthName： handleException
     * @Description： 统一的异常处理方法
     * @Author： tanyp
     * @Date： 2021/10/22 17:16
     * @Param： [e]
     * @return： com.kuiper.qms.common.Wrapper
     **/
    @ExceptionHandler(value = Exception.class)
    public MsgVo handleException(Exception e) {
        if (e instanceof BusinessException) {
            BusinessException ex = (BusinessException) e;
            log.error("系统自定义业务异常：{}", ex.getMessage());
            return new MsgVo(ex.getCode(), ex.getMessage(), null);
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            log.error("参数校验异常：{}", Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
            return new  MsgVo(403, "参数有误：" + ex.getBindingResult().getFieldError().getDefaultMessage(), null);
        } else {
            log.error("系统异常：{}", e.getMessage() );
            if (e instanceof MalformedURLException || e instanceof EncoderException){
                log.error("URL格式错误：{}", e.getMessage());
                return new MsgVo(500, "请填写正确的视频路径", null);
            }
            return new MsgVo(500, "系统异常，请稍后重试！", null);
        }
    }

}

package wakoo.fun.common;

import java.lang.annotation.*;

/**
 * @path：com.demo.utils.annotation.Log.java
 * @className：Log.java
 * @description：自定义操作日志注解
 * @author：tanyp
 * @dateTime：2021/11/18 13:53
 * @editNote：
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 操作模块
     * @return
     */
    String modul() default "";

    /**
     * 操作类型
     * @return
     */
    String type() default "";

    /**
     * 操作说明
     * @return
     */
    String desc() default "";

}


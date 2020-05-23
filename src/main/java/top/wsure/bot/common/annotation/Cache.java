package top.wsure.bot.common.annotation;

import java.lang.annotation.*;

/**
 * FileName: Cache
 * Author:   Administrator
 * Date:     2020-5-5
 * Description: 自定义缓存
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    String name() default "";
}

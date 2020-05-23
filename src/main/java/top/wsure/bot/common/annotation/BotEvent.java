package top.wsure.bot.common.annotation;

import top.wsure.bot.common.enums.CommandAuthorityEnum;

import java.lang.annotation.*;

/**
 * FileName: BotEvent
 * Author:   Administrator
 * Date:     2020/1/31
 * Description: 机器人事件
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BotEvent {
    String name() default "";
    CommandAuthorityEnum[] level() default {CommandAuthorityEnum.DEVELOPER};
}

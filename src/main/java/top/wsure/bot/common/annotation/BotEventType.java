package top.wsure.bot.common.annotation;

import top.wsure.bot.common.enums.CommandAuthorityEnum;
import top.wsure.bot.common.enums.EventsEnum;

import java.lang.annotation.*;

/**
 * FileName: BotEventType
 * Author:   Administrator
 * Date:     2020-4-4
 * Description: 机器人事件类型
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BotEventType {
    EventsEnum[] type() default {EventsEnum.PRIVATE_MSG};
    String[] alias() default {};
}

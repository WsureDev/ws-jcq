package top.wsure.bot.common.core;

import lombok.extern.java.Log;
import org.apache.commons.collections.CollectionUtils;
import top.wsure.bot.common.annotation.BotEvent;
import top.wsure.bot.common.annotation.BotEventType;
import top.wsure.bot.common.enums.CommandAuthorityEnum;
import top.wsure.bot.common.enums.EventsEnum;
import top.wsure.bot.entity.CommandDo;
import top.wsure.bot.entity.MenuDo;
import top.wsure.bot.entity.MessageDo;
import top.wsure.bot.utils.CommandUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.meowy.cqp.jcq.entity.IMsg.MSG_IGNORE;
import static top.wsure.bot.common.config.Constants.*;
import static top.wsure.bot.Bot.CQ;

/**
 * FileName: MessageHandler
 * Author:   wsure
 * Date:     2020/1/17 下午2:53
 * Description:
 */
@Log
public class EventHandler {

    private static class EventHandlerHolder {
        private static final EventHandler INSTANCE = new EventHandler();
    }

    private EventHandler(){}

    public static final EventHandler getInstance() {
        return EventHandlerHolder.INSTANCE;
    }

    public int eventProcess(MessageDo message){
        if(EventsEnum.messageEvent().contains(message.getEvent().getEvent()))
        {
            List<MenuDo> menus = CommandUtils.getMenu(message.getMsg());
            switch (message.getEvent()){
                case PRIVATE_MSG:
                    menus.forEach( menu -> CQ.sendPrivateMsg(message.getFromQQ(),menu.getAnswer()));
                    break;
                case GROUP_MSG:
                    menus.forEach( menu -> CQ.sendGroupMsg(message.getFromGroup(),CC.at(message.getFromQQ()) + menu.getAnswer()));
                    break;
            }
        }
        List<CommandDo> commands = CommandUtils.getCommand(message.getMsg());
        List<Method> methods = botEventMap.get(message.getEvent().getEvent());
        if(CollectionUtils.isNotEmpty(methods)){
            commands.forEach( cmd -> methods.stream().filter(method ->
                    Arrays.asList( method.getAnnotation(BotEventType.class).alias()).contains(cmd.getAlia()) &&
                            method.getDeclaringClass().getAnnotation(BotEvent.class).name().equals(cmd.getComponentName()) &&
                    Arrays.stream(method.getDeclaringClass().getAnnotation(BotEvent.class).level())
                            .anyMatch(v-> CommandAuthorityEnum.isAllowed(message.getFromQQ(),message.getFromGroup(),v))
            ).forEach( method -> {
                try {
//                    log.info("开始执行" + cmd.getCommand() + " " + message.getEvent().getEvent() +" 方法"+method.getName());
                    method.invoke(method.getDeclaringClass().newInstance(),message,cmd);
//                    log.info("执行结束" + cmd.getCommand() + " " + message.getEvent().getEvent() +" 方法"+method.getName());
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    log.warning(cmd.getComponentName()+" > " + cmd.getCommand() + " " + message.getEvent().getEvent()+"执行失败，原因:"+e.getMessage());
                    e.printStackTrace();
                }
            }));

        }
        return MSG_IGNORE;
    }
}

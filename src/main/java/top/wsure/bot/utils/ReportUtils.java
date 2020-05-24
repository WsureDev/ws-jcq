package top.wsure.bot.utils;

import org.apache.commons.collections.CollectionUtils;
import org.meowy.cqp.jcq.entity.Group;
import org.meowy.cqp.jcq.entity.Member;
import top.wsure.bot.entity.PersonDo;

import java.util.List;
import java.util.Random;

import static top.wsure.bot.Bot.CQ;
import static top.wsure.bot.common.config.Constants.ROBOT_CONFIG;

/**
 * FileName: ReportUtils
 * Author:   Administrator
 * Date:     2020/1/23
 * Description: 通知相关工具
 */
public class ReportUtils {

    public static boolean reportMessageToMaster(String message){
        if(ROBOT_CONFIG == null || CollectionUtils.isEmpty(ROBOT_CONFIG.getMaster()))
            return false;
        try {

            sendPrivateToPersons(message,ROBOT_CONFIG.getMaster());

        } catch (Exception e){
            CQ.logWarning("通知主人","发送通知出现异常");
            return false;
        }

        return true;
    }

    public static boolean reportMessageToDeveloper(String message){
        if(ROBOT_CONFIG == null || CollectionUtils.isEmpty(ROBOT_CONFIG.getDev()))
            return false;
        try {

            sendPrivateToPersons(message,ROBOT_CONFIG.getDev());

        } catch (Exception e){
            CQ.logWarning("通知开发者","发送通知出现异常");
            return false;
        }

        return true;
    }

    public static boolean sendToAllGroup(String message){
        List<Group> list = CQ.getGroupList();
        if(CollectionUtils.isEmpty(list)){
            CQ.logWarning("发送消息到所有群","群列表为空");
            return false;
        }
        try{
            sendGroupMessage(message,list);
        }catch (Exception e){
            CQ.logWarning("发送消息到所有群","发送消息出现异常");
            return false;
        }
        return true;
    }

    public static void sendPrivateToPersons(String message,List<PersonDo> list){
        ((Runnable) () -> {
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(master -> {
                    CQ.sendPrivateMsg(master.getQq(), message);
                    try {
                        Thread.sleep(3000+new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        CQ.logInfo("sendPrivateToPersons发送失败","QQ:"+master.getQq()+",message:"+message);
                    }
                });
            }
        }).run();
    }

    public static void sendPrivateToMembers(String message, List<Member> list){
        ((Runnable) () -> {
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(master -> {
                    CQ.sendPrivateMsg(master.getQQId(), message);
                    try {
                        Thread.sleep(3000+new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        CQ.logInfo("sendPrivateToMembers发送失败","QQ:"+master.getQQId()+",message:"+message);
                    }
                });
            }
        }).run();
    }

    public static void sendGroupMessage(String message, List<Group> list){
        ((Runnable) () -> {
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(group -> {
                    CQ.sendGroupMsg(group.getId(), message);
                    try {
                        Thread.sleep(3000+new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        CQ.logInfo("sendGroupMessage发送失败","groupId:"+group.getId()+",message:"+message);
                    }
                });
            }
        }).run();
    }

    public static boolean isMaster(Long qq){
        List<PersonDo> masters = ROBOT_CONFIG.getMaster();
        if(qq == null || ROBOT_CONFIG == null || CollectionUtils.isEmpty(masters))
            return false;
        return masters.stream().anyMatch(v -> v.getQq() == qq);
    }

    public static boolean isDeveloper(Long qq){
        List<PersonDo> masters = ROBOT_CONFIG.getDev();
        if(qq == null || ROBOT_CONFIG == null || CollectionUtils.isEmpty(masters))
            return false;
        return masters.stream().anyMatch(v -> v.getQq() == qq);
    }

}

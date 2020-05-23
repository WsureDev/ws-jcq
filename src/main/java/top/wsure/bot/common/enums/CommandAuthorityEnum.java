package top.wsure.bot.common.enums;

import static top.wsure.bot.utils.ReportUtils.isDeveloper;
import static top.wsure.bot.utils.ReportUtils.isMaster;
import static top.wsure.bot.Bot.CQ;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meowy.cqp.jcq.entity.Member;

/**
 * FileName: CommandAuthorityEnum
 * Author:   Administrator
 * Date:     2020-5-24
 * Description: 指令权限等级
 */
@Getter
@AllArgsConstructor
public enum CommandAuthorityEnum {
    MEMBER(1),
    ADMIN(2),
    OWNER(3),
    DEVELOPER(4),
    MASTER(5),
    ;

    private int level;

    public static int getAuthority(long fromQQ,Long fromGroup){
        if(isMaster(fromQQ))
            return MASTER.getLevel();
        else if(isDeveloper(fromQQ))
            return DEVELOPER.getLevel();
        else if(fromGroup == null)
            return MEMBER.getLevel();
        else {
            Member member = CQ.getGroupMemberInfo(fromGroup,fromQQ,true);
            if(member == null) return MEMBER.getLevel();
            return member.getAuthority().value();
        }
    }

    public static boolean isAllowed(long fromQQ,Long fromGroup,CommandAuthorityEnum level){
        return getAuthority(fromQQ, fromGroup) >= level.getLevel();
    }

}

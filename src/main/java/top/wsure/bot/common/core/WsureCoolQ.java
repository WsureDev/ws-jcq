package top.wsure.bot.common.core;

import org.meowy.cqp.jcq.entity.*;

import java.io.File;
import java.util.Collection;
import java.util.List;
import static top.wsure.bot.Bot.AppID;

/**
 * FileName: WsureCoolQ
 * Author:   Administrator
 * Date:     2020-4-25
 * Description: 静态代理
 */
public class WsureCoolQ {
    private CoolQ innerCQ;

    public WsureCoolQ(CoolQ CQ) {
        this.innerCQ = CQ;
    }


    public int sendPrivateMsg(long qqId, String msg) {
        return this.innerCQ.sendPrivateMsg(qqId, msg);
    }

    public int sendGroupMsg(long groupId, String msg) {
        return this.innerCQ.sendGroupMsg(groupId, msg);
    }

    public int sendDiscussMsg(long discussionId, String msg) {
        return this.innerCQ.sendDiscussMsg(discussionId, msg);
    }

    public int deleteMsg(long msgId) {
        return this.innerCQ.deleteMsg(msgId);
    }

    public int sendLike(long qqId, int times) {
        return this.innerCQ.sendLike(qqId, times);

    }

    public String getCookies() {
        return this.innerCQ.getCookies();

    }

    public int getCsrfToken() {
        return this.innerCQ.getCsrfToken();

    }

    public String getRecord(String file, String outformat) {
        return this.innerCQ.getRecord(file, outformat);
    }

    public int setGroupKick(long groupId, long qqId, boolean notBack) {
        return this.innerCQ.setGroupKick(groupId, qqId, notBack);
    }

    public int setGroupBan(long groupId, long qqId, long banTime) {
        return this.innerCQ.setGroupBan(groupId, qqId, banTime);
    }

    public int setGroupAdmin(long groupId, long qqId, boolean isAdmin) {
        return this.innerCQ.setGroupAdmin(groupId, qqId, isAdmin);
    }

    public int setGroupWholeBan(long groupId, boolean isBan) {
        return this.innerCQ.setGroupWholeBan(groupId, isBan);
    }

    public int setGroupAnonymousBan(long groupId, String anonymous, long banTime) {
        return this.innerCQ.setGroupAnonymousBan(groupId, anonymous, banTime);
    }

    public int setGroupAnonymous(long groupId, boolean isAnonymous) {
        return this.innerCQ.setGroupAnonymous(groupId, isAnonymous);
    }

    public int setGroupCard(long groupId, long qqId, String nick) {
        return this.innerCQ.setGroupCard(groupId, qqId, nick);
    }

    public int setGroupLeave(long groupId, boolean isDisband) {
        return this.innerCQ.setGroupLeave(groupId, isDisband);
    }

    public int setGroupSpecialTitle(long groupId, long qqId, String title, long expireTime) {
        return this.innerCQ.setGroupSpecialTitle(groupId, qqId, title, expireTime);
    }

    public Member getGroupMemberInfo(long groupId, long qqId, boolean notCache) {
        return this.innerCQ.getGroupMemberInfo(groupId, qqId, notCache);
    }

    public Member getGroupMemberInfo(long groupId, long qqId) {
        return this.innerCQ.getGroupMemberInfo(groupId, qqId);
    }

    public Member getGroupMemberInfo(long groupId, long qqId, boolean notCache, Member member) {
        return this.innerCQ.getGroupMemberInfo(groupId, qqId, notCache, member);
    }

    public QQInfo getStrangerInfo(long qqId, boolean notCache) {
        return this.innerCQ.getStrangerInfo(qqId, notCache);
    }

    public QQInfo getStrangerInfo(long qqId) {
        return this.innerCQ.getStrangerInfo(qqId);
    }

    public int setDiscussLeave(long discussionId) {
        return this.innerCQ.setDiscussLeave(discussionId);
    }

    public int setFriendAddRequest(String responseFlag, int backType, String remarks) {
        return this.innerCQ.setFriendAddRequest(responseFlag, backType, remarks);
    }

    public int setGroupAddRequest(String responseFlag, int requestType, int backType, String reason) {
        return this.innerCQ.setGroupAddRequest(responseFlag, requestType, backType, reason);
    }

    public List<Member> getGroupMemberList(long groupId) {
        return this.innerCQ.getGroupMemberList(groupId);
    }

    public List<Group> getGroupList() {
        return this.innerCQ.getGroupList();
    }

    public Group getGroupInfo(long groupId, boolean notCache) {
        return this.innerCQ.getGroupInfo(groupId, notCache);
    }

    public List<Friend> getFriendList() {
        return this.innerCQ.getFriendList();
    }

    public int addLog(int priority, String category, String content) {
        return this.innerCQ.addLog(priority, category, content);
    }

    public int logTrace(String category, String content) {
        return this.innerCQ.logTrace(decodeCQCode(category),decodeCQCode(content));
    }
    public int logTrace(String category, String content,boolean origin) {
        if(origin) return this.innerCQ.logTrace(category,content);
        return this.innerCQ.logTrace(decodeCQCode(category),decodeCQCode(content));
    }

    public int logDebug(String category, String content) {
        return this.innerCQ.logDebug(decodeCQCode(category), decodeCQCode(content));
    }
    public int logDebug(String category, String content,boolean origin) {
        if(origin) return this.innerCQ.logDebug(category, content);
        return this.innerCQ.logDebug(decodeCQCode(category), decodeCQCode(content));
    }

    public int logInfo(String category, String content) {
        return this.innerCQ.logInfo(decodeCQCode(category), decodeCQCode(content));
    }
    public int logInfo(String category, String content,boolean origin) {
        if(origin) return this.innerCQ.logInfo(category, content);
        return this.innerCQ.logInfo(decodeCQCode(category), decodeCQCode(content));
    }

    public int logInfoRecv(String category, String content) {
        return this.innerCQ.logInfoRecv(decodeCQCode(category), decodeCQCode(content));
    }
    public int logInfoRecv(String category, String content,boolean origin) {
        if(origin) return this.innerCQ.logInfoRecv(category, content);
        return this.innerCQ.logInfoRecv(decodeCQCode(category), decodeCQCode(content));
    }

    public int logInfoSend(String category, String content) {
        return this.innerCQ.logInfoSend(decodeCQCode(category), decodeCQCode(content));
    }
    public int logInfoSend(String category, String content,boolean origin) {
        if(origin) return this.innerCQ.logInfoSend(category, content);
        return this.innerCQ.logInfoSend(decodeCQCode(category), decodeCQCode(content));
    }

    public int logInfoSuccess(String category, String content) {
        return this.innerCQ.logInfoSuccess(decodeCQCode(category), decodeCQCode(content));
    }
    public int logInfoSuccess(String category, String content,boolean origin) {
        if(origin) return this.innerCQ.logInfoSuccess(category, content);
        return this.innerCQ.logInfoSuccess(decodeCQCode(category), decodeCQCode(content));
    }

    public int logWarning(String category, String content) {
        return this.innerCQ.logWarning(decodeCQCode(category), decodeCQCode(content));
    }
    public int logWarning(String category, String content,boolean origin) {
        if(origin) return this.innerCQ.logWarning(category, content);
        return this.innerCQ.logWarning(decodeCQCode(category), decodeCQCode(content));
    }

    public int logError(String category, String content) {
        return this.innerCQ.logError(decodeCQCode(category), decodeCQCode(content));
    }
    public int logError(String category, String content,boolean origin) {
        if(origin) return this.innerCQ.logError(category, content);
        return this.innerCQ.logError(decodeCQCode(category), decodeCQCode(content));
    }

    public int logFatal(String category, String content) {
        return this.innerCQ.logFatal(decodeCQCode(category), decodeCQCode(content));
    }
    public int logFatal(String category, String content,boolean origin) {
        if(origin) return this.innerCQ.logFatal(category, content);
        return this.innerCQ.logFatal(decodeCQCode(category), decodeCQCode(content));
    }

    public String getAppDirectory() {
        if(this.innerCQ.getClass().equals(CQDebug.class))
            return this.innerCQ.getAppDirectory()+"data/app/org.meowy.cqp.jcq/app/"+ AppID;
        return this.innerCQ.getAppDirectory();
    }

    public int setFatal(String errorInfo) {
        return this.innerCQ.setFatal(errorInfo);
    }

    public long getLoginQQ() {
        return this.innerCQ.getLoginQQ();
    }

    public String getLoginNick() {
        return this.innerCQ.getLoginNick();
    }

    public int logTrace(String category, Throwable e) {
        return this.innerCQ.logTrace(category, e);
    }

    public int logTrace(String category, String formatContent, Object... arguments) {
        return this.innerCQ.logTrace(category, formatContent,arguments);
    }

    public int logDebug(String category, Throwable e) {
        return this.innerCQ.logDebug(category, e);
    }

    public int logDebug(String category, String formatContent, Object... arguments) {
        return this.innerCQ.logDebug(category, formatContent,arguments);
    }

    public int logInfo(String category, Throwable e) {
        return this.innerCQ.logInfo(category, e);
    }

    public int logInfo(String category, String formatContent, Object... arguments) {
        return this.innerCQ.logInfo(category, formatContent,arguments);
    }

    public int logInfoRecv(String category, Throwable e) {
        return this.innerCQ.logInfoRecv(category, e);
    }

    public int logInfoRecv(String category, String formatContent, Object... arguments) {
        return this.innerCQ.logInfoRecv(category, formatContent, arguments);
    }

    public int logInfoSend(String category, Throwable e) {
        return this.innerCQ.logInfoSend(category, e);
    }

    public int logInfoSend(String category, String formatContent, Object... arguments) {
        return this.innerCQ.logInfoSend(category, formatContent, arguments);
    }

    public int logInfoSuccess(String category, Throwable e) {
        return this.innerCQ.logInfoSuccess(category, e);
    }

    public int logInfoSuccess(String category, String formatContent, Object... arguments) {
        return this.innerCQ.logInfoSuccess(category, formatContent, arguments);
    }

    public int logWarning(String category, Throwable e) {
        return this.innerCQ.logWarning(category, e);
    }

    public int logWarning(String category, String formatContent, Object... arguments) {
        return this.innerCQ.logWarning(category, formatContent, arguments);
    }

    public int logError(String category, Throwable e) {
        return this.innerCQ.logError(category, e);
    }

    public int logError(String category, String formatContent, Object... arguments) {
        return this.innerCQ.logError(category, formatContent, arguments);
    }

    public int logFatal(String category, Throwable e) {
        return this.innerCQ.logFatal(category, e);
    }

    public int logFatal(String category, String formatContent, Object... arguments) {
        return this.innerCQ.logFatal(category, formatContent, arguments);
    }

    public int sendLike(long qqId) {
        return this.innerCQ.sendLike(qqId);
    }

    public String getCookies(String domain) {
        return this.innerCQ.getCookies(domain);
    }

    public String getImage(String file) {
        return this.innerCQ.getImage(file);
    }

    public List<String> getImages(Collection<String> files) {
        return this.innerCQ.getImages(files);
    }

    public File getImageFile(String file) {
        return this.innerCQ.getImageFile(file);
    }

    public List<File> getImageFiles(Collection<String> files) {
        return this.innerCQ.getImageFiles(files);
    }

    public boolean canSendImage() {
        return this.innerCQ.canSendImage();
    }

    public boolean canSendRecord() {
        return this.innerCQ.canSendRecord();
    }

    public Anonymous getAnonymous(String source) {
        return this.innerCQ.getAnonymous(source);
    }

    public GroupFile getGroupFile(String source) {
        return this.innerCQ.getGroupFile(source);
    }

    public Font getFont(int font) {
        return this.innerCQ.getFont(font);
    }

    public CQStatus getLastStatus() {
        return this.innerCQ.getLastStatus();
    }


    /**
     * 爷吐了，每次特么因为cq码debug折腾好久，淦！
     * @param input
     * @return
     */
    public static String decodeCQCode(String input){
        input = org.meowy.cqp.jcq.util.StringUtils.stringReplace(input, "&#91;", "【");
        input = org.meowy.cqp.jcq.util.StringUtils.stringReplace(input, "&#93;", "】");
        input = org.meowy.cqp.jcq.util.StringUtils.stringReplace(input, "&#44;", "，");
        input = org.meowy.cqp.jcq.util.StringUtils.stringReplace(input, "&amp;", "＆");
        return input;
    }
}

package top.wsure.bot;

import lombok.SneakyThrows;
import org.meowy.cqp.jcq.entity.*;
import org.meowy.cqp.jcq.event.JcqAppAbstract;
import top.wsure.bot.common.core.*;
import top.wsure.bot.common.exceptions.BotException;
import top.wsure.bot.entity.MessageDo;
import top.wsure.bot.common.enums.EventsEnum;
import top.wsure.bot.utils.CommandUtils;
import top.wsure.bot.utils.FileUtils;
import top.wsure.bot.utils.ReflectionsUtils;

import static top.wsure.bot.common.config.Constants.*;

import javax.swing.*;
import java.lang.reflect.*;
import java.util.stream.Collectors;

/**
 * 本文件是JCQ插件的主类<br>
 * <br>
 * <p>
 * 注意修改json中的class来加载主类，如不设置则利用appid加载，最后一个单词自动大写查找<br>
 * 例：appid(com.example.demo) 则加载类 com.example.Demo<br>
 * 文档地址： https://gitee.com/Sobte/JCQ-CoolQ <br>
 * 帖子：https://cqp.cc/t/37318 <br>
 * 辅助开发变量: {@link JcqAppAbstract#CQ CQ}({@link org.meowy.cqp.jcq.entity.CoolQ 酷Q核心操作类}),
 * {@link JcqAppAbstract#CC CC}({@link org.meowy.cqp.jcq.message.CQCode 酷Q码操作类}),
 * 具体功能可以查看文档
 */

public abstract class Bot extends JcqAppAbstract implements ICQVer, IMsg, IRequest {

    public static String AppID = null;
    /**
     * 关于新版：本版本只是为了测试下新做的插件能不能正常运行，并不包含任何 “新” 内容
     * 新：指代 打包，调试运行
     * 新版改了整体架构，内部改动非常大，使用上 除了包名改动别无区别
     * 关于包名：可以通过批量替换将老程序里的[com.sobte]全部替换成[org.meowy]即可
     */

    public static WsureCoolQ CQ = null;
    /**
     * 老的方式依然支持，也就是不强行定构造方法也行
     */
    Bot() {
        CQ = new WsureCoolQ(this.getCoolQ());
        AppID = ReflectionsUtils.getAppId(this.getClass());
    }


    /**
     * 使用新的方式加载CQ （建议使用这种方式）
     *
     * @param cq CQ初始化
     */
    public Bot(CoolQ cq) {
        super(cq);
        CQ = new WsureCoolQ(cq);
        AppID = ReflectionsUtils.getAppId(this.getClass());
    }


    /**
     * 打包后将不会调用 请不要在此事件中写其他代码
     * 本函数【禁止】处理其他任何代码，以免发生异常情况。
     * 如需执行初始化代码请在 startup 事件中执行（Type=1001）。
     * 应用AppID,规则见 http://d.cqp.me/Pro/开发/基础信息#appid
     * return ICQVer.CQAPIVER + "," + AppID;
     * @return 返回应用的ApiVer、Appid
     */
    public String appInfo() {
        return ICQVer.CQAPIVER + "," + ReflectionsUtils.getAppId(this.getClass());
    }

    /**
     * 酷Q启动 (Type=1001)<br>
     * 本方法会在酷Q【主线程】中被调用。<br>
     * 请在这里执行插件初始化代码。<br>
     * 请务必尽快返回本子程序，否则会卡住其他插件以及主程序的加载。
     *
     * @return 请固定返回0
     */
    public int startup() {
        WsureCoolQ proxy = ReflectionsUtils.createProxy(CQ.getClass(),new JCQInterceptor()
                ,new Class[]{CoolQ.class},new Object[]{this.getCoolQ()});
        CQ = proxy == null ? CQ : proxy ;


        //缓存监听器
        listener.start();
        // 获取应用数据目录(无需储存数据时，请将此行注释)
//        appDirectory = FileUtils.getAppDirectory();

        try {
            ROBOT_CONFIG = FileUtils.importYaml();
        }catch (Exception e){
            e.printStackTrace();
            CQ.logError("配置文件加载异常","配置文件加载出现异常导致程序无法继续运行");
            return 0;
        }

        ROBOT_COMMANDS = CommandUtils.createCommandMap(ROBOT_CONFIG);

        CQ.logInfo("RobotName",ROBOT_CONFIG.getRobotName());

        // 注解方式加载各个模块
        InitReflectionsMethod.init();
        botEventMap.forEach( (k,v)-> CQ.logInfo(k,v.stream().map(Method::getName).collect(Collectors.joining(","))));

        CQ.logInfo("startup()","week up!");
        // 返回如：D:\CoolQ\data\app\org.meowy.cqp.jcq\data\app\com.example.demo\
        // 应用的所有数据、配置【必须】存放于此目录，避免给用户带来困扰。
        onStart();
        return 0;
    }

    public abstract void onStart();

    /**
     * 酷Q退出 (Type=1002)<br>
     * 本方法会在酷Q【主线程】中被调用。<br>
     * 无论本应用是否被启用，本函数都会在酷Q退出前执行一次，请在这里执行插件关闭代码。
     *
     * @return 请固定返回0，返回后酷Q将很快关闭，请不要再通过线程等方式执行其他代码。
     */
    public int exit() {
        listener.interrupt();
        return 0;
    }

    /**
     * 应用已被启用 (Type=1003)<br>
     * 当应用被启用后，将收到此事件。<br>
     * 如果酷Q载入时应用已被启用，则在 {@link #startup startup}(Type=1001,酷Q启动) 被调用后，本函数也将被调用一次。<br>
     * 如非必要，不建议在这里加载窗口。
     *
     * @return 请固定返回0。
     */
    public int enable() {
        enable = true;
        return 0;
    }

    /**
     * 应用将被停用 (Type=1004)<br>
     * 当应用被停用前，将收到此事件。<br>
     * 如果酷Q载入时应用已被停用，则本函数【不会】被调用。<br>
     * 无论本应用是否被启用，酷Q关闭前本函数都【不会】被调用。
     *
     * @return 请固定返回0。
     */
    public int disable() {
        enable = false;
        return 0;
    }

    /**
     * 私聊消息 (Type=21)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType 子类型，11/来自好友 1/来自在线状态 2/来自群 3/来自讨论组
     * @param msgId   消息ID
     * @param fromQQ  来源QQ
     * @param msg     消息内容
     * @param font    字体
     * @return 返回值*不能*直接返回文本 如果要回复消息，请调用api发送<br>
     * 这里 返回  {@link IMsg#MSG_INTERCEPT MSG_INTERCEPT} - 截断本条消息，不再继续处理<br>
     * 注意：应用优先级设置为"最高"(10000)时，不得使用本返回值<br>
     * 如果不回复消息，交由之后的应用/过滤器处理，这里 返回  {@link IMsg#MSG_IGNORE MSG_IGNORE} - 忽略本条消息
     */
    public int privateMsg(int subType, int msgId, long fromQQ, String msg, int font) {

        return EventHandler.getInstance().eventProcess(MessageDo.builder()
                .event(EventsEnum.PRIVATE_MSG)
                .subType(subType)
                .msgId(msgId)
                .fromQQ(fromQQ)
                .msg(msg)
                .font(font)
                .build()
        );
        // 这里处理消息
//        CQ.sendPrivateMsg(fromQQ, "你发送了这样的消息：" + msg + "\n来自Java插件"+"\n"+messageCache.getCacheDataByKey("1s"));
    }

    /**
     * 群消息 (Type=2)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType       子类型，目前固定为1
     * @param msgId         消息ID
     * @param fromGroup     来源群号
     * @param fromQQ        来源QQ号
     * @param fromAnonymous 来源匿名者
     * @param msg           消息内容
     * @param font          字体
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    public int groupMsg(int subType, int msgId, long fromGroup, long fromQQ, String fromAnonymous, String msg,
                        int font) {
        // 如果消息来自匿名者
        if (fromQQ == 80000000L && !fromAnonymous.equals("")) {
            // 将匿名用户信息放到 anonymous 变量中
            Anonymous anonymous = CQ.getAnonymous(fromAnonymous);
        }

        // 解析CQ码案例 如：[CQ:at,qq=100000]
        // 解析CQ码 常用变量为 CC(CQCode) 此变量专为CQ码这种特定格式做了解析和封装
        // CC.analysis();// 此方法将CQ码解析为可直接读取的对象
        // 解析消息中的QQID
        // long qqId = CC.getAt(msg);// 此方法为简便方法，获取第一个CQ:at里的QQ号，错误时为：-1000
        // List<Long> qqIds = CC.getAts(msg); // 此方法为获取消息中所有的CQ码对象，错误时返回 已解析的数据
        // 解析消息中的图片
        // String image = CC.getImage(msg);// 此方法为简便方法，获取第一个CQ:image里的图片数据，错误时打印异常到控制台，返回 null
        // String file = CQ.getImage(image);// 获取酷Q 下载的图片地址

        // 这里处理消息
        return EventHandler.getInstance().eventProcess(MessageDo.builder()
                .event(EventsEnum.GROUP_MSG)
                .subType(subType)
                .msgId(msgId)
                .fromGroup(fromGroup)
                .fromQQ(fromQQ)
                .fromAnonymous(fromAnonymous)
                .msg(msg)
                .font(font)
                .build()
        );
    }

    /**
     * 讨论组消息 (Type=4)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType     子类型，目前固定为1
     * @param msgId       消息ID
     * @param fromDiscuss 来源讨论组
     * @param fromQQ      来源QQ号
     * @param msg         消息内容
     * @param font        字体
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    public int discussMsg(int subType, int msgId, long fromDiscuss, long fromQQ, String msg, int font) {
        // 这里处理消息
        CQ.sendDiscussMsg(fromDiscuss,"溜了，我和讨论组聊不来");
        CQ.setDiscussLeave(fromDiscuss);
        return EventHandler.getInstance().eventProcess(MessageDo.builder()
                .event(EventsEnum.DISCUSS_MSG)
                .subType(subType)
                .msgId(msgId)
                .fromDiscuss(fromDiscuss)
                .fromQQ(fromQQ)
                .msg(msg)
                .font(font)
                .build()
        );
    }

    /**
     * 群文件上传事件 (Type=11)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType   子类型，目前固定为1
     * @param sendTime  发送时间(时间戳)// 10位时间戳
     * @param fromGroup 来源群号
     * @param fromQQ    来源QQ号
     * @param file      上传文件信息
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    public int groupUpload(int subType, int sendTime, long fromGroup, long fromQQ, String file) {
        GroupFile groupFile = CQ.getGroupFile(file);
        if (groupFile == null) { // 解析群文件信息，如果失败直接忽略该消息
            return IMsg.MSG_IGNORE;
        }

        // 这里处理消息
        return EventHandler.getInstance().eventProcess(MessageDo.builder()
                .event(EventsEnum.GROUP_UPLOAD)
                .subType(subType)
                .sendTime(sendTime)
                .fromGroup(fromGroup)
                .fromQQ(fromQQ)
                .file(file)
                .build()
        );
    }

    /**
     * 群事件-管理员变动 (Type=101)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType        子类型，1/被取消管理员 2/被设置管理员
     * @param sendTime       发送时间(时间戳)
     * @param fromGroup      来源群号
     * @param beingOperateQQ 被操作QQ
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    public int groupAdmin(int subType, int sendTime, long fromGroup, long beingOperateQQ) {

        return EventHandler.getInstance().eventProcess(MessageDo.builder()
                .event(EventsEnum.GROUP_ADMIN)
                .subType(subType)
                .sendTime(sendTime)
                .fromGroup(fromGroup)
                .beingOperateQQ(beingOperateQQ)
                .build()
        );
    }

    /**
     * 群事件-群成员减少 (Type=102)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType        子类型，1/群员离开 2/群员被踢
     * @param sendTime       发送时间(时间戳)
     * @param fromGroup      来源群号
     * @param fromQQ         操作者QQ(仅子类型为2时存在)
     * @param beingOperateQQ 被操作QQ
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    public int groupMemberDecrease(int subType, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
        // 这里处理消息

        return EventHandler.getInstance().eventProcess(MessageDo.builder()
                .event(EventsEnum.GROUP_MEMBER_DECREASE)
                .subType(subType)
                .sendTime(sendTime)
                .fromGroup(fromGroup)
                .fromQQ(fromQQ)
                .beingOperateQQ(beingOperateQQ)
                .build()
        );
    }

    /**
     * 群事件-群成员增加 (Type=103)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType        子类型，1/管理员已同意 2/管理员邀请
     * @param sendTime       发送时间(时间戳)
     * @param fromGroup      来源群号
     * @param fromQQ         操作者QQ(即管理员QQ)
     * @param beingOperateQQ 被操作QQ(即加群的QQ)
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    public int groupMemberIncrease(int subType, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
        // 这里处理消息
        CQ.logInfo("fromGroup", "" + fromGroup);
        CQ.logInfo("fromQQ", "" + fromQQ);
        CQ.logInfo("beingOperateQQ", "" + beingOperateQQ);
        CQ.sendGroupMsg(fromGroup,CC.at(beingOperateQQ)+"欢迎新人");
        return EventHandler.getInstance().eventProcess(MessageDo.builder()
                .event(EventsEnum.GROUP_MEMBER_INCREASE)
                .subType(subType)
                .sendTime(sendTime)
                .fromGroup(fromGroup)
                .fromQQ(fromQQ)
                .beingOperateQQ(beingOperateQQ)
                .build()
        );
    }

    /**
     * 群事件-群禁言 (Type=104)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType        子类型，1/被解禁 2/被禁言
     * @param sendTime       发送时间(时间戳)
     * @param fromGroup      来源群号
     * @param fromQQ         操作者QQ
     * @param beingOperateQQ 被操作QQ(若为全群禁言/解禁，则本参数为 0)
     * @param duration       禁言时长(单位 秒，仅子类型为2时可用)
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    public int groupBan(int subType, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ, long duration) {
        // 这里处理消息

        return EventHandler.getInstance().eventProcess(MessageDo.builder()
                .event(EventsEnum.GROUP_BAN)
                .subType(subType)
                .sendTime(sendTime)
                .fromGroup(fromGroup)
                .fromQQ(fromQQ)
                .beingOperateQQ(beingOperateQQ)
                .duration(duration)
                .build()
        );
    }

    /**
     * 好友事件-好友已添加 (Type=201)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType  子类型，目前固定为1
     * @param sendTime 发送时间(时间戳)
     * @param fromQQ   来源QQ
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    public int friendAdd(int subType, int sendTime, long fromQQ) {
        // 这里处理消息

        return EventHandler.getInstance().eventProcess( MessageDo.builder()
                .event(EventsEnum.FRIEND_ADD)
                .subType(subType)
                .sendTime(sendTime)
                .fromQQ(fromQQ)
                .build()
        );
    }

    /**
     * 请求-好友添加 (Type=301)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType      子类型，目前固定为1
     * @param sendTime     发送时间(时间戳)
     * @param fromQQ       来源QQ
     * @param msg          附言
     * @param responseFlag 反馈标识(处理请求用)
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    public int requestAddFriend(int subType, int sendTime, long fromQQ, String msg, String responseFlag) {
        // 这里处理消息

        /**
         * REQUEST_ADOPT 通过
         * REQUEST_REFUSE 拒绝
         */

        // CQ.setFriendAddRequest(responseFlag, REQUEST_ADOPT, null); // 同意好友添加请求
        return EventHandler.getInstance().eventProcess( MessageDo.builder()
                .event(EventsEnum.REQUEST_ADD_FRIEND)
                .subType(subType)
                .sendTime(sendTime)
                .fromQQ(fromQQ)
                .msg(msg)
                .responseFlag(responseFlag)
                .build()
        );
    }

    /**
     * 请求-群添加 (Type=302)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType      子类型，1/他人申请入群 2/自己(即登录号)受邀入群
     * @param sendTime     发送时间(时间戳)
     * @param fromGroup    来源群号
     * @param fromQQ       来源QQ
     * @param msg          附言
     * @param responseFlag 反馈标识(处理请求用)
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    public int requestAddGroup(int subType, int sendTime, long fromGroup, long fromQQ, String msg,
                               String responseFlag) {
        // 这里处理消息

        /**
         * REQUEST_ADOPT 通过
         * REQUEST_REFUSE 拒绝
         * REQUEST_GROUP_ADD 群添加
         * REQUEST_GROUP_INVITE 群邀请
         */
		/*if(subtype == 1){ // 本号为群管理，判断是否为他人申请入群
			CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD, REQUEST_ADOPT, null);// 同意入群
		}
		if(subtype == 2){
			CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_INVITE, REQUEST_ADOPT, null);// 同意进受邀群
		}*/
        return EventHandler.getInstance().eventProcess(MessageDo.builder()
                .event(EventsEnum.REQUEST_ADD_GROUP)
                .subType(subType)
                .sendTime(sendTime)
                .fromGroup(fromGroup)
                .fromQQ(fromQQ)
                .msg(msg)
                .responseFlag(responseFlag)
                .build()
        );
    }

    /**
     * 本函数会在JCQ【线程】中被调用。
     *
     * @return 固定返回0
     */
    public int menuA() {
        JOptionPane.showMessageDialog(null, "这是测试菜单A，可以在这里加载窗口");
        return 0;
    }

    /**
     * 本函数会在酷Q【线程】中被调用。
     *
     * @return 固定返回0
     */
    public int menuB() {
        JOptionPane.showMessageDialog(null, "这是测试菜单B，可以在这里加载窗口");
        return 0;
    }

}

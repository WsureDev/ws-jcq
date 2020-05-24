package top.wsure.bot.common.config;

import org.meowy.cqp.jcq.message.CQCode;
import top.wsure.bot.common.annotation.BotApiAfterDo;
import top.wsure.bot.common.cache.CacheListener;
import top.wsure.bot.common.cache.CacheManagerImpl;
import top.wsure.bot.entity.CommandDo;
import top.wsure.bot.entity.RobotConfigDo;
import top.wsure.bot.common.enums.CacheEnum;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * FileName: Constants
 * Author:   Administrator
 * Date:     2020-4-5
 * Description: 常量
 */
public class Constants {

    public static CQCode CC = new CQCode();
    public static CacheListener listener = new CacheListener();
//    public static CacheManagerImpl warFrameCache = new CacheManagerImpl(CacheEnum.WARFRAME_CACHE.getName());
//    public static CacheManagerImpl messageCache = new CacheManagerImpl(CacheEnum.MESSAGE_CACHE.getName());
    public static RobotConfigDo ROBOT_CONFIG ;
    public static List<CommandDo> ROBOT_COMMANDS ;
    public static ConcurrentMap<String,Object> COMPONENTS_MAP = new ConcurrentHashMap<>();
    public static Map<String , List<Method>> botEventMap = new HashMap<>();
    public static Map<String , List<Class<?>>> botApiAfterMap = new HashMap<>();
    public static Map<String , List<Class<?>>> botApiBeforeMap = new HashMap<>();

}

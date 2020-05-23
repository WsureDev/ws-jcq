package top.wsure.bot.common.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import top.wsure.bot.common.annotation.ApiAfter;
import top.wsure.bot.common.annotation.ApiBefore;
import top.wsure.bot.common.annotation.BotEventType;
import top.wsure.bot.common.annotation.Cache;
import top.wsure.bot.common.cache.CacheManagerImpl;
import top.wsure.bot.common.cache.EntityCache;
import top.wsure.bot.common.enums.CacheEnum;
import top.wsure.bot.utils.DynamicEnumUtils;
import top.wsure.bot.utils.ReflectionsUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static top.wsure.bot.common.config.Constants.*;

/**
 * FileName: InitReflectionsMethod
 * Author:   Administrator
 * Date:     2020-4-5
 * Description: 反射获取模块下方法
 */
public class InitReflectionsMethod {
    public static void init(){
        List<Method> methods = new ArrayList<>(ReflectionsUtils.getAnnotatedMethod(BotEventType.class));
        if(CollectionUtils.isNotEmpty(methods))
            methods.forEach( method -> Arrays.stream(method.getAnnotation(BotEventType.class).type()).forEach(
                    events -> {
                        List<Method> methodList = botEventMap.get(events.getEvent());
                        if(CollectionUtils.isNotEmpty(methodList)){
                            methodList.add(method);
                        } else {
                            botEventMap.put(events.getEvent(),new ArrayList<>(Collections.singletonList(method)));
                        }
                    }
            ));
        List<Class<?>> classes = new ArrayList<>(ReflectionsUtils.getFullReflections(ApiAfter.class));
        if(CollectionUtils.isNotEmpty(classes)){
            classes.stream()
//                    .filter( clazz -> clazz.isAssignableFrom(BotApiAfterDo.class))
                    .forEach( clazz -> Arrays.stream(clazz.getAnnotation(ApiAfter.class).apiName()).forEach(
                    api -> {
                        List<Class<?>> classList = botApiAfterMap.get(api);
                        if(CollectionUtils.isNotEmpty(classList)){
                            classList.add(clazz);
                        } else {
                            botApiAfterMap.put(api,new ArrayList<>(Collections.singletonList(clazz)));
                        }
                    }));
        }
        List<Class<?>> classes2 = new ArrayList<>(ReflectionsUtils.getFullReflections(ApiBefore.class));
        if(CollectionUtils.isNotEmpty(classes2)){
            classes2.stream()
//                    .filter( clazz -> clazz.isAssignableFrom(BotApiAfterDo.class))
                    .forEach( clazz -> Arrays.stream(clazz.getAnnotation(ApiBefore.class).apiName()).forEach(
                            api -> {
                                List<Class<?>> classList = botApiBeforeMap.get(api);
                                if(CollectionUtils.isNotEmpty(classList)){
                                    classList.add(clazz);
                                } else {
                                    botApiBeforeMap.put(api,new ArrayList<>(Collections.singletonList(clazz)));
                                }
                            }));
        }

        List<Class<?>> cacheClasses = new ArrayList<>(ReflectionsUtils.getFullReflections(Cache.class));
        if(CollectionUtils.isNotEmpty(cacheClasses)){
            cacheClasses.stream()
//                    .filter( clazz -> clazz.isAssignableFrom(BotApiAfterDo.class))
                    .forEach( cacheClass -> {
                        String cacheName = cacheClass.getAnnotation(Cache.class).name();
                        if(StringUtils.isNotBlank(cacheName) && CacheEnum.getManager(cacheName) == null){
                            Map<String, EntityCache> cacheMap = new ConcurrentHashMap<>();
                            DynamicEnumUtils.addEnum(CacheEnum.class,cacheName,
                                    new Class[]{String.class},
                                    new Object[]{cacheName});
                        }
                    });
        }
    }
}

package top.wsure.bot.utils;

import lombok.extern.java.Log;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.meowy.cqp.jcq.entity.CoolQ;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisBase;
import org.objenesis.ObjenesisHelper;
import org.objenesis.ObjenesisStd;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import top.wsure.bot.common.annotation.BotEvent;
import top.wsure.bot.common.annotation.BotEventType;
import static top.wsure.bot.Bot.CQ;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * FileName: ReflectionsUtils
 * Author:   Administrator
 * Date:     2020-4-5
 * Description: Reflections工具
 */
@Log
public class ReflectionsUtils {
    private static Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages("top.wsure.warframe") // 指定路径URL
                .addScanners(new SubTypesScanner()) // 添加子类扫描工具
            .addScanners(new FieldAnnotationsScanner()) // 添加 属性注解扫描工具
            .addScanners(new MethodAnnotationsScanner() ) // 添加 方法注解扫描工具
            .addScanners(new MethodParameterScanner() ) // 添加方法参数扫描工具
            );

    /**
     * 如果没有配置scanner，默认使用SubTypesScanner和TypeAnnotationsScanner
     * @return
     */
    public static Set<Class<?>> getFullReflections(Class<? extends Annotation> clazz){

        return reflections.getTypesAnnotatedWith(clazz);
    }

    public static Set<Method> getAnnotatedMethod(Class<? extends Annotation> clazz){
        return reflections.getMethodsAnnotatedWith(clazz);
    }

    public static Set<Field> getField(Class<? extends Annotation> clazz){
        return reflections.getFieldsAnnotatedWith(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T>T createProxy(Class<T> classToMock, MethodInterceptor interceptor, Class<?>[] classes, Object[] args){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(classToMock);
        enhancer.setCallback(interceptor);
        return (T) enhancer.create(classes,args);
    }

    public static String getAppId(Class<?> botClass){
        String simpleName = botClass.getSimpleName();
        return botClass.getPackage().getName()+"."+simpleName.substring(0,1).toLowerCase()+simpleName.substring(1);
    }

}
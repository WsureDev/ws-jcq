package top.wsure.bot.common.core;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.collections.CollectionUtils;
import top.wsure.bot.common.annotation.BotApiAfterDo;
import top.wsure.bot.common.annotation.BotApiBeforeDo;
import top.wsure.bot.common.exceptions.BotException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static top.wsure.bot.common.config.Constants.botApiAfterMap;
import static top.wsure.bot.common.config.Constants.botApiBeforeMap;

/**
 * FileName: JCQInterceptor
 * Author:   Administrator
 * Date:     2020-4-24
 * Description:
 */
public class JCQInterceptor implements MethodInterceptor {

    public JCQInterceptor(){}
    @Override
    public Object intercept(Object CQ, Method method, Object[] args, MethodProxy methodProxy)  {
        System.out.println(CQ == null ? "CQ is null":CQ.getClass().getName());
        String apiName = method.getName();
        Object result = null;
        AtomicBoolean abort = new AtomicBoolean(false);
        List<Class<?>> beforeClasses = botApiBeforeMap.get(apiName);

        if(CollectionUtils.isNotEmpty(beforeClasses)){
            beforeClasses.forEach(clazz -> {
                try {
                    BotApiBeforeDo beforeDo = (BotApiBeforeDo) clazz.newInstance();
                    if (beforeDo.execute(apiName,args))
                        abort.set(true);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
        if(abort.get()){
            ((WsureCoolQ)CQ).logInfo("代理被阻止，代理的API："+apiName,"BotApiBeforeDo");
        } else {
            try {
                result = methodProxy.invokeSuper(CQ,args);
            } catch (Throwable e){
                e.printStackTrace();
                ((WsureCoolQ)CQ).logInfo("代理失败，代理的API："+apiName,"BotApiRunning");
            }
        }

        List<Class<?>> afterClasses = botApiAfterMap.get(apiName);
        if(CollectionUtils.isNotEmpty(afterClasses)){
            Object finalResult = result;
            afterClasses.forEach(clazz -> {
                try {
                    BotApiAfterDo afterDo = (BotApiAfterDo) clazz.newInstance();
                    afterDo.execute(apiName,args, finalResult);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
        return result;
    }
}

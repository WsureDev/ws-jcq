package top.wsure.bot.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.wsure.bot.common.cache.CacheManagerImpl;
import top.wsure.bot.common.cache.EntityCache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * FileName: CacheEnums
 * Author:   wsure
 * Date:     2020/1/19 上午11:41
 * Description:
 */

@Getter
public enum CacheEnum {

    WARFRAME_CACHE("warframe"),

    MESSAGE_CACHE("message");

     private String name;

     private CacheManagerImpl manager;

     private ConcurrentHashMap<String, EntityCache> cache ;

    CacheEnum(String name) {
        this.name = name;
        this.cache = new ConcurrentHashMap<>();
        this.manager = new CacheManagerImpl(this.cache);
    }

    public static ConcurrentHashMap<String, EntityCache> getCacheByName(String name){
         for(CacheEnum e:CacheEnum.values()){
             if(e.getName().equals(name)){
                 return e.getCache();
             }
         }
         return null;
     }

    public static CacheManagerImpl getManager(String name){
        for(CacheEnum e:CacheEnum.values()){
            if(e.getName().equals(name)){
                return e.getManager();
            }
        }
        return null;
    }
}

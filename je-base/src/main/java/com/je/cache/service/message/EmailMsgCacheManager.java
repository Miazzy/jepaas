package com.je.cache.service.message;

import com.je.cache.service.EhcacheManager;
import com.je.core.util.bean.DynaBean;

public class EmailMsgCacheManager {
    /**
     * 获取缓存值
     * @param key 缓存键
     * @return
     */
    public static DynaBean getCacheValue(String key){
        String cacheName="emailMsgCache";
        return (DynaBean) EhcacheManager.getCacheValue(cacheName, key);
    }
    /**
     * 添加缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    public static void putCache(String key,DynaBean value){
        String cacheName="emailMsgCache";
        EhcacheManager.putCache(cacheName, key, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="emailMsgCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param key 缓存键
     */
    public static void removeCache(String key){
        String cacheName="emailMsgCache";
        EhcacheManager.removeCache(cacheName, key);
    }
}

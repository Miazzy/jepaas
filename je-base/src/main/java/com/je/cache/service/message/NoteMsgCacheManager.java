package com.je.cache.service.message;

import com.je.cache.service.EhcacheManager;

public class NoteMsgCacheManager {
    /**
     * 获取缓存值
     * @param key 缓存键
     * @return
     */
    public static String getCacheValue(String key){
        String cacheName="noteMsgCache";
        return (String) EhcacheManager.getCacheValue(cacheName, key);
    }
    /**
     * 添加缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    public static void putCache(String key,String value){
        String cacheName="noteMsgCache";
        EhcacheManager.putCache(cacheName, key, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="noteMsgCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param key 缓存键
     */
    public static void removeCache(String key){
        String cacheName="noteMsgCache";
        EhcacheManager.removeCache(cacheName, key);
    }
}

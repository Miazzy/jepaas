package com.je.cache.service.config;

import com.je.cache.service.EhcacheManager;

import java.util.Map;

/**
 * SAAS变量缓存
 */
public class SaasCacheManager {
    /**
     * 获取缓存值
     * @param key 缓存键
     * @return
     */
    public static Map<String,Object> getCacheValue(String key){
        String cacheName="saasConfigCache";
        return (Map<String,Object>) EhcacheManager.getCacheValue(cacheName, key);
    }
    /**
     * 添加缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    public static void putCache(String key,Map<String,Object> value){
        String cacheName="saasConfigCache";
        EhcacheManager.putCache(cacheName, key, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="saasConfigCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param key 缓存键
     */
    public static void removeCache(String key){
        String cacheName="saasConfigCache";
        EhcacheManager.removeCache(cacheName, key);
    }
}

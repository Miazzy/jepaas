package com.je.cache.service.config;

import com.je.cache.service.EhcacheManager;

import java.util.Map;

/**
 * 后台变量缓存
 */
public class BackCacheManager {
    /**
     * 获取缓存值
     * @param key 键
     * @return
     */
    public static String getCacheValue(String key){
        String cacheName="backConfigCache";
        return (String) EhcacheManager.getCacheValue(cacheName, key);
    }
    /**
     * 添加缓存
     * @param key 键
     * @param value 缓存值
     */
    public static void putCache(String key,String value){
        String cacheName="backConfigCache";
        EhcacheManager.putCache(cacheName, key, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="backConfigCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param key 缓存键
     */
    public static void removeCache(String key){
        String cacheName="backConfigCache";
        EhcacheManager.removeCache(cacheName, key);
    }

    /**
     * 获取所有cache值
     * @return
     */
    public static Map<String,Object> getCacheValues(){
        String cacheName="backConfigCache";
        return EhcacheManager.getAllCache(cacheName);
    }
}

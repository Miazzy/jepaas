package com.je.cache.service.rbac;

import com.je.cache.service.EhcacheManager;

import java.util.List;

public class LoginCheckUserCacheManager {
    /**
     * 获取缓存值
     * @param key 缓存键
     * @return
     */
    public static List<String> getCacheValue(String key){
        String cacheName="loginCheckUserCache";
        return (List<String>) EhcacheManager.getCacheValue(cacheName, key);
    }
    /**
     * 添加缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    public static void putCache(String key,List<String> value){
        String cacheName="loginCheckUserCache";
        EhcacheManager.putCache(cacheName, key, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="loginCheckUserCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param key 缓存键
     */
    public static void removeCache(String key){
        String cacheName="loginCheckUserCache";
        EhcacheManager.removeCache(cacheName, key);
    }
}

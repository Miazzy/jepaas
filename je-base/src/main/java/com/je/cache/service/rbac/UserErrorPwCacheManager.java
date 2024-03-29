package com.je.cache.service.rbac;

import com.je.cache.service.EhcacheManager;

import java.util.Map;

/**
 * 存放登录用户错误密码次数
 */
public class UserErrorPwCacheManager {
    /**
     * 获取缓存值
     * @param key 缓存键
     * @return
     */
    public static Integer getCacheValue(String key){
        String cacheName="userErrorPwCache";
        return (Integer) EhcacheManager.getCacheValue(cacheName, key);
    }
    /**
     * 添加缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    public static void putCache(String key,Integer value){
        String cacheName="userErrorPwCache";
        EhcacheManager.putCache(cacheName, key, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="userErrorPwCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param key 缓存键
     */
    public static void removeCache(String key){
        String cacheName="userErrorPwCache";
        EhcacheManager.removeCache(cacheName, key);
    }

    /**
     * 获取所有cache值
     * @return
     */
    public static Map<String,Object> getCacheValues(){
        String cacheName="userErrorPwCache";
        return EhcacheManager.getAllCache(cacheName);
    }
}

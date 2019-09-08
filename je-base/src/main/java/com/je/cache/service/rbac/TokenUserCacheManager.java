package com.je.cache.service.rbac;

import com.je.cache.service.EhcacheManager;
import com.je.rbac.model.EndUser;

import java.util.Map;

/**
 * 存放登录用户
 */
public class TokenUserCacheManager {
    /**
     * 获取缓存值
     * @param key 缓存键
     * @return
     */
    public static EndUser getCacheValue(String key){
        String cacheName="tokenUserCache";
        return (EndUser) EhcacheManager.getCacheValue(cacheName, key);
    }
    /**
     * 添加缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    public static void putCache(String key, EndUser value){
        String cacheName="tokenUserCache";
        EhcacheManager.putCache(cacheName, key, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="tokenUserCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param key 缓存键
     */
    public static void removeCache(String key){
        String cacheName="tokenUserCache";
        EhcacheManager.removeCache(cacheName, key);
    }

    /**
     * 获取所有cache值
     * @return
     */
    public static Map<String,Object> getCacheValues(){
        String cacheName="tokenUserCache";
        return EhcacheManager.getAllCache(cacheName);
    }
}

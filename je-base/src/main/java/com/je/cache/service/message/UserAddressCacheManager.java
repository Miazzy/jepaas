package com.je.cache.service.message;

import com.je.cache.service.EhcacheManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户通讯录
 */
public class UserAddressCacheManager {
    /**
     * 获取缓存值
     * @param key 缓存键
     * @return
     */
    public static HashMap<String,String> getCacheValue(String key){
        String cacheName="userAddressMsgCache";
        return (HashMap<String,String>) EhcacheManager.getCacheValue(cacheName, key);
    }
    /**
     * 添加缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    public static void putCache(String key,HashMap<String,String> value){
        String cacheName="userAddressMsgCache";
        EhcacheManager.putCache(cacheName, key, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="userAddressMsgCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param key 缓存键
     */
    public static void removeCache(String key){
        String cacheName="userAddressMsgCache";
        EhcacheManager.removeCache(cacheName, key);
    }

    /**
     * 获取所有cache值
     * @return
     */
    public static Map<String,Object> getCacheValues(){
        String cacheName="userAddressMsgCache";
        return EhcacheManager.getAllCache(cacheName);
    }
}

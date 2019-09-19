package com.je.cache.service.jms;

import com.je.cache.service.EhcacheManager;
import com.je.jms.vo.JmsChannel;

import java.util.Map;

/**
 * 讨论组通道
 */
public class GroupChannelCacheManager {
    /**
     * 获取缓存值
     * @param key 缓存键
     * @return
     */
    public static JmsChannel getCacheValue(String key){
        String cacheName="groupChannelJmsCache";
        return (JmsChannel) EhcacheManager.getCacheValue(cacheName, key);
    }
    /**
     * 添加缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    public static void putCache(String key,JmsChannel value){
        String cacheName="groupChannelJmsCache";
        EhcacheManager.putCache(cacheName, key, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="groupChannelJmsCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param key
     */
    public static void removeCache(String key){
        String cacheName="groupChannelJmsCache";
        EhcacheManager.removeCache(cacheName, key);
    }

    /**
     * 获取所有cache值
     * @return
     */
    public static Map<String,Object> getCacheValues(){
        String cacheName="historyStackJmsCache";
        return EhcacheManager.getAllCache(cacheName);
    }
}

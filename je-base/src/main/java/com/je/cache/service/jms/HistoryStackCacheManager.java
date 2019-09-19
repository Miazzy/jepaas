package com.je.cache.service.jms;

import com.je.cache.service.EhcacheManager;
import com.je.core.util.bean.DynaBean;

import java.util.List;
import java.util.Map;

/**
 * 消息记录堆栈
 */
public class HistoryStackCacheManager {
    /**
     * 获取缓存值
     * @param key 缓存键
     * @return
     */
    public static DynaBean getCacheValue(String key){
        String cacheName="historyStackJmsCache";
        return (DynaBean) EhcacheManager.getCacheValue(cacheName, key);
    }
    /**
     * 添加缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    public static void putCache(String key,DynaBean value){
        String cacheName="historyStackJmsCache";
        EhcacheManager.putCache(cacheName, key, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="historyStackJmsCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param key 缓存键
     */
    public static void removeCache(String key){
        String cacheName="historyStackJmsCache";
        EhcacheManager.removeCache(cacheName, key);
    }
    /**
     * 获取所有cache值
     * @return
     */
    public static List getKeys(){
        String cacheName="historyStackJmsCache";
        return EhcacheManager.getKeys(cacheName);
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

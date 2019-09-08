package com.je.cache.service.message;

import com.je.cache.service.EhcacheManager;
import com.je.message.vo.app.MsgConfigVo;

import java.util.Map;

/**
 * 个推配置缓存
 */
public class GtConfigCacheManager {
    /**
     * 获取缓存值
     * @param key 缓存键
     * @return
     */
    public static MsgConfigVo getCacheValue(String key){
        String cacheName="gtConfigMsgCache";
        return (MsgConfigVo) EhcacheManager.getCacheValue(cacheName, key);
    }
    /**
     * 添加缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    public static void putCache(String key,MsgConfigVo value){
        String cacheName="gtConfigMsgCache";
        EhcacheManager.putCache(cacheName, key, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="gtConfigMsgCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param key 缓存键
     */
    public static void removeCache(String key){
        String cacheName="gtConfigMsgCache";
        EhcacheManager.removeCache(cacheName, key);
    }

    /**
     * 获取所有cache值
     * @return
     */
    public static Map<String,Object> getCacheValues(){
        String cacheName="gtConfigMsgCache";
        return EhcacheManager.getAllCache(cacheName);
    }
}

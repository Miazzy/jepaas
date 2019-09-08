package com.je.cache.service.doc;

import com.je.cache.service.EhcacheManager;
import com.je.core.util.bean.DynaBean;

public class DocumentCacheManager {
    /**
     * 获取缓存值
     * @param key 缓存键
     * @return
     */
    public static DynaBean getCacheValue(String key){
        String cacheName="documentCache";
        return (DynaBean) EhcacheManager.getCacheValue(cacheName, key);

    }
    /**
     * 添加缓存
     * @param code 缓存code
     * @param value 缓存值
     */
    public static void putCache(String code, DynaBean value){
        String cacheName="documentCache";
        EhcacheManager.putCache(cacheName, code, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="documentCache";
        EhcacheManager.clearAllCache(cacheName);
    }

    /**
     * 清空指定的缓存
     * @param code 缓存code
     */
    public static void removeCache(String code){
        String cacheName="documentCache";
        EhcacheManager.removeCache(cacheName, code);
    }
}

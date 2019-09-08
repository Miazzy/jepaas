package com.je.cache.service.rbac;

import com.je.cache.service.EhcacheManager;
import com.je.core.util.bean.DynaBean;

import java.util.List;

/**
 * 用户首页信息
 */
public class UserHomeCacheManager {
    /**
     * 获取缓存值
     * @param userId 用户id
     * @return
     */
    public static List<DynaBean> getCacheValue(String userId){
        String cacheName="userHomeCache";
        return (List<DynaBean>) EhcacheManager.getCacheValue(cacheName, userId);
    }
    /**
     * 添加缓存
     * @param userId 用户id
     * @param value 值
     */
    public static void putCache(String userId, List<DynaBean> value){
        String cacheName="userHomeCache";
        EhcacheManager.putCache(cacheName, userId, value);
    }
    /**
     * 清空所有缓存
     */
    public static void clearAllCache(){
        String cacheName="userHomeCache";
        EhcacheManager.clearAllCache(cacheName);
    }
    /**
     * 清空指定的缓存
     * @param userId 用户id
     */
    public static void removeCache(String userId){
        String cacheName="userHomeCache";
        EhcacheManager.removeCache(cacheName, userId);
    }
}

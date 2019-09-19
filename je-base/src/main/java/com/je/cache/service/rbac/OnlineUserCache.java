package com.je.cache.service.rbac;

import com.je.cache.service.EhcacheManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class OnlineUserCache {
    public static void put(String key, Date value) {
        if (null == value) {
            value = new Date();
        }
        EhcacheManager.putCache("onlineUserCache", key, value);
    }

    public static Date get(String key) {
        Object value = EhcacheManager.getCacheValue("onlineUserCache", key);
        if (null == value) {
            return null;
        }
        return (Date) value;
    }

    public static Map<String, Object> getAll() {
        return EhcacheManager.getAllCache("onlineUserCache");
    }
    public static List getAllKey() {
        return EhcacheManager.getKeys("onlineUserCache");
    }

    /**
     * 删除KEY 如果KEY还存在则表示删除失败
     *
     * @param key 缓存键
     * @return
     */
    public static void deleteKey(String key) {
        EhcacheManager.removeCache("onlineUserCache", key);
    }
}

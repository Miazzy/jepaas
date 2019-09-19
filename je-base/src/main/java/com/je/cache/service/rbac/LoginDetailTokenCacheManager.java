package com.je.cache.service.rbac;

import com.google.common.collect.Lists;
import com.je.cache.service.EhcacheManager;

import java.util.List;
import java.util.Map;

/**
 * 存放登陆token和用户登录的方式信息
 */
public class LoginDetailTokenCacheManager {
    public static void put(String key, List<Map<String, String>> value) {
        if (null == value) {
            value = Lists.newArrayList();
        }
        EhcacheManager.putCache("loginDetailTokenCache", key, value);
    }

    public static List<Map<String, String>> get(String key) {
        Object value = EhcacheManager.getCacheValue("loginDetailTokenCache", key);
        if (null == value) {
            return null;
        }
        return (List<Map<String, String>>) value;
    }

    public static Map<String, Object> getAll() {
        return EhcacheManager.getAllCache("loginDetailTokenCache");
    }

    public static List getAllKey() {
        return EhcacheManager.getKeys("loginDetailTokenCache");
    }

    /**
     * 删除KEY 如果KEY还存在则表示删除失败
     * @param key 缓存键
     * @return
     */
    public static void deleteKey(String key) {
        EhcacheManager.removeCache("loginDetailTokenCache", key);
    }
}

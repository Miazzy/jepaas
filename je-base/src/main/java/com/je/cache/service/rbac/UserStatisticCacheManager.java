package com.je.cache.service.rbac;

import com.je.saas.vo.UserStatisticVo;
import com.je.cache.service.EhcacheManager;

import java.util.List;
import java.util.Map;

public class UserStatisticCacheManager {
    /**
     * @Description //TODO
     * @Date  2019/7/4
     * @Param key:
     * @Param value:
     * @return: void
    **/
    public static void put(String key, UserStatisticVo value) {
        EhcacheManager.putCache("UserStatisticCache", key, value);
    }

    /**
     * @Description //TODO
     * @Date  2019/7/4
     * @Param key:
     * @return: com.je.saas.vo.UserStatisticVo
    **/
    public static UserStatisticVo get(String key) {
        Object value = EhcacheManager.getCacheValue("UserStatisticCache", key);
        if (null == value) {
            return null;
        }
        return (UserStatisticVo) value;
    }

    /**
     * @Description //TODO
     * @Date  2019/7/4
     * @return: java.util.Map<java.lang.String,java.lang.Object>
    **/
    public static Map<String, Object> getAll() {
        return EhcacheManager.getAllCache("UserStatisticCache");
    }

    /**
     * @Description //TODO
     * @Date  2019/7/4
     * @return: java.util.List
    **/
        public static List getAllKey() {
        return EhcacheManager.getKeys("UserStatisticCache");
    }

    /**
     * 删除KEY 如果KEY还存在则表示删除失败
     * @param key 用户id
     * @return
     */
    public static void deleteCacheByKey(String key) {
        EhcacheManager.removeCache("UserStatisticCache", key);
    }
}

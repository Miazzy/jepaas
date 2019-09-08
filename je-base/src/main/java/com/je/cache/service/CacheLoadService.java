package com.je.cache.service;

import com.alibaba.fastjson.JSONArray;
import com.je.core.util.bean.DynaBean;

import javax.servlet.http.HttpServletRequest;

/**
 *  缓存的执行
 */
public interface CacheLoadService {
    /**
     * 执行缓存预加载
     * @param request TODO未处理
     * @param staticizeFuncs TODO未处理
     */
    public void doCache(HttpServletRequest request, JSONArray staticizeFuncs);
    /**
     * 执行缓存预加载
     * @param request TODO未处理
     * @param staticizeFuncs TODO未处理
     */
    public void doUserCache(HttpServletRequest request, JSONArray staticizeFuncs);

    /**
     * 执行共有缓存，即不分租户，系统只有一套的缓存
     * @param cacheLoad TODO未处理
     */
    public void doPublicCache(DynaBean cacheLoad);
    /**
     *  执行以租户为主体的缓存。 即按照租户隔离的缓存
     * @param cacheLoad TODO未处理
     * @param zhId TODO未处理
     * @param zhMc TODO未处理
     * @param staticizeFuncs TODO未处理
     */
    public void doZhCache(DynaBean cacheLoad, String zhId, String zhMc, JSONArray staticizeFuncs);
    /**
     * 执行以人员为主体的缓存。 即按照租户下人员隔离的缓存
     * @param cacheLoad TODO未处理
     * @param zhId TODO未处理
     * @param zhMc TODO未处理
     */
    public void doZhUserCache(DynaBean cacheLoad, String zhId, String zhMc);

    /**
     * 缓存执行成功
     * @param type 类型
     * @param name name
     * @param code code
     * @param zhId TODO
     * @param zhMc TODO
     * @param userId 用户id
     * @param userName 用户name
     */
    public DynaBean doCacheLog(String cacheLoadPkValue, String type, String name, String code, String zhId, String zhMc, String userId, String userName, String result, String exStr);
}

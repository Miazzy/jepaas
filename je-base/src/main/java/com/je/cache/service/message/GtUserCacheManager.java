package com.je.cache.service.message;

import com.je.cache.service.EhcacheManager;
import com.je.message.vo.app.UserAppInfo;

import java.util.List;

/**
 * 工作流缓存
 * @author zhangshuaipeng
 *
 */
public class GtUserCacheManager {
	/**
	 * 获取缓存值
	 * @param key 缓存键
	 * @return
	 */
	public static List<UserAppInfo> getCacheValue(String key){
		String cacheName="gtUserCache";
		return (List<UserAppInfo>) EhcacheManager.getCacheValue(cacheName, key);
	}
	/**
	 * 添加缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	public static void putCache(String key,List<UserAppInfo> value){
		String cacheName="gtUserCache";
		EhcacheManager.putCache(cacheName, key, value);
	}
	/**
	 * 清空所有缓存
	 */
	public static void clearAllCache(){
		String cacheName="gtUserCache";
		EhcacheManager.clearAllCache(cacheName);
	}
	/**
	 * 清空指定的缓存
	 * @param key 缓存值
	 */
	public static void removeCache(String key){
		String cacheName="gtUserCache";
		EhcacheManager.removeCache(cacheName, key);
	}
}

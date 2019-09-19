package com.je.cache.service.rbac;

import com.je.cache.service.EhcacheManager;
import com.je.rbac.model.UserLeader;

public class UserLeaderCacheManager {
	/**
	 * 获取缓存值
	 * @param key 用户id
	 * @return
	 */
	public static UserLeader getCacheValue(String key){
		String cacheName="userLeaderCache";
		return (UserLeader) EhcacheManager.getCacheValue(cacheName, key);
	}
	/**
	 * 添加缓存
	 * @param key 用户id
	 * @param value 值
	 */
	public static void putCache(String key, UserLeader value){
		String cacheName="userLeaderCache";
		EhcacheManager.putCache(cacheName, key, value);
	}
	/**
	 * 清空所有缓存
	 */
	public static void clearAllCache(){
		String cacheName="userLeaderCache";
		EhcacheManager.clearAllCache(cacheName);
	}
	/**
	 * 清空指定的缓存
	 * @param key 用户id
	 */
	public static void removeCache(String key){
		String cacheName="userLeaderCache";
		EhcacheManager.removeCache(cacheName, key);
	}
}

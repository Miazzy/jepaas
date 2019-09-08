package com.je.cache.service.rbac;

import com.je.cache.service.EhcacheManager;
import com.je.rbac.model.UserLeader;

public class UserBranchCacheManager {
	/**
	 * 获取缓存值
	 * @param key 缓存键
	 * @return
	 */
	public static UserLeader getCacheValue(String key){
		String cacheName="userBranchCache";
		return (UserLeader) EhcacheManager.getCacheValue(cacheName, key);
	}
	/**
	 * 添加缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	public static void putCache(String key, UserLeader value){
		String cacheName="userBranchCache";
		EhcacheManager.putCache(cacheName, key, value);
	}
	/**
	 * 清空所有缓存
	 */
	public static void clearAllCache(){
		String cacheName="userBranchCache";
		EhcacheManager.clearAllCache(cacheName);
	}
	/**
	 * 清空指定的缓存
	 * @param key 缓存键
	 */
	public static void removeCache(String key){
		String cacheName="userBranchCache";
		EhcacheManager.removeCache(cacheName, key);
	}
}

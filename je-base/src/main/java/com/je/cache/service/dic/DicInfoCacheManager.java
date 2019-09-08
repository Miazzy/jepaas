package com.je.cache.service.dic;

import com.je.cache.service.EhcacheManager;
import com.je.core.util.bean.DynaBean;
/**
 * 数据字典信息缓存
 * @author marico
 *
 */
public class DicInfoCacheManager {
	/**
	 * 获取缓存值
	 * @param key 缓存键
	 * @return
	 */
	public static DynaBean getCacheValue(String key){
		String cacheName="dicInfoCache";
		return (DynaBean) EhcacheManager.getCacheValue(cacheName, key);
	}
	/**
	 * 添加缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	public static void putCache(String key,DynaBean value,String zhId){
		String cacheName="dicInfoCache";
		EhcacheManager.putCache(cacheName, key, value,zhId);
	}
	/**
	 * 添加缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	public static void putCache(String key,DynaBean value){
		String cacheName="dicInfoCache";
		EhcacheManager.putCache(cacheName, key, value);
	}
	/**
	 * 清空所有缓存
	 */
	public static void clearAllCache(){
		String cacheName="dicInfoCache";
		EhcacheManager.clearAllCache(cacheName);
	}
	/**
	 * 清空指定的缓存
	 * @param key 缓存键
	 */
	public static void removeCache(String key){
		String cacheName="dicInfoCache";
		EhcacheManager.removeCache(cacheName, key);
		
	}
}

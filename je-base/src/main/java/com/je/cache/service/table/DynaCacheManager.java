package com.je.cache.service.table;


import com.je.cache.service.EhcacheManager;

/**
 * 前台model缓存
 * @author zhangshuaipeng
 *
 */
public class DynaCacheManager {
	/**
	 * 获取缓存值
	 * @param key
	 * @return
	 */
	public static String getCacheValue(String tableName){
		String cacheName="dynaCache";
		return (String) EhcacheManager.getCacheValue(cacheName, tableName);
	}
	/**
	 * 添加缓存
	 * @param key
	 * @param value
	 */
	public static void putCache(String tableName,String value){
		String cacheName="dynaCache";
		EhcacheManager.putCache(cacheName, tableName, value);
	}
	/**
	 * 清空所有缓存
	 */
	public static void clearAllCache(){
		String cacheName="dynaCache";
		EhcacheManager.clearAllCache(cacheName);
	}
	/**
	 * 清空指定的缓存
	 * @param key
	 */
	public static void removeCache(String tableName){
		String cacheName="dynaCache";
		EhcacheManager.removeCache(cacheName, tableName);
	}
}

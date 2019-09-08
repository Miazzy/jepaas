package com.je.cache.service.table;

import com.je.cache.service.EhcacheManager;
import com.je.core.util.bean.DynaBean;


/**
 * 前台model缓存
 * @author zhangshuaipeng
 *
 */
public class TableCacheManager {
	/**
	 * 获取缓存值
	 * @param key
	 * @return
	 */
	public static DynaBean getCacheValue(String tableName){
		String cacheName="tableCache";
		return (DynaBean) EhcacheManager.getCacheValue(cacheName, tableName);
	}
	/**
	 * 添加缓存
	 * @param key
	 * @param value
	 */
	public static void putCache(String tableName, DynaBean value){
		String cacheName="tableCache";
		EhcacheManager.putDiskCache(cacheName, tableName, value);
	}
	/**
	 * 清空所有缓存
	 */
	public static void clearAllCache(){
		String cacheName="tableCache";
		EhcacheManager.clearAllDiskCache(cacheName);
	}
	/**
	 * 清空指定的缓存
	 * @param key
	 */
	public static void removeCache(String tableName){
		String cacheName="tableCache";
		EhcacheManager.removeDiskCache(cacheName, tableName);
	}
}

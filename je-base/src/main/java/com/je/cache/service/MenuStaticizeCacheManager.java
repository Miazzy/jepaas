package com.je.cache.service;

import net.sf.ehcache.Cache;

import com.je.core.entity.extjs.JSONTreeNode;

public class MenuStaticizeCacheManager {

	/**
	 * @Description //获取缓存
	 * @Date  2019/7/4
	 * @return: Cache
	**/
	public static Cache getCache(){
		String cacheName="menuStaticizeCache";
		Cache cache=EhcacheManager.getCache(cacheName);
		return cache;
	}
	/**
	 * 获取缓存值
	 * @param key
	 * @return
	 */
	public static String getCacheValue(String key){
		String cacheName="menuStaticizeCache";
		return (String) EhcacheManager.getCacheValue(cacheName, key);
	}
	/**
	 * 添加缓存
	 * @param key 缓存key
	 * @param value 缓存value
	 */
	public static void putCache(String key,String value){
		String cacheName="menuStaticizeCache";
		EhcacheManager.putCache(cacheName, key, value);
	}
	/**
	 * 清空所有缓存
	 */
	public static void clearAllCache(){
		String cacheName="menuStaticizeCache";
		EhcacheManager.clearAllCache(cacheName);
	}
	/**
	 * 清空指定的缓存
	 * @param key 缓存key
	 */
	public static void removeCache(String key){
		String cacheName="menuStaticizeCache";
		EhcacheManager.removeCache(cacheName, key);
	}
	/**
	 * 获取缓存值
	 * @param key 缓存key
	 * @return
	 */
	public static JSONTreeNode getTreeCacheValue(String key){
		String cacheName="menuStaticizeCache";
		return (JSONTreeNode)EhcacheManager.getCacheValue(cacheName, key);
	}
	/**
	 * 添加缓存
	 * @param key 缓存key
	 * @param value 缓存value
	 */
	public static void putTreeCache(String key,JSONTreeNode value){
		String cacheName="menuStaticizeCache";
		EhcacheManager.putCache(cacheName, key, value);
	}
}

package com.je.cache.service.dic;

import com.je.cache.service.EhcacheManager;
import com.je.core.entity.extjs.JSONTreeNode;
/**
 * 数据字典缓存
 * @author marico
 *
 */
public class DicCacheManager {
	/**
	 * 获取缓存值
	 * @param key 缓存键
	 * @return
	 */
	public static String getCacheValue(String key){
		String cacheName="dicCache";
		return (String) EhcacheManager.getCacheValue(cacheName, key);
	}
	/**
	 * 添加缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	public static void putCache(String key,String value){
		String cacheName="dicCache";
		EhcacheManager.putCache(cacheName, key, value);
	}
	/**
	 * 添加缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	public static void putCache(String key,String value,String zhId){
		String cacheName="dicCache";
		EhcacheManager.putCache(cacheName, key, value,zhId);
	}
	/**
	 * 清空所有缓存
	 */
	public static void clearAllCache(){
		String cacheName="dicCache";
		EhcacheManager.clearAllCache(cacheName);
	}
	/**
	 * 清空指定的缓存
	 * @param key 缓存键
	 */
	public static void removeCache(String key){
		String cacheName="dicCache";
		EhcacheManager.removeCache(cacheName, key);
	}
	public static void removeLikeCache(String key){
		String cacheName="dicCache";
		EhcacheManager.removeLikeCache(cacheName, key);
	}
	/**
	 * 获取缓存值
	 * @param key 缓存键
	 * @return
	 */
	public static JSONTreeNode getTreeCacheValue(String key){
		String cacheName="dicCache";
		return (JSONTreeNode)EhcacheManager.getCacheValue(cacheName, key);
	}
	/**
	 * 添加缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	public static void putTreeCache(String key,JSONTreeNode value){
		String cacheName="dicCache";
		EhcacheManager.putCache(cacheName, key, value);
	}
}

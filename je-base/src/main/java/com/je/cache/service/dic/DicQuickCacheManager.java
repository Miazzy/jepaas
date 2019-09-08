package com.je.cache.service.dic;

import java.util.List;

import com.je.cache.service.EhcacheManager;
import com.je.core.entity.extjs.JSONTreeNode;
/**
 * 数据字典缓存
 * @author marico
 *
 */
public class DicQuickCacheManager {
	/**
	 * 获取缓存值
	 * @param key 缓存键
	 * @return
	 */
	public static List<JSONTreeNode> getCacheValue(String key){
		String cacheName="dicQuickCache";
		return (List<JSONTreeNode>) EhcacheManager.getCacheValue(cacheName, key);
	}
	/**
	 * 添加缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	public static void putCache(String key,List<JSONTreeNode> value){
		String cacheName="dicQuickCache";
//		List<JSONTreeNode> nodes=new ArrayList<JSONTreeNode>();
//		for(JSONTreeNode n:value){
//			nodes.add(n.clone());
//		}
		EhcacheManager.putCache(cacheName, key, value);
	}
	/**
	 * 添加缓存
	 * @param key  缓存键
	 * @param value 缓存值
	 */
	public static void putCache(String key,List<JSONTreeNode> value,String zhId){
		String cacheName="dicQuickCache";
//		List<JSONTreeNode> nodes=new ArrayList<JSONTreeNode>();
//		for(JSONTreeNode n:value){
//			nodes.add(n.clone());
//		}
		EhcacheManager.putCache(cacheName, key, value);
	}
	/**
	 * 清空所有缓存
	 */
	public static void clearAllCache(){
		String cacheName="dicQuickCache";
		EhcacheManager.clearAllCache(cacheName);
	}
	/**
	 * 清空指定的缓存
	 * @param key 缓存键
	 */
	public static void removeCache(String key){
		String cacheName="dicQuickCache";
		EhcacheManager.removeCache(cacheName, key);
	}
	/**
	 * 清空指定的缓存
	 * @param key 缓存键
	 */
	public static void removeLikeCache(String key){
		String cacheName="dicQuickCache";
		EhcacheManager.removeLikeCache(cacheName, key);
	}
	/**
	 * 添加缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	public static void putTreeCache(String key,JSONTreeNode value){
		String cacheName="dicQuickCache";
		EhcacheManager.putCache(cacheName, key, value);
	}
}

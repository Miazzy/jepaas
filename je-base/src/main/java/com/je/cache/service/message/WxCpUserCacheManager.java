package com.je.cache.service.message;

import java.util.List;

import com.je.cache.service.EhcacheManager;
import com.je.core.util.bean.DynaBean;

public class WxCpUserCacheManager {
	/**
	 * 获取缓存值
	 * @param key 缓存键
	 * @return
	 */
	public static List<DynaBean> getCacheValue(String key){
		String cacheName="wxCpUserMsgCache";
		return (List<DynaBean>) EhcacheManager.getCacheValue(cacheName, key);
	}
	/**
	 * 添加缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	public static void putCache(String key,List<DynaBean> value){
		String cacheName="wxCpUserMsgCache";
		EhcacheManager.putCache(cacheName, key, value);
	}
	/**
	 * 清空所有缓存
	 */
	public static void clearAllCache(){
		String cacheName="wxCpUserMsgCache";
		EhcacheManager.clearAllCache(cacheName);
	}
	/**
	 * 清空指定的缓存
	 * @param pdid TODO
	 */
	public static void removeCache(String pdid){
		String cacheName="wxCpUserMsgCache";
		EhcacheManager.removeCache(cacheName, pdid);
	}
}

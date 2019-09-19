package com.je.cache.service.doc;

import com.je.cache.service.EhcacheManager;
import com.je.document.vo.FileType;
/**
 * 文件类型工具类
 * @author zhangshuaipeng
 *
 */
public class FileTypeCacheManager {
	/**
	 * 获取缓存值
	 * @param key 缓存键
	 * @return
	 */
	public static FileType getCacheValue(String key){
		String cacheName="fileTypeCache";
		return (FileType) EhcacheManager.getCacheValue(cacheName, key);
		
	}

	/**
	 * @Description //获取默认值
	 * @Date  2019/7/4
	 * @return: com.je.document.vo.FileType
	**/
	public static FileType getDefaultValue(){
		FileType fileType=new FileType();
		fileType.setName("未知类型");
		fileType.setIconCls("fa fa-file");
		return fileType;
	}
	/**
	 * 添加缓存
	 * @param code 缓存键
	 * @param value 缓存值
	 */
	public static void putCache(String code,FileType value){
		String cacheName="fileTypeCache";
		EhcacheManager.putCache(cacheName, code, value);
	}
	/**
	 * 清空所有缓存
	 */
	public static void clearAllCache(){
		String cacheName="fileTypeCache";
		EhcacheManager.clearAllCache(cacheName);
	}
	/**
	 * 清空指定的缓存
	 * @param code 缓存键
	 */
	public static void removeCache(String code){
		String cacheName="fileTypeCache";
		EhcacheManager.removeCache(cacheName, code);
	}
}

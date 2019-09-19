package com.je.cache.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.je.cache.redis.RedisCache;
import com.je.cache.redis.service.RedisServiceManager;
import com.je.cluster.ClusterBooter;
import com.je.cache.model.ClusterCacheElement;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.util.SecurityUserHolder;
import com.je.core.util.SpringContextHolder;
import com.je.core.util.StringUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description //TODO
 * @Date  2019/7/11
 * @Param null:
 * @return: null
**/
public class EhcacheManager {
	private final static Logger logger = LoggerFactory.getLogger(EhcacheManager.class);
	private static CacheManager cacheManager;
	private static RedisServiceManager redisServiceManager;
	private static RedisCache redisCache;

	static {
		System.setProperty("net.sf.ehcache.enableShutdownHook", "true");
		cacheManager = CacheManager.getInstance();
	}


	/**
	 * @Description //获取缓存
	 * @Date  2019/7/4
	 * @Param cacheName: 缓存name
	 * @return: Cache
	**/
	public static Cache getCache(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		return cache;
	}
	/**
	 * @param cacheName
	 * @param key
	 * @return
	 * @description 获取缓存的信息
	 * @author qiaoshipeng
	 */
	public static Object getCacheValue(String cacheName, String key){
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			return null;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return null;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		//是否只是用key-value的形式存储
		if (cacheConfiguration.isCommonRedisCache()) {
			if (redisCache == null) {
				redisCache = SpringContextHolder.getBean("redisCache");
			}
			redisCache.expire(cacheName + "_" + key, cacheConfiguration.getCommonRedisTimeOut());
			return redisCache.get(cacheName + "_" + key);
		}
		//是否启用saas,如果启用saas那么,会把租户的id拼接起来
		if (cacheConfiguration.isUseSaas()) {
			String zhId = SecurityUserHolder.getCurrentUser().getZhId();
			key = zhId + "_" + key;
		}
		//首先从一级缓存获取value
		Object result = null;
		if (cacheConfiguration.isUseOneLevelCache()) {
			Element element = cache.get(key);
			if (element != null) {
				result = element.getObjectValue();
			}
			if (null != result) {
				return result;
			}
		}
		//从二级缓存获取
		if (cacheConfiguration.isUseTwoLevelCache()) {
			if (redisServiceManager == null) {
				redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
			}
			result = redisServiceManager.getObjectFromSecondLevelCache(cacheName, key);
			//如果二级缓存不是空的,那么把二级缓存的内容,放入到一级缓存中
			if (null != result) {
				if (cacheConfiguration.isUseOneLevelCache()) {
					Element element = new Element(key, result);
					cache.put(element);
					cache.flushDisk();
					cache.flush();
				}
			}
		}
		return result;
	}
	/**
	 * @param cacheName 缓存名称
	 * @param key 缓存键
	 * @return
	 * @description 获取缓存的信息
	 * @author qiaoshipeng
	 */
	public static Object getCacheValue(String cacheName, String key,String zhId) {
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			return null;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return null;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		//是否只是用key-value的形式存储
		if (cacheConfiguration.isCommonRedisCache()) {
			if (redisCache == null) {
				redisCache = SpringContextHolder.getBean("redisCache");
			}
			redisCache.expire(cacheName + "_" + key, cacheConfiguration.getCommonRedisTimeOut());
			return redisCache.get(cacheName + "_" + key);
		}
		//是否启用saas,如果启用saas那么,会把租户的id拼接起来
		if (cacheConfiguration.isUseSaas()) {
			if(StringUtil.isEmpty(zhId)) {
				zhId = SecurityUserHolder.getCurrentUser().getZhId();
			}
			key = zhId + "_" + key;
		}
		//首先从一级缓存获取value
		Object result = null;
		if (cacheConfiguration.isUseOneLevelCache()) {
			Element element = cache.get(key);
			if (element != null) {
				result = element.getObjectValue();
			}
			if (null != result) {
				return result;
			}
		}
		//从二级缓存获取
		if (cacheConfiguration.isUseTwoLevelCache()) {
			if (redisServiceManager == null) {
				redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
			}
			result = redisServiceManager.getObjectFromSecondLevelCache(cacheName, key);
			//如果二级缓存不是空的,那么把二级缓存的内容,放入到一级缓存中
			if (null != result) {
				if (cacheConfiguration.isUseOneLevelCache()) {
					Element element = new Element(key, result);
					cache.put(element);
					cache.flushDisk();
					cache.flush();
				}
			}
		}
		return result;
	}
	/**
	 * @param cacheName 缓存名称
	 * @param key 缓存键
	 * @return
	 * @description 获取缓存的信息
	 * @author qiaoshipeng
	 */
	public static Object getTestCacheValue(String type,String cacheName, String key) {
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			logger.info("缓存中的cacheName或者key为空,cacheName:{},key:{}",cacheName,key);
			return null;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			logger.info("缓存配置文件没有找到");
			return null;
		}
		if (StringUtil.isEmpty(type) || "1".equals(type)){
			Element element = cache.get(key);
			return element.getObjectValue();
		}else{
			if (redisServiceManager == null) {
				redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
			}
			return redisServiceManager.getObjectFromSecondLevelCache(cacheName, key);
		}
	}

	/**
	 * TODO未处理
	 * @param list
	 * @return
	 */
	private static boolean isEmpty(Collection list) {
		if (null == list || list.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * TODO未处理
	 * @param map
	 * @return
	 */
	private static boolean isEmpty(Map map) {
		if (null == map || map.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param cacheName 缓存的名字
	 * @return
	 * @description 获取缓存中的keys, 注意:key_value形式的信息不能调用该方法
	 * @author qiaoshipeng
	 */
	public static List getKeys(String cacheName) {
		List list = Lists.newArrayList();
		if (StringUtils.isEmpty(cacheName)) {
			return Lists.newArrayList();
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return Lists.newArrayList();
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		//首先从一级缓存获取keys
		if (cacheConfiguration.isUseOneLevelCache()) {
			list = cache.getKeys();
		}
		//从二级缓存获取keys
		if (isEmpty(list) && cacheConfiguration.isUseTwoLevelCache()) {
			Set<Object> map = redisCache.getHashKeys(cacheName);
			if (null == map || map.isEmpty()) {
				return Lists.newArrayList();
			} else {
				list = Lists.newArrayList(map);
			}
		}

		if (cacheConfiguration.isUseSaas()) {
			List resultList = Lists.newArrayList();
			String zhId = SecurityUserHolder.getCurrentUser().getZhId();
			for (Object o : list) {
				String k = (String) o;
				if (k.indexOf(zhId) >= 0) {
					resultList.add(k.replaceFirst(zhId + "_", ""));
				}
			}
			return resultList;
		}
		return list;
	}


	/**
	 * @param cacheName 缓存的名字
	 * @return
	 * @description 获取缓存中的信息, 注意:key_value形式的信息不能调用该方法
	 * @author qiaoshipeng
	 */
	public static Map<String, Object> getAllCache(String cacheName) {
		Map<String, Object> cacheValues = Maps.newHashMap();
		if (StringUtils.isEmpty(cacheName)) {
			return cacheValues;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (null == cache) {
			return cacheValues;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		Map<String, Object> oneCacheValues = Maps.newHashMap();
		Map<String, Object> twoCacheValues = Maps.newHashMap();
		//从一级缓存中获取
		if (cacheConfiguration.isUseOneLevelCache()) {
			for (Object keyObj : cache.getKeys()) {
				String key = (String) keyObj;
				Element element = cache.get(key);
				if (element != null) {
					oneCacheValues.put(key, element.getObjectValue());
				}
			}
		}


		//如果启用一级缓存，且从一级缓存拿到的值不为空，则直接返回，否则从二级缓存拿值
		if (cacheConfiguration.isUseTwoLevelCache()) {
			if (redisServiceManager == null) {
				redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
			}
			Long size = redisServiceManager.getHashSize(cacheName);
			//如果二级缓存为空
			if (isEmpty(oneCacheValues) && (null == size || size.longValue() == 0)) {
				return cacheValues;
			}
			if (size.longValue() == oneCacheValues.size()) {
				twoCacheValues = oneCacheValues;
			} else {
				Map<Object, Object> secondCacheValues = redisServiceManager.getCache(cacheName);
				for (Object o : secondCacheValues.keySet()) {
					twoCacheValues.put((String) o, secondCacheValues.get(o));
				}
				if (size.longValue() > oneCacheValues.size()) {
					if (cacheConfiguration.isUseOneLevelCache()) {
						cache.removeAll();
						for (Object o : secondCacheValues.keySet()) {
							String key = (String) o;
							Element element = new Element(key, secondCacheValues.get(o));
							cache.put(element);
						}
						cache.flush();
						cache.flushDisk();
					}
				} else if (size.longValue() < oneCacheValues.size()) {
					logger.error(String.format("获取缓存[%s]一级缓存中的信息比二级缓存还有多,出现异常", cacheName));
				}
			}
		} else {
			twoCacheValues = oneCacheValues;
		}
		//是否启用了租户,如果启用了租户,那么需要把key中的租户替换一下
		if (cacheConfiguration.isUseSaas()) {
			Map<String, Object> resultValues = Maps.newHashMap();
			String zhId = SecurityUserHolder.getCurrentUser().getZhId();
			for (String s : twoCacheValues.keySet()) {
				if (s.startsWith(zhId + "_")) {
					resultValues.put(s.replaceFirst(zhId + "_", ""), twoCacheValues.get(s));
				}
			}
			return resultValues;
		}
		return twoCacheValues;
	}

	/**
	 * @param cacheName 缓存的名字
	 * @param key       缓存key
	 * @param value     缓存value(如果同样的key,那么后面的会把前面的覆盖掉)
	 * @param zhId      租户id
	 * @return
	 * @description 存入缓存
	 * @author qiaoshipeng
	 */
	public static void putCache(String cacheName, String key, Object value, String zhId) {
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		key = zhId + "_" + key;
		//如果启用二级缓存,那么在缓存中添加
		if (cacheConfiguration.isUseTwoLevelCache()) {
			if (redisCache == null) {
				redisCache = SpringContextHolder.getBean("redisCache");
			}
			redisCache.hashPut(cacheName, key, value);
		}
		//如果启用一级缓存,在一级缓存中添加
		if (cacheConfiguration.isUseOneLevelCache()) {
			Element element = new Element(key, value);
			cache.put(element);
			cache.flush();
			cache.flushDisk();
			removeOtherOneLevelCache(cacheName,key);
		}
	}

	/**
	 * 清空其他电脑上的一级缓存:条件(1:启用一级缓存[useOneLevelCache:true],2:启用集群方式[cluster.enable:1])
	 * @param cacheName 缓存名称
	 * @param key 缓存key
	 */
	private static void removeOtherOneLevelCache(String cacheName,String key){
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			return;
		}
		//开始进行其他的一级缓存的remove
		if (redisServiceManager == null) {
			redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
		}
		ClusterCacheElement cacheElement = new ClusterCacheElement(cacheName, key, true);
		try {
			redisServiceManager.sendClusterCacheMessage(cacheElement);
		} catch (IOException e) {
			throw new PlatformException("发送清空一级缓存异常", PlatformExceptionEnum.JE_CORE_CACHE_ERROR,e);
//			logger.warn("发送清空一级缓存异常",e);
//			e.printStackTrace();
		}
	}

	/**
	 * @param cacheName 缓存的名字
	 * @param key       缓存key
	 * @param value     缓存value(如果同样的key,那么后面的会把前面的覆盖掉)
	 * @return
	 * @description 存入缓存
	 * @author qiaoshipeng
	 */
	public static void putCache(String cacheName, String key, Object value) {
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		//是否只用key_value形式存储
		if (cacheConfiguration.isCommonRedisCache()) {
			//这里只用redis做缓存,就不在做后面操作
			if (redisCache == null) {
				redisCache = SpringContextHolder.getBean("redisCache");
			}
			redisCache.put(cacheName + "_" + key, value, cacheConfiguration.getCommonRedisTimeOut());
			return;
		}
		if (cacheConfiguration.isUseSaas()) {
			String zhId = SecurityUserHolder.getCurrentUser().getZhId();
			key = zhId + "_" + key;
		}
		//如果启用二级缓存,那么在缓存中添加
		if (cacheConfiguration.isUseTwoLevelCache()) {
			if (redisCache == null) {
				redisCache = SpringContextHolder.getBean("redisCache");
			}
			redisCache.hashPut(cacheName, key, value);
		}
		//如果启用一级缓存,在一级缓存中添加
		if (cacheConfiguration.isUseOneLevelCache()) {
			Element element = new Element(key, value);
			cache.put(element);
			cache.flush();
			cache.flushDisk();
			//开始进行其他的一级缓存的remove
			removeOtherOneLevelCache(cacheName,key);
		}
	}

	/**
	 * @param cacheName 缓存的名字
	 * @param key       缓存key
	 * @param value     缓存value(如果同样的key,那么后面的会把前面的覆盖掉)
	 * @return
	 * @description 存入缓存
	 * @author qiaoshipeng
	 */
	public static void putDiskCache(String cacheName, String key, Object value) {
		putCache(cacheName, key, value);
	}

	/**
	 * @param cacheName 缓存的名字
	 * @param key       缓存key
	 * @return
	 * @description 移除缓存
	 * @author qiaoshipeng
	 */
	public static void removeCache(String cacheName, String key) {
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		//是否启用key_value形式存储
		if (cacheConfiguration.isCommonRedisCache()) {
			if (redisCache == null) {
				redisCache = SpringContextHolder.getBean("redisCache");
			}
			redisCache.remove(cacheName + "_" + key);
			return;
		}
		//是否启用saas
		if (cacheConfiguration.isUseSaas()) {
			String zhId = SecurityUserHolder.getCurrentUser().getZhId();
			key = zhId + "_" + key;
		}
		//移除二级缓存
		if (ClusterBooter.isClusterEnabled() && cacheConfiguration.isUseCluster() && cacheConfiguration.isClusterRemove()) {
			if (redisServiceManager == null) {
				redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
			}
			ClusterCacheElement cacheElement = new ClusterCacheElement(cacheName, key, true);
			//redis发送消息,移除其他机器上的一级缓存
			redisServiceManager.sendClusterCacheMessage(cacheElement, cacheConfiguration.isUseTwoLevelCache(), cacheConfiguration.isUseOneLevelCache());
		}
		//移除本机一级缓存
		if (cacheConfiguration.isUseOneLevelCache()) {
			cache.remove(key);
			cache.flush();
			cache.flushDisk();
		}

	}

	/**
	 * TODO未处理
	 * @param cacheName
	 * @param key
	 */
	public static void removeDiskCache(String cacheName, String key) {
		removeCache(cacheName, key);
	}

	/**
	 * @param cacheName 缓存的名字
	 * @return
	 * @description 清空整个cache, 注意key_value形式的在这里不能使用
	 * @author qiaoshipeng
	 */
	public static void clearAllCache(String cacheName) {
		if (StringUtils.isEmpty(cacheName)) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		if (cacheConfiguration.isUseSaas() && cacheConfiguration.isRemoveAllSaas() == false) {
			String key = "";
			String zhId = SecurityUserHolder.getCurrentUser().getZhId();
			key = zhId + "_" + key;
			removeLikeCache(cacheName, key);
		} else {
			clearAll(cacheName);
		}

	}

	/**
	 * @param cacheName 缓存的名字
	 * @return
	 * @description 清空整个cache, 注意key_value形式的在这里不能使用
	 * @author qiaoshipeng
	 */
	private static void clearAll(String cacheName) {
		if (StringUtils.isEmpty(cacheName)) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		//清空二级缓存,并发送消息
		if (ClusterBooter.isClusterEnabled() && cacheConfiguration.isUseCluster() && cacheConfiguration.isClusterRemoveAll()) {
			if (redisServiceManager == null) {
				redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
			}
			ClusterCacheElement cacheElement = new ClusterCacheElement(cacheName, true);
			redisServiceManager.sendClusterCacheMessage(cacheElement, cacheConfiguration.isUseTwoLevelCache(), cacheConfiguration.isUseOneLevelCache());
		}
		//清空本地缓存
		if (cacheConfiguration.isUseOneLevelCache()) {
			cache.removeAll();
			cache.flush();
			cache.flushDisk();
		}
	}

	/**
	 * @param cacheName 缓存的名字
	 * @return
	 * @description 清空整个cache, 注意key_value形式的在这里不能使用
	 * @author qiaoshipeng
	 */
	public static void clearAllDiskCache(String cacheName) {
		clearAll(cacheName);
	}

	/**
	 * @param cacheName 缓存的名字
	 * @param keyWord   匹配的缓存key
	 * @return
	 * @description 清空匹配keyWord的缓存, 注意key_value形式的在这里不能使用
	 * @author qiaoshipeng
	 */
	public static void removeLikeCache(String cacheName, String keyWord) {
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(keyWord)) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		if (cacheConfiguration.isUseCluster() && cacheConfiguration.isUseTwoLevelCache()) {
			if (redisServiceManager == null) {
				redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
			}
			Set<Object> sets = redisServiceManager.getCacheKeys(cacheName);
			List<String> keys = Lists.newArrayList();
			for (Object o : sets) {
				String key = (String) o;
				if (key.indexOf(keyWord) >= 0) {
					keys.add(key);
				}
			}
			if (keys.isEmpty()) {
			} else {
				removeCacheKeys(cacheName, keys);
			}
		}
	}

	/**
	 * @param cacheName 缓存名称
	 * @param keys 缓存keys
	 * @description 私有方法, 清空keys
	 */
	private static void removeCacheKeys(String cacheName, List<String> keys) {
		if (StringUtils.isEmpty(cacheName) || (null == keys || keys.isEmpty())) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		String[] keysArray = new String[keys.size()];
		for (int i = 0; i < keys.size(); i++) {
			keysArray[i] = keys.get(i);
		}
		//删除二级缓存
		redisCache.hashDelete(cacheName, keysArray);
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		//是否启用了一级缓存
		if (cacheConfiguration.isUseOneLevelCache()) {
			for (int i = 0; i < keys.size(); i++) {
				cache.remove(keys.get(i));
			}
			cache.flush();
			cache.flushDisk();
		}
		//发送给redis清除其他的缓存
		String keyString = StringUtil.buildSplitString(keysArray, ",");
		if (ClusterBooter.isClusterEnabled() && cacheConfiguration.isUseCluster() && cacheConfiguration.isClusterRemoveAll()) {
			if (redisServiceManager == null) {
				redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
			}
			ClusterCacheElement cacheElement = new ClusterCacheElement(cacheName, keyString, true, true);
			redisServiceManager.sendClusterCacheMessage(cacheElement, cacheConfiguration.isUseTwoLevelCache(), cacheConfiguration.isUseOneLevelCache());
		}
	}

	/**
	 * @param cacheName 缓存的名字
	 * @param key       缓存key
	 * @param zhId      租户id
	 * @return
	 * @description 清空租户下的缓存, 注意key_value形式的在这里不能使用
	 * @author qiaoshipeng
	 */
	public static void removeCache(String cacheName, String key, String zhId) {
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		key = zhId + "_" + key;
		if (cacheConfiguration.isCommonRedisCache()) {
			if (redisCache == null) {
				redisCache = SpringContextHolder.getBean("redisCache");
			}
			redisCache.remove(cacheName + "_" + key);
			return;
		}
		//清空二级缓存
		if (ClusterBooter.isClusterEnabled() && cacheConfiguration.isUseCluster() && cacheConfiguration.isClusterRemove()) {
			if (redisServiceManager == null) {
				redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
			}
			ClusterCacheElement cacheElement = new ClusterCacheElement(cacheName, key, true);
			redisServiceManager.sendClusterCacheMessage(cacheElement, cacheConfiguration.isUseTwoLevelCache(), cacheConfiguration.isUseOneLevelCache());
		}

		//清空本地缓存
		if (cacheConfiguration.isUseOneLevelCache()) {
			cache.remove(key);
			cache.flush();
			cache.flushDisk();
		}
	}

	/**
	 * @Description //清空缓存
	 * @Date  2019/7/4
	 * @Param cacheName: 缓存名称
	 * @Param key: 缓存key
	 * @Param removeAllZhCache:TODO
	 * @return: void
	**/
	public static void removeCache(String cacheName, String key, boolean removeAllZhCache) {
		if (!removeAllZhCache) {
			removeCache(cacheName, key);
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		//启用了租户,并且要移除这个租户下面的缓存
		if (cacheConfiguration.isUseSaas() && removeAllZhCache) {
			if (cacheConfiguration.isUseCluster() && cacheConfiguration.isUseTwoLevelCache()) {
				if (redisServiceManager == null) {
					redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
				}
				Set<Object> map = redisServiceManager.getCacheKeys(cacheName);
				List<String> keys = Lists.newArrayList();
				for (Object o : map) {
					String k = (String) o;
					if (StringUtils.isEmpty(k)) {
						continue;
					}
					k = k.substring(k.indexOf("_") + 1);
					if (k.equals(key)) {
						keys.add((String) o);
					}
				}
				if (keys.isEmpty()) {
				} else {
					removeCacheKeys(cacheName, keys);
				}
			}
		}

	}

	/**
	 * @Description //删除租户缓存
	 * @Date  2019/7/4
	 * @Param cacheName: 缓存名称
	 * @Param key: 缓存key
	 * @return: void
	**/
	public static void removeCacheWithNoZhId(String cacheName, String key) {
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		if (cacheConfiguration.isCommonRedisCache()) {
			if (redisCache == null) {
				redisCache = SpringContextHolder.getBean("redisCache");
			}
			redisCache.remove(cacheName + "_" + key);
			return;
		}
		//清空二级缓存
		if (ClusterBooter.isClusterEnabled() && cacheConfiguration.isUseCluster() && cacheConfiguration.isClusterRemove()) {
			if (redisServiceManager == null) {
				redisServiceManager = SpringContextHolder.getBean("redisServiceManager");
			}
			ClusterCacheElement cacheElement = new ClusterCacheElement(cacheName, key, true);
			redisServiceManager.sendClusterCacheMessage(cacheElement, cacheConfiguration.isUseTwoLevelCache(), cacheConfiguration.isUseOneLevelCache());
		}

		//清空本地缓存
		if (cacheConfiguration.isUseOneLevelCache()) {
			cache.remove(key);
			cache.flush();
			cache.flushDisk();
		}
	}

	/**
	 * @param cacheName 缓存的名字
	 * @return
	 * @description 清空keys的缓存, 缓存中不调用, 只是在接收到redis通知时调用
	 * @author qiaoshipeng
	 */
	public static void clearKeysWithFlush(String cacheName, String key) {
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		if (StringUtils.isNotEmpty(key)) {
			List<String> keys = Lists.newArrayList(key.split(","));
			cache.removeAll(keys);
			cache.flush();
			cache.flushDisk();
		}
	}

	/**
	 * @param cacheName 缓存的名字
	 * @param key       缓存key
	 * @return
	 * @description 移除缓存, 注意该方法, 缓存中不调用, 只是在接收到redis通知时调用
	 * @author qiaoshipeng
	 */
	public static void removeCacheWithNoCluster(String cacheName, String key) {
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		if (cacheConfiguration.isUseOneLevelCache()) {
			cache.remove(key);
		}
		cache.flush();
		cache.flushDisk();
	}

	/**
	 * @param cacheName 缓存的名字
	 * @return
	 * @description 清空整个cache, 注意key_value形式的在这里不能使用, 缓存中不调用, 只是在接收到redis通知时调用
	 * @author qiaoshipeng
	 */
	public static void clearAllDiskCacheWithNoCluster(String cacheName) {
		if (StringUtils.isEmpty(cacheName)) {
			return;
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			return;
		}
		CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
		if (cacheConfiguration.isUseOneLevelCache()) {
			cache.removeAll();
			cache.flush();
			cache.flushDisk();
		}
	}
}

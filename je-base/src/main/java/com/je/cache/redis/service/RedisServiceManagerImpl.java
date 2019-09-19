package com.je.cache.redis.service;

import com.google.common.collect.Maps;
import com.je.cache.model.ClusterCacheElement;
import com.je.cluster.ClusterBooter;
import com.je.core.facade.extjs.JsonBuilder;
import com.uncode.session.data.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@Component("redisServiceManager")
public class RedisServiceManagerImpl implements RedisServiceManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceManagerImpl.class);
	/**
	 * 集群消息通道
	 */
	public static final String PLUS_CLUSTER_CHANNEL = "_plusClusterChannel";
	public static final String SCRIPT_SESSION_LOCATION = "_plusScriptSessionLocation";

	private Object lock = new Object();

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void sendMessage(String channel, Object message) {
		redisTemplate.convertAndSend(channel, message);
		LOGGER.debug("cluster cache sysnc message sended");
	}

	@Override
	public Map<Object, Object> getCache(String cache) {
		return redisTemplate.opsForHash().entries(cache);
	}

	@Override
	public Object getObjectFromSecondLevelCache(String cacheName, String key) {
		return redisTemplate.opsForHash().get(cacheName, key);
	}

	@Override
	public Set<Object> getCacheKeys(String cacheName) {
		return redisTemplate.opsForHash().keys(cacheName);
	}
	@Override
	public Long getHashSize(String cacheName) {
		return redisTemplate.opsForHash().size(cacheName);
	}

	@Override
	public void sendClusterServiceMessage(String serviceName, String methodName, Object[] args) {
		Map<String, Object> messageMap = Maps.newHashMap();
		messageMap.put("type", "serviceInvoke");
		messageMap.put("serviceName", serviceName);
		messageMap.put("methodName", methodName);
		byte[] argBytes = SerializeUtil.serialize(args);
		messageMap.put("args", argBytes);
		JsonBuilder jsonBuilder = JsonBuilder.getInstance();
		sendMessage(PLUS_CLUSTER_CHANNEL, jsonBuilder.toJson(messageMap));
	}

	@Override
	public void sendClusterCacheMessage(ClusterCacheElement cacheElement) {
		if (!ClusterBooter.isClusterEnabled()) {
			throw new RuntimeException("current platform is not running in cluster mode,this method can not be called");
		}
		sendMessage(PLUS_CLUSTER_CHANNEL, buildCacheMap(cacheElement));
	}

	@Override
	public void sendClusterCacheMessage(ClusterCacheElement cacheElement, boolean useSecondLevel, boolean useOneLevel) {
		if (!ClusterBooter.isClusterEnabled()) {
			throw new RuntimeException("current platform is not running in cluster mode,this method can not be called");
		}
		if ((useSecondLevel && ClusterCacheElement.CACHE_REMOVE.equals(cacheElement.getOperation())) || (useSecondLevel && ClusterCacheElement.CACHE_REMOVE_ALL.equals(cacheElement.getOperation()))) {
			executeRemoveClusterMessage(cacheElement, useSecondLevel, useOneLevel, cacheElement.getOperation());
		} else if (useSecondLevel && ClusterCacheElement.CACHE_REMOVE_KEYS.equals(cacheElement.getOperation())) {
			sendClusterCacheMessage(cacheElement);
		} else {
			throw new RuntimeException("unsupported operation is not allowed");
		}
	}
		/**
		 * @Description //TODO 
		 * @Date  2019/7/4
		 * @Param cacheElement: 
		 * @Param useSecondLevel: 
		 * @Param useOneLevel: 
		 * @Param operation: 
		 * @return: void 
		**/
	private void executeRemoveClusterMessage(ClusterCacheElement cacheElement, boolean useSecondLevel, boolean useOneLevel, String operation) {
		if (useSecondLevel) {
			redisTemplate.executePipelined(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
					RedisSerializer<Object> serializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();

					if (useSecondLevel && ClusterCacheElement.CACHE_REMOVE.equals(operation)) {
						stringRedisConnection.hDel(cacheElement.getCacheName(), cacheElement.getKey());
					} else if (useSecondLevel && ClusterCacheElement.CACHE_REMOVE_ALL.equals(operation)) {
						stringRedisConnection.del(cacheElement.getCacheName());
					}
					//这里判断加上==false更好的读懂代码, 意思是如果没有启动一级缓存,只启动了二级缓存,那么就不再同步到二级缓存了
					if (useOneLevel == false && useSecondLevel == true) {
					} else {
						stringRedisConnection.publish(PLUS_CLUSTER_CHANNEL.getBytes(), serializer.serialize(buildCacheMap(cacheElement)));
					}
					return null;
				}
			});
		} else {
			sendClusterCacheMessage(cacheElement);
		}
	}

	/**
	 * @Description //TODO 
	 * @Date  2019/7/4
	 * @Param cacheElement: 
	 * @return: java.util.Map<java.lang.String,java.lang.Object> 
	**/
	private Map<String, Object> buildCacheMap(ClusterCacheElement cacheElement) {
		Map<String, Object> messageMap = Maps.newHashMap();
		messageMap.put("type", "cacheSync");
		messageMap.put("index", ClusterBooter.getClusterIndex());
		messageMap.put("cacheName", cacheElement.getCacheName());
		messageMap.put("key", cacheElement.getKey());
		messageMap.put("operation", cacheElement.getOperation());
		messageMap.put("removePattern", cacheElement.getRemovePattern());
		messageMap.put("flush", cacheElement.isFlush() ? "1" : "0");
		messageMap.put("datas", cacheElement.getValue());
		messageMap.put("project", ClusterBooter.getClusterProject());
		return messageMap;
	}
}

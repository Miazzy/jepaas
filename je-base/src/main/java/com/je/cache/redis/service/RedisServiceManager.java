package com.je.cache.redis.service;


import com.je.cache.model.ClusterCacheElement;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface RedisServiceManager {

	/**
	 * @Description //给通道道发送消息
	 * @Date  2019/7/4
	 * @Param channel: 通道
	 * @Param message: 消息体
	 * @return: void
	**/
	void sendMessage(String channel, Object message);

	/**
	 * @Description //获取缓存
	 * @Date  2019/7/4
	 * @Param cache: TODO
	 * @return: java.util.Map<java.lang.Object,java.lang.Object>
	**/
	Map<Object,Object> getCache(String cache);
	/**
	 * 获取redis中的所有的key
	 * @param cache TODO
	 * @return
	 */
	Set<Object> getCacheKeys(String cache);
	/**
	 * 从二级缓存获取对象
	 * @param cacheName TODO
	 * @param key TODO
	 * @return
	 */
	Object getObjectFromSecondLevelCache(String cacheName, String key);

	/**
	 * 远程异步调用集群中Spring服务方法
	 * @param serviceName 服务名字
	 * @param methodName 方法名字
	 * @param args TODO
	 */
	void sendClusterServiceMessage(String serviceName, String methodName, Object[] args);

	/**
	 * 发送集群间缓存同步消息
	 * @param obj 群集缓存元素
	 * @throws IOException
	 */
	void sendClusterCacheMessage(ClusterCacheElement obj) throws IOException;

	/**
	 * 发送集群间缓存同步消息
	 * @param obj 群集缓存元素
	 * @param useSecondLevel 是否使用二级缓存
	 */
	void sendClusterCacheMessage(ClusterCacheElement obj, boolean useSecondLevel, boolean useOneLevel);

	Long getHashSize(String cacheName);
}

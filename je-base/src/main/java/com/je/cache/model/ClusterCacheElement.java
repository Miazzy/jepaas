package com.je.cache.model;

import java.io.Serializable;

/**
 * 用于集群下发送消息至缓存同步操作
 * @author Administrator
 */
public class ClusterCacheElement implements Serializable{

    private static final long serialVersionUID = -2352003121533968918L;
    /**
     * @Description //TODO
    **/
    public static final String CACHE_PUT = "_cachePut";
    /**
     * @Description //TODO
     **/
    public static final String CACHE_REMOVE = "_cacheRemove";
    /**
     * @Description //TODO
     **/
    public static final String CACHE_REMOVE_ALL = "_cacheRemoveAll";
    /**
     * @Description //TODO
     **/
    public static final String CACHE_REMOVE_KEYS = "_cacheRemoveKeys";
    /**
     * @Description //TODO
     **/
    public static final String CACHE_REMOVE_PATTERN = "_cacheRemovePattern";
    /**
     * @Description //TODO
     **/
    public static final String REMOVE_ALL = "_removeAll";
    /**
     * @Description //TODO
    **/
    private String operation;
    /**
     * @Description //缓存名称
     **/
    private String cacheName;
    /**
     * @Description //缓存Key
     **/
    private String key;
    /**
     * @Description //缓存值，必须实现序列�?
     **/
    private Object value;
    /**
     * @Description //是否执行Flush操作
     **/
    private boolean flush;
    /**
     * @Description //TODO
    **/
    private String removePattern;

    /**
     * Cache Put操作
     * @param cacheName 缓存名称
     * @param key 缓存Key
     * @param value 缓存值，必须实现序列�?
     * @param flush 是否执行Flush操作
     */
    public ClusterCacheElement(String cacheName, String key, Object value, boolean flush) {
        this.operation = CACHE_PUT;
        this.cacheName = cacheName;
        this.key = key;
        this.value = value;
        this.flush = flush;
    }

    /**
     * 删除操作
     * @param cacheName 缓存名称
     * @param key 缓存键�??
     * @param flush 是否执行Flush操作
     */
    public ClusterCacheElement(String cacheName, String key, boolean flush) {
        this.operation = CACHE_REMOVE;
        this.cacheName = cacheName;
        this.key = key;
        this.flush = flush;
    }
    /**
     * 删除操作根据key的list匹配删除
     * @param cacheName 缓存名称
     * @param key 缓存键�??
     * @param flush 是否执行Flush操作
     */
    public ClusterCacheElement(String cacheName, String key, boolean flush, boolean isKeyRemove) {
        this.operation = CACHE_REMOVE_KEYS;
        this.cacheName = cacheName;
        this.key = key;
        this.flush = flush;
    }

    /**
     * 删除cacheName所有缓存
     * @param cacheName 缓存名称
     * @param flush 是否执行Flush操作
     */
    public ClusterCacheElement(String cacheName, boolean flush) {
        this.operation = CACHE_REMOVE_ALL;
        this.cacheName = cacheName;
        this.flush = flush;
    }

    /**
     * 根据正则删除缓存
     * @param cacheName  缓存名称
     * @param removePattern TODO
     */
    public ClusterCacheElement(String cacheName, String removePattern) {
        this.operation = CACHE_REMOVE_PATTERN;
        this.cacheName = cacheName;
        this.removePattern = removePattern;
    }

    /**
     * �?全构造函�?
     * @param operation TODO
     * @param cacheName 缓存名称
     * @param key  缓存键
     * @param value 值
     * @param flush 是否执行Flush操作
     * @param removePattern TODO
     */
    public ClusterCacheElement(String operation, String cacheName, String key, Object value, boolean flush,
                               String removePattern) {
        this.operation = operation;
        this.cacheName = cacheName;
        this.key = key;
        this.value = value;
        this.flush = flush;
        this.removePattern = removePattern;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public boolean isFlush() {
        return flush;
    }

    public void setFlush(boolean flush) {
        this.flush = flush;
    }

    public String getRemovePattern() {
        return removePattern;
    }

    public void setRemovePattern(String removePattern) {
        this.removePattern = removePattern;
    }

    @Override
    public String toString() {
        return "ClusterCacheElement [operation=" + operation + ", cacheName=" + cacheName + ", key=" + key + ", value="
                + value + ", flush=" + flush + ", removePattern=" + removePattern + "]";
    }

}

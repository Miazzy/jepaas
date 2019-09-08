package com.je.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 此方法封装了RedisTemplate所需要的基础操作，主要包括如下数据结构：
 * 1、KEY-VALUE形式
 * 2、HASH（散列）形式，方法都以hash开头
 * 3、List(列表结构)，方法都以list开头
 * 4、Set（集合），用法都以set开头
 * 5、ZSet（有序集合），用法都以zSet开头
 * 如有方法没实现或者存在BUG，请在此类上修正，提供统一服务工具类
 */
@Component
public class RedisCache {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    protected RedisTemplate<String, Object> redisTemplate;

    /**
     * 存入平台用户认证信息
     * @param key 键
     * @param value 值
     */
    public void putMap(PlatformCacheType type, String key, Object value){
        putCache(type,key,value);
    }

    /**
     * 存入平台用户认证信息
     * @param userValues 用户信息集合
     */
    public void putAllMap(PlatformCacheType type, Map<String,Object> userValues){
        putCache(type,userValues);
    }

    /**
     * 获取某用户的认证信息
     * @param key 键
     * @return
     */
    public Object getMap(PlatformCacheType type, String key){
        return getCacheItem(type,key);
    }

    /**
     * @Description //获取所有用户认证信息
     * @Date  2019/7/10
     * @Param type: 平台Redis缓存类型
     * @return: java.util.Map<java.lang.Object,java.lang.Object>
    **/
    public Map<Object,Object> getAllMap(PlatformCacheType type){
        return getCacheAll(type);
    }

    /**
     * @Description //用户是否认证
     * @Date  2019/7/10
     * @Param type: 平台Redis缓存类型
     * @Param key: 键值
     * @return: boolean
    **/
    public boolean hasMapKey(PlatformCacheType type, String key){
        return cacheItemExist(type,key);
    }

    /**
     * 存入缓存
     * @param cacheType 平台Redis缓存类型
     * @param name 名称
     * @param value 值
     */
    public void putCache(PlatformCacheType cacheType, String name, Object value){
        hashPut(cacheType.getValue(),name,value);
    }

    /**
     * 存入缓存
     * @param cacheType 平台Redis缓存类型
     * @param nameValues 缓存集合
     */
    public void putCache(PlatformCacheType cacheType, Map<String,Object> nameValues){
        hashPutAll(cacheType.getValue(),nameValues);
    }

    /**
     * 获取缓存项
     * @param cacheType 平台Redis缓存类型
     * @param name 名称
     * @return
     */
    public Object getCacheItem(PlatformCacheType cacheType, String name){
        return hashGet(cacheType.getValue(),name);
    }

    /**
     * 获取缓存中所有
     * @param cacheType 平台Redis缓存类型
     * @return
     */
    public Map<Object,Object> getCacheAll(PlatformCacheType cacheType){
        return hashGet(cacheType.getValue());
    }

    /**
     * 缓存是否存在
     * @param cacheType 平台Redis缓存类型
     * @return
     */
    public boolean cacheExist(PlatformCacheType cacheType){
        return exist(cacheType.getValue());
    }

    /**
     * 检查缓存中某项是否存在
     * @param cacheType 平台Redis缓存类型
     * @param name 名称
     * @return
     */
    public boolean cacheItemExist(PlatformCacheType cacheType, String name){
        return cacheItemExist(cacheType.getValue(),name);
    }

    /**
     * 检查缓存下是否存在某个缓存
     * @param key 键值
     * @param name 名称
     * @return
     */
    public boolean cacheItemExist(String key,String name){
        return hashHasKey(key,name);
    }

    /**
     * 移除缓存
     * @param cacheType 平台Redis缓存类型
     */
    public void cacheRemove(PlatformCacheType cacheType){
        remove(cacheType.getValue());
    }

    /**
     * 移除缓存中的某一项或多项
     * @param cacheType 平台Redis缓存类型
     * @param names 名称数组
     * @return
     */
    public long cacheItemsRemove(PlatformCacheType cacheType, String... names){
        return hashDelete(cacheType.getValue(),names);
    }

    /**
     * 有序集合的Add
     * @param key 键
     * @param value 值
     * @param score TODO
     * @return
     */
    public boolean zSetAdd(String key,Object value,double score){
        return redisTemplate.opsForZSet().add(key,value,score);
    }

    /**
     * 有序集合中分值之间的元素数量
     * @param key 值
     * @param startScore TODO
     * @param endScore  TODO
     * @return
     */
    public long zSetCount(String key,double startScore,double endScore){
        return redisTemplate.opsForZSet().count(key,startScore,endScore);
    }

    /**
     * 有序集合中给某个元素增加delta分值
     * @param key 键
     * @param value 值
     * @param delta TODO
     * @return
     */
    public double zSetIncrementScore(String key,Object value,double delta){
        return redisTemplate.opsForZSet().incrementScore(key,value,delta);
    }

    /**
     * 有序集合求交集
     * @param key1 键1
     * @param key2 键2
     * @param key3 键3
     * @return
     */
    public long zSetIntersectAndStore(String key1,String key2,String key3){
        return redisTemplate.opsForZSet().intersectAndStore(key1,key2,key3);
    }

    /**
     * 有序集合求交集
     * @param key1 键1
     * @param values 值集合
     * @param key2 键2
     * @return
     */
    public long zSetIntersectAndStore(String key1,Collection<String> values,String key2){
        return redisTemplate.opsForZSet().intersectAndStore(key1,values,key2);
    }

    /**
     * 获取指定范围内的对象
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return
     */
    public Set<Object> zSetRange(String key,long start,long end){
        return redisTemplate.opsForZSet().range(key,start,end);
    }

    /**
     * 获取指定范围内的对象
     * @param key 键
     * @param range RedisZSetCommands
     * @return
     */
    public Set<Object> zSetRangeByLex(String key, RedisZSetCommands.Range range){
        return redisTemplate.opsForZSet().rangeByLex(key,range);
    }

    /**
     * @Description //获取指定范围内的对象
     * @Date  2019/7/10
     * @Param key: 键
     * @Param range:  Range
     * @Param limit: Limit
     * @return: java.util.Set<java.lang.Object>
    **/
    public Set<Object> zSetRangeByLex(String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit){
        return redisTemplate.opsForZSet().rangeByLex(key,range,limit);
    }

    /**
     * 获取指定分数范围的对象
     * @param key 键
     * @param startScore TODO
     * @param endScore TODO
     * @return
     */
    public Set<Object> zSetRangeByScore(String key,double startScore,double endScore){
        return redisTemplate.opsForZSet().rangeByScore(key,startScore,endScore);
    }

    /**
     * @Description //获取指定分数范围的对象
     * @Date  2019/7/10
     * @Param key: 键
     * @Param min: 是否以最小
     * @Param max: 是否以最大
     * @Param offset: TODO
     * @Param count:  数量
     * @return: java.util.Set<java.lang.Object>
    **/
    public Set<Object> zSetRangeByScore(String key,double min,double max,long offset,long count){
        return redisTemplate.opsForZSet().rangeByScore(key,min,max,offset,count);
    }

    /**
     * 查找指定分数范围内的对象
     * @param key 键
     * @param min 是否以最小
     * @param max 是否以最大
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zSetRangeByScoreWithScores(String key, double min, double max){
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key,min,max);
    }

    /**
     * 查找指定位置范围按分数排序
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zSetRangeWithScores(String key, long start, long end){
        return redisTemplate.opsForZSet().rangeWithScores(key,start,end);
    }

    /**
     * 获取排序
     * @param key 键
     * @param value 值
     * @return
     */
    public long zSetRank(String key,Object value){
        return redisTemplate.opsForZSet().rank(key,value);
    }

    /**
     * 有序集合移除元素
     * @param key 键
     * @param values 值数组
     * @return
     */
    public long zSetRemove(String key,Object... values){
        return redisTemplate.opsForZSet().remove(key,values);
    }

    /**
     * 有序集合移除指定位置范围的元素
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return
     */
    public long zSetRemoveRange(String key,long start,long end){
        return redisTemplate.opsForZSet().removeRange(key,start,end);
    }

    /**
     * 有序集合移除分数范围之间的元素
     * @param key 键
     * @param min 是否以最小
     * @param max 是否以最大
     * @return
     */
    public long zSetRemoveRangeByScore(String key,double min,double max){
        return redisTemplate.opsForZSet().removeRangeByScore(key,min,max);
    }

    /**
     * 有序集合逆向
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return
     */
    public Set<Object> zSetReverseRange(String key,long start,long end){
        return redisTemplate.opsForZSet().reverseRange(key,start,end);
    }

    /**
     * 有序集合逆向
     * @param key 键
     * @param min 是否以最小
     * @param max 是否以最大
     * @return
     */
    public Set<Object> zSetReverseRangeByScore(String key,double min,double max){
        return redisTemplate.opsForZSet().reverseRangeByScore(key,min,max);
    }

    /**
     * @Description //TODO
     * @Date  2019/7/10
     * @Param key: 键
     * @Param min: 是否以最小
     * @Param max: 是否以最大
     * @return: java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple<java.lang.Object>>
    **/
    public Set<ZSetOperations.TypedTuple<Object>> zSetReverseRangeByScoreWithScores(String key, double min, double max){
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,min,max);
    }

    /**
     * @Description //TODO
     * @Date  2019/7/10
     * @Param key: 键
     * @Param value:  值对象
     * @return: long
    **/
    public long zSetReverseRank(String key,Object value){
        return redisTemplate.opsForZSet().reverseRank(key,value);
    }

    /**
     * @Description //TODO
     * @Date  2019/7/10
     * @Param key: 键
     * @Param start: 开始位置
     * @Param end: 结束位置
     * @return: java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple<java.lang.Object>>
    **/
    public Set<ZSetOperations.TypedTuple<Object>> zSetReverseRangeWithScores(String key, long start, long end){
        return redisTemplate.opsForZSet().reverseRangeWithScores(key,start,end);
    }

    /**
     * @Description //TODO
     * @Date  2019/7/10
     * @Param key: 键
     * @Param value: 值
     * @return: double TODO
    **/
    public double zSetScore(String key,Object value){
        return redisTemplate.opsForZSet().score(key,value);
    }

    /**
     * @Description //TODO
     * @Date  2019/7/10
     * @Param key: 键
     * @return: long
    **/
    public long zSetSize(String key){
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * @Description //TODO
     * @Date  2019/7/10
     * @Param key1: 键1
     * @Param key2: 键2
     * @Param key3: 键3
     * @return: long
    **/
    public long zSetUnionAndStore(String key1,String key2,String key3){
        return redisTemplate.opsForZSet().unionAndStore(key1,key2,key3);
    }

    /**
     * @Description //TODO
     * @Date  2019/7/10
     * @Param key1: 键1
     * @Param values: 值集合
     * @Param key2: 键2
     * @return: long 值集合
    **/
    public long zSetUnionAndStore(String key1,Collection<String> values,String key2){
        return redisTemplate.opsForZSet().unionAndStore(key1,values,key2);
    }

    /**
     * @Description //TODO
     * @Date  2019/7/10
     * @Param key: 键
     * @return: long
    **/
    public long zSetzCard(String key){
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 集合中加入元素
     * @param key 键
     * @param values 值
     * @return
     */
    public long setAdd(String key,Object... values){
        return redisTemplate.opsForSet().add(key,values);
    }

    /**
     * 判断集合中是否有元素
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean setIsMember(String key,Object value){
        return redisTemplate.opsForSet().isMember(key,value);
    }

    /**
     * 获取集合中的元素
     * @param key 键
     * @return
     */
    public Set<Object> setMembers(String key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 两个集合中的不同
     * @param key1 键1
     * @param key2 键2
     * @return
     */
    public Set<Object> setDifference(String key1,String key2){
        return redisTemplate.opsForSet().difference(key1,key2);
    }

    /**
     * 与集合中的不同
     * @param key 键
     * @param values 值集合
     * @return
     */
    public Set<Object> setDifference(String key,Collection<String> values){
        return redisTemplate.opsForSet().difference(key,values);
    }

    /**
     * 找到不同的元素，添加到第三个中
     * @param key1 键1
     * @param key2 键2
     * @param key3 键3
     * @return
     */
    public long setDifferenceAndStore(String key1,String key2,String key3){
        return redisTemplate.opsForSet().differenceAndStore(key1,key2,key3);
    }

    /**
     * 找到与Collection不同的元素，添加到第三个中
     * @param key1 键1
     * @param values 值集合
     * @param key2 键2
     * @return
     */
    public long setDifferenceAndStore(String key1,Collection<String> values,String key2){
        return redisTemplate.opsForSet().differenceAndStore(key1,values,key2);
    }

    /**
     * 集合交集
     * @param key1 键1
     * @param key2 键2
     * @return
     */
    public Set<Object> setIntersect(String key1,String key2){
        return redisTemplate.opsForSet().intersect(key1,key2);
    }

    /**
     * 集合交集
     * @param key1 键1
     * @param values 值集合
     * @return
     */
    public Set<Object> setIntersect(String key1,Collection<String> values){
        return redisTemplate.opsForSet().intersect(key1,values);
    }

    /**
     * 集合交集，并放至第三个集合
     * @param key1 键1
     * @param key2 键2
     * @param key3 键3
     * @return
     */
    public long setIntersectAndStore(String key1,String key2,String key3){
        return redisTemplate.opsForSet().intersectAndStore(key1,key2,key3);
    }

    /**
     * 集合与values交集，并放至第二个集合
     * @param key1 键1
     * @param values 值集合
     * @param key2 键2
     * @return
     */
    public long setIntersectAndStore(String key1,Collection<String> values,String key2){
        return redisTemplate.opsForSet().intersectAndStore(key1,values,key2);
    }

    /**
     * 集合中的某个Value移到另一个
     * @param key1 键1
     * @param value 值
     * @param key2 键2
     * @return
     */
    public boolean setMove(String key1,Object value,String key2){
        return redisTemplate.opsForSet().move(key1,value,key2);
    }

    /**
     * 集合推出一个对象
     * @param key 键
     * @return
     */
    public Object setPop(String key){
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 从集合中随即获取一个
     * @param key 键
     * @return
     */
    public Object setRandomMember(String key){
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 从集合中随即获取一组
     * @param key 键
     * @param l TODO
     * @return
     */
    public List<Object> randomMembers(String key,long l){
        return redisTemplate.opsForSet().randomMembers(key,l);
    }

    /**
     * 从集合中删除
     * @param key 键
     * @param values 值数组
     * @return
     */
    public long setRemove(String key,Object...values){
        return redisTemplate.opsForSet().remove(key,values);
    }

    /**
     * 两个集合求并集
     * @param key1 键1
     * @param key2 键2
     */
    public Set<Object> setUnion(String key1,String key2){
        return redisTemplate.opsForSet().union(key1,key2);
    }

    /**
     * 集合和values求并集
     * @param key 键
     * @param values 值集合
     * @return
     */
    public Set<Object> setUnion(String key,Collection<String> values){
        return redisTemplate.opsForSet().union(key,values);
    }

    /**
     * 求并集并存储到第三个
     * @param key1 键1
     * @param key2 键2
     * @param key3 键3
     * @return
     */
    public long unionAndStore(String key1,String key2,String key3){
        return redisTemplate.opsForSet().unionAndStore(key1,key2,key3);
    }

    /**
     * 集合和Values求并集并存储到第三个
     * @param key1 键1
     * @param values 值集合
     * @param key2  键2
     * @return
     */
    public long unionAndStore(String key1,Collection<String> values,String key2){
        return redisTemplate.opsForSet().unionAndStore(key1,values,key2);
    }

    /**
     * 集合中的元素个数
     * @param key 键
     * @return
     */
    public long setSize(String key){
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 从左边推入列表
     * @param key 键
     * @param value 值
     * @return
     */
    public long listLeftPush(String key,Object value){
        return redisTemplate.opsForList().leftPush(key,value);
    }

    /**
     * 如果列表存在，从左边推入列表
     * @param key 键
     * @param value 值
     */
    public void listLeftPushIfPresent(String key,Object value){
        redisTemplate.opsForList().leftPushIfPresent(key,value);
    }

    /**
     * 从左边推入列表
     * @param key 键
     * @param values 值数组
     * @return
     */
    public long listLeftPushAll(String key,Object... values){
        return redisTemplate.opsForList().leftPush(key,values);
    }

    /**
     * 从左边推入列表
     * @param key 键
     * @param values 值数组
     * @return
     */
    public long listLeftPushAll(String key,Collection<Object> values){
        return redisTemplate.opsForList().leftPushAll(key,values);
    }

    /**
     * 从左边推入列表
     * @param key 键
     * @param value 值
     * @return
     */
    public long listRightPush(String key,Object value){
        return redisTemplate.opsForList().rightPush(key,value);
    }

    /**
     * 如果列表存在，从右边推入列表
     * @param key 键
     * @param value 值
     */
    public void rightLeftPushIfPresent(String key,Object value){
        redisTemplate.opsForList().rightPushIfPresent(key,value);
    }

    /**
     * 从左边推入列表
     * @param key 键
     * @param values 值数组
     * @return
     */
    public long listRightPushAll(String key,Object... values){
        return redisTemplate.opsForList().rightPush(key,values);
    }

    /**
     * 从左边推入列表
     * @param key 键
     * @param values 值数组
     * @return
     */
    public long listRightPushAll(String key,Collection<Object> values){
        return redisTemplate.opsForList().rightPushAll(key,values);
    }

    /**
     * 从列表左边推出
     * @param key 键
     * @return
     */
    public Object listLeftPop(String key){
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从列表左边推出第pos个元素
     * @param key 键
     * @param pos TODO
     * @param timeUnit TODO
     * @return
     */
    public Object listLeftPop(String key,long pos,TimeUnit timeUnit){
        return redisTemplate.opsForList().leftPop(key,pos,timeUnit);
    }

    /**
     * 从列表右边推出
     * @param key 键
     * @return
     */
    public Object listRightPop(String key){
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 从列表右边推出第pos个元素
     * @param key 键
     * @param pos TODO
     * @param timeUnit TODO
     * @return
     */
    public Object listRightPop(String key,long pos,TimeUnit timeUnit){
        return redisTemplate.opsForList().rightPop(key,pos,timeUnit);
    }

    /**
     * 查询列表大小
     * @param key 键
     * @return
     */
    public long listSize(String key){
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取列表指定位置的对象，不推出列表
     * @param key 键
     * @param index TODO
     * @return
     */
    public Object listIndex(String key,long index){
        return redisTemplate.opsForList().index(key,index);
    }

    /**
     * 获取指定范围的对象，不推出列表
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return
     */
    public List<Object> listRange(String key,long start,long end){
        return redisTemplate.opsForList().range(key,start,end);
    }

    /**
     * 裁剪列表
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     */
    public void listTrim(String key,long start,long end){
        redisTemplate.opsForList().trim(key,start,end);
    }

    /**
     * 从一个List的右边POP，从另一个List的左边PUSH
     * @param key1 键1
     * @param key2 键2
     */
    public void listRightPopAndLeftPush(String key1,String key2){
        redisTemplate.opsForList().rightPopAndLeftPush(key1,key2);
    }

    /**
     * 设置列表指定位置的VALUE
     * @param key 键
     * @param pos TODO
     * @param value 值
     */
    public void listSet(String key,long pos,Object value){
        redisTemplate.opsForList().set(key,pos,value);
    }

    /**
     * 散列的PUT操作
     * @param key 键
     * @param childKey TODO
     * @param value 值
     */
    public void hashPut(String key,String childKey,Object value){
        logger.debug(String.format("Redis中添加HASH[%s]---[%s]---[%s]",key,childKey,value));
        redisTemplate.opsForHash().put(key,childKey,value);
    }

    /**
     * @Description //TODO
     * @Date  2019/7/10
     * @Param key: 键
     * @Param childKey:TODO
     * @Param value: 值
     * @return: boolean
    **/
    public boolean hashPutIfAbsent(String key,String childKey,Object value){
        logger.debug(String.format("Redis中添加HASH[%s]---[%s]---[%s]",key,childKey,value));
        return redisTemplate.opsForHash().putIfAbsent(key,childKey,value);
    }

    /**
     * 散列的PUTALL操作
     * @param key 键
     * @param value 值
     */
    public void hashPutAll(String key, Map<String,Object> value){
        logger.debug(String.format("Redis中添加HASH[%s]---[%s]",key,value));
        redisTemplate.opsForHash().putAll(key,value);
    }

    /**
     * 从散列中获取操作
     * @param key 键
     * @param childKey TODO
     */
    public Object hashGet(String key,String childKey){
        logger.debug(String.format("Redis中获取[%s]---[%s]",key,childKey));
        return redisTemplate.opsForHash().get(key,childKey);
    }

    /**
     * 从散列中获取所有键值对
     * @param key 键
     * @return
     */
    public Map<Object,Object> hashGet(String key){
        logger.debug(String.format("Redis中获取[%s]键",key));
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取散列所有的KEY
     * @param key 键
     * @return
     */
    public Set<Object> getHashKeys(String key){
        logger.debug(String.format("Redis中获取[%s]键",key));
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 从散列中删除指定的KEY
     * @param key 键
     * @param childKeys TODO
     * @return
     */
    public long hashDelete(String key,String... childKeys){
        logger.debug(String.format("Redis中删除[%s]---[%s]",key,childKeys));
        return redisTemplate.opsForHash().delete(key,childKeys);
    }

    /**
     * 散列中指定键增加VALUE
     * @param key 键
     * @param childKey TODO
     * @param value 值
     * @return
     */
    public long hashIncrement(String key,String childKey,long value){
        return redisTemplate.opsForHash().increment(key,childKey,value);
    }

    /**
     * 散列中指定键增加VALUE
     * @param key 键
     * @param childKey TODO
     * @param value 值
     * @return
     */
    public double hashIncrement(String key,String childKey,double value){
        return redisTemplate.opsForHash().increment(key,childKey,value);
    }

    /**
     * 散列中是否包含此键
     * @param key 键
     * @param childKey TODO
     * @return
     */
    public boolean hashHasKey(String key,String childKey){
        return redisTemplate.opsForHash().hasKey(key,childKey);
    }

    /**
     * 获取多个键
     * @param key 键
     * @param childKeys TODO
     * @return
     */
    public List<Object> hashMuliGet(String key, Collection<Object> childKeys){
        return redisTemplate.opsForHash().multiGet(key,childKeys);
    }

    /**
     * 获取散列集合的大小
     * @param key 键
     * @return
     */
    public long hashSize(String key){
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 获取散列中所有的VALUE
     * @param key 键
     * @return
     */
    public List<Object> hashValues(String key){
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * KEY-VALUE的PUT操作
     * @param key 键
     * @param value 值
     */
    public void put(String key, Object value) {
        logger.debug("Redis中添加[" + key + "]---[" + value + "]");
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * KEY-VALUE的PUT操作
     * @param key 键
     * @param value 值
     * @param timeout TODO
     */
    public void put(String key, Object value, long timeout) {
        logger.debug("Redis中添加[" + key + "]---[" + value + "]");
        redisTemplate.opsForValue().set(key, value,timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取KEY-VALUE的值
     * @param key 键
     * @return
     */
    public Object get(String key) {
        if (null == key){
            return null;
        }
        logger.debug("Redis中获取[" + key + "]");
        Object value = redisTemplate.opsForValue().get(key);
        if (null == value) {
            return null;
        } else {
            return redisTemplate.opsForValue().get(key);
        }
    }

    /**
     * 获取KEY-VALUE的值
     * @param key 键
     * @param timeout TODO
     * @return
     */
    public Object get(String key, long timeout) {
        if (null == key){
            return null;
        }
        logger.debug("Redis中获取[" + key + "]");
        Object value = redisTemplate.opsForValue().get(key);
        if (null == value) {
            return null;
        } else {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            return redisTemplate.opsForValue().get(key);
        }
    }

    /**
     * 获取并清除KEY-VALUE的VALUE
     * @param key 键
     * @return
     */
    public Object getAndclean(String key) {
        if (null == key){
            return null;
        }
        logger.debug("Redis中获取[" + key + "]");
        Object value = redisTemplate.opsForValue().get(key);
        if (null == value) {
            return null;
        } else {
            Object v1 = redisTemplate.opsForValue().get(key);
//            Object resultValue = new Object();
//            BeanUtils.copyProperties(v1, resultValue);
            redisTemplate.delete(key);
            return v1;
        }
    }

    /**
     * 移除KEY
     * @param key 键
     */
    public void remove(String key) {
        if (null == key){
            return;
        }
        logger.debug("Redis中删除[" + key + "]");
        redisTemplate.delete(key);
    }

    /**
     * 是否存在
     * @param key 键
     * @return
     */
    public boolean exist(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置超时时间
     * @param key
     * @return
     */
    public void expire(String key,long timeout){
        try {
            if (timeout == -1) {
                redisTemplate.expire(key, 365, TimeUnit.DAYS);
            } else {
                redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.error("redis设置超时时间失败,有可能key不存在");
        }
    }



}

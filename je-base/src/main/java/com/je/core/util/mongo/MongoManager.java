package com.je.core.util.mongo;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 用于得到Mongodb的数据库连接句柄
 * @author 云凤程
 */
public class MongoManager {
	 private static Mongo mongo = null;
	 protected static Logger logger = LoggerFactory.getLogger(MongoManager.class);
     private MongoManager() {
     }
	 
     /**
      * 根据名称获取DB，相当于是连接
      * @param dbName
      * @return
      */
     public static DB getDB(String dbName) {
         if (mongo == null) {
             // 初始化
             init();
         }
         DB db = mongo.getDB(dbName);
         return db;
     }
 
     /**
      * 初始化连接池，设置参数。
      */
     private static void init() {
    	 //得到mongdo的所有配置
    	 MConf conf = new MConf();
         String host = conf.getHost();// 主机名
         int port = conf.getPort();// 端口
         int poolSize = conf.getPoolSize();// 连接数量
         int blockSize = conf.getBlockSize(); // 等待队列长度
         // 其他参数根据实际情况进行添加
         try {
             MongoOptions opt = new MongoOptions();
             opt.connectionsPerHost = poolSize;
             opt.threadsAllowedToBlockForConnectionMultiplier = blockSize;
             opt.autoConnectRetry = false;//默认不进行错误尝试重新连接
             ServerAddress serverAddress=new ServerAddress(host, port);
             mongo = new Mongo(serverAddress, opt);
         } catch (Exception e) {
        	 logger.error("创建mongodb数据库连接句柄出错..."+e);
         }
    }
}




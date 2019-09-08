package com.je.core.util.mongo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 关于MONGODB数据库配置参数载体,由于是缓存数据库不采用数据认证的形式启动
 * 配合配置参数文件一直使用
 *
 * @author 云凤程
 */
public class MConf {
    public MConf() {
        Properties map = new Properties();
        InputStream inputStream = null;
        try {
//			map.load(new FileInputStream((MConf.class.getResource("/").getPath()+"mongo.properties")));
            inputStream = new FileInputStream("E:/work/workspace/platform/WebRoot/WEB-INF/classes/mongo.properties");
            map.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //拿到后台变量集合
        //取值赋值
        String host = map.getProperty("MONGO_HOST");
        int port = Integer.parseInt(map.getProperty("MONGO_PORT"));
        int poolSize = Integer.parseInt(map.getProperty("MONGO_POOLSIZE"));
        int blockSize = Integer.parseInt(map.getProperty("MONGO_BLOCKSIZE"));
        String dbName = map.getProperty("MONGO_DBNAME");
        setBlockSize(blockSize);
        setDbName(dbName);
        setHost(host);
        setPoolSize(poolSize);
        setPort(port);
    }

    //数据库连接地址
    private String host;
    //数据库连接端口
    private int port;
    //连接数量
    private int poolSize;
    //等待队列长度
    private int blockSize;
    //数据库名称
    private String dbName;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}

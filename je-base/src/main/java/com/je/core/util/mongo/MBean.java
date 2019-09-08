package com.je.core.util.mongo;

import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 操作MONGODB时使用的数据载体
 * @author 云凤程
 *
 */
public class MBean {
	//辅助信息数据载体
	private HashMap<String , Object> conf = new HashMap<String , Object>();
	//数据信息载体
	private DBObject data = new BasicDBObject();
	/**
	 * 有参构造函数
	 * @param listName 集合名称
	 */
	public MBean(String listName){
		conf.put(MBeanUtils.CONF_LIST_NAME, listName);
	}
	/**
	 * 有参构造函数
	 * @param listName 集合名称
	 * @param dbObject 数据
	 */
	public MBean(String listName,DBObject dbObject){
		conf.put(MBeanUtils.CONF_LIST_NAME, listName);
		data = dbObject;
	}
	/**
	 * 得到数据载体
	 * @return
	 */
	public DBObject getData(){
		return data;
	}
	/**
	 * 赋值操作
	 * @param dbObject
	 */
	public void setData(DBObject dbObject){
		this.data = dbObject;
	}
	/**
	 * 根据key得到MBean里面data的数值
	 * @param key
	 * @return
	 */
	public Object getD(String key){
		return data.get(key);
	}
	/**
	 * 根据key得到MBean里面conf的数值
	 * @param key
	 * @return
	 */
	public Object getC(String key){
		return conf.get(key);
	}
}




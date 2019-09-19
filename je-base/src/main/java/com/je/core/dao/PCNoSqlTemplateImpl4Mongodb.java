package com.je.core.dao;

import com.je.core.util.mongo.MBean;
import com.je.core.util.mongo.MBeanUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * 平台操作MONGODB统一入口
 * @author 云凤程
 */
@Component("PCNoSqlTemplateImpl4Mongodb")
public class PCNoSqlTemplateImpl4Mongodb implements PCNoSqlTemplateImpl{
	//数据库句柄
	private static DB db = null;
	protected static Logger logger = LoggerFactory.getLogger(PCNoSqlTemplateImpl4Mongodb.class);
	static{
		if(db == null){
			//MConf conf = new MConf();
			//dbsql = MongoManager.getDB(conf.getDbName());
		}
	}
	/**
	 * 添加数据
	 * @param mBean
	 * @return 返回新的mBean如果是自动ID非常有必要
	 */
	@Override
	public MBean insert(MBean mBean) {
		//1.得到集合
		DBCollection coll = db.getCollection((String)mBean.getC(MBeanUtils.CONF_LIST_NAME));
		coll.insert(mBean.getData());
		return mBean;
	}
	/**
	 * 批量增加
	 * @param mBeans 
	 * @param simple 集合提是否针对单一数据库链表
	 * @return
	 */	
	@Override
	public int insertBatch(List<MBean> mBeans, boolean simple) {
		//1.整理数据
		String listName = null;
		List<DBObject> dbObjects = new ArrayList<DBObject>();
		for(MBean mBean : mBeans){
			listName = (String)mBean.getC(MBeanUtils.CONF_LIST_NAME);
			dbObjects.add(mBean.getData());
		}
		//单一集合体操作
		if(simple){
			//1.得到集合
			DBCollection coll = db.getCollection(listName);
			//2.插入操作
			return coll.insert(dbObjects).getN();
		}else{
			int i = 0;
			DBObject dbObject = null;
			DBCollection coll = null;
			for(MBean mBean : mBeans){
				listName = (String)mBean.getC(MBeanUtils.CONF_LIST_NAME);
				dbObject = mBean.getData();
				//1.得到集合
				coll = db.getCollection(listName);
				//2.插入操作
				i+=coll.insert(dbObject).getN();
			}			
			return i;
		}
	}
	/**
	 * 根据_id删除数据
	 * @param _id 可以是_id的字符串,也可以是ObjectId对象实例
	 * @param listName 链表名字
	 * @param mongoAutoId 是否是mongo自动管理的_id
	 * @return
	 */
	public int deleteById(Object _id,String listName,boolean mongoAutoId){
		//1.得到集合
		DBCollection coll = db.getCollection(listName);
		ObjectId ido = null;
		String idClass = _id.getClass().getSimpleName();
		if(idClass.equals("String")){
			if(mongoAutoId){
				ido = new ObjectId((String)_id);
			}else{
				DBObject dbObject = new BasicDBObject();
				dbObject.put("_id", _id);
				MBean mBean = new MBean("listName",dbObject);
				return deleteByMBean(mBean);
			}
		}else if(idClass.equals("ObjectId")){
			ido = (ObjectId)_id;
		}else{
			logger.error("MONGGODB删除数据出错,_id传值失败.导致无法进行删除操作，参数:_id:"+_id+" listName:"+listName+"");
		}
		DBObject dbs = new BasicDBObject("_id", ido);
		int count = coll.remove(dbs).getN();
		return count;
	}
	/**
	 * 根据组合条件删除数据
	 * @param mBean
	 * @return
	 */
	public int deleteByMBean(MBean mBean){
		//1.得到集合
		DBCollection coll = db.getCollection((String)mBean.getC(MBeanUtils.CONF_LIST_NAME));
		int count = coll.remove(mBean.getData()).getN();
		return count;
	}
	/**
	 * 根据条件查出一条记录
	 * @param ref
	 * @param keys
	 * @return
	 */	
	@Override
	public MBean findOne(MBean ref,MBean keys) {
		//1.得到集合
		DBCollection coll = db.getCollection((String)ref.getC(MBeanUtils.CONF_LIST_NAME));
		DBObject cur = null;
		if(keys != null){
			cur = coll.findOne(ref.getData(), keys.getData());
		}else{
			cur = coll.findOne(ref.getData());
		}
		ref.setData(cur);
		return ref;
	}
	

}

package com.je.core.dao;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.constants.ConstantVars;
import com.je.core.entity.BaseEntity;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.sql.BuildingQuery;
import com.je.core.util.*;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.impl.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * PCAT平台核心DAO接口对于[ORACLE]实现类
 * @author YUNFENGCHENG
 * 2011-8-30 下午01:11:05
 */
@Component("PCDAOTemplateORCL")
public class PCDaoTemplateImpl implements PCDaoTemplate {
	/*系统数据库变量*/
	public static String DBNAME="";
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private static EntityUtils entityUtils;
	private static BuildingQuery buildingQuery;
	protected static Logger logger = LoggerFactory.getLogger(PCDaoTemplateImpl.class);
	static{
		entityUtils = EntityUtils.getInstance();
		buildingQuery = BuildingQuery.getInstance();
	}
	/**
	 * 保存实体
	 * 研发部:云凤程
	 * 2011-8-30 下午01:14:45
	 * @param entity
	 * @return 保存是否成功
	 */
	@Override
	public Object save(Object entity) {
		try {
			hibernateTemplate.save(entity);
			return entity;
		} catch (Exception e) {
			throw new PlatformException("实体BEAN保存数据出错!", PlatformExceptionEnum.JE_CORE_ENEITY_SAVE_ERROR,new Object[]{entity},e);
		}
	}

	/**
	 * TODO 暂不明确
	 */
	@Override
	public void flush() {
		hibernateTemplate.flush();
	}
	/**
	 * 更新实体
	 * 研发部:云凤程
	 * @param entity
	 * @return
	 */
	@Override
	public Object updateEntity(Object entity) {
		try {
			hibernateTemplate.saveOrUpdate(entity);
			return entity;
		} catch (Exception e) {
			throw new PlatformException("实体BEAN修改数据出错!", PlatformExceptionEnum.JE_CORE_ENEITY_UPDATE_ERROR,new Object[]{entity},e);
			//logger.error("数据保存出错：" + e);
			//logger.error("类结构：" + entity.toString());
			//return false;
		}
	}
	/**
	 * 建议用在表单列表
	 * 更新实体 PCAT平台使用的更新采用拼接SQL方法而不采用HIBERNATE自带更新对象方法
	 * 研发部:云凤程
	 * 2011-8-30 下午01:27:43
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Object updateBySQL(Object entity) {
		//1.得到可以执行的Query
		Query query = buildingQuery.getListUpdateQuery(entity, sessionFactory);
		query.executeUpdate();
		return entity;
	}
	/**
	 * 建议用在表单更新
	 * 更新实体(纯HIBERNATE操作)
	 * 研发部:云凤程
	 * 2011-8-30 下午03:18:42
	 * @param entity
	 * @return
	 */
	@Override
	public Object updateByHQL(Object entity){
		//准备
		Class<?> entityClass = entity.getClass();
		//String idName = entityUtils.getEntityInfo(entityClass).getIdName();
		String idValue = entityUtils.getEntityIdValue(entity);
		//1.根据ID查找改对象
		Object obj = hibernateTemplate.get(entityClass, idValue);
		//2.对象更新
		if(obj != null){//存在数据
			obj = entityUtils.copyNewField(obj, entity);
		}
		//3.持久化对象
		sessionFactory.getCurrentSession().update(obj);
		return obj;
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#countByHql(java.lang.String)
	 */

	/**
	 * HQL count分页方法
	 * @param hql
	 * @return
	 */
	@Override
	public Long countByHql(String hql) {
		Long c = 0L;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		Object count = query.uniqueResult();
		if(null != count && StringUtil.isInteger(count.toString())) {
			c = Long.parseLong(count.toString());
		}
		return c;
	}

	/**
	 * 通过SQL查询获取表字段列表
	 * @param sql
	 * @return
	 */
	@Override
	public List<String> getColumnListBySql(String sql) {
		List<String> columnNames = new ArrayList<String>();
		ConnectionProvider cp = ((SessionFactoryImplementor)sessionFactory).getConnectionProvider();
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connection = cp.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			if(null != rs) {
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				for(int i=1; i<=columnCount; i++) {
					columnNames.add(metaData.getColumnName(i));
				}
			}

		} catch (SQLException e) {
//			e.printStackTrace();
			throw new PlatformException("实体BEAN修改数据出错!", PlatformExceptionEnum.JE_CORE_ENEITY_SQL_COLUMN_ERROR,new Object[]{sql},e);
		} finally {
			JdbcUtil.close(rs, stmt, connection);
		}
		return columnNames;
	}

	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#countBySql(java.lang.String)
	 */

	/**
	 * SQL count分页方法
	 * @param sql
	 * @return
	 */
	@Override
	public Long countBySql(String sql) {
		Long c = 0L;
		Query query = buildingQuery.getSqlQuery(sql,"sql",null,null, sessionFactory);
		Object count = query.uniqueResult();
		if(null != count && StringUtil.isInteger(count.toString())) {
			c = Long.parseLong(count.toString());
		}
		return c;
	}

	@Override
	public Long countBySql(String sql, Object[] params) {
		Long c = 0L;
		Query query = buildingQuery.getSqlQuery(sql,"index",params,null, sessionFactory);
		Object count = query.uniqueResult();
		if(null != count && StringUtil.isInteger(count.toString())) {
			c = Long.parseLong(count.toString());
		}
		return c;
	}

	@Override
	public Long countBySql(String sql, Map<String, Object> params) {
		Long c = 0L;
		Query query = buildingQuery.getSqlQuery(sql,"name",null,params, sessionFactory);
		Object count = query.uniqueResult();
		if(null != count && StringUtil.isInteger(count.toString())) {
			c = Long.parseLong(count.toString());
		}
		return c;
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#deleteEntityById(java.lang.Class, java.io.Serializable)
	 */

	/**
	 * 针对某一实体类型进行指定ID删除
	 * @param c
	 * @param id
	 */
	@Override
	public void removeEntityById(Class<?> c, Serializable id) {
		Object entity = hibernateTemplate.get(c, id);
		if(null != entity){
			hibernateTemplate.delete(entity);
		}

	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#executeHql(java.lang.String)
	 */

	/**
	 * 执行HQL
	 * @param hql
	 * @return
	 */
	@Override
	public Long executeHql(String hql) {
		return Long.valueOf(hibernateTemplate.bulkUpdate(hql));
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#executeProcedure(java.lang.String)
	 */

	/**
	 * 调用存储过程
	 * @param procedureName
	 */
	@Override
	public void executeProcedureByName(String procedureName) {
		Query query = buildingQuery.getNamedQuery(procedureName, sessionFactory);
		query.executeUpdate();
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#executeSql(java.lang.String)
	 */

	/**
	 * 执行SQL
	 * @param sql
	 * @return
	 */
	@Override
	public Long executeSql(String sql) {
		Long c = 0L;
		Query query = buildingQuery.getSqlQuery(sql,"sql",null,null, sessionFactory);
		Object count = query.executeUpdate();
		if(null != count && StringUtil.isInteger(count.toString())) {
			c = Long.parseLong(count.toString());
		}
		return c;
	}

	@Override
	public Long executeSql(String sql, Object[] params) {
		Long c = 0L;
		Query query = buildingQuery.getSqlQuery(sql,"index",params,null, sessionFactory);
		Object count = query.executeUpdate();
		if(null != count && StringUtil.isInteger(count.toString())) {
			c = Long.parseLong(count.toString());
		}
		return c;
	}

	@Override
	public Long executeSql(String sql, Map<String, Object> params) {
		Long c = 0L;
		Query query = buildingQuery.getSqlQuery(sql,"name",null,params, sessionFactory);
		Object count = query.executeUpdate();
		if(null != count && StringUtil.isInteger(count.toString())) {
			c = Long.parseLong(count.toString());
		}
		return c;
	}

	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#getByUniqueValue(java.lang.Class, java.lang.String, java.lang.Object)
	 */

	/**
	 * 按一个唯一约束字段获取单个实体
	 * @param entityClass
	 * @param columnName
	 * @param value
	 * @return
	 */
	@Override
	public Object getByUniqueValue(Class<BaseEntity> entityClass, String columnName, Object value) {
		String hql = "from "+entityClass+" where "+columnName+" = '"+value+"'";
		Object obj = buildingQuery.getHqlQuery(hql, sessionFactory);
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#getEntityByUniqueValue(java.lang.String)
	 */

	/**
	 * 通过unique值获取一个实体，如果获取了多于一个的实体记录，则抛出DuplicatedDataException
	 * @param hql
	 * @return
	 */
	@Override
	public BaseEntity getEntityByUniqueValue(String hql) {
		List<?> oneRow = queryByHql(hql);
		if(null != oneRow && 1 == oneRow.size()) {
			return (BaseEntity)oneRow.iterator().next();
		}
		if(null != oneRow && 1 < oneRow.size()) {
			throw new PlatformException("实体BEAN查询一条数据出错!", PlatformExceptionEnum.JE_CORE_ENEITY_SQL_COLUMN_ERROR,new Object[]{hql});
		}
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#getEntityById(java.lang.Class, java.io.Serializable)
	 */

	/**
	 * 针对某一实体类型进行指定ID获取
	 * @param clazz
	 * @param id
	 * @return
	 */
	@Override
	public BaseEntity getEntityById(Class<?> clazz, Serializable id) {
		return getEntityById(clazz, id, true);
	}

	/**
	 * 可选使用缓存
	 * @param clazz
	 * @param id
	 * @param useCache
	 * @return
	 */
	@Override
	public BaseEntity getEntityById(Class<?> clazz, Serializable id, boolean useCache) {
		BaseEntity entity = null;
		if(useCache) {
			entity = (BaseEntity) hibernateTemplate.get(clazz, id);
		} else {
			SessionImpl si = (SessionImpl)sessionFactory.getCurrentSession();
			entity = (BaseEntity) si.immediateLoad(clazz.getName(), id);
		}
		return entity;
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#queryByExample(com.je.core.entity.BaseEntity)
	 *//**
	 * 根据实体模板like获取实体列表
	 * 	 * 1.不支持主键
	 * 	 * 2.不支持关联
	 * 	 * 3.不支持NULL
	@Override
	public List<?> queryByExample(BaseEntity example) {
//		Example temp = Example.create(example).ignoreCase().enableLike(MatchMode.ANYWHERE);
		return hibernateTemplate.findByExample(example);
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#queryByExample(com.je.core.entity.BaseEntity, int, int)
	 */
	@Override
	public List<?> queryByExample(BaseEntity example, int start, int limit) {
//		Example temp = Example.create(example).ignoreCase().enableLike(MatchMode.ANYWHERE);
		return hibernateTemplate.findByExample(example, start, limit);

	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#queryByHql(java.lang.String)
	 */

	/**
	 * 根据HQL查询实体列表
	 * @param hql
	 * @return
	 */
	@Override
	public List<?> queryByHql(String hql) {
		return hibernateTemplate.find(hql);
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#queryByHql(java.lang.String, int, int)
	 */
	/**
	 * 改动：查询一次流程实例表，没有用到，注释了
	 * zhangshuaipeng
	 */
	@Override
	public List<?> queryByHql(String hql, int start, int limit) {
//		try {
//			ResultSet rs = connection.createStatement().executeQuery("select * from EXECUTION t");
//			logger.debug("");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//Query query = buildingQuery.getSqlQuery("select * "+hql, sessionFactory);
		Query query = buildingQuery.getHqlQuery(hql, sessionFactory);
		if(0 < limit) {
			query.setFirstResult(start);
			query.setMaxResults(limit);
		}

		return query.list();
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#queryBySql(java.lang.String)
	 */

	/**
	 * 根据SQL查询实体列表
	 * @param sql
	 * @return
	 */
	@Override
	public List<?> queryBySql(String sql) {
		Query query = buildingQuery.getSqlQuery(sql,"sql",null,null, sessionFactory);
		List<?> list = query.list();
		return list;
	}

	@Override
	public List<?> queryBySql(String sql, Object[] params) {
		Query query = buildingQuery.getSqlQuery(sql,"index",params,null, sessionFactory);
		List<?> list = query.list();
		return list;
	}

	@Override
	public List<?> queryBySql(String sql, Map<String, Object> params) {
		Query query = buildingQuery.getSqlQuery(sql,"name",null,params, sessionFactory);
		List<?> list = query.list();
		return list;
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#queryBySql(java.lang.String)
	 */

	/**
	 * 根据SQL查询实体列表
	 * @param sql
	 * @return
	 */
	@Override
	public List<Map> queryMapBySql(String sql) {
		Query query = buildingQuery.getMapSqlQuery(sql,"sql",null,null, sessionFactory);
		List<Map> lists=query.list();
		DBSqlUtils.setClobList(lists);
		return lists;
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#queryBySql(java.lang.String)
	 */

	/**
	 * 设定参数查询
	 * @param sql
	 * @param params
	 * @return
	 */
	@Override
	public List<Map> queryMapBySql(String sql,Object[] params) {
		Query query = buildingQuery.getMapSqlQuery(sql,"index",params,null, sessionFactory);
		List<Map> lists=query.list();
		DBSqlUtils.setClobList(lists);
		return lists;
	}

	@Override
	public List<Map> queryMapBySql(String sql, Map<String, Object> params) {
		Query query = buildingQuery.getMapSqlQuery(sql,"name",null,params, sessionFactory);
		List<Map> lists=query.list();
		DBSqlUtils.setClobList(lists);
		return lists;
	}

	@Override
	public List<Map> queryMapBySql(String sql, int start, int limit) {
		Query query = buildingQuery.getMapSqlQuery(sql,"sql",null,null, sessionFactory);
		if(limit>0){
			query.setFirstResult(start);
			query.setMaxResults(limit);
		}
		List<Map> lists=query.list();
		DBSqlUtils.setClobList(lists);
		return lists;
	}

	/**
	 * 设定参数查询
	 * @param sql
	 * @param params
	 * @param start
	 * @return
	 */
	public List<Map> queryMapBySql(String sql,Object[] params,int start,int limit){
		Query query = buildingQuery.getMapSqlQuery(sql,"index",params,null, sessionFactory);
		if(limit>0){
			query.setFirstResult(start);
			query.setMaxResults(limit);
		}
		List<Map> lists=query.list();
		DBSqlUtils.setClobList(lists);
		return lists;
	}

	@Override
	public List<Map> queryMapBySql(String sql, Map<String, Object> params, int start, int limit) {
		Query query = buildingQuery.getMapSqlQuery(sql,"name",null,params, sessionFactory);
		if(limit>0){
			query.setFirstResult(start);
			query.setMaxResults(limit);
		}
		List<Map> lists=query.list();
		DBSqlUtils.setClobList(lists);
		return lists;
	}

	/**
	 * 查询SQL分页查询
	 * @param sql
	 * @param fieldVos
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<Map> queryMapBySql(String sql,List<DbFieldVo> fieldVos,int start,int limit){
		Query query = buildingQuery.getMapSqlQuery(sql,fieldVos, sessionFactory);
		if(limit>0){
			query.setFirstResult(start);
			query.setMaxResults(limit);
		}
		List<Map> lists=query.list();
		DBSqlUtils.setClobList(lists);
		return lists;
	}

	/**
	 *  查询SQL全部数据
	 */
	@Override
	public Long countMapBySql(String sql, List<DbFieldVo> fieldVos) {
		// TODO Auto-generated method stub
		sql=DbProduceUtil.buildCountSql(sql);
		Query query = buildingQuery.countSqlQuery(sql,fieldVos, sessionFactory);
		Long allCount= 0L;
		Object count = query.uniqueResult();
		if(count!=null && StringUtil.isNotEmpty(count+"")){
			allCount=Long.parseLong(count+"");
		}
		return allCount;
	}

	@Override
	public Long executeProcedure(String callSql) {
		Long c = 0L;
		Query query = buildingQuery.getProcedureQuery(callSql,"sql",null,null, sessionFactory);
		Object count = query.executeUpdate();
		query.executeUpdate();
		if(null != count && StringUtil.isInteger(count.toString())) {
			c = Long.parseLong(count.toString());
		}
		return c;
	}

	/**
	 * 执行存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 * @return
	 */
	@Override
	public Long executeProcedure(String callSql, Object[] params) {
		// TODO Auto-generated method stub
		Long c = 0L;
		Query query = buildingQuery.getProcedureQuery(callSql,"index",params,null, sessionFactory);
		Object count = query.executeUpdate();
		query.executeUpdate();
		if(null != count && StringUtil.isInteger(count.toString())) {
			c = Long.parseLong(count.toString());
		}
		return c;
	}

	@Override
	public Long executeProcedure(String callSql, Map<String, Object> params) {
		Long c = 0L;
		Query query = buildingQuery.getProcedureQuery(callSql,"name",null,params, sessionFactory);
		Object count = query.executeUpdate();
		query.executeUpdate();
		if(null != count && StringUtil.isInteger(count.toString())) {
			c = Long.parseLong(count.toString());
		}
		return c;
	}

	@Override
	public List queryProcedure(String callSql) {
		Query query = buildingQuery.getProcedureQuery(callSql,"sql",null,null, sessionFactory);
		List lists=query.list();
		return lists;
	}

	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 * @return
	 */
	@Override
	public List queryProcedure(String callSql, Object[] params) {
		// TODO Auto-generated method stub
		Query query = buildingQuery.getProcedureQuery(callSql,"index",params,null, sessionFactory);
		List lists=query.list();
		return lists;
	}

	@Override
	public List queryProcedure(String callSql, Map<String, Object> params) {
		Query query = buildingQuery.getProcedureQuery(callSql,"name",null,params, sessionFactory);
		List lists=query.list();
		return lists;
	}

	@Override
	public List<Map> queryMapProcedure(String callSql) {
		Query query = buildingQuery.getMapProcedureQuery(callSql,"sql",null,null, sessionFactory);
		List<Map> lists=query.list();
		DBSqlUtils.setClobList(lists);
		return lists;
	}

	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 * @return
	 */
	@Override
	public List<Map> queryMapProcedure(String callSql, Object[] params) {
		// TODO Auto-generated method stub
		Query query = buildingQuery.getMapProcedureQuery(callSql,"index",params,null, sessionFactory);
		List<Map> lists=query.list();
		DBSqlUtils.setClobList(lists);
		return lists;
	}

	@Override
	public List<Map> queryMapProcedure(String callSql, Map<String, Object> params) {
		Query query = buildingQuery.getMapProcedureQuery(callSql,"name",null,params, sessionFactory);
		List<Map> lists=query.list();
		DBSqlUtils.setClobList(lists);
		return lists;
	}

	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param fieldVos 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 * @return
	 */
	@Override
	public Map queryMapProcedure(String callSql, List<DbFieldVo> fieldVos) {
		// TODO Auto-generated method stub
		Connection con=getConnection();
		CallableStatement proc = null;
		Object result = null;
		Map returnObj=new HashMap();
		try {
			proc = con.prepareCall(callSql);
			DbProduceUtil.registerParams(proc, fieldVos);
			DbProduceUtil.loadResultSetParams(proc, fieldVos, returnObj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new PlatformException("调用存储过程异常!", PlatformExceptionEnum.JE_CORE_ENEITY_PROCEDURE_ERROR,new Object[]{callSql,fieldVos},e);
//			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, proc, con);
		}
		return returnObj;
	}

	@Override
	public Map queryMapOutParamProcedure(String callSql) {
		Connection con=getConnection();
		CallableStatement proc = null;
		Object result = null;
		Map returnObj=new HashMap();
		try {
			proc = con.prepareCall(callSql);
			DbProduceUtil.registerParams(proc, new ArrayList<DbFieldVo>());
			DbProduceUtil.loadResultSetParams(proc, new ArrayList<DbFieldVo>(), returnObj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new PlatformException("调用存储过程异常!", PlatformExceptionEnum.JE_CORE_ENEITY_PROCEDURE_ERROR,new Object[]{callSql},e);
//			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, proc, con);
		}
		return returnObj;
	}

	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 * @return
	 */
	@Override
	public Map queryMapOutParamProcedure(String callSql, Object[] params) {
		// TODO Auto-generated method stub
		List<Map> lists = new ArrayList<Map>();
		List outParamValues=new ArrayList();
		Connection con=getConnection();
		CallableStatement proc = null;
		ResultSet rs = null;
		try {
			proc = con.prepareCall(callSql);
			List<Integer> outParams=setExecuteParams(proc, params);
			rs = proc.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (null != rs && rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < columnCount; i++) {
					String name = rsmd.getColumnName(i + 1);
					Object value = rs.getObject(name);
					map.put(name, value);
				}
				lists.add(map);
			}
			for(Integer outIndex:outParams){
				outParamValues.add(rs.getObject(outIndex));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new PlatformException("调用含有出参存储过程异常!", PlatformExceptionEnum.JE_CORE_ENEITY_PROCEDURE_OUT_ERROR,new Object[]{callSql,params},e);
//			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, proc, con);
		}
		Map res=new HashMap();
		res.put("out", outParamValues);
		res.put("resultset", lists);
		return res;
	}

	private List<Integer> setExecuteParams(CallableStatement proc,Object[] params) throws SQLException{
		List<Integer> outParams=new ArrayList<Integer>();
		if(params!=null){
			for(int i=1;i<=params.length;i++){
				Object paramVal=params[i-1];
				if(paramVal instanceof Integer){
					Integer val=(Integer) params[i-1];
					proc.setInt(i, val);
				}else if(paramVal instanceof Float){
					Float val=(Float) params[i-1];
					proc.setFloat(i, val);
				}else if(paramVal instanceof Double){
					Double val=(Double) params[i-1];
					proc.setDouble(i, val);
				}else if(paramVal instanceof String){
					String paramV=(String) paramVal;
					if(paramV!=null && paramV.startsWith("out_param_")){
						String type=paramV.split("_")[2];
						if("int".equals(type)){
							proc.registerOutParameter(i, Types.INTEGER);
						}else if("double".equals(type)){
							proc.registerOutParameter(i, Types.DOUBLE);
						}else if("float".equals(type)){
							proc.registerOutParameter(i, Types.FLOAT);
						}else{
							proc.registerOutParameter(i, Types.VARCHAR);
						}
						outParams.add(i);
					}else{
						proc.setString(i, paramV);
					}
				}else{
					proc.setObject(i, params[i-1]);
				}
			}
		}
		return outParams;
	}
	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#queryBySql(java.lang.String, java.lang.Class)
	 */

	/**
	 * 通过SQL查询已有实体bean的数据
	 * @param sql
	 * @param c
	 * @return
	 */
	@Override
	public List<?> queryBySql(String sql, Class<?> c) {
		SQLQuery query = buildingQuery.getSqlQuery(sql,"sql",null,null, sessionFactory);
		query.addEntity(c);
		List<?> list = query.list();
		return list;
	}

	@Override
	public List<?> queryBySql(String sql, Object[] params, Class<?> c) {
		SQLQuery query = buildingQuery.getSqlQuery(sql,"index",params,null, sessionFactory);
		query.addEntity(c);
		List<?> list = query.list();
		return list;
	}

	@Override
	public List<?> queryBySql(String sql, Map<String, Object> params, Class<?> c) {
		SQLQuery query = buildingQuery.getSqlQuery(sql,"name",null,params, sessionFactory);
		query.addEntity(c);
		List<?> list = query.list();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.je.core.dao.PCDaoTemplate#queryBySql(java.lang.String, int, int)
	 */

	/**
	 * 根据HQL分页查询实体列表
	 * @param sql
	 * @param start
	 * @param limit
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<?> queryBySql(String sql, int start, int limit, Class<?> c) {
		SQLQuery query = buildingQuery.getSqlQuery(sql,"sql",null,null, sessionFactory);
		query.addEntity(c);
		query.setFirstResult(start);
		query.setMaxResults(limit);
		List<Object> list = query.list();
		return list;
	}

	@Override
	public List<?> queryBySql(String sql, Object[] params, int start, int limit, Class<?> c) {
		SQLQuery query = buildingQuery.getSqlQuery(sql,"index",params,null, sessionFactory);
		query.addEntity(c);
		query.setFirstResult(start);
		query.setMaxResults(limit);
		List<Object> list = query.list();
		return list;
	}

	@Override
	public List<?> queryBySql(String sql, Map<String, Object> params, int start, int limit, Class<?> c) {
		SQLQuery query = buildingQuery.getSqlQuery(sql,"name",null,params, sessionFactory);
		query.addEntity(c);
		query.setFirstResult(start);
		query.setMaxResults(limit);
		List<Object> list = query.list();
		return list;
	}

	/**
	 * 根据实体模板like获取实体列表
	 * 	 * 1.不支持主键
	 * 	 * 2.不支持关联
	 * 	 * 3.不支持NULL
	 * @param example
	 * @return
	 */
	@Override
	public List<?> queryByExample(BaseEntity example) {
		return null;
	}

	public void queryByPureSql(String sql) throws SQLException {
//		DataSource ds = SessionFactoryUtils.getDataSource(sessionFactory);
//		Connection conn = null;
//		Statement stmt = null;
////		ResultSet rs = null;
//		conn = ds.getConnection();
//		stmt = conn.createStatement();
//		rs = stmt.executeQuery(sql);


	}

	/**
	 * 获取数据库连接
	 * @return
	 */
	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		Connection connection=null;
		try {
			connection=SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new PlatformException("获取链接异常!", PlatformExceptionEnum.JE_CORE_ENEITY_CONNENTION_ERROR,e);
//			e.printStackTrace();
		}
		return connection;
	}
	@Override
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 获取当前sessionFactory
	 * @return
	 */
	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	@Resource(name="sf")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void removeEntity(Object entity) {
		// TODO Auto-generated method stub
		hibernateTemplate.delete(entity);
	}

}











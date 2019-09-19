package com.je.core.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.entity.BaseEntity;

/**
 * PCAT平台核心DAO接口
 * @author YUNFENGCHENG
 * 2011-8-30 下午01:11:05
 */
public interface PCDaoTemplate {
	/**
	 * 保存实体
	 * 研发部:云凤程
	 * 2011-8-30 下午01:14:45
	 * @param entity
	 * @return 保存成功返回新的对象,不成返回false
	 */
	public Object save(Object entity);
	/**
	 * 更新实体
	 * 研发部:云凤程
	 * @param entity
	 * @return
	 */
	public Object updateEntity(Object entity);
	/**
	 * 建议用在列表更新
	 * 更新实体 PCAT平台使用的更新采用拼接SQL方法而不采用HIBERNATE自带更新对象方法
	 * 研发部:云凤程
	 * 2011-8-30 下午01:27:43
	 * @param entity
	 * @return
	 */
	public Object updateBySQL(Object entity);
	/**
	 * 建议用在表单更新
	 * 更新实体(纯HIBERNATE操作)
	 * 研发部:云凤程
	 * 2011-8-30 下午03:18:42
	 * @param entity
	 * @return
	 */
	public Object updateByHQL(Object entity);

	/**
	 * 针对某一实体类型进行指定ID删除
	 * chenmeng
	 * 2011-12-27 下午02:26:50
	 * @param c
	 * @param id
	 */
	public void removeEntityById(Class<?> c, Serializable id);
	public void removeEntity(Object entity);

	/**
	 * 针对某一实体类型进行指定ID获取
	 * chenmeng
	 * 2011-12-27 下午02:29:29
	 * @param c
	 * @param id
	 * @return
	 */
	public BaseEntity getEntityById(Class<?> c, Serializable id);

	/**
	 * 按一个唯一约束字段获取单个实体
	 * chenmeng
	 * 2011-12-27 下午02:33:52
	 * @param c
	 * @param columnName
	 * @param value
	 * @return
	 */
	public Object getByUniqueValue(Class<BaseEntity> c, String columnName, Object value);

	/**
	 * 执行SQL
	 * chenmeng
	 * 2011-12-27 下午02:37:16
	 * @param sql
	 * @return
	 */
	public Long executeSql(String sql);
	/**
	 * 执行SQL
	 * chenmeng
	 * 2011-12-27 下午02:37:16
	 * @param sql
	 * @return
	 */
	public Long executeSql(String sql,Object[] params);
	/**
	 * 执行SQL
	 * chenmeng
	 * 2011-12-27 下午02:37:16
	 * @param sql
	 * @return
	 */
	public Long executeSql(String sql,Map<String,Object> params);

	/**
	 * 执行HQL
	 * chenmeng
	 * 2011-12-27 下午02:37:16
	 * @param hql
	 * @return
	 */
	public Long executeHql(String hql);

	/**
	 * 调用存储过程
	 * chenmeng
	 * 2011-12-26 下午03:02:20
	 * @param procedureName
	 */
	public void executeProcedureByName(String procedureName);

	/**
	 * 根据HQL查询实体列表
	 * chenmeng
	 * 2011-12-27 下午02:40:02
	 * @param hql
	 * @return
	 */
	public List<?> queryByHql(String hql);

	/**
	 * 根据HQL分页查询实体列表
	 * chenmeng
	 * 2011-12-27 下午02:42:49
	 * @param hql
	 * @param start
	 * @return
	 */
	public List<?> queryByHql(String hql, int start, int limit);

	/**
	 * 根据SQL查询实体列表
	 * chenmeng
	 * 2011-12-27 下午02:40:02
	 * @param sql
	 * @return
	 */
	public List<?> queryBySql(String sql);
	/**
	 * 根据SQL查询实体列表
	 * chenmeng
	 * 2011-12-27 下午02:40:02
	 * @param sql
	 * @return
	 */
	public List<?> queryBySql(String sql,Object[] params);
	/**
	 * 根据SQL查询实体列表
	 * chenmeng
	 * 2011-12-27 下午02:40:02
	 * @param sql
	 * @return
	 */
	public List<?> queryBySql(String sql,Map<String,Object> params);
	/**
	 * 根据SQL查询实体列表
	 * chenmeng
	 * 2011-12-27 下午02:40:02
	 * @param sql
	 * @return
	 */
	public List<Map> queryMapBySql(String sql);
	/**
	 * 设定参数查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map> queryMapBySql(String sql, Object[] params);
	/**
	 * 设定参数查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map> queryMapBySql(String sql,Map<String,Object> params);
	/**
	 * 设定参数查询
	 * @param sql
	 * @return
	 */
	public List<Map> queryMapBySql(String sql, int start, int limit);
	/**
	 * 设定参数查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map> queryMapBySql(String sql, Object[] params, int start, int limit);
	/**
	 * 设定参数查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map> queryMapBySql(String sql,Map<String,Object> params, int start, int limit);
	/**
	 * 查询SQL分页查询
	 * @param sql
	 * @param fieldVos
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<Map> queryMapBySql(String sql, List<DbFieldVo> fieldVos, int start, int limit);
	/**
	 * 查询SQL分页查询
	 * @param sql
	 * @param fieldVos
	 * @return
	 */
	public Long countMapBySql(String sql, List<DbFieldVo> fieldVos);
	/**
	 * 执行存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 */
	public Long executeProcedure(String callSql);
	/**
	 * 执行存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 */
	public Long executeProcedure(String callSql, Object[] params);
	/**
	 * 执行存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 */
	public Long executeProcedure(String callSql, Map<String,Object> params);
	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 */
	public List queryProcedure(String callSql);
	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 */
	public List queryProcedure(String callSql, Object[] params);
	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 */
	public List queryProcedure(String callSql, Map<String,Object> params);
	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 */
	public List<Map> queryMapProcedure(String callSql);
	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 */
	public List<Map> queryMapProcedure(String callSql, Object[] params);
	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 */
	public List<Map> queryMapProcedure(String callSql, Map<String,Object> params);
	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param fieldVos 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 */
	public Map queryMapProcedure(String callSql, List<DbFieldVo> fieldVos);
	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 */
	public Map queryMapOutParamProcedure(String callSql);
	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 */
	public Map queryMapOutParamProcedure(String callSql, Object[] params);
	/**
	 * 根据SQL分页查询实体列表
	 * chenmeng
	 * 2011-12-27 下午02:42:49
	 * @param sql
	 * @param start
	 * @return
	 */
	public List<?> queryBySql(String sql, int start, int limit, Class<?> c);
	/**
	 * 根据SQL分页查询实体列表
	 * chenmeng
	 * 2011-12-27 下午02:42:49
	 * @param sql
	 * @param start
	 * @return
	 */
	public List<?> queryBySql(String sql,Object[] params, int start, int limit, Class<?> c);
	/**
	 * 根据SQL分页查询实体列表
	 * chenmeng
	 * 2011-12-27 下午02:42:49
	 * @param sql
	 * @param start
	 * @return
	 */
	public List<?> queryBySql(String sql,Map<String,Object> params, int start, int limit, Class<?> c);

	/**
	 * 根据实体模板like获取实体列表
	 * 1.不支持主键
	 * 2.不支持关联
	 * 3.不支持NULL
	 * chenmeng
	 * 2011-12-26 下午03:51:05
	 * @param example
	 * @return
	 */
	public List<?> queryByExample(BaseEntity example);

	/**
	 * 根据实体模板like获取分页实体列表
	 * 1.不支持主键
	 * 2.不支持关联
	 * 3.不支持NULL
	 * Example example = Example.create(entityClass).ignoreCase().enableLike(MatchMode.ANYWHERE);
	 * chenmeng
	 * 2011-12-26 下午03:48:58
	 * @param example
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<?> queryByExample(BaseEntity example, int start, int limit);

	/**
	 * HQL count分页方法
	 * chenmeng
	 * 2011-12-26 下午02:22:07
	 * @param hql
	 */
	public Long countByHql(String hql);

	/**
	 * SQL count分页方法
	 * chenmeng
	 * 2011-12-26 下午02:22:07
	 * @param sql
	 */
	public Long countBySql(String sql);
	/**
	 * SQL count分页方法
	 * chenmeng
	 * 2011-12-26 下午02:22:07
	 * @param sql
	 */
	public Long countBySql(String sql,Object[] params);
	/**
	 * SQL count分页方法
	 * chenmeng
	 * 2011-12-26 下午02:22:07
	 * @param sql
	 */
	public Long countBySql(String sql,Map<String,Object> params);
	/**
	 * 获取当前sessionFactory
	 * chenmeng
	 * 2012-1-4 上午11:10:18
	 * @return
	 */
	public SessionFactory getSessionFactory();
	/**
	 * 通过SQL查询已有实体bean的数据
	 * chenmeng
	 * 2012-1-9 下午12:21:30
	 * @param sql
	 * @param c
	 * @return
	 */
	public List<?> queryBySql(String sql, Class<?> c);
	/**
	 * 通过SQL查询已有实体bean的数据
	 * chenmeng
	 * 2012-1-9 下午12:21:30
	 * @param sql
	 * @param c
	 * @return
	 */
	public List<?> queryBySql(String sql,Object[] params, Class<?> c);
	/**
	 * 通过SQL查询已有实体bean的数据
	 * chenmeng
	 * 2012-1-9 下午12:21:30
	 * @param sql
	 * @param c
	 * @return
	 */
	public List<?> queryBySql(String sql,Map<String,Object> params, Class<?> c);
	/**
	 * 可选使用缓存
	 * @author chenmeng
	 * @date 2012-3-23 下午03:02:15
	 * @param clazz
	 * @param id
	 * @param useCache
	 * @return
	 */
	public BaseEntity getEntityById(Class<?> clazz, Serializable id, boolean useCache);

	/**
	 * 通过unique值获取一个实体，如果获取了多于一个的实体记录，则抛出DuplicatedDataException
	 * @param hql
	 * @return
	 */
	public BaseEntity getEntityByUniqueValue(String hql);
	/**
	 * get hibernateTemplate
	 * @return
	 */
	public HibernateTemplate getHibernateTemplate();
	/**
	 * 获取数据库连接
	 */
	public Connection getConnection();
	/**
	 * TODO 暂不明确
	 */
	public abstract void flush();
	/**
	 * 通过SQL查询获取表字段列表
	 * @param sql
	 * @return
	 */
	public List<String> getColumnListBySql(String sql);
}

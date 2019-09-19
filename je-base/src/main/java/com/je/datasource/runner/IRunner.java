package com.je.datasource.runner;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.entity.extjs.Model;
import com.je.datasource.DataSourceContext;
import com.je.datasource.callable.IResultSetCallable;

/**
 * Copyright: Copyright (c) 2018 jeplus.cn
 * @Description: 执行器统一调度接口
 * @version: v1.0.0
 * @author: LIULJ
 * @date: 2018年4月16日 下午9:06:22
 *
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年4月16日     LIULJ           v1.0.0               初始创建
 *
 *
 */
public interface IRunner {

	/**
	 * 获取执行上下文
	 * @return
	 */
	public DataSourceContext getDataSourceContext();

	/**
	 * 执行SQL语句
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int execute(String sql) throws SQLException;

	/**
	 * 执行带参数SQL语句
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int execute(String sql, Object... params) throws SQLException;

	/**
	 * 执行SQL批处理
	 * @param sqlArray
	 * @return
	 * @throws SQLException
	 */
	public int[] executeAsBatch(String[] sqlArray) throws SQLException;

	/**
	 * 执行SQL批处理
	 * @param sqlList
	 * @return
	 * @throws SQLException
	 */
	public int[] executeAsBatch(List<String> sqlList) throws SQLException;

//	/**
//	 * 执行存储过程
//	 * @param procedureName
//	 * @param params
//	 * @throws SQLException
//	 */
//	public void executeProcedure(String procedureName, Object... params) throws SQLException;

	/**
	 * 执行查询
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryMapList(String sql) throws SQLException;

	/**
	 * 执行带参查询
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryMapList(String sql, Object... params) throws SQLException;
	/**
	 * 执行存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 */
	public Long executeProcedure(String callSql, Object[] params) throws SQLException;
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
	public Map queryMapOutParamProcedure(String callSql, Object[] params);
	/**
	 * 加载存储过程字段
	 * @param callSql
	 * @param params
	 * @return
	 */
	public List<Model> loadProcedure(String callSql, Object[] params) throws SQLException;

	/**
	 * 加载存储过程字段
	 * @param callSql
	 * @param fieldVos
	 * @return
	 * @throws SQLException
	 */
	public List<Model> loadProcedure(String callSql, List<DbFieldVo> fieldVos) throws SQLException;
	/**
	 * 执行存储过程
	 * @param callSql
	 * @param fieldVos
	 * @return
	 */
	public Map queryMapProcedure(String callSql, List<DbFieldVo> fieldVos) throws Exception;
	/**
	 * 根据SQL语句查询结果(一般查询多个字段返回List<Object[]>)
	 * @param sql
	 * @return
	 */
	public List<Map> queryMapBySql(String sql);
	/**
	 * 根据SQL语句查询结果
	 * @param sql
	 * @param paramValues 设定参数值
	 * @return
	 */
	public List<Map> queryMapBySql(String sql, Object[] paramValues);

	/**
	 * 设定参数查询
	 * @param sql
	 * @param paramValues
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<Map> queryMapBySql(String sql, Object[] paramValues, int start, int limit);

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
	 * 加载存储过程字段
	 * @param sql
	 * @param fieldVos
	 * @return
	 * @throws Exception
	 */
	public List<Model> loadSql(String sql, List<DbFieldVo> fieldVos) throws Exception;

	/**
	 * 查询指定bean对象
	 * @param sql
	 * @param beanClass
	 * @param <T>
	 * @return
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public <T> List<T> queryBeanList(String sql, Class<T> beanClass) throws SQLException, InstantiationException, IllegalAccessException;

	/**
	 * 使用参数查询指定Bean对象
	 * @param sql
	 * @param beanClass
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T> List<T> queryBeanList(String sql, Class<T> beanClass, Object... params) throws SQLException, InstantiationException, IllegalAccessException;

	/**
	 * 自定义回调处理解析类
	 * @param sql
	 * @param rsc
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryBeanList(String sql, IResultSetCallable<T> rsc) throws SQLException;

	/**
	 * 自定义回调处理解析类
	 * @param sql
	 * @param rsc
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryBeanList(String sql, IResultSetCallable<T> rsc, Object... params) throws SQLException;

	/**
	 * 查询Bean
	 * @param sql
	 * @param beanClass
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 */
	public <T> T queryBean(String sql, Class<T> beanClass) throws SQLException, InstantiationException, IllegalAccessException;

	/**
	 * 查询Bean
	 * @param sql
	 * @param beanClass
	 * @param params
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 */
	public <T> T queryBean(String sql, Class<T> beanClass, Object... params) throws SQLException, InstantiationException, IllegalAccessException;

	/**
	 * 关闭连接
	 * @throws SQLException
	 */
	public void close() throws SQLException;
}

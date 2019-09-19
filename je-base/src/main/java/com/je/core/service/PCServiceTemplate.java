package com.je.core.service;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.entity.BaseEntity;
import com.je.core.entity.QueryInfo;
import com.je.core.entity.TreeBaseEntity;
import com.je.core.entity.extjs.JSONTreeNode;

/**
 * 平台和行服务接口
 * @author YUNFENGCHENG
 * 2011-9-1 上午11:15:56
 */
public interface PCServiceTemplate {

    /**
     * 执行SQL
     * @param sql
     * @return
     */
    public Long executeSql(String sql);
    /**
     * 执行SQL
     * @param sql
     * @param params 占位参数值
     * @return
     */
    public Long executeSql(String sql,Object[] params);
    /**
     * 执行SQL
     * @param sql
     * @param params 占位参数值
     * @return
     */
    public Long executeSql(String sql,Map<String,Object> params);
    /**
     * 建议用在列表更新
     * 更新实体 PCAT平台使用的更新采用拼接SQL方法而不采用HIBERNATE自带更新对象方法
     * @param updateSqls
     * @return
     */
    public int listUpdate(String[] updateSqls);
    /**
     * 调用存储过程
     * @param procedureName
     */
    @Deprecated
    public void executeProcedureByName(String procedureName);

    /**
     * 根据SQL语句查询结果(一般查询多个字段返回List<Object[]>)
     * @param sql
     * @return
     */
    public List<?> queryBySql(String sql);
    /**
     * 根据SQL语句查询结果(一般查询多个字段返回List<Object[]>)
     * @param sql
     * @param params 占位参数值
     * @return
     */
    public List<?> queryBySql(String sql,Object[] params);
    /**
     * 根据SQL语句查询结果(一般查询多个字段返回List<Object[]>)
     * @param sql
     * @param params 占位参数值
     * @return
     */
    public List<?> queryBySql(String sql,Map<String,Object> params);
    /**
     * 根据SQL语句查询结果(一般查询多个字段返回List<Object[]>)
     * @param sql
     * @return
     */
    public List<Map> queryMapBySql(String sql);
    /**
     * 根据SQL语句查询结果
     * @param sql
     * @param params 占位参数值
     * @return
     */
    public List<Map> queryMapBySql(String sql, Object[] params);
    /**
     * 根据SQL语句查询结果
     * @param sql
     * @param params 占位参数值
     * @return
     */
    public List<Map> queryMapBySql(String sql, Map<String,Object> params);
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
    public List<Map> queryMapBySql(String sql, Map<String,Object> params, int start, int limit);
    /**
     * 查询SQL分页查询
     * @param sql
     * @param fieldVos
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
     * 根据Sql分页查询集合
     * @param sql
     * @param start
     * @return
     */
    public List<?> queryBySql(String sql, int start, int limit, Class<?> c);
    /**
     * 根据Sql分页查询集合
     * @param sql
     * @param  params
     * @param start
     * @return
     */
    public List<?> queryBySql(String sql,Object[] params, int start, int limit, Class<?> c);
    /**
     * 根据Sql分页查询集合
     * @param sql
     * @param start
     * @return
     */
    public List<?> queryBySql(String sql,Map<String,Object> params, int start, int limit, Class<?> c);
    /**
     * SQL count分页方法
     * @param sql
     */
    public Long countBySql(String sql);
    /**
     * SQL count分页方法
     * @param sql
     */
    public Long countBySql(String sql,Object[] params);
    /**
     * SQL count分页方法
     * @param sql
     */
    public Long countBySql(String sql,Map<String,Object> params);
    /***
     * 通过SQL查询一个实体BEAN列表
     * @param sql
     * @param c
     * @return
     */
    public List<?> queryBySql(String sql, Class<?> c);
    /***
     * 通过SQL查询一个实体BEAN列表
     * @param sql
     * @param c
     * @return
     */
    public List<?> queryBySql(String sql,Object[] params, Class<?> c);
    /***
     * 通过SQL查询一个实体BEAN列表
     * @param sql
     * @param c
     * @return
     */
    public List<?> queryBySql(String sql,Map<String,Object> params, Class<?> c);

    /**
     * 根据sql语句批量更新   [{sql:""},{sql:""}]
     */
    public void updateBatch(String jsonSql);
    /**
     * 取得JSONTreeNode
     * chenmeng
     * 2012-1-9 下午03:58:50
     * @param aList
     * @return
     */
    public JSONTreeNode buildJSONTree(List<JSONTreeNode> aList);
    /**
     * 递归查询树形数据
     * @param rootId 根节点ID
     * @param tableName 表名
     * @param template 模版
     * @param queryInfo 查询对象
     * @param includeIds 包含主键
     * @param beanFields
     * @return
     */
    public List<JSONTreeNode> getJsonTreeNodeList(String rootId, String tableName, JSONTreeNode template, QueryInfo queryInfo, List<String> includeIds, String[] beanFields);

    /**
     * 查询树形数据集合
     * @param rootId 根节点ID
     * @param tableName 表名
     * @param template 树形模版类
     * @param queryInfo 查询信息
     * @return
     */
    public List<JSONTreeNode> getJsonTreeNodeList(String rootId, String tableName, JSONTreeNode template, QueryInfo queryInfo);

    /**
     * 根据根ID，表名，和模板指定的表中字段名封装一棵抽象树（不带查询条件,不带包含ID）
     * @param rootId
     * @param tableName
     * @param template
     * @return
     */
    public List<JSONTreeNode> getJsonTreeNodeList(String rootId, String tableName, JSONTreeNode template);
    /**
     * 根据parentId读取一层树元素
     * @param rootId
     * @param tableName
     * @param template
     * @param queryInfo
     * @return
     */
    public abstract List<JSONTreeNode> getAsynJsonTreeNodeList(String rootId, String tableName, JSONTreeNode template, QueryInfo queryInfo, Boolean isRoot, Boolean onlyWhereSql);
    /**
     * 为异步树请求下一层结点的子数量
     * @param rootId
     * @param tableName
     * @param template
     * @param queryInfo
     * @return
     */
    public abstract Long getAsynJsonTreeNodeCount(String rootId, String tableName, JSONTreeNode template, QueryInfo queryInfo);

    /**
     * 通过SQL获取结果集字段列表名
     * @param sql
     * @return
     */
    public List<String> getColumnListBySql(String sql);
    /**
     * 根据ids主键列表停用一组记录
     * @param c
     * @param ids
     */
    public void doDisable(Class<? extends BaseEntity> c, String[] ids);
    /**
     * 获取sessionFactory
     * @return
     */
    public SessionFactory getSessionFactory();
    /**
     * 获取数据库连接
     * @return
     */
    public Connection getConnection();
    /**
     * 将list递归成树   替换原有的for循环嵌套
     * @param lists
     * @return
     */
    public JSONTreeNode buildJSONNewTree(List<JSONTreeNode> lists, String rootId);



    /**----------------------原hibernate的实体类操作，HQL语句 后续直接删除-----------------------------**/
    /**
     * 保存实体
     * @param entity
     * @return 保存成功返回新的对象,不成返回false
     */
    public Object save(Object entity);
    /**
     * 更新实体
     * @param entity
     * @return
     */
    public Object updateEntity(Object entity);
    /**
     * 建议用在表单更新
     * 更新实体(纯HIBERNATE操作)
     * @param entity
     * @return
     */
    public Object formUpdate(Object entity);

    /**
     * 针对某一实体类型进行指定ID删除
     * @param c
     * @param id
     */
    public void removeEntityById(Class<BaseEntity> c, Serializable id);

    /**
     * 针对某一实体类型进行指定ID获取
     * @param c
     * @param id
     * @return
     */
    public BaseEntity getEntityById(Class<?> c, Serializable id);

    /**
     * 按一个唯一约束字段获取单个实体
     * @param c
     * @param columnName
     * @param value
     * @return
     */
    public Object getByUniqueValue(Class<BaseEntity> c, String columnName, Object value);

    /**
     * 执行HQL
     * @param hql
     * @return
     */
    public Long executeHql(String hql);

    /**
     * 根据HQL查询实体列表
     * @param hql
     * @return
     */
    public List<?> queryByHql(String hql);

    /**
     * 根据HQL分页查询实体列表
     * @param hql
     * @param start
     * @return
     */
    public List<?> queryByHql(String hql, int start, int limit);
    /**
     * 根据实体模板like获取实体列表
     * 1.不支持主键
     * 2.不支持关联
     * 3.不支持NULL
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
     * @param example
     * @param start
     * @param limit
     * @return
     */
    public List<?> queryByExample(BaseEntity example, int start, int limit);

    /**
     * HQL count分页方法
     * @param hql
     */
    public Long countByHql(String hql);
    /**
     * 保存treeEntity
     * 之前的方法中，前台不能传layer和nodeType，因此在后台实现。
     * 只在DictionaryItem中调用
     * chenmeng
     * 2012-1-6 下午01:36:40
     * @param treeEntity
     * @return
     */
    public Object saveTreeEntity(TreeBaseEntity treeEntity);
    /**
     * 可选使用缓存
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
     * 根据ID列表批量删除
     * @param c
     * @param ids
     */
    public void removeEntitiesByIds(Class<?> c, String[] ids);
}

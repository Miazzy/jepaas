package com.je.core.sql;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.util.DbProduceUtil;
import com.je.core.util.EntityUtils;
import com.je.core.util.ReflectionUtils;
import com.je.core.util.bean.BeanUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 构建纯参数格式的SQL QUERY
 *
 * @ProjectName: je-platform
 * @Package: com.je.core.sql
 * @ClassName: BuildingParamsQuery
 * @Description: 构建纯参数格式的SQL QUERY，主要防止SQL注入
 * @Author: LIULJ
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class BuildingParamsQuery {

    private static final Logger logger = LoggerFactory.getLogger(BuildingQuery.class);

    private static EntityUtils entityUtils;
    private static ReflectionUtils reflectionUtils;
    private static BeanUtils beanUtils;
    private static BuildingParamsQuery BUILDING_PARAMS_QUERY;

    public BuildingParamsQuery() {
        beanUtils = BeanUtils.getInstance();
        entityUtils = EntityUtils.getInstance();
        reflectionUtils = ReflectionUtils.getInstance();
    }

    public static BuildingParamsQuery getInstance(){
        if(BUILDING_PARAMS_QUERY == null){
            synchronized (BuildingParamsQuery.class){
                if(BUILDING_PARAMS_QUERY == null){
                    BUILDING_PARAMS_QUERY = new BuildingParamsQuery();
                }
            }
        }
        return BUILDING_PARAMS_QUERY;
    }

    /**
     * 在当前session中，根据索引创建新的SQL Query
     * @param sessionFactory
     * @param sql
     * @param indexParams
     * @return
     */
    public SQLQuery acquireSqlQuery(SessionFactory sessionFactory,String sql,Object... indexParams) {
        SQLQuery sqlQuery=sessionFactory.getCurrentSession().createSQLQuery(sql);
        fillQueryParamsByIndexes(sqlQuery,indexParams);
        return sqlQuery;
    }

    /**
     * 在当前session中,根据名称参数创建新的SQL Query
     * @param sessionFactory
     * @param sql
     * @param nameParams
     * @return
     */
    public SQLQuery acquireSqlQuery(SessionFactory sessionFactory,String sql,Map<String,Object> nameParams) {
        SQLQuery sqlQuery=sessionFactory.getCurrentSession().createSQLQuery(sql);
        fillQueryParamsByNames(sqlQuery,nameParams);
        return sqlQuery;
    }

    /**
     * 在当前session中，根据名称
     * @param sessionFactory
     * @param sql
     * @param indexParams
     * @return
     */
    public SQLQuery acquireMapSqlQuery(SessionFactory sessionFactory,String sql,Object... indexParams) {
        SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sql);
        fillQueryParamsByIndexes(sqlQuery,indexParams);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery;
    }

    public SQLQuery acquireMapSqlQuery(SessionFactory sessionFactory,String sql,Map<String,Object> nameParams) {
        SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sql);
        fillQueryParamsByNames(sqlQuery,nameParams);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery;
    }

    public SQLQuery acquireProcedureQuery(SessionFactory sessionFactory,String callSql,Object... indexParams){
        SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(callSql);
        fillQueryParamsByIndexes(query,indexParams);
        return query;
    }

    public SQLQuery acquireProcedureQuery(SessionFactory sessionFactory,String callSql,Map<String,Object> nameParams){
        SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(callSql);
        fillQueryParamsByIndexes(query,nameParams);
        return query;
    }

    public Query acquireMapProcedureQuery(SessionFactory sessionFactory,String callSql,Object... indexParams){
        Query query = acquireProcedureQuery(sessionFactory,callSql,indexParams);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query;
    }

    public Query acquireMapProcedureQuery(SessionFactory sessionFactory,String callSql,Map<String,Object> nameParams){
        Query query = acquireProcedureQuery(sessionFactory,callSql,nameParams);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query;
    }

    public Query getMapSqlQuery(String sql, List<DbFieldVo> fieldVos, SessionFactory sessionFactory) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString());
        if(fieldVos!=null){
            try {
                DbProduceUtil.setQueryParams(query, fieldVos);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query;
    }

    /**
     * 在当前session中获取NamedQuery
     * chenmeng
     * 2011-12-30 上午11:17:31
     * @param name
     * @param sessionFactory
     * @return
     */
    public Query acquireNamedQuery(String name, SessionFactory sessionFactory) {
        return sessionFactory.getCurrentSession().getNamedQuery(name);
    }

    /**
     * 填充数据库操作条件的占位符
     * @param sqlQuery
     * @param indexParams
     */
    public void fillQueryParamsByIndexes(SQLQuery sqlQuery, Object... indexParams){
        for(int i=0;i<indexParams.length;i++){
            fillQueryFieldParamByIndex(sqlQuery,i,indexParams[i]);
        }
    }

    /**
     * 填充数据库操作条件的占位符
     * @param sqlQuery 构建的查询对象
     * @param nameParams 字段名，值对象
     */
    public void fillQueryParamsByNames(SQLQuery sqlQuery, Map<String,Object> nameParams){
        for (Map.Entry<String,Object> eachEntry:nameParams.entrySet()) {
            fillQueryFieldParamByName(sqlQuery,eachEntry.getKey(),eachEntry.getValue());
        }
    }


    /**
     * 根据名称填充参数值
     * @param sqlQuery
     * @param name
     * @param paramVal
     */
    private void fillQueryFieldParamByName(SQLQuery sqlQuery,String name,Object paramVal){
        Type patamType = acquireFieldType(paramVal);
        if (patamType == null) {
            sqlQuery.setParameter(name, paramVal, StandardBasicTypes.STRING);
        }else{
            sqlQuery.setParameter(name, paramVal);
        }
    }

    /**
     * 根据索引填充参数值
     * @param sqlQuery
     * @param index
     * @param paramVal
     */
    private void fillQueryFieldParamByIndex(SQLQuery sqlQuery,Integer index,Object paramVal){
        Type patamType = acquireFieldType(paramVal);
        if(patamType == null){
            sqlQuery.setParameter(index, paramVal);
        }else{
            sqlQuery.setParameter(index, paramVal, patamType);
        }
    }

    /**
     * 获取值类型
     * @param paramVal 参数值
     * @return
     */
    private Type acquireFieldType(Object paramVal){
        Type resultType = null;
        if (paramVal instanceof String) {
            resultType = StandardBasicTypes.STRING;
        } else if (paramVal instanceof Integer) {
            resultType = StandardBasicTypes.INTEGER;
        } else if (paramVal instanceof Float) {
            resultType = StandardBasicTypes.FLOAT;
        } else if (paramVal instanceof Double) {
            resultType = StandardBasicTypes.DOUBLE;
        } else if (paramVal instanceof Date) {
            resultType = StandardBasicTypes.DATE;
        } else if (paramVal instanceof BigDecimal) {
            resultType = StandardBasicTypes.BIG_DECIMAL;
        } else if (paramVal instanceof Boolean) {
            resultType = StandardBasicTypes.BOOLEAN;
        } else if (paramVal instanceof Long) {
            resultType = StandardBasicTypes.LONG;
        } else if (paramVal instanceof Short) {
            resultType = StandardBasicTypes.SHORT;
        } else if (paramVal instanceof BigInteger) {
            resultType = StandardBasicTypes.BIG_INTEGER;
        }
        return resultType;
    }

}

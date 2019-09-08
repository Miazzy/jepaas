package com.je.datasource.runner;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.hibernate.Query;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.constants.ConstantVars;
import com.je.core.dao.PCDaoTemplateImpl;
import com.je.core.entity.extjs.Model;
import com.je.core.util.DBSqlUtils;
import com.je.core.util.DbProduceUtil;
import com.je.core.util.JdbcUtil;
import com.je.core.util.StringUtil;
import com.je.datasource.DataSourceContext;
import com.je.datasource.callable.IResultSetCallable;

import oracle.jdbc.OracleTypes;

/**
 * Copyright: Copyright (c) 2018 jeplus.cn
 * @Description: SQL执行器
 * @version: v1.0.0
 * @author: LIULJ
 * @date: 2018年4月16日 下午9:06:13
 *
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年4月16日     LIULJ           v1.0.0               初始创建
 *
 *
 */
public class SqlRunner implements IRunner {

    private static Map<String, SqlRunner> sqlRunnerMap = new HashMap<String, SqlRunner>();
    public static SqlRunner getInstance(String dataSourceName) {
        if(sqlRunnerMap.get(dataSourceName) == null) {
            sqlRunnerMap.put(dataSourceName, new SqlRunner(dataSourceName));
        }
        return sqlRunnerMap.get(dataSourceName);
    }

    /**
     * SQL执行器上下文
     */
    protected DataSourceContext dataSourceContext;

    /**
     * 获取执行上下文
     * @return
     */
    @Override
    public DataSourceContext getDataSourceContext() {
        return dataSourceContext;
    }

    public SqlRunner(String dataSourceName) {
        this.dataSourceContext = new DataSourceContext(dataSourceName);
    }

    /**
     * 执行SQL语句
     * @param sql
     * @return
     * @throws SQLException
     */
    @Override
    public int execute(String sql) throws SQLException {
        Statement stmt = null;
        Connection con=null;
        try {
            con= dataSourceContext.getConnection();
            stmt = con.createStatement();
            return stmt.executeUpdate(sql);
        }finally {
            JdbcUtil.close(null, stmt, con);
        }
    }

    /**
     * 执行带参数SQL语句
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public int execute(String sql, Object... params) throws SQLException {
        PreparedStatement preStmt = null;
        Connection con=null;
        try {
            con= dataSourceContext.getConnection();
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            return preStmt.executeUpdate();
        } finally {
            JdbcUtil.close(null,preStmt, con);
        }
    }

    /**
     * 执行SQL批处理
     * @param sqlArray
     * @return
     * @throws SQLException
     */
    @Override
    public int[] executeAsBatch(String[] sqlArray) throws SQLException {
        Statement stmt = null;
        Connection con=null;
        try {
            con= dataSourceContext.getConnection();
            stmt = con.createStatement();
            for (String sql : sqlArray) {
                stmt.addBatch(sql);
            }
            return stmt.executeBatch();
        } finally {
            JdbcUtil.close(null, stmt, con);
        }
    }

    /**
     * 执行SQL批处理
     * @param sqlList
     * @return
     * @throws SQLException
     */
    public int[] executeAsBatch(List<String> sqlList) throws SQLException {
        return executeAsBatch(sqlList.toArray(new String[] {}));
    };

//	@Override
//	public void executeProcedure(String procedureName, Object... params) throws SQLException {
//		CallableStatement proc = null;
//        try {
//        	Connection con = dataSourceContext.getConnection();
//            proc = con.prepareCall(procedureName);
//            for (int i = 0; i < params.length; i++) {
//                proc.setObject(i + 1, params[i]);
//            }
//            proc.execute();
//        } finally {
//            if (null != proc)
//                proc.close();
//        }
//	}

    /**
     * 加载存储过程字段
     * @param callSql
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public List<Model> loadProcedure(String callSql, Object[] params) throws SQLException {
        // TODO Auto-generated method stub
        List<Model> models=new ArrayList<Model>();
        CallableStatement proc = null;
        ResultSet rs = null;
        Connection con=null;
        try {
            con= dataSourceContext.getConnection();
            proc = con.prepareCall(callSql);
            setExecuteParams(proc, params);
            rs = proc.executeQuery();
            ResultSetMetaData data = rs.getMetaData();
            int count = data.getColumnCount();
            for (int i = 1; i <= count; i++) {
                // 获得指定列的列名
                String columnName = data.getColumnName(i);
                Integer type=data.getColumnType(i);
                Model model=DBSqlUtils.getExtModel(columnName, type);
                if(model!=null){
                    models.add(model);
                }
            }
        } finally {
            JdbcUtil.close(rs, proc, con);
        }
        return models;
    }

    /**
     * 加载存储过程字段
     * @param callSql
     * @param fieldVos
     * @return
     * @throws SQLException
     */
    @Override
    public List<Model> loadProcedure(String callSql, List<DbFieldVo> fieldVos) throws SQLException {
        // TODO Auto-generated method stub
        List<Model> models=new ArrayList<Model>();
        CallableStatement proc = null;
        ResultSet rs = null;
        Connection con=null;
        try {
            con = dataSourceContext.getConnection();
            proc = con.prepareCall(callSql);
            DbProduceUtil.registerParams(proc, fieldVos);
            rs = proc.executeQuery();
            ResultSetMetaData data = rs.getMetaData();
            int count = data.getColumnCount();
            for (int i = 1; i <= count; i++) {
                // 获得指定列的列名
                String columnName = data.getColumnName(i);
                Integer type=data.getColumnType(i);
                Model model=DBSqlUtils.getExtModel(columnName, type);
                if(model!=null){
                    models.add(model);
                }
            }
        }catch(Exception e){
            throw new PlatformException("调用存储过程异常", PlatformExceptionEnum.JE_CORE_DSFDB_PROCEDURE_ERROR,new Object[]{callSql,fieldVos},e);
        } finally {
            JdbcUtil.close(rs, proc, con);
        }
        return models;
    }

    /**
     * 加载存储过程字段
     * @param sql
     * @param fieldVos
     * @return
     * @throws Exception
     */
    @Override
    public List<Model> loadSql(String sql, List<DbFieldVo> fieldVos)  throws Exception{
        // TODO Auto-generated method stub
        List<Model> models=new ArrayList<Model>();
        Connection connection=null;
        CallableStatement proc=null;
        ResultSet resultSet=null;
        try{
            connection=dataSourceContext.getConnection();
            proc = connection.prepareCall(sql);
            DbProduceUtil.registerParams(proc, fieldVos);
            proc.execute();
            resultSet = proc.getResultSet();
            ResultSetMetaData data = resultSet.getMetaData();
            int count = data.getColumnCount();
            for (int i = 1; i <= count; i++) {
                // 获得指定列的列名
                String columnName = data.getColumnName(i);
                Integer type=data.getColumnType(i);
                Model model=DBSqlUtils.getExtModel(columnName, type);
                if(model!=null){
                    models.add(model);
                }
            }
        }catch(Exception e){
            throw new PlatformException("查询SQL数据异常", PlatformExceptionEnum.JE_CORE_DSFDB_SQL_ERROR,new Object[]{sql,fieldVos},e);
        }finally{
            JdbcUtil.close(resultSet, proc, connection);
        }
        return models;
    }

    /**
     * 执行存储过程
     * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
     * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
     * @return
     * @throws SQLException
     */
    @Override
    public Long executeProcedure(String callSql, Object[] params) throws SQLException {
        // TODO Auto-generated method stub
        long result=-1;
        CallableStatement proc = null;
        Connection con=null;
        try {
            con= dataSourceContext.getConnection();
            proc = con.prepareCall(callSql);
            setExecuteParams(proc, params);
            Boolean flag=proc.execute();
            if(flag){
                result=1;
            }
        } finally {
            JdbcUtil.close(null, proc, con);
        }
        return result;
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

    /**
     * 执行查询存储过程
     * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
     * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
     * @return
     */
    @Override
    public List<Map> queryMapProcedure(String callSql, Object[] params){
        // TODO Auto-generated method stub
        List<Map> lists = new ArrayList<Map>();
        CallableStatement proc = null;
        ResultSet rs = null;
        Connection con=null;
        try {
            con= dataSourceContext.getConnection();
            proc = con.prepareCall(callSql);
            setExecuteParams(proc, params);
            rs = proc.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (null != rs && rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(name);
                    if(value instanceof Clob){
                        map.put(name, StringUtil.getClobValue(value));
                    }else {
                        map.put(name, value);
                    }
                }
                lists.add(map);
            }
        }catch(SQLException e){
            throw new PlatformException("调用存储过程异常", PlatformExceptionEnum.JE_CORE_DSFDB_PROCEDURE_ERROR,new Object[]{callSql,params},e);
        } finally {
            JdbcUtil.close(rs, proc, con);
        }
        return lists;
    }

    /**
     * 执行查询存储过程
     * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
     * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
     * @return
     */
    @Override
    public Map queryMapOutParamProcedure(String callSql, Object[] params){
        // TODO Auto-generated method stub
        List<Map> lists = new ArrayList<Map>();
        List outParamValues=new ArrayList();
        CallableStatement proc = null;
        ResultSet rs = null;
        Connection con=null;
        try {
            con=dataSourceContext.getConnection();
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
                    if(value instanceof Clob){
                        map.put(name, StringUtil.getClobValue(value));
                    }else {
                        map.put(name, value);
                    }
                }
                lists.add(map);
            }
            for(Integer outIndex:outParams){
                outParamValues.add(rs.getObject(outIndex));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new PlatformException("查询SQL数据异常", PlatformExceptionEnum.JE_CORE_DSFDB_SQL_ERROR,new Object[]{callSql,params},e);
        } finally {
            JdbcUtil.close(rs, proc, con);
        }
        Map res=new HashMap();
        res.put("out", outParamValues);
        res.put("resultset", lists);
        return res;
    }

    /**
     * 执行存储过程
     * @param callSql
     * @param fieldVos
     * @return
     */
    @Override
    public Map queryMapProcedure(String callSql, List<DbFieldVo> fieldVos){
        // TODO Auto-generated method stub
        Connection con=null;
        CallableStatement proc = null;
        Map returnObj=new HashMap();
        try {
            con=dataSourceContext.getConnection();
            proc = con.prepareCall(callSql);
            DbProduceUtil.registerParams(proc, fieldVos);
            DbProduceUtil.loadResultSetParams(proc, fieldVos, returnObj);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new PlatformException("调用存储过程异常", PlatformExceptionEnum.JE_CORE_DSFDB_PROCEDURE_ERROR,new Object[]{callSql,fieldVos},e);
        }finally {
            JdbcUtil.close(null, proc, con);
        }
        return returnObj;
    }

    /**
     * 执行查询
     * @param sql
     * @return
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> queryMapList(String sql) throws SQLException {
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        Statement preStmt = null;
        ResultSet rs = null;
        Connection con=null;
        try {
            con= dataSourceContext.getConnection();
            preStmt = con.createStatement();
            rs = preStmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (null != rs && rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(name);
                    if(value instanceof Clob){
                        map.put(name, StringUtil.getClobValue(value));
                    }else {
                        map.put(name, value);
                    }
                }
                lists.add(map);
            }
        } finally {
            JdbcUtil.close(rs, preStmt, con);
        }
        return lists;
    }

    /**
     * 执行带参查询
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> queryMapList(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con=null;
        try {
            con= dataSourceContext.getConnection();
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            rs = preStmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (null != rs && rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(name);
                    if(value instanceof Clob){
                        map.put(name, StringUtil.getClobValue(value));
                    }else {
                        map.put(name, value);
                    }
                }
                lists.add(map);
            }
        } finally {
            JdbcUtil.close(rs, preStmt, con);
        }
        return lists;
    }

    /**
     * 根据SQL语句查询结果(一般查询多个字段返回List<Object[]>)
     * @param sql
     * @return
     */
    @Override
    public List<Map> queryMapBySql(String sql) {
        // TODO Auto-generated method stub
        Connection con=null;
        CallableStatement proc = null;
        ResultSet rs=null;
        List<Map> lists = new ArrayList<Map>();
        try {
            con=dataSourceContext.getConnection();
            proc = con.prepareCall(sql);
            rs=proc.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            //设定输出参数
            int columnCount = rsmd.getColumnCount();
            while (null != rs && rs.next()) {
                Map<String,Object> map=new HashMap<String,Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Integer type=rsmd.getColumnType(i+1);
                    Object value = DbProduceUtil.getObject(rs, name, type);
                    if(value instanceof Clob){
                        map.put(name, StringUtil.getClobValue(value));
                    }else {
                        map.put(name, value);
                    }
                }
                lists.add(map);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new PlatformException("查询SQL数据异常", PlatformExceptionEnum.JE_CORE_DSFDB_SQL_ERROR,new Object[]{sql},e);
        }finally {
            JdbcUtil.close(rs, proc, con);
        }
        return lists;
    }

    /**
     * 根据SQL语句查询结果
     * @param sql
     * @param params
     * @return
     */
    @Override
    public List<Map> queryMapBySql(String sql, Object[] params) {
        // TODO Auto-generated method stub
        List<Map> lists = new ArrayList<Map>();
        CallableStatement proc = null;
        ResultSet rs = null;
        Connection con=null;
        try {
            con = dataSourceContext.getConnection();
            proc = con.prepareCall(sql);
            setExecuteParams(proc, params);
            rs = proc.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (null != rs && rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(name);
                    if(value instanceof Clob){
                        map.put(name, StringUtil.getClobValue(value));
                    }else {
                        map.put(name, value);
                    }
                }
                lists.add(map);
            }
        }catch(SQLException e){
            throw new PlatformException("查询SQL数据异常", PlatformExceptionEnum.JE_CORE_DSFDB_SQL_ERROR,new Object[]{sql,params},e);
        } finally {
            JdbcUtil.close(rs, proc, con);
        }
        return lists;
    }

    /**
     * 设定参数查询
     * @param sql
     * @param params
     * @param start
     * @param limit
     * @return
     */
    @Override
    public List<Map> queryMapBySql(String sql, Object[] params, int start,int limit) {
        // TODO Auto-generated method stub
        List<Map> lists = new ArrayList<Map>();
        CallableStatement proc = null;
        ResultSet rs = null;
        Connection con=null;
        try {
            con= dataSourceContext.getConnection();
            sql=DbProduceUtil.buildPageSql(sql, start, limit);
            proc = con.prepareCall(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            setExecuteParams(proc, params);
            if(limit>0){
                proc.setMaxRows(start+limit);
            }
            rs = proc.executeQuery();
            if(limit>0){
                rs.absolute(start);
            }
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (null != rs && rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(name);
                    if(value instanceof Clob){
                        map.put(name, StringUtil.getClobValue(value));
                    }else {
                        map.put(name, value);
                    }
                }
                lists.add(map);
            }
        }catch(SQLException e){
            throw new PlatformException("查询SQL数据异常", PlatformExceptionEnum.JE_CORE_DSFDB_SQL_ERROR,new Object[]{sql,params,start,limit},e);
        } finally {
            JdbcUtil.close(rs, proc, con);
        }
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
    @Override
    public List<Map> queryMapBySql(String sql, List<DbFieldVo> fieldVos,int start, int limit) {
        // TODO Auto-generated method stub
        Connection con=null;
        CallableStatement proc = null;
        ResultSet rs=null;
        List<Map> lists = new ArrayList<Map>();
        try {
            con=dataSourceContext.getConnection();
            if(limit>0){
                sql=DbProduceUtil.buildPageSql(sql,start,limit);
            }else{
                sql=DbProduceUtil.buildQuerySql(sql);
            }
            proc = con.prepareCall(sql);
            DbProduceUtil.registerParams(proc, fieldVos);
            rs=proc.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            //设定输出参数
            int columnCount = rsmd.getColumnCount();
            int beforeIndex=0;
            if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_SQLSERVER)){
                beforeIndex=start;
            }
            int nowIndex=0;
            while (null != rs && rs.next()) {
                if(nowIndex<beforeIndex){
                    nowIndex++;
                    continue;
                }
                Map<String,Object> map=new HashMap<String,Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Integer type=rsmd.getColumnType(i+1);
                    Object value = DbProduceUtil.getObject(rs, name, type);
                    if(value instanceof Clob){
                        map.put(name, StringUtil.getClobValue(value));
                    }else {
                        map.put(name, value);
                    }
                }
                lists.add(map);
                nowIndex++;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new PlatformException("查询SQL数据异常", PlatformExceptionEnum.JE_CORE_DSFDB_SQL_ERROR,new Object[]{sql,fieldVos,start,limit},e);
        }
        finally {
            JdbcUtil.close(rs, proc, con);
        }
        return lists;
    }

    /**
     * 查询SQL分页查询
     * @param sql
     * @param fieldVos
     * @return
     */
    @Override
    public Long countMapBySql(String sql, List<DbFieldVo> fieldVos) {
        // TODO Auto-generated method stub
        Connection con=null;
        CallableStatement proc = null;
        ResultSet rs=null;
        Long allCount=new Long(0);
        try {
            con=dataSourceContext.getConnection();
            sql=DbProduceUtil.buildCountSql(sql);
            proc = con.prepareCall(sql);
            DbProduceUtil.registerParams(proc, fieldVos);
            rs=proc.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            //设定输出参数
            int columnCount = rsmd.getColumnCount();
            while (null != rs && rs.next()) {
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Integer type=rsmd.getColumnType(i+1);
                    Object value = DbProduceUtil.getObject(rs, name, type);
                    if(value!=null && StringUtil.isNotEmpty(value+"")){
                        allCount=Long.parseLong(value+"");
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new PlatformException("查询SQL数据异常", PlatformExceptionEnum.JE_CORE_DSFDB_SQL_ERROR,new Object[]{sql,fieldVos},e);
        }
        finally {
            JdbcUtil.close(rs, proc, con);
        }
        return allCount;
    }

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
    @Override
    public <T> List<T> queryBeanList(String sql, Class<T> beanClass) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> lists = new ArrayList<T>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con=null;
        Field[] fields = null;
        try {
            con= dataSourceContext.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            fields = beanClass.getDeclaredFields();
            for (Field f : fields)
                f.setAccessible(true);
            while (null != rs && rs.next()) {
                T t = beanClass.newInstance();
                for (Field f : fields) {
                    String name = f.getName();
                    Object value = rs.getObject(name);
                    setValue(t, f, value);
                }
                lists.add(t);
            }
        } finally {
            JdbcUtil.close(rs, stmt, con);
        }
        return lists;
    }

    /**
     * 使用参数查询指定Bean对象
     * @param sql
     * @param beanClass
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public <T> List<T> queryBeanList(String sql, Class<T> beanClass, Object... params) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> lists = new ArrayList<T>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con=null;
        Field[] fields = null;
        try {
            con= dataSourceContext.getConnection();
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            rs = preStmt.executeQuery();
            fields = beanClass.getDeclaredFields();
            for (Field f : fields)
                f.setAccessible(true);
            while (null != rs && rs.next()) {
                T t = beanClass.newInstance();
                for (Field f : fields) {
                    String name = f.getName();
                    Object value = rs.getObject(name);
                    setValue(t, f, value);
                }
                lists.add(t);
            }
        } finally {
            JdbcUtil.close(rs, preStmt, con);
        }
        return lists;
    }

    /**
     * 自定义回调处理解析类
     * @param sql
     * @param rsc
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> List<T> queryBeanList(String sql, IResultSetCallable<T> rsc) throws SQLException {
        List<T> lists = new ArrayList<T>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con=null;
        try {
            con = dataSourceContext.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (null != rs && rs.next())
                lists.add(rsc.invoke(rs));
        } finally {
            JdbcUtil.close(rs, stmt, con);
        }
        return lists;
    }

    /**
     * 自定义回调处理解析类
     * @param sql
     * @param rsc
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> List<T> queryBeanList(String sql, IResultSetCallable<T> rsc, Object... params) throws SQLException {
        List<T> lists = new ArrayList<T>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con=null;
        try {
            con = dataSourceContext.getConnection();
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);
            rs = preStmt.executeQuery();
            while (null != rs && rs.next())
                lists.add(rsc.invoke(rs));
        } finally {
            JdbcUtil.close(rs, preStmt, con);
        }
        return lists;
    }

    /**
     * 查询Bean
     * @param sql
     * @param beanClass
     * @param <T>
     * @return
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public <T> T queryBean(String sql, Class<T> beanClass) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> lists = queryBeanList(sql, beanClass);
        if (lists.size() >1){
            throw new PlatformException("查询一条数据异常,返回了多条", PlatformExceptionEnum.JE_CORE_DSFDB_QUERY_ONE_ERROR,new Object[]{sql});
        }else if (lists.size()==0){
            return lists.get(0);
        }else{
            return null;
        }
    }

    /**
     * 查询Bean
     * @param sql
     * @param beanClass
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public <T> T queryBean(String sql, Class<T> beanClass, Object... params) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> lists = queryBeanList(sql, beanClass, params);
        if (lists.size() >1){
            throw new PlatformException("查询一条数据异常,返回了多条", PlatformExceptionEnum.JE_CORE_DSFDB_QUERY_ONE_ERROR,new Object[]{sql});
        }else if (lists.size()==0){
            return lists.get(0);
        }else{
            return null;
        }
    }

    /**
     * 关闭连接
     * @throws SQLException
     */
    @Override
    public void close() throws SQLException {
        dataSourceContext.close();
    }

    private <T> void setValue(T t, Field f, Object value) throws IllegalAccessException {
        // TODO 以数据库类型为准绳，还是以java数据类型为准绳？还是混合两种方式？
        if (null == value)
            return;
        String v = value.toString();
        String n = f.getType().getName();
        if ("java.lang.Byte".equals(n) || "byte".equals(n)) {
            f.set(t, Byte.parseByte(v));
        } else if ("java.lang.Short".equals(n) || "short".equals(n)) {
            f.set(t, Short.parseShort(v));
        } else if ("java.lang.Integer".equals(n) || "int".equals(n)) {
            f.set(t, Integer.parseInt(v));
        } else if ("java.lang.Long".equals(n) || "long".equals(n)) {
            f.set(t, Long.parseLong(v));
        } else if ("java.lang.Float".equals(n) || "float".equals(n)) {
            f.set(t, Float.parseFloat(v));
        } else if ("java.lang.Double".equals(n) || "double".equals(n)) {
            f.set(t, Double.parseDouble(v));
        } else if ("java.lang.String".equals(n)) {
            f.set(t, value.toString());
        } else if ("java.lang.Character".equals(n) || "char".equals(n)) {
            f.set(t, (Character) value);
        } else if ("java.lang.Date".equals(n)) {
            f.set(t, new Date(((java.sql.Date) value).getTime()));
        } else if ("java.lang.Timer".equals(n)) {
            f.set(t, new Time(((Time) value).getTime()));
        } else if ("java.sql.Timestamp".equals(n)) {
            f.set(t, (java.sql.Timestamp) value);
        } else {
            System.out.println("SqlError：暂时不支持此数据类型，请使用其他类型代替此类型！");
        }
    }
}

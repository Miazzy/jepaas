package com.je.core.service;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.rbac.model.EndUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.je.core.entity.extjs.Model;
import com.je.core.util.DBSqlUtils;
import com.je.core.util.JdbcUtil;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;

/**
 * 根据SQL查询数据实现类
 */
@Component("pcDbSQLManager")
public class PcDbSQLManagerImpl implements PcDbSQLManager {
	@Autowired
	private PCServiceTemplate pcServiceTemplate;
	@Autowired
	private PCDynaServiceTemplate serviceTemplate;

	/**
	 * 查询数据
	 * @param sql sql语句
	 * @return
	 */
	@Override
	public List<Model> selectModel(String sql){
		// TODO Auto-generated method stub
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		List<Model> models=new ArrayList<Model>();
		try{
			connection=pcServiceTemplate.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
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
//			e.printStackTrace();
			throw new PlatformException("根据SQL获取列信息失败!", PlatformExceptionEnum.JE_CORE_SQL_MODEL_ERROR,new Object[]{sql},e);
		}finally{
			JdbcUtil.close(resultSet, statement, connection);
		}
		return models;
	}

	/**
	 * 查询出SQL的名称
	 * @param sql sql语句
	 * @return
	 */
	@Override
	public List<DynaBean> select(String sql){
		// TODO Auto-generated method stub
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		List<DynaBean> beans=new ArrayList<DynaBean>();
		try{
			connection=pcServiceTemplate.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			ResultSetMetaData data = resultSet.getMetaData();
			int count = data.getColumnCount();
			while (resultSet.next()) {
				DynaBean bean=new DynaBean();
				for (int i = 1; i <= count; i++) {
					// 获得指定列的列名
					String columnName = data.getColumnName(i);
					Object obj=resultSet.getObject(i);
					if(obj instanceof Clob){
						bean.set(columnName, StringUtil.getClobValue(obj));
					}else {
						String columnValue = StringUtil.getDefaultValue(obj, "");
						if(StringUtil.isNotEmpty(columnName)){
							bean.set(columnName, columnValue);
						}
					}
				}
				beans.add(bean);
			}
		}catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
			throw new PlatformException("根据SQL获取数据信息失败!", PlatformExceptionEnum.JE_CORE_SQL_DATA_ERROR,new Object[]{sql},e);
		}finally{
			JdbcUtil.close(resultSet, statement, connection);
		}
		return beans;
	}

	/**
	 * 查询出SQL的名称
	 * @param sql sql语句
	 * @param limit 分页
	 * @return
	 */
	@Override
	public List<DynaBean> select(String sql, int limit) {
		// TODO Auto-generated method stub
		List<DynaBean> beans=new ArrayList<DynaBean>();
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		try{
			connection=pcServiceTemplate.getConnection();
			statement = connection.createStatement();
			if(limit>0){
				statement.setMaxRows(limit);
			}
			resultSet = statement.executeQuery(sql);
			ResultSetMetaData data = resultSet.getMetaData();
			int count = data.getColumnCount();
			while (resultSet.next()) {
				DynaBean bean=new DynaBean();
				for (int i = 1; i <= count; i++) {
					// 获得指定列的列名
					String columnName = data.getColumnName(i);
					Object obj=resultSet.getObject(i);
					if(obj instanceof Clob){
						bean.set(columnName, StringUtil.getClobValue(obj));
					}else {
						String columnValue = StringUtil.getDefaultValue(obj, "");
						if(StringUtil.isNotEmpty(columnName)){
							bean.set(columnName, columnValue);
						}
					}
				}
				beans.add(bean);
			}
		}catch(Exception e){
			throw new PlatformException("根据SQL获取指定列数数据信息失败!", PlatformExceptionEnum.JE_CORE_SQL_DATA_ERROR,new Object[]{sql,limit},e);
		}finally{
			JdbcUtil.close(resultSet, statement, connection);
		}
		return beans;
	}

	/**
	 * 执行前台的事务操作
	 * @param arrays
	 * @param currentUser 当前登陆人信息
	 */
	@Override
	public void doTransaction(JSONArray arrays,EndUser currentUser) {
		// TODO Auto-generated method stub
		for(Integer i=0;i<arrays.size();i++){
			JSONObject task=arrays.getJSONObject(i);
			String taskKey=task.getString("taskKey");
			if("insert".equals(taskKey)){
				JSONObject config=task.getJSONObject("config");
				String tableCode=config.getString("tableCode");
				DynaBean dynaBean=new DynaBean(tableCode,true);
				dynaBean.setJsonValues(config.getJSONObject("values"));
				serviceTemplate.buildModelCreateInfo(dynaBean,currentUser);
				serviceTemplate.insert(dynaBean);
			}else if("remove".equals(taskKey)){
				JSONObject config=task.getJSONObject("config");
				String tableCode=config.getString("tableCode");
				String whereSql=config.getString("whereSql");
				serviceTemplate.deleteByWehreSql(tableCode, whereSql);
			}else if("removeByIds".equals(taskKey)){
				JSONObject config=task.getJSONObject("config");
				String tableCode=config.getString("tableCode");
				String ids=config.getString("ids");
				String pkCode=BeanUtils.getInstance().getPKeyFieldNames(tableCode);
				serviceTemplate.deleteByIds(ids, tableCode, pkCode);
			}else if("update".equals(taskKey)){
				JSONObject config=task.getJSONObject("config");
				String tableCode=config.getString("tableCode");
				DynaBean dynaBean=new DynaBean(tableCode,true);
				dynaBean.setJsonValues(config.getJSONObject("values"));
				serviceTemplate.buildModelModifyInfo(dynaBean,currentUser);
				serviceTemplate.update(dynaBean);
			}else if("updateBatch".equals(taskKey)){
				JSONObject config=task.getJSONObject("config");
				String tableCode=config.getString("tableCode");
				DynaBean dynaBean=new DynaBean(tableCode,true);
				dynaBean.setJsonValues(config.getJSONObject("values"));
				dynaBean.set(BeanUtils.KEY_WHERE, config.getString("whereSql"));
				serviceTemplate.listUpdate(dynaBean);
			}else if("sql".equals(taskKey)){
				String sql=task.getString("config");
				pcServiceTemplate.executeSql(sql);
			}
		}
	}
}

package com.je.core.service;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.je.cache.service.table.DynaCacheManager;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.facade.extjs.JsonAssist;
import com.je.core.util.*;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.constants.ConstantVars;
import com.je.core.constants.table.ColumnType;
import com.je.core.dao.PCDaoTemplateImpl;
import com.je.core.entity.extjs.DbIndex;
import com.je.core.entity.extjs.DbModel;
import com.je.core.entity.extjs.Model;
import com.je.datasource.runner.SqlRunner;

/**
 * 加载表跟SQL模型及结构
 */
@Component("pcDataService")
public class PCDataServiceImpl implements PCDataService{
	private static Logger logger = LoggerFactory.getLogger(PCDataServiceImpl.class);
	@Autowired
	private PCServiceTemplate pcServiceTemplate;

	/**
	 * 加载存储过程字段模型
	 * @param dataSource 数据来源
	 * @param procedureName 程序名称
	 * @param fieldVos TODO 暂不明确
	 * @return
	 */
	@Override
	public List<Model> loadProcedure(String dataSource,String procedureName,List<DbFieldVo> fieldVos) {
		// TODO Auto-generated method stub
		List<Model> models=new ArrayList<Model>();
		Object[] params=new Object[fieldVos.size()];
		Connection con=null;
		CallableStatement proc = null;
		ResultSet rs = null;
		try{
			if(StringUtil.isEmpty(dataSource) || "local".equals(dataSource)){
				con = pcServiceTemplate.getConnection();
				proc = con.prepareCall("{call "+procedureName+"("+getCallParams(params)+")}");
				DbProduceUtil.registerParams(proc, fieldVos);
				proc.execute();
				if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
					Integer index=DbProduceUtil.loadOutCursorIndex(fieldVos);
					if(index>0){
						rs=(ResultSet) proc.getObject(index);
					}else{
						rs=proc.getResultSet();
					}
				}else{
					rs=proc.getResultSet();
				}
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
			}else{
				SqlRunner sqlRunner=SqlRunner.getInstance(dataSource);
				return sqlRunner.loadProcedure("{call "+procedureName+"("+getCallParams(params)+")}", fieldVos);
			}
		}catch(Exception e){
			throw new PlatformException("根据数据源获取存储过程列信息异常!", PlatformExceptionEnum.JE_CORE_DB_COLUMN_PROCEDURE_ERROR,new Object[]{dataSource,procedureName,fieldVos},e);
//			e.printStackTrace();
		}finally{
			JdbcUtil.close(rs, proc, con);
		}
		return models;
	}
	private void setExecuteParams(CallableStatement proc,Object[] params) throws SQLException{
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
				}else{
					proc.setObject(i, params[i-1]);
				}
			}
		}
	}

	/**
	 * 加载SQL字段模型
	 * @param dataSource 数据来源
	 * @param sql sql语句
	 * @param fieldVos TODO 暂不明确
	 * @return
	 */
	@Override
	public List<Model> loadSql(String dataSource, String sql,List<DbFieldVo> fieldVos) {
		// TODO Auto-generated method stub
		List<Model> models=new ArrayList<Model>();
		Connection connection=null;
		CallableStatement proc=null;
		ResultSet resultSet=null;
		try {
			if(StringUtil.isEmpty(dataSource) || "local".equals(dataSource)){
				connection=pcServiceTemplate.getConnection();
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
			}else{
				SqlRunner sqlRunner=SqlRunner.getInstance(dataSource);
				return sqlRunner.loadSql(sql,fieldVos);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			throw new PlatformException("根据数据源获取SQL列信息异常!", PlatformExceptionEnum.JE_CORE_DB_COLUMN_SQL_ERROR,new Object[]{dataSource,sql,fieldVos},e);
		}finally{
			JdbcUtil.close(resultSet, proc, connection);
		}
		return models;
	}

	/**
	 * TODO 暂不明确
	 * @param tableCode
	 * @return
	 */
	@Override
	public Boolean existsTable(String tableCode) {
		// TODO Auto-generated method stub
		List<String> databaseTableNames = DataBaseUtils.getInstance().getDataBaseTableNames();
		return databaseTableNames.contains(tableCode);
	}

	/**
	 * 加载指定表名数据库字段模型
	 * @param tableCode 表code
	 * @return
	 */
	@Override
	public List<DbModel> loadTableColumn(String tableCode) {
		// TODO Auto-generated method stub
		List<DbModel> models=new ArrayList<DbModel>();
		String sql=" SELECT * FROM "+tableCode+" WHERE 1!=1";
		List<DbFieldVo> fieldVos=new ArrayList<DbFieldVo>();
		Connection connection=null;
		CallableStatement proc=null;
		ResultSet resultSet=null;
		try {
			connection=pcServiceTemplate.getConnection();
			proc = connection.prepareCall(sql);
			DbProduceUtil.registerParams(proc, fieldVos);
			proc.execute();
			resultSet = proc.getResultSet();
			ResultSetMetaData data = resultSet.getMetaData();
			int count = data.getColumnCount();
			for (int i = 1; i <= count; i++) {
				// 获得指定列的列名
				String columnName = data.getColumnName(i).toUpperCase();
//					Integer type=data.getColumnType(i);
				String type =  data.getColumnTypeName(i).toUpperCase();
				String typeLength=data.getPrecision(i)+"";
				int length=data.getPrecision(i);
				int scale=data.getScale(i);
				DbModel model=new DbModel();
				int nullAbleStatus=data.isNullable(i);
				if(nullAbleStatus==0){
					model.setNull(false);
				}else{
					model.setNull(true);
				}
				model.setCode(columnName);
//            	model.setLength(length);
				if(type.startsWith("VARCHAR")){
					model.setType(ColumnType.VARCHAR);
//						column.set("TABLECOLUMN_TYPE","VARCHAR");
					if("255".equals(length+"")){
						model.setType(ColumnType.VARCHAR255);
						model.setLength("");
					}else if("100".equals(length+"")){
						model.setType(ColumnType.VARCHAR100);
						model.setLength("");
					}else if("50".equals(length+"")){
						model.setType(ColumnType.VARCHAR50);
						model.setLength("");
					}else if("30".equals(length+"")){
						model.setType(ColumnType.VARCHAR30);
						model.setLength("");
					}else if("1000".equals(length+"")){
						model.setType(ColumnType.VARCHAR1000);
						model.setLength("");
					}else if("4000".equals(length+"")){
						model.setType(ColumnType.VARCHAR4000);
						model.setLength("");
					}else if("4".equals(length+"")){
						model.setType(ColumnType.YESORNO);
						model.setLength("");
					}else if(length>4){
						model.setType(ColumnType.CLOB);
						model.setLength("");
					}else if(length>0){
						model.setType(ColumnType.VARCHAR);
						model.setLength(length+"");
					}
				}else if(type.startsWith("INT")){
					model.setType(ColumnType.NUMBER);
					model.setLength("");
				}else if(type.startsWith("FLOAT") || type.startsWith("NUMERIC") || type.startsWith("DECIMAL") || type.startsWith("NUMBER")){
					if(scale<=0){
						model.setType(ColumnType.NUMBER);
						if(length==20){
							model.setLength("");
						}else{
							model.setLength(typeLength);
						}
					}else{
						model.setType(ColumnType.FLOAT);
						if(length==20){
							typeLength=scale+"";
						}else{
							typeLength=length+","+scale;
						}
						model.setLength(typeLength);
					}
				}else if(type.startsWith("TEXT") || type.startsWith("LONGTEXT") || type.startsWith("CLOB")){
					model.setType(ColumnType.CLOB);
					model.setLength("");

				}else{
					model.setType(ColumnType.CUSTOM);
					model.setLength(type);
				}
				models.add(model);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			throw new PlatformException("根据表名获取列信息异常!", PlatformExceptionEnum.JE_CORE_DB_COLUMN_TABLE_ERROR,new Object[]{tableCode},e);
		}finally{
			JdbcUtil.close(resultSet, proc, connection);
		}
		return models;
	}

	/**
	 * 加载指定表名数据库字段模型
	 * @param tableCode 表code
	 * @return
	 */
	@Override
	public List<DbModel> loadTableColumnBySql(String tableCode) {
		List<DbModel> models=new ArrayList<DbModel>();
		if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME)){
			List<Map> lists=pcServiceTemplate.queryMapBySql(" select COLUMN_NAME,DATA_TYPE,DATA_LENGTH,DATA_PRECISION,DATA_SCALE,NULLABLE from user_tab_columns WHERE TABLE_NAME='"+tableCode+"'");
			for(Map columnInfo:lists){
				String columnName=columnInfo.get("COLUMN_NAME")+"";
				String type=(columnInfo.get("DATA_TYPE")+"").toUpperCase();
				String typeLength=columnInfo.get("DATA_LENGTH")+"";
				String nullAble=columnInfo.get("NULLABLE")+"";
				int length=0;
				int scale=0;
				if(StringUtil.isNotEmpty(columnInfo.get("DATA_PRECISION")+"")){
					length=Integer.parseInt(columnInfo.get("DATA_PRECISION")+"");
				}
				if(StringUtil.isNotEmpty(columnInfo.get("DATA_SCALE")+"")){
					scale=Integer.parseInt(columnInfo.get("DATA_SCALE")+"");
				}
				models.add(buildDbModel(columnName, type, typeLength, length, scale, nullAble));
			}
		}else if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME)){
			List<Map> lists=pcServiceTemplate.queryMapBySql(" show columns from "+tableCode+"");
			for(Map columnInfo:lists){
				String columnName=columnInfo.get("Field")+"";
				String type=(columnInfo.get("Type")+"").toUpperCase();
				String typeLength="";
				String nullAble=columnInfo.get("Null")+"";
				int length=0;
				int scale=0;
				if("NO".equals(nullAble)){
					nullAble="N";
				}else{
					nullAble="Y";
				}
				if(type.indexOf("(")!=-1){
					typeLength=type.substring(type.indexOf("(")+1,type.indexOf(")"));
					if(typeLength.indexOf(",")!=-1){
						length=Integer.parseInt(typeLength.split(",")[0]);
						scale=Integer.parseInt(typeLength.split(",")[1]);
						typeLength=length+"";
					}else{
						length=Integer.parseInt(typeLength);
					}
					type=type.substring(0,type.indexOf("("));
				}
				models.add(buildDbModel(columnName, type, typeLength, length, scale, nullAble));
			}
		}else{
			List<Map> lists=pcServiceTemplate.queryMapBySql(" SELECT COLUMN_NAME,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE,IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='"+tableCode+"'");
			for(Map columnInfo:lists){
				String columnName=columnInfo.get("COLUMN_NAME")+"";
				String type=(columnInfo.get("DATA_TYPE")+"").toUpperCase();
				String typeLength=columnInfo.get("CHARACTER_MAXIMUM_LENGTH")+"";
				String nullAble=columnInfo.get("IS_NULLABLE")+"";
				int length=0;
				int scale=0;
				if("NO".equals(nullAble)){
					nullAble="N";
				}else{
					nullAble="Y";
				}
				if(StringUtil.isNotEmpty(typeLength)){
					length=Integer.parseInt(typeLength);
				}
				if(StringUtil.isNotEmpty(columnInfo.get("NUMERIC_PRECISION")+"")){
					length=Integer.parseInt(columnInfo.get("NUMERIC_PRECISION")+"");
				}
				if(StringUtil.isNotEmpty(columnInfo.get("NUMERIC_SCALE")+"")){
					scale=Integer.parseInt(columnInfo.get("NUMERIC_SCALE")+"");
				}
				models.add(buildDbModel(columnName, type, typeLength, length, scale, nullAble));
			}
		}
		return models;
	}
	private DbModel buildDbModel(String columnName,String type,String typeLength,int length,int scale,String nullAble){
		DbModel model=new DbModel();
		model.setCode(columnName.toUpperCase());
		if("N".equals(nullAble)){
			model.setNull(false);
		}else{
			model.setNull(true);
		}
		if(type.startsWith("VARCHAR")){
			model.setType(ColumnType.VARCHAR);
//				column.set("TABLECOLUMN_TYPE","VARCHAR");
			if("255".equals(length+"")){
				model.setType(ColumnType.VARCHAR255);
				model.setLength("");
			}else if("100".equals(length+"")){
				model.setType(ColumnType.VARCHAR100);
				model.setLength("");
			}else if("50".equals(length+"")){
				model.setType(ColumnType.VARCHAR50);
				model.setLength("");
			}else if("30".equals(length+"")){
				model.setType(ColumnType.VARCHAR30);
				model.setLength("");
			}else if("1000".equals(length+"")){
				model.setType(ColumnType.VARCHAR1000);
				model.setLength("");
			}else if("4000".equals(length+"")){
				model.setType(ColumnType.VARCHAR4000);
				model.setLength("");
			}else if("4".equals(length+"")){
				model.setType(ColumnType.YESORNO);
				model.setLength("");
			}else if(length>4){
				model.setType(ColumnType.CLOB);
				model.setLength(length+"");
			}else if(length>0){
				model.setType(ColumnType.VARCHAR);
				model.setLength(length+"");
			}
		}else if(type.startsWith("INT")){
			model.setType(ColumnType.NUMBER);
			model.setLength("");
		}else if(type.startsWith("FLOAT") || type.startsWith("NUMERIC") || type.startsWith("DECIMAL") || type.startsWith("NUMBER")){
			if(scale<=0){
				model.setType(ColumnType.NUMBER);
				if(length==20){
					model.setLength("");
				}else{
					model.setLength(typeLength);
				}
			}else{
				model.setType(ColumnType.FLOAT);
				if(length==20){
					typeLength=scale+"";
				}else{
					typeLength=length+","+scale;
				}
				model.setLength(typeLength);
			}
		}else if(type.startsWith("TEXT") || type.startsWith("LONGTEXT") || type.startsWith("CLOB")){
			model.setType(ColumnType.CLOB);
			model.setLength("");

		}else{
			model.setType(ColumnType.CUSTOM);
			model.setLength(type);
		}
		return model;
	}

	/**
	 * 查询出表中的索引
	 * @param tableCode 表code
	 * @return
	 */
	@Override
	public List<DbIndex> loadTableIndex(String tableCode) {
		// TODO Auto-generated method stub
		List<DbIndex> indexs=new ArrayList<DbIndex>();
		if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME)){
			List<Map> lists=pcServiceTemplate.queryMapBySql(" select INDEX_NAME,TABLE_NAME,COLUMN_NAME from user_ind_columns WHERE TABLE_NAME='"+tableCode+"'");
			for(Map vals:lists){
				DbIndex index=new DbIndex();
				index.setColumnName(vals.get("COLUMN_NAME")+"");
				index.setIndexName(vals.get("INDEX_NAME")+"");
				indexs.add(index);
			}
		}else if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME)){
			List<Map> lists=pcServiceTemplate.queryMapBySql(" show index from "+tableCode+"");
			for(Map vals:lists){
				DbIndex index=new DbIndex();
				index.setColumnName(vals.get("Column_name")+"");
				index.setIndexName(vals.get("Key_name")+"");
				indexs.add(index);
			}
		}else{
			StringBuffer querySql=new StringBuffer();
			querySql.append("SELECT a.NAME AS INDEX_NAME,c.NAME AS TABLE_NAME,d.NAME AS COLUMN_NAME ");
			querySql.append(" FROM   sysindexes   a  ");
			querySql.append(" JOIN   sysindexkeys   b   ON   a.id=b.id   AND   a.indid=b.indid  ");
			querySql.append(" JOIN   sysobjects   c   ON   b.id=c.id  ");
			querySql.append(" JOIN   syscolumns   d   ON   b.id=d.id   AND   b.colid=d.colid  ");
			querySql.append(" WHERE   a.indid   NOT IN(0,255)  ");
			querySql.append(" and   c.xtype='U'   ");
			querySql.append(" AND   c.name='"+tableCode+"'  ");
			List<Map> lists=pcServiceTemplate.queryMapBySql(querySql.toString());
			for(Map vals:lists){
				DbIndex index=new DbIndex();
				index.setColumnName(vals.get("COLUMN_NAME")+"");
				index.setIndexName(vals.get("INDEX_NAME")+"");
				indexs.add(index);
			}
		}
		return indexs;
	}

	/**
	 * 加载数据库所有索引名称(不支持MySQL)
	 * @return
	 */
	@Override
	public List<String> loadDbIndex() {
		// TODO Auto-generated method stub
		List<String> indexs=new ArrayList<String>();
		if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME)){
			List<Map> lists=pcServiceTemplate.queryMapBySql(" select INDEX_NAME from user_ind_columns ");
			for(Map vals:lists){
				indexs.add(vals.get("INDEX_NAME")+"");
			}
		}else if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME)){

		}else{
			StringBuffer querySql=new StringBuffer();
			querySql.append("SELECT a.NAME AS INDEX_NAME,c.NAME AS TABLE_NAME,d.NAME AS COLUMN_NAME ");
			querySql.append(" FROM   sysindexes   a  ");
			querySql.append(" JOIN   sysindexkeys   b   ON   a.id=b.id   AND   a.indid=b.indid  ");
			querySql.append(" JOIN   sysobjects   c   ON   b.id=c.id  ");
			querySql.append(" JOIN   syscolumns   d   ON   b.id=d.id   AND   b.colid=d.colid  ");
			querySql.append(" WHERE   a.indid   NOT IN(0,255)  ");
			querySql.append(" and   c.xtype='U'   ");
			List<Map> lists=pcServiceTemplate.queryMapBySql(querySql.toString());
			for(Map vals:lists){
				indexs.add(vals.get("INDEX_NAME")+"");
			}
		}
		return indexs;
	}
	/**
	 * 构建存储过程占位参数
	 * @param params
	 * @return
	 */
	@Override
	public String getCallParams(Object[] params) {
		// TODO Auto-generated method stub
		if(params==null){
			return "";
		}
		String[] callParams=new String[params.length];
		for(int i=0;i<callParams.length;i++){
			callParams[i]="?";
		}
		return StringUtil.buildSplitString(callParams, ",");
	}

	/**
	 * 构建前端使用的模型
	 * @param tableName 表明
	 * @param modelName TODO 暂不明确
	 * @param doTree TODO 暂不明确
	 * @param excludes TODO 暂不明确
	 * @return
	 */
	@Override
	public String buildModel(String tableName,String modelName,Boolean doTree,String excludes){
		JsonAssist jsonAssist=JsonAssist.getInstance();
		// 实体方式
		String modelStr="";
		if(StringUtil.isNotEmpty(modelName)) {
			modelStr= DynaCacheManager.getCacheValue(modelName);
			if(StringUtil.isEmpty(modelStr)){
				Field[] fields = EntityUtils.getInstance().getEntityInfo(modelName).getAllBaseFields();
				if(StringUtil.isNotEmpty(excludes)){
					modelStr = jsonAssist.getMoldeJsonByFields4Extjs(modelName,fields, excludes);
				}else{
					modelStr = jsonAssist.getMoldeJsonByFields4Extjs(modelName,fields, null);
				}
				DynaCacheManager.putCache(modelName, modelStr);
			}
			// DynaBean方式
		}else if(StringUtil.isNotEmpty(tableName)){
			modelStr=DynaCacheManager.getCacheValue(tableName);
			if(StringUtil.isEmpty(modelStr) || doTree){
				DynaBean resourceTable = BeanUtils.getInstance().getResourceTable(tableName);
				if(null != resourceTable && null != resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS)) {
					List<DynaBean> columns=(List<DynaBean>)resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
					List<DynaBean> resColumns=new ArrayList<DynaBean>();
					resColumns.addAll(columns);
					if(doTree){
						buildTreeField(resColumns);
					}
					modelStr = jsonAssist.getMoldeJsonByFields4Extjs(tableName, resColumns, null);
					DynaCacheManager.putCache(tableName, modelStr);
				} else {
					throw new PlatformException("根据表名获取列信息异常!", PlatformExceptionEnum.JE_CORE_DYNABEAN_MODEL_ERROR,new Object[]{tableName,modelName,doTree,excludes});
//					modelStr = ConstantVars.BLANK_STR;
				}
			}
		}else{
			throw new PlatformException("传入实体名和表名失败，无法获取字段信息!", PlatformExceptionEnum.JE_CORE_DYNABEAN_MODEL_ERROR,new Object[]{tableName,modelName,doTree,excludes});
//			logger.error("传入实体名和表名失败，无法获取字段信息！");
//			modelStr = ConstantVars.BLANK_STR;
		}
		return modelStr;
	}
	/**
	 * 构建树形字段
	 *
	 * @param columns
	 */
	private void buildTreeField(List<DynaBean> columns){
		String[] fields=new String[]{"id","text","cls","leaf","href","hrefTarget","description","code","icon","iconCls","bigIcon","bigIconCls","parent","nodeInfo","nodeInfoType","disabled","nodePath"};
		for(String field:fields){
			DynaBean column=new DynaBean("JE_CORE_TABLECOLUMN",false);
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("TABLECOLUMN_CODE", field);
			if("leaf".equals(field)){
				column.set("TABLECOLUMN_TYPE", "boolean");
			}else{
				column.set("TABLECOLUMN_TYPE", "varchar255");
			}
			columns.add(column);
		}
	}
}

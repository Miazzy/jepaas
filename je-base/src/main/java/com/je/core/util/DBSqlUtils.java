package com.je.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.je.core.constants.ConstantVars;
import com.je.core.constants.ExtFieldType;
import com.je.core.dao.PCDaoTemplateImpl;
import com.je.core.entity.extjs.Model;
import com.je.core.service.PcDBMethodManager;
import com.je.core.service.PcDBMethodManagerImpl4Oracle;
import com.je.core.service.PcDBMethodManagerImplByMySql;
import com.je.core.service.PcDBMethodManagerImplBySqlServer;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;

/**
 * 格式化兼容数据库的工具类
 * @author zhangshuaipeng
 *
 */
public class DBSqlUtils {
	public static PcDBMethodManager getPcDBMethodManager(){
		PcDBMethodManager pcDBMethodManager = null;
		if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
			pcDBMethodManager=new PcDBMethodManagerImpl4Oracle();
		}else if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_SQLSERVER)){
			pcDBMethodManager=new PcDBMethodManagerImplBySqlServer();
		}else if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_MYSQL)){
			pcDBMethodManager=new PcDBMethodManagerImplByMySql();
		}
		return pcDBMethodManager;
	}
	/**
	 * 将使用逗号隔开的字段从oracle转换成sqlserver可以执行的字段
	 * @param fields
	 * @return
	 */
	public static String formatOracleToSqlServer(String fields){
		if(StringUtils.isNotEmpty(fields)){
			fields="["+fields+"]";
			fields=fields.replace(",", "],[");
			return fields;
		}else{
			return fields;
		}
	}
	/**
	 * 得到可以执行的sql语句
	 * @param sql   sql语句
	 * @param keys  关键字数组
	 * @return
	 */
	public static String getExecuteSql(String sql,String[] keys){
		if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
			return sql;
		}else{
			for(String key:keys){
				sql=sql=sql.replace(key, "["+key+"]");
			}
			return sql;
		}
	}
	public static Model getExtModel(String columnName,Integer columnType){
		if(StringUtil.isNotEmpty(columnName)){
			String type="auto";
//			Types
			if(columnType!=null){
				switch (columnType) {
				case Types.VARCHAR:
					type=ExtFieldType.STRING;
					break;
				case Types.CHAR:
					type=ExtFieldType.STRING;
					break;
				case Types.CLOB:
					type=ExtFieldType.STRING;
					break;
				case Types.DECIMAL:
					type=ExtFieldType.FLOAT;
					break;
				case Types.INTEGER:
					type=ExtFieldType.INT;
					break;
				case Types.BLOB:
					type=ExtFieldType.STRING;
					break;
				case Types.VARBINARY:
					type=ExtFieldType.INT;
					break;
				case Types.OTHER:
					type=ExtFieldType.STRING;
					break;
				case Types.SMALLINT:
					type=ExtFieldType.INT;
					break;
				case Types.DOUBLE:
					type=ExtFieldType.FLOAT;
					break;
				case Types.FLOAT:
					type=ExtFieldType.FLOAT;
					break;
				case Types.LONGVARCHAR:
					type=ExtFieldType.STRING;
					break;
				case Types.LONGNVARCHAR:
					type=ExtFieldType.STRING;
					break;
				case Types.NUMERIC:
					type=ExtFieldType.FLOAT;
					break;
				case Types.REAL:
					type=ExtFieldType.FLOAT;
					break;
				case Types.NCHAR:
					type=ExtFieldType.STRING;
					break;
				case Types.NVARCHAR:
					type=ExtFieldType.STRING;
					break;
				case Types.DATE:
					type=ExtFieldType.DATE;
					break;
				default:
					type=ExtFieldType.AUTO;
					break;
				}
			}
			return new Model(columnName, type);
		}else{
			return null;
		}
	}

	/**
	 * 设定数据库操作条件的占位置
	 * @param sqlQuery
	 * @param type
	 * @param indexParams
	 * @param nameParams
	 */
	public static void setQueryParam(SQLQuery sqlQuery,String type, Object[] indexParams, Map<String,Object> nameParams){
		if("index".equals(type)){
			for(int i=0;i<indexParams.length;i++){
				Object paramVal=indexParams[i];
				setQueryFieldParam(sqlQuery,paramVal,type,i,"");
			}
		}else if("name".equals(type)){
			for(String key:nameParams.keySet()){
				Object paramVal=nameParams.get(key);
				setQueryFieldParam(sqlQuery,paramVal,type,null,key);
			}
		}
	}
	public static void setQueryFieldParam(SQLQuery sqlQuery,Object paramVal,String type,Integer index,String name){
//		Object paramVal=indexParams[i];
//		if(paramVal instanceof String){
//			String val=(String) paramVal;
//			sqlQuery.setString(index, val);
//		}else if(paramVal instanceof Integer){
//			Integer val=(Integer) paramVal;
//			sqlQuery.setInteger(index, val);
//		}else if(paramVal instanceof Float){
//			Float val=(Float) paramVal;
//			sqlQuery.setFloat(index, val);
//		}else if(paramVal instanceof Double){
//			Double val=(Double) paramVal;
//			sqlQuery.setDouble(index, val);
//		}else if(paramVal instanceof Date){
//			Date val=(Date) paramVal;
//			sqlQuery.setTime(index,val);
//		}else if(paramVal instanceof BigDecimal){
//			BigDecimal val=(BigDecimal) paramVal;
//			sqlQuery.setBigDecimal(index,val);
//		}else if(paramVal instanceof Boolean){
//			Boolean val=(Boolean) paramVal;
//			sqlQuery.setBoolean(index,val);
//		}else if(paramVal instanceof Long){
//			Long val=(Long) paramVal;
//			sqlQuery.setLong(index,val);
//		}else if(paramVal instanceof Short){
//			Short val=(Short) paramVal;
//			sqlQuery.setShort(index,val);
//		}else if(paramVal instanceof BigInteger){
//			BigInteger val=(BigInteger) paramVal;
//			sqlQuery.setBigInteger(index,val);
//		}else{
//			sqlQuery.setParameter(index, paramVal);
//		}
		if("index".equals(type)) {
			if (paramVal instanceof String) {
				String val = (String) paramVal;
				sqlQuery.setParameter(index, val, StandardBasicTypes.STRING);
			} else if (paramVal instanceof Integer) {
				Integer val = (Integer) paramVal;
				sqlQuery.setParameter(index, val, StandardBasicTypes.INTEGER);
			} else if (paramVal instanceof Float) {
				Float val = (Float) paramVal;
				sqlQuery.setParameter(index, val, StandardBasicTypes.FLOAT);
			} else if (paramVal instanceof Double) {
				Double val = (Double) paramVal;
				sqlQuery.setParameter(index, val, StandardBasicTypes.DOUBLE);
			} else if (paramVal instanceof Date) {
				Date val = (Date) paramVal;
				sqlQuery.setParameter(index, val, StandardBasicTypes.DATE);
			} else if (paramVal instanceof BigDecimal) {
				BigDecimal val = (BigDecimal) paramVal;
				sqlQuery.setParameter(index, val, StandardBasicTypes.BIG_DECIMAL);
			} else if (paramVal instanceof Boolean) {
				Boolean val = (Boolean) paramVal;
				sqlQuery.setParameter(index, val, StandardBasicTypes.BOOLEAN);
			} else if (paramVal instanceof Long) {
				Long val = (Long) paramVal;
				sqlQuery.setParameter(index, val, StandardBasicTypes.LONG);
			} else if (paramVal instanceof Short) {
				Short val = (Short) paramVal;
				sqlQuery.setParameter(index, val, StandardBasicTypes.SHORT);
			} else if (paramVal instanceof BigInteger) {
				BigInteger val = (BigInteger) paramVal;
				sqlQuery.setParameter(index, val, StandardBasicTypes.BIG_INTEGER);
			} else {
				sqlQuery.setParameter(index, paramVal);
			}
		}else if("name".equals(type)){
			if (paramVal instanceof String) {
				String val = (String) paramVal;
				sqlQuery.setParameter(name, val, StandardBasicTypes.STRING);
			} else if (paramVal instanceof Integer) {
				Integer val = (Integer) paramVal;
				sqlQuery.setParameter(name, val, StandardBasicTypes.INTEGER);
			} else if (paramVal instanceof Float) {
				Float val = (Float) paramVal;
				sqlQuery.setParameter(name, val, StandardBasicTypes.FLOAT);
			} else if (paramVal instanceof Double) {
				Double val = (Double) paramVal;
				sqlQuery.setParameter(name, val, StandardBasicTypes.DOUBLE);
			} else if (paramVal instanceof Date) {
				Date val = (Date) paramVal;
				sqlQuery.setParameter(name, val, StandardBasicTypes.DATE);
			} else if (paramVal instanceof BigDecimal) {
				BigDecimal val = (BigDecimal) paramVal;
				sqlQuery.setParameter(name, val, StandardBasicTypes.BIG_DECIMAL);
			} else if (paramVal instanceof Boolean) {
				Boolean val = (Boolean) paramVal;
				sqlQuery.setParameter(name, val, StandardBasicTypes.BOOLEAN);
			} else if (paramVal instanceof Long) {
				Long val = (Long) paramVal;
				sqlQuery.setParameter(name, val, StandardBasicTypes.LONG);
			} else if (paramVal instanceof Short) {
				Short val = (Short) paramVal;
				sqlQuery.setParameter(name, val, StandardBasicTypes.SHORT);
			} else if (paramVal instanceof BigInteger) {
				BigInteger val = (BigInteger) paramVal;
				sqlQuery.setParameter(name, val, StandardBasicTypes.BIG_INTEGER);
			} else {
				sqlQuery.setParameter(name, paramVal);
			}
		}
	}

	/**
	 * 如果是oracle数据库，将值转换成string
	 * @param lists
	 */
	public static void setClobList(List<Map> lists){
		if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
			for(Map map:lists){
				for(Object key:map.keySet()){
					if(key!=null){
						Object v=map.get(key);
						if(v instanceof Clob){
							map.put(key, StringUtil.getClobValue(v));
						}
					}
				}
			}
		}
	}
}

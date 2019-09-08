package com.je.core.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.constants.ConstantVars;
import com.je.core.constants.db.DbFieldType;
import com.je.core.dao.PCDaoTemplateImpl;

public class DbProduceUtil {
	public static final Object[][] sqlTypes = {{ "CURSOR", -10 },{"CODE",Types.INTEGER},{"LIMIT",Types.INTEGER},{"TOTALCOUNT",Types.INTEGER}
			,{"NOWPAGE",Types.INTEGER},{"MSG",Types.VARCHAR},{"ORDERFIELD",Types.VARCHAR},{"ORDERTYPE",Types.VARCHAR},{"COLUMNCONFIG",Types.VARCHAR},{ "BIT", Types.BIT }, { "TINYINT", Types.TINYINT },{"ORDER",Types.VARCHAR},
			{ "SMALLINT", Types.SMALLINT }, { "INTEGER", Types.INTEGER }, { "SYSVAR", Types.VARCHAR }, { "BIGINT", Types.BIGINT },
			{ "FLOAT", Types.FLOAT }, { "REAL", Types.REAL }, { "DOUBLE", Types.DOUBLE }, { "NUMERIC", Types.NUMERIC },
			{ "DECIMAL", Types.DECIMAL }, { "CHAR", Types.CHAR }, { "VARCHAR", Types.VARCHAR },
			{ "LONGVARCHAR", Types.LONGVARCHAR }, { "DATE", Types.DATE }, { "TIME", Types.TIME },
			{ "TIMESTAMP", Types.TIMESTAMP }, { "BINARY", Types.BINARY }, { "VARBINARY", Types.VARBINARY },
			{ "LONGVARBINARY", Types.LONGVARBINARY }, { "NULL", Types.NULL }, { "OTHER", Types.OTHER },
			{ "JAVA_OBJECT", Types.JAVA_OBJECT }, { "DISTINCT", Types.DISTINCT }, { "STRUCT", Types.STRUCT },
			{ "ARRAY", Types.ARRAY }, { "BLOB", Types.BLOB }, { "CLOB", Types.CLOB }, { "REF", Types.REF },
			{ "DATALINK", Types.DATALINK }, { "BOOLEAN", Types.BOOLEAN }, { "ROWID", Types.ROWID },
			{ "NCHAR", Types.NCHAR }, { "NVARCHAR", Types.NVARCHAR }, { "LONGNVARCHAR", Types.LONGNVARCHAR },
			{ "NCLOB", Types.NCLOB }, { "SQLXML", Types.SQLXML }};
	/**
	 * 为执行存储过程，查询SQL 注册参数
	 * @param proc
	 * @param fieldVos
	 * @throws Exception
	 */
	public static void registerParams(CallableStatement proc,List<DbFieldVo> fieldVos) throws Exception{
		for(int index=1;index<=fieldVos.size();index++){
			DbFieldVo fieldVo=fieldVos.get(index-1);
			String paramType=fieldVo.getParamType();
			String fieldType=fieldVo.getFieldType();
			Object value=fieldVo.getValue();
			Integer typeObj=getFieldType(fieldType);
			if("out".equals(paramType)){
				proc.registerOutParameter(index, typeObj);
			}else{
				setObject(proc, index, typeObj, value);
			}
		}
	}
	public static void setQueryParams(Query query,List<DbFieldVo> fieldVos) throws Exception{
		for(Integer i=0;i<fieldVos.size();i++){
			DbFieldVo fieldVo=fieldVos.get(i);
			setObject(query, i, getFieldType(fieldVo.getFieldType()), fieldVo.getValue());
		}
	}
	public static String buildCountSql(String sql){
		return " SELECT COUNT(*) FROM ("+sql+") COUNT_QUERY";
	}
	public static String buildQuerySql(String sql){
		return " SELECT * FROM ("+sql+") DATA_QUERY WHERE 1=1 ";
	}
	public static Integer loadOutCursorIndex(List<DbFieldVo> fieldVos){
		int index=-1;
		for(int i=1;i<=fieldVos.size();i++){
			DbFieldVo fieldVo=fieldVos.get(i-1);
			if("out".equals(fieldVo.getParamType()) && DbFieldType.CURSOR.equals(fieldVo.getFieldType())){
				index=i;
			}
		}
		return index;
	}
	/**
	 * 加载存储过程输出集合
	 * @param st
	 * @param fieldVos
	 * @param returnObj
	 * @throws Exception
	 */
	public static void loadResultSetParams(CallableStatement st,List<DbFieldVo> fieldVos,Map returnObj) throws Exception{
		//输出参数
		Object result=null;
		ResultSetMetaData rsmd=null;
		st.execute();
		ResultSet rs=null;
		if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
			Integer index=loadOutCursorIndex(fieldVos);
			if(index>0){
				rs=(ResultSet) st.getObject(index);
			}else{
				rs=st.getResultSet();
			}
		}else{
			rs=st.getResultSet();
		}
		int columnCount = 0;
		if(rs!=null){
			rsmd = rs.getMetaData();
			columnCount=rsmd.getColumnCount();
		}
		//设定输出参数
		List<Map> lists = new ArrayList<Map>();
		while (null != rs && rs.next()) {
			Map<String,Object> map=new HashMap<String,Object>();
			for (int i = 0; i < columnCount; i++) {
				String name = rsmd.getColumnName(i + 1);
				Integer type=rsmd.getColumnType(i+1);
				Object value = getObject(rs, name, type);
				map.put(name, value);
			}
			lists.add(map);
		}
		returnObj.put("rows", lists);
		for(int index=1;index<=fieldVos.size();index++){
			DbFieldVo fieldVo=fieldVos.get(index-1);
			//如果是输出参数
			if("out".equals(fieldVo.getParamType())){
				String fieldType=fieldVo.getFieldType();
				if(!"CURSOR".equalsIgnoreCase(fieldType)){
					Object value=getObject(st,index,getFieldType(fieldVo.getFieldType()));
					returnObj.put(fieldVo.getName(), value);
				}
			}
		}
		if(rs!=null) rs.close();
//		if(result instanceof ResultSet){
//			 if(rs!=null) rs.close();
//		}else{
//			returnObj.put("execute", "1");
//			returnObj.put("updateCount", result);
//		}
//		if(result instanceof ResultSet){
//			ResultSet rs=(ResultSet) result;
//			if (null != rs)
//			rs.close();
//		}
	}
	public static String buildPageSql(String sql,int start,int limit){
		if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
			return "SELECT * FROM ("+sql+") WHERE RN BETWEEN "+(start+1)+" AND "+limit+"";
		}else if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_MYSQL)){
			return sql+" limit "+start+","+limit;
		}else{
			return "SELECT TOP "+(start+limit)+" * FROM ("+sql+") PAGE_QUERY";
		}
	}
	/**
	 * 根据字段类型名称获得字段类型编号。如果名称为空返回VARCHAR，如果没有找到返回null。
	 * @param name 字段类型名称。
	 * @return 字段类型编号。
	 */
	public static Integer getFieldType(String name) {
		if (StringUtil.isEmpty(name))
			return Types.VARCHAR;
		int i, j = sqlTypes.length;

		for (i = 0; i < j; i++)
			if (name.equalsIgnoreCase((String) sqlTypes[i][0]))
				return (Integer) (sqlTypes[i][1]);
		if (StringUtil.isInteger(name))
			return Integer.parseInt(name);
		return null;
	}
	/**
	 * 根据字段类型编号获得字段类型名称。如果没有找到名称直接返回type。
	 * @param type 字段类型编号。
	 * @return 字段类型名称。
	 */
	public static String getTypeName(int type) {
		int i, j = sqlTypes.length;

		switch (type) {
			case Types.CHAR:
			case Types.NCHAR:
			case Types.VARCHAR:
			case Types.NVARCHAR:
				return null;
		}
		for (i = 0; i < j; i++)
			if (type == (Integer) sqlTypes[i][1])
				return ((String) (sqlTypes[i][0])).toLowerCase();
		return Integer.toString(type);
	}
	/**
	 * 判断指定字段类型是否是二进制类型。
	 * @param type 字段类型。
	 * @return true二进制类型，false不是二进制类型。
	 */
	public static boolean isBlobField(int type) {
		switch (type) {
			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
			case Types.BLOB:
				return true;
		}
		return false;
	}
	/**
	 * 判断指定字段类型是否是大文本类型。
	 * @param type 字段类型。
	 * @return true大文本类型，false不是大文本类型。
	 */
	public static boolean isTextField(int type) {
		switch (type) {
			case Types.LONGVARCHAR:
			case Types.LONGNVARCHAR:
			case Types.CLOB:
			case Types.NCLOB:
				return true;
		}
		return false;
	}
	/**
	 * 判断指定字段类型是否是字符串类型。
	 * @param type 字段类型。
	 * @return true字符串类型，false不是字符串类型。
	 */
	public static boolean isStringField(int type) {
		switch (type) {
			case Types.CHAR:
			case Types.NCHAR:
			case Types.VARCHAR:
			case Types.NVARCHAR:
				return true;
		}
		return false;
	}
	/**
	 * 判断指定字段类型是否可能是浮点数类型。
	 * @param type 字段类型。
	 * @return true可能是浮点数类型，false不可能是浮点数类型。
	 */
	public static boolean maybeFloatField(int type) {
		switch (type) {
			case Types.FLOAT:
			case Types.REAL:
			case Types.DOUBLE:
			case Types.NUMERIC:
			case Types.DECIMAL:
				return true;
		}
		return false;
	}
	/**
	 * 获取指定字段类型的大类别。
	 * @param type 字段类型。
	 * @return 类别。
	 */
	public static String getTypeCategory(int type) {
		switch (type) {
			case Types.BIGINT:
			case Types.INTEGER:
			case Types.SMALLINT:
			case Types.TINYINT:
			case Types.BOOLEAN:
			case Types.BIT:
				// boolean bit为兼容不同数据库返回int型
				return "int";
			case Types.DECIMAL:
			case Types.DOUBLE:
			case Types.FLOAT:
			case Types.NUMERIC:
			case Types.REAL:
				return "float";
			case Types.TIMESTAMP:
			case Types.DATE:
			case Types.TIME:
				return "date";
			default:
				return "string";
		}
	}
	/**
	 * 获得结果集当前记录指定索引号大文本字段的字符串值。大文本字段通常指类似CLOB类型的字段。
	 * @param rs 结果集对象。
	 * @param index 字段索引号。
	 * @return 字段值。
	 * @throws Exception 读取过程发生异常。
	 */
	public static String getText(ResultSet rs, int index) throws Exception {
		return (String) getObject(rs, index, -1);
	}
	/**
	 * 获得结果集当前记录指定名称大文本字段的字符串值。大文本字段通常指类似CLOB类型的字段。
	 * @param rs 结果集对象。
	 * @param fieldName 字段名称。
	 * @return 字段值。
	 * @throws Exception 读取过程发生异常。
	 */
	public static String getText(ResultSet rs, String fieldName) throws Exception {
		return (String) getObject(rs, fieldName, -1);
	}
	/**
	 * 设置PreparedStatement指定索引号参数的大文本值。大文本值将使用字符串流的方式进行设置。
	 * @param statement PreparedStatement对象。
	 * @param index 参数引号。
	 * @param value 设置的值。
	 * @throws Exception 设置参数值过程发生异常。
	 */
	public static void setText(PreparedStatement statement, int index, String value) throws Exception {
		setObject(statement, index, -1, value);
	}
	/**
	 * 获取CallableStatement指定索引号的参数值。
	 * @param statement CallableStatement对象。
	 * @param index 参数索引号。
	 * @param type 参数类型。
	 * @return 参数值。
	 * @throws Exception 获取参数值过程发生异常。
	 */
	public static Object getObject(CallableStatement statement, int index, int type) throws Exception {
		Object obj;
		switch (type) {
			case Types.CHAR:
			case Types.NCHAR:
			case Types.VARCHAR:
			case Types.NVARCHAR:
				obj = statement.getString(index);
				break;
			case Types.INTEGER:
				obj = statement.getInt(index);
				break;
			case Types.TINYINT:
				obj = statement.getByte(index);
				break;
			case Types.SMALLINT:
				obj = statement.getShort(index);
				break;
			case Types.BIGINT:
				obj = statement.getLong(index);
				break;
			case Types.REAL:
			case Types.FLOAT:
				obj = statement.getFloat(index);
				break;
			case Types.DOUBLE:
				obj = statement.getDouble(index);
				break;
			case Types.DECIMAL:
			case Types.NUMERIC:
				obj = statement.getBigDecimal(index);
				break;
			case Types.TIMESTAMP:
				obj = statement.getTimestamp(index);
				break;
			case Types.DATE:
				obj = statement.getDate(index);
				break;
			case Types.TIME:
				obj = statement.getTime(index);
				break;
			case Types.BOOLEAN:
			case Types.BIT:
				// boolean bit为兼容不同数据库返回int型
				obj = statement.getBoolean(index) ? 1 : 0;
				break;
			case Types.LONGVARCHAR:
			case Types.LONGNVARCHAR:
			case Types.CLOB:
//		case Types.NCLOB:
//			Reader rd = statement.getCharacterStream(index);
//			if (rd == null)
//				obj = null;
//			else
//				obj = SysUtil.readString(rd);
//			break;
			default:
				obj = statement.getObject(index);
		}
		if (statement.wasNull()){
			return null;
		}else{
			return obj;
		}
	}
	/**
	 * 获取结果集当前记录指定索引号的字段值。
	 * @param rs 结果集。
	 * @param index 字段索引号。
	 * @param type 字段类型。
	 * @return 字段值。
	 * @throws Exception 获取字段值过程发生异常。
	 */
	public static Object getObject(ResultSet rs, int index, int type) throws Exception {
		Object obj;
		switch (type) {
			case Types.CHAR:
			case Types.NCHAR:
			case Types.VARCHAR:
			case Types.NVARCHAR:
				obj = rs.getString(index);
				break;
			case Types.INTEGER:
				obj = rs.getInt(index);
				break;
			case Types.TINYINT:
				obj = rs.getByte(index);
				break;
			case Types.SMALLINT:
				obj = rs.getShort(index);
				break;
			case Types.BIGINT:
				obj = rs.getLong(index);
				break;
			case Types.REAL:
			case Types.FLOAT:
				obj = rs.getFloat(index);
				break;
			case Types.DOUBLE:
				obj = rs.getDouble(index);
				break;
			case Types.DECIMAL:
			case Types.NUMERIC:
				obj = rs.getBigDecimal(index);
				break;
			case Types.TIMESTAMP:
				obj = rs.getTimestamp(index);
				break;
			case Types.DATE:
				obj = rs.getDate(index);
				break;
			case Types.TIME:
				obj = rs.getTime(index);
				break;
			case Types.BOOLEAN:
			case Types.BIT:
				// boolean bit为兼容不同数据库返回int型
				obj = rs.getBoolean(index) ? 1 : 0;
				break;
			case Types.LONGVARCHAR:
			case Types.LONGNVARCHAR:
			case Types.CLOB:
//			case Types.NCLOB:
//				Reader rd = rs.getCharacterStream(index);
//				if (rd == null)
//					obj = null;
//				else
//					obj = SysUtil.readString(rd);
//				break;
			case Types.BLOB:
			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				InputStream is = rs.getBinaryStream(index);
				if (is != null)
					is.close();// 读取之后再关闭wasNull方法才可判断是否为空
				obj = "(blob)";
				// 如果需要读取数据可直接读取流
				break;
			default:
				obj = rs.getObject(index);
		}
		if (rs.wasNull())
			return null;
		else
			return obj;
	}

	/**
	 * 获取结果集当前记录指定名称的字段值。
	 * @param rs 结果集。
	 * @param fieldName 字段名称。
	 * @param type 字段类型。
	 * @return 字段值。
	 * @throws Exception 获取字段值过程发生异常。
	 */
	public static Object getObject(ResultSet rs, String fieldName, int type) throws Exception {
		Object obj;
		switch (type) {
			case Types.CHAR:
			case Types.NCHAR:
			case Types.VARCHAR:
			case Types.NVARCHAR:
				obj = rs.getString(fieldName);
				break;
			case Types.INTEGER:
				obj = rs.getInt(fieldName);
				break;
			case Types.TINYINT:
				obj = rs.getByte(fieldName);
				break;
			case Types.SMALLINT:
				obj = rs.getShort(fieldName);
				break;
			case Types.BIGINT:
				obj = rs.getLong(fieldName);
				break;
			case Types.REAL:
			case Types.FLOAT:
				obj = rs.getFloat(fieldName);
				break;
			case Types.DOUBLE:
				obj = rs.getDouble(fieldName);
				break;
			case Types.DECIMAL:
			case Types.NUMERIC:
				obj = rs.getBigDecimal(fieldName);
				break;
			case Types.TIMESTAMP:
				obj = rs.getTimestamp(fieldName);
				break;
			case Types.DATE:
				obj = rs.getDate(fieldName);
				break;
			case Types.TIME:
				obj = rs.getTime(fieldName);
				break;
			case Types.BOOLEAN:
			case Types.BIT:
				// boolean bit为兼容不同数据库返回int型
				obj = rs.getBoolean(fieldName) ? 1 : 0;
				break;
			case Types.LONGVARCHAR:
			case Types.LONGNVARCHAR:
			case Types.CLOB:
				if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
					obj=StringUtil.getClobValue(fieldName);
				}
			case Types.NCLOB:
				Reader rd = rs.getCharacterStream(fieldName);
				if (rd == null)
					obj = null;
				else
					obj = StringUtil.readString(rd);
				break;
			case Types.BLOB:
			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				InputStream is = rs.getBinaryStream(fieldName);
				if (is != null)
					is.close();// 读取之后再关闭wasNull方法才可判断是否为空
				obj = "(blob)";
				// 如果需要读取数据可直接读取流
				break;
			default:
				obj = rs.getObject(fieldName);
		}
		if (rs.wasNull())
			return null;
		else
			return obj;
	}
	/**
	 * 设置PreparedStatement对象指定索引号的参数值。
	 * @param statement PreparedStatement对象。
	 * @param index 参数索引。
	 * @param type 参数类型。
	 * @param object 参数值。
	 * @throws Exception 设置参数值过程发生异常。
	 */
	public static void setObject(Query statement, int index, int type, Object object) throws Exception{
		if (object == null || object instanceof String) {
			String value;
			if (object == null)
				value = null;
			else
				value = (String) object;
			if (StringUtil.isEmpty(value))
				statement.setParameter(index, null);
			else {
				switch (type) {
					case Types.CHAR:
					case Types.NCHAR:
					case Types.VARCHAR:
					case Types.NVARCHAR:
						if (StringUtil.isEmpty(value))
							statement.setString(index, "");
						else
							statement.setString(index, value);
						break;
					case Types.INTEGER:
						statement.setInteger(index, Integer.parseInt(StringUtil.convertBool(value)));
						break;
					case Types.TINYINT:
						statement.setByte(index, Byte.parseByte(StringUtil.convertBool(value)));
						break;
					case Types.SMALLINT:
						statement.setShort(index, Short.parseShort(StringUtil.convertBool(value)));
						break;
					case Types.BIGINT:
						statement.setLong(index, Long.parseLong(StringUtil.convertBool(value)));
						break;
					case Types.REAL:
					case Types.FLOAT:
						statement.setFloat(index, Float.parseFloat(StringUtil.convertBool(value)));
						break;
					case Types.DOUBLE:
						statement.setDouble(index, Double.parseDouble(StringUtil.convertBool(value)));
						break;
					case Types.DECIMAL:
					case Types.NUMERIC:
						statement.setBigDecimal(index, new BigDecimal(StringUtil.convertBool(value)));
						break;
					case Types.TIMESTAMP:
						statement.setTimestamp(index, Timestamp.valueOf(DateUtils.fixTimestamp(value, false)));
						break;
					case Types.DATE:
						// 如果值有空格使用timestamp类型处理
						if (value.indexOf(' ') != -1)
							statement.setTimestamp(index, Timestamp.valueOf(DateUtils.fixTimestamp(value, false)));
						else
							statement.setDate(index, java.sql.Date.valueOf(DateUtils.fixTimestamp(value, true)));
						break;
					case Types.TIME:
						// 如果值有空格使用timestamp类型处理
						if (value.indexOf(' ') != -1)
							statement.setTimestamp(index, Timestamp.valueOf(DateUtils.fixTimestamp(value, false)));
						else
							statement.setTime(index, Time.valueOf(DateUtils.fixTime(value)));
						break;
					case Types.BOOLEAN:
					case Types.BIT:
						statement.setBoolean(index, StringUtil.getBool(value));
						break;
					case Types.LONGVARCHAR:
					case Types.LONGNVARCHAR:
					case Types.CLOB:
					case Types.NCLOB:
						statement.setText(index, value);
//						statement.setCharacterStream(,);
						break;
					case Types.BLOB:
					case Types.BINARY:
					case Types.VARBINARY:
					case Types.LONGVARBINARY:
						// 字符串存储到二进制字段视为BASE64编码
//						InputStream is = new ByteArrayInputStream();
						statement.setBinary(index, StringUtil.decodeBase64(value));
						break;
					default:
						statement.setParameter(index, value);
				}
			}
		} else {
			if (object instanceof InputStream){
				statement.setParameter(index, object);
//					statement.setBinaryStream(index, (InputStream) object, ((InputStream) object).available());
			}else if (object instanceof java.util.Date){
				statement.setTimestamp(index, new Timestamp(((java.util.Date) object).getTime()));
			}else
				statement.setParameter(index, object);
		}
	}
	/**
	 * 设置PreparedStatement对象指定索引号的参数值。
	 * @param statement PreparedStatement对象。
	 * @param index 参数索引。
	 * @param type 参数类型。
	 * @param object 参数值。
	 * @throws Exception 设置参数值过程发生异常。
	 */
	public static void setObject(PreparedStatement statement, int index, int type, Object object) throws Exception{
		if (object == null || object instanceof String) {
			String value;

			if (object == null)
				value = null;
			else
				value = (String) object;
			if (StringUtil.isEmpty(value))
				statement.setNull(index, type);
			else {
				switch (type) {
					case Types.CHAR:
					case Types.NCHAR:
					case Types.VARCHAR:
					case Types.NVARCHAR:
						if (StringUtil.isEmpty(value))
							statement.setString(index, "");
						else
							statement.setString(index, value);
						break;
					case Types.INTEGER:
						statement.setInt(index, Integer.parseInt(StringUtil.convertBool(value)));
						break;
					case Types.TINYINT:
						statement.setByte(index, Byte.parseByte(StringUtil.convertBool(value)));
						break;
					case Types.SMALLINT:
						statement.setShort(index, Short.parseShort(StringUtil.convertBool(value)));
						break;
					case Types.BIGINT:
						statement.setLong(index, Long.parseLong(StringUtil.convertBool(value)));
						break;
					case Types.REAL:
					case Types.FLOAT:
						statement.setFloat(index, Float.parseFloat(StringUtil.convertBool(value)));
						break;
					case Types.DOUBLE:
						statement.setDouble(index, Double.parseDouble(StringUtil.convertBool(value)));
						break;
					case Types.DECIMAL:
					case Types.NUMERIC:
						statement.setBigDecimal(index, new BigDecimal(StringUtil.convertBool(value)));
						break;
					case Types.TIMESTAMP:
						statement.setTimestamp(index, Timestamp.valueOf(DateUtils.fixTimestamp(value, false)));
						break;
					case Types.DATE:
						// 如果值有空格使用timestamp类型处理
						if (value.indexOf(' ') != -1)
							statement.setTimestamp(index, Timestamp.valueOf(DateUtils.fixTimestamp(value, false)));
						else
							statement.setDate(index, java.sql.Date.valueOf(DateUtils.fixTimestamp(value, true)));
						break;
					case Types.TIME:
						// 如果值有空格使用timestamp类型处理
						if (value.indexOf(' ') != -1)
							statement.setTimestamp(index, Timestamp.valueOf(DateUtils.fixTimestamp(value, false)));
						else
							statement.setTime(index, Time.valueOf(DateUtils.fixTime(value)));
						break;
					case Types.BOOLEAN:
					case Types.BIT:
						statement.setBoolean(index, StringUtil.getBool(value));
						break;
					case Types.LONGVARCHAR:
					case Types.LONGNVARCHAR:
					case Types.CLOB:
					case Types.NCLOB:
						statement.setCharacterStream(index, new StringReader(value), value.length());
						break;
					case Types.BLOB:
					case Types.BINARY:
					case Types.VARBINARY:
					case Types.LONGVARBINARY:
						// 字符串存储到二进制字段视为BASE64编码
						InputStream is = new ByteArrayInputStream(StringUtil.decodeBase64(value));
						statement.setBinaryStream(index, is, is.available());
						break;
					default:
						statement.setObject(index, value, type);
				}
			}
		} else {
			if (object instanceof InputStream)
				statement.setBinaryStream(index, (InputStream) object, ((InputStream) object).available());
			else if (object instanceof java.util.Date)
				statement.setTimestamp(index, new Timestamp(((java.util.Date) object).getTime()));
			else
				statement.setObject(index, object, type);
		}
	}
}

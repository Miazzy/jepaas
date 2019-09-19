package com.je.core.sql;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.constants.ConstantVars;
import com.je.core.constants.table.ColumnType;
import com.je.core.dao.PCDaoTemplateImpl;
import com.je.core.entity.EntityInfo;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.util.*;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 用于创建可以直接执行的query
 * @author YUNFENGCHENG
 * 2011-8-31 上午09:33:00
 */
public class BuildingQuery {
	private static EntityUtils entityUtils;
	private static ReflectionUtils reflectionUtils;
	private static BeanUtils beanUtils;
	private static Logger logger = LoggerFactory.getLogger(BuildingQuery.class);
	private BuildingQuery(){
		beanUtils = BeanUtils.getInstance();
		entityUtils = EntityUtils.getInstance();
		reflectionUtils = ReflectionUtils.getInstance();
	}
	/**
	 * 实例化此类
	 * 研发部:云凤程
	 * 2011-8-31 上午09:37:45
	 * @return
	 */
	public static BuildingQuery getInstance(){
		return BuildingQueryHolder.BUILDING_QUERY;
	}
	/**
	 * 静态内部类用于初始化对象和缓存树形
	 * 研发部:云凤程
	 * 2011-8-31 上午09:38:00
	 */
	private static class BuildingQueryHolder{
		private static final BuildingQuery BUILDING_QUERY = new BuildingQuery();
	}
	/**
	 * 得到插入语句的SQLQuery
	 * 研发部:云凤程
	 * @param session     数据信息
	 * @param dynaBean 要保存的BEAN
	 * @return 插入语句SQL
	 * 张帅鹏修改：将插入语句的的替换单引号操作取消
	 */
	public SQLQuery getInsertSQLQuery(Session session,DynaBean dynaBean) {
		//得到表的主键编码
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String tablePKCode = beanUtils.getPKeyFieldNames(dynaBean);
		//主键系统通用UUID
		//主键系统通用UUID
		if(!StringUtil.isNotEmpty(dynaBean.getStr(tablePKCode))){
//			String uuid = UUID.randomUUID()+"";
			String uuid=JEUUID.uuid();
			dynaBean.setStr(tablePKCode, uuid);
		}
		String sql = BuildingSql.getInstance().getInsertSql(dynaBean);
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		//得到表结构结构的描述
		DynaBean table = beanUtils.getResourceTable(dynaBean.getStr(BeanUtils.KEY_TABLE_CODE));
		List<DynaBean> columns = (List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
		for(DynaBean column : columns){
			String fieldCode = column.getStr("TABLECOLUMN_CODE");
			String fieldType = column.getStr("TABLECOLUMN_TYPE");
			String fieldLength = column.getStr("TABLECOLUMN_LENGTH");
			String fieldValue = dynaBean.getStr(fieldCode, "");
			if(ColumnType.ID.equals(column.getStr("TABLECOLUMN_TYPE"))){
				//id
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.CLOB.equals(fieldType)){
				//大数据
//				fieldValue = SqlUtil.escape((String)fieldValue);
				if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
					if(StringUtil.isNotEmpty(fieldValue)) {
						sqlQuery.setText(fieldCode, fieldValue);
					}else {
						sqlQuery.setText(fieldCode, null);
					}
				}else{
					sqlQuery.setText(fieldCode, (String)fieldValue);
				}
			}else if(ColumnType.BIGCLOB.equals(fieldType)){
				//大数据
//				fieldValue = SqlUtil.escape((String)fieldValue);
				if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
					if(StringUtil.isNotEmpty(fieldValue)) {
						sqlQuery.setText(fieldCode, fieldValue);
					}else {
						sqlQuery.setText(fieldCode, null);
					}
				}else{
					sqlQuery.setText(fieldCode, (String)fieldValue);
				}
			}else if(ColumnType.DATE.equals(fieldType)){
				//时间
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.DATETIME.equals(fieldType)){
				//时间
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.FLOAT.equals(fieldType)){
				//小数
				//研发部:云凤程12年12月7日改动
				Double value=StringUtil.isNotEmpty((String)fieldValue)?new Double(fieldValue.toString()):new Double(0.0);
				if(StringUtil.isNotEmpty(fieldLength)){
					Integer length=Integer.parseInt(fieldLength);
					DecimalFormat myFormat = new DecimalFormat("0."+StringUtil.getSameString("0", length));
					String strFloat = myFormat.format(value);
					value=Double.parseDouble(strFloat);
				}
				//修改精度
				sqlQuery.setDouble(fieldCode,value);
			}else if(ColumnType.FLOAT2.equals(fieldType)){
				//小数
				//研发部:云凤程12年12月7日改动
				Double value=StringUtil.isNotEmpty((String)fieldValue)?new Double(fieldValue.toString()):new Double(0.0);
				Integer length=2;
				DecimalFormat myFormat = new DecimalFormat("0."+StringUtil.getSameString("0", length));
				String strFloat = myFormat.format(value);
				value=Double.parseDouble(strFloat);
				//修改精度
				sqlQuery.setDouble(fieldCode,value);
			}else if(ColumnType.CUSTOM.equals(fieldType)){
				//自定义,当选择自定义的时候长度 就写成类似(VARCHAE2(154))
				if(fieldLength.indexOf("VARCHAE") != -1){
					sqlQuery.setString(fieldCode, (String)fieldValue);
				}else if(fieldLength.indexOf("FLOAT") != -1){
					sqlQuery.setFloat(fieldCode, (null!=fieldValue?Float.parseFloat(fieldValue.toString()):0));
				}else if(fieldLength.indexOf("NUMBER") != -1){
					sqlQuery.setInteger(fieldCode, ((null!=fieldValue && !fieldValue.equals(""))?Integer.parseInt(fieldValue.toString()):0));
				}else{
					sqlQuery.setString(fieldCode, (String)fieldValue);
				}
			}else if(ColumnType.NUMBER.equals(fieldType)){
				//整数
				sqlQuery.setInteger(fieldCode, (((!"".equals(fieldValue))&&(null!=fieldValue))?Integer.parseInt(fieldValue.toString()):0));
			}else if(ColumnType.VARCHAR1000.equals(fieldType)){
				//大字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR4000.equals(fieldType)){
				//大字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR255.equals(fieldType)){
				//一般字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR100.equals(fieldType)){
				//一般字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR50.equals(fieldType)){
				//一般字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR30.equals(fieldType)){
				//一般字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR.equals(fieldType)){
				//自定义长度字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.YESORNO.equals(fieldType)){
				//YESORNO
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.FOREIGNKEY.equals(fieldType)){
				if(StringUtil.isNotEmpty(fieldValue+"")){
					sqlQuery.setString(fieldCode, (String)fieldValue);
				}else{
					sqlQuery.setString(fieldCode,null);
				}
			}else{
				throw new PlatformException("创建SQlQuery过程的时候,生成列出错", PlatformExceptionEnum.JE_CORE_DYNABEAN_BUILDING_ERROR,new Object[]{dynaBean});
//				logger.error("创建SQlQuery过程的时候,生成列出错");
			}
		}
		dynaBean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
		return sqlQuery;
	}
	/**
	 * 研发部:云凤程
	 * 得到用于根据id进行删除操作的SQLQuery
	 * @param session
	 * @param dynaBean 动态实体bean
	 * @return
	 */
	public SQLQuery getDeleteByIdSQLQuery(Session session,DynaBean dynaBean){
		String sql = BuildingSql.getInstance().getDeleteByIdSql(dynaBean);
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		String tablePKValue = dynaBean.getStr(dynaBean.getStr(BeanUtils.KEY_PK_CODE));
		DBSqlUtils.setQueryParam(sqlQuery,"index",new Object[]{tablePKValue},null);
		return sqlQuery;
	}
	/**
	 * 研发部:云凤程
	 * 得到根据whereSql进行删除操作SQLQuery
	 * @param session
	 * @param dynaBean
	 * @return
	 */
	public SQLQuery getDeleteByWhereSQLQuery(Session session,DynaBean dynaBean,String type,Object[] indexParams,Map<String,Object> nameParams){
		String sql = BuildingSql.getInstance().getDeleteByWhereSql(dynaBean);
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		DBSqlUtils.setQueryParam(sqlQuery,type,indexParams,nameParams);
		return sqlQuery;
	}
	/**
	 * 研发部:云凤程
	 * 得到根据ids进行删除操作SQLQuery
	 * @param session
	 * @param ids
	 * @return
	 */
	public SQLQuery getDeleteByIdsSQLQuery(Session session,String ids ,String tableName ,String idName){
		String sql = BuildingSql.getInstance().getDeleteByIdsSql(ids,tableName,idName);
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		String[] idArray=ids.split(",");
		Integer paramCount=0;
		for(String id:idArray){
			if(StringUtil.isNotEmpty(id)){
				paramCount++;
			}
		}
		if(paramCount>0) {
			int index=0;
			Object[] paramVals = new Object[paramCount];
			for(int i=0;i<idArray.length;i++){
				if(StringUtil.isNotEmpty(idArray[i])){
					paramVals[index]=idArray[i];
					index++;
				}
			}
			DBSqlUtils.setQueryParam(sqlQuery,"index",paramVals,null);
		}
		return sqlQuery;
	}
	/**
	 * 研发部:云凤程
	 * 得到根据ID查询一条数据的SQL
	 * @param session
	 * @param dynaBean
	 * @return
	 */
	public SQLQuery getSelectByIdSQLQuery(Session session,DynaBean dynaBean){
		String sql = BuildingSql.getInstance().getSelecOnetByIdSql(dynaBean);
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		//3.得到表主键的值
		String tablePKValue = dynaBean.getStr(dynaBean.getStr(BeanUtils.KEY_PK_CODE));
		DBSqlUtils.setQueryParam(sqlQuery,"index",new Object[]{tablePKValue},null);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery;
	}
	/**
	 * 研发部:云凤程
	 * 得到根据WHERE查询一条数据的SQL
	 * @param session
	 * @param dynaBean
	 * @return
	 */
	public SQLQuery getSelectByWhereSQLQuery(Session session,DynaBean dynaBean,String type,Object[] indexParams,Map<String,Object> nameParams){
		String sql = BuildingSql.getInstance().getSelectOneByWhereSql(dynaBean);
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		DBSqlUtils.setQueryParam(sqlQuery,type,indexParams,nameParams);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery;
	}
	/**
	 * 研发部:云凤程
	 * 得到根据WHERE查询数据的总条数SQL
	 * @param session
	 * @param dynaBean
	 * @return
	 */
	public SQLQuery getSelectCountByWhereSQLQuery(Session session,DynaBean dynaBean,String type,Object[] indexParams,Map<String,Object> nameParams){
		String sql = BuildingSql.getInstance().getSelectCountByWhereSql(dynaBean);
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		DBSqlUtils.setQueryParam(sqlQuery,type,indexParams,nameParams);
		return sqlQuery;
	}
	/**
	 * 研发部:云凤程
	 * 基于主键得到更新Bean的SQLQuery
	 * @param session
	 * @param dynaBean
	 * @return
	 */
	public SQLQuery getUpdateOneSQLQuery(Session session,DynaBean dynaBean){
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
//		String tablePKCode = beanUtils.getPKeyFieldNames(dynaBean);
		String sql = BuildingSql.getInstance().getUpdateByIdOrWhereSql(dynaBean,false);
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		//得到表结构结构的描述
		DynaBean table = beanUtils.getResourceTable(tableCode);
		List<DynaBean> columns = (List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
		Map<String,Object> values=dynaBean.getValues();
		for(DynaBean column : columns){
			String fieldCode = column.getStr("TABLECOLUMN_CODE");
			if(values.get(fieldCode)==null)continue;
			String fieldType = column.getStr("TABLECOLUMN_TYPE");
			String fieldLength = column.getStr("TABLECOLUMN_LENGTH");
			String fieldValue = dynaBean.getStr(fieldCode, "");
			if(ColumnType.ID.equals(column.getStr("TABLECOLUMN_TYPE"))){
				//id
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.CLOB.equals(fieldType)){
				//大数据
//				fieldValue = SqlUtil.escape((String)fieldValue);
				if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
					if(StringUtil.isNotEmpty(fieldValue)) {
						sqlQuery.setText(fieldCode, fieldValue);
					}else {
						sqlQuery.setText(fieldCode, null);
					}
				}else{
					sqlQuery.setText(fieldCode, (String)fieldValue);
				}
			}else if(ColumnType.BIGCLOB.equals(fieldType)){
				//大数据
//				fieldValue = SqlUtil.escape((String)fieldValue);
				if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
					if(StringUtil.isNotEmpty(fieldValue)) {
						sqlQuery.setText(fieldCode, fieldValue);
					}else {
						sqlQuery.setText(fieldCode, null);
					}
				}else{
					sqlQuery.setText(fieldCode, (String)fieldValue);
				}
			}else if(ColumnType.DATE.equals(fieldType)){
				//时间
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.DATETIME.equals(fieldType)){
				//时间
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.FLOAT.equals(fieldType)){
				//小数
				//研发部:云凤程12年12月7日改动
				Double value=StringUtil.isNotEmpty((String)fieldValue)?new Double(fieldValue.toString()):new Double(0.0);
				if(StringUtil.isNotEmpty(fieldLength)){
					Integer length=Integer.parseInt(fieldLength);
					DecimalFormat myFormat = new DecimalFormat("0."+StringUtil.getSameString("0", length));
					String strFloat = myFormat.format(value);
					value=Double.parseDouble(strFloat);
				}
				//修改精度
				sqlQuery.setDouble(fieldCode,value);
			}else if(ColumnType.FLOAT2.equals(fieldType)){
				//小数
				//研发部:云凤程12年12月7日改动
				Double value=StringUtil.isNotEmpty((String)fieldValue)?new Double(fieldValue.toString()):new Double(0.0);
				Integer length=2;
				DecimalFormat myFormat = new DecimalFormat("0."+StringUtil.getSameString("0", length));
				String strFloat = myFormat.format(value);
				value=Double.parseDouble(strFloat);
				//修改精度
				sqlQuery.setDouble(fieldCode,value);
			}else if(ColumnType.CUSTOM.equals(fieldType)){
				//自定义,当选择自定义的时候长度 就写成类似(VARCHAE2(154))
				if(fieldLength.indexOf("VARCHAE") != -1){
					sqlQuery.setString(fieldCode, (String)fieldValue);
				}else if(fieldLength.indexOf("FLOAT") != -1){
					sqlQuery.setFloat(fieldCode, (null!=fieldValue?Float.parseFloat(fieldValue.toString()):0));
				}else if(fieldLength.indexOf("NUMBER") != -1){
					sqlQuery.setInteger(fieldCode, ((null!=fieldValue && !fieldValue.equals(""))?Integer.parseInt(fieldValue.toString()):0));
				}else{
					sqlQuery.setString(fieldCode, (String)fieldValue);
				}
			}else if(ColumnType.NUMBER.equals(fieldType)){
				//整数
				sqlQuery.setInteger(fieldCode, (((!"".equals(fieldValue))&&(null!=fieldValue))?Integer.parseInt(fieldValue.toString()):0));
			}else if(ColumnType.VARCHAR1000.equals(fieldType)){
				//大字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR4000.equals(fieldType)){
				//大字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR255.equals(fieldType)){
				//一般字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR100.equals(fieldType)){
				//一般字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR50.equals(fieldType)){
				//一般字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR30.equals(fieldType)){
				//一般字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR.equals(fieldType)){
				//自定义长度字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.YESORNO.equals(fieldType)){
				//YESORNO
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.FOREIGNKEY.equals(fieldType)){
				if(StringUtil.isNotEmpty(fieldValue+"")){
					sqlQuery.setString(fieldCode, (String)fieldValue);
				}else{
					sqlQuery.setString(fieldCode,null);
				}
			}else{
				throw new PlatformException("创建SQlQuery过程的时候,生成列出错", PlatformExceptionEnum.JE_CORE_DYNABEAN_BUILDING_ERROR,new Object[]{dynaBean});
//				logger.error("创建SQlQuery过程的时候,生成列出错");
			}
		}
		dynaBean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
		return sqlQuery;
	}
	/**
	 * 研发部:云凤程
	 * 基于WHERE得到更新Bean的SQLQuery
	 * @param session
	 * @param dynaBean
	 * @return
	 */
	public SQLQuery getListUpdateSQLQuery(Session session,DynaBean dynaBean,String type,Object[] indexParams,Map<String,Object> nameParams){
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
//		String tablePKCode = beanUtils.getPKeyFieldNames(dynaBean);
		String sql = BuildingSql.getInstance().getUpdateByIdOrWhereSql(dynaBean,true);
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		DBSqlUtils.setQueryParam(sqlQuery,type,indexParams,nameParams);
		//得到表结构结构的描述
		DynaBean table = beanUtils.getResourceTable(tableCode);
		List<DynaBean> columns = (List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
		Map<String,Object> values=dynaBean.getValues();
		for(DynaBean column : columns){
			String fieldCode = column.getStr("TABLECOLUMN_CODE");
			if(values.get(fieldCode)==null)continue;
			String fieldType = column.getStr("TABLECOLUMN_TYPE");
			String fieldLength = column.getStr("TABLECOLUMN_LENGTH");
			String fieldValue = dynaBean.getStr(fieldCode, "");
			if(ColumnType.ID.equals(column.getStr("TABLECOLUMN_TYPE"))){
				//id
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.CLOB.equals(fieldType)){
				//大数据
//						fieldValue = SqlUtil.escape((String)fieldValue);
				if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
					if(StringUtil.isNotEmpty(fieldValue)) {
						sqlQuery.setText(fieldCode, fieldValue);
					}else {
						sqlQuery.setText(fieldCode, null);
					}
				}else{
					sqlQuery.setText(fieldCode, (String)fieldValue);
				}
			}else if(ColumnType.BIGCLOB.equals(fieldType)){
				//大数据
//				fieldValue = SqlUtil.escape((String)fieldValue);
				if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
					if(StringUtil.isNotEmpty(fieldValue)) {
						sqlQuery.setText(fieldCode, fieldValue);
					}else {
						sqlQuery.setText(fieldCode, null);
					}
				}else{
					sqlQuery.setText(fieldCode, (String)fieldValue);
				}
			}else if(ColumnType.DATE.equals(fieldType)){
				//时间
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.DATETIME.equals(fieldType)){
				//时间
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.FLOAT.equals(fieldType)){
				//小数
				//研发部:云凤程12年12月7日改动
				Double value=StringUtil.isNotEmpty((String)fieldValue)?new Double(fieldValue.toString()):new Double(0.0);
				if(StringUtil.isNotEmpty(fieldLength)){
					Integer length=Integer.parseInt(fieldLength);
					DecimalFormat myFormat = new DecimalFormat("0."+StringUtil.getSameString("0", length));
					String strFloat = myFormat.format(value);
					value=Double.parseDouble(strFloat);
				}
				//修改精度
				sqlQuery.setDouble(fieldCode,value);
			}else if(ColumnType.FLOAT2.equals(fieldType)){
				//小数
				//研发部:云凤程12年12月7日改动
				Double value=StringUtil.isNotEmpty((String)fieldValue)?new Double(fieldValue.toString()):new Double(0.0);
				Integer length=2;
				DecimalFormat myFormat = new DecimalFormat("0."+StringUtil.getSameString("0", length));
				String strFloat = myFormat.format(value);
				value=Double.parseDouble(strFloat);
				//修改精度
				sqlQuery.setDouble(fieldCode,value);
			}else if(ColumnType.CUSTOM.equals(fieldType)){
				//自定义,当选择自定义的时候长度 就写成类似(VARCHAE2(154))
				if(fieldLength.indexOf("VARCHAE") != -1){
					sqlQuery.setString(fieldCode, (String)fieldValue);
				}else if(fieldLength.indexOf("FLOAT") != -1){
					sqlQuery.setFloat(fieldCode, (null!=fieldValue?Float.parseFloat(fieldValue.toString()):0));
				}else if(fieldLength.indexOf("NUMBER") != -1){
					sqlQuery.setInteger(fieldCode, ((null!=fieldValue && !fieldValue.equals(""))?Integer.parseInt(fieldValue.toString()):0));
				}else{
					sqlQuery.setString(fieldCode, (String)fieldValue);
				}
			}else if(ColumnType.NUMBER.equals(fieldType)){
				//整数
				sqlQuery.setInteger(fieldCode, (((!"".equals(fieldValue))&&(null!=fieldValue))?Integer.parseInt(fieldValue.toString()):0));
			}else if(ColumnType.VARCHAR1000.equals(fieldType)){
				//大字符串
//						fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR4000.equals(fieldType)){
				//大字符串
//						fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR255.equals(fieldType)){
				//一般字符串
//						fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR100.equals(fieldType)){
				//一般字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR50.equals(fieldType)){
				//一般字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR30.equals(fieldType)){
				//一般字符串
//				fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.VARCHAR.equals(fieldType)){
				//自定义长度字符串
//						fieldValue = SqlUtil.escape((String)fieldValue);
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.YESORNO.equals(fieldType)){
				//YESORNO
				sqlQuery.setString(fieldCode, (String)fieldValue);
			}else if(ColumnType.FOREIGNKEY.equals(fieldType)){
				if(StringUtil.isNotEmpty(fieldValue+"")){
					sqlQuery.setString(fieldCode, (String)fieldValue);
				}else{
					sqlQuery.setString(fieldCode,null);
				}
			}else{
				throw new PlatformException("创建SQlQuery过程的时候,生成列出错", PlatformExceptionEnum.JE_CORE_DYNABEAN_BUILDING_ERROR,new Object[]{dynaBean});
//				logger.error("创建SQlQuery过程的时候,生成列出错");
			}
		}
		dynaBean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
		return sqlQuery;
	}
	/**
	 * 通过指定Entity获取相应的HQL Query对象
	 * 云凤程
	 * 2011-12-30 上午11:00:53
	 * @param entity
	 * @param sessionFactory
	 * @return
	 */
	@Deprecated
	public Query getListUpdateQuery(Object entity,SessionFactory sessionFactory) {
		//1.得到类的信息类
		EntityInfo entityInfo = entityUtils.getEntityInfo(entity.getClass());
		//2.得到entityName
		String entityName = entityInfo.getEntityName();
		//3.拼接SQL语句
		StringBuilder SQL = new StringBuilder("update "+entityName+" set ");
		Field[] canListUpdateFields = entityInfo.getCanListUpdateFields();
		if(canListUpdateFields != null){
			for(Field field : canListUpdateFields){
				SQL.append(field.getName()+" = ? ,");
			}
			SQL = new StringBuilder(SQL.substring(0, SQL.length()-1));
			String idName = entityInfo.getIdName();
			Object idValue = reflectionUtils.invokeMethod(entity, entityUtils.getReadMethod(idName), null);
			if("Long".equals(idValue.getClass().getSimpleName())){
				SQL.append(" WHERE "+ idName +"= "+idValue);
			}else{
				SQL.append(" WHERE "+ idName +"= '"+idValue+"'");
			}
			//4.构建用于类表更新的Query
			Query query = sessionFactory.getCurrentSession().createQuery(SQL.toString());
			Object value = null;
			int i = 0;
			for(Field field : canListUpdateFields){
				value = reflectionUtils.invokeMethod(entity, entityUtils.getReadMethod(field.getName()), null);
				if(value != null){
					query.setParameter(i, value);
					i++;
				}
			}
			return query;
		}else{
			throw new PlatformException("类["+entity.getClass().getSimpleName()+"]中没有可以进行类表编辑的字段...", PlatformExceptionEnum.JE_CORE_DYNABEAN_BUILDING_ERROR,new Object[]{entity});
		}
	}
	/**
	 * 在当前session中创建新的HQL Query
	 * chenmeng
	 * 2011-12-30 上午11:20:25
	 * @param hql
	 * @param sessionFactory
	 * @return
	 */
	public Query getHqlQuery(String hql, SessionFactory sessionFactory) {
		return sessionFactory.getCurrentSession().createQuery(hql);
	}
	/**
	 * 在当前session中创建新的SQL Query
	 * chenmeng
	 * 2011-12-30 上午11:05:45
	 * @param sql
	 * @param sessionFactory
	 * @return
	 */
	public SQLQuery getSqlQuery(String sql,String type,Object[] indexParams,Map<String,Object> nameParams,SessionFactory sessionFactory) {
		SQLQuery sqlQuery=sessionFactory.getCurrentSession().createSQLQuery(sql);
		DBSqlUtils.setQueryParam(sqlQuery,type,indexParams,nameParams);
		return sqlQuery;
	}
	public Query getMapSqlQuery(String sql,String type,Object[] indexParams,Map<String,Object> nameParams,SessionFactory sessionFactory) {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString());
		DBSqlUtils.setQueryParam(query,type,indexParams,nameParams);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query;
	}
	public Query getProcedureQuery(String callSql,String type,Object[] indexParams,Map<String,Object> nameParams,SessionFactory sessionFactory){
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(callSql);
		DBSqlUtils.setQueryParam(query,type,indexParams,nameParams);
		return query;
	}
	public Query getMapProcedureQuery(String callSql,String type,Object[] indexParams,Map<String,Object> nameParams,SessionFactory sessionFactory){
		Query query = getProcedureQuery(callSql,type,indexParams,nameParams, sessionFactory);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query;
	}
	public Query getMapSqlQuery(String sql,List<DbFieldVo> fieldVos,SessionFactory sessionFactory) {
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
	public Query countSqlQuery(String sql,List<DbFieldVo> fieldVos,SessionFactory sessionFactory) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString());
		if(fieldVos!=null){
			try {
				DbProduceUtil.setQueryParams(query, fieldVos);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return query;
	}
	/**
	 * 在当前session中获取NamedQuery
	 * chenmeng
	 * 2011-12-30 上午11:17:31
	 * @param namedStmt
	 * @param sessionFactory
	 * @return
	 */
	public Query getNamedQuery(String namedStmt, SessionFactory sessionFactory) {
		return sessionFactory.getCurrentSession().getNamedQuery(namedStmt);
	}


}
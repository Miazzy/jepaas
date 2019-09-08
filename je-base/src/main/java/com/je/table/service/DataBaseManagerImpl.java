package com.je.table.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.document.util.JeFileUtil;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import com.je.core.constants.ConstantVars;
import com.je.core.constants.ExtFieldType;
import com.je.core.constants.table.ColumnType;
import com.je.core.constants.table.TableType;
import com.je.core.entity.extjs.Model;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.sql.BuildingSql;
import com.je.core.util.ArrayUtils;
import com.je.core.util.DBSqlUtils;
import com.je.core.util.FileOperate;
import com.je.core.util.JdbcUtil;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;

/**
 * 转化数据库操作层次实现类
 */
@Component("dataBaseManager")
public class DataBaseManagerImpl implements DataBaseManager {
	private PCDynaServiceTemplate serviceTemplate;
	private PCServiceTemplate pcServiceTemplate;

	/**
	 * 生成数据库SQL
	 * @param dbName
	 */
	@Override
	public void generateSql(String dbName) {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
//		工作流SQL
//		FileOperate.createFile(JeFileUtil.webrootAbsPath+"/JE/data/config/jbpm.sql", getJbpmSql(dbName),"UTF-8");
//		sql.append(getJbpmSql(dbName)+"\n");
		//资源表 SQL
		sql.append(getResourceTableSql(dbName)+"\n");
		//Rbac SQL
		sql.append(getRbacSql(dbName)+"\n");
//		FileOperate.createFile(JeFileUtil.webrootAbsPath+"/JE/data/config/rbac.sql", getRbacSql(dbName),"UTF-8");
		//所有数据表的SQL
		sql.append(getTableSql(dbName)+"\n");
//		FileOperate.createFile(JeFileUtil.webrootAbsPath+"/JE/data/config/table.sql", getTableSql(dbName),"UTF-8");
		//视图SQL
//		FileOperate.createFile(JeFileUtil.webrootAbsPath+"/JE/data/config/view.sql", getViewCreateSql(dbName),"UTF-8");
//		sql.append(getViewCreateSql(dbName)+"\n");
		FileOperate.createFile(JeFileUtil.webrootAbsPath+"/JE/data/config/dbsql.sql", sql.toString());
	}
	/**
	 * 获取JBPM创建SQL
	 * @param dbName
	 * @return
	 */
	private String getJbpmSql(String dbName){
		String fileName="jbpm.sqlserver.create.sql";
		if(dbName.equals(ConstantVars.STR_ORACLE)){
			fileName="jbpm.oracle.create.sql";
		}else if(dbName.equals(ConstantVars.STR_MYSQL)){
			fileName="jbpm.mysql.create.sql";
		}
		String jbpmSql=FileOperate.readTxt(JeFileUtil.webrootAbsPath+"/JE/data/config/"+fileName, "UTF-8", "\n");
		return jbpmSql;
	}
	private String getResourceTableSql(String dbName){
		StringBuffer tableSql=new StringBuffer();
		tableSql.append(getTableCreateSql("JE_CORE_RESOURCETABLE", dbName));
		tableSql.append(getTableCreateSql("JE_CORE_TABLECOLUMN", dbName));
		tableSql.append(getTableCreateSql("JE_CORE_TABLEKEY", dbName));
		tableSql.append(getTableCreateSql("JE_CORE_TABLEINDEX", dbName));
		return tableSql.toString();
	}
	/**
	 * 获取RBAC的SQL语句
	 * @param dbName
	 * @return
	 */
	private String getRbacSql(String dbName){
		StringBuffer rbacSql=new StringBuffer();
		rbacSql.append(getTableCreateSql("JE_CORE_DEPARTMENT", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_ROLE", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_ENDUSER", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_WORKGROUP", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_ROLEGROUP", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_PERMISSION", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_ADMININFO", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_ADMINPERM", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_WORKGROUP_USER", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_SENTRY_USER", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_ROLE_USER", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_ROLEGROUP_PERM", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_ROLE_PERM", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_ADMIN_USER", dbName));
		rbacSql.append(getTableCreateSql("JE_CORE_ADMIN_PERM", dbName));
		//加入联合主键的约束  中间表对其他两张的外键关系
		return rbacSql.toString();
	}
	/**
	 * 获取所有数据表的SQL
	 * @param dbName
	 * @return
	 */
	private String getTableSql(String dbName){
		StringBuffer sql=new StringBuffer();
		String[] rbacTables=new String[]{"JE_CORE_RESOURCETABLE","JE_CORE_TABLECOLUMN","JE_CORE_TABLEKEY","JE_CORE_TABLEINDEX","JE_CORE_DEPARTMENT","JE_CORE_ROLE","JE_CORE_ENDUSER","JE_CORE_WORKGROUP","JE_CORE_ROLEGROUP","JE_CORE_PERMISSION","JE_CORE_ADMININFO","JE_CORE_ADMINPERM","JE_CORE_WORKGROUP_USER","JE_CORE_SENTRY_USER","JE_CORE_ROLE_USER","JE_CORE_ROLEGROUP_PERM","JE_CORE_ROLE_PERM","JE_CORE_ADMIN_USER","JE_CORE_ADMIN_PERM"};
		List<DynaBean> tables=serviceTemplate.selectList("JE_CORE_RESOURCETABLE", " AND RESOURCETABLE_TABLECODE NOT IN ("+StringUtil.buildArrayToString(rbacTables)+") AND RESOURCETABLE_TYPE IN ('"+TableType.PTTABLE+"','"+TableType.TREETABLE+"') ORDER BY RESOURCETABLE_PARENTTABLECODES DESC ");
		List<DynaBean> resultTables=new ArrayList<DynaBean>();
		List<String> haveTableCodes=new ArrayList<String>();//声明父表名集合，，  用于排序    必须保证 先主表后子表
		for(DynaBean table:tables){
			//表升级。。。 如果主子表。。 则一定要遵循  先主后子的顺序
			Boolean flag=false;
			Integer addIndex=0;
			for(Integer i=0;i<haveTableCodes.size();i++){
				String parentTableCodes=haveTableCodes.get(i);
				if(StringUtil.isEmpty(parentTableCodes))continue;
				for(String parentTableCode:parentTableCodes.split(",")){
					if(table.getStr("RESOURCETABLE_TABLECODE").equals(parentTableCode)){
						flag=true;
						break;
					}
				}
				if(flag){
					addIndex=i;
					break;
				}
			}
			if(flag){
				resultTables.add(addIndex,table);
				haveTableCodes.add(addIndex,table.getStr("RESOURCETABLE_PARENTTABLECODES"));
			}else{
				resultTables.add(table);
				haveTableCodes.add(table.getStr("RESOURCETABLE_PARENTTABLECODES"));
			}
		}
		for(DynaBean table:resultTables){
			sql.append(getTableCreateSql(table.getStr("RESOURCETABLE_TABLECODE"), dbName));
		}
		return sql.toString();
	}
	/**
	 * 获取所有视图的SQL语句
	 * @param dbName
	 * @return
	 */
	private String getViewCreateSql(String dbName){
		StringBuffer sql=new StringBuffer();
		List<DynaBean> views=serviceTemplate.selectList("JE_CORE_RESOURCETABLE", " AND RESOURCETABLE_TYPE='"+TableType.VIEWTABLE+"'");
		for(DynaBean view:views){
			//SQLServer实现方式
			List<Map> lists=pcServiceTemplate.queryMapBySql("select TABLE_NAME,VIEW_DEFINITION from INFORMATION_SCHEMA.VIEWS WHERE TABLE_NAME='"+view.getStr("RESOURCETABLE_TABLECODE")+"'");
			sql.append("--"+view.getStr("RESOURCETABLE_TABLENAME")+"("+view.getStr("RESOURCETABLE_TABLECODE")+")视图创建  开始\n");
			if(lists.size()>0){
				Map values=lists.get(0);
				sql.append(values.get("VIEW_DEFINITION")+"");
			}
			sql.append("--"+view.getStr("RESOURCETABLE_TABLENAME")+"("+view.getStr("RESOURCETABLE_TABLECODE")+")视图创建  结束\n");
		}
		return sql.toString();
	}
	private String getTableCreateSql(String tableCode,String dbName){
		DynaBean resourceTable=BeanUtils.getInstance().getResourceTable(tableCode);
		StringBuffer sql=new StringBuffer();
		//表结构
//		sql.append("--"+resourceTable.getStr("RESOURCETABLE_TABLENAME")+"("+resourceTable.getStr("RESOURCETABLE_TABLECODE")+")表结构创建  开始\n");
		List<String> arraySql = BuildingSql.getInstance(dbName).getDDL4CreateTable(resourceTable);
		for(String cSql:arraySql){
			if(StringUtil.isNotEmpty(cSql)){
				sql.append(cSql+";\n");
			}
		}
//		sql.append("--"+resourceTable.getStr("RESOURCETABLE_TABLENAME")+"("+resourceTable.getStr("RESOURCETABLE_TABLECODE")+")表结构创建  结束\n");
//		sql.append("--"+resourceTable.getStr("RESOURCETABLE_TABLENAME")+"("+resourceTable.getStr("RESOURCETABLE_TABLECODE")+")表数据插入SQL  开始\n");
		//MySql排除的数据
		String[] excludeTables=new String[]{"JE_CORE_HELPMSG","JE_CORE_REPORT","JE_SELL_QQ","JE_SYS_EMAILDATA","JE_CORE_EXCEPTION","JE_SYS_TASKLOG","JE_DEMO_BOOKZL","JE_SCGL_BUG","JE_CORE_JEJAVAAPI","JE_CORE_JEAPI","JE_CORE_JEJAVAAPIFUNC","JE_SYS_HOLIDAYS"};
		String whereSql="";
		if(ArrayUtils.contains(excludeTables, tableCode)){
			whereSql=" AND 1!=1";
		}else if(tableCode.startsWith("JE_EXAM_")){
			whereSql=" AND 1!=1";
		}
		List<DynaBean> datas=serviceTemplate.selectList(tableCode,whereSql);
		Set<String> numberFields=new HashSet<String>();
		for(DynaBean column:(List<DynaBean>)resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS)){
			String fieldType=column.getStr("TABLECOLUMN_TYPE");
			String length=column.getStr("TABLECOLUMN_LENGTH","").toUpperCase();
			if(ColumnType.FLOAT.equals(fieldType)
					|| ColumnType.NUMBER.equals(fieldType)
					|| (ColumnType.CUSTOM.equals(fieldType) && (length.indexOf("FLOAT") != -1))
					|| (ColumnType.CUSTOM.equals(fieldType) && (length.indexOf("NUMBER") != -1))
					){
				numberFields.add(column.getStr("TABLECOLUMN_CODE"));
			}
		}
		if(ArrayUtils.contains(new String[]{"JE_CORE_EXECUTIONLOG","JE_CORE_EXCEPTION"}, tableCode))return sql.toString();
		for(DynaBean bean:datas){
			StringBuilder insertSql = new StringBuilder();
			String fieldNames = BeanUtils.getInstance().getNames4Sql(resourceTable);
			StringBuilder values = new StringBuilder();
			//根据ResourceTable得到DyanBean
			DynaBean dynaBean = BeanUtils.getInstance().getDynaBeanByResourceTable(resourceTable);
			String[] arrayName = BeanUtils.getInstance().getNames(dynaBean);
			for(String name : arrayName){
				if(!name.startsWith(StringUtil.DOLLAR)) {
					//oracle字段内容过长判断
					String[] tables=new String[]{"JE_SYS_TASKLOG","JE_EXAM_THEME","JE_EXAM_PTHEME","JE_EXAM_PPTHEME","JE_EXAM_SPPA","JE_CORE_FUNCINFO","JE_CORE_JEJAVAAPI","JE_CORE_JEJAVAAPIFUNC","JE_CORE_RESOURCEBUTTON","JE_CORE_PROCESSINFO","JE_CORE_REPORT","JE_CORE_JEAPI","JE_CORE_PROTAL","JE_SYS_JMSHISTORY","JE_CORE_QUERYSTRATEGY","JE_CORE_RESOURCECOLUMN","JE_CORE_RESOURCEFIELD","JE_CORE_DICTIONARY","JE_CORE_IMGNEW","JE_CORE_DATASOURCE","JE_CORE_CHARTS"};
					String[] fields=new String[]{"TASKLOG_EXCEPTION","THEME_TM","THEME_JTSL","FUNCINFO_HELP","FUNCINFO_GRIDPRINTINFO","FUNCINFO_GRIDPRINTINFO","FUNCINFO_TABLEFORMTPL","FUNCINFO_GRIDJSLISTENER","FUNCINFO_FORMJSLISTENER","JEAPI_REMARK","JEJAVAAPIFUNC_SM","RESOURCEBUTTON_JSLISTENER","PROCESSINFO_DISPLAYCONFIGINFO","PROCESSINFO_JBPMCONTEXT","PROCESSINFO_BUTTONJSON","PROCESSINFO_DIYEVENTJSON","PROCESSINFO_EVENTJSON","PROCESSINFO_MESSAGEJSON","PROCESSINFO_TASKJSON","PROCESSINFO_EDITORCONTEXT","PROCESSINFO_STARTEXPFN","REPORT_DSCONFIG","REPORT_HTML","REPORT_RENDERER","JEAPI_REMARK","JEAPI_EXAMPLE","PROTAL_DATA","JMSHISTORY_CONTEXT","QUERYSTRATEGY_FN","RESOURCECOLUMN_JSLISTENER","RESOURCEFIELD_JSLISTENER","DICTIONARY_SQLPZXXLB","DICTIONARY_SQLCONFIG","IMGNEW_ZW","DATASOURCE_DATA","DATASOURCE_CONFIG","CHARTS_DSCONFIG","CHARTS_ATTRCONFIG"};
					String[] enFields=new String[]{"RESOURCECOLUMN_NAME_EN","RESOURCEFIELD_NAME_EN","MENU_MENUSUBNAME"};
					if(ConstantVars.STR_ORACLE.equals(dbName) && ArrayUtils.contains(tables, tableCode) && ArrayUtils.contains(fields, name)){
						values.append("NULL,");
					}else if(bean.get(name)==null || ArrayUtils.contains(enFields,name)){
						values.append("NULL,");
					}else if(("SY_PARENT".equals(name) || "PARENT".equals(name)) && StringUtil.isEmpty(bean.getStr(name))){
						values.append("NULL,");
					}else if(numberFields.contains(name)){
						values.append(""+bean.get(name)+",");
					}else{
						values.append("'"+StringUtil.getDefaultValue(bean.get(name),"").replaceAll("'", "''")+"',");
					}
				}
			}
			values.setLength(values.length()-1);
			insertSql.append("INSERT INTO "+tableCode+"("+fieldNames+") VALUES ("+values+");\n");
			sql.append(insertSql);
		}
//		sql.append("--"+resourceTable.getStr("RESOURCETABLE_TABLENAME")+"("+resourceTable.getStr("RESOURCETABLE_TABLECODE")+")表数据插入SQL  结束\n");
		return sql.toString();
	}

	/**
	 * 同步数据
	 * @throws Exception
	 */
	public void syncOracleData() throws Exception{
		//查询当前数据库缺失数据
		JdbcUtil jdbcUtil=JdbcUtil.getInstance("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://192.168.27.5\\SQLSERVER08;DatabaseName=NEWXJ;", "sa", "Aaa123456");
		Map<String,String> impTablesInfo=new HashMap<String,String>();
//		
//		impTablesInfo.put("JE_CORE_PROTAL", "PROTAL_DATA");
//		impTablesInfo.put("JE_SYS_TASKLOG", "TASKLOG_EXCEPTION");

//		impTablesInfo.put("JE_EXAM_THEME", "THEME_TM,THEME_JTSL");
//		impTablesInfo.put("JE_EXAM_PTHEME", "THEME_TM,THEME_JTSL");

//		impTablesInfo.put("JE_EXAM_PPTHEME", "THEME_TM,THEME_JTSL");
//		impTablesInfo.put("JE_EXAM_SPPA", "THEME_TM,THEME_JTSL");

//		impTablesInfo.put("JE_CORE_FUNCINFO", "FUNCINFO_HELP,FUNCINFO_GRIDPRINTINFO,FUNCINFO_GRIDPRINTINFO,FUNCINFO_TABLEFORMTPL,FUNCINFO_GRIDJSLISTENER,FUNCINFO_FORMJSLISTENER");
//		
//		impTablesInfo.put("JE_CORE_JEJAVAAPI", "JEAPI_REMARK");
//		impTablesInfo.put("JE_CORE_JEJAVAAPIFUNC", "JEJAVAAPIFUNC_SM");
		//未更新
//		impTablesInfo.put("JE_CORE_PROCESSINFO", "PROCESSINFO_DISPLAYCONFIGINFO,PROCESSINFO_JBPMCONTEXT,PROCESSINFO_BUTTONJSON,PROCESSINFO_DIYEVENTJSON,PROCESSINFO_EVENTJSON,PROCESSINFO_MESSAGEJSON,PROCESSINFO_TASKJSON,PROCESSINFO_EDITORCONTEXT,PROCESSINFO_STARTEXPFN");
//		impTablesInfo.put("JE_CORE_REPORT", "REPORT_DSCONFIG,REPORT_HTML,REPORT_RENDERER");
//		impTablesInfo.put("JE_CORE_JEAPI", "JEAPI_REMARK,JEAPI_EXAMPLE");
//		impTablesInfo.put("JE_SYS_JMSHISTORY", "JMSHISTORY_CONTEXT");
//		impTablesInfo.put("JE_CORE_QUERYSTRATEGY", "QUERYSTRATEGY_FN");
//		impTablesInfo.put("JE_CORE_RESOURCEBUTTON", "RESOURCEBUTTON_JSLISTENER");

//		impTablesInfo.put("JE_CORE_RESOURCECOLUMN", "RESOURCECOLUMN_JSLISTENER");
//		impTablesInfo.put("JE_CORE_RESOURCEFIELD", "RESOURCEFIELD_JSLISTENER");

//		impTablesInfo.put("JE_CORE_DICTIONARY", "DICTIONARY_SQLPZXXLB,DICTIONARY_SQLCONFIG");
//		impTablesInfo.put("JE_CORE_IMGNEW", "IMGNEW_ZW");
//		impTablesInfo.put("JE_CORE_DATASOURCE", "DATASOURCE_DATA,DATASOURCE_CONFIG");
//		impTablesInfo.put("JE_CORE_CHARTS", "CHARTS_DSCONFIG,CHARTS_ATTRCONFIG");
		for(String tableCode:impTablesInfo.keySet()){
			DynaBean table=BeanUtils.getInstance().getResourceTable(tableCode);
			String fieldNames =BeanUtils.getInstance().getNames4Sql(table);
			//更新功能
			List<HashMap> datas=jdbcUtil.query("SELECT "+fieldNames+" from "+tableCode);
			for(Integer i=0;i<datas.size();i++){
				HashMap values=datas.get(i);
				//			System.out.println(values.toString());
				//			DynaBean dynaBean=new DynaBean("JE_SYS_TASKLOG",true);
				//			dynaBean.setBeanValues(values);
				//			serviceTemplate.insert(dynaBean);
				//			JdbcUtil util=JdbcUtil.getInstance();
				//			Connection conn=util.getConnection();
				//			PreparedStatement pstmt=conn.prepareStatement(" UPDATE JE_SYS_TASKLOG SET TASKLOG_EXCEPTION=:TASKLOG_EXCEPTION WHERE JE_SYS_TASKLOG_ID='"+values.get("JE_SYS_TASKLOG_ID")+"'");
				//			Clob clob=Hibernate.createClob(values.getString("TASKLOG_EXCEPTION"));
				//		    pstmt.setClob(0,(Clob) values.get("TASKLOG_EXCEPTION"));
				//		    pstmt.setString(1, values.get("TASKLOG_EXCEPTION")+"");
				//		    pstmt.executeUpdate();
				//		    pstmt.close();
				String id=values.get(tableCode+"_ID")+"";
//				DynaBean dynaBean=serviceTemplate.selectOneByPk(tableCode, id);
//				if(dynaBean==null)continue;
				DynaBean dynaBean=new DynaBean(tableCode,true);
				dynaBean.set(tableCode+"_ID", id);
				Long count=serviceTemplate.selectCount(tableCode, " and "+tableCode+"_ID"+" ='"+id+"'");
				for(String column:impTablesInfo.get(tableCode).split(",")){
					dynaBean.set(column, values.get(column));
				}
				if(count>0){
					serviceTemplate.update(dynaBean);
				}else{
					serviceTemplate.insert(dynaBean);
				}
			}
		}
		jdbcUtil.close();
	}

	/**
	 * 同步mySql数据
	 * @throws Exception
	 */
	public void syncMySqlData() throws Exception{
		//查询当前数据库缺失数据
		JdbcUtil jdbcUtil=JdbcUtil.getInstance("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://192.168.27.4\\PCAT;DatabaseName=JELEPHANT", "sa", "Aaa123456");
		Map<String,String> impTablesInfo=new HashMap<String,String>();
//		impTablesInfo.put("JE_SELL_QQ", "");
//		impTablesInfo.put("JE_SYS_EMAILDATA", "");
//		impTablesInfo.put("JE_CORE_EXCEPTION", "");
//		impTablesInfo.put("JE_SYS_TASKLOG", "");
//		impTablesInfo.put("JE_SYS_TASKLOG", "TASKLOG_EXCEPTION");
//		impTablesInfo.put("JE_EXAM_THEME", "THEME_TM,THEME_JTSL");
//		impTablesInfo.put("JE_EXAM_PTHEME", "THEME_TM,THEME_JTSL");
//		impTablesInfo.put("JE_EXAM_PPTHEME", "THEME_TM,THEME_JTSL");
//		impTablesInfo.put("JE_EXAM_SPPA", "THEME_TM,THEME_JTSL");
//		
//		impTablesInfo.put("JE_CORE_HELPMSG", "");
//		impTablesInfo.put("JE_SYS_JMSHISTORY", "JMSHISTORY_CONTEXT");



		//1.第一步
//		impTablesInfo.put("JE_CORE_JEAPI", "");
//		//2.第二步
//		impTablesInfo.put("JE_CORE_JEJAVAAPI", "");
//		impTablesInfo.put("JE_CORE_REPORT", "");
//		impTablesInfo.put("JE_CORE_FUNCINFO", "FUNCINFO_HELP,FUNCINFO_GRIDPRINTINFO,FUNCINFO_GRIDPRINTINFO,FUNCINFO_TABLEFORMTPL,FUNCINFO_GRIDJSLISTENER,FUNCINFO_FORMJSLISTENER");
//		impTablesInfo.put("JE_CORE_PROCESSINFO", "PROCESSINFO_EVENTJSON,PROCESSINFO_DIYEVENTJSON,PROCESSINFO_BUTTONJSON,PROCESSINFO_DISPLAYCONFIGINFO,PROCESSINFO_MESSAGEJSON,PROCESSINFO_TASKJSON,PROCESSINFO_EDITORCONTEXT,PROCESSINFO_STARTEXPFN");
//		impTablesInfo.put("JE_CORE_REPORT", "REPORT_DSCONFIG,REPORT_HTML,REPORT_RENDERER");
		impTablesInfo.put("JE_CORE_ASSOCIATIONFIELD", "QUERYSTRATEGY_FN");
//		impTablesInfo.put("JE_CORE_FUNCRELATION", "");
//		impTablesInfo.put("JE_CORE_FUNC_PERM", "");
//		impTablesInfo.put("JE_CORE_RESOURCECOLUMN", "RESOURCECOLUMN_JSLISTENER,RESOURCECOLUMN_NAME_EN");
//		impTablesInfo.put("JE_CORE_RESOURCEFIELD", "RESOURCEFIELD_JSLISTENER,RESOURCEFIELD_NAME_EN");
//		impTablesInfo.put("JE_CORE_RESOURCEBUTTON", "RESOURCEBUTTON_JSLISTENER,RESOURCEBUTTON_NAME_EN");
//		impTablesInfo.put("JE_CORE_DICTIONARY", "DICTIONARY_SQLPZXXLB,DICTIONARY_SQLCONFIG");
//		impTablesInfo.put("JE_CORE_IMGNEW", "IMGNEW_ZW");
//		impTablesInfo.put("JE_CORE_DATASOURCE", "DATASOURCE_DATA,DATASOURCE_CONFIG");
//		impTablesInfo.put("JE_CORE_CHARTS", "CHARTS_DSCONFIG,CHARTS_ATTRCONFIG");
////		//3.第三步
//		impTablesInfo.put("JE_CORE_JEJAVAAPIFUNC", "");
//		impTablesInfo.put("JE_SYS_HOLIDAYS", "");
//		impTablesInfo.put("JE_CORE_PROTAL", "PROTAL_DATA");
//		impTablesInfo.put("JE_SCGL_BUG", "");
		for(String tableCode:impTablesInfo.keySet()){
			try{
				DynaBean table=BeanUtils.getInstance().getResourceTable(tableCode);
				String fieldNames =BeanUtils.getInstance().getNames4Sql(table);
				//更新功能
				List<HashMap> datas=jdbcUtil.query("SELECT "+fieldNames+" from "+tableCode+("JE_CORE_PROTAL".equals(tableCode)?" WHERE JE_CORE_PROTAL_ID!='f5feb11b-502a-4496-bafb-ba97d35c33dd'":""));
				for(Integer i=0;i<datas.size();i++){
					HashMap values=datas.get(i);
					//			System.out.println(values.toString());
					//			DynaBean dynaBean=new DynaBean("JE_SYS_TASKLOG",true);
					//			dynaBean.setBeanValues(values);
					//			serviceTemplate.insert(dynaBean);
					//			JdbcUtil util=JdbcUtil.getInstance();
					//			Connection conn=util.getConnection();
					//			PreparedStatement pstmt=conn.prepareStatement(" UPDATE JE_SYS_TASKLOG SET TASKLOG_EXCEPTION=:TASKLOG_EXCEPTION WHERE JE_SYS_TASKLOG_ID='"+values.get("JE_SYS_TASKLOG_ID")+"'");
					//			Clob clob=Hibernate.createClob(values.getString("TASK LOG_EXCEPTION"));
					//		    pstmt.setClob(0,(Clob) values.get("TASKLOG_EXCEPTION"));
					//		    pstmt.setString(1, values.get("TASKLOG_EXCEPTION")+"");
					//		    pstmt.executeUpdate();
					//		    pstmt.close();
					String id=values.get(tableCode+"_ID")+"";
					//				DynaBean dynaBean=serviceTemplate.selectOneByPk(tableCode, id);
					//				if(dynaBean==null)continue;
					DynaBean dynaBean=new DynaBean(tableCode,true);
					dynaBean.set(tableCode+"_ID", id);
					Long count=serviceTemplate.selectCount(tableCode, " and "+tableCode+"_ID"+" ='"+id+"'");
					for(String column:fieldNames.split(",")){
						dynaBean.set(column, values.get(column));
					}
					if(count>0){
						serviceTemplate.update(dynaBean);
					}else{
						try{
							serviceTemplate.insert(dynaBean);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}catch(Exception e){

			}
		}
		jdbcUtil.close();
	}
	/**
	 * 同步jbpm数据
	 */
	public void syncOracleJbpm(){
		JdbcUtil jdbcUtil=JdbcUtil.getInstance("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://192.168.27.4\\PCAT;DatabaseName=JELEPHANT", "sa", "Aaa123456");
		List<String> tables=new ArrayList<String>();
//		tables.add("JBPM4_DEPLOYMENT");
//		tables.add("JBPM4_DEPLOYPROP");
//		tables.add("JBPM4_EXECUTION");
//		tables.add("JBPM4_HIST_PROCINST");
//		tables.add("JBPM4_HIST_TASK");
//		tables.add("JBPM4_HIST_VAR");
//		tables.add("JBPM4_HIST_ACTINST");

//		tables.add("JBPM4_HIST_DETAIL");
//		tables.add("JBPM4_ID_GROUP");
//		tables.add("JBPM4_ID_MEMBERSHIP");
//		tables.add("JBPM4_ID_USER");
//		tables.add("JBPM4_JOB");
		//加入数据考虑image对象存储到oracle的clob  
//		tables.add("JBPM4_LOB");
		//添加这个先删除原有表数据
//		tables.add("JBPM4_PROPERTY");
//		tables.add("JBPM4_SWIMLANE");

		tables.add("JBPM4_TASK");
		tables.add("JBPM4_PARTICIPATION");
		tables.add("JBPM4_VARIABLE");
		for(String tableCode:tables){
			List<HashMap> datas=jdbcUtil.query("SELECT * from "+tableCode+"");
			if(datas.size()>0){
				String[] fields=ArrayUtils.getArray(datas.get(0).keySet());
				String[] values=new String[fields.length];
				for(Integer i =0;i<fields.length;i++){
					values[i]=":"+fields[i];
				}
				for(HashMap data:datas){
					String insertSql="INSERT INTO "+tableCode+"("+StringUtil.buildSplitString(fields, ",")+")"+" VALUES ("+StringUtil.buildSplitString(values, ",")+")";
					Session session=pcServiceTemplate.getSessionFactory().getCurrentSession();
					SQLQuery sqlQuery = session.createSQLQuery(insertSql);
					for(String fieldCode:fields){
						Object value=data.get(fieldCode);
						if(value instanceof Timestamp){
							sqlQuery.setTimestamp(fieldCode, (Timestamp)data.get(fieldCode));
						}
						//处理将image对象转换成blob存储
						else if("JBPM4_LOB".equals(tableCode) && "BLOB_VALUE_".equals(fieldCode)){
							sqlQuery.setBinary(fieldCode, (byte[])data.get(fieldCode));
						}
						//MySql使用  开始
						else if("JBPM4_DEPLOYPROP".equals(tableCode) && "LONGVAL_".equals(fieldCode)){
							if(StringUtil.isEmpty(value+"")){
								sqlQuery.setBigInteger(fieldCode, null);
							}else{
								sqlQuery.setString(fieldCode, StringUtil.getDefaultValue(value, ""));
							}
						}else if(value instanceof BigDecimal){
							sqlQuery.setBigDecimal(fieldCode, (BigDecimal)value);
						}else if(value==null){
							sqlQuery.setString(fieldCode, null);
						}
						//MySql使用  结束
						else{
							sqlQuery.setString(fieldCode, StringUtil.getDefaultValue(value, ""));
						}
					}
					try{
						sqlQuery.executeUpdate();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	public byte[] getByte(InputStream value){
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		try {
			while ((rc = value.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

	/**
	 * 转Myql
	 */
	@Override
	public void toMysql() {
		// TODO Auto-generated method stub
		Map<String,String> configs=FileOperate.readProperties(JeFileUtil.webrootAbsPath+"/JE/data/config/mysql.jdbc.properties");
		String dbName=configs.get("dbsql.dbname");
		String url=configs.get("dbsql.url");
		String username=configs.get("dbsql.username");
		String password=configs.get("dbsql.password");
		JdbcUtil jdbcUtil=JdbcUtil.getInstance("com.mysql.jdbc.Driver", url+"mysql", username,password);
		jdbcUtil.executeSql("CREATE DATABASE "+dbName+" DEFAULT CHARACTER SET gbk -- GBK Simplified Chinese");
		jdbcUtil.close();
		jdbcUtil=JdbcUtil.getInstance("com.mysql.jdbc.Driver", url+dbName, username,password);
		String[] rbacTables=new String[]{"JE_CORE_RESOURCETABLE","JE_CORE_TABLECOLUMN","JE_CORE_TABLEKEY","JE_CORE_TABLEINDEX","JE_CORE_DEPARTMENT","JE_CORE_ROLE","JE_CORE_ENDUSER","JE_CORE_WORKGROUP","JE_CORE_ROLEGROUP","JE_CORE_PERMISSION","JE_CORE_ADMININFO","JE_CORE_ADMINPERM","JE_CORE_WORKGROUP_USER","JE_CORE_SENTRY_USER","JE_CORE_ROLE_USER","JE_CORE_ROLEGROUP_PERM","JE_CORE_ROLE_PERM","JE_CORE_ADMIN_USER","JE_CORE_ADMIN_PERM"};
		List<DynaBean> tables=serviceTemplate.selectList("JE_CORE_RESOURCETABLE", " AND RESOURCETABLE_TABLECODE NOT IN ("+StringUtil.buildArrayToString(rbacTables)+") AND RESOURCETABLE_TYPE IN ('"+TableType.PTTABLE+"','"+TableType.TREETABLE+"') ORDER BY RESOURCETABLE_PARENTTABLECODES ASC ");
		for(String tableCode:rbacTables){
			doTableCreate(tableCode, ConstantVars.STR_MYSQL, jdbcUtil);
		}
		for(DynaBean table:tables){
			doTableCreate(table.getStr("RESOURCETABLE_TABLECODE"), ConstantVars.STR_MYSQL, jdbcUtil);
		}
		doJbpmCreate(jdbcUtil, ConstantVars.STR_MYSQL);
		doViewCreate(jdbcUtil,ConstantVars.STR_MYSQL);
		jdbcUtil.close();
	}

	/**
	 * 转oracle
	 */
	@Override
	public void toOracle() {
		// TODO Auto-generated method stub
		Map<String,String> configs=FileOperate.readProperties(JeFileUtil.webrootAbsPath+"/JE/data/config/oracle.jdbc.properties");
		//1.创建oracle用户并授权
		String dbaUsername=configs.get("dba.username");
		String dbaPassword=configs.get("dba.password");
		String url=configs.get("dbsql.url");
		String tablespace=configs.get("dbsql.tablespace");
		String tablespaceDir=configs.get("dbsql.tablespace.dir");
		String username=configs.get("dbsql.username");
		String password=configs.get("dbsql.password");
		JdbcUtil jdbcUtil=JdbcUtil.getInstance("oracle.jdbc.OracleDriver", url, dbaUsername,dbaPassword);
		Integer cursor=2000;
		//扩大游标数。
		List<HashMap> cursors=jdbcUtil.query("SELECT MAX(A.VALUE) AS HIGHEST_OPEN_CUR, P.VALUE AS MAX_OPEN_CUR FROM V$SESSTAT A, V$STATNAME B, V$PARAMETER P WHERE A.STATISTIC# = B.STATISTIC# AND B.NAME = 'opened cursors current' AND P.NAME = 'open_cursors' GROUP BY P.VALUE");
		if(cursors.size()>0){
			cursor=Integer.parseInt(cursors.get(0).get("MAX_OPEN_CUR")+"");
		}
		if(cursor<20000){
			jdbcUtil.executeSql("alter system set open_cursors = 20000");
		}
		//创建表空间
		jdbcUtil.executeSql("create tablespace "+tablespace+" datafile '"+tablespaceDir+"/"+tablespace+".dbf' size 10m autoextend on next 5m maxsize unlimited extent management local");
		//创建用户
		jdbcUtil.executeSql("create user "+username+" "+
				"identified by \""+password+"\" "+
				"default tablespace "+tablespace+" "+
				"temporary tablespace temp "+
				"profile default "+
				"quota unlimited on "+tablespace+"");
		//授权
		jdbcUtil.executeSql("grant connect to "+username+"");
		jdbcUtil.executeSql("grant exp_full_database to "+username+"");
		jdbcUtil.executeSql("grant imp_full_database to "+username+"");
		jdbcUtil.close();
		//2.使用创建的用户登录
		jdbcUtil=JdbcUtil.getInstance("oracle.jdbc.OracleDriver", url, username, password);
		String[] rbacTables=new String[]{"JE_CORE_RESOURCETABLE","JE_CORE_TABLECOLUMN","JE_CORE_TABLEKEY","JE_CORE_TABLEINDEX","JE_CORE_DEPARTMENT","JE_CORE_ROLE","JE_CORE_ENDUSER","JE_CORE_WORKGROUP","JE_CORE_ROLEGROUP","JE_CORE_PERMISSION","JE_CORE_ADMININFO","JE_CORE_ADMINPERM","JE_CORE_WORKGROUP_USER","JE_CORE_SENTRY_USER","JE_CORE_ROLE_USER","JE_CORE_ROLEGROUP_PERM","JE_CORE_ROLE_PERM","JE_CORE_ADMIN_USER","JE_CORE_ADMIN_PERM"};
		List<DynaBean> tables=serviceTemplate.selectList("JE_CORE_RESOURCETABLE", " AND RESOURCETABLE_TABLECODE NOT IN ("+StringUtil.buildArrayToString(rbacTables)+") AND RESOURCETABLE_TYPE IN ('"+TableType.PTTABLE+"','"+TableType.TREETABLE+"') ORDER BY RESOURCETABLE_PARENTTABLECODES ASC ");
		for(String tableCode:rbacTables){
			doTableCreate(tableCode, ConstantVars.STR_ORACLE, jdbcUtil);
		}
		for(DynaBean table:tables){
			doTableCreate(table.getStr("RESOURCETABLE_TABLECODE"), ConstantVars.STR_ORACLE, jdbcUtil);
		}
		doJbpmCreate(jdbcUtil,ConstantVars.STR_ORACLE);
		doViewCreate(jdbcUtil,ConstantVars.STR_ORACLE);
		jdbcUtil.close();
		if(cursor<20000){
			jdbcUtil=JdbcUtil.getInstance("oracle.jdbc.OracleDriver", url, dbaUsername, dbaPassword);
			jdbcUtil.executeSql("alter system set open_cursors = "+cursor+"");
			jdbcUtil.close();
		}
	}
	public void doViewCreate(JdbcUtil jdbcUtil,String dbName){
		try {
			String fileName="view.oracle.create.sql";
			if(dbName.equals(ConstantVars.STR_SQLSERVER)){
				fileName="view.sqlserver.create.sql";
			}else if(dbName.equals(ConstantVars.STR_MYSQL)){
				fileName="view.mysql.create.sql";
			}
			//String[] sysViews=new String[]{"JE_CORE_VCOLUMNANDFIELD","JE_CORE_VFIELDINFO","JE_SYS_FUNCSELECT","JE_CORE_VCURRENTTASK","JE_CORE_VROLEUSER","JE_CORE_VDEPTUSER","JE_CORE_VPROCESSINSTANCE","PC_CORE_VCURRENTTASK","JE_CORE_VSENTRYUSER","JE_SYS_EMAILSELECT"};
			Connection conn=jdbcUtil.getConnection();
			ScriptRunner runner=new ScriptRunner(conn, false, false);
			runner.setErrorLogWriter(null);
			runner.setLogWriter(null);
			runner.runScript(Resources.getResourceAsReader(JeFileUtil.webrootAbsPath+"/JE/data/config/"+fileName));
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 工作流表资源处理
	 * @param jdbcUtil
	 */
	public void doJbpmCreate(JdbcUtil jdbcUtil,String dbName){
		String fileName="jbpm.sqlserver.create.sql";
		if(dbName.equals(ConstantVars.STR_ORACLE)){
			fileName="jbpm.oracle.create.sql";
		}else if(dbName.equals(ConstantVars.STR_MYSQL)){
			fileName="jbpm.mysql.create.sql";
		}
		try {
			Connection connection=pcServiceTemplate.getConnection();
			Connection conn=jdbcUtil.getConnection();
			ScriptRunner runner=new ScriptRunner(conn, false, false);
			runner.setErrorLogWriter(null);
			runner.setLogWriter(null);
			runner.runScript(Resources.getResourceAsReader(JeFileUtil.webrootAbsPath+"/JE/data/config/"+fileName));
			//插入流程数据
			String[] jbpmTables=new String[]{"JBPM4_DEPLOYMENT","JBPM4_DEPLOYPROP","JBPM4_EXECUTION","JBPM4_HIST_PROCINST","JBPM4_HIST_TASK","JBPM4_HIST_VAR","JBPM4_HIST_ACTINST","JBPM4_HIST_DETAIL","JBPM4_ID_GROUP","JBPM4_ID_MEMBERSHIP","JBPM4_ID_USER","JBPM4_JOB","JBPM4_LOB","JBPM4_PROPERTY","JBPM4_SWIMLANE","JBPM4_TASK","JBPM4_PARTICIPATION","JBPM4_VARIABLE"};
			for(String tableCode:jbpmTables){
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from "+tableCode+"");
				ResultSetMetaData data = resultSet.getMetaData();
				int count = data.getColumnCount();
				String[] fields=new String[count];
				String[] values=new String[count];
				Model[] models=new Model[count];
				for (int i = 1; i <= count; i++) {
					String columnName = data.getColumnName(i);
					fields[i-1]=columnName;
					values[i-1]="?";
					Model model=DBSqlUtils.getExtModel(columnName, data.getColumnType(i));
					models[i-1]=model;
				}
				String insertSql="INSERT INTO "+tableCode+"("+StringUtil.buildSplitString(fields, ",")+")"+" VALUES ("+StringUtil.buildSplitString(values, ",")+")";
				while (resultSet.next()) {
					PreparedStatement  pstmt=conn.prepareStatement(insertSql);
					for (int i = 1; i <= count; i++) {
						Model model=models[i-1];
						if(ExtFieldType.INT.equals(model.getType())){
							pstmt.setInt(i, resultSet.getInt(i));
						}else if(ExtFieldType.STRING.equals(model.getType())){
							pstmt.setString(i, resultSet.getString(i));
						}else{
							pstmt.setObject(i, resultSet.getObject(i));
						}
					}
					pstmt.executeUpdate();
					pstmt.close();
				}
				statement.close();
			}
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void doTableCreate(String tableCode,String dbName,JdbcUtil jdbcUtil){
		DynaBean resourceTable=BeanUtils.getInstance().getResourceTable(tableCode);
//		StringBuffer sql=new StringBuffer();
		//表结构
		List<String> arraySql = BuildingSql.getInstance(dbName).getDDL4CreateTable(resourceTable);
		for(String cSql:arraySql){
			if(StringUtil.isNotEmpty(cSql)){
//				sql.append(cSql);
				jdbcUtil.executeSql(cSql);
			}
		}
		List<DynaBean> datas=serviceTemplate.selectList(tableCode,"");
		Set<String> numberFields=new HashSet<String>();
		List<DynaBean> columns=(List<DynaBean>)resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
		String[] fields=new String[columns.size()];
		String[] values=new String[columns.size()];
		for(int i=0;i<columns.size();i++){
			DynaBean column=columns.get(i);
			fields[i]=column.getStr("TABLECOLUMN_CODE");
			values[i]="?";
			String fieldType=column.getStr("TABLECOLUMN_TYPE");
			String length=column.getStr("TABLECOLUMN_LENGTH","").toUpperCase();
			if(ColumnType.FLOAT.equals(fieldType)
					|| ColumnType.NUMBER.equals(fieldType)
					|| (ColumnType.CUSTOM.equals(fieldType) && (length.indexOf("FLOAT") != -1))
					|| (ColumnType.CUSTOM.equals(fieldType) && (length.indexOf("NUMBER") != -1))
					){
				numberFields.add(column.getStr("TABLECOLUMN_CODE"));
			}
		}
		String insertSql="INSERT INTO "+tableCode+"("+StringUtil.buildSplitString(fields, ",")+")"+" VALUES ("+StringUtil.buildSplitString(values, ",")+")";
		Connection conn=jdbcUtil.getConnection();
		for(DynaBean bean:datas){
			PreparedStatement pstmt = null;
			try{
				pstmt = conn.prepareStatement(insertSql);
				for(int i=0;i<columns.size();i++){
					DynaBean column=columns.get(i);
					String fieldType=column.getStr("TABLECOLUMN_TYPE");
					String length=column.getStr("TABLECOLUMN_LENGTH","").toUpperCase();
					String code=column.getStr("TABLECOLUMN_CODE");
					if(ColumnType.FLOAT.equals(fieldType) || (ColumnType.CUSTOM.equals(fieldType) && (length.indexOf("FLOAT") != -1)) ){
						pstmt.setDouble(i+1, bean.getDouble(code,0));
					}else if(ColumnType.NUMBER.equals(fieldType) || (ColumnType.CUSTOM.equals(fieldType) && (length.indexOf("NUMBER") != -1))){
						pstmt.setInt(i+1, bean.getInt(code,0));
					}else if(ColumnType.CLOB.equals(fieldType)){
						pstmt.setString(i+1, bean.getStr(code,""));
					}else if(ColumnType.BIGCLOB.equals(fieldType)){
						pstmt.setString(i+1, bean.getStr(code,""));
					}else{
						pstmt.setString(i+1, bean.getStr(code,""));
					}
				}
				pstmt.executeUpdate();
				pstmt.close();
			}catch(Exception e){
				throw new PlatformException("将资源表格式数据应用到其他数据库", PlatformExceptionEnum.JE_CORE_TABLE_CREATEOTHERDB_ERROR,new Object[]{dbName,tableCode},e);
			} finally {
				JdbcUtil.close(null, pstmt,conn);
			}
		}
	}
}

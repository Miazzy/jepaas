package com.je.core.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;

import com.je.core.constants.ConstantVars;
import com.je.core.constants.table.ColumnType;
import com.je.core.constants.table.TableType;
import com.je.core.dao.PCDaoTemplateImpl;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;

/**
 * 此类用于查询分析数据库表结构
 * @author YUNFENGCHENG
 *
 */
public class DataBaseUtils {
	private static Logger logger = LoggerFactory.getLogger(DataBaseUtils.class);
	/**
	 * 实例化此类
	 * 研发部:云凤程
	 * 2012-4-7
	 * @return
	 */
	public static DataBaseUtils getInstance(){
		return DataBaseUtilsHolder.DataBaseUtils;
	}
	private static class DataBaseUtilsHolder{
		private static final DataBaseUtils DataBaseUtils = new DataBaseUtils();
	}
	/**
	 * 目前单单适合月oracle ,其他数据库可一直接用DatabaseMetaData得到
	 * 返回表的基本系信息
	 * 云哥
	 * @return
	 */
	public static DynaBean getTableBaseInfo(DynaBean table){
		Connection connection  = null;
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		try {
			connection = ((DataSource)SpringContextHolder.getBean("dataSource")).getConnection();
		} catch (Exception e) {
			throw new PlatformException("获得数据库连接失败",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
//			logger.error("获得数据库连接失败");
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		//1.定义表对象
		DynaBean resourceTable = table;
		try {
			try {
				ps = connection.prepareStatement("select * from USER_TAB_COMMENTS t where  t.table_name= '"+tableCode+"'");
				rs = ps.executeQuery();
				//装载TableCode
				resourceTable.set("RESOURCETABLE_TABLECODE",tableCode);
				resourceTable.set("RESOURCETABLE_OLDTABLECODE",tableCode);
				resourceTable.set("RESOURCETABLE_TYPE",TableType.PTTABLE);
				while (rs.next()) {
					String  comments  = (String)rs.getObject("COMMENTS");
					//装载表注释
					resourceTable.set("RESOURCETABLE_TABLENAME",comments);
				}
				//装载他是已经被创建的
				resourceTable.set("RESOURCETABLE_ISCREATE","1");
			} catch (Exception e) {
				throw new PlatformException("获取资源表结构异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
			} finally {
				JdbcUtil.close(rs, ps);
			}

			//2.定义和装载键对象
			List<DynaBean> keys = new ArrayList<DynaBean>();
			List<DynaBean> indexs=new ArrayList<DynaBean>();
			DynaBean tableKey = null;
			DynaBean tableIndex = null;
			DatabaseMetaData metaData = connection.getMetaData();
			try {
				//主键
				rs = metaData.getPrimaryKeys(null, metaData.getUserName(), tableCode);
				String pkName="";
				while (rs.next()) {
					tableKey = new DynaBean("JE_CORE_TABLEKEY",false);
					tableKey.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEKEY_ID");
					tableKey.set("TABLEKEY_COLUMNCODE",rs.getString("COLUMN_NAME"));
					tableKey.set("TABLEKEY_TYPE","Primary");
					tableKey.set("TABLEKEY_ISCREATE","1");
					tableKey.set("TABLEKEY_CODE",rs.getString("PK_NAME"));
					keys.add(tableKey);
					pkName=rs.getString("COLUMN_NAME");
					tableIndex=new DynaBean("JE_CORE_TABLEINDEX",false);
					tableIndex.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEINDEX_ID");
					tableIndex.set("TABLEINDEX_NAME","JE_"+DateUtils.getUniqueTime()+"_ID");
					tableIndex.set("TABLEINDEX_FIELDCODE",pkName);
					tableIndex.set("TABLEINDEX_FIELDNAME","主键ID");
					tableIndex.set("TABLEINDEX_ISCREATE","1");
					tableIndex.set("TABLEINDEX_UNIQUE","1");
					tableIndex.set("TABLEINDEX_CLASSIFY","SYS");
					tableIndex.set("SY_ORDERINDEX",0);
					indexs.add(tableIndex);
				}
				resourceTable.set("RESOURCETABLE_PKCODE", pkName);
			} catch (SQLException e) {
				throw new PlatformException("获取资源表【"+tableCode+"】结构异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
			} finally {
				JdbcUtil.close(rs, null);
			}
			//外键
			rs = metaData.getImportedKeys(null, metaData.getUserName(), tableCode);
			//默认给成否
			resourceTable.set("RESOURCETABLE_ISUSEFOREIGNKEY","0");
			while (rs.next()) {
				/**
				 PKTABLE_CAT String => 被导进的主键表种别（可为 null）
				 PKTABLE_SCHEM String => 被导进的主键表模式（可为 null）
				 PKTABLE_NAME String => 被导进的主键表名称
				 PKCOLUMN_NAME String => 被导进的主键列名称
				 FKTABLE_CAT String => 外键表种别（可为 null）
				 FKTABLE_SCHEM String => 外键表模式（可为 null）
				 FKTABLE_NAME String => 外键表名称
				 FKCOLUMN_NAME String => 外键列名称
				 KEY_SEQ short => 外键中的序列号（值 1 表示外键中的第一列，值 2 表示外键中的第二列）
				 UPDATE_RULE short => 更新主键时外键发生的变化
				 DELETE_RULE short => 删除主键时外键发生的变化
				 PK_NAME String => 主键的名称（可为 null）
				 FK_NAME String => 外键的名称（可为 null）
				 DEFERRABILITY short => 是否可以将对外键约束的评估延迟到提交时间
				 */
				tableKey = new DynaBean("JE_CORE_TABLEKEY",false);
				tableKey.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEKEY_ID");
				String linkType = rs.getString("DELETE_RULE");
				if("0".equals(linkType)){
					tableKey.set("TABLEKEY_LINETYLE","Cascade");
				}else if("1".equals(linkType)){
					tableKey.set("TABLEKEY_LINETYLE","Noaction");
				}else if("2".equals(linkType)){
					tableKey.set("TABLEKEY_LINETYLE","Setnull");
				}
				tableKey.set("TABLEKEY_LINKTABLE",rs.getString("PKTABLE_NAME"));
				tableKey.set("TABLEKEY_LINECOLUMNCODE",rs.getString("PKCOLUMN_NAME"));
				tableKey.set("TABLEKEY_COLUMNCODE",rs.getString("FKCOLUMN_NAME"));
				tableKey.set("TABLEKEY_TYPE","Foreign");
				tableKey.set("TABLEKEY_CODE",rs.getString("FK_NAME"));
				if(StringUtil.isNotEmpty(resourceTable.getStr("RESOURCETABLE_CHILDTABLECODES"))){
					resourceTable.set("RESOURCETABLE_CHILDTABLECODES",resourceTable.getStr("RESOURCETABLE_CHILDTABLECODES")+","+rs.getString("PKTABLE_NAME"));
				}else{
					resourceTable.set("RESOURCETABLE_CHILDTABLECODES",rs.getString("PKTABLE_NAME"));
				}
				resourceTable.set("RESOURCETABLE_ISUSEFOREIGNKEY","1");
				keys.add(tableKey);
			}
			List<DynaBean> columns = null;
			DynaBean column = null;
			Map<String,String> columnsComments = null;

			try {
				//3.定义和装载列对象
				columns = new ArrayList<DynaBean>();
				column = null;
				ps = connection.prepareStatement("select * from USER_COL_COMMENTS t where  t.table_name= '"+tableCode+"'");
				rs = ps.executeQuery();
				columnsComments = new HashMap<String,String>();
				while (rs.next()) {
					String  comments  = (String)rs.getObject("COMMENTS");
					String  columnCode  = (String)rs.getObject("column_name");
					columnsComments.put(columnCode, comments);
				}
			} catch (SQLException e) {
				throw new PlatformException("获取资源表【"+tableCode+"】结构键信息异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
			} finally {
				JdbcUtil.close(rs, ps);
			}

			try {
				ps = connection.prepareStatement("select * from "+tableCode);
				rs = ps.executeQuery();
				ResultSetMetaData rsme = rs.getMetaData();
				int columnCount = rsme.getColumnCount();
				for (int i = 1; i <= columnCount ; i++) {
					column = new DynaBean("JE_CORE_TABLECOLUMN",false);
					column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
					column.set("TABLECOLUMN_CODE",rsme.getColumnName(i).toUpperCase());
					column.set("TABLECOLUMN_OLDCODE",rsme.getColumnName(i).toUpperCase());
					column.set("TABLECOLUMN_NAME",columnsComments.get(rsme.getColumnName(i)));
					column.set("TABLECOLUMN_ISCREATE","1");
					column.set("TABLECOLUMN_UNIQUE","0");
					column.set("TABLECOLUMN_TREETYPE","NORMAL");
					column.set("TABLECOLUMN_TABLECODE",tableCode);
					column.set("TABLECOLUMN_CLASSIFY","PRO");
					column.set("SY_STATUS","1");
					column.set("SY_ORDERINDEX",i);
					String type =  rsme.getColumnTypeName(i);
					int len=rsme.getPrecision(i);
					int scale=rsme.getScale(i);
					String length=rsme.getPrecision(i)+"";
					if("-1".equals(length)){
						column.set("TABLECOLUMN_LENGTH","");
					}else{
						column.set("TABLECOLUMN_LENGTH",length);
					}
					if(type.startsWith("VARCHAR")){
						column.set("TABLECOLUMN_TYPE","VARCHAR");
						if("255".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR255");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("100".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR100");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("50".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR50");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("30".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR30");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("1000".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR1000");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("4000".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR4000");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("4".equals(length)){
							column.set("TABLECOLUMN_TYPE","YESORNO");
							column.set("TABLECOLUMN_LENGTH","");
						}else if(length.length()>=4){
							column.set("TABLECOLUMN_TYPE","CLOB");
							column.set("TABLECOLUMN_LENGTH","");
						}else if(len>0){
							column.set("TABLECOLUMN_TYPE",ColumnType.VARCHAR);
							column.set("TABLECOLUMN_LENGTH",length+"");
						}else{
							column.set("TABLECOLUMN_TYPE","VARCHAR255");
							column.set("TABLECOLUMN_LENGTH","");
						}
					}else if(type.startsWith("INT")){
						column.set("TABLECOLUMN_TYPE","NUMBER");
					}else if(type.startsWith("FLOAT") || type.startsWith("NUMERIC") || type.startsWith("DECIMAL") || type.startsWith("NUMBER")){
						if(scale<=0){
							column.set("TABLECOLUMN_TYPE","NUMBER");
							if(len==20){
								column.set("TABLECOLUMN_LENGTH","");
							}else{
								column.set("TABLECOLUMN_LENGTH",length);
							}
						}else{
							column.set("TABLECOLUMN_TYPE",ColumnType.FLOAT);
							if(len==20){
								length=scale+"";
							}else{
								length=len+","+scale;
							}
							column.set("TABLECOLUMN_LENGTH",length);
	//						model.setLength(typeLength);
						}
					}else if(type.startsWith("TEXT") || type.startsWith("LONGTEXT") || type.startsWith("CLOB")){
						column.set("TABLECOLUMN_TYPE","CLOB");
						column.set("TABLECOLUMN_LENGTH","");
					}else{
						column.set("TABLECOLUMN_TYPE",ColumnType.CUSTOM);
						column.set("TABLECOLUMN_LENGTH",type);
					}
					column.set("TABLECOLUMN_ISNULL",rsme.isNullable(i)+"");
					//放入集合
					columns.add(column);
				}
			} catch (SQLException e) {
				throw new PlatformException("获取资源表【"+tableCode+"】结构列信息异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
			} finally {
				JdbcUtil.close(rs, ps);
			}
//            //5.加载表的索引信息
//            rs = metaData.getIndexInfo(null, metaData.getUserName(), tableCode, false, false);
//            while (rs.next()) {
//            	String columnName=rs.getString("COLUMN_NAME");
//            	if(StringUtil.isNotEmpty(columnName)){
//
//            	}
//
//            	String[] arrys=new String[]{"TABLE_CAT",
//            	"TABLE_SCHEM",
//            	"TABLE_NAME",
//            	"NON_UNIQUE",
//            	"INDEX_QUALIFIER",
//            	"INDEX_NAME",
//            	"TYPE",
//            	"ORDINAL_POSITION",
//            	"COLUMN_NAME",
//            	"ASC_OR_DESC",
//            	"CARDINALITY",
//            	"PAGES",
//            	"FILTER_CONDITION"};
//            	for(String cn:arrys){
//            		System.out.println(cn+"    "+rs.getString(cn));
//            	}
//            }
			//装载
			resourceTable.set(BeanUtils.KEY_TABLE_KEYS,keys);
			resourceTable.set(BeanUtils.KEY_TABLE_COLUMNS,columns);
			resourceTable.set(BeanUtils.KEY_TABLE_INDEXS, indexs);
		} catch (Exception e) {
			throw new PlatformException("获取资源表结构【"+tableCode+"】异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
		}finally{
				JdbcUtil.close(rs, ps, connection);
		}
		return resourceTable;
	}
	/**
	 * 目前单单适合月oracle ,其他数据库可一直接用DatabaseMetaData得到
	 * 返回视图的基本系信息
	 * 张帅鹏
	 * @return
	 */
	public static DynaBean getViewBaseInfo(DynaBean table,Connection connection){
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		PreparedStatement ps = null;
		ResultSet rs = null;
		//1.定义表对象
		DynaBean resourceTable = table;
		try {
			try {
				ps = connection.prepareStatement("select * from USER_TAB_COMMENTS t where  t.table_name= '"+tableCode+"'");
				rs = ps.executeQuery();
				//装载TableCode
				resourceTable.set("RESOURCETABLE_TABLECODE",tableCode);
				resourceTable.set("RESOURCETABLE_OLDTABLECODE",tableCode);
				resourceTable.set("RESOURCETABLE_TYPE",TableType.VIEWTABLE);
				while (rs.next()) {
					String  comments  = (String)rs.getObject("COMMENTS");
					//装载表注释
					resourceTable.set("RESOURCETABLE_TABLENAME",comments);
				}
				//装载他是已经被创建的
				resourceTable.set("RESOURCETABLE_ISCREATE","1");
			} catch (SQLException e) {
				throw new PlatformException("获得视图【"+tableCode+"】结构异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
			} finally {
				JdbcUtil.close(rs, ps);
			}
			//3.定义和装载列对象
			List<DynaBean> columns = new ArrayList<DynaBean>();
			DynaBean column = null;
			Map<String,String> columnsComments = null;
			try {
				ps = connection.prepareStatement("select * from USER_COL_COMMENTS t where  t.table_name= '"+tableCode+"'");
				rs = ps.executeQuery();
				columnsComments = new HashMap<String,String>();
				while (rs.next()) {
					String  comments  = (String)rs.getObject("COMMENTS");
					String  columnCode  = (String)rs.getObject("column_name");
					columnsComments.put(columnCode, comments);
				}
			} catch (SQLException e) {
				throw new PlatformException("获得视图【"+tableCode+"】结构字段信息异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
			} finally {
				JdbcUtil.close(rs, ps);
			}

			try {
				ps = connection.prepareStatement("select * from "+tableCode);
				rs = ps.executeQuery();
				ResultSetMetaData rsme = rs.getMetaData();
				int columnCount = rsme.getColumnCount();
				for (int i = 1; i <= columnCount ; i++) {
					column = new DynaBean("JE_CORE_TABLECOLUMN",false);
					column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
					column.set("TABLECOLUMN_CODE",rsme.getColumnName(i).toUpperCase());
					column.set("TABLECOLUMN_NAME",columnsComments.get(rsme.getColumnName(i)));
					column.set("TABLECOLUMN_ISCREATE","1");
					String type =  rsme.getColumnTypeName(i).toUpperCase();
					String length=rsme.getPrecision(i)+"";
					int len=rsme.getPrecision(i);
					int scale=rsme.getScale(i);
					column.set("TABLECOLUMN_LENGTH",length);
					if(type.startsWith("VARCHAR")){
						column.set("TABLECOLUMN_TYPE","VARCHAR");
						if("255".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR255");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("100".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR100");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("50".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR50");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("30".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR30");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("1000".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR1000");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("4000".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR4000");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("4".equals(length)){
							column.set("TABLECOLUMN_TYPE",ColumnType.YESORNO);
							column.set("TABLECOLUMN_LENGTH","");
						}else if(length.length()>4){
							column.set("TABLECOLUMN_TYPE","CLOB");
							column.set("TABLECOLUMN_LENGTH","");
						}else if(len>0){
							column.set("TABLECOLUMN_TYPE",ColumnType.VARCHAR);
							column.set("TABLECOLUMN_LENGTH",length+"");
						}else{
							column.set("TABLECOLUMN_TYPE","VARCHAR255");
							column.set("TABLECOLUMN_LENGTH","");
						}
					}else if(type.startsWith("INT") || type.startsWith("BIGINT") ){
						column.set("TABLECOLUMN_TYPE","NUMBER");
					}else if(type.startsWith("FLOAT") || type.startsWith("NUMERIC") || type.startsWith("DECIMAL") || type.startsWith("NUMBER")){
						if(scale<=0){
							column.set("TABLECOLUMN_TYPE","NUMBER");
							if(len==20){
								column.set("TABLECOLUMN_LENGTH","");
							}else{
								column.set("TABLECOLUMN_LENGTH",length);
							}
						}else{
							column.set("TABLECOLUMN_TYPE",ColumnType.FLOAT);
							if(len==20){
								length=scale+"";
							}else{
								length=len+","+scale;
							}
							column.set("TABLECOLUMN_LENGTH",length);
	//						model.setLength(typeLength);
						}
					}else if(type.startsWith("TEXT") || type.startsWith("LONGTEXT") || type.startsWith("CLOB")){
						column.set("TABLECOLUMN_TYPE","CLOB");
						column.set("TABLECOLUMN_LENGTH","");
					}else{
						column.set("TABLECOLUMN_TYPE",ColumnType.CUSTOM);
						column.set("TABLECOLUMN_LENGTH",type);
					}
					column.set("TABLECOLUMN_ISNULL",rsme.isNullable(i)+"");
					//放入集合
					columns.add(column);
				}
			} catch (SQLException e) {
				throw new PlatformException("获得视图【"+tableCode+"】结构字段信息异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
			} finally {
				JdbcUtil.close(rs, ps);
			}
			//装载
			resourceTable.set(BeanUtils.KEY_TABLE_COLUMNS,columns);
		} catch (Exception e) {
			throw new PlatformException("获得视图【"+tableCode+"】结构异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
		}finally{
			JdbcUtil.close(rs, ps, connection);
		}
		return resourceTable;
	}
	/**
	 * 适合SQLServer数据库
	 * 返回视图的基本系信息
	 * 张帅鹏
	 * @return
	 */
	public static DynaBean getViewBaseInfoBySQLServer(DynaBean table,Connection connection){
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		PreparedStatement ps = null;
		ResultSet rs = null;
		//1.定义表对象
		DynaBean resourceTable = table;
		try {
//			ps = connection.prepareStatement("select * from sysobjects t where  t.name= '"+tableCode+"' and t.xtype='V'");
//			rs = ps.executeQuery();
			//装载TableCode
			resourceTable.set("RESOURCETABLE_TABLECODE",tableCode);
			resourceTable.set("RESOURCETABLE_OLDTABLECODE",tableCode);
			resourceTable.set("RESOURCETABLE_TYPE",TableType.VIEWTABLE);
			//装载他是已经被创建的
			resourceTable.set("RESOURCETABLE_ISCREATE","1");
			//3.定义和装载列对象
			List<DynaBean> columns = new ArrayList<DynaBean>();
			DynaBean column = null;
			ps = connection.prepareStatement("select * from "+tableCode);
			rs = ps.executeQuery();
			ResultSetMetaData rsme = rs.getMetaData();
			int columnCount = rsme.getColumnCount();
			for (int i = 1; i <= columnCount ; i++) {
				column = new DynaBean("JE_CORE_TABLECOLUMN",false);
				column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
				column.set("TABLECOLUMN_CODE",rsme.getColumnName(i).toUpperCase());
				column.set("TABLECOLUMN_ISCREATE","1");
				String type =  rsme.getColumnTypeName(i).toUpperCase();
				String length=rsme.getPrecision(i)+"";
				int len=rsme.getPrecision(i);
				int scale=rsme.getScale(i);
				column.set("TABLECOLUMN_LENGTH",length);
				if(type.startsWith("VARCHAR")){
					column.set("TABLECOLUMN_TYPE","VARCHAR");
					if("255".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR255");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("100".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR100");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("50".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR50");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("30".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR30");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("1000".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR1000");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("4000".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR4000");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("4".equals(length)){
						column.set("TABLECOLUMN_TYPE",ColumnType.YESORNO);
						column.set("TABLECOLUMN_LENGTH","");
					}else if(length.length()>4){
						column.set("TABLECOLUMN_TYPE","CLOB");
						column.set("TABLECOLUMN_LENGTH","");
					}else if(len>0){
						column.set("TABLECOLUMN_TYPE",ColumnType.VARCHAR);
						column.set("TABLECOLUMN_LENGTH",length+"");
					}else{
						column.set("TABLECOLUMN_TYPE","VARCHAR255");
						column.set("TABLECOLUMN_LENGTH","");
					}
				}else if(type.startsWith("INT") || type.startsWith("BIGINT") ){
					column.set("TABLECOLUMN_TYPE","NUMBER");
				}else if(type.startsWith("FLOAT") || type.startsWith("NUMERIC") || type.startsWith("DECIMAL") || type.startsWith("NUMBER")){
					if(scale<=0){
						column.set("TABLECOLUMN_TYPE","NUMBER");
						if(len==20){
							column.set("TABLECOLUMN_LENGTH","");
						}else{
							column.set("TABLECOLUMN_LENGTH",length);
						}
					}else{
						column.set("TABLECOLUMN_TYPE",ColumnType.FLOAT);
						if(len==20){
							length=scale+"";
						}else{
							length=len+","+scale;
						}
						column.set("TABLECOLUMN_LENGTH",length);
//						model.setLength(typeLength);
					}
				}else if(type.startsWith("TEXT") || type.startsWith("LONGTEXT") || type.startsWith("CLOB")){
					column.set("TABLECOLUMN_TYPE","CLOB");
					column.set("TABLECOLUMN_LENGTH","");
				}else{
					column.set("TABLECOLUMN_TYPE",ColumnType.CUSTOM);
					column.set("TABLECOLUMN_LENGTH",type);
				}
				column.set("TABLECOLUMN_ISNULL",rsme.isNullable(i)+"");
				//放入集合
				columns.add(column);
			}
			//装载
			resourceTable.set(BeanUtils.KEY_TABLE_COLUMNS,columns);
		} catch (Exception e) {
			throw new PlatformException("获得资源表【"+tableCode+"】结构字段信息异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
		}finally{
			JdbcUtil.close(rs, ps, connection);
		}
		return resourceTable;
	}
	/**
	 * 适合SQLServer库 返回表的基本系信息
	 * 张帅鹏
	 * @return
	 */
	public static DynaBean getTableBaseInfoBySQLServer(DynaBean table,Connection connection){
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		PreparedStatement ps = null;
		ResultSet rs = null;
		//1.定义表对象
		DynaBean resourceTable = table;
		try {
			List<DynaBean> keys = null;
			List<DynaBean> indexs = null;
			try {
//				ps = connection.prepareStatement("select * from sysobjects t where  t.name= '"+tableCode+"' and t.xtype='U'");
//				rs = ps.executeQuery();
				//装载TableCode
				resourceTable.set("RESOURCETABLE_TABLECODE",tableCode);
				resourceTable.set("RESOURCETABLE_OLDTABLECODE",tableCode);
				resourceTable.set("RESOURCETABLE_TYPE",TableType.PTTABLE);
				//装载他是已经被创建的
				resourceTable.set("RESOURCETABLE_ISCREATE","1");
				//2.定义和装载键对象
				keys = new ArrayList<DynaBean>();
				indexs = new ArrayList<DynaBean>();
				DynaBean tableKey = null;
				DynaBean tableIndex=null;
				String pkName="";
				DatabaseMetaData metaData = connection.getMetaData();
				//主键
				rs = metaData.getPrimaryKeys(null, metaData.getUserName(), tableCode);
				while (rs.next()) {
					tableKey = new DynaBean("JE_CORE_TABLEKEY",false);
					tableKey.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEKEY_ID");
					tableKey.set("TABLEKEY_COLUMNCODE",rs.getString("COLUMN_NAME"));
					tableKey.set("TABLEKEY_TYPE","Primary");
					tableKey.set("TABLEKEY_ISCREATE","1");
					tableKey.set("TABLEKEY_CODE",rs.getString("PK_NAME"));
					keys.add(tableKey);
					pkName=rs.getString("COLUMN_NAME");
					tableIndex=new DynaBean("JE_CORE_TABLEINDEX",false);
					tableIndex.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEINDEX_ID");
					tableIndex.set("TABLEINDEX_NAME","JE_"+DateUtils.getUniqueTime()+"_ID");
					tableIndex.set("TABLEINDEX_FIELDCODE",pkName);
					tableIndex.set("TABLEINDEX_FIELDNAME","主键ID");
					tableIndex.set("TABLEINDEX_ISCREATE","1");
					tableIndex.set("TABLEINDEX_UNIQUE","1");
					tableIndex.set("TABLEINDEX_CLASSIFY","SYS");
					tableIndex.set("SY_ORDERINDEX",0);
					indexs.add(tableIndex);
				}
				//外键
				rs = metaData.getImportedKeys(null, metaData.getUserName(), tableCode);
				//默认给成否
				resourceTable.set("RESOURCETABLE_ISUSEFOREIGNKEY","0");
				resourceTable.set("RESOURCETABLE_PKCODE", pkName);
				while (rs.next()) {
					/**
					 PKTABLE_CAT String => 被导进的主键表种别（可为 null）
					 PKTABLE_SCHEM String => 被导进的主键表模式（可为 null）
					 PKTABLE_NAME String => 被导进的主键表名称
					 PKCOLUMN_NAME String => 被导进的主键列名称
					 FKTABLE_CAT String => 外键表种别（可为 null）
					 FKTABLE_SCHEM String => 外键表模式（可为 null）
					 FKTABLE_NAME String => 外键表名称
					 FKCOLUMN_NAME String => 外键列名称
					 KEY_SEQ short => 外键中的序列号（值 1 表示外键中的第一列，值 2 表示外键中的第二列）
					 UPDATE_RULE short => 更新主键时外键发生的变化
					 DELETE_RULE short => 删除主键时外键发生的变化
					 PK_NAME String => 主键的名称（可为 null）
					 FK_NAME String => 外键的名称（可为 null）
					 DEFERRABILITY short => 是否可以将对外键约束的评估延迟到提交时间
					 */
					tableKey = new DynaBean("JE_CORE_TABLEKEY",false);
					tableKey.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEKEY_ID");
					String linkType = rs.getString("DELETE_RULE");
					if("0".equals(linkType)){
						tableKey.set("TABLEKEY_LINETYLE","Cascade");
					}else if("1".equals(linkType)){
						tableKey.set("TABLEKEY_LINETYLE","Noaction");
					}else if("2".equals(linkType)){
						tableKey.set("TABLEKEY_LINETYLE","Setnull");
					}
					tableKey.set("TABLEKEY_LINKTABLE",rs.getString("PKTABLE_NAME"));
					tableKey.set("TABLEKEY_LINECOLUMNCODE",rs.getString("PKCOLUMN_NAME"));
					tableKey.set("TABLEKEY_COLUMNCODE",rs.getString("FKCOLUMN_NAME"));
					tableKey.set("TABLEKEY_TYPE","Foreign");
					tableKey.set("TABLEKEY_CODE",rs.getString("FK_NAME"));
					if(StringUtil.isNotEmpty(resourceTable.getStr("RESOURCETABLE_CHILDTABLECODES"))){
						resourceTable.set("RESOURCETABLE_CHILDTABLECODES",resourceTable.getStr("RESOURCETABLE_CHILDTABLECODES")+","+rs.getString("PKTABLE_NAME"));
					}else{
						resourceTable.set("RESOURCETABLE_CHILDTABLECODES",rs.getString("PKTABLE_NAME"));
					}
					resourceTable.set("RESOURCETABLE_ISUSEFOREIGNKEY","1");
					keys.add(tableKey);
				}
			} catch (SQLException e) {
				throw new PlatformException("获得资源表【"+tableCode+"】表信息结构异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
			} finally {
				JdbcUtil.close(rs, ps);
			}

			//3.定义和装载列对象
			List<DynaBean> columns = new ArrayList<DynaBean>();
			DynaBean column = null;
			ps = connection.prepareStatement("select * from "+tableCode);
			rs = ps.executeQuery();
			ResultSetMetaData rsme = rs.getMetaData();
			int columnCount = rsme.getColumnCount();
			for (int i = 1; i <= columnCount ; i++) {
				column = new DynaBean("JE_CORE_TABLECOLUMN",false);
				column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
				column.set("TABLECOLUMN_CODE",rsme.getColumnName(i).toUpperCase());
				column.set("TABLECOLUMN_ISCREATE","1");
				column.set("TABLECOLUMN_UNIQUE","0");
				column.set("TABLECOLUMN_TREETYPE","NORMAL");
				column.set("TABLECOLUMN_TABLECODE",tableCode);
				column.set("TABLECOLUMN_CLASSIFY","PRO");
				column.set("SY_STATUS","1");
				column.set("SY_ORDERINDEX",i);
				String type =  rsme.getColumnTypeName(i).toUpperCase();
				String length=rsme.getPrecision(i)+"";
				int len=rsme.getPrecision(i);
				int scale=rsme.getScale(i);
				column.set("TABLECOLUMN_LENGTH",length);
				if(type.startsWith("VARCHAR")){
					column.set("TABLECOLUMN_TYPE","VARCHAR");
					if("255".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR255");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("100".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR100");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("50".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR50");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("30".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR30");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("1000".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR1000");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("4000".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR4000");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("4".equals(length)){
						column.set("TABLECOLUMN_TYPE",ColumnType.YESORNO);
						column.set("TABLECOLUMN_LENGTH","");
					}else if(length.length()>4){
						column.set("TABLECOLUMN_TYPE","CLOB");
						column.set("TABLECOLUMN_LENGTH","");
					}else if(len>0){
						column.set("TABLECOLUMN_TYPE",ColumnType.VARCHAR);
						column.set("TABLECOLUMN_LENGTH",length+"");
					}else{
						column.set("TABLECOLUMN_TYPE","VARCHAR255");
						column.set("TABLECOLUMN_LENGTH","");
					}
				}else if(type.startsWith("INT")){
					column.set("TABLECOLUMN_TYPE","NUMBER");
				}else if(type.startsWith("FLOAT") || type.startsWith("NUMERIC") || type.startsWith("DECIMAL") || type.startsWith("NUMBER")){
					if(scale<=0){
						column.set("TABLECOLUMN_TYPE","NUMBER");
						if(len==20){
							column.set("TABLECOLUMN_LENGTH","");
						}else{
							column.set("TABLECOLUMN_LENGTH",length);
						}
					}else{
						column.set("TABLECOLUMN_TYPE",ColumnType.FLOAT);
						if(len==20){
							length=scale+"";
						}else{
							length=len+","+scale;
						}
						column.set("TABLECOLUMN_LENGTH",length);
//						model.setLength(typeLength);
					}
				}else if(type.startsWith("TEXT") || type.startsWith("LONGTEXT") || type.startsWith("CLOB")){
					column.set("TABLECOLUMN_TYPE","CLOB");
					column.set("TABLECOLUMN_LENGTH","");
				}else{
					column.set("TABLECOLUMN_TYPE",ColumnType.CUSTOM);
					column.set("TABLECOLUMN_LENGTH",type);
				}
				column.set("TABLECOLUMN_ISNULL",rsme.isNullable(i)+"");
				//放入集合
				columns.add(column);
			}
			//装载
			resourceTable.set(BeanUtils.KEY_TABLE_KEYS,keys);
			resourceTable.set(BeanUtils.KEY_TABLE_COLUMNS,columns);
			resourceTable.set(BeanUtils.KEY_TABLE_INDEXS, indexs);
		} catch (Exception e) {
			throw new PlatformException("获得资源表【"+tableCode+"】表信息结构异常",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
		}finally{
			JdbcUtil.close(rs, ps, connection);
		}
		return resourceTable;
	}
	/**
	 * 得到数据是有表名字的集合
	 * @return
	 */
	public static List<String> getDataBaseTableNames(){
		Connection connection  = null;
		JdbcUtil jdbcUtil=JdbcUtil.getInstance();
		try {
			connection = jdbcUtil.getConnection();
		} catch (Exception e) {
			throw new PlatformException("获取数据链接失败!",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,e);
		}
		List<String> tableNames = new ArrayList<String>();
		ResultSet rs = null;
		try {
			DatabaseMetaData metaData = connection.getMetaData();
			if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME)){
				rs=metaData.getTables(null,metaData.getUserName(), null, new String[]{"TABLE"});
			}else{
				rs=metaData.getTables(null, null, null, new String[]{"TABLE"});
			}
			while(rs.next()) {
				tableNames.add(rs.getString("TABLE_NAME").toUpperCase());
			}
		} catch (SQLException e) {
			throw new PlatformException("获取数据库当前所有表名异常!",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,e);
		}finally{
			JdbcUtil.close(rs, null, connection);
		}
		return tableNames;
	}
	/**
	 * 目前单单适合月oracle ,其他数据库可一直接用DatabaseMetaData得到
	 * 返回表的基本系信息
	 * 云哥
	 * @return
	 */
	public static DynaBean getTableBaseInfoByMySQL(DynaBean table,Connection connection){
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		PreparedStatement ps = null;
		ResultSet rs = null;
		//1.定义表对象
		DynaBean resourceTable = table;
		try {
			//装载TableCode
			resourceTable.set("RESOURCETABLE_TABLECODE",tableCode);
			resourceTable.set("RESOURCETABLE_OLDTABLECODE",tableCode);
			resourceTable.set("RESOURCETABLE_TYPE",TableType.PTTABLE);
			//装载他是已经被创建的
			resourceTable.set("RESOURCETABLE_ISCREATE","1");
			//2.定义和装载键对象
			List<DynaBean> keys = new ArrayList<DynaBean>();
			List<DynaBean> indexs = new ArrayList<DynaBean>();
			DynaBean tableKey = null;
			DynaBean tableIndex=null;
			String pkName="";
			DatabaseMetaData metaData = connection.getMetaData();
			//主键
			rs = metaData.getPrimaryKeys(null, metaData.getUserName(), tableCode);
			while (rs.next()) {
				tableKey = new DynaBean("JE_CORE_TABLEKEY",false);
				tableKey.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEKEY_ID");
				tableKey.set("TABLEKEY_COLUMNCODE",rs.getString("COLUMN_NAME"));
				tableKey.set("TABLEKEY_TYPE","Primary");
				tableKey.set("TABLEKEY_ISCREATE","1");
				tableKey.set("TABLEKEY_CODE","JE_"+DateUtils.getUniqueTime()+"_PK");
				keys.add(tableKey);
				pkName=rs.getString("COLUMN_NAME");
				tableIndex=new DynaBean("JE_CORE_TABLEINDEX",false);
				tableIndex.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEINDEX_ID");
				tableIndex.set("TABLEINDEX_NAME","JE_"+DateUtils.getUniqueTime()+"_ID");
				tableIndex.set("TABLEINDEX_FIELDCODE",pkName);
				tableIndex.set("TABLEINDEX_FIELDNAME","主键ID");
				tableIndex.set("TABLEINDEX_ISCREATE","1");
				tableIndex.set("TABLEINDEX_UNIQUE","1");
				tableIndex.set("TABLEINDEX_CLASSIFY","SYS");
				tableIndex.set("SY_ORDERINDEX",0);
				indexs.add(tableIndex);
			}
			//外键
			rs = metaData.getImportedKeys(null, metaData.getUserName(), tableCode);
			//默认给成否
			resourceTable.set("RESOURCETABLE_ISUSEFOREIGNKEY","0");
			resourceTable.set("RESOURCETABLE_PKCODE", pkName);
			while (rs.next()) {
				/**
				 PKTABLE_CAT String => 被导进的主键表种别（可为 null）
				 PKTABLE_SCHEM String => 被导进的主键表模式（可为 null）
				 PKTABLE_NAME String => 被导进的主键表名称
				 PKCOLUMN_NAME String => 被导进的主键列名称
				 FKTABLE_CAT String => 外键表种别（可为 null）
				 FKTABLE_SCHEM String => 外键表模式（可为 null）
				 FKTABLE_NAME String => 外键表名称
				 FKCOLUMN_NAME String => 外键列名称
				 KEY_SEQ short => 外键中的序列号（值 1 表示外键中的第一列，值 2 表示外键中的第二列）
				 UPDATE_RULE short => 更新主键时外键发生的变化
				 DELETE_RULE short => 删除主键时外键发生的变化
				 PK_NAME String => 主键的名称（可为 null）
				 FK_NAME String => 外键的名称（可为 null）
				 DEFERRABILITY short => 是否可以将对外键约束的评估延迟到提交时间
				 */
				tableKey = new DynaBean("JE_CORE_TABLEKEY",false);
				tableKey.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEKEY_ID");
				String linkType = rs.getString("DELETE_RULE");
				if("0".equals(linkType)){
					tableKey.set("TABLEKEY_LINETYLE","Cascade");
				}else if("1".equals(linkType)){
					tableKey.set("TABLEKEY_LineTyle","Noaction");
				}else if("2".equals(linkType)){
					tableKey.set("TABLEKEY_LineTyle","Setnull");
				}
				tableKey.set("TABLEKEY_LINKTABLE",rs.getString("PKTABLE_NAME").toUpperCase().toString());
				tableKey.set("TABLEKEY_LINECOLUMNCODE",rs.getString("PKCOLUMN_NAME"));
				tableKey.set("TABLEKEY_COLUMNCODE",rs.getString("FKCOLUMN_NAME"));
				tableKey.set("TABLEKEY_TYPE","Foreign");
				tableKey.set("TABLEKEY_CODE",rs.getString("FK_NAME"));
				tableKey.set("TABLEKEY_ISCREATE","1");
				if(StringUtil.isNotEmpty(resourceTable.getStr("RESOURCETABLE_CHILDTABLECODES"))){
					resourceTable.set("RESOURCETABLE_CHILDTABLECODES",resourceTable.getStr("RESOURCETABLE_CHILDTABLECODES")+","+rs.getString("FKCOLUMN_NAME"));
				}else{
					resourceTable.set("RESOURCETABLE_CHILDTABLECODES",rs.getString("FKCOLUMN_NAME"));
				}
				resourceTable.set("RESOURCETABLE_ISUSEFOREIGNKEY","1");
				keys.add(tableKey);
			}
			//3.定义和装载列对象
			List<DynaBean> columns = new ArrayList<DynaBean>();
			DynaBean column = null;

			Map<String,String> columnsComments = null;
			try {
				ps = connection.prepareStatement("show full columns from "+tableCode+"");
				rs = ps.executeQuery();
				columnsComments = new HashMap<String,String>();
				while (rs.next()) {
					String  comments  = (String)rs.getObject("Comment");
					String  columnCode  = (String)rs.getObject("Field");
					columnsComments.put(columnCode, comments);
				}
			} catch (SQLException e) {
				throw new PlatformException("获取资源表【"+tableCode+"】表信息结构异常!",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
			} finally {
				JdbcUtil.close(rs, ps);
			}

			try {
				ps = connection.prepareStatement("select * from "+tableCode);
				rs = ps.executeQuery();
				ResultSetMetaData rsme = rs.getMetaData();
				int columnCount = rsme.getColumnCount();
				for (int i = 1; i <= columnCount ; i++) {
					column = new DynaBean("JE_CORE_TABLECOLUMN",false);
					column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
					column.set("TABLECOLUMN_CODE",rsme.getColumnName(i).toUpperCase());
					column.set("TABLECOLUMN_NAME",columnsComments.get(rsme.getColumnName(i)));
					column.set("TABLECOLUMN_ISCREATE","1");
					column.set("TABLECOLUMN_UNIQUE","0");
					column.set("TABLECOLUMN_TREETYPE","NORMAL");
					column.set("TABLECOLUMN_TABLECODE",tableCode);
					column.set("TABLECOLUMN_CLASSIFY","PRO");
					column.set("SY_STATUS","1");
					column.set("SY_ORDERINDEX",i);
					String type =  rsme.getColumnTypeName(i).toUpperCase();
					String length=rsme.getPrecision(i)+"";
					int len=rsme.getPrecision(i);
					int scale=rsme.getScale(i);
					column.set("TABLECOLUMN_LENGTH",length);
					if(type.startsWith("VARCHAR")){
						column.set("TABLECOLUMN_TYPE","VARCHAR");
						if("255".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR255");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("100".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR100");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("50".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR50");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("30".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR30");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("1000".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR1000");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("4000".equals(length)){
							column.set("TABLECOLUMN_TYPE","VARCHAR4000");
							column.set("TABLECOLUMN_LENGTH","");
						}else if("4".equals(length)){
							column.set("TABLECOLUMN_TYPE",ColumnType.YESORNO);
							column.set("TABLECOLUMN_LENGTH","");
						}else if(length.length()>4){
							column.set("TABLECOLUMN_TYPE","CLOB");
							column.set("TABLECOLUMN_LENGTH","");
						}else if(len>0){
							column.set("TABLECOLUMN_TYPE",ColumnType.VARCHAR);
							column.set("TABLECOLUMN_LENGTH",length+"");
						}else{
							column.set("TABLECOLUMN_TYPE","VARCHAR255");
							column.set("TABLECOLUMN_LENGTH","");
						}
					}else if(type.startsWith("INT")){
						column.set("TABLECOLUMN_TYPE","NUMBER");
					}else if(type.startsWith("FLOAT") || type.startsWith("NUMERIC") || type.startsWith("DECIMAL") || type.startsWith("NUMBER")){
						if(scale<=0){
							column.set("TABLECOLUMN_TYPE","NUMBER");
							if(len==20){
								column.set("TABLECOLUMN_LENGTH","");
							}else{
								column.set("TABLECOLUMN_LENGTH",length);
							}
						}else{
							column.set("TABLECOLUMN_TYPE",ColumnType.FLOAT);
							if(len==20){
								length=scale+"";
							}else{
								length=len+","+scale;
							}
							column.set("TABLECOLUMN_LENGTH",length);
	//						model.setLength(typeLength);
						}
					}else if(type.startsWith("TEXT") || type.startsWith("LONGTEXT") || type.startsWith("CLOB")){
						column.set("TABLECOLUMN_TYPE","CLOB");
						column.set("TABLECOLUMN_LENGTH","");
					}else{
						column.set("TABLECOLUMN_TYPE",ColumnType.CUSTOM);
						column.set("TABLECOLUMN_LENGTH",type);
					}
					column.set("TABLECOLUMN_ISNULL",rsme.isNullable(i)+"");
					//放入集合
					columns.add(column);
				}
			} catch (SQLException e) {
				throw new PlatformException("获取资源表【"+tableCode+"】表信息结构异常!",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);

			} finally {
				JdbcUtil.close(rs, ps);
			}
			//装载
			resourceTable.set(BeanUtils.KEY_TABLE_KEYS,keys);
			resourceTable.set(BeanUtils.KEY_TABLE_COLUMNS,columns);
			resourceTable.set(BeanUtils.KEY_TABLE_INDEXS, indexs);
		} catch (Exception e) {
			throw new PlatformException("获取资源表【"+tableCode+"】表信息结构异常!",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
		}finally{
			JdbcUtil.close(rs, ps, connection);
		}
		return resourceTable;
	}

	/**
	 * 目前单单适合月oracle ,其他数据库可一直接用DatabaseMetaData得到
	 * 返回视图的基本系信息
	 * 张帅鹏
	 * @return
	 */
	public static DynaBean getViewBaseInfoByMySQL(DynaBean table,Connection connection){
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		PreparedStatement ps = null;
		ResultSet rs = null;
		//1.定义表对象
		DynaBean resourceTable = table;
		try {
			//装载TableCode
			resourceTable.set("RESOURCETABLE_TABLECODE",tableCode);
			resourceTable.set("RESOURCETABLE_TYPE",TableType.VIEWTABLE);
			//装载他是已经被创建的
			resourceTable.set("RESOURCETABLE_ISCREATE","1");
			//3.定义和装载列对象
			List<DynaBean> columns = new ArrayList<DynaBean>();
			DynaBean column = null;
			Map<String,String> columnsComments = null;
			try {
				ps = connection.prepareStatement("show full columns from "+tableCode+";");
				rs = ps.executeQuery();
				columnsComments = new HashMap<String,String>();
				while (rs.next()) {
					String  comments  = (String)rs.getObject("Comment");
					String  columnCode  = (String)rs.getObject("Field");
					columnsComments.put(columnCode, comments);
				}
			} catch (SQLException e) {
				throw new PlatformException("获取资源表【"+tableCode+"】表信息结构异常!",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
			} finally {
				JdbcUtil.close(rs, ps);
			}

			ps = connection.prepareStatement("select * from "+tableCode);
			rs = ps.executeQuery();
			ResultSetMetaData rsme = rs.getMetaData();
			int columnCount = rsme.getColumnCount();
			for (int i = 1; i <= columnCount ; i++) {
				column = new DynaBean("JE_CORE_TABLECOLUMN",false);
				column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
				column.set("TABLECOLUMN_CODE",rsme.getColumnName(i).toUpperCase());
				column.set("TABLECOLUMN_NAME",columnsComments.get(rsme.getColumnName(i)));
				column.set("TABLECOLUMN_ISCREATE","1");
				String type =  rsme.getColumnTypeName(i).toUpperCase();
				String length=rsme.getPrecision(i)+"";
				int len=rsme.getPrecision(i);
				int scale=rsme.getScale(i);
				column.set("TABLECOLUMN_LENGTH",length);
				if(type.startsWith("VARCHAR")){
					column.set("TABLECOLUMN_TYPE","VARCHAR");
					if("255".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR255");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("100".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR100");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("50".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR50");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("30".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR30");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("1000".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR1000");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("4000".equals(length)){
						column.set("TABLECOLUMN_TYPE","VARCHAR4000");
						column.set("TABLECOLUMN_LENGTH","");
					}else if("4".equals(length)){
						column.set("TABLECOLUMN_TYPE",ColumnType.YESORNO);
						column.set("TABLECOLUMN_LENGTH","");
					}else if(length.length()>4){
						column.set("TABLECOLUMN_TYPE","CLOB");
						column.set("TABLECOLUMN_LENGTH","");
					}else if(len>0){
						column.set("TABLECOLUMN_TYPE",ColumnType.VARCHAR);
						column.set("TABLECOLUMN_LENGTH",length+"");
					}else{
						column.set("TABLECOLUMN_TYPE","VARCHAR255");
						column.set("TABLECOLUMN_LENGTH","");
					}
				}else if(type.startsWith("INT") || type.startsWith("BIGINT") ){
					column.set("TABLECOLUMN_TYPE","NUMBER");
				}else if(type.startsWith("FLOAT") || type.startsWith("NUMERIC") || type.startsWith("DECIMAL") || type.startsWith("NUMBER")){
					if(scale<=0){
						column.set("TABLECOLUMN_TYPE","NUMBER");
						if(len==20){
							column.set("TABLECOLUMN_LENGTH","");
						}else{
							column.set("TABLECOLUMN_LENGTH",length);
						}
					}else{
						column.set("TABLECOLUMN_TYPE",ColumnType.FLOAT);
						if(len==20){
							length=scale+"";
						}else{
							length=len+","+scale;
						}
						column.set("TABLECOLUMN_LENGTH",length);
//						model.setLength(typeLength);
					}
				}else if(type.startsWith("TEXT") || type.startsWith("LONGTEXT") || type.startsWith("CLOB")){
					column.set("TABLECOLUMN_TYPE","CLOB");
					column.set("TABLECOLUMN_LENGTH","");
				}else{
					column.set("TABLECOLUMN_TYPE",ColumnType.CUSTOM);
					column.set("TABLECOLUMN_LENGTH",type);
				}
				column.set("TABLECOLUMN_ISNULL",rsme.isNullable(i)+"");
				//放入集合
				columns.add(column);
			}
			//装载
			resourceTable.set(BeanUtils.KEY_TABLE_COLUMNS,columns);
		} catch (Exception e) {
			throw new PlatformException("获取资源表【"+tableCode+"】表信息结构异常!",PlatformExceptionEnum.JE_CORE_UTIL_DATABASE_ERROR,new Object[]{table},e);
		}finally{
			JdbcUtil.close(rs, ps, connection);
		}
		return resourceTable;
	}
}










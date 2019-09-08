package com.je.table.service;

import com.je.cache.service.table.DynaCacheManager;
import com.je.core.constants.ConstantVars;
import com.je.core.constants.table.ColumnType;
import com.je.core.constants.table.TableType;
import com.je.core.constants.tree.TreeNodeType;
import com.je.core.dao.PCDaoTemplateImpl;
import com.je.core.entity.extjs.DbModel;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.SecurityUserHolder;
import com.je.core.service.PCDataService;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.sql.BuildingSql;
import com.je.core.util.ArrayUtils;
import com.je.core.util.DBSqlUtils;
import com.je.core.util.DataBaseUtils;
import com.je.core.util.DateUtils;
import com.je.core.util.JEUUID;
import com.je.core.util.JdbcUtil;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.develop.service.DevelopLogManager;
import com.je.rbac.model.Department;
import com.je.rbac.model.EndUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库DDL统一操作实现类
 */
@Service("tableManager")
public class TableManagerImpl implements TableManager {
	private static Logger logger = LoggerFactory.getLogger(TableManagerImpl.class);
	//	private static BuildingSql buildingSql;
	@Autowired
	private PCServiceTemplate pcServiceTemplate;
	@Autowired
	private PCDynaServiceTemplate serviceTemplate;
	@Autowired
	private PCDataService pcDataService;
	@Autowired
	private TableInfoManager tableInfoManager;
	@Autowired
	private DevelopLogManager developLogManager;

	/**
	 * 数据库创建操作
	 * @param resourceTableId TODO 暂不明确
	 * @return
	 */
	@Override
	public boolean createTable(String resourceTableId) {
		boolean success = true;
		try {
			DynaBean resourceTable=serviceTemplate.selectOneByPk("JE_CORE_RESOURCETABLE", resourceTableId);
			Boolean jeCore=("1".equals(resourceTable.getStr("SY_JECORE")));
			String tableCode = resourceTable.getStr("RESOURCETABLE_TABLECODE");
			List<DynaBean> columns=serviceTemplate.selectList("JE_CORE_TABLECOLUMN", " and TABLECOLUMN_RESOURCETABLE_ID='"+resourceTable.getStr("JE_CORE_RESOURCETABLE_ID")+"' ORDER BY TABLECOLUMN_CLASSIFY ASC,SY_ORDERINDEX ASC");
			List<DynaBean> keys=serviceTemplate.selectList("JE_CORE_TABLEKEY", " and TABLEKEY_RESOURCETABLE_ID='"+resourceTable.getStr("JE_CORE_RESOURCETABLE_ID")+"' ORDER BY TABLEKEY_CLASSIFY DESC,SY_ORDERINDEX ASC");
			List<DynaBean> indexs=serviceTemplate.selectList("JE_CORE_TABLEINDEX", " and TABLEINDEX_RESOURCETABLE_ID='"+resourceTable.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
			//对列进行校验
			String errors=checkColumns(columns,jeCore);
			if(StringUtil.isEmpty(errors)){
				resourceTable.set(BeanUtils.KEY_TABLE_COLUMNS, columns);
			}else{
				throw new PlatformException(errors, PlatformExceptionEnum.JE_CORE_TABLE_CHECKCOLUMN_ERROR,new Object[]{resourceTable});
			}
			//对键进行校验
			errors=checkKeys(keys);
			if(StringUtil.isEmpty(errors)){
				resourceTable.set(BeanUtils.KEY_TABLE_KEYS, keys);
			}else{
				throw new PlatformException(errors, PlatformExceptionEnum.JE_CORE_TABLE_CHECKKEY_ERROR,new Object[]{resourceTable});
			}
			//对索引进行校验
			errors=checkIndexs(indexs);
			if(StringUtil.isEmpty(errors)){
				resourceTable.set(BeanUtils.KEY_TABLE_INDEXS, indexs);
			}else{
				throw new PlatformException(errors, PlatformExceptionEnum.JE_CORE_TABLE_CHECKINDEX_ERROR,new Object[]{resourceTable});
			}
			//清空缓存并传入带有列和键的表资源对象
//			BeanUtils.getInstance().clearCache(tableCode);
//			DynaCacheManager.removeCache(resourceTable.getStr(BeanUtils.KEY_TABLE_CODE));
			List<String> arraySql = BuildingSql.getInstance().getDDL4CreateTable(resourceTable);
			for(String sql : arraySql){
				if(StringUtil.isNotEmpty(sql)){
					pcServiceTemplate.executeSql(sql);
				}
			}
			//设置他已经被创建
			resourceTable.set("RESOURCETABLE_ISCREATE","1");
//			List<DynaBean> keys=pcDynaServiceTemplate.selectList("JE_CORE_TABLEKEY", " and TABLEKEY_RESOURCETABLE_ID='"+resourceTableId+"'");
			boolean use = false;
			List<String> parentTableCodes=new ArrayList<String>();
			for(DynaBean key :keys){
				if("Foreign".equals(key.get("TABLEKEY_TYPE"))){
					//把自己的TableCode 写到父表的childTableCodes中用","分开
					DynaBean fatherTable=serviceTemplate.selectOne("JE_CORE_RESOURCETABLE"," and RESOURCETABLE_TABLECODE='"+key.getStr("TABLEKEY_LINKTABLE")+"'");
					String childTableCodes = fatherTable.getStr("RESOURCETABLE_CHILDTABLECODES");
					if(StringUtil.isNotEmpty(childTableCodes)){
						//不是空的操作
						if(childTableCodes.indexOf(tableCode) == -1){
							childTableCodes = childTableCodes +","+tableCode;
						}
						fatherTable.set("RESOURCETABLE_CHILDTABLECODES",childTableCodes);
					}else{
						fatherTable.set("RESOURCETABLE_CHILDTABLECODES",tableCode);
					}
					//处理表类型并赋值图标
					if(StringUtils.isNotEmpty(fatherTable.getStr("RESOURCETABLE_PARENTTABLECODES"))){
						if(!"TREE".equals(fatherTable.getStr("RESOURCETABLE_TYPE"))){
							fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-main-subtable");
						}
					}else{
						fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-master-table");
					}
					parentTableCodes.add(fatherTable.getStr("RESOURCETABLE_TABLECODE"));
					use =true;
					fatherTable.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_RESOURCETABLE");
					serviceTemplate.update(fatherTable);
				}else if("Primary".equals(key.getStr("TABLEKEY_TYPE"))){
					resourceTable.set("RESOURCETABLE_PKCODE", key.getStr("TABLEKEY_COLUMNCODE"));
				}
			}
			//设置其用到了外键
			if(use){
				for(String parentTableCode:parentTableCodes){
					String tableCodes = resourceTable.getStr("RESOURCETABLE_PARENTTABLECODES");
					if(StringUtil.isNotEmpty(tableCodes)){
						//不是空的操作
						if(tableCodes.indexOf(parentTableCode) == -1){
							tableCodes = tableCodes +","+parentTableCode;
						}
						resourceTable.set("RESOURCETABLE_PARENTTABLECODES",tableCodes);
					}else{
						resourceTable.set("RESOURCETABLE_PARENTTABLECODES",parentTableCode);
					}
					//处理表类型并赋值图标
					if(StringUtils.isNotEmpty(resourceTable.getStr("RESOURCETABLE_CHILDTABLECODES"))){
						if(!"TREE".equals(resourceTable.getStr("RESOURCETABLE_TYPE"))){
							resourceTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-main-subtable");
						}
					}else{
						resourceTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-child-table");
					}
				}
				resourceTable.set("RESOURCETABLE_ISUSEFOREIGNKEY","1");
			}else{
				resourceTable.set("RESOURCETABLE_ISUSEFOREIGNKEY","0");
			}
			resourceTable.set("RESOURCETABLE_OLDTABLECODE", resourceTable.getStr("RESOURCETABLE_TABLECODE"));
			resourceTable.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_RESOURCETABLE");
			resourceTable=serviceTemplate.update(resourceTable);
			//设置键和字段全为已经创建
			pcServiceTemplate.executeSql("UPDATE JE_CORE_TABLECOLUMN SET TABLECOLUMN_ISCREATE='1',TABLECOLUMN_OLDCODE=TABLECOLUMN_CODE,TABLECOLUMN_OLDUNIQUE=TABLECOLUMN_UNIQUE,TABLECOLUMN_TABLECODE='"+tableCode+"' WHERE TABLECOLUMN_RESOURCETABLE_ID='"+resourceTableId+"'");
			pcServiceTemplate.executeSql("UPDATE JE_CORE_TABLEKEY SET TABLEKEY_ISCREATE='1',TABLEKEY_TABLECODE='"+tableCode+"' WHERE TABLEKEY_RESOURCETABLE_ID='"+resourceTableId+"'");
			pcServiceTemplate.executeSql("UPDATE JE_CORE_TABLEINDEX SET TABLEINDEX_ISCREATE='1',TABLEINDEX_TABLECODE='"+tableCode+"' WHERE TABLEINDEX_RESOURCETABLE_ID='"+resourceTableId+"'");
			//清理缓存
			BeanUtils.getInstance().clearCache(tableCode);
			DynaCacheManager.removeCache(tableCode);
			//如果是树形则插入一条ROOT数据
			if(resourceTable.get("RESOURCETABLE_TYPE").equals(TableType.TREETABLE)){
				JSONTreeNode template=BeanUtils.getInstance().buildJSONTreeNodeTemplate((List<DynaBean>)BeanUtils.getInstance().getResourceTable(resourceTable.getStr("RESOURCETABLE_TABLECODE")).get(BeanUtils.KEY_TABLE_COLUMNS));
				DynaBean bean=new DynaBean(resourceTable.getStr("RESOURCETABLE_TABLECODE"),false);
				bean.set(BeanUtils.KEY_PK_CODE, template.getId());
				bean.set(template.getId(), "ROOT");
				bean.set(template.getParent(), null);
				bean.set(template.getText(),"ROOT");
				bean.set(template.getCode(), "ROOT");
				bean.set("SY_NODETYPE", "ROOT");
				bean.set("SY_PATH", "/ROOT");
				bean.set("SY_LAYER", 0);
				bean.set("SY_ORDERINDEX", 1);
				bean.set("SY_TREEORDERINDEX", StringUtil.preFillUp("1", 6, '0'));
				serviceTemplate.insert(bean);
			}
			saveTableTrace("JE_CORE_RESOURCETABLE", null, resourceTable, "INSERT", resourceTableId);
		} catch (Exception e) {
			success = false;
			if(e instanceof PlatformException){
				throw e;
			}else {
				throw new PlatformException("表创建失败异常", PlatformExceptionEnum.JE_CORE_TABLE_CREATE_ERROR, new Object[]{resourceTableId}, e);
			}
		}
		return success;
	}

	/**
	 * 删除表,和表在标管理功能下的数据
	 * @param ids
	 * @param isPhy 是否删除表结构
	 * @return
	 */
	@Override
	public boolean removeTable(String ids,Boolean isPhy) {
		boolean success = true;
		try {
			if(StringUtil.isNotEmpty(ids)) {
				String[] idArray = ids.split(ArrayUtils.SPLIT);
				for(String id : idArray) {
					//数据删除
					DynaBean resourceTable = serviceTemplate.selectOneByPk("JE_CORE_RESOURCETABLE", id);
					List<DynaBean> keys=serviceTemplate.selectList("JE_CORE_TABLEKEY"," and TABLEKEY_RESOURCETABLE_ID='"+id+"'");
					for(DynaBean key : keys){
						if("Foreign".equals(key.get("TABLEKEY_TYPE"))){//如果存在外键
							//清楚他在主表中的数据
							String tableCode = resourceTable.getStr("RESOURCETABLE_TABLECODE");
							DynaBean fatherTable=serviceTemplate.selectOne("JE_CORE_RESOURCETABLE"," and RESOURCETABLE_TABLECODE='"+key.getStr("TABLEKEY_LINKTABLE")+"'");
							if(fatherTable!=null){
								String childTableCodes = fatherTable.getStr("RESOURCETABLE_CHILDTABLECODES");
								if(StringUtil.isNotEmpty(childTableCodes)){
									if(childTableCodes.indexOf(","+key.getStr("TABLEKEY_LINKTABLE")) != -1){
										fatherTable.set("RESOURCETABLE_CHILDTABLECODES",childTableCodes.replaceAll(","+tableCode, ""));
									}else{
										fatherTable.set("RESOURCETABLE_CHILDTABLECODES",childTableCodes.replaceAll(tableCode, ""));
									}
									//处理表类型并赋值图标
									if(StringUtils.isNotEmpty(fatherTable.getStr("RESOURCETABLE_PARENTTABLECODES"))){
										if(!"TREE".equals(fatherTable.getStr("RESOURCETABLE_TYPE"))){
											if(StringUtil.isNotEmpty(fatherTable.getStr("RESOURCETABLE_CHILDTABLECODES"))){
												fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-main-subtable");
											}else{
												fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-child-table");
											}
										}
									}else{
										if(!"TREE".equals(fatherTable.getStr("RESOURCETABLE_TYPE"))){
											if(StringUtil.isNotEmpty(fatherTable.getStr("RESOURCETABLE_CHILDTABLECODES"))){
												fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-master-table");
											}else{
												fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-general-table");
											}
										}
									}
									fatherTable.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_RESOURCETABLE");
									serviceTemplate.update(fatherTable);
								}
							}
						}
					}
					//
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_TABLEKEY WHERE TABLEKEY_RESOURCETABLE_ID='"+id+"'");
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_TABLECOLUMN WHERE TABLECOLUMN_RESOURCETABLE_ID='"+id+"'");
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_TABLEINDEX WHERE TABLEINDEX_RESOURCETABLE_ID='"+id+"'");
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_TABLETRACE WHERE TABLETRACE_RESOURCETABLE_ID='"+id+"'");
					pcServiceTemplate.executeSql("DELETE FROM JE_CORE_RESOURCETABLE WHERE JE_CORE_RESOURCETABLE_ID='"+id+"'");
					//删除已经被创建的表
					if(isPhy){
						if("1".equals(resourceTable.getStr("RESOURCETABLE_ISCREATE"))){
							if("VIEW".equals(resourceTable.getStr("RESOURCETABLE_TYPE"))){
								pcServiceTemplate.executeSql("DROP VIEW "+resourceTable.getStr("RESOURCETABLE_TABLECODE"));
							}else{
								pcServiceTemplate.executeSql("DROP TABLE "+resourceTable.getStr("RESOURCETABLE_TABLECODE"));
							}
						}
					}
					developLogManager.doDevelopLog("DELETE","删除","TABLE","资源表",resourceTable.getStr("RESOURCETABLE_TABLENAME"),resourceTable.getStr("RESOURCETABLE_TABLECODE"),resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
				}
			}
		} catch (Exception e) {
			success = false;
			if(e instanceof PlatformException){
				throw e;
			}else {
				throw new PlatformException("表删除失败异常",PlatformExceptionEnum.JE_CORE_TABLE_DELETE_ERROR,new Object[]{ids,isPhy},e);
			}
		}
		return success;
	}

	/**
	 * 通过表名,把数据量中的表导入到资源表中管理
	 * @param table TODO 暂不明确
	 * @return
	 */
	@SuppressWarnings("static-access")
	@Override
	public DynaBean impTable(DynaBean table) {
		DynaBean resourceTable =null;
		String uuid=JEUUID.uuid();
		if(StringUtil.isNotEmpty(table.getStr("SY_PATH"))){
			table.set("SY_PATH", table.getStr("SY_PATH")+"/"+uuid);
			table.set("JE_CORE_RESOURCETABLE_ID", uuid);
		}
		//判断所用的数据库并调用指定的方法得到resourceTable
		if(PCDaoTemplateImpl.DBNAME.equalsIgnoreCase(ConstantVars.STR_ORACLE)){
			resourceTable=DataBaseUtils.getInstance().getTableBaseInfo(table);
		}else if(PCDaoTemplateImpl.DBNAME.equalsIgnoreCase(ConstantVars.STR_SQLSERVER)){
			resourceTable=DataBaseUtils.getInstance().getTableBaseInfoBySQLServer(table,pcServiceTemplate.getConnection());
		}else if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_MYSQL)){
			resourceTable=DataBaseUtils.getInstance().getTableBaseInfoByMySQL(table,pcServiceTemplate.getConnection());
		}
		if("1".equals(resourceTable.getStr("RESOURCETABLE_ISCREATE"))){
			List<DynaBean> columns=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
			List<DynaBean> keys=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_KEYS);
			List<DynaBean> indexs=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_INDEXS);
			resourceTable.remove(BeanUtils.KEY_TABLE_COLUMNS);
			resourceTable.remove(BeanUtils.KEY_TABLE_KEYS);
			EndUser user=SecurityUserHolder.getCurrentUser();
			Department dept=SecurityUserHolder.getCurrentUserDept();
			String nowDate=DateUtils.formatDateTime(new Date());
			resourceTable.set("SY_JECORE", "0");
			resourceTable.set("SY_JESYS", "0");
			resourceTable.set("SY_CREATETIME", nowDate);
			resourceTable.set("SY_CREATEUSER", user.getUserCode());
			resourceTable.set("SY_CREATEUSERNAME", user.getUsername());
			resourceTable.set("SY_CREATEORG", dept.getDeptCode());
			resourceTable.set("SY_CREATEORGNAME", dept.getDeptName());
			resourceTable.set("RESOURCETABLE_OLDTABLECODE", resourceTable.getStr("RESOURCETABLE_TABLECODE"));
			DynaBean inserted=serviceTemplate.insert(resourceTable);
			for(DynaBean column:columns){
				column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
				column.set("SY_CREATETIME", nowDate);
				column.set("SY_CREATEUSER", user.getUserCode());
				column.set("SY_CREATEUSERNAME", user.getUsername());
				column.set("SY_CREATEORG", dept.getDeptCode());
				column.set("SY_CREATEORGNAME", dept.getDeptName());
				column.set("TABLECOLUMN_OLDCODE",column.getStr("TABLECOLUMN_CODE"));
				column.set("TABLECOLUMN_TABLECODE", inserted.getStr("RESOURCETABLE_TABLECODE"));
				column.set("TABLECOLUMN_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
				serviceTemplate.insert(column);
			}
			for(DynaBean key:keys){
				key.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_TABLEKEY");
				key.set("SY_CREATETIME", nowDate);
				key.set("SY_CREATEUSER", user.getUserCode());
				key.set("SY_CREATEUSERNAME", user.getUsername());
				key.set("SY_CREATEORG", dept.getDeptCode());
				key.set("SY_CREATEORGNAME", dept.getDeptName());
				key.set("TABLEKEY_TABLECODE", inserted.getStr("RESOURCETABLE_TABLECODE"));
				key.set("TABLEKEY_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
				serviceTemplate.insert(key);
			}
			for(DynaBean index:indexs){
				index.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_TABLEINDEX");
				index.set("SY_CREATETIME", nowDate);
				index.set("SY_CREATEUSER", user.getUserCode());
				index.set("SY_CREATEUSERNAME", user.getUsername());
				index.set("SY_CREATEORG", dept.getDeptCode());
				index.set("SY_CREATEORGNAME", dept.getDeptName());
				index.set("TABLEINDEX_TABLECODE", inserted.getStr("RESOURCETABLE_TABLECODE"));
				index.set("TABLEINDEX_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
				serviceTemplate.insert(index);
			}
		}
		return resourceTable;
	}

	/**
	 * 同步表结构
	 * @param table TODO 暂不明确
	 * @return
	 */
	@Override
	public DynaBean syncTable(DynaBean table) {
		// TODO Auto-generated method stub
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		List<DynaBean> columns=serviceTemplate.selectList("JE_CORE_TABLECOLUMN"," AND TABLECOLUMN_RESOURCETABLE_ID='"+table.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		List<DbModel> dbModels=pcDataService.loadTableColumn(tableCode);
		//找到需要删除的列
		for(DynaBean column:columns){
			String columnCode=column.getStr("TABLECOLUMN_CODE","").toUpperCase();
			boolean have=false;
			for(DbModel dbModel:dbModels){
				if(columnCode.equals(dbModel.getCode().toUpperCase())){
					have=true;
				}
			}
			if(!have){
				tableInfoManager.removeColumn(column, column.getStr("JE_CORE_TABLECOLUMN_ID"),false);
//				catDaoTemplate.executeSql(" DELETE FROM JE_CORE_TABLECOLUMN WHERE JE_CORE_TABLECOLUMN_ID='"+column.getStr("JE_CORE_TABLECOLUMN_ID")+"'");
			}
		}
		String pkCode=table.getStr("RESOURCETABLE_PKCODE");
		if(StringUtil.isEmpty(pkCode)){
			List<DynaBean> keys=serviceTemplate.selectList("JE_CORE_TABLEKEY", " AND TABLEKEY_RESOURCETABLE_ID='"+table.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
			for(DynaBean key:keys){
				if("Primary".equals(key.getStr("TABLEKEY_TYPE"))){
					pkCode=key.getStr("TABLEKEY_COLUMNCODE");
				}
			}
			if(StringUtil.isNotEmpty(pkCode)){
				table.set("RESOURCETABLE_PKCODE", pkCode);
				table=serviceTemplate.update(table);
			}
		}
		for(DbModel dbModel:dbModels){
			boolean have=false;
			for(DynaBean column:columns){
				String columnCode=column.getStr("TABLECOLUMN_CODE","").toUpperCase();
				if(columnCode.equals(dbModel.getCode().toUpperCase())){
					have=true;
					boolean update=false;
					if(!column.getStr("TABLECOLUMN_TYPE","").equals(dbModel.getType())){
						update=true;
						column.set("TABLECOLUMN_TYPE", dbModel.getType());
					}
					if(!column.getStr("TABLECOLUMN_LENGTH","").equals(dbModel.getLength())){
						column.set("TABLECOLUMN_LENGTH", dbModel.getLength());
						update=true;
					}
					if(!column.getStr("TABLECOLUMN_ISNULL","").equals((dbModel.isNull()?"1":"0"))){
						column.set("TABLECOLUMN_ISNULL",dbModel.isNull()?"1":"0");
						update=true;
					}
					if("0".equals(column.getStr("TABLECOLUMN_ISCREATE"))){
						column.set("TABLECOLUMN_ISCREATE", "1");
						update=true;
					}
					if(column.getStr("TABLECOLUMN_CODE","").equals(table.getStr("RESOURCETABLE_PKCODE"))){
						column.set("TABLECOLUMN_TYPE", "ID");
						update=true;
					}
					if(update){
						serviceTemplate.update(column);
					}
				}
			}
			if(!have){
				DynaBean column = new DynaBean("JE_CORE_TABLECOLUMN",false);
				column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
				column.set("TABLECOLUMN_CODE",dbModel.getCode());
				column.set("TABLECOLUMN_OLDCODE",dbModel.getCode());
				column.set("TABLECOLUMN_NAME","");
				column.set("TABLECOLUMN_ISCREATE","1");
				column.set("TABLECOLUMN_UNIQUE","0");
				column.set("TABLECOLUMN_TREETYPE","NORMAL");
				column.set("TABLECOLUMN_TABLECODE",tableCode);
				column.set("TABLECOLUMN_CLASSIFY","PRO");
				column.set("SY_STATUS","1");
				column.set("SY_ORDERINDEX",columns.size()+1);
				column.set("TABLECOLUMN_ISNULL",dbModel.isNull()?"1":"0");
				column.set("TABLECOLUMN_TYPE", dbModel.getType());
				column.set("TABLECOLUMN_LENGTH", dbModel.getLength());
				column.set("TABLECOLUMN_TABLECODE", table.getStr("RESOURCETABLE_TABLECODE"));
				column.set("TABLECOLUMN_RESOURCETABLE_ID", table.getStr("JE_CORE_RESOURCETABLE_ID"));
				serviceTemplate.buildModelCreateInfo(column);
				if(column.getStr("TABLECOLUMN_CODE","").equals(table.getStr("RESOURCETABLE_PKCODE"))){
					column.set("TABLECOLUMN_TYPE", "ID");
				}
				column=serviceTemplate.insert(column);
				columns.add(column);
				//放入集合
//            	columns.add(column);
			}
		}
		BeanUtils.getInstance().clearCache(table.getStr("RESOURCETABLE_TABLECODE"));
		DynaCacheManager.removeCache(table.getStr("RESOURCETABLE_TABLECODE"));
		//找到需要导入的列
		return table;
	}

	/**
	 * 通过视图名,把数据量中的表导入到资源表中管理
	 * @param table TODO 暂不明确
	 * @return
	 */
	@SuppressWarnings("static-access")
	@Override
	public DynaBean impView(DynaBean table) {
		DynaBean resourceTable =null;
		String uuid=JEUUID.uuid();
		if(StringUtil.isNotEmpty(table.getStr("SY_PATH"))){
			table.set("SY_PATH", table.getStr("SY_PATH")+"/"+uuid);
			table.set("JE_CORE_RESOURCETABLE_ID", uuid);
		}
		//判断所用的数据库并调用指定的方法得到resourceTable
		if(PCDaoTemplateImpl.DBNAME.equalsIgnoreCase(ConstantVars.STR_ORACLE)){
			resourceTable=DataBaseUtils.getInstance().getViewBaseInfo(table,pcServiceTemplate.getConnection());
		}else if(PCDaoTemplateImpl.DBNAME.equalsIgnoreCase(ConstantVars.STR_SQLSERVER)){
			resourceTable=DataBaseUtils.getInstance().getViewBaseInfoBySQLServer(table,pcServiceTemplate.getConnection());
		}else if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_MYSQL)){
			resourceTable=DataBaseUtils.getInstance().getViewBaseInfoByMySQL(table,pcServiceTemplate.getConnection());
		}
		if("1".equals(resourceTable.getStr("RESOURCETABLE_ISCREATE"))){
			List<DynaBean> columns=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
			List<DynaBean> keys=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_KEYS);
			if(keys==null){
				keys=new ArrayList<DynaBean>();
			}
			resourceTable.remove(BeanUtils.KEY_TABLE_COLUMNS);
			resourceTable.remove(BeanUtils.KEY_TABLE_KEYS);
			EndUser user=SecurityUserHolder.getCurrentUser();
			Department dept=SecurityUserHolder.getCurrentUserDept();
			String nowDate=DateUtils.formatDateTime(new Date());
			resourceTable.set("RESOURCETABLE_MOREROOT", "0");
			resourceTable.set("SY_JECORE", "0");
			resourceTable.set("SY_JESYS", "0");
			resourceTable.set("SY_CREATETIME", nowDate);
			resourceTable.set("SY_CREATEUSER", user.getUserCode());
			resourceTable.set("SY_CREATEUSERNAME", user.getUsername());
			resourceTable.set("SY_CREATEORG", dept.getDeptCode());
			resourceTable.set("SY_CREATEORGNAME", dept.getDeptName());
			resourceTable.set("RESOURCETABLE_OLDTABLECODE", resourceTable.getStr("RESOURCETABLE_TABLECODE"));
			DynaBean inserted=serviceTemplate.insert(resourceTable);
			for(DynaBean column:columns){
				column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
				column.set("SY_CREATETIME", nowDate);
				column.set("SY_CREATEUSER", user.getUserCode());
				column.set("SY_CREATEUSERNAME", user.getUsername());
				column.set("SY_CREATEORG", dept.getDeptCode());
				column.set("SY_CREATEORGNAME", dept.getDeptName());
				column.set("TABLECOLUMN_OLDCODE",column.getStr("TABLECOLUMN_CODE"));
				column.set("TABLECOLUMN_TABLECODE", inserted.getStr("RESOURCETABLE_TABLECODE"));
				column.set("TABLECOLUMN_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
				column.set("TABLECOLUMN_UNIQUE", "0");
				column.set("TABLECOLUMN_CLASSIFY", "PRO");
				column.set("TABLECOLUMN_TREETYPE", "NORMAL");
				serviceTemplate.insert(column);
			}
			for(DynaBean key:keys){
				key.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_TABLEKEY");
				key.set("SY_CREATETIME", nowDate);
				key.set("SY_CREATEUSER", user.getUserCode());
				key.set("SY_CREATEUSERNAME", user.getUsername());
				key.set("SY_CREATEORG", dept.getDeptCode());
				key.set("SY_CREATEORGNAME", dept.getDeptName());
				key.set("TABLEKEY_TABLECODE", inserted.getStr("RESOURCETABLE_TABLECODE"));
				key .set("TABLEKEY_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
				serviceTemplate.insert(key);
			}
		}
		return resourceTable;
	}

	/**
	 * 创建视图
	 * @param table TODO 暂不明确
	 * @return
	 */
	@Override
	public DynaBean createView(DynaBean table) {
		// TODO Auto-generated method stub
		String sql=table.getStr("RESOURCETABLE_SQL");
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		table.setStr("RESOURCETABLE_OLDTABLECODE",table.getStr("RESOURCETABLE_TABLECODE"));
		//创建视图
		pcServiceTemplate.executeSql(" CREATE VIEW "+tableCode+" AS "+sql);
		return table;
	}

	/**
	 * 持久视图信息
	 * @param table TODO 暂不明确
	 * @param fields TODO 暂不明确
	 * @return
	 */
	@Override
	public DynaBean saveViewInfo(DynaBean table, String fields){
		//获取视图列信息
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		DynaBean recourceTable=new DynaBean("JE_CORE_RESOURCETABLE",false);
		recourceTable.set("RESOURCETABLE_TABLECODE", tableCode);
		table.set("RESOURCETABLE_ISCREATE", "1");
		if(PCDaoTemplateImpl.DBNAME.equalsIgnoreCase(ConstantVars.STR_ORACLE)){
			recourceTable=DataBaseUtils.getInstance().getViewBaseInfo(recourceTable,pcServiceTemplate.getConnection());
		}else if(PCDaoTemplateImpl.DBNAME.equalsIgnoreCase(ConstantVars.STR_SQLSERVER)){
			recourceTable=DataBaseUtils.getInstance().getViewBaseInfoBySQLServer(recourceTable,pcServiceTemplate.getConnection());
		}else if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_MYSQL)){
			recourceTable=DataBaseUtils.getInstance().getViewBaseInfoByMySQL(recourceTable,pcServiceTemplate.getConnection());
		}
		List<DynaBean> columns=(List<DynaBean>) recourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
		DynaBean inserted=serviceTemplate.insert(table);
		EndUser user=SecurityUserHolder.getCurrentUser();
		Department dept=SecurityUserHolder.getCurrentUserDept();
		String nowDate=DateUtils.formatDateTime(new Date());
		Map<String,JSONObject> columnsInfo=buildColumnInfo(fields);
		String pkCode="";
		for(DynaBean column:columns){
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("SY_CREATETIME", nowDate);
			column.set("SY_CREATEUSER", user.getUserCode());
			column.set("SY_CREATEUSERNAME", user.getUsername());
			column.set("SY_CREATEORG", dept.getDeptCode());
			column.set("SY_CREATEORGNAME", dept.getDeptName());
			column.set("TABLECOLUMN_OLDCODE",column.getStr("TABLECOLUMN_CODE"));
			column.set("TABLECOLUMN_TABLECODE", inserted.getStr("RESOURCETABLE_TABLECODE"));
			column.set("TABLECOLUMN_RESOURCETABLE_ID", inserted.getStr("JE_CORE_RESOURCETABLE_ID"));
			column.set("TABLECOLUMN_UNIQUE", "0");
			column.set("TABLECOLUMN_CLASSIFY", "PRO");
			column.set("TABLECOLUMN_TREETYPE", "NORMAL");
			if(columnsInfo.get(column.getStr("TABLECOLUMN_CODE"))!=null){
				JSONObject objs=columnsInfo.get(column.getStr("TABLECOLUMN_CODE"));
				column.set("TABLECOLUMN_NAME", objs.getString("name"));
				column.set("TABLECOLUMN_VIEWCONFIG", objs.toString());
				String type=objs.getString("dataType");//存储视图列的配置信息
				if(StringUtil.isNotEmpty(type)){
					column.set("TABLECOLUMN_TYPE", type);
					if("ID".equals(type)){
						pkCode=column.getStr("TABLECOLUMN_CODE");
					}
				}

			}
			serviceTemplate.insert(column);
		}
		if(StringUtil.isNotEmpty(pkCode)){
			DynaBean pk = new DynaBean("JE_CORE_TABLEKEY",false);
			pk.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			pk.set("TABLEKEY_CODE","JE_"+DateUtils.getUniqueTime()+"_ID");
			pk.set("TABLEKEY_COLUMNCODE",pkCode);
			pk.set("TABLEKEY_TYPE","Primary");
			pk.set("TABLEKEY_ISRESTRAINT","1");
			pk.set("TABLEKEY_CHECKED","1");
			pk.set("TABLEKEY_CLASSIFY","SYS");
			pk.set("SY_ORDERINDEX",0);
			pk.set("SY_CREATETIME", nowDate);
			pk.set("SY_CREATEUSER", user.getUserCode());
			pk.set("SY_CREATEUSERNAME", user.getUsername());
			pk.set("SY_CREATEORG", dept.getDeptCode());
			pk.set("SY_CREATEORGNAME", dept.getDeptName());
			pk.set("TABLEKEY_ISCREATE", "1");
			pk.set("TABLEKEY_TABLECODE", inserted.get("RESOURCETABLE_TABLECODE"));
			pk.set("TABLEKEY_RESOURCETABLE_ID", inserted.getStr("JE_CORE_RESOURCETABLE_ID"));
			serviceTemplate.insert(pk);
			inserted.set("RESOURCETABLE_PKCODE", pkCode);
			inserted=serviceTemplate.update(inserted);
		}
		return inserted;
	}

	/**
	 * 修改视图
	 * @param table TODO 暂不明确
	 * @return
	 */
	@Override
	public DynaBean updateView(DynaBean table) {
		// TODO Auto-generated method stub
		String sql=table.getStr("RESOURCETABLE_SQL");
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		table.setStr("RESOURCETABLE_OLDTABLECODE",table.getStr("RESOURCETABLE_TABLECODE"));
		String dbUpdateSql=DBSqlUtils.getPcDBMethodManager().getUpdateView();
		pcServiceTemplate.executeSql(dbUpdateSql+" "+tableCode+" AS "+sql);
		return table;
	}

	/**
	 * 获取视图的创建语句
	 * @param tableCode TODO 暂不明确
	 * @return
	 */
	@Override
	public String getViewCreateSql(String tableCode) {
		// TODO Auto-generated method stub
		String createSql="";
		if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME)){
			JdbcUtil jdbcUtil=JdbcUtil.getInstance();
			List<HashMap> lists=jdbcUtil.query("SELECT VIEW_NAME,TEXT FROM USER_VIEWS WHERE VIEW_NAME='"+tableCode+"'");
			if(lists!=null && lists.size()>0){
				createSql=lists.get(0).get("TEXT")+"";
				if(StringUtil.isNotEmpty(createSql)){
					createSql=createSql.replaceAll("\r\n", " \r\n");
					createSql=createSql.replaceAll("\n", " \n");
				}
			}
			jdbcUtil.close();
		}else if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME)){
			List<Map> lists=pcServiceTemplate.queryMapBySql("SHOW CREATE VIEW "+tableCode+"");
			if(lists!=null && lists.size()>0){
				createSql=lists.get(0).get("Create View")+"";
				if(StringUtil.isNotEmpty(createSql)){
					int createIndex=createSql.indexOf("AS");
					if(createIndex!=-1){
						createSql=createSql.substring(createIndex+3);
					}
				}
				if(StringUtil.isNotEmpty(createSql)){
					createSql=createSql.replaceAll("\r\n", " \r\n");
					createSql=createSql.replaceAll("\n", " \n");
				}
			}
		}else{
			List<Map> lists=pcServiceTemplate.queryMapBySql("sp_helptext '"+tableCode+"'");
			StringBuffer createVal=new StringBuffer();
			if(lists!=null && lists.size()>0){
				for(Map v:lists){
					createVal.append(v.get("Text"));
				}
			}
			createSql=createVal.toString();
			if(StringUtil.isNotEmpty(createSql)){
				int createIndex=createSql.indexOf("AS");
				if(createIndex!=-1){
					createSql=createSql.substring(createIndex+3);
					createSql=createSql.replaceAll("\r\n", " \r\n");
					createSql=createSql.replaceAll("\n", " \n");
				}
			}
		}
		return createSql;
	}

	/**
	 * 持久视图信息
	 * @param table TODO 暂不明确
	 * @param fields TODO 暂不明确
	 * @param syncView 是否同步视图，是则不修改功能信息和键信息   只同步列信息
	 * @return
	 */
	@Override
	public DynaBean updateViewInfo(DynaBean table, String fields,Boolean syncView) {
		// TODO Auto-generated method stub
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		DynaBean recourceTable=new DynaBean("JE_CORE_RESOURCETABLE",false);
		recourceTable.set("RESOURCETABLE_TABLECODE", tableCode);
		table.set("RESOURCETABLE_ISCREATE", "1");
		if(PCDaoTemplateImpl.DBNAME.equalsIgnoreCase(ConstantVars.STR_ORACLE)){
			recourceTable=DataBaseUtils.getInstance().getViewBaseInfo(recourceTable,pcServiceTemplate.getConnection());
		}else if(PCDaoTemplateImpl.DBNAME.equalsIgnoreCase(ConstantVars.STR_SQLSERVER)){
			recourceTable=DataBaseUtils.getInstance().getViewBaseInfoBySQLServer(recourceTable,pcServiceTemplate.getConnection());
		}else if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_MYSQL)){
			recourceTable=DataBaseUtils.getInstance().getViewBaseInfoByMySQL(recourceTable,pcServiceTemplate.getConnection());
		}
		List<DynaBean> columns=(List<DynaBean>) recourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
		//需添加的列
		List<DynaBean> addColumns=new ArrayList<DynaBean>();
		//需修改的列
		List<DynaBean> updateColumns=new ArrayList<DynaBean>();
		//需删除的列
		List<String> delColumns=new ArrayList<String>();
		//构建  操作的列和键信息
		Set<String> haveCodes=new HashSet<String>();
		Map<String,JSONObject> columnsInfo=buildColumnInfo(fields);
		List<DynaBean> oldColumns=serviceTemplate.selectList("JE_CORE_TABLECOLUMN", " AND TABLECOLUMN_RESOURCETABLE_ID='"+table.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		String pkCode=table.getStr("RESOURCETABLE_PKCODE");

		for(DynaBean column:columns){
			String columnCode=column.getStr("TABLECOLUMN_CODE");
			Boolean  have=false;
			for(DynaBean oldColumn:oldColumns){
				if(columnCode.equalsIgnoreCase(oldColumn.getStr("TABLECOLUMN_CODE"))){
					//如果找到相同的 修改
					if(StringUtil.isNotEmpty(column.getStr("TABLECOLUMN_NAME"))){
						oldColumn.set("TABLECOLUMN_NAME", column.getStr("TABLECOLUMN_NAME"));
					}
					oldColumn.set("TABLECOLUMN_CODE", column.getStr("TABLECOLUMN_CODE"));
					oldColumn.set("TABLECOLUMN_TYPE", column.getStr("TABLECOLUMN_TYPE"));
					oldColumn.set("TABLECOLUMN_LENGTH", column.getStr("TABLECOLUMN_LENGTH"));
					oldColumn.set("TABLECOLUMN_ISNULL", column.getStr("TABLECOLUMN_ISNULL"));
					String newPkCode=buildViewColumn(columnsInfo, oldColumn);
					if(StringUtil.isEmpty(pkCode)){
						pkCode=newPkCode;
					}
					updateColumns.add(oldColumn);
					have=true;
					haveCodes.add(columnCode);
					break;
				}
			}
			if(!have){
				String newPkCode=buildViewColumn(columnsInfo, column);
				if(StringUtil.isEmpty(pkCode)){
					pkCode=newPkCode;
				}
				addColumns.add(column);
			}
		}
		if(StringUtil.isEmpty(pkCode) && StringUtil.isNotEmpty(table.getStr("RESOURCETABLE_PKCODE"))){
			pkCode=table.getStr("RESOURCETABLE_PKCODE");
		}
		for(DynaBean oldColumn:oldColumns){
			if(!haveCodes.contains(oldColumn.getStr("TABLECOLUMN_CODE"))){
				delColumns.add(oldColumn.getStr("TABLECOLUMN_CODE"));
			}
		}
		EndUser user=SecurityUserHolder.getCurrentUser();
		Department dept=SecurityUserHolder.getCurrentUserDept();
		String nowDate=DateUtils.formatDateTime(new Date());
		//处理列
		for(DynaBean column:addColumns){
			column.set("SY_CREATETIME", nowDate);
			column.set("SY_CREATEUSER", user.getUserCode());
			column.set("SY_CREATEUSERNAME", user.getUsername());
			column.set("SY_CREATEORG", dept.getDeptCode());
			column.set("SY_CREATEORGNAME", dept.getDeptName());
			column.set("TABLECOLUMN_OLDCODE",column.getStr("TABLECOLUMN_CODE"));
			column.set("TABLECOLUMN_TABLECODE", table.getStr("RESOURCETABLE_TABLECODE"));
			column.set("TABLECOLUMN_RESOURCETABLE_ID", table.getStr("JE_CORE_RESOURCETABLE_ID"));
			column.set("TABLECOLUMN_UNIQUE", "0");
			column.set("TABLECOLUMN_ISCREATE", "1");
			column.set("TABLECOLUMN_CLASSIFY", "PRO");
			column.set("TABLECOLUMN_TREETYPE", "NORMAL");
			if(column.getStr("TABLECOLUMN_CODE","").equals(pkCode)){
				column.set("TABLECOLUMN_TYPE", "ID");
			}
			serviceTemplate.insert(column);
		}
		for(DynaBean column:updateColumns){
			column.set("TABLECOLUMN_ISCREATE", "1");
			column.set("SY_MODIFYTIME", nowDate);
			column.set("SY_MODIFYUSER", user.getUserCode());
			column.set("SY_MODIFYUSERNAME", user.getUsername());
			column.set("SY_MODIFYORG", dept.getDeptCode());
			column.set("SY_MODIFYORGNAME", dept.getDeptName());
			if(column.getStr("TABLECOLUMN_CODE","").equals(pkCode)){
				column.set("TABLECOLUMN_TYPE", "ID");
			}
			serviceTemplate.update(column);
		}
		if(delColumns.size()>0){
			pcServiceTemplate.executeSql("DELETE FROM JE_CORE_TABLECOLUMN WHERE TABLECOLUMN_CODE IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(delColumns))+") AND TABLECOLUMN_RESOURCETABLE_ID='"+table.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		}
//		if(syncView){
//			BeanUtils.getInstance().clearCache(table.getStr("RESOURCETABLE_TABLECODE"));
//			DynaCacheManager.removeCache(table.getStr("RESOURCETABLE_TABLECODE"));
//			return table;
//		}
		//处理键
		if(StringUtil.isNotEmpty(pkCode)){
			table.set("RESOURCETABLE_PKCODE", pkCode);
		}
		DynaBean updated=serviceTemplate.update(table);
		DynaBean tablePk=serviceTemplate.selectOne("JE_CORE_TABLEKEY"," AND TABLEKEY_RESOURCETABLE_ID='"+updated.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		Boolean updatePk=false;
		if(StringUtil.isNotEmpty(pkCode)){
			if(tablePk!=null && !pkCode.equals(tablePk.getStr("TABLEKEY_COLUMNCODE"))){
				updatePk=true;
				tablePk.set("TABLEKEY_COLUMNCODE", pkCode);
				tablePk.set("TABLEKEY_CODE", pkCode+"_CODE");
				serviceTemplate.update(tablePk);
			}
		}else{
			if( tablePk!=null && delColumns.contains(tablePk.getStr("TABLEKEY_COLUMNCODE"))){
				serviceTemplate.deleteByWehreSql("JE_CORE_TABLEKEY", " AND JE_CORE_TABLEKEY_ID='"+tablePk.getStr("JE_CORE_TABLEKEY_ID")+"'");
			}
		}
		//处理功能
		List<DynaBean> funcs=serviceTemplate.selectList("JE_CORE_FUNCINFO", " and FUNCINFO_TABLENAME='"+updated.getStr("RESOURCETABLE_TABLECODE")+"'");
		for(DynaBean fun:funcs){
			String funId=fun.getStr("JE_CORE_FUNCINFO_ID");
			if(updatePk){
				fun.set("FUNCINFO_PKNAME", pkCode);
				serviceTemplate.update(fun);
			}
			if(delColumns.size()>0){
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_RESOURCECOLUMN WHERE RESOURCECOLUMN_CODE IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(delColumns))+") AND RESOURCECOLUMN_FUNCINFO_ID='"+funId+"'");
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_RESOURCEFIELD WHERE RESOURCEFIELD_CODE IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(delColumns))+") AND RESOURCEFIELD_FUNCINFO_ID='"+funId+"'");
			}
			if(addColumns.size()>0 && !syncView){
				String columnSql = "select RESOURCECOLUMN_CODE from JE_CORE_RESOURCECOLUMN where RESOURCECOLUMN_FUNCINFO_ID = '"+funId+"' and RESOURCECOLUMN_IFIMPL = '1'";
				List<String> columnList = (List<String>) pcServiceTemplate.queryBySql(columnSql);
				String fieldSql = "select RESOURCEFIELD_CODE from JE_CORE_RESOURCEFIELD where RESOURCEFIELD_FUNCINFO_ID = '"+funId+"' and RESOURCEFIELD_IFIMPL = '1'";
				List<String> fieldList = (List<String>) pcServiceTemplate.queryBySql(fieldSql);
				List<Object> maxOrder=(List<Object>) pcServiceTemplate.queryBySql("select max(SY_ORDERINDEX) from JE_CORE_RESOURCECOLUMN where RESOURCECOLUMN_FUNCINFO_ID = '"+funId+"' and RESOURCECOLUMN_IFIMPL = '1'");
				List<Object> fieldMaxOrder=(List<Object>) pcServiceTemplate.queryBySql("select max(SY_ORDERINDEX) from JE_CORE_RESOURCEFIELD where RESOURCEFIELD_FUNCINFO_ID = '"+funId+"' and RESOURCEFIELD_IFIMPL = '1'");
				Integer fieldOrderIndex=0;
				Integer columnOrderIndex=0;
				if(maxOrder!=null && maxOrder.size()>0){
					Object orderObj=maxOrder.iterator().next();
					if(orderObj==null){
						orderObj=0;
					}
					columnOrderIndex=Integer.parseInt(orderObj.toString());
				}
				if(fieldMaxOrder!=null && fieldMaxOrder.size()>0){
					Object orderObj=fieldMaxOrder.iterator().next();
					if(orderObj==null){
						orderObj=0;
					}
					fieldOrderIndex=Integer.parseInt(orderObj.toString());
				}
				for(Integer i=0;i<addColumns.size();i++){
					DynaBean column=addColumns.get(i);
					if(!columnList.contains(column.getStr("TABLECOLUMN_CODE"))){
						DynaBean rc = new DynaBean("JE_CORE_RESOURCECOLUMN",false);
						rc.set(BeanUtils.KEY_PK_CODE, "JE_CORE_RESOURCECOLUMN_ID");
						rc.set("RESOURCECOLUMN_FUNCINFO_ID",fun.getStr("JE_CORE_FUNCINFO_ID"));
						rc.set("RESOURCECOLUMN_CODE",column.get("TABLECOLUMN_CODE"));
						rc.set("RESOURCECOLUMN_NAME",column.get("TABLECOLUMN_NAME"));
						rc.set("RESOURCECOLUMN_IFIMPL","1");    //将导入的字段标识为true
						rc.set("RESOURCECOLUMN_QUICKQUERY","0");  //是否快速查询 false
						rc.set("SY_ORDERINDEX",columnOrderIndex+i+1);
						rc.set("RESOURCECOLUMN_WIDTH","80");    //设置默认宽度为80
						rc.set("RESOURCECOLUMN_WIDTH_EN","80");    //设置默认宽度为80
						rc.set("RESOURCECOLUMN_CONVERSION","0");  //是否拥有转换器conversion
						rc.set("RESOURCECOLUMN_HIGHLIGHTING","0");  //是否拥有转换器highlighting
						rc.set("RESOURCECOLUMN_HYPERLINK","0");     //是否超链接hyperLink
						rc.set("RESOURCECOLUMN_ISDD","0");    //是否在字典中取
						rc.set("RESOURCECOLUMN_MERGE","0");   //合并单元格      否
						rc.set("RESOURCECOLUMN_LOCKED","0");    //锁定列  否
						rc.set("RESOURCECOLUMN_MORECOLUMN","0");  //多表头   否
						rc.set("RESOURCECOLUMN_ISPK","0");
						rc.set("RESOURCECOLUMN_ENABLEICON","0");//启用图标
						rc.set("RESOURCECOLUMN_SYSMODE",fun.getStr("FUNCINFO_SYSMODE"));
						rc.set("RESOURCECOLUMN_ALLOWBLANK","1");   //是否可以为空  true
						rc.set("RESOURCECOLUMN_QUERYTYPE","no");
						rc.set("RESOURCECOLUMN_XTYPE","uxcolumn");  //字段类型  默认为无  不可编辑
						rc.set("RESOURCECOLUMN_ALLOWEDIT","0");    //列表编辑   默认为否
						rc.set("RESOURCECOLUMN_HIDDEN","1");
						rc.set("RESOURCECOLUMN_ALIGN","LEAF");
						rc.set("RESOURCECOLUMN_INDEX","0");
						rc.set("RESOURCECOLUMN_ORDER","0");
						rc.set("SY_CREATETIME", nowDate);
						rc.set("SY_CREATEUSER", user.getUserCode());
						rc.set("SY_CREATEUSERNAME", user.getUsername());
						rc.set("SY_CREATEORG", dept.getDeptCode());
						rc.set("SY_CREATEORGNAME", dept.getDeptName());
						serviceTemplate.insert(rc);
						DynaBean rf = new DynaBean("JE_CORE_RESOURCEFIELD",false);
						rf.set(BeanUtils.KEY_PK_CODE, "JE_CORE_RESOURCEFIELD_ID");
						rf.set("RESOURCEFIELD_FUNCINFO_ID",fun.getStr("JE_CORE_FUNCINFO_ID"));
						rf.set("RESOURCEFIELD_CODE",column.get("TABLECOLUMN_CODE"));
						rf.set("RESOURCEFIELD_NAME",column.get("TABLECOLUMN_NAME"));
						rf.set("RESOURCEFIELD_IFIMPL","1");   //将导入的字段标识为true
						rf.set("RESOURCEFIELD_DISABLED","1");  //是否可用  true
						rf.set("SY_ORDERINDEX",fieldOrderIndex+i+1);   //默认序号为0
//						if(flag){
//							rf.set("SY_ORDERINDEX",0.0);
//						}
						rf.set("RESOURCEFIELD_ALLOWBLANK","1");   //是否可以为空  true
						rf.set("RESOURCEFIELD_READONLY","0");     //是否只读
						String columnType=column.getStr("TABLECOLUMN_TYPE");
						//数值型
						if("NUMBER".equals(columnType) || "FLOAT".equals(columnType)){
							rf.set("RESOURCEFIELD_XTYPE","numberfield");  //字段类型   默认为文本框
							if("FLOAT".equals(columnType) && StringUtil.isNotEmpty(column.getStr("TABLECOLUMN_LENGTH"))){
								String lengthStr=column.getStr("TABLECOLUMN_LENGTH");
								if(lengthStr.indexOf(",")!=-1){
									rf.set("RESOURCEFIELD_CONFIGINFO",lengthStr.split(",")[1]);  //默认小数精度
								}else{
									rf.set("RESOURCEFIELD_CONFIGINFO",lengthStr);  //默认小数精度
								}
							}else if("NUMBER".equals(columnType)){
								rf.set("RESOURCEFIELD_CONFIGINFO","0");  //默认小数精度
							}
						}else if("DATETIME".equals(columnType)){
							rf.set("RESOURCEFIELD_XTYPE","datetimefield");  //字段类型   默认为文本框
						}else{
							rf.set("RESOURCEFIELD_XTYPE","textfield");  //字段类型   默认为文本框
						}
						rf.set("RESOURCEFIELD_COLSPAN",1);   //所占列数
						rf.set("RESOURCEFIELD_EDITABLE","0");    //可选可编辑   默认否
						rf.set("RESOURCEFIELD_HIDDEN","1");
						rf.set("RESOURCEFIELD_ROWSPAN",1);   //所占行数
						rf.set("RESOURCEFIELD_SYSMODE",fun.getStr("FUNCINFO_SYSMODE"));
						rf.set("RESOURCEFIELD_ISPK","0");
						//根据列的添加方式 字典添加，表添加来快速初始化功能配置项
						if(StringUtil.isNotEmpty(column.getStr("TABLECOLUMN_DICCONFIG"))){
							String dicOtherConfig=column.getStr("TABLECOLUMN_DICQUERYFIELD");
							if(StringUtil.isNotEmpty(dicOtherConfig)){
								if(dicOtherConfig.indexOf("cascadeField")!=-1){
									JSONObject dicConfig=JSONObject.fromObject(dicOtherConfig);
									String where=dicConfig.getString("where");
									String other=dicConfig.getString("other");
									rf.set("RESOURCEFIELD_XTYPE", "cbbfield");
									rf.set("RESOURCEFIELD_CONFIGINFO", column.getStr("TABLECOLUMN_DICCONFIG"));
									rf.set("RESOURCEFIELD_OTHERCONFIG",other);
									rf.set("RESOURCEFIELD_WHERESQL", where);
								}else{
									rf.set("RESOURCEFIELD_XTYPE", "treessfield");
									rf.set("RESOURCEFIELD_CONFIGINFO", column.getStr("TABLECOLUMN_DICCONFIG"));
									rf.set("RESOURCEFIELD_OTHERCONFIG", URLDecoder.decode("%7B%22queryField%22%3A%22"+column.getStr("TABLECOLUMN_DICQUERYFIELD")+"%22%7D "));
								}
							}else{
								rf.set("RESOURCEFIELD_XTYPE", "cbbfield");
								rf.set("RESOURCEFIELD_CONFIGINFO", column.getStr("TABLECOLUMN_DICCONFIG"));
							}
						}else if(StringUtil.isNotEmpty(column.getStr("TABLECOLUMN_QUERYCONFIG"))){
							if(column.getStr("TABLECOLUMN_QUERYCONFIG").startsWith("JE_CORE_QUERYUSER,")){
								rf.set("RESOURCEFIELD_XTYPE", "queryuserfield");
								rf.set("RESOURCEFIELD_CONFIGINFO", column.getStr("TABLECOLUMN_QUERYCONFIG"));
								rf.set("RESOURCEFIELD_OTHERCONFIG", URLDecoder.decode("%7B%22DEPT%22%3A%221%22%2C%22ROLE%22%3A%221%22%2C%22SENTRY%22%3A%221%22%2C%22WORKGROUP%22%3A%221%22%7D"));
							}else{
								rf.set("RESOURCEFIELD_XTYPE", "gridssfield");
								rf.set("RESOURCEFIELD_CONFIGINFO", column.getStr("TABLECOLUMN_QUERYCONFIG"));
							}
						}
						rf.set("SY_CREATETIME", nowDate);
						rf.set("SY_CREATEUSER", user.getUserCode());
						rf.set("SY_CREATEUSERNAME", user.getUsername());
						rf.set("SY_CREATEORG", dept.getDeptCode());
						rf.set("SY_CREATEORGNAME", dept.getDeptName());
						if("1".equals(rf.getStr("RESOURCEFIELD_HIDDEN"))){
							rf.set("RESOURCEFIELD_FIELDLAZY","0");
						}else{
							rf.set("RESOURCEFIELD_FIELDLAZY","1");
						}
						serviceTemplate.insert(rf);
					}
				}
			}
		}
		BeanUtils.getInstance().clearCache(updated.getStr("RESOURCETABLE_TABLECODE"));
		DynaCacheManager.removeCache(updated.getStr("RESOURCETABLE_TABLECODE"));
		return updated;
	}
	/**
	 * 更新表结构
	 * 1.表被创建后表的CODE不能被修改了
	 * 2.字段类型不能修改
	 * 3.字段删除操作会事前强硬的吧数据清空
	 * 4.字段的长度只能变大不能变小
	 * 5.字段和表的注解是可以改的
	 * 6.字段的编码是不可以该的
	 * 7.主外键关系只能添加不能删除和更新
	 * 如果有特殊的修改的话请自己到数据库中修改,但是一定要保证数据库与平台保持一致
	 * @param resourceTableId
	 * @param isFuncs  是否级联更新功能
	 * @return
	 */
	@Override
	public boolean updateTable(String resourceTableId,Boolean isFuncs) {
		boolean success = true;
		try {
			DynaBean resourceTable =serviceTemplate.selectOneByPk("JE_CORE_RESOURCETABLE", resourceTableId);
			Boolean jeCore=("1".equals(resourceTable.getStr("SY_JECORE")));
			String tableCode = resourceTable.getStr("RESOURCETABLE_TABLECODE");
			//清空缓存并传入带有列和键的表资源对象
//			BeanUtils.getInstance().clearCache(tableCode);
//			DynaBean dynaTable=BeanUtils.getInstance().getResourceTable(tableCode);
			List<DynaBean> columns=serviceTemplate.selectList("JE_CORE_TABLECOLUMN", " and TABLECOLUMN_RESOURCETABLE_ID='"+resourceTable.getStr("JE_CORE_RESOURCETABLE_ID")+"' ORDER BY TABLECOLUMN_CLASSIFY ASC,SY_ORDERINDEX ASC");
			List<DynaBean> keys=serviceTemplate.selectList("JE_CORE_TABLEKEY", " and TABLEKEY_RESOURCETABLE_ID='"+resourceTable.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
			List<DynaBean> indexs=serviceTemplate.selectList("JE_CORE_TABLEINDEX", " and TABLEINDEX_RESOURCETABLE_ID='"+resourceTable.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
			//对列进行校验
			String errors=checkColumns(columns,jeCore);
			if(StringUtil.isEmpty(errors)){
				resourceTable.set(BeanUtils.KEY_TABLE_COLUMNS, columns);
			}else{
				throw new PlatformException(errors,PlatformExceptionEnum.JE_CORE_TABLE_CHECKCOLUMN_ERROR,new Object[]{resourceTable});
			}
			//对键进行校验
			errors=checkKeys(keys);
			if(StringUtil.isEmpty(errors)){
				resourceTable.set(BeanUtils.KEY_TABLE_KEYS, keys);
			}else{
				throw new PlatformException(errors,PlatformExceptionEnum.JE_CORE_TABLE_CHECKKEY_ERROR,new Object[]{resourceTable});
			}
			//对索引进行校验
			errors=checkIndexs(indexs);
			if(StringUtil.isEmpty(errors)){
				resourceTable.set(BeanUtils.KEY_TABLE_INDEXS, indexs);
			}else{
				throw new PlatformException(errors,PlatformExceptionEnum.JE_CORE_TABLE_CHECKINDEX_ERROR,new Object[]{resourceTable});
			}
			//获取clob字段
			List<String> arraySql = BuildingSql.getInstance().getDDL4UpdateTable(resourceTable,pcDataService.loadTableColumnBySql(tableCode));
			for(String sql : arraySql){
				try {
					if(StringUtil.isNotEmpty(sql)){
						pcServiceTemplate.executeSql(sql);
					}
				} catch (Exception e) {
					String message=e.getMessage();
					if(e.getCause()!=null){
						message=e.getCause().getMessage();
					}
					if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME)){
						if(message.indexOf("无效的列修改")!=-1 || message.indexOf("数据类型的更改无效")!=-1){
							logger.error("没有变化的修改导致异常,不影响成像运行"+sql);
						}else{
							throw new PlatformException("执行更新表异常",PlatformExceptionEnum.JE_CORE_TABLE_UPDATE_ERROR,new Object[]{sql},e);
						}
					}else if(ConstantVars.STR_SQLSERVER.equals(PCDaoTemplateImpl.DBNAME) && message.indexOf("无法更新或删除属性")!=-1){
						logger.error("没有变化的修改导致异常,不影响成像运行"+sql);
					}else{
						throw new PlatformException("执行更新表异常",PlatformExceptionEnum.JE_CORE_TABLE_UPDATE_ERROR,new Object[]{sql},e);
					}
				}
			}
//			//把字段和键的标示符改掉
			//声明已创建的索引
			Set<String> indexCodes=new HashSet<String>();
			for(DynaBean column:columns){
				if("0".equals(column.getStr("TABLECOLUMN_ISCREATE"))){
					saveTableTrace("JE_CORE_TABLECOLUMN", null,column , "INSERT", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
				}
			}
			for(DynaBean index:indexs){
				indexCodes.add(index.getStr("TABLEINDEX_FIELDCODE"));
				if("0".equals(index.getStr("TABLEINDEX_ISCREATE"))){
					saveTableTrace("JE_CORE_TABLEINDEX", null,index , "INSERT", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
				}
			}
			for(DynaBean key:keys){
				if("0".equals(key.getStr("TABLEKEY_ISCREATE"))){
					saveTableTrace("JE_CORE_TABLEKEY", null,key , "INSERT", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
				}
			}
			/**---------------------------功能中增加字段开始-----------------------------------------*/
			List<DynaBean> funcs=serviceTemplate.selectList("JE_CORE_FUNCINFO", " and FUNCINFO_TABLENAME='"+resourceTable.getStr("RESOURCETABLE_TABLECODE")+"'");
			for(DynaBean fun:funcs){
				String funId=fun.getStr("JE_CORE_FUNCINFO_ID");
				String columnSql = "select RESOURCECOLUMN_CODE from JE_CORE_RESOURCECOLUMN where RESOURCECOLUMN_FUNCINFO_ID = '"+funId+"' and RESOURCECOLUMN_IFIMPL = '1'";
				List<String> columnList = (List<String>) pcServiceTemplate.queryBySql(columnSql);
				String fieldSql = "select RESOURCEFIELD_CODE from JE_CORE_RESOURCEFIELD where RESOURCEFIELD_FUNCINFO_ID = '"+funId+"' and RESOURCEFIELD_IFIMPL = '1'";
				List<String> fieldList = (List<String>) pcServiceTemplate.queryBySql(fieldSql);
				List<Object> maxOrder=(List<Object>) pcServiceTemplate.queryBySql("select max(SY_ORDERINDEX) from JE_CORE_RESOURCECOLUMN where RESOURCECOLUMN_FUNCINFO_ID = '"+funId+"' and RESOURCECOLUMN_IFIMPL = '1'");
				List<Object> fieldMaxOrder=(List<Object>) pcServiceTemplate.queryBySql("select max(SY_ORDERINDEX) from JE_CORE_RESOURCEFIELD where RESOURCEFIELD_FUNCINFO_ID = '"+funId+"' and RESOURCEFIELD_IFIMPL = '1'");
				Integer fieldOrderIndex=0;
				Integer columnOrderIndex=0;
				if(maxOrder!=null && maxOrder.size()>0){
					Object orderObj=maxOrder.iterator().next();
					if(orderObj==null){
						orderObj=0;
					}
					columnOrderIndex=Integer.parseInt(orderObj.toString());
				}
				if(fieldMaxOrder!=null && fieldMaxOrder.size()>0){
					Object orderObj=fieldMaxOrder.iterator().next();
					if(orderObj==null){
						orderObj=0;
					}
					fieldOrderIndex=Integer.parseInt(orderObj.toString());
				}
				EndUser user=SecurityUserHolder.getCurrentUser();
				Department dept=SecurityUserHolder.getCurrentUserDept();
				String nowDate=DateUtils.formatDateTime(new Date());
				for(int i=0;i<columns.size();i++){
					DynaBean column=columns.get(i);
					if("SY__POSTIL".equals(column.getStr("TABLECOLUMN_CODE")) || "SY__MARK".equals(column.getStr("TABLECOLUMN_CODE"))){
						continue;
					}
					if(!columnList.contains(column.getStr("TABLECOLUMN_CODE"))){
						Boolean isUpdate=false;
						if(columnList.contains(column.getStr("TABLECOLUMN_OLDCODE"))){
							isUpdate=true;
						}
						if(!isUpdate){
							DynaBean rc = new DynaBean("JE_CORE_RESOURCECOLUMN",false);
							rc.set(BeanUtils.KEY_PK_CODE, "JE_CORE_RESOURCECOLUMN_ID");
							rc.set("RESOURCECOLUMN_FUNCINFO_ID",fun.getStr("JE_CORE_FUNCINFO_ID"));
							rc.set("RESOURCECOLUMN_CODE",column.get("TABLECOLUMN_CODE"));
							rc.set("RESOURCECOLUMN_NAME",column.get("TABLECOLUMN_NAME"));
							rc.set("RESOURCECOLUMN_IFIMPL","1");    //将导入的字段标识为true
							rc.set("RESOURCECOLUMN_QUICKQUERY","0");  //是否快速查询 false
							rc.set("SY_ORDERINDEX",columnOrderIndex+i+1);
							rc.set("RESOURCECOLUMN_WIDTH","80");    //设置默认宽度为80
							rc.set("RESOURCECOLUMN_WIDTH_EN","80");    //设置默认宽度为80
							rc.set("RESOURCECOLUMN_CONVERSION","0");  //是否拥有转换器conversion
							rc.set("RESOURCECOLUMN_HIGHLIGHTING","0");  //是否拥有转换器highlighting
							rc.set("RESOURCECOLUMN_HYPERLINK","0");     //是否超链接hyperLink
							rc.set("RESOURCECOLUMN_ISDD","0");    //是否在字典中取
							rc.set("RESOURCECOLUMN_MERGE","0");   //合并单元格      否
							rc.set("RESOURCECOLUMN_LOCKED","0");    //锁定列  否
							rc.set("RESOURCECOLUMN_MORECOLUMN","0");  //多表头   否
							rc.set("RESOURCECOLUMN_ISPK","0");
							rc.set("RESOURCECOLUMN_ENABLEICON","0");//启用图标
							rc.set("RESOURCECOLUMN_SYSMODE",fun.getStr("FUNCINFO_SYSMODE"));
							rc.set("RESOURCECOLUMN_ALLOWBLANK","1");   //是否可以为空  true
							rc.set("RESOURCECOLUMN_QUERYTYPE","no");
							rc.set("RESOURCECOLUMN_XTYPE","uxcolumn");  //字段类型  默认为无  不可编辑
							rc.set("RESOURCECOLUMN_ALLOWEDIT","0");    //列表编辑   默认为否
							rc.set("RESOURCECOLUMN_HIDDEN","1");
							rc.set("RESOURCECOLUMN_ALIGN","LEAF");
							if("SY_ACKFLAG".equals(rc.getStr("RESOURCECOLUMN_CODE"))){
								rc.set("RESOURCECOLUMN_LAZYLOAD","1");
							}
							if("SY_FUNCEDIT".equals(rc.getStr("RESOURCECOLUMN_CODE"))){
								rc.set("RESOURCECOLUMN_LAZYLOAD","1");
							}
							if(indexCodes.contains(column.get("TABLECOLUMN_CODE"))){
								rc.set("RESOURCECOLUMN_INDEX","1");
								rc.set("RESOURCECOLUMN_ORDER","1");
							}else{
								rc.set("RESOURCECOLUMN_INDEX","0");
								rc.set("RESOURCECOLUMN_ORDER","0");
							}
							rc.set("SY_CREATETIME", nowDate);
							rc.set("SY_CREATEUSER", user.getUserCode());
							rc.set("SY_CREATEUSERNAME", user.getUsername());
							rc.set("SY_CREATEORG", dept.getDeptCode());
							rc.set("SY_CREATEORGNAME", dept.getDeptName());
							rc.set("RESOURCECOLUMN_DESCRIPTION", column.getStr("TABLECOLUMN_REMARK"));
							serviceTemplate.insert(rc);
						}else{
							//如果是修改列名的情况。 则修改功能中的column列编码
							DynaBean rc =serviceTemplate.selectOne("JE_CORE_RESOURCECOLUMN", " and RESOURCECOLUMN_FUNCINFO_ID='"+fun.getStr("JE_CORE_FUNCINFO_ID")+"' and RESOURCECOLUMN_CODE='"+column.get("TABLECOLUMN_OLDCODE")+"'");
							rc.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_RESOURCECOLUMN");
							rc.set("RESOURCECOLUMN_CODE", column.get("TABLECOLUMN_CODE"));
							rc.set("RESOURCECOLUMN_NAME", column.get("TABLECOLUMN_NAME"));
							if(indexCodes.contains(column.get("TABLECOLUMN_CODE"))){
								rc.set("RESOURCECOLUMN_INDEX","1");
								rc.set("RESOURCECOLUMN_ORDER","1");
							}else{
								rc.set("RESOURCECOLUMN_INDEX","0");
								rc.set("RESOURCECOLUMN_ORDER","0");
							}
							serviceTemplate.update(rc);
						}
					}else if(indexCodes.contains(column.get("TABLECOLUMN_CODE"))){
						DynaBean rc =serviceTemplate.selectOne("JE_CORE_RESOURCECOLUMN", " and RESOURCECOLUMN_FUNCINFO_ID='"+fun.getStr("JE_CORE_FUNCINFO_ID")+"' and RESOURCECOLUMN_CODE='"+column.get("TABLECOLUMN_CODE")+"' AND RESOURCECOLUMN_INDEX!='1'");
						if(rc!=null){
							rc.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_RESOURCECOLUMN");
							rc.set("RESOURCECOLUMN_INDEX","1");
							rc.set("RESOURCECOLUMN_ORDER","1");
							serviceTemplate.update(rc);
						}
					}

					if(!fieldList.contains(column.getStr("TABLECOLUMN_CODE"))){
						Boolean isUpdate=false;
						if(fieldList.contains(column.getStr("TABLECOLUMN_OLDCODE"))){
							isUpdate=true;
						}
						if(!isUpdate){
							DynaBean rf = new DynaBean("JE_CORE_RESOURCEFIELD",false);
							rf.set(BeanUtils.KEY_PK_CODE, "JE_CORE_RESOURCEFIELD_ID");
							rf.set("RESOURCEFIELD_FUNCINFO_ID",fun.getStr("JE_CORE_FUNCINFO_ID"));
							rf.set("RESOURCEFIELD_CODE",column.get("TABLECOLUMN_CODE"));
							rf.set("RESOURCEFIELD_NAME",column.get("TABLECOLUMN_NAME"));
							rf.set("RESOURCEFIELD_IFIMPL","1");   //将导入的字段标识为true
							rf.set("RESOURCEFIELD_DISABLED","1");  //是否可用  true
							rf.set("SY_ORDERINDEX",fieldOrderIndex+i+1);   //默认序号为0
//							if(flag){
//								rf.set("SY_ORDERINDEX",0.0);
//							}
							rf.set("RESOURCEFIELD_ALLOWBLANK","1");   //是否可以为空  true
							rf.set("RESOURCEFIELD_READONLY","0");     //是否只读
							String columnType=column.getStr("TABLECOLUMN_TYPE");
							//数值型
							if("NUMBER".equals(columnType) || "FLOAT".equals(columnType)){
								rf.set("RESOURCEFIELD_XTYPE","numberfield");  //字段类型   默认为文本框
								if("FLOAT".equals(columnType) && StringUtil.isNotEmpty(column.getStr("TABLECOLUMN_LENGTH"))){
									String lengthStr=column.getStr("TABLECOLUMN_LENGTH");
									if(lengthStr.indexOf(",")!=-1){
										rf.set("RESOURCEFIELD_CONFIGINFO",lengthStr.split(",")[1]);  //默认小数精度
									}else{
										rf.set("RESOURCEFIELD_CONFIGINFO",lengthStr);  //默认小数精度
									}
								}else if("NUMBER".equals(columnType)){
									rf.set("RESOURCEFIELD_CONFIGINFO","0");  //默认小数精度
								}
							}else if("FLOAT2".equals(columnType)){
								rf.set("RESOURCEFIELD_XTYPE","numberfield");  //字段类型   默认为文本框
								rf.set("RESOURCEFIELD_CONFIGINFO","2");  //默认小数精度
							}else if("DATE".equals(columnType)){
								rf.set("RESOURCEFIELD_XTYPE","datefield");  //字段类型   默认为文本框
							}else if("DATETIME".equals(columnType)){
								rf.set("RESOURCEFIELD_XTYPE","datetimefield");  //字段类型   默认为文本框
							}else{
								rf.set("RESOURCEFIELD_XTYPE","textfield");  //字段类型   默认为文本框
							}
							rf.set("RESOURCEFIELD_COLSPAN",1);   //所占列数
							rf.set("RESOURCEFIELD_EDITABLE","0");    //可选可编辑   默认否
							rf.set("RESOURCEFIELD_HIDDEN","1");
							rf.set("RESOURCEFIELD_ROWSPAN",1);   //所占行数
							rf.set("RESOURCEFIELD_SYSMODE",fun.getStr("FUNCINFO_SYSMODE"));
							rf.set("RESOURCEFIELD_ISPK","0");
							//根据列的添加方式 字典添加，表添加来快速初始化功能配置项
							if(StringUtil.isNotEmpty(column.getStr("TABLECOLUMN_DICCONFIG"))){
								String dicOtherConfig=column.getStr("TABLECOLUMN_DICQUERYFIELD");
								if(StringUtil.isNotEmpty(dicOtherConfig)){
									if(dicOtherConfig.indexOf("cascadeField")!=-1){
										JSONObject dicConfig=JSONObject.fromObject(dicOtherConfig);
										String where=dicConfig.getString("where");
										String other=dicConfig.getString("other");
										rf.set("RESOURCEFIELD_XTYPE", "cbbfield");
										rf.set("RESOURCEFIELD_CONFIGINFO", column.getStr("TABLECOLUMN_DICCONFIG"));
										rf.set("RESOURCEFIELD_OTHERCONFIG",other);
										rf.set("RESOURCEFIELD_WHERESQL", where);
									}else{
										rf.set("RESOURCEFIELD_XTYPE", "treessfield");
										rf.set("RESOURCEFIELD_CONFIGINFO", column.getStr("TABLECOLUMN_DICCONFIG"));
										rf.set("RESOURCEFIELD_OTHERCONFIG", URLDecoder.decode("%7B%22queryField%22%3A%22"+column.getStr("TABLECOLUMN_DICQUERYFIELD")+"%22%7D "));
									}
								}else{
									rf.set("RESOURCEFIELD_XTYPE", "cbbfield");
									rf.set("RESOURCEFIELD_CONFIGINFO", column.getStr("TABLECOLUMN_DICCONFIG"));
								}
							}else if(StringUtil.isNotEmpty(column.getStr("TABLECOLUMN_QUERYCONFIG"))){
								if(column.getStr("TABLECOLUMN_QUERYCONFIG").startsWith("JE_CORE_QUERYUSER,")){
									rf.set("RESOURCEFIELD_XTYPE", "queryuserfield");
									rf.set("RESOURCEFIELD_CONFIGINFO", column.getStr("TABLECOLUMN_QUERYCONFIG"));
									rf.set("RESOURCEFIELD_OTHERCONFIG", URLDecoder.decode("%7B%22DEPT%22%3A%221%22%2C%22SENTRY%22%3A%221%22%2C%22ROLE%22%3A%221%22%7D"));
								}else{
									rf.set("RESOURCEFIELD_XTYPE", "gridssfield");
									rf.set("RESOURCEFIELD_CONFIGINFO", column.getStr("TABLECOLUMN_QUERYCONFIG"));
								}
							}
							if("SY_WARNFLAG".equals(column.get("TABLECOLUMN_CODE"))){
								rf.set("RESOURCEFIELD_XTYPE", "cbbfield");
								rf.set("RESOURCEFIELD_CONFIGINFO", "JE_WARNFLAG");
							}else if("SY_ZHMC".equals(column.get("TABLECOLUMN_CODE"))){
								rf.set("RESOURCEFIELD_VALUE", "@USER_ZHMC@");
							}else if("SY_ZHID".equals(column.get("TABLECOLUMN_CODE"))){
								rf.set("RESOURCEFIELD_VALUE", "@USER_ZHID@");
							}else if("SY_ACKFLAG".equals(column.get("TABLECOLUMN_CODE"))){
								rf.set("RESOURCEFIELD_READONLY","1");     //是否只读
								rf.set("RESOURCEFIELD_VALUE","0");
								rf.set("RESOURCEFIELD_XTYPE","rgroup");  //字段类型   默认为文本框
								rf.set("RESOURCEFIELD_CONFIGINFO","SY_ACKFLAG,SY_ACKFLAG,code,S");  //字段类型   默认为文本框
							}
							rf.set("SY_CREATETIME", nowDate);
							rf.set("SY_CREATEUSER", user.getUserCode());
							rf.set("SY_CREATEUSERNAME", user.getUsername());
							rf.set("SY_CREATEORG", dept.getDeptCode());
							rf.set("SY_CREATEORGNAME", dept.getDeptName());
							if("1".equals(rf.getStr("RESOURCEFIELD_HIDDEN"))){
								rf.set("RESOURCEFIELD_FIELDLAZY","0");
							}else{
								rf.set("RESOURCEFIELD_FIELDLAZY","1");
							}
							rf.set("RESOURCEFIELD_DESCRIPTION", column.getStr("TABLECOLUMN_REMARK"));
							serviceTemplate.insert(rf);
						}else{
							//如果是修改列名的情况。 则修改功能中的column列编码
							DynaBean rc =serviceTemplate.selectOne("JE_CORE_RESOURCEFIELD", " and RESOURCEFIELD_FUNCINFO_ID='"+fun.getStr("JE_CORE_FUNCINFO_ID")+"' and RESOURCEFIELD_CODE='"+column.get("TABLECOLUMN_OLDCODE")+"'");
							rc.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_RESOURCEFIELD");
							rc.set("RESOURCEFIELD_CODE", column.get("TABLECOLUMN_CODE"));
							rc.set("RESOURCEFIELD_NAME", column.get("TABLECOLUMN_NAME"));
							serviceTemplate.update(rc);
						}
					}
				}
			}
			//设置键和字段全为已经创建   在功能字段处理之后
			pcServiceTemplate.executeSql("UPDATE JE_CORE_TABLECOLUMN SET TABLECOLUMN_ISCREATE='1',TABLECOLUMN_OLDCODE=TABLECOLUMN_CODE,TABLECOLUMN_OLDUNIQUE=TABLECOLUMN_UNIQUE,TABLECOLUMN_TABLECODE='"+tableCode+"' WHERE TABLECOLUMN_RESOURCETABLE_ID='"+resourceTableId+"'");
			pcServiceTemplate.executeSql("UPDATE JE_CORE_TABLEKEY SET TABLEKEY_ISCREATE='1',TABLEKEY_TABLECODE='"+tableCode+"' WHERE TABLEKEY_RESOURCETABLE_ID='"+resourceTableId+"'");
			pcServiceTemplate.executeSql("UPDATE JE_CORE_TABLEINDEX SET TABLEINDEX_ISCREATE='1',TABLEINDEX_TABLECODE='"+tableCode+"' WHERE TABLEINDEX_RESOURCETABLE_ID='"+resourceTableId+"'");
			pcServiceTemplate.executeSql("UPDATE JE_CORE_TABLETRACE SET TABLETRACE_SFYY='1' WHERE TABLETRACE_RESOURCETABLE_ID='"+resourceTableId+"' and TABLETRACE_SFYY='0'");
			/**---------------------------功能中增加字典结束-----------------------------------------*/
			keys=serviceTemplate.selectList("JE_CORE_TABLEKEY", " and TABLEKEY_RESOURCETABLE_ID='"+resourceTableId+"'");
			resourceTable.set("RESOURCETABLE_OLDTABLECODE", resourceTable.getStr("RESOURCETABLE_TABLECODE"));
			resourceTable.set("RESOURCETABLE_PKCODE","");
			updateTableKey(resourceTable, keys);
			BeanUtils.getInstance().clearCache(resourceTable.getStr("RESOURCETABLE_TABLECODE"));
			DynaCacheManager.removeCache(resourceTable.getStr("RESOURCETABLE_TABLECODE"));
			developLogManager.doDevelopLog("APPLY","应用","TABLE","资源表",resourceTable.getStr("RESOURCETABLE_TABLENAME"),resourceTable.getStr("RESOURCETABLE_TABLECODE"),resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
		}catch (Exception e) {
//			e.printStackTrace();
//			String message=e.getMessage();
//			if(e.getCause()!=null){
//				message=e.getCause().getMessage().replaceAll("\n", "").replaceAll("'", "");
//			}
			success = false;
			if(e instanceof PlatformException){
				throw e;
			}else {
				throw new PlatformException("表更新应用异常",PlatformExceptionEnum.JE_CORE_TABLE_UPDATE_ERROR,new Object[]{resourceTableId,isFuncs},e);
			}

		}
		return success;
	}
	/**
	 * 同步外键图标样式
	 */
	public void updateTableKey(DynaBean  resourceTable,List<DynaBean> keys){
		String tableCode = resourceTable.getStr("RESOURCETABLE_TABLECODE");
		List<String> parentTableCodes=new ArrayList<String>();
		for(DynaBean key :keys){
			if("Foreign".equals(key.get("TABLEKEY_TYPE"))){
				parentTableCodes.add(key.getStr("TABLEKEY_LINKTABLE"));
			}else if("Primary".equals(key.getStr("TABLEKEY_TYPE"))){
				resourceTable.set("RESOURCETABLE_PKCODE", key.getStr("TABLEKEY_COLUMNCODE"));
			}
		}
		String oldParentTaleCodes=resourceTable.getStr("RESOURCETABLE_PARENTTABLECODES","");
		List<String> oldParentTables=new ArrayList<String>();
		for(String oldParent:oldParentTaleCodes.split(",")){
			if(StringUtil.isNotEmpty(oldParent)){
				oldParentTables.add(oldParent);
			}
		}
		//设置其用到了外键
		for(String tC:parentTableCodes){
			if(!oldParentTables.contains(tC)){
				oldParentTables.add(tC);
			}
		}
		//更新父表关联信息
		for(String parentTableCode:oldParentTables){
			DynaBean fatherTable=serviceTemplate.selectOne("JE_CORE_RESOURCETABLE"," and RESOURCETABLE_TABLECODE='"+parentTableCode+"'");
			//如果存在外键
			String childTableCodes = fatherTable.getStr("RESOURCETABLE_CHILDTABLECODES","");
			String childParentTables=fatherTable.getStr("RESOURCETABLE_PARENTTABLECODES","");
			if(parentTableCodes.contains(parentTableCode)){
				if(StringUtil.isNotEmpty(childTableCodes)){
//						//不是空的操作
					if(childTableCodes.indexOf(tableCode) == -1){
						childTableCodes = childTableCodes +","+tableCode;
					}
					fatherTable.set("RESOURCETABLE_CHILDTABLECODES",childTableCodes);
				}else{
					fatherTable.set("RESOURCETABLE_CHILDTABLECODES",tableCode);
				}
				//处理表类型并赋值图标
				if(!"TREE".equals(fatherTable.getStr("RESOURCETABLE_TYPE"))){
					if(StringUtils.isNotEmpty(childParentTables)){
						fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-main-subtable");
					}else{
						fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-master-table");
					}
				}
			}else{
				List<String> parentChildCodes=new ArrayList<String>();
				for(String childCode:childTableCodes.split(",")){
					if(StringUtil.isNotEmpty(childCode)){
						parentChildCodes.add(childCode);
					}
				}
				parentChildCodes.remove(tableCode);
				fatherTable.set("RESOURCETABLE_CHILDTABLECODES",StringUtil.buildSplitString(ArrayUtils.getArray(parentChildCodes),","));
				//处理表类型并赋值图标
				if(!"TREE".equals(fatherTable.getStr("RESOURCETABLE_TYPE"))){
					if(StringUtils.isNotEmpty(fatherTable.getStr("RESOURCETABLE_PARENTTABLECODES")) && parentChildCodes.size()>0){
						fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-main-subtable");
					}else if(StringUtils.isNotEmpty(fatherTable.getStr("RESOURCETABLE_PARENTTABLECODES")) && parentChildCodes.size()<=0){
						fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-child-table");
					}else if(parentChildCodes.size()>0){
						fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-master-table");
					} else{
						fatherTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-general-table");
					}
				}
			}
			serviceTemplate.update(fatherTable);
		}
		resourceTable.set("RESOURCETABLE_PARENTTABLECODES",StringUtil.buildSplitString(ArrayUtils.getArray(parentTableCodes),","));
		if(!"TREE".equals(resourceTable.getStr("RESOURCETABLE_TYPE"))){
			if(StringUtils.isNotEmpty(resourceTable.getStr("RESOURCETABLE_CHILDTABLECODES")) && StringUtil.isNotEmpty(resourceTable.getStr("RESOURCETABLE_PARENTTABLECODES"))){
				resourceTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-main-subtable");
			}else if(StringUtils.isEmpty(resourceTable.getStr("RESOURCETABLE_CHILDTABLECODES")) && StringUtil.isNotEmpty(resourceTable.getStr("RESOURCETABLE_PARENTTABLECODES"))){
				resourceTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-child-table");
			}else if(StringUtils.isNotEmpty(resourceTable.getStr("RESOURCETABLE_CHILDTABLECODES")) && StringUtil.isEmpty(resourceTable.getStr("RESOURCETABLE_PARENTTABLECODES"))){
				resourceTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-master-table");
			}else{
				resourceTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-general-table");
			}
		}
		if(parentTableCodes.size()>0){
			resourceTable.set("RESOURCETABLE_ISUSEFOREIGNKEY","1");
		}
		resourceTable.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_RESOURCETABLE");
		serviceTemplate.update(resourceTable);
	}

	/**
	 * 物理删除指定表的字段
	 * @param tableCode 暂不明确
	 * @param columns 暂不明确
	 * @param isDdl 暂不明确
	 */
	@Override
	public void deleteColumn(String tableCode, List<DynaBean> columns,Boolean isDdl) {
		// TODO Auto-generated method stub
		//删除表字段
		if(isDdl){
			List<String> delSqls=BuildingSql.getInstance().getDeleteColumnSql(tableCode, columns);
			for(String delSql:delSqls){
				if(StringUtil.isNotEmpty(delSql)){
					pcServiceTemplate.executeSql(delSql);
				}
			}
		}
		List<String> columnCodes=new ArrayList<String>();
		String[] delIds=new String[columns.size()];
		for(int i=0;i<columns.size();i++){
			DynaBean tc=columns.get(i);
			columnCodes.add(tc.getStr("TABLECOLUMN_CODE"));
			delIds[i]=tc.getStr("JE_CORE_TABLECOLUMN_ID");
		}
		pcServiceTemplate.executeSql("DELETE FROM JE_CORE_TABLECOLUMN WHERE JE_CORE_TABLECOLUMN_ID IN ("+StringUtil.buildArrayToString(delIds)+")");
		BeanUtils.getInstance().clearCache(tableCode);
		List<DynaBean> funcs=serviceTemplate.selectList("JE_CORE_FUNCINFO", " and FUNCINFO_TABLENAME='"+tableCode+"'");
		for(DynaBean funInfo:funcs){
			List<DynaBean> funColumns=serviceTemplate.selectList("JE_CORE_RESOURCECOLUMN","  and RESOURCECOLUMN_FUNCINFO_ID='"+funInfo.getStr("JE_CORE_FUNCINFO_ID")+"'");
			List<DynaBean> funFields=serviceTemplate.selectList("JE_CORE_RESOURCEFIELD","  and RESOURCEFIELD_FUNCINFO_ID='"+funInfo.getStr("JE_CORE_FUNCINFO_ID")+"'");
			//删除功能表单字段
			Boolean flag=false;
			List<String> ids=new ArrayList<String>();
			for(DynaBean rc:funColumns){
				if(columnCodes.contains(rc.getStr("RESOURCECOLUMN_CODE"))){
					flag=true;
					ids.add(rc.getStr("JE_CORE_RESOURCECOLUMN_ID"));
				}
			}
			if(flag){
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_RESOURCECOLUMN WHERE JE_CORE_RESOURCECOLUMN_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(ids))+")");
			}
			Boolean fieldFlag=false;
			List<String> fieldIds=new ArrayList<String>();
			//删除功能列表字段
			for(DynaBean rf:funFields){
				if(columnCodes.contains(rf.getStr("RESOURCEFIELD_CODE"))){
					fieldFlag=true;
					fieldIds.add(rf.getStr("JE_CORE_RESOURCEFIELD_ID"));
				}
			}
			if(fieldFlag){
				pcServiceTemplate.executeSql("DELETE FROM JE_CORE_RESOURCEFIELD WHERE JE_CORE_RESOURCEFIELD_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(fieldIds))+")");
			}
		}
	}

	/**
	 * 物理删除指定表的键
	 * @param tableCode 表名字
	 * @param keys TODO 暂不明确
	 * @param ddl 是否删除
	 */
	@Override
	public void deleteKey(String tableCode, List<DynaBean> keys,String ddl) {
		// TODO Auto-generated method stub
		DynaBean table=serviceTemplate.selectOne("JE_CORE_RESOURCETABLE", " AND RESOURCETABLE_TYPE!='MODULE' AND RESOURCETABLE_TABLECODE='"+tableCode+"'");
		//删除表字段
		String delSql=BuildingSql.getInstance().getDeleteKeySql(tableCode, keys);
		List<String> columnCodes=new ArrayList<String>();
		String[] delIds=new String[keys.size()];
		for(int i=0;i<keys.size();i++){
			DynaBean tc=keys.get(i);
			columnCodes.add(tc.getStr("TABLEKEY_CODE"));
			delIds[i]=tc.getStr("JE_CORE_TABLEKEY_ID");
		}
		if(StringUtil.isNotEmpty(delSql) && table!=null && !TableType.VIEWTABLE.equals(table.getStr("RESOURCETABLE_TYPE"))){
			if("1".equals(ddl)){
				pcServiceTemplate.executeSql(delSql);
			}
		}
		pcServiceTemplate.executeSql("DELETE FROM JE_CORE_TABLEKEY WHERE JE_CORE_TABLEKEY_ID IN ("+StringUtil.buildArrayToString(delIds)+")");
	}

	/**
	 * 物理删除指定表的索引
	 * @param tableCode 表名
	 * @param indexs 索引
	 */
	@Override
	public void deleteIndex(String tableCode, List<DynaBean> indexs) {
		// TODO Auto-generated method stub
		//删除表字段
		String delSql=BuildingSql.getInstance().getDeleteIndexSql(tableCode, indexs);
		String[] delIds=new String[indexs.size()];
		for(int i=0;i<indexs.size();i++){
			DynaBean tc=indexs.get(i);
			delIds[i]=tc.getStr("JE_CORE_TABLEINDEX_ID");
		}
		if(StringUtil.isNotEmpty(delSql)){
			try{
				pcServiceTemplate.executeSql(delSql);
			}catch(Exception e){
				throw new PlatformException("删除执行索引出错",PlatformExceptionEnum.JE_CORE_TABLEINDEX_DELETE_ERROR,new Object[]{tableCode,delSql},e);
			}
		}
		pcServiceTemplate.executeSql("DELETE FROM JE_CORE_TABLEINDEX WHERE JE_CORE_TABLEINDEX_ID IN ("+StringUtil.buildArrayToString(delIds)+")");
	}

	/**
	 * 初始化表格列信息
	 * @param resourceTable 修改表的信息
	 * @param isTree 是否根节点
	 */
	@Override
	public void initColumns(DynaBean resourceTable, Boolean isTree) {
		// TODO Auto-generated method stub
		String tableCode=resourceTable.getStr("RESOURCETABLE_TABLECODE");
		List<DynaBean> columns=new ArrayList<DynaBean>();
		/**审核标记*/
		DynaBean audFlag = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(audFlag, "SY_AUDFLAG", "审核标记", 311);
		audFlag.set("TABLECOLUMN_TYPE", "VARCHAR");
		audFlag.set("TABLECOLUMN_LENGTH", "20");
		audFlag.set("TABLECOLUMN_NAME_EN", "Audit Flag");
		columns.add(audFlag);
		/**登记者所在部门主键*/
		DynaBean createOrgId = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(createOrgId, "SY_CREATEORGID", "登记部门主键", 312);
		createOrgId.set("TABLECOLUMN_TYPE", "VARCHAR50");
		createOrgId.set("TABLECOLUMN_LENGTH", "");
		createOrgId.set("TABLECOLUMN_NAME_EN", "Dept ID");
		columns.add(createOrgId);
		/**登记者所在部门编码*/
		DynaBean createOrg = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(createOrg, "SY_CREATEORG", "登记部门编码", 313);
		createOrg.set("TABLECOLUMN_TYPE", "VARCHAR50");
		createOrg.set("TABLECOLUMN_LENGTH", "");
		createOrg.set("TABLECOLUMN_NAME_EN", "Dept Code");
		columns.add(createOrg);
		/**登记者所在部门名称*/
		DynaBean createOrgName = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(createOrgName, "SY_CREATEORGNAME", "登记部门", 314);
		createOrgName.set("TABLECOLUMN_TYPE", "VARCHAR50");
		createOrgName.set("TABLECOLUMN_LENGTH", "");
		createOrgName.set("TABLECOLUMN_NAME_EN", "Dept");
		columns.add(createOrgName);
		/**登记时间*/
		DynaBean createTime = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(createTime, "SY_CREATETIME", "登记时间", 315);
		createTime.set("TABLECOLUMN_TYPE", "DATETIME");
		createTime.set("TABLECOLUMN_NAME_EN", "Create Time");
		columns.add(createTime);
		/**登记人主键*/
		DynaBean createUserId = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(createUserId, "SY_CREATEUSERID", "登记人主键", 316);
		createUserId.set("TABLECOLUMN_TYPE", "VARCHAR50");
		createUserId.set("TABLECOLUMN_LENGTH", "");
		createUserId.set("TABLECOLUMN_NAME_EN", "User ID");
		columns.add(createUserId);
		/**登记人编码*/
		DynaBean createUser = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(createUser, "SY_CREATEUSER", "登记人编码", 317);
		createUser.set("TABLECOLUMN_TYPE", "VARCHAR50");
		createUser.set("TABLECOLUMN_LENGTH", "");
		createUser.set("TABLECOLUMN_NAME_EN", "User Code");
		columns.add(createUser);
		/**登记人姓名*/
		DynaBean createUserName = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(createUserName, "SY_CREATEUSERNAME", "登记人", 318);
		createUserName.set("TABLECOLUMN_TYPE", "VARCHAR50");
		createUserName.set("TABLECOLUMN_LENGTH", "");
		createUserName.set("TABLECOLUMN_NAME_EN", "User");
		columns.add(createUserName);
		/**数据状态*/
		DynaBean status = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(status, "SY_STATUS", "数据状态", 319);
		status.set("TABLECOLUMN_TYPE", "YESORNO");
		status.set("TABLECOLUMN_NAME_EN", "Data Status");
		columns.add(status);
		/**排序字段*/
		DynaBean orderIndex = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(orderIndex, "SY_ORDERINDEX", "排序字段", 320);
		orderIndex.set("TABLECOLUMN_NAME_EN", "Sort Field");
		if(isTree){
			orderIndex.set("TABLECOLUMN_TREETYPE", TreeNodeType.ORDERINDEX.toString());
		}
		columns.add(orderIndex);
		/**流程实例ID*/
		DynaBean PIID = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(PIID, "SY_PIID", "流程实例ID", 321);
		PIID.set("TABLECOLUMN_NAME_EN", "Process Case ID");
		columns.add(PIID);
		/**流程定义ID*/
		DynaBean PDID = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(PDID, "SY_PDID", "流程定义ID", 322);
		PDID.set("TABLECOLUMN_NAME_EN", "Process Definition ID");
		columns.add(PDID);
		/**集团公司名称*/
		DynaBean JTGSMC = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(JTGSMC, "SY_JTGSMC", "集团公司名称", 323);
		JTGSMC.set("TABLECOLUMN_TYPE", "VARCHAR50");
		JTGSMC.set("TABLECOLUMN_LENGTH", "");
		JTGSMC.set("TABLECOLUMN_NAME_EN", "Group Company Name");
		columns.add(JTGSMC);
		/**集团公司ID*/
		DynaBean JTGSID = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(JTGSID, "SY_JTGSID", "集团公司ID", 324);
		JTGSID.set("TABLECOLUMN_TYPE", "VARCHAR50");
		JTGSID.set("TABLECOLUMN_LENGTH", "");
		JTGSID.set("TABLECOLUMN_NAME_EN", "Group Company ID");
		columns.add(JTGSID);
		/**ID字段*/
		DynaBean id = new DynaBean("JE_CORE_TABLECOLUMN",false);
		id.set("SY_ORDERINDEX",300);
		id.set("TABLECOLUMN_CODE",tableCode+"_ID");
		id.set("TABLECOLUMN_TYPE","ID");
		id.set("TABLECOLUMN_NAME","主键ID");
		id.set("TABLECOLUMN_ISNULL","0");
		id.set("TABLECOLUMN_CLASSIFY","SYS");
		id.set("TABLECOLUMN_UNIQUE","1");
		id.set("TABLECOLUMN_NAME_EN","Primary Key");
		if(isTree){
			id.set("TABLECOLUMN_TREETYPE",TreeNodeType.ID.toString());
		}else{
			id.set("TABLECOLUMN_TREETYPE","NORMAL");
		}
		columns.add(id);
		/**树形字段设置*/
		if(isTree){
			String prefix=tableCode.substring(tableCode.lastIndexOf("_")+1);
			//父节点
			DynaBean parent = new DynaBean("JE_CORE_TABLECOLUMN",false);
			parent.set("SY_ORDERINDEX",301);
			parent.set("TABLECOLUMN_TREETYPE",TreeNodeType.PARENT.toString());
			parent.set("TABLECOLUMN_CODE","SY_PARENT");
			parent.set("TABLECOLUMN_TYPE","VARCHAR50");
			parent.set("TABLECOLUMN_NAME","父节点ID");
			parent.set("TABLECOLUMN_LENGTH","");
			parent.set("TABLECOLUMN_ISNULL","1");
			parent.set("TABLECOLUMN_CLASSIFY","SYS");
			parent.set("TABLECOLUMN_NAME_EN","Parent Node ID");
			columns.add(parent);
			//节点类型， 根、叶子、普通  区分
			DynaBean leaf = new DynaBean("JE_CORE_TABLECOLUMN",false);
			leaf.set("SY_ORDERINDEX",302);
			leaf.set("TABLECOLUMN_TREETYPE",TreeNodeType.NODETYPE.toString());
			leaf.set("TABLECOLUMN_CODE","SY_NODETYPE");
			leaf.set("TABLECOLUMN_TYPE","VARCHAR50");
			leaf.set("TABLECOLUMN_NAME","节点类型");
			leaf.set("TABLECOLUMN_LENGTH","");
			leaf.set("TABLECOLUMN_ISNULL","1");
			leaf.set("TABLECOLUMN_CLASSIFY","SYS");
			leaf.set("TABLECOLUMN_NAME_EN","Node Type");
			columns.add(leaf);
			//父亲路径
			DynaBean nodeInfo = new DynaBean("JE_CORE_TABLECOLUMN",false);
			nodeInfo.set("SY_ORDERINDEX",303);
			nodeInfo.set("TABLECOLUMN_TREETYPE",TreeNodeType.PARENTPATH.toString());
			nodeInfo.set("TABLECOLUMN_CODE","SY_PARENTPATH");
			nodeInfo.set("TABLECOLUMN_TYPE","VARCHAR1000");
			nodeInfo.set("TABLECOLUMN_LENGTH","");
			nodeInfo.set("TABLECOLUMN_NAME","父节点路径");
			nodeInfo.set("TABLECOLUMN_ISNULL","1");
			nodeInfo.set("TABLECOLUMN_CLASSIFY","SYS");
			nodeInfo.set("TABLECOLUMN_NAME_EN","Parent Node Path");
			columns.add(nodeInfo);
			//节点名称
			DynaBean text = new DynaBean("JE_CORE_TABLECOLUMN",false);
			text.set("SY_ORDERINDEX",1);
			text.set("TABLECOLUMN_TREETYPE",TreeNodeType.TEXT.toString());
			text.set("TABLECOLUMN_CODE",prefix+"_TEXT");
			text.set("TABLECOLUMN_TYPE","VARCHAR255");
			text.set("TABLECOLUMN_NAME","节点名称");
			text.set("TABLECOLUMN_ISNULL","1");
			text.set("TABLECOLUMN_CLASSIFY","PRO");
			columns.add(text);
			//节点编码
			DynaBean code = new DynaBean("JE_CORE_TABLECOLUMN",false);
			code.set("SY_ORDERINDEX",2);
			code.set("TABLECOLUMN_TREETYPE",TreeNodeType.CODE.toString());
			code.set("TABLECOLUMN_CODE",prefix+"_CODE");
			code.set("TABLECOLUMN_TYPE","VARCHAR255");
			code.set("TABLECOLUMN_NAME","节点编码");
			code.set("TABLECOLUMN_ISNULL","1");
			code.set("TABLECOLUMN_CLASSIFY","PRO");
			columns.add(code);
			//层次
			DynaBean layer = new DynaBean("JE_CORE_TABLECOLUMN",false);
			layer.set("SY_ORDERINDEX",306);
			layer.set("TABLECOLUMN_TREETYPE",TreeNodeType.LAYER.toString());
			layer.set("TABLECOLUMN_CODE","SY_LAYER");
			layer.set("TABLECOLUMN_NAME","层次");
			layer.set("TABLECOLUMN_TYPE","NUMBER");
			layer.set("TABLECOLUMN_LENGTH","");
			layer.set("TABLECOLUMN_ISNULL","1");
			layer.set("TABLECOLUMN_CLASSIFY","SYS");
			layer.set("TABLECOLUMN_NAME_EN","Layer");
			columns.add(layer);
			//树形路径
			DynaBean path = new DynaBean("JE_CORE_TABLECOLUMN",false);
			path.set("SY_ORDERINDEX",307);
			path.set("TABLECOLUMN_TREETYPE",TreeNodeType.NODEPATH.toString());
			path.set("TABLECOLUMN_CODE","SY_PATH");
			path.set("TABLECOLUMN_TYPE","VARCHAR1000");
			path.set("TABLECOLUMN_LENGTH","");
			path.set("TABLECOLUMN_NAME","树形路径");
			path.set("TABLECOLUMN_ISNULL","1");
			path.set("TABLECOLUMN_CLASSIFY","SYS");
			path.set("TABLECOLUMN_NAME_EN","Tree Path");
			columns.add(path);
			//是否启用
			DynaBean disabled = new DynaBean("JE_CORE_TABLECOLUMN",false);
			disabled.set("SY_ORDERINDEX",308);
			disabled.set("TABLECOLUMN_TREETYPE",TreeNodeType.DISABLED.toString());
			disabled.set("TABLECOLUMN_CODE","SY_DISABLED");
			disabled.set("TABLECOLUMN_TYPE","YESORNO");
			disabled.set("TABLECOLUMN_LENGTH","");
			disabled.set("TABLECOLUMN_NAME","是否启用");
			disabled.set("TABLECOLUMN_ISNULL","1");
			disabled.set("TABLECOLUMN_CLASSIFY","SYS");
			disabled.set("TABLECOLUMN_NAME_EN","Enable");
			columns.add(disabled);
			//树形排序
			DynaBean treeOrderIndex = new DynaBean("JE_CORE_TABLECOLUMN",false);
			treeOrderIndex.set("SY_ORDERINDEX",309);
			treeOrderIndex.set("TABLECOLUMN_TREETYPE",TreeNodeType.TREEORDERINDEX.toString());
			treeOrderIndex.set("TABLECOLUMN_CODE","SY_TREEORDERINDEX");
			treeOrderIndex.set("TABLECOLUMN_TYPE","VARCHAR");
			if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME)){
				treeOrderIndex.set("TABLECOLUMN_LENGTH","383");
			}else{
				treeOrderIndex.set("TABLECOLUMN_LENGTH","900");
			}
			treeOrderIndex.set("TABLECOLUMN_NAME","树形排序字段");
			treeOrderIndex.set("TABLECOLUMN_ISNULL","1");
			treeOrderIndex.set("TABLECOLUMN_CLASSIFY","SYS");
			treeOrderIndex.set("TABLECOLUMN_NAME_EN","Tree Sort Field");
			columns.add(treeOrderIndex);
		}
		EndUser user=SecurityUserHolder.getCurrentUser();
		Department dept=SecurityUserHolder.getCurrentUserDept();
		String nowDate=DateUtils.formatDateTime(new Date());
		for(DynaBean column:columns){
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("SY_CREATETIME", nowDate);
			column.set("SY_CREATEUSER", user.getUserCode());
			column.set("SY_CREATEUSERNAME", user.getUsername());
			column.set("SY_CREATEORG", dept.getDeptCode());
			column.set("SY_CREATEORGNAME", dept.getDeptName());
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set("TABLECOLUMN_TABLECODE", tableCode);
			if(!"1".equals(column.getStr("TABLECOLUMN_UNIQUE",""))){
				column.set("TABLECOLUMN_UNIQUE", "0");
			}
			column.set("TABLECOLUMN_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
			serviceTemplate.insert(column);
		}
	}
	/**
	 * 初始化修改信息
	 * @param resourceTable
	 */
	@Override
	public void initUpdateColumns(DynaBean resourceTable){
		String tableCode=resourceTable.getStr("RESOURCETABLE_TABLECODE");
		List<DynaBean> columns=new ArrayList<DynaBean>();
		/**修改人部门编码*/
		DynaBean modifyOrgId = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(modifyOrgId, "SY_MODIFYORGID", "修改人部门主键", 325);
		modifyOrgId.set("TABLECOLUMN_TYPE", "VARCHAR50");
		modifyOrgId.set("TABLECOLUMN_LENGTH", "");
		modifyOrgId.set("TABLECOLUMN_NAME_EN","Modifier Dept ID");
		columns.add(modifyOrgId);
		/**修改人部门编码*/
		DynaBean modifyOrg = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(modifyOrg, "SY_MODIFYORG", "修改人部门编码", 326);
		modifyOrg.set("TABLECOLUMN_TYPE", "VARCHAR50");
		modifyOrg.set("TABLECOLUMN_LENGTH", "");
		modifyOrg.set("TABLECOLUMN_NAME_EN","Modifier Dept CODE");
		columns.add(modifyOrg);
		/**修改人部门*/
		DynaBean modifyOrgName = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(modifyOrgName, "SY_MODIFYORGNAME", "修改人部门", 327);
		modifyOrgName.set("TABLECOLUMN_TYPE", "VARCHAR50");
		modifyOrgName.set("TABLECOLUMN_LENGTH", "");
		modifyOrgName.set("TABLECOLUMN_NAME_EN","Modifier Dept");
		columns.add(modifyOrgName);
		/**修改人主键*/
		DynaBean modifyUserId = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(modifyUserId, "SY_MODIFYUSERID", "修改人主键", 328);
		modifyUserId.set("TABLECOLUMN_TYPE", "VARCHAR50");
		modifyUserId.set("TABLECOLUMN_LENGTH", "");
		modifyUserId.set("TABLECOLUMN_NAME_EN","Modifier User ID");
		columns.add(modifyUserId);
		/**修改人编码*/
		DynaBean modifyUser = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(modifyUser, "SY_MODIFYUSER", "修改人编码", 329);
		modifyUser.set("TABLECOLUMN_TYPE", "VARCHAR50");
		modifyUser.set("TABLECOLUMN_LENGTH", "");
		modifyUser.set("TABLECOLUMN_NAME_EN","Modifier User Code");
		columns.add(modifyUser);
		/**修改人*/
		DynaBean modifyUserName = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(modifyUserName, "SY_MODIFYUSERNAME", "修改人", 330);
		modifyUserName.set("TABLECOLUMN_TYPE", "VARCHAR50");
		modifyUserName.set("TABLECOLUMN_LENGTH", "");
		modifyUserName.set("TABLECOLUMN_NAME_EN","Modifier User");
		columns.add(modifyUserName);
		/**修改时间*/
		DynaBean modifyTime = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(modifyTime, "SY_MODIFYTIME", "修改时间", 331);
		modifyTime.set("TABLECOLUMN_TYPE", "DATETIME");
		modifyTime.set("TABLECOLUMN_NAME_EN","Modify Time");
		columns.add(modifyTime);
		EndUser user=SecurityUserHolder.getCurrentUser();
		Department dept=SecurityUserHolder.getCurrentUserDept();
		String nowDate=DateUtils.formatDateTime(new Date());
		for(DynaBean column:columns){
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("SY_CREATETIME", nowDate);
			column.set("SY_CREATEUSER", user.getUserCode());
			column.set("SY_CREATEUSERNAME", user.getUsername());
			column.set("SY_CREATEORG", dept.getDeptCode());
			column.set("SY_CREATEORGNAME", dept.getDeptName());
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set("TABLECOLUMN_TABLECODE", tableCode);
			if(!"1".equals(column.getStr("TABLECOLUMN_UNIQUE",""))){
				column.set("TABLECOLUMN_UNIQUE", "0");
			}
			column.set("TABLECOLUMN_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
			serviceTemplate.insert(column);
		}
	}
	/**
	 * 初始化修改信息
	 * @param resourceTable
	 */
	@Override
	public void initShColumns(DynaBean resourceTable){
		String tableCode=resourceTable.getStr("RESOURCETABLE_TABLECODE");
		List<DynaBean> columns=new ArrayList<DynaBean>();
		/**审核标识*/
		DynaBean shFlag = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(shFlag, "SY_ACKFLAG", "审核标识", 325);
		shFlag.set("TABLECOLUMN_TYPE", "YESORNO");
		shFlag.set("TABLECOLUMN_LENGTH", "");
		shFlag.set("TABLECOLUMN_NAME_EN","");
		columns.add(shFlag);
		/**审核人*/
		DynaBean shUserName = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(shUserName, "SY_ACKUSERNAME", "审核人", 326);
		shUserName.set("TABLECOLUMN_TYPE", "VARCHAR255");
		shUserName.set("TABLECOLUMN_LENGTH", "");
		shUserName.set("TABLECOLUMN_NAME_EN","");
		columns.add(shUserName);
		/**修改人部门*/
		DynaBean shUserId = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(shUserId, "SY_ACKUSERID", "审核人ID", 327);
		shUserId.set("TABLECOLUMN_TYPE", "VARCHAR255");
		shUserId.set("TABLECOLUMN_LENGTH", "");
		shUserId.set("TABLECOLUMN_NAME_EN","");
		columns.add(shUserId);
		/**修改人主键*/
		DynaBean shTime = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(shTime, "SY_ACKTIME", "审核时间", 328);
		shTime.set("TABLECOLUMN_TYPE", "DATETIME");
		shTime.set("TABLECOLUMN_LENGTH", "");
		shTime.set("TABLECOLUMN_NAME_EN","");
		columns.add(shTime);
		EndUser user=SecurityUserHolder.getCurrentUser();
		Department dept=SecurityUserHolder.getCurrentUserDept();
		String nowDate=DateUtils.formatDateTime(new Date());
		for(DynaBean column:columns){
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("SY_CREATETIME", nowDate);
			column.set("SY_CREATEUSER", user.getUserCode());
			column.set("SY_CREATEUSERNAME", user.getUsername());
			column.set("SY_CREATEORG", dept.getDeptCode());
			column.set("SY_CREATEORGNAME", dept.getDeptName());
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set("TABLECOLUMN_TABLECODE", tableCode);
			if(!"1".equals(column.getStr("TABLECOLUMN_UNIQUE",""))){
				column.set("TABLECOLUMN_UNIQUE", "0");
			}
			column.set("TABLECOLUMN_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
			serviceTemplate.insert(column);
		}
	}

	/**
	 * 初始化工作流信息
	 * @param resourceTable 修改表的信息
	 */
	@Override
	public void initProcessColumns(DynaBean resourceTable) {
		// TODO Auto-generated method stub
		if("1".equals(resourceTable.getStr("RESOURCETABLE_IMPLWF"))){
			return;
		}
		String tableCode=resourceTable.getStr("RESOURCETABLE_TABLECODE");
		List<DynaBean> columns=new ArrayList<DynaBean>();
		/**流程启动人*/
		DynaBean startUser = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(startUser, "SY_STARTEDUSER", "流程启动人ID", 332);
		startUser.set("TABLECOLUMN_NAME_EN","Starter ID");
		columns.add(startUser);
		/**流程启动人*/
		DynaBean startUserName = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(startUserName, "SY_STARTEDUSERNAME", "流程启动人", 333);
		startUserName.set("TABLECOLUMN_NAME_EN","Starter");
		columns.add(startUserName);
		/**流程已执行人*/
		DynaBean approvedUser = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(approvedUser, "SY_APPROVEDUSERS", "流程已执行人ID", 334);
		approvedUser.set("TABLECOLUMN_NAME_EN","Executor ID");
		approvedUser.set("TABLECOLUMN_TYPE","CLOB");
		approvedUser.set("TABLECOLUMN_LENGTH","");
		columns.add(approvedUser);
		/**流程已执行人*/
		DynaBean approvedUserName = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(approvedUserName, "SY_APPROVEDUSERNAMES", "流程已执行人", 335);
		approvedUserName.set("TABLECOLUMN_NAME_EN","Executor");
		approvedUserName.set("TABLECOLUMN_TYPE","CLOB");
		approvedUserName.set("TABLECOLUMN_LENGTH","");
		columns.add(approvedUserName);
		/**流程已执行人*/
		DynaBean preapprovUser = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(preapprovUser, "SY_PREAPPROVUSERS", "流程待执行人ID", 336);
		preapprovUser.set("TABLECOLUMN_NAME_EN","Waiting ID");
		preapprovUser.set("TABLECOLUMN_TYPE","VARCHAR");
		preapprovUser.set("TABLECOLUMN_LENGTH","1000");
		columns.add(preapprovUser);
		/**流程已执行人*/
		DynaBean preapprovUserName = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(preapprovUserName, "SY_PREAPPROVUSERNAMES", "流程待执行人", 337);
		preapprovUserName.set("TABLECOLUMN_NAME_EN","Waiting");
		preapprovUserName.set("TABLECOLUMN_TYPE","VARCHAR");
		preapprovUserName.set("TABLECOLUMN_LENGTH","1000");
		columns.add(preapprovUserName);
		/**流程任务指派人*/
		DynaBean lastFlowUser = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(lastFlowUser, "SY_LASTFLOWUSER", "流程任务指派人", 338);
		lastFlowUser.set("TABLECOLUMN_NAME_EN","Waiting");
		lastFlowUser.set("TABLECOLUMN_TYPE","VARCHAR");
		lastFlowUser.set("TABLECOLUMN_LENGTH","1000");
		columns.add(lastFlowUser);
		DynaBean lastFlowUserId = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(lastFlowUserId, "SY_LASTFLOWUSERID", "流程任务指派人ID", 338);
		lastFlowUserId.set("TABLECOLUMN_NAME_EN","Waiting");
		lastFlowUserId.set("TABLECOLUMN_TYPE","VARCHAR");
		lastFlowUserId.set("TABLECOLUMN_LENGTH","1000");
		columns.add(lastFlowUserId);
		/**流程预警延期信息*/
		DynaBean warnFlowUser = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(warnFlowUser, "SY_WFWARN", "流程预警延期信息", 339);
		warnFlowUser.set("TABLECOLUMN_NAME_EN","Warning");
		warnFlowUser.set("TABLECOLUMN_TYPE","CLOB");
		warnFlowUser.set("TABLECOLUMN_LENGTH","");
		columns.add(warnFlowUser);
		/**流程预警延期标识*/
		DynaBean warnFlag= new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(warnFlag, "SY_WARNFLAG", "流程预警延期标识", 340);//0正常，1预警，2延期
		warnFlag.set("TABLECOLUMN_NAME_EN","Warning Flag");
		warnFlag.set("TABLECOLUMN_TYPE","YESORNO");
		warnFlag.set("TABLECOLUMN_LENGTH","");
		columns.add(warnFlag);
		/**流程预警延期标识*/
		DynaBean currentTask= new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(currentTask, "SY_CURRENTTASK", "当前执行节点", 340);//0正常，1预警，2延期
		currentTask.set("TABLECOLUMN_NAME_EN","Warning Flag");
		currentTask.set("TABLECOLUMN_TYPE","VARCHAR255");
		currentTask.set("TABLECOLUMN_LENGTH","");
		columns.add(currentTask);

		EndUser user=SecurityUserHolder.getCurrentUser();
		Department dept=SecurityUserHolder.getCurrentUserDept();
		String nowDate=DateUtils.formatDateTime(new Date());
		for(DynaBean column:columns){
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("SY_CREATETIME", nowDate);
			column.set("SY_CREATEUSER", user.getUserCode());
			column.set("SY_CREATEUSERNAME", user.getUsername());
			column.set("SY_CREATEORG", dept.getDeptCode());
			column.set("SY_CREATEORGNAME", dept.getDeptName());
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set("TABLECOLUMN_TABLECODE", tableCode);
			if(!"1".equals(column.getStr("TABLECOLUMN_UNIQUE",""))){
				column.set("TABLECOLUMN_UNIQUE", "0");
			}
			column.set("TABLECOLUMN_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
			serviceTemplate.insert(column);
		}
		resourceTable.set("RESOURCETABLE_IMPLWF", "1");
		serviceTemplate.update(resourceTable);
	}

	/**
	 * 初始化租户信息
	 * @param resourceTable 修改表的信息
	 */
	@Override
	public void initSaasColumns(DynaBean resourceTable) {
		// TODO Auto-generated method stub
		String tableCode=resourceTable.getStr("RESOURCETABLE_TABLECODE");
		List<DynaBean> columns=new ArrayList<DynaBean>();
		/**流程启动人*/
		DynaBean zhId = new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(zhId, "SY_ZHID", "租户ID", 23);
		zhId.set("TABLECOLUMN_NAME_EN","SaasUser ID");
		zhId.set("TABLECOLUMN_TYPE","VARCHAR50");
		zhId.set("TABLECOLUMN_LENGTH","");
		zhId.set("TABLECOLUMN_NAME_EN","SaasUser ID");
		columns.add(zhId);
		DynaBean zhMc= new DynaBean("JE_CORE_TABLECOLUMN",false);
		buildColumn(zhMc, "SY_ZHMC", "租户名称", 23);
		zhMc.set("TABLECOLUMN_NAME_EN","SaasUser Name");
		zhMc.set("TABLECOLUMN_TYPE","VARCHAR100");
		zhMc.set("TABLECOLUMN_LENGTH","");
		columns.add(zhMc);
		EndUser user=SecurityUserHolder.getCurrentUser();
		Department dept=SecurityUserHolder.getCurrentUserDept();
		String nowDate=DateUtils.formatDateTime(new Date());
		for(DynaBean column:columns){
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("SY_CREATETIME", nowDate);
			column.set("SY_CREATEUSER", user.getUserCode());
			column.set("SY_CREATEUSERNAME", user.getUsername());
			column.set("SY_CREATEORG", dept.getDeptCode());
			column.set("SY_CREATEORGNAME", dept.getDeptName());
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set("TABLECOLUMN_TABLECODE", tableCode);
			if(!"1".equals(column.getStr("TABLECOLUMN_UNIQUE",""))){
				column.set("TABLECOLUMN_UNIQUE", "0");
			}
			column.set("TABLECOLUMN_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
			serviceTemplate.insert(column);
		}
	}

	/**
	 * 初始化拓展字段
	 * @param resourceTable 修改表的信息
	 */
	@Override
	public void initExtendColumns(DynaBean resourceTable) {
		String tableCode=resourceTable.getStr("RESOURCETABLE_TABLECODE");
		EndUser user=SecurityUserHolder.getCurrentUser();
		Department dept=SecurityUserHolder.getCurrentUserDept();
		String nowDate=DateUtils.formatDateTime(new Date());
		int num = 10;
		//默十个自定义字段
		for (int i = 1; i <= num; i++) {
			String rowNum =  String.format("%02d", i);
			DynaBean column = new DynaBean("JE_CORE_TABLECOLUMN",false);
			buildColumn(column, "SY_EXTEND"+rowNum, "扩展字段"+rowNum, 200+i);
			column.set("TABLECOLUMN_TYPE","VARCHAR100");
			column.set("TABLECOLUMN_LENGTH","");
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("SY_CREATETIME", nowDate);
			column.set("SY_CREATEUSER", user.getUserCode());
			column.set("SY_CREATEUSERNAME", user.getUsername());
			column.set("SY_CREATEORG", dept.getDeptCode());
			column.set("SY_CREATEORGNAME", dept.getDeptName());
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set("TABLECOLUMN_TABLECODE", tableCode);
			if(!"1".equals(column.getStr("TABLECOLUMN_UNIQUE",""))){
				column.set("TABLECOLUMN_UNIQUE", "0");
			}
			column.set("TABLECOLUMN_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
			serviceTemplate.insert(column);
		}
	}

	/**
	 * 初始化表格键信息
	 * @param resourceTable 修改表的地方
	 * @param isTree 是否根节点
	 */
	@Override
	public void initKeys(DynaBean resourceTable, Boolean isTree) {
		// TODO Auto-generated method stub
		/**ID的键设置*/
		List<DynaBean> keys = new ArrayList<DynaBean>();
		DynaBean pk = new DynaBean("JE_CORE_TABLEKEY",false);
		pk.set("TABLEKEY_CODE",resourceTable.get("RESOURCETABLE_TABLECODE")+"_ID_CODE");
		pk.set("TABLEKEY_COLUMNCODE",resourceTable.get("RESOURCETABLE_TABLECODE")+"_ID");
		pk.set("TABLEKEY_TYPE","Primary");
		pk.set("TABLEKEY_ISRESTRAINT","1");
		pk.set("TABLEKEY_CHECKED","1");
		pk.set("TABLEKEY_CLASSIFY","SYS");
		pk.set("SY_ORDERINDEX",0);
		keys.add(pk);
		if(isTree){
			DynaBean forenignKey = new DynaBean("JE_CORE_TABLEKEY",false);
			forenignKey.set("TABLEKEY_CODE","JE_"+DateUtils.getUniqueTime()+"_PARENT");
			forenignKey.set("TABLEKEY_COLUMNCODE","SY_PARENT");
			forenignKey.set("TABLEKEY_TYPE","Inline");
			forenignKey.set("TABLEKEY_CHECKED","0");
			forenignKey.set("TABLEKEY_LINKTABLE",resourceTable.get("RESOURCETABLE_TABLECODE"));
			forenignKey.set("TABLEKEY_LINECOLUMNCODE",resourceTable.get("RESOURCETABLE_TABLECODE")+"_ID");
			forenignKey.set("TABLEKEY_LINETYLE","Noaction");
			forenignKey.set("TABLEKEY_ISRESTRAINT","0");
			forenignKey.set("TABLEKEY_CLASSIFY","SYS");
			forenignKey.set("SY_ORDERINDEX",1);
			keys.add(forenignKey);
		}
		EndUser user=SecurityUserHolder.getCurrentUser();
		Department dept=SecurityUserHolder.getCurrentUserDept();
		String nowDate=DateUtils.formatDateTime(new Date());
		for(DynaBean key:keys){
			key.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			key.set("SY_CREATETIME", nowDate);
			key.set("SY_CREATEUSER", user.getUserCode());
			key.set("SY_CREATEUSERNAME", user.getUsername());
			key.set("SY_CREATEORG", dept.getDeptCode());
			key.set("SY_CREATEORGNAME", dept.getDeptName());
			key.set("TABLEKEY_ISCREATE", "0");
			key.set("TABLEKEY_TABLECODE", resourceTable.get("RESOURCETABLE_TABLECODE"));
			key.set("TABLEKEY_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
			serviceTemplate.insert(key);
		}
	}

	/**
	 * 初始化表格键信息
	 * @param resourceTable 修改表的地方
	 * @param isTree 是否根节点
	 */
	@Override
	public void initIndexs(DynaBean resourceTable, Boolean isTree) {
		// TODO Auto-generated method stub
		/**ID的键设置*/
		List<DynaBean> indexs = new ArrayList<DynaBean>();
		DynaBean pkIndex = new DynaBean("JE_CORE_TABLEINDEX",false);
		pkIndex.set("TABLEINDEX_NAME","JE_"+DateUtils.getUniqueTime()+"_ID");
		pkIndex.set("TABLEINDEX_FIELDCODE",resourceTable.get("RESOURCETABLE_TABLECODE")+"_ID");
		pkIndex.set("TABLEINDEX_FIELDNAME","主键ID");
		pkIndex.set("TABLEINDEX_ISCREATE","1");
		pkIndex.set("TABLEINDEX_UNIQUE","1");
		pkIndex.set("TABLEINDEX_CLASSIFY","SYS");
		pkIndex.set("SY_ORDERINDEX",0);
		indexs.add(pkIndex);
		DynaBean orderIndex = new DynaBean("JE_CORE_TABLEINDEX",false);
		orderIndex.set("TABLEINDEX_NAME","JE_"+DateUtils.getUniqueTime()+"_ORDER");
		orderIndex.set("TABLEINDEX_FIELDCODE","SY_ORDERINDEX");
		orderIndex.set("TABLEINDEX_FIELDNAME","排序字段");
		orderIndex.set("TABLEINDEX_ISCREATE","0");
		orderIndex.set("TABLEINDEX_UNIQUE","0");
		orderIndex.set("TABLEINDEX_CLASSIFY","SYS");
		orderIndex.set("SY_ORDERINDEX",1);
		indexs.add(orderIndex);
//		if(isTree){
//			DynaBean forenignIndex = new DynaBean("JE_CORE_TABLEINDEX",false);
//			forenignIndex.set("TABLEINDEX_NAME","JE_"+DateUtils.getUniqueTime()+"_ID");
//			forenignIndex.set("TABLEINDEX_FIELDCODE","SY_PARENT");
//			forenignIndex.set("TABLEINDEX_FIELDNAME","父节点ID");
//			forenignIndex.set("TABLEINDEX_ISCREATE","0");
//			forenignIndex.set("TABLEINDEX_UNIQUE","0");
//			forenignIndex.set("TABLEINDEX_CLASSIFY","SYS");
//			forenignIndex.set("SY_ORDERINDEX",2);
//			indexs.add(forenignIndex);
//			DynaBean parentPathIndex = new DynaBean("JE_CORE_TABLEINDEX",false);
//			parentPathIndex.set("TABLEINDEX_NAME","JE_"+DateUtils.getUniqueTime()+"_PATH");
//			parentPathIndex.set("TABLEINDEX_FIELDCODE","SY_PARENTPATH");
//			parentPathIndex.set("TABLEINDEX_FIELDNAME","父节点路径");
//			parentPathIndex.set("TABLEINDEX_ISCREATE","0");
//			parentPathIndex.set("TABLEINDEX_UNIQUE","0");
//			parentPathIndex.set("TABLEINDEX_CLASSIFY","SYS");
//			parentPathIndex.set("SY_ORDERINDEX",3);
//			indexs.add(parentPathIndex);
//		}
		EndUser user=SecurityUserHolder.getCurrentUser();
		Department dept=SecurityUserHolder.getCurrentUserDept();
		String nowDate=DateUtils.formatDateTime(new Date());
		for(DynaBean index:indexs){
			index.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			index.set("SY_CREATETIME", nowDate);
			index.set("SY_CREATEUSER", user.getUserCode());
			index.set("SY_CREATEUSERNAME", user.getUsername());
			index.set("SY_CREATEORG", dept.getDeptCode());
			index.set("SY_CREATEORGNAME", dept.getDeptName());
			index.set("TABLEINDEX_ISCREATE", "0");
			index.set("TABLEINDEX_TABLECODE", resourceTable.get("RESOURCETABLE_TABLECODE"));
			index.set("TABLEINDEX_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
			serviceTemplate.insert(index);
		}
	}
	/**
	 * 辅助函数初始化列(传址操作)
	 * @param tableColumn
	 * @param code
	 * @param Name
	 * @param orderIndex
	 */
	private void buildColumn(DynaBean tableColumn ,String code,String Name ,int orderIndex){
		tableColumn.set("TABLECOLUMN_CODE",code);
		tableColumn.set("TABLECOLUMN_NAME",Name);
		tableColumn.set("TABLECOLUMN_TREETYPE", "NORMAL");
		if(code.equals("SY_ORDERINDEX")){
			tableColumn.set("TABLECOLUMN_TYPE","NUMBER");
			tableColumn.set("TABLECOLUMN_LENGTH","20");
		}else{
			tableColumn.set("TABLECOLUMN_TYPE","VARCHAR255");
		}
		tableColumn.set("SY_ORDERINDEX",orderIndex);
		tableColumn.set("TABLECOLUMN_ISNULL","1");
		tableColumn.set("TABLECOLUMN_CLASSIFY","SYS");
	}

	/**
	 * 生成创建语句SQL和数据插入SQL
	 * @param table 表信息
	 * @param type 表类型
	 * @return
	 */
	public String generateSql(DynaBean table,String type){
		StringBuffer sql=new StringBuffer();
		String tableCode=table.getStr("RESOURCETABLE_TABLECODE");
		BeanUtils.getInstance().clearCache(tableCode);
		DynaCacheManager.removeCache(tableCode);
		DynaBean resourceTable=BeanUtils.getInstance().getResourceTable(tableCode);
		if("TABLE".equals(type)){
			sql.append("--"+table.getStr("RESOURCETABLE_TABLENAME")+"("+table.getStr("RESOURCETABLE_TABLECODE")+") 表结构的创建语句\n\n");
			List<String> arraySql = BuildingSql.getInstance().getDDL4CreateTable(resourceTable);
			for(String cSql:arraySql){
				sql.append(cSql+";\n");
			}
		}else if("DATA".equals(type)){
			sql.append("--"+table.getStr("RESOURCETABLE_TABLENAME")+"("+table.getStr("RESOURCETABLE_TABLECODE")+")表数据插入SQL\n\n");
			List<DynaBean> datas=serviceTemplate.selectList(tableCode, "");
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
			for(DynaBean bean:datas){
				StringBuilder insertSql = new StringBuilder();
				String fieldNames = BeanUtils.getInstance().getNames4Sql(resourceTable);
				StringBuilder values = new StringBuilder();
				//根据ResourceTable得到DyanBean
				DynaBean dynaBean = BeanUtils.getInstance().getDynaBeanByResourceTable(resourceTable);
				String[] arrayName = BeanUtils.getInstance().getNames(dynaBean);
				for(String name : arrayName){
					if(!name.startsWith(StringUtil.DOLLAR)) {
						if(bean.get(name)==null){
							values.append("NULL,");
						}else if(numberFields.contains(name)){
							values.append(""+bean.get(name)+",");
						}else{
							values.append("'"+bean.get(name)+"',");
						}
					}
				}
				values.setLength(values.length()-1);
				insertSql.append("INSERT INTO "+tableCode+"("+fieldNames+") VALUES ("+values+");\n");
				sql.append(insertSql);
			}
		}
		return sql.toString();
	}

	/**
	 * 构建表数据的数据留痕
	 * @param tableCode  操作的表编码
	 * @param oldBean	 原实体
	 * @param newBean   新实体
	 * @param oper   	 操作
	 */
	@Override
	public void saveTableTrace(String tableCode,DynaBean oldBean,DynaBean newBean,String oper,String tableId){
		DynaBean resourceTable=BeanUtils.getInstance().getResourceTable(tableCode);
		List<DynaBean> columns=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		Department currentDept=SecurityUserHolder.getCurrentUserDept();
		String nowTime=DateUtils.formatDateTime(new Date());
		StringBuffer context=new StringBuffer();
		context.append("<table width=100% border=1 style='text-align:center;border-collapse: collapse;border-spacing: 0;'>");
		context.append("<tr>");
		context.append("<td width=100 style='background:#cccccc;'><b>中文名</b></td>");
		context.append("<td width=100 style='background:#cccccc;'><b>编码</b></td>");
		if("INSERT".equals(oper) || "DELETE".equals(oper)){
			context.append("<td width=150 style='background:#cccccc;'><b>值</b></td>");
		}else if("UPDATE".equals(oper)){
			context.append("<td width=150 style='background:#cccccc;'><b>更新前</b></td>");
			context.append("<td width=150 style='background:#cccccc;'><b>更新后</b></td>");
		}
		context.append("</tr>");
		DynaBean trace=new DynaBean("JE_CORE_TABLETRACE",false);
		trace.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLETRACE_ID");
		if("INSERT".equals(oper)){
			trace.set("TABLETRACE_OPER", "添加");
			for(DynaBean column:columns){
				if(column.getStr("TABLECOLUMN_CODE").startsWith("SY_")){
					continue;
				}
				if(!StringUtil.isNotEmpty(newBean.getStr(column.getStr("TABLECOLUMN_CODE")))){
					continue;
				}
				context.append("<tr><td>"+column.getStr("TABLECOLUMN_NAME")+"</td><td>"+column.getStr("TABLECOLUMN_CODE")+"</td><td><font color=red>"+newBean.getStr(column.getStr("TABLECOLUMN_CODE"))+"</font></td></tr>");
			}
			trace.set("TABLETRACE_SFYY", "1");
		}else if("UPDATE".equals(oper)){
			trace.set("TABLETRACE_OPER", "修改");
			for(DynaBean column:columns){
				if(column.getStr("TABLECOLUMN_CODE").startsWith("SY_")){
					continue;
				}
				if(!newBean.containsKey(column.getStr("TABLECOLUMN_CODE")) || oldBean.getStr(column.getStr("TABLECOLUMN_CODE"),"").equals(newBean.getStr(column.getStr("TABLECOLUMN_CODE"),""))){
					continue;
				}
				context.append("<tr><td>"+column.getStr("TABLECOLUMN_NAME")+"</td><td>"+column.getStr("TABLECOLUMN_CODE")+"</td><td><font color=red>"+oldBean.getStr(column.getStr("TABLECOLUMN_CODE"))+"</font></td><td><font color=red>"+newBean.getStr(column.getStr("TABLECOLUMN_CODE"))+"</font></td></tr>");
			}
			trace.set("TABLETRACE_SFYY", "0");
		}else if("DELETE".equals(oper)){
			trace.set("TABLETRACE_OPER", "删除");
			for(DynaBean column:columns){
				if(column.getStr("TABLECOLUMN_CODE").startsWith("SY_")){
					continue;
				}
				if(StringUtil.isEmpty(oldBean.getStr(column.getStr("TABLECOLUMN_CODE")))){
					continue;
				}
				context.append("<tr><td>"+column.getStr("TABLECOLUMN_NAME")+"</td><td>"+column.getStr("TABLECOLUMN_CODE")+"</td><td><font color=red>"+oldBean.getStr(column.getStr("TABLECOLUMN_CODE"))+"</font></td></tr>");
			}
			trace.set("TABLETRACE_SFYY", "1");
		}
		context.append("</table>");
		if("JE_CORE_RESOURCETABLE".equalsIgnoreCase(tableCode)){
			trace.set("TABLETRACE_TABLENAME", "资源表");
		}else if("JE_CORE_TABLECOLUMN".equalsIgnoreCase(tableCode)){
			trace.set("TABLETRACE_TABLENAME", "表格列");
		}else if("JE_CORE_TABLEKEY".equalsIgnoreCase(tableCode)){
			trace.set("TABLETRACE_TABLENAME", "表格键");
		}else if("JE_CORE_TABLEINDEX".equalsIgnoreCase(tableCode)){
			trace.set("TABLETRACE_TABLENAME", "索引");
		}
		trace.set("TABLETRACE_TABLECODE", tableCode);
		trace.set("TABLETRACE_CZRNAME", currentUser.getUsername());
		trace.set("TABLETRACE_CZRCODE", currentUser.getUserCode());
		trace.set("TABLETRACE_CZSJ", nowTime);
		trace.set("TABLETRACE_RESOURCETABLE_ID", tableId);
		trace.set("TABLETRACE_XGNR", context.toString());
		trace.set("SY_CREATETIME", nowTime);
		trace.set("SY_CREATEUSER", currentUser.getUserCode());
		trace.set("SY_CREATEUSERNAME", currentUser.getUsername());
		trace.set("SY_CREATEORG", currentDept.getDeptCode());
		trace.set("SY_CREATEORGNAME", currentDept.getDeptName());
		serviceTemplate.insert(trace);
	}

	/**
	 * 粘贴表操作
	 * @param newTableCode 被操作表
	 * @param dynaBean TODO 暂不明确
	 * @param useNewName 用户名字 (黏贴)
	 * @return
	 */
	@Override
	public DynaBean pasteTable(String newTableCode, DynaBean dynaBean,String useNewName) {
		// TODO Auto-generated method stub
		Boolean useNew=false;
		if("1".equals(useNewName)){
			useNew=true;
		}
		DynaBean sourceTable=serviceTemplate.selectOneByPk("JE_CORE_RESOURCETABLE", dynaBean.getStr("JE_CORE_RESOURCETABLE_ID"));
		String oldTableCode=dynaBean.getStr("RESOURCETABLE_TABLECODE");
		String oldPkName=sourceTable.getStr("RESOURCETABLE_PKCODE");
		String newPkName=newTableCode+"_ID";
		if(StringUtil.isNotEmpty(dynaBean.getStr("SY_PATH"))){
			String uuid = JEUUID.uuid();
			sourceTable.setStr("JE_CORE_RESOURCETABLE_ID", uuid);
			sourceTable.set("SY_PATH", dynaBean.getStr("SY_PATH")+"/"+uuid);
			sourceTable.set("SY_PARENT", dynaBean.get("SY_PARENT"));
			sourceTable.set("SY_NODETYPE", dynaBean.get("SY_NODETYPE"));
			sourceTable.set("SY_LAYER", dynaBean.get("SY_LAYER"));
			sourceTable.set("SY_DISABLED", dynaBean.get("SY_DISABLED"));
		}
		sourceTable.set("RESOURCETABLE_ISCREATE", "0");
		sourceTable.set("RESOURCETABLE_TABLENAME", "Copy from :"+sourceTable.getStr("RESOURCETABLE_TABLENAME"));
		sourceTable.set("RESOURCETABLE_TABLECODE", newTableCode);
		sourceTable.set("RESOURCETABLE_PKCODE", newPkName);
		if(!"TREE".equals(sourceTable.getStr("RESOURCETABLE_ICONCLS"))){
			sourceTable.set("RESOURCETABLE_ICONCLS", "jeicon jeicon-general-table");
		}
		sourceTable.set("RESOURCETABLE_CHILDTABLECODES", "");
		sourceTable.set("RESOURCETABLE_ISUSEFOREIGNKEY", "0");
		sourceTable.set("RESOURCETABLE_OLDTABLECODE", "");
		sourceTable.set("RESOURCETABLE_PARENTTABLECODES", "");
		sourceTable.set("RESOURCETABLE_USEFUNC", "");
		sourceTable.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_RESOURCETABLE");
		List<DynaBean> columns=serviceTemplate.selectList("JE_CORE_TABLECOLUMN", " and TABLECOLUMN_RESOURCETABLE_ID='"+dynaBean.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		List<DynaBean> keys=serviceTemplate.selectList("JE_CORE_TABLEKEY", " and TABLEKEY_RESOURCETABLE_ID='"+dynaBean.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		List<DynaBean> indexs=serviceTemplate.selectList("JE_CORE_TABLEINDEX", " and TABLEINDEX_RESOURCETABLE_ID='"+dynaBean.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		DynaBean pasted=serviceTemplate.insert(sourceTable);
		String oldTablePre=oldTableCode;
		if(oldTableCode.lastIndexOf("_")!=-1){
			if(oldTableCode.lastIndexOf("_")==oldTableCode.length()-1){
				oldTablePre="";
			}else{
				oldTablePre=oldTableCode.substring(oldTableCode.lastIndexOf("_")+1);
			}
		}
		String newTablePre=newTableCode;
		if(newTableCode.lastIndexOf("_")!=-1){
			if(newTableCode.lastIndexOf("_")==newTableCode.length()-1){
				newTablePre="";
			}else{
				newTablePre=newTableCode.substring(newTableCode.lastIndexOf("_")+1);
			}
		}
		for(DynaBean column:columns){
			if(oldPkName.equals(column.getStr("TABLECOLUMN_CODE"))){//主键使用新表主键
				column.set("TABLECOLUMN_CODE", newPkName);
			}else{
				if(useNew){
					String columnCode=column.getStr("TABLECOLUMN_CODE");
					if(columnCode.startsWith(oldTablePre+"_")){
						columnCode=newTablePre+columnCode.substring(columnCode.indexOf("_"));
					}
					column.set("TABLECOLUMN_CODE", columnCode);
				}
			}
			column.set("TABLECOLUMN_RESOURCETABLE_ID", pasted.getStr("JE_CORE_RESOURCETABLE_ID"));
			column.set("TABLECOLUMN_TABLECODE", pasted.getStr("RESOURCETABLE_TABLECODE"));
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_TABLECOLUMN");
			column.set("JE_CORE_TABLECOLUMN_ID", null);
			serviceTemplate.insert(column);
		}
		for(Integer i=0;i<keys.size();i++){
			DynaBean key=keys.get(i);
			if("Inline".equals(key.getStr("TABLEKEY_TYPE")) || "Primary".equals(key.getStr("TABLEKEY_TYPE"))){
				if("Inline".equals(key.getStr("TABLEKEY_TYPE"))){
					key.set("TABLEKEY_CODE", "JE_"+DateUtils.getUniqueTime()+"_PARENT");
					key.set("TABLEKEY_LINKTABLE", newTableCode);
					key.set("TABLEKEY_LINECOLUMNCODE", newPkName);
				}else{
					key.set("TABLEKEY_CODE", key.getStr("TABLEKEY_CODE").replace(oldTableCode, newTableCode));
					key.set("TABLEKEY_COLUMNCODE", newPkName);
				}
			}else{
				key.set("TABLEKEY_CODE", "JE_"+DateUtils.getUniqueTime());
				if(useNew){
					String columnCode=key.getStr("TABLEKEY_COLUMNCODE");
					if(columnCode.startsWith(oldTablePre+"_")){
						columnCode=newTablePre+columnCode.substring(columnCode.indexOf("_"));
					}
					key.set("TABLEKEY_COLUMNCODE", columnCode);
				}
			}
			key.set("TABLEKEY_ISCREATE", "0");
			key.set("JE_CORE_TABLEKEY_ID",null);
			key.set("TABLEKEY_RESOURCETABLE_ID", pasted.getStr("JE_CORE_RESOURCETABLE_ID"));
			key.set("TABLEKEY_TABLECODE", pasted.getStr("RESOURCETABLE_TABLECODE"));
			key.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_TABLEKEY");
			serviceTemplate.insert(key);
		}
		for(Integer i=0;i<indexs.size();i++){
			DynaBean index=indexs.get(i);
			if(oldPkName.equals(index.getStr("TABLEINDEX_FIELDCODE"))){
				index.set("TABLEINDEX_NAME", "JE_"+DateUtils.getUniqueTime()+"_ID");
				index.set("TABLEINDEX_FIELDCODE", newPkName);
			}else if("SY_ORDERINDEX".equals(index.getStr("TABLEINDEX_FIELDCODE"))){
				index.set("TABLEINDEX_NAME", "JE_"+DateUtils.getUniqueTime()+"_ORDER");
				index.set("TABLEINDEX_FIELDCODE", "SY_ORDERINDEX");
			}else{
				index.set("TABLEINDEX_NAME", "JE_"+DateUtils.getUniqueTime());
				if(useNew){
					String columnCode=index.getStr("TABLEINDEX_FIELDCODE");
					if(columnCode.startsWith(oldTablePre+"_")){
						columnCode=newTablePre+columnCode.substring(columnCode.indexOf("_"));
					}
					index.set("TABLEINDEX_FIELDCODE", columnCode);
				}
			}
			index.set("TABLEINDEX_ISCREATE", "0");
			index.set("JE_CORE_TABLEINDEX_ID",null);
			index.set("TABLEINDEX_RESOURCETABLE_ID", pasted.getStr("JE_CORE_RESOURCETABLE_ID"));
			index.set("TABLEINDEX_TABLECODE", pasted.getStr("RESOURCETABLE_TABLECODE"));
			index.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_TABLEINDEX");
			serviceTemplate.insert(index);
		}
		return sourceTable;
	}

	/**
	 * 创建索引
	 * @param funcInfo 功能
	 * @param columnCode 列编码
	 * @param columnId 列ID
	 * @return
	 */
	@Override
	public DynaBean createIndexByColumn(DynaBean funcInfo,DynaBean resourceTable, String columnCode,String columnId) {
		String tableCode=funcInfo.getStr("FUNCINFO_TABLENAME");
		if(TableType.VIEWTABLE.equals(resourceTable.getStr("RESOURCETABLE_TYPE"))){
			throw new PlatformException("视图无法创建索引!",PlatformExceptionEnum.JE_CORE_DB_COLUMN_TABLE_ERROR);
		}
		DynaBean column=serviceTemplate.selectOne("JE_CORE_TABLECOLUMN"," AND TABLECOLUMN_CODE='"+columnCode+"' AND TABLECOLUMN_RESOURCETABLE_ID='"+resourceTable.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		String type=column.getStr("TABLECOLUMN_TYPE");
		if(ColumnType.CLOB.equals(type) || ColumnType.BIGCLOB.equals(type)){
			throw new PlatformException("【超大文本】类型无法创建索引!",PlatformExceptionEnum.JE_CORE_DB_COLUMN_TABLE_ERROR);
		}
		//验证数据类型是否支持索引
		if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_MYSQL)){
			if(ColumnType.VARCHAR1000.equals(type) || ColumnType.VARCHAR4000.equals(type) || (ColumnType.VARCHAR.equals(type) && Integer.parseInt(column.getStr("TABLECOLUMN_LENGTH"))>383)){
				throw new PlatformException("Mysql数据库索引支持最大长度是383长度!",PlatformExceptionEnum.JE_CORE_DB_COLUMN_TABLE_ERROR);
			}
		}else if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_SQLSERVER)){
			if(ColumnType.VARCHAR1000.equals(type) || ColumnType.VARCHAR4000.equals(type) || (ColumnType.VARCHAR.equals(type) && Integer.parseInt(column.getStr("TABLECOLUMN_LENGTH"))>900)){
				throw new PlatformException("SqlServer数据库索引支持最大长度是900长度!",PlatformExceptionEnum.JE_CORE_DB_COLUMN_TABLE_ERROR);
			}
		}

		DynaBean index=new DynaBean("JE_CORE_TABLEINDEX",false);
		index.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEINDEX_ID");
		index.set("TABLEINDEX_ISCREATE", "1");
		index.set("TABLEINDEX_UNIQUE", "0");
		index.set("TABLEINDEX_RESOURCETABLE_ID", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
		index.set("TABLEINDEX_CLASSIFY", "PRO");
		index.set("TABLEINDEX_FIELDNAME", column.getStr("TABLECOLUMN_NAME"));
		index.set("TABLEINDEX_TABLECODE", tableCode);
		index.set("TABLEINDEX_FIELDCODE", columnCode);
		index.set("SY_ORDERINDEX", 0);
		serviceTemplate.buildModelCreateInfo(index);
		String nowDateTime=DateUtils.formatDateTime(new Date());
		nowDateTime=nowDateTime.replaceAll("-", "");
		nowDateTime=nowDateTime.replaceAll(" ", "");
		nowDateTime=nowDateTime.replaceAll(":", "");
		index.set("TABLEINDEX_NAME", "JE_"+nowDateTime);
		index=serviceTemplate.insert(index);
		saveTableTrace("JE_CORE_TABLEINDEX", null, index, "INSERT", resourceTable.getStr("JE_CORE_RESOURCETABLE_ID"));
		String indexSql=BuildingSql.getInstance().getDDL4AddIndex(index, resourceTable);
		pcServiceTemplate.executeSql(indexSql);
		pcServiceTemplate.executeSql("UPDATE JE_CORE_RESOURCECOLUMN SET RESOURCECOLUMN_INDEX='1' WHERE JE_CORE_RESOURCECOLUMN_ID='"+columnId+"'");
		BeanUtils.getInstance().clearCache(tableCode);
		DynaCacheManager.removeCache(tableCode);
		return index;
	}

	/**
	 * 删除索引
	 * @param funcId 功能id
	 * @param columnCode TODO 暂不明确
	 * @param columnId TODO 暂不明确
	 * @return
	 */
	@Override
	public String removeIndexByColumn(String funcId, String columnCode,String columnId) {
		// TODO Auto-generated method stub
		DynaBean funcInfo=serviceTemplate.selectOneByPk("JE_CORE_FUNCINFO", funcId);
		String tableCode=funcInfo.getStr("FUNCINFO_TABLENAME");
		DynaBean resourceTable=BeanUtils.getInstance().getResourceTable(tableCode);
		DynaBean index=serviceTemplate.selectOne("JE_CORE_TABLEINDEX"," AND TABLEINDEX_FIELDCODE='"+columnCode+"' AND TABLEINDEX_RESOURCETABLE_ID='"+resourceTable.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		if("SYS".equals(index.getStr("TABLEINDEX_CLASSIFY"))){
			return "系统索引无法删除!";
		}
		saveTableTrace("JE_CORE_TABLEINDEX", index, null, "DELETE", index.getStr("TABLEINDEX_RESOURCETABLE_ID"));
		List<DynaBean> indexs=new ArrayList<DynaBean>();
		indexs.add(index);
		deleteIndex(tableCode, indexs);
		pcServiceTemplate.executeSql("UPDATE JE_CORE_RESOURCECOLUMN SET RESOURCECOLUMN_INDEX='0' WHERE JE_CORE_RESOURCECOLUMN_ID='"+columnId+"'");
		BeanUtils.getInstance().clearCache(tableCode);
		DynaCacheManager.removeCache(tableCode);
		return null;
	}

	/**
	 * 对列进行检测。。 主要检测字段为空和字段重复
	 * @param columns TODO 暂不明确
	 * @param jeCore TODO 暂不明确
	 * @return
	 */
	@Override
	public String checkColumns(List<DynaBean> columns,Boolean jeCore) {
		// TODO Auto-generated method stub
		String errors="";
		Set<String> columnCodes=new HashSet<String>();
		Integer pkCount=0;
		String tableCode="";
		Integer lengthCount=0;
		List<String> pkCodeList=new ArrayList<String>();
		for(DynaBean column:columns){
			if(StringUtil.isNotEmpty(column.getStr("TABLECOLUMN_TABLECODE")) && StringUtil.isEmpty(tableCode)){
				tableCode=column.getStr("TABLECOLUMN_TABLECODE");
			}
			String columnCode=column.getStr("TABLECOLUMN_CODE");
			String columnType=column.getStr("TABLECOLUMN_TYPE");
			if(StringUtil.isEmpty(columnCode)){
				errors="字段编码为空，请检查！";
				break;
			}
			if(columnCode.length()>30){
				errors="字段【"+column.getStr("TABLECOLUMN_CODE")+"】长度大于30，请更换编码！";
				break;
			}
			if(columnCodes.contains(columnCode)){
				errors="字段【"+column.getStr("TABLECOLUMN_CODE")+"】在表中重复，请删除！";
				break;
			}
			if(ColumnType.ID.equals(columnType)){
				pkCodeList.add(column.getStr("TABLECOLUMN_CODE"));
				pkCount++;
			}
			//计算行长度
			if(ColumnType.VARCHAR255.equals(columnType)){
				lengthCount+=255;
			}else if(ColumnType.VARCHAR100.equals(columnType)){
				lengthCount+=100;
			}else if(ColumnType.VARCHAR50.equals(columnType)){
				lengthCount+=50;
			}else if(ColumnType.VARCHAR30.equals(columnType)){
				lengthCount+=30;
			}else if(ColumnType.VARCHAR1000.equals(columnType)){
				lengthCount+=1000;
			}else if(ColumnType.VARCHAR4000.equals(columnType)){
				if(!((jeCore || ("SYS".equals(column.getStr("TABLECOLUMN_CLASSIFY")))) && ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME))){
					lengthCount+=4000;
				}
			}else if(ColumnType.VARCHAR.equals(columnType)){
				if(StringUtil.isNotEmpty(column.getStr("TABLECOLUMN_LENGTH")) && StringUtil.isNumber(column.getStr("TABLECOLUMN_LENGTH"))){
					lengthCount+=column.getInt(column.getStr("TABLECOLUMN_LENGTH"), 0);
				}
			}
//			if(columnType.equals(""+Constant.FLOAT) && StringUtil.isEmpty(column.getStr("TABLECOLUMN_LENGTH"))){
//				errors="字段【"+column.getStr("TABLECOLUMN_CODE")+"】为小数,请指定小数精度！";
//			}
//			if(ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME) && columnType.equals(Constant.NUMBER+"") && StringUtil.isEmpty(column.getStr("TABLECOLUMN_LENGTH"))){
//				errors="字段【"+column.getStr("TABLECOLUMN_CODE")+"】为整数,请指定整数指定有效位数！";
//			}
			columnCodes.add(columnCode);
		}
		if(pkCount>1){
			errors="表【"+tableCode+"】只能有一个主键，字段为【"+StringUtil.buildSplitString(ArrayUtils.getArray(pkCodeList), ",")+"】!";
		}
		if(ConstantVars.STR_MYSQL.equals(PCDaoTemplateImpl.DBNAME) && lengthCount>=32766){
			errors="MySql数据库所有字符串的长度加起来不能超过32766个字符!";
		}
		return errors;
	}

	/**
	 * 对键进行检测  主要检测键为空和键重复
	 * @param keys TODO 暂不明确
	 * @return
	 */
	@Override
	public String checkKeys(List<DynaBean> keys) {
		// TODO Auto-generated method stub
		String errors="";
		Set<String> keyCodes=new HashSet<String>();
		String tableCode="";
		Integer primaryCount=0;
		for(DynaBean key:keys){
			if(StringUtil.isNotEmpty(key.getStr("TABLEKEY_TABLECODE")) && StringUtil.isEmpty(tableCode)){
				tableCode=key.getStr("TABLEKEY_TABLECODE");
			}
			String keyStr="";
			String keyType=key.getStr("TABLEKEY_TYPE");
			if("Primary".equals(keyType)){
				if(StringUtil.isEmpty(key.getStr("TABLEKEY_COLUMNCODE"))){
					errors="主键未指定字段，无法创建";
					break;
				}
				keyStr=keyType+"_"+key.getStr("TABLEKEY_COLUMNCODE");
				primaryCount++;
			}
			if(primaryCount>1){
				errors="主键只能有一个，请检查!";
				break;
			}
			if("Unique".equals(keyType)){
				if(StringUtil.isEmpty(key.getStr("TABLEKEY_COLUMNCODE"))){
					errors="唯一键必须指定列!";
					break;
				}
				keyStr=keyType+"_"+key.getStr("TABLEKEY_COLUMNCODE");
			}
			if("Foreign".equals(keyType)){
				if(StringUtil.isEmpty(key.getStr("TABLEKEY_COLUMNCODE"))){
					errors="外键必须指定列!";
					break;
				}else if(StringUtil.isEmpty(key.getStr("TABLEKEY_LINKTABLE"))){
					errors="外键必须关联表!";
					break;
				}else if(StringUtil.isEmpty(key.getStr("TABLEKEY_LINECOLUMNCODE"))){
					errors="外键必须关联字段!";
					break;
				}
				keyStr=keyType+"_"+key.getStr("TABLEKEY_COLUMNCODE")+"_"+key.getStr("TABLEKEY_LINKTABLE")+"_"+key.getStr("TABLEKEY_LINECOLUMNCODE");
			}
			if("Inline".equals(keyType)){
				if(StringUtil.isEmpty(key.getStr("TABLEKEY_COLUMNCODE"))){
					errors="内联外键必须指定列!";
					break;
				}else if(StringUtil.isEmpty(key.getStr("TABLEKEY_LINKTABLE"))){
					errors="内联外键必须关联表!";
					break;
				}else if(StringUtil.isEmpty(key.getStr("TABLEKEY_LINECOLUMNCODE"))){
					errors="内联外键必须关联字段!";
					break;
				}
				keyStr=keyType+"_"+key.getStr("TABLEKEY_COLUMNCODE")+"_"+key.getStr("TABLEKEY_LINKTABLE")+"_"+key.getStr("TABLEKEY_LINECOLUMNCODE");
			}
			if(keyCodes.contains(keyStr)){
				errors="指定键有重复，请检查!";
				break;
			}
			keyCodes.add(keyStr);
		}
		if(StringUtil.isNotEmpty(errors)){
			return "表【"+tableCode+"】"+errors;
		}else{
			return errors;
		}
	}

	/**
	 * 对索引进行检测，主要检测索引重复和索引为空
	 * @param indexs 检索的索引
	 * @return
	 */
	@Override
	public String checkIndexs(List<DynaBean> indexs) {
		// TODO Auto-generated method stub
		String errors="";
		Set<String> indexCodes=new HashSet<String>();
		for(DynaBean index:indexs){
			String columnCode=index.getStr("TABLEINDEX_FIELDCODE");
			if(StringUtil.isEmpty(columnCode)){
				errors="索引编码为空，请检查！";
				break;
			}
			if(indexCodes.contains(columnCode)){
				errors="索引【"+index.getStr("TABLEINDEX_CODE")+"】在表中重复，请删除！";
				break;
			}
			indexCodes.add(columnCode);
		}
		return errors;
	}
	/**
	 * 将数组的视图列信息 构建成map code为键 信息为值
	 * @param fields
	 * @return
	 */
	private Map<String,JSONObject> buildColumnInfo(String fields){
		Map<String,JSONObject> columnInfos=new HashMap<String,JSONObject>();
		JSONArray columns=JSONArray.fromObject(fields);
		for(Integer i=0;i<columns.size();i++){
			JSONObject objs=columns.getJSONObject(i);
//			column.value=viewConfig.value;
//			column.table=viewConfig.table;
//			column.formula=viewConfig.formula;
//			column.group=viewConfig.group;
//			column.order=viewConfig.order;
//			whereValue=viewConfig.whereValue;
			JSONObject values=new JSONObject();
			values.put("name", objs.get("name"));
			values.put("code", objs.get("code"));
			values.put("value", objs.get("value"));
			values.put("dataType", objs.get("dataType"));
			values.put("table", objs.get("table"));
			values.put("length", objs.get("length"));
			values.put("formula", objs.get("formula"));
			values.put("group", objs.get("group"));
			values.put("order", objs.get("order"));
			values.put("whereValue", objs.get("whereValue"));
			columnInfos.put(objs.getString("code").toUpperCase(), values);
		}
		return columnInfos;
	}
	private String buildViewColumn(Map<String,JSONObject> columnsInfo,DynaBean column){
		String pkCode="";
		if(columnsInfo.get(column.getStr("TABLECOLUMN_CODE"))!=null){
			JSONObject objs=columnsInfo.get(column.getStr("TABLECOLUMN_CODE"));
			column.set("TABLECOLUMN_NAME", objs.getString("name"));
			column.set("TABLECOLUMN_VIEWCONFIG", objs.toString());
			String type=objs.getString("dataType");//存储视图列的配置信息
			if(StringUtil.isNotEmpty(type)){
				column.set("TABLECOLUMN_TYPE", type);
				if(objs.containsKey("length")){
					column.set("TABLECOLUMN_LENGTH", objs.getString("length"));
				}
				if("ID".equals(type)){
					pkCode=column.getStr("TABLECOLUMN_CODE");
				}
			}

		}
		return pkCode;
	}

	/**
	 * 表保存
	 * @param dynaBean TODO 暂不明确
	 * @return
	 */
	@Override
	public DynaBean doSave(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		serviceTemplate.buildModelCreateInfo(dynaBean);
		if(StringUtil.isNotEmpty(dynaBean.getStr("SY_PATH"))){
			String uuid = JEUUID.uuid();
			dynaBean.setStr(dynaBean.getStr(BeanUtils.KEY_PK_CODE), uuid);
			dynaBean.set("SY_PATH", dynaBean.getStr("SY_PATH")+"/"+uuid);
		}
		dynaBean.set("SY_JECORE", "0");
		dynaBean.set("SY_JESYS", "0");
		dynaBean.set("RESOURCETABLE_PKCODE", dynaBean.get("RESOURCETABLE_TABLECODE")+"_ID");
		DynaBean inserted = serviceTemplate.insert(dynaBean);
		Boolean isTree=false;
		if(TableType.TREETABLE.equals(inserted.getStr("RESOURCETABLE_TYPE"))){
			isTree=true;
		}
		initColumns(inserted, isTree);
		initKeys(inserted, isTree);
		initIndexs(inserted, isTree);
		return inserted;
	}

	/**
	 * 表修改
	 * @param dynaBean TODO 暂不明确
	 * @return
	 */
	@Override
	public DynaBean doUpdate(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		DynaBean oldTable=serviceTemplate.selectOneByPk("JE_CORE_RESOURCETABLE", dynaBean.getStr("JE_CORE_RESOURCETABLE_ID"));
		if(!dynaBean.get("RESOURCETABLE_TABLECODE").equals(oldTable.get("RESOURCETABLE_TABLECODE"))){
			pcServiceTemplate.executeSql("UPDATE JE_CORE_TABLECOLUMN SET TABLECOLUMN_TABLECODE='"+dynaBean.getStr("JE_CORE_RESOURCETABLE_ID")+"' WHERE TABLECOLUMN_RESOURCETABLE_ID='"+dynaBean.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
			pcServiceTemplate.executeSql("UPDATE JE_CORE_TABLEKEY SET TABLEKEY_TABLECODE='"+dynaBean.getStr("RESOURCETABLE_TABLECODE")+"' WHERE TABLEKEY_RESOURCETABLE_ID='"+dynaBean.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		}
		serviceTemplate.buildModelModifyInfo(dynaBean);
		DynaBean updated = serviceTemplate.update(dynaBean);
		if("1".equals(updated.getStr("RESOURCETABLE_ISCREATE"))){
			saveTableTrace("JE_CORE_RESOURCETABLE", oldTable, updated, "UPDATE", updated.getStr("JE_CORE_RESOURCETABLE_ID"));
		}
		return updated;
	}

	/**
	 * 复制表
	 * @param dynaBean TODO 暂不明确
	 * @return
	 */
	@Override
	public DynaBean copyTable(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		String pkValue=dynaBean.getStr("JE_CORE_RESOURCETABLE_ID");
		DynaBean oldTable=serviceTemplate.selectOneByPk("JE_CORE_RESOURCETABLE", pkValue);
		if(oldTable!=null){
			oldTable.set("RESOURCETABLE_ISCREATE", "0");
			oldTable.set("RESOURCETABLE_TABLECODE", dynaBean.getStr("RESOURCETABLE_TABLECODE"));
			oldTable.set("RESOURCETABLE_TABLENAME", oldTable.getStr("RESOURCETABLE_TABLENAME")+"(复制)");
			if(StringUtil.isNotEmpty(oldTable.getStr("SY_PATH"))){
				String uuid = JEUUID.uuid();
				oldTable.setStr(dynaBean.getStr(BeanUtils.KEY_PK_CODE), uuid);
				oldTable.set("SY_PATH", oldTable.getStr("SY_PATH").substring(0, oldTable.getStr("SY_PATH").lastIndexOf("/")+1)+"/"+uuid);
			}
			DynaBean newTable=serviceTemplate.insert(oldTable);
			List<DynaBean> columns=serviceTemplate.selectList("JE_CORE_TABLECOLUMN", " and TABLECOLUMN_RESOURCETABLE_ID='"+pkValue+"'");
			List<DynaBean> keys=serviceTemplate.selectList("JE_CORE_TABLEKEY", " and TABLEKEY_RESOURCETABLE_ID='"+pkValue+"'");
			List<DynaBean> indexs=serviceTemplate.selectList("JE_CORE_TABLEINDEX", " and TABLEINDEX_RESOURCETABLE_ID='"+pkValue+"'");
			for(DynaBean column:columns){
				column.set("JE_CORE_TABLECOLUMN_ID", null);
				column.set("TABLECOLUMN_ISCREATE", "0");
				column.set("TABLECOLUMN_TABLECODE", newTable.getStr("RESOURCETABLE_TABLECODE"));
				column.set("TABLECOLUMN_RESOURCETABLE_ID", newTable.getStr("JE_CORE_RESOURCETABLE_ID"));
				serviceTemplate.insert(column);
			}
			for(DynaBean key:keys){
				key.set("JE_CORE_TABLEKEY_ID", null);
				if("Inline".equals(key.getStr("TABLEKEY_TYPE"))){
					key.set("TABLEKEY_CODE", "JE_"+DateUtils.getUniqueTime()+"_PARENT");
				}else if("Primary".equals(key.getStr("TABLEKEY_TYPE"))){
					key.set("TABLEKEY_CODE", key.getStr("TABLEKEY_CODE").replace(oldTable.getStr("RESOURCETABLE_TABLECODE"), newTable.getStr("RESOURCETABLE_TABLECODE")));
				}else{
					key.set("TABLEKEY_CODE", "JE_"+DateUtils.getUniqueTime());
				}
				key.set("TABLEKEY_ISCREATE", "0");
				key.set("TABLEKEY_TABLECODE", newTable.getStr("RESOURCETABLE_TABLECODE"));
				key.set("TABLEKEY_RESOURCETABLE_ID", newTable.getStr("JE_CORE_RESOURCETABLE_ID"));
				serviceTemplate.insert(key);
			}
			for(DynaBean index:indexs){
				if((oldTable.getStr("RESOURCETABLE_TABLECODE")+"_ID").equals(index.getStr("TABLEINDEX_FIELDCODE"))){
					index.set("TABLEINDEX_NAME", index.getStr("TABLEINDEX_NAME").replace(oldTable.getStr("RESOURCETABLE_TABLECODE"),newTable.getStr("RESOURCETABLE_TABLECODE")));
					index.set("TABLEINDEX_FIELDCODE",newTable.getStr("RESOURCETABLE_PKCODE"));
				}else if("SY_ORDERINDEX".equals(index.getStr("TABLEINDEX_FIELDCODE"))){
					index.set("TABLEINDEX_NAME", index.getStr("TABLEINDEX_NAME").replace(oldTable.getStr("RESOURCETABLE_TABLECODE"), newTable.getStr("RESOURCETABLE_TABLECODE")));
					index.set("TABLEINDEX_FIELDCODE", "SY_ORDERINDEX");
				}else{
					index.set("TABLEINDEX_NAME", "JE_"+DateUtils.getUniqueTime());
					String columnCode=index.getStr("TABLEINDEX_FIELDCODE");
					index.set("TABLEINDEX_FIELDCODE", columnCode);
				}
			}
			return newTable;
		}else{
			return null;
		}
	}

	/**
	 * 表移动
	 * @param dynaBean TODO 暂不明确
	 * @param oldParentId 移动前表的id
	 * @return
	 */
	@Override
	public DynaBean tableMove(DynaBean dynaBean,String oldParentId) {
		// TODO Auto-generated method stub
		DynaBean table=serviceTemplate.selectOneByPk("JE_CORE_RESOURCETABLE", dynaBean.getStr("JE_CORE_RESOURCETABLE_ID"));
		String newParentId=dynaBean.getStr("SY_PARENT");
		String oldPath=table.getStr("SY_PATH");
		table.set("SY_PATH", dynaBean.getStr("SY_PATH")+"/"+dynaBean.getStr("JE_CORE_RESOURCETABLE_ID"));
		table.set("SY_PARENT", newParentId);
		Integer chaLayer=dynaBean.getInt("SY_LAYER",0)-table.getInt("SY_LAYER",0);
		table.set("SY_LAYER", dynaBean.get("SY_LAYER"));
		table.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_RESOURCETABLE");
		//更新当前节点下所有孩子的路径信息
		pcServiceTemplate.executeSql("UPDATE JE_CORE_RESOURCETABLE SET SY_PATH=REPLACE(SY_PATH,'"+oldPath+"','"+table.getStr("SY_PATH")+"'),SY_LAYER=(SY_LAYER+"+chaLayer+") WHERE SY_PATH LIKE '%"+oldPath+"%' AND JE_CORE_RESOURCETABLE_ID!='"+ dynaBean.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		DynaBean updated=serviceTemplate.update(table);
		serviceTemplate.updateTreePanent4NodeType("JE_CORE_RESOURCETABLE", oldParentId);
		serviceTemplate.updateTreePanent4NodeType("JE_CORE_RESOURCETABLE", newParentId);
		//更新权限的模块编码，为了节省授权的性能
		return updated;
	}
	public  List<String> getOracleClobFields(String tableCode){
		List<String> clobCodes=new ArrayList<String>();
		if(!ConstantVars.STR_ORACLE.equals(PCDaoTemplateImpl.DBNAME)){
			return clobCodes;
		}
		Connection connection  =pcServiceTemplate.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("select * from "+tableCode);
			rs = ps.executeQuery();
			ResultSetMetaData rsme = rs.getMetaData();
			int columnCount = rsme.getColumnCount();
			for (int i = 1; i <= columnCount ; i++) {
				String type =  rsme.getColumnTypeName(i);
				if("clob".equalsIgnoreCase(type)){
					clobCodes.add(rsme.getColumnName(i).toUpperCase());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw  new PlatformException("获取表【"+tableCode+"】的clob字段失败",PlatformExceptionEnum.JE_CORE_TABLECOLUMN_CLOBFIELD_ERROR,new Object[]{tableCode},e);

		}finally{
			JdbcUtil.close(rs,ps,connection);
		}
		return clobCodes;
	}
}

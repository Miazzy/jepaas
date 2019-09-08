package com.je.core.util.bean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.develop.vo.FuncRelationField;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.je.cache.service.table.TableCacheManager;
import com.je.core.constants.table.ColumnType;
import com.je.core.constants.tree.TreeNodeType;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.ArrayUtils;
import com.je.core.util.SpringContextHolder;
import com.je.core.util.StringUtil;
/**
 * 辅助自定义动态类，提供一些方便使用的扩展方法。
 * 研发部:云凤程
 */
public class BeanUtils {
    /** 特殊属性：表编码 */
    public static final String KEY_TABLE_CODE = "$TABLE_CODE$";

    /** 特殊属性：结果集 */
    public static final String KEY_ROWSET = "$ROWSET$";

    /** 特殊属性：当前页面记录数 */
    public static final String KEY_PAGE_COUNT = "$P_COUNT$";

    /** 特殊属性：总记录数 */
    public static final String KEY_ALL_COUNT = "$A_COUNT$";
    
    /** 特殊属性：查询结果的列表 */
    public static final String KEY_COLUMNS = "$A_COLUMENS$";
    
    /** 特殊属性：本条数据的所在行数 */
    public static final String KEY_ROW_NUM = "$ROW_NUM$";
    
    /** 特殊属性：SQL语句 */
    public static final String KEY_SQL = "$SQL$";

    /** 特殊属性：select字段列 */
    public static final String KEY_SELECT = "$SELECT$";
    
    /** 特殊属性：Where条件 */
    public static final String KEY_WHERE = "$WHERE$";
    
    /** 特殊属性：group by */
    public static final String KEY_GROUP = "$GROUP$";

    /** 特殊属性: Order By */
    public static final String KEY_ORDER = "$ORDER$";

    /** 特殊属性：主键字符串 */
    public static final String KEY_PK_CODE = "$PK_CODE$";
    
    /** 特殊属性：查询字段集合 */
    public static final String KEY_QUERY_FIELDS = "$QUERY_FIELDS$";

    /** 特殊属性：表的所有列*/
    public static final String KEY_TABLE_COLUMNS = "$TABLECOLUMNS$";

    /** 特殊属性：表的所有键 */
    public static final String KEY_TABLE_KEYS = "$TABLEKEYS$";
    /** 特殊属性：表的所有索引 */
    public static final String KEY_TABLE_INDEXS = "$TABLEINDEXS$";
    
    /** 特殊属性：大文本内容 */
    public static final String KEY_LONG_FIELD = "$LONG$";

    /** 特殊属性：操作用户名 */
    public static final String KEY_USER = "$USER$";

    /** 特殊属性：request对象 */
    public static final String KEY_REQUEST = "$REQUEST$";

    /** 特殊属性：response对象 */
    public static final String KEY_RESPONSE = "$RESPONSE$";
    
    /** 特殊属性：唯一组重复字段 */
    public static final String KEY_DUPCODE = "$DUPCODE$";
    
    /** 上传文档信息 */
    public static final String KEY_DOC_INFO = "$DOC_INFO$";
    
    /** 参数属性：是否进行数据库匹配 */
    public static final String PARAM_IS_DBMAP = "isDBMap";

    /** 表定义的参数：字段列表 */
    public static final String DEF_ALL_FIELDS = "$ALL_FIELDS$";

    /** 表定义的参数：主键列表 */
    public static final String DEF_PK_FIELDS = "PK_FIELDS";

    /** 表定义的参数：非主键列表 */
    public static final String DEF_NPK_FIELDS = "NPK_FIELDS";

    /** 特殊属性：空字符串，在Where生成时使用自动替换为空 */
    public static final String KEY_VALUE_NULL = "~$￥$~==~￥$￥~";

    /** 特殊属性：数字零，在Where生成时使用自动替换为数字零 */
    public static final String KEY_VALUE_ZERO = "~￥$￥~==~$￥$~";
    
    /** 特殊属性：是否在流程中，当前数据Bean是否在流程处理中执行 */
    public static final String KEY_IS_IN_WORKFLOW = "$IS_IN_WORKFLOW$";

    /** 日志记录 */
    private static Log logger = LogFactory.getLog(BeanUtils.class);
    
    private BeanUtils(){
    	
    }
	/**
	 * 实例化此类
	 * 研发部:云凤程 
	 * 2011-8-31 上午09:37:45
	 * @return
	 */
	public static BeanUtils getInstance(){
		return BeanUtilsHolder.BEANUTILS;
	}
	/**
	 * 静态内部类用于初始化对象和缓存树形
	 * 研发部:云凤程 
	 * 2011-8-31 上午09:38:00
	 */
	private static class BeanUtilsHolder{
		private static final BeanUtils BEANUTILS = new BeanUtils();
		/**
		 * 缓存表表结构
		 */
		private static Map<String, DynaBean> tables = new HashMap<String, DynaBean>(); 
	}    
	/**
	 * 根据表编码得到表的描述性信息
	 * @param tableCode
	 * @return ResourceTable
	 */
	public DynaBean getResourceTable(String tableCode){
//		if(BeanUtilsHolder.tables.get("JE_CORE_RESOURCETABLE")==null){
//			//初始化资源表模版
//			DynaBean resourceTable=initResourceTable();
//			BeanUtilsHolder.tables.put("JE_CORE_RESOURCETABLE", resourceTable);
//		}
//		if(BeanUtilsHolder.tables.get("JE_CORE_TABLECOLUMN")==null){
//			DynaBean tableColumn=initTableColumn();
//			BeanUtilsHolder.tables.put("JE_CORE_TABLECOLUMN", tableColumn);
//		}
//		if(BeanUtilsHolder.tables.get("JE_CORE_TABLEKEY")==null){
//			DynaBean tableKey=initTableKey();
//			BeanUtilsHolder.tables.put("JE_CORE_TABLEKEY", tableKey);
//		}
//		if(BeanUtilsHolder.tables.get("JE_CORE_TABLEINDEX")==null){
//			DynaBean tableIndex=initTableIndex();
//			BeanUtilsHolder.tables.put("JE_CORE_TABLEINDEX", tableIndex);
//		}
		if(TableCacheManager.getCacheValue("JE_CORE_RESOURCETABLE")==null){
			//初始化资源表模版
			DynaBean resourceTable=initSysTable("JE_CORE_RESOURCETABLE");
			TableCacheManager.putCache("JE_CORE_RESOURCETABLE", resourceTable);
		}
		if(TableCacheManager.getCacheValue("JE_CORE_TABLECOLUMN")==null){
			DynaBean tableColumn=initSysTable("JE_CORE_TABLECOLUMN");
			TableCacheManager.putCache("JE_CORE_TABLECOLUMN", tableColumn);
		}
		if(TableCacheManager.getCacheValue("JE_CORE_TABLEKEY")==null){
			DynaBean tableKey=initSysTable("JE_CORE_TABLEKEY");
			TableCacheManager.putCache("JE_CORE_TABLEKEY", tableKey);
		}
		if(TableCacheManager.getCacheValue("JE_CORE_TABLEINDEX")==null){
			DynaBean tableIndex=initSysTable("JE_CORE_TABLEINDEX");
			TableCacheManager.putCache("JE_CORE_TABLEINDEX", tableIndex);
		}
//		DynaBean table=BeanUtilsHolder.tables.get(tableCode);
		DynaBean table=TableCacheManager.getCacheValue(tableCode);
		if(table==null){
			try{
				PCDynaServiceTemplate pcDynaServiceTemplate=SpringContextHolder.getBean("PCDynaServiceTemplate");
				//TODO 增加 where条件" and RESOURCETABLE_TYPE != 'MODULE' "，可能会引起未知的BUG
				table=pcDynaServiceTemplate.selectOne("JE_CORE_RESOURCETABLE", " and RESOURCETABLE_TABLECODE='"+tableCode+"' and RESOURCETABLE_TYPE != 'MODULE'",getNoClobQueryFields("JE_CORE_RESOURCETABLE"));
				List<DynaBean> columns=pcDynaServiceTemplate.selectList("JE_CORE_TABLECOLUMN", " and TABLECOLUMN_RESOURCETABLE_ID='"+table.getStr("JE_CORE_RESOURCETABLE_ID")+"' AND TABLECOLUMN_ISCREATE='1' ORDER BY TABLECOLUMN_CLASSIFY,TABLECOLUMN_CODE");
				List<DynaBean> keys=pcDynaServiceTemplate.selectList("JE_CORE_TABLEKEY", " and TABLEKEY_RESOURCETABLE_ID='"+table.getStr("JE_CORE_RESOURCETABLE_ID")+"'  AND TABLEKEY_ISCREATE='1' ORDER BY SY_ORDERINDEX");
				List<DynaBean> indexs=pcDynaServiceTemplate.selectList("JE_CORE_TABLEINDEX", " and TABLEINDEX_RESOURCETABLE_ID='"+table.getStr("JE_CORE_RESOURCETABLE_ID")+"' AND TABLEINDEX_ISCREATE='1' ORDER BY SY_ORDERINDEX");
				table.set(BeanUtils.KEY_TABLE_COLUMNS,columns);
				table.set(BeanUtils.KEY_TABLE_KEYS, keys);
				table.set(BeanUtils.KEY_TABLE_INDEXS, indexs);
				for(DynaBean key : keys){
        			//本系统目前不支持符合主键 2012-5-2
        			//研发部 : 云凤程
        			if("Primary".equals(key.getStr("TABLEKEY_TYPE"))){
        				String tablePK = key.getStr("TABLEKEY_COLUMNCODE");    				
        				//同时装载到bean的特殊字段上
        				table.setStr(BeanUtils.KEY_PK_CODE,tablePK);    				
        				break;
        			}
        		} 
				table.set(BeanUtils.KEY_TABLE_CODE, tableCode);
//				BeanUtilsHolder.tables.put(tableCode, table);
				TableCacheManager.putCache(tableCode, table);
			}catch(Exception e){
				throw new PlatformException("根据tableCode("+tableCode+")取得资源表信息出错",PlatformExceptionEnum.JE_CORE_DYNABEAN_ERROR,new Object[]{tableCode},e);
			}
		}
		return table;
	}
    /**
     * 根据表定义的编码得到对应的主键字段名称列表
     * @param dynaBean 动态Bean
     * @return 主键字段名称列表，使用~进行分隔
     * 装载了 bean的KEY_PK_CODE
     */
    public String getPKeyFieldNames(DynaBean dynaBean){
    	String tablePK = "";
    	String tableCode = null;
    	try {
    		tableCode = dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
    		DynaBean table = getResourceTable(tableCode);
    		if(null != table) {
    			for(DynaBean key : (List<DynaBean>)table.get(BeanUtils.KEY_TABLE_KEYS)){
        			//本系统目前不支持符合主键 2012-5-2
        			//研发部 : 云凤程
        			if("Primary".equals(key.getStr("TABLEKEY_TYPE"))){
        				tablePK = key.getStr("TABLEKEY_COLUMNCODE");    				
        				//同时装载到bean的特殊字段上
        				dynaBean.setStr(BeanUtils.KEY_PK_CODE,tablePK);    				
        				break;
        			}
        		} 
    		} else {
    			throw new PlatformException("数据表:" + tableCode + " 未找到",PlatformExceptionEnum.JE_CORE_DYNABEAN_ERROR,new Object[]{dynaBean});
    		}
    		
		} catch (Exception e) {
			throw new PlatformException("根据tableCode("+tableCode+")取得表主键信息出错",PlatformExceptionEnum.JE_CORE_DYNABEAN_ERROR,new Object[]{dynaBean},e);
		}
  		return tablePK;
    }
    /**
     * 根据表定义的编码得到对应的主键字段名称列表
     * @param TableCode 表定义编码
     * @return 主键字段名称列表，使用~进行分隔
     * 装载了 bean的KEY_PK_CODE
     */
    public String getPKeyFieldNames(String tableCode) {
    	String tablePK = "";
    	try {
    		DynaBean table = getResourceTable(tableCode);
    		if(null != table) {
    			for(DynaBean key :  (List<DynaBean>)table.get(BeanUtils.KEY_TABLE_KEYS)){
        			//本系统目前不支持符合主键 2012-5-2
        			//研发部 : 云凤程
        			if("Primary".equals(key.getStr("TABLEKEY_TYPE"))){
        				tablePK = key.getStr("TABLEKEY_COLUMNCODE");     				
        				break;
        			}
        		} 
    		} else {
    			throw new PlatformException("数据表:" + tableCode + " 未找到",PlatformExceptionEnum.JE_CORE_DYNABEAN_ERROR,new Object[]{tableCode});
    		}
    		
		} catch (Exception e) {
			throw new PlatformException("根据tableCode("+tableCode+")取得表主键信息出错",PlatformExceptionEnum.JE_CORE_DYNABEAN_ERROR,new Object[]{tableCode},e);
		}
  		return tablePK;
    }
    /**
     * 获取当前表外键字段CODE
     * @param tableCode 当前表
     * @param parentTableCode 目标表
     * @param parentPkCode 目标表主键
     * @return
     */
    public String getForeignKeyField(String tableCode,String parentTableCode,String parentPkCode,List<FuncRelationField> relatedFields){
    	DynaBean resourceTable=getResourceTable(tableCode);
		String foreignKey="";
		//找到外键关联并构建删除条件
		List<DynaBean> keys=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_KEYS);
		for(DynaBean key:keys){
			if("Foreign".equals(key.getStr("TABLEKEY_TYPE")) && parentTableCode.equals(key.getStr("TABLEKEY_LINKTABLE")) && parentPkCode.equals(key.getStr("TABLEKEY_LINECOLUMNCODE"))){
				foreignKey = key.getStr("TABLEKEY_COLUMNCODE");    				
				break;
			}
		}
		//如果资源表无主外关系则在关联字段中加入
		if(StringUtil.isEmpty(foreignKey)){
			for(int i=0;i<relatedFields.size();i++){
				FuncRelationField funcRelationField=relatedFields.get(i);
				if(parentPkCode.equals(funcRelationField.getFieldCode())){
					foreignKey=funcRelationField.getChildFieldCode();
					break;
				}
			}
		}
		return foreignKey;
    }
    /**
     * 得到动态类的所有属性的名称
     * 
     * @param dynaBean
     *            动态类
     * @return 属性名称集
     */
    @SuppressWarnings("rawtypes")
	public String[] getNames(DynaBean dynaBean) {
        HashMap valueMap = dynaBean.getValues();
        String[] names = new String[valueMap.size()];
        int i = 0;
        for (Iterator it = valueMap.keySet().iterator(); it.hasNext(); i++) {
            names[i] = (String) it.next();
        }
        return names;
    }
    /**
     * 研发部:云凤程
     * 传输modeBean
     * 得到动态类所有的属性名称,中间用","分开
     * @return
     */
    public String getNames4Sql(DynaBean table){
    	StringBuilder names = new StringBuilder();
    	//根据ResourceTable得到DyanBean
    	DynaBean dynaBean = getDynaBeanByResourceTable(table);
    	String[] arrayName = getNames(dynaBean);
    	for(String name : arrayName){
    		if(!name.startsWith(StringUtil.DOLLAR)) {
    			names.append(name+",");
    		}
    	}
    	names.setLength(names.length()-1);
    	return names.toString();
    }
    /**
     * 研发部:云凤程
     * 传输modeBean
     * 得到修改信息的sql语句段
     * @return
     */
    public String getUpdateInfos4Sql(DynaBean resourceTable,Map values){
    	StringBuilder names = new StringBuilder();
    	DynaBean dynaBean = getDynaBeanByResourceTable(resourceTable);
    	String[] arrayName = getNames(dynaBean);
    	for(String name : arrayName){
    		if(!name.startsWith(StringUtil.DOLLAR)) {
    			if(values.get(name)!=null){
    				names.append(name+"=:"+name+",");
    			}
    		}
    	}
    	names.setLength(names.length()-1);
    	return names.toString();
    }
    /**
     * 研发部:云凤程
     * 传输dynaBean
     * 得到动态类所有的属性名称,中间用","分开
     * @return
     */    
    public String getNames2DynaBean4Sql(DynaBean dynaBean){
    	StringBuilder names = new StringBuilder();
    	String[] arrayName = getNames(dynaBean);
    	for(String name : arrayName){
    		if(!name.startsWith(StringUtil.DOLLAR)) {
    			names.append(name+",");
    		}
    	}
    	names.setLength(names.length()-1);
    	return names.toString();
    }
    /**
     * 研发部:云凤程
     * 得到动态类的所有属性名称,格式化成 :name ,:age 的样子用于Sql
     * @param table
     * @return
     */
    public String getValues4Sql(DynaBean table){
    	StringBuilder values = new StringBuilder();
    	//根据ResourceTable得到DyanBean
    	DynaBean dynaBean = getDynaBeanByResourceTable(table);
    	String[] arrayName = getNames(dynaBean);
    	for(String name : arrayName){
    		if(!name.startsWith(StringUtil.DOLLAR)) {
    			values.append(":"+name+",");
    		}
    	}
    	values.setLength(values.length()-1);
    	return values.toString();    	
    }
    /**
     * 研发部:云凤程
     * 根据ResourceTable得到DynaBean的结构
     * @param table
     * @return
     */
    public DynaBean getDynaBeanByResourceTable(DynaBean table){
    	DynaBean dynaBean = new DynaBean(table.getStr("RESOURCETABLE_TABLECODE"));
    	List<DynaBean> columns = (List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
    	for(DynaBean column : columns){
    		dynaBean.set(column.getStr("TABLECOLUMN_CODE"), "");
    	}
    	return dynaBean;
    }
    /**
     * 得到动态类的所有值的内容
     * 
     * @param dynaBean
     *            动态类
     * @return 动态类包含的值集
     */
    @SuppressWarnings("rawtypes")
    public Object[] getValues(DynaBean dynaBean) {
        HashMap valueMap = dynaBean.getValues();
        Object[] values = new String[valueMap.size()];
        int i = 0;
        for (Iterator it = valueMap.keySet().iterator(); it.hasNext(); i++) {
            values[i] = valueMap.get((String) it.next());
        }
        return values;
    }

    /**
     * 得到动态类的内容转为字符串。
     * 
     * @param dynaBean
     *            动态类
     * @return 动态类对应的字符串内容
     */
    @SuppressWarnings("rawtypes")
    public static String toString(DynaBean dynaBean) {
        HashMap valueMap = dynaBean.getValues();
        StringBuffer sb = new StringBuffer(valueMap.size() * 4);
        String name;
        for (Iterator it = valueMap.keySet().iterator(); it.hasNext();) {
            name = (String) it.next();
            sb.append(name).append("=").append(valueMap.get(name)).append(";");
        }
        return sb.toString();
    }

    /**
     * 得到动态类中字符串类型的值
     * 
     * @param dynaBean
     *            动态类
     * @param name
     *            属性
     * @return 值
     */
    public static String getStringValue(DynaBean dynaBean, String name) {
        Object value = dynaBean.get(name);
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    /**
     * 得到动态类中字符串类型的值
     * 
     * @param dynaBean
     *            动态类
     * @param name
     *            属性
     * @param defValue
     *            缺省值， 如果取不到就返回缺省值
     * @return 值
     */
    public static String getStringValue(DynaBean dynaBean, String name,
            String defValue) {
        Object value = dynaBean.get(name);
        if (value == null) {
            return defValue;
        } else {
            return value.toString();
        }
    }
    /**
     * 将更新字符串josn构建成list的dynaBean集合
     * @param updateStr
     * @param tableCode
     * @return
     */
    public List<DynaBean> buildUpdateList(String updateStr,String tableCode){
    	List<DynaBean> beans=new ArrayList<DynaBean>();
    	if(StringUtil.isEmpty(updateStr)){
    		return beans;
    	}
    	List<Map> sqlMapList = JsonBuilder.getInstance().fromJsonArray(updateStr);
    	for(int i=0; i<sqlMapList.size(); i++) {
			Map sqlMap = sqlMapList.get(i);
			DynaBean dynaBean=new DynaBean(tableCode,true);
			for(Object obj:sqlMap.entrySet()){
				Entry entry=(Entry) obj;
				String k=StringUtil.getDefaultValue(entry.getKey(), "");
				if(StringUtil.isNotEmpty(k)){
					dynaBean.set(k, sqlMap.get(k));
				}
			}
			beans.add(dynaBean);
		}
    	return beans;
	}
    /**
     * 获取dynaBean的id数组  便于查询
     * @param beans
     * @param pkCode
     * @return
     */
    public String[] getDynaBeanIdArray(List<DynaBean> beans,String pkCode){
    	String[] idArray=new String[beans.size()];
    	for(Integer i=0;i<beans.size();i++){
    		idArray[i]=beans.get(i).getStr(pkCode,"");
    	}
    	return idArray;
    }
    /**
     * 初始化资源表 结构
     * @return
     */
    public DynaBean initSysTable(String tableCode){
    	String tableQueryFields="RESOURCETABLE_CHILDTABLECODES,RESOURCETABLE_ICONCLS,RESOURCETABLE_IMPLWF,RESOURCETABLE_ISCREATE,RESOURCETABLE_ISUSEFOREIGNKEY,RESOURCETABLE_MOREROOT,RESOURCETABLE_NODEINFO,RESOURCETABLE_NODEINFOTYPE,RESOURCETABLE_OLDTABLECODE,RESOURCETABLE_PARENTTABLECODES,RESOURCETABLE_PKCODE,RESOURCETABLE_REMARK,RESOURCETABLE_TABLECODE,RESOURCETABLE_TABLENAME,RESOURCETABLE_TABLENOTE,RESOURCETABLE_TYPE,RESOURCETABLE_USEFUNC,SY_DISABLED,SY_JECORE,SY_JESYS,JE_CORE_RESOURCETABLE_ID,SY_AUDFLAG,SY_CREATEORG,SY_CREATEORGNAME,SY_CREATETIME,SY_CREATEUSER,SY_CREATEUSERNAME,SY_FLAG,SY_LAYER,SY_MODIFYORG,SY_MODIFYORGNAME,SY_MODIFYTIME,SY_MODIFYUSER,SY_MODIFYUSERNAME,SY_NODETYPE,SY_ORDERINDEX,SY_PARENT,SY_PARENTPATH,SY_PATH,SY_PDID,SY_PIID,SY_STATUS,SY_TREEORDERINDEX";
    	PCDynaServiceTemplate serviceTemplate=SpringContextHolder.getBean("PCDynaServiceTemplate");
//    	List<DynaBean> lists=pcDynaServiceTemplate.selectListBySql();
		List<Map> lists=serviceTemplate.queryMapBySql("SELECT "+tableQueryFields+" FROM JE_CORE_RESOURCETABLE WHERE RESOURCETABLE_TABLECODE='"+tableCode+"'");
    	Map tableVals=lists.get(0);
		DynaBean resourceTable=new DynaBean();
    	resourceTable.set(BeanUtils.KEY_PK_CODE, tableCode+"_ID");
    	resourceTable.set(BeanUtils.KEY_TABLE_CODE, tableCode);
		for(Object keyObj:tableVals.keySet()){
			String key=keyObj+"";
			resourceTable.set(key,tableVals.get(key));
		}
		List<Map> columnVals=serviceTemplate.queryMapBySql("SELECT * FROM JE_CORE_TABLECOLUMN WHERE TABLECOLUMN_RESOURCETABLE_ID='"+resourceTable.getStr("JE_CORE_RESOURCETABLE_ID")+"' ORDER BY TABLECOLUMN_CLASSIFY,TABLECOLUMN_CODE");
		List<DynaBean> columns=new ArrayList<>();
		for(Map columnVal:columnVals){
			DynaBean column=new DynaBean();
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_TABLECOLUMN");
			for(Object keyObj:columnVal.keySet()){
				String key=keyObj+"";
				column.set(key,columnVal.get(key));
			}
			columns.add(column);
		}
		List<Map> keyVals=serviceTemplate.queryMapBySql("SELECT * FROM JE_CORE_TABLEKEY WHERE TABLEKEY_RESOURCETABLE_ID='"+resourceTable.getStr("JE_CORE_RESOURCETABLE_ID")+"'  ORDER BY SY_ORDERINDEX");
    	List<DynaBean> keys=new ArrayList<>();
    	for(Map keyVal:keyVals){
			DynaBean key=new DynaBean();
			key.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEKEY_ID");
			key.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_TABLEKEY");
			for(Object keyObj:keyVal.keySet()){
				String keyV=keyObj+"";
				key.set(keyV,keyVal.get(keyV));
			}
			keys.add(key);
		}
		List<Map> indexVals=serviceTemplate.queryMapBySql("SELECT * FROM JE_CORE_TABLEINDEX WHERE TABLEINDEX_RESOURCETABLE_ID='"+resourceTable.getStr("JE_CORE_RESOURCETABLE_ID")+"' ORDER BY SY_ORDERINDEX");
    	List<DynaBean> indexs=new ArrayList<>();
		for(Map indexVal:indexVals){
			DynaBean index=new DynaBean();
			index.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEKEY_ID");
			index.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_TABLEKEY");
			for(Object keyObj:indexVal.keySet()){
				String keyV=keyObj+"";
				index.set(keyV,indexVal.get(keyV));
			}
			indexs.add(index);
		}
    	resourceTable.set(BeanUtils.KEY_TABLE_COLUMNS,columns);
    	resourceTable.set(BeanUtils.KEY_TABLE_KEYS, keys);
    	resourceTable.set(BeanUtils.KEY_TABLE_INDEXS, indexs);
    	return resourceTable; 
    }
    /**
     * 拼接指定表的所有列 字段的字符串     SY_CREATEUSER,SY_CREATETIME,SY_STATUS
     * @param resourceTable
     * @param excludes
     * @return
     */
    public String getFieldNames(DynaBean resourceTable,String[] excludes){
    	List<DynaBean> columns=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
    	StringBuffer fieldCodes=new StringBuffer();
    	for(DynaBean column:columns){
    		String columnCode=column.getStr("TABLECOLUMN_CODE");
    		if(!ArrayUtils.contains(excludes, columnCode)){
    			fieldCodes.append(columnCode+",");
    		}
    	}
    	fieldCodes.deleteCharAt(fieldCodes.length()-1);
    	return fieldCodes.toString();
    }
    /**
     * 获取系统级别的查询字段(含主键)
     * 当需要查询局部字段的时候，使用此方法获取到所有系统级别，然后再根据资源表把需查询业务字段拼接 
     * 如： 局部查询TEST_FIELD1,TEST_FIELD2和所有系统字段   "TEST_FIELD1,TEST_FIELD2,"+BeanUtils.getInstance().getSysQueryFields(表名)
     * @param tableCode 表名
     * @return 所有系统级别的字段按逗号隔开
     */
    public String getSysQueryFields(String tableCode){
    	DynaBean resourceTable=getResourceTable(tableCode);
    	List<DynaBean> columns=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
    	StringBuffer fieldCodes=new StringBuffer();
    	for(DynaBean column:columns){
    		if("SYS".equalsIgnoreCase(column.getStr("TABLECOLUMN_CLASSIFY"))){
    			String columnCode=column.getStr("TABLECOLUMN_CODE");
    			fieldCodes.append(columnCode+",");
    		}
    	}
    	fieldCodes.deleteCharAt(fieldCodes.length()-1);
    	return fieldCodes.toString();
    }
    /**
     * 获取业务级别的查询字段(不含主键)
     * 当需要局部查询业务字段数据时，使用此方法可以得到所有业务字段
     * 如：局部查询业务字段和主键    "主键,"+BeanUtils.getInstance().getSysQueryFields(表名)
     * @param tableCode 表名
     * @return 查询字段按逗号隔开
     */
    public String getProQueryFields(String tableCode){
    	DynaBean resourceTable=getResourceTable(tableCode);
    	List<DynaBean> columns=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
    	StringBuffer fieldCodes=new StringBuffer();
    	for(DynaBean column:columns){
    		if("PRO".equalsIgnoreCase(column.getStr("TABLECOLUMN_CLASSIFY"))){
    			String columnCode=column.getStr("TABLECOLUMN_CODE");
    			fieldCodes.append(columnCode+",");
    		}
    	}
    	fieldCodes.deleteCharAt(fieldCodes.length()-1);
    	return fieldCodes.toString();
    }
    /**
     * 获取查询字段
     * 当 需要查询所有字段想指定排除一些存储量大的字段，使用此方法可以完成操作
     * @param tableCode 表名
     * @param excludes 排除字段 
     * @return 查询字段按逗号隔开
     */
    public String getQueryFields(String tableCode,String[] excludes){
    	DynaBean resourceTable=getResourceTable(tableCode);
    	List<DynaBean> columns=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
    	StringBuffer fieldCodes=new StringBuffer();
    	for(DynaBean column:columns){
			String columnCode=column.getStr("TABLECOLUMN_CODE");
			if(!ArrayUtils.contains(excludes, columnCode)){
				fieldCodes.append(columnCode+",");
			}
    	}
    	fieldCodes.deleteCharAt(fieldCodes.length()-1);
    	return fieldCodes.toString();
    }
    /**
     * 获取不包含大文本的所有字段
     * 当需要查询数据用于操作时，一些大文本字段不会被操作和修改，则可以调用方法获取到查询字段(不包含大文本字段)
     * 大文本字段：MySql的text SQLServer的text Oracle的clob(clob字段尽量别查询，因为查询要构建值的过程需用到文件流，会暂用java的内存很大) 
     * @param tableCode 表名
     * @return 查询字段按逗号隔开
     */
    public String getNoClobQueryFields(String tableCode){
    	DynaBean resourceTable=getResourceTable(tableCode);
    	List<DynaBean> columns=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
    	StringBuffer fieldCodes=new StringBuffer();
    	for(DynaBean column:columns){
    		if(!ColumnType.CLOB.equalsIgnoreCase(column.getStr("TABLECOLUMN_TYPE")) || !ColumnType.BIGCLOB.equalsIgnoreCase(column.getStr("TABLECOLUMN_TYPE"))){
    			String columnCode=column.getStr("TABLECOLUMN_CODE");
				fieldCodes.append(columnCode+",");
    		}
    	}
    	fieldCodes.deleteCharAt(fieldCodes.length()-1);
    	return fieldCodes.toString();
    }
    /**
     * 清空指定表的缓存
     * @param tableCode
     */
    public void clearCache(String tableCode){
    	TableCacheManager.removeCache(tableCode);
//    	BeanUtilsHolder.tables.remove(tableCode);
    }
    /**
     * 清空表所有缓存
     */
    public void clearAll(){
    	TableCacheManager.clearAllCache();
//    	BeanUtilsHolder.tables.clear();
    }
    /**
     * 得到树形模版对象
     * @param tableCode
     * @return
     */
    public JSONTreeNode getTreeTemplate(String tableCode){
    	DynaBean resourceTable=getResourceTable(tableCode);
		List<DynaBean> tableColumns=(List<DynaBean>) resourceTable.get(BeanUtils.KEY_TABLE_COLUMNS);
		JSONTreeNode template=buildJSONTreeNodeTemplate(tableColumns);
		return template;
    }
    /**
     * 构建dyanBean的树形模版类
     * 排除bean的大文本字段
     * @param columns
     * @return
     */
    public JSONTreeNode buildJSONTreeNodeTemplate(List<DynaBean> columns){
    	JSONTreeNode node=new JSONTreeNode();
    	for(DynaBean column:columns){
    		if(StringUtil.isEmpty(column.getStr("TABLECOLUMN_TREETYPE"))){
    			node.setFieldCodes(node.getFieldCodes()+","+column.getStr("TABLECOLUMN_CODE")+"");
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.ID.toString())){
    			node.setId(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.TEXT.toString())){
    			node.setText(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.CODE.toString())){
    			node.setCode(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.PARENT.toString())){
    			node.setParent(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.NODEINFO.toString())){
    			node.setNodeInfo(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.NODEINFOTYPE.toString())){
    			node.setNodeInfoType(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.NODETYPE.toString())){
    			node.setNodeType(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.ICON.toString())){
    			node.setIcon(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.ICONCLS.toString())){
    			node.setIconCls(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.DISABLED.toString())){
    			node.setDisabled(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.NODEPATH.toString())){
    			node.setNodePath(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.PARENTPATH.toString())){
    			node.setParentPath(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.HREF.toString())){
    			node.setHref(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.HREFTARGET.toString())){
    			node.setHrefTarget(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.DESCRIPTION.toString())){
    			node.setDescription(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.ORDERINDEX.toString())){
    			node.setOrderIndex(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.LAYER.toString())){
    			node.setLayer(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.TREEORDERINDEX.toString())){
    			node.setTreeOrderIndex(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.ENFIELD.toString())){
    			node.setEnField(column.getStr("TABLECOLUMN_CODE"));
    		}else if(column.getStr("TABLECOLUMN_TREETYPE").equals(TreeNodeType.NONE.toString())){
    			
    		}else{
//    			if(!ColumnType.CLOB.equals(column.getStr("TABLECOLUMN_TYPE"))){
    				node.setFieldCodes(node.getFieldCodes()+","+column.getStr("TABLECOLUMN_CODE")+"");
//    			}
    		}
    	}
    	return node;
    }
}

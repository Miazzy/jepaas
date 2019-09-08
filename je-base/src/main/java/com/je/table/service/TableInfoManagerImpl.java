package com.je.table.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.je.cache.service.table.DynaCacheManager;
import com.je.core.constants.tree.NodeType;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.ArrayUtils;
import com.je.core.util.DataBaseUtils;
import com.je.core.util.DateUtils;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;

/**
 * 表信息修改  主要负责 键 列 索引的维护实现类
 */
@Component("tableInfoManager")
public class TableInfoManagerImpl implements TableInfoManager{
	private PCDynaServiceTemplate serviceTemplate;
	private TableManager tableManager;
	private PCServiceTemplate pcServiceTemplate;

	/**
	 * 添加列
	 * @param dynaBean
	 * @return
	 */
	@Override
	public DynaBean addField(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		DynaBean table=serviceTemplate.selectOne("JE_CORE_RESOURCETABLE"," and RESOURCETABLE_TABLECODE='"+dynaBean.getStr("TABLECOLUMN_TABLECODE")+"'");
		dynaBean.set("TABLECOLUMN_RESOURCETABLE_ID",table.getStr("JE_CORE_RESOURCETABLE_ID"));
		serviceTemplate.buildModelCreateInfo(dynaBean);
		int count=1;
		List<Map> countInfos=pcServiceTemplate.queryMapBySql("SELECT MAX(SY_ORDERINDEX) ORDERINDEX FROM JE_CORE_TABLECOLUMN WHERE TABLECOLUMN_CLASSIFY='PRO' AND TABLECOLUMN_RESOURCETABLE_ID='"+dynaBean.getStr("TABLECOLUMN_RESOURCETABLE_ID")+"'");
		if(countInfos!=null && countInfos.size()>0){
			String countStr=countInfos.get(0).get("ORDERINDEX")+"";
			if(StringUtil.isNotEmpty(countStr)){
				count=Integer.parseInt(countStr)+1;
			}
		}
		dynaBean.set("SY_ORDERINDEX", count);
		dynaBean=serviceTemplate.insert(dynaBean);
		tableManager.updateTable(dynaBean.getStr("TABLECOLUMN_RESOURCETABLE_ID"),true);
		return dynaBean;
	}

	/**
	 * 删除列
	 * @param dynaBean 自定义动态类
	 * @param ids TODO 暂不明确
	 * @param isDll TODO 咋不明确
	 * @return
	 */
	@Override
	public Integer removeColumn(DynaBean dynaBean, String ids,Boolean isDll) {
		// TODO Auto-generated method stub
		List<DynaBean> columns=serviceTemplate.selectList("JE_CORE_TABLECOLUMN"," and JE_CORE_TABLECOLUMN_ID in ("+StringUtil.buildArrayToString(ids.split(","))+")");
		List<DynaBean> keys=new ArrayList<DynaBean>();
		List<DynaBean> indexs=new ArrayList<DynaBean>();
		for(DynaBean column:columns){
			if("1".equals(column.getStr("TABLECOLUMN_ISCREATE"))){
				tableManager.saveTableTrace("JE_CORE_TABLECOLUMN", column, null, "DELETE", column.getStr("TABLECOLUMN_RESOURCETABLE_ID"));
			}
			if("FOREIGNKEY".equals(column.getStr("TABLECOLUMN_TYPE"))){
				DynaBean key=serviceTemplate.selectOne("JE_CORE_TABLEKEY", " AND TABLEKEY_RESOURCETABLE_ID='"+column.getStr("TABLECOLUMN_RESOURCETABLE_ID")+"' AND TABLEKEY_COLUMNCODE='"+column.getStr("TABLECOLUMN_CODE")+"'");
				if(key!=null){
					keys.add(key);
				}
			}
			List<DynaBean> columnIndexs=serviceTemplate.selectList("JE_CORE_TABLEINDEX"," AND TABLEINDEX_RESOURCETABLE_ID='"+column.getStr("TABLECOLUMN_RESOURCETABLE_ID")+"' AND TABLEINDEX_FIELDCODE='"+column.getStr("TABLECOLUMN_CODE")+"'");
			indexs.addAll(columnIndexs);
		}
		//级联删除key键
		for(DynaBean key:keys){
			if("1".equals(key.getStr("TABLEKEY_ISCREATE"))){
				tableManager.saveTableTrace("JE_CORE_TABLEKEY", key, null, "DELETE", key.getStr("TABLEKEY_RESOURCETABLE_ID"));
			}
		}
		for(DynaBean index:indexs){
			if("1".equals(index.getStr("TABLEINDEX_ISCREATE"))){
				tableManager.saveTableTrace("JE_CORE_TABLEINDEX", index, null, "DELETE", index.getStr("TABLEINDEX_RESOURCETABLE_ID"));
			}
		}
		tableManager.deleteKey(dynaBean.getStr("TABLECOLUMN_TABLECODE"), keys,"1");
		tableManager.deleteIndex(dynaBean.getStr("TABLECOLUMN_TABLECODE"), indexs);
		tableManager.deleteColumn(dynaBean.getStr("TABLECOLUMN_TABLECODE"), columns,isDll);
		BeanUtils.getInstance().clearCache(dynaBean.getStr("TABLECOLUMN_TABLECODE"));
		DynaCacheManager.removeCache(dynaBean.getStr("TABLECOLUMN_TABLECODE"));
		return columns.size();
	}

	/**
	 * 增量导入
	 * @param dynaBean
	 */
	@Override
	public void impNewCols(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		List<DynaBean> tableCols=serviceTemplate.selectList("JE_CORE_TABLECOLUMN", " and TABLECOLUMN_RESOURCETABLE_ID='"+dynaBean.getStr("JE_CORE_RESOURCETABLE_ID")+"'");
		DynaBean currentTable = DataBaseUtils.getTableBaseInfo(dynaBean);
		//当前表中真实存在的列
		List<DynaBean> currentCols = (List<DynaBean>) currentTable.get(BeanUtils.KEY_TABLE_COLUMNS);
		//列表中显示的列
		for(int x=0;x<currentCols.size();x++){
			DynaBean entity = currentCols.get(x);
			boolean flag = true;
			for(int i = 0;i < tableCols.size();i++){
				if(entity.getStr("TABLECOLUMN_CODE").equals(tableCols.get(i).getStr("TABLECOLUMN_CODE"))) {
					flag = false;
					break;
				}

			}
			if(flag){
				serviceTemplate.buildModelCreateInfo(entity);
				serviceTemplate.insert(entity);
			}
		}
	}

	/**
	 * 字典辅助添加列
	 * @param request
	 */
	@Override
	public void addColumnByDD(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String ids=request.getParameter("ids");
		String pkValue=request.getParameter("pkValue");
		String ddName=request.getParameter("ddName");
		String ddCode=request.getParameter("ddCode");
		String ddType=request.getParameter("ddType");
		int count=1;
		List<Map> countInfos=pcServiceTemplate.queryMapBySql("SELECT MAX(SY_ORDERINDEX) ORDERINDEX FROM JE_CORE_TABLECOLUMN WHERE TABLECOLUMN_CLASSIFY='PRO' AND TABLECOLUMN_RESOURCETABLE_ID='"+pkValue+"'");
		if(countInfos!=null && countInfos.size()>0){
			String countStr=countInfos.get(0).get("ORDERINDEX")+"";
			if(StringUtil.isNotEmpty(countStr)){
				count=Integer.parseInt(countStr)+1;
			}
		}
		String[] codeArray=ids.split(",");
		String queryField="";
		Boolean idField=false;
		for(String code:codeArray){
			if("CODE".equalsIgnoreCase(code.substring(code.lastIndexOf("_")+1))){
				queryField=code;
			}
		}
		if(StringUtil.isEmpty(queryField)){
			for(String code:codeArray){
				if("ID".equalsIgnoreCase(code.substring(code.lastIndexOf("_")+1))){
					queryField=code;
					idField=true;
				}
			}
		}
		String[] configInfoArray=new String[codeArray.length];
		for(int i=0;i<codeArray.length;i++){
			String code=codeArray[i];
			String type=code.substring(code.lastIndexOf("_")+1);
			if("name".equalsIgnoreCase(type)){
				configInfoArray[i]="text";
			}else if("code".equalsIgnoreCase(type)){
				configInfoArray[i]="code";
			}else if("id".equalsIgnoreCase(type)){
				configInfoArray[i]="id";
			}
		}
		for(String code:codeArray){
			DynaBean column=new DynaBean("JE_CORE_TABLECOLUMN",false);
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("TABLECOLUMN_CODE", code);
			String type=code.substring(code.lastIndexOf("_")+1);
			if("name".equalsIgnoreCase(type)){
				if(ArrayUtils.contains(new String[]{"DYNA_TREE","SQL_TREE","TREE","SQL","CUSTOM"}, ddType)){
					column.set("TABLECOLUMN_NAME", ddName);
				}else{
					column.set("TABLECOLUMN_NAME", ddName+"_NAME");
				}
			}else if("code".equalsIgnoreCase(type)){
				if(ArrayUtils.contains(new String[]{"DYNA_TREE","SQL_TREE","TREE","SQL","CUSTOM"}, ddType)){
					column.set("TABLECOLUMN_NAME", ddName+"_CODE");
				}else{
					column.set("TABLECOLUMN_NAME", ddName);
				}
			}else if("id".equalsIgnoreCase(type)){
				column.set("TABLECOLUMN_NAME", ddName+"_ID");
			}
			column.set("TABLECOLUMN_TYPE", "VARCHAR50");
			column.set("TABLECOLUMN_UNIQUE", "0");
			column.set("TABLECOLUMN_CLASSIFY", "PRO");
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set("TABLECOLUMN_ISNULL", "1");
			column.set("TABLECOLUMN_LENGTH", "");
			column.set("TABLECOLUMN_TREETYPE", "NORMAL");
			column.set("TABLECOLUMN_RESOURCETABLE_ID", pkValue);
			String configInfo=StringUtil.buildSplitString(configInfoArray, "~");
			//树形字典
			if(ArrayUtils.contains(new String[]{"DYNA_TREE","SQL_TREE","TREE","SQL","CUSTOM"}, ddType)){
				if("NAME".equalsIgnoreCase(code.substring(code.lastIndexOf("_")+1))){
					column.set("TABLECOLUMN_DICCONFIG", ddCode+","+ids.replace(",", "~")+","+configInfo+",S");
					column.set("TABLECOLUMN_DICQUERYFIELD", queryField);
				}
			}else{
				if("CODE".equalsIgnoreCase(code.substring(code.lastIndexOf("_")+1))){
					column.set("TABLECOLUMN_DICCONFIG", ddCode+","+ids.replace(",", "~")+","+configInfo+",S");
				}
			}
			serviceTemplate.buildModelCreateInfo(column);
			column.set("SY_ORDERINDEX", count);
			count++;
			serviceTemplate.insert(column);
		}
	}

	/**
	 * 字典辅助添加列
	 * @param request
	 */
	@Override
	public void addColumnByDDList(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String strData=request.getParameter("strData");
		String pkValue=request.getParameter("pkValue");
		String whereSql=request.getParameter("whereSql");
		int count=1;
		List<Map> countInfos=pcServiceTemplate.queryMapBySql("SELECT MAX(SY_ORDERINDEX) ORDERINDEX FROM JE_CORE_TABLECOLUMN WHERE TABLECOLUMN_CLASSIFY='PRO' AND TABLECOLUMN_RESOURCETABLE_ID='"+pkValue+"'");
		if(countInfos!=null && countInfos.size()>0){
			String countStr=countInfos.get(0).get("ORDERINDEX")+"";
			if(StringUtil.isNotEmpty(countStr)){
				count=Integer.parseInt(countStr)+1;
			}
		}
		JSONArray fields=JSONArray.fromObject(strData);
		for(int i=0;i<fields.size();i++){
			JSONObject field=fields.getJSONObject(i);
			String type=field.getString("type");
			String name=field.getString("text");
			String code=field.getString("name");
			String configInfo=field.getString("configInfo");
			String otherConfig=field.getString("otherConfig");
			JSONObject config=new JSONObject();
			config.put("other", otherConfig);
			config.put("where", whereSql);
			DynaBean column=new DynaBean("JE_CORE_TABLECOLUMN",true);
			column.set("TABLECOLUMN_NAME", name);
			column.set("TABLECOLUMN_CODE", code);
			column.set("TABLECOLUMN_TYPE", "VARCHAR50");
			column.set("TABLECOLUMN_UNIQUE", "0");
			column.set("TABLECOLUMN_CLASSIFY", "PRO");
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set("TABLECOLUMN_ISNULL", "1");
			column.set("TABLECOLUMN_LENGTH", "");
			column.set("TABLECOLUMN_TREETYPE", "NORMAL");
			column.set("TABLECOLUMN_RESOURCETABLE_ID", pkValue);
			column.set("TABLECOLUMN_DICCONFIG", configInfo);
			column.set("TABLECOLUMN_DICQUERYFIELD", config.toString());
			serviceTemplate.buildModelCreateInfo(column);
			column.set("SY_ORDERINDEX", count);
			count++;
			serviceTemplate.insert(column);
		}
	}

	/**
	 * 表辅助添加列
	 * @param request
	 * @return
	 */
	@Override
	public Integer addColumnByTable(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String strData=request.getParameter("strData");
		String tableCode=request.getParameter("tableCode");
		String pkValue=request.getParameter("pkValue");
		List<Map> sqlMapList = JsonBuilder.getInstance().fromJsonArray(strData);
		String targerTableCode=request.getParameter("TARGERTABLECODE");
		String createChild=request.getParameter("CREATECHILD");
		int count=1;
		List<Map> countInfos=pcServiceTemplate.queryMapBySql("SELECT MAX(SY_ORDERINDEX) ORDERINDEX FROM JE_CORE_TABLECOLUMN WHERE TABLECOLUMN_CLASSIFY='PRO' AND TABLECOLUMN_RESOURCETABLE_ID='"+pkValue+"'");
		if(countInfos!=null && countInfos.size()>0){
			String countStr=countInfos.get(0).get("ORDERINDEX")+"";
			if(StringUtil.isNotEmpty(countStr)){
				count=Integer.parseInt(countStr)+1;
			}
		}
		for(Map map:sqlMapList){
			//插入表字段
			DynaBean column=new DynaBean("JE_CORE_TABLECOLUMN",false);
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("TABLECOLUMN_NAME", map.get("TABLECOLUMN_NAME"));
			column.set("TABLECOLUMN_CODE", map.get("TABLECOLUMN_CLASSIFY"));
			column.set("TABLECOLUMN_UNIQUE","0");
			if("ID".equals(map.get("TABLECOLUMN_TYPE")+"")){
				column.set("TABLECOLUMN_TYPE","VARCHAR");
				column.set("TABLECOLUMN_LENGTH","50");
			}else{
				column.set("TABLECOLUMN_TYPE",map.get("TABLECOLUMN_TYPE"));
				column.set("TABLECOLUMN_LENGTH",map.get("TABLECOLUMN_LENGTH"));
			}
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set("TABLECOLUMN_ISNULL", "1");
			column.set("TABLECOLUMN_CLASSIFY", "PRO");
			column.set("TABLECOLUMN_TABLECODE", tableCode);
			column.set("TABLECOLUMN_RESOURCETABLE_ID", pkValue);
			column.set("TABLECOLUMN_TREETYPE", "NORMAL");
			if(map.containsKey("TABLECOLUMN_DICCONFIG")) {
				String dicConfig=map.get("TABLECOLUMN_DICCONFIG")+"";
				if(StringUtil.isNotEmpty(dicConfig)) {
					for(Map vals:sqlMapList){
						dicConfig=dicConfig.replace(vals.get("TABLECOLUMN_CODE")+"", vals.get("TABLECOLUMN_CLASSIFY")+"");
					}
					column.setStr("TABLECOLUMN_DICCONFIG", dicConfig);
				}
			}
			if(map.containsKey("TABLECOLUMN_DICQUERYFIELD")) {
				String dicQueryConfig=map.get("TABLECOLUMN_DICQUERYFIELD")+"";
				if(StringUtil.isNotEmpty(dicQueryConfig)) {
					for(Map vals:sqlMapList){
						dicQueryConfig=dicQueryConfig.replace(vals.get("TABLECOLUMN_CODE")+"", vals.get("TABLECOLUMN_CLASSIFY")+"");
					}
					column.setStr("TABLECOLUMN_DICQUERYFIELD", dicQueryConfig);
				}
			}
//			if(map.containsKey("TABLECOLUMN_QUERYCONFIG")) {
//				String queryConfig=map.get("TABLECOLUMN_QUERYCONFIG")+"";
//				if(StringUtil.isNotEmpty(queryConfig)) {
//					String[] queryArray=queryConfig.split(",");
//					if(queryArray.length>1){
//						String funcFields=queryArray[1];
//						for(Map vals:sqlMapList){
//							funcFields=funcFields.replace(vals.get("TABLECOLUMN_CODE")+"",vals.get("TABLECOLUMN_CLASSIFY")+"");
//						}
//						queryArray[1]=funcFields;
//					}
//					column.setStr("TABLECOLUMN_QUERYCONFIG", StringUtil.buildSplitString(queryArray, ","));
//				}
//			}
			if("1".equals(createChild)){
				column.set("TABLECOLUMN_CHILDCONFIG", targerTableCode+","+map.get("TABLECOLUMN_CODE"));
			}
			serviceTemplate.buildModelCreateInfo(column);
			if("1".equals(map.get("TABLECOLUMN_ISNULL")+"")){
				//查询选择配置
				if(StringUtil.isNotEmpty(request.getParameter("FUNCCODE"))){
					column.set("TABLECOLUMN_QUERYCONFIG", request.getParameter("FUNCCODE")+","+request.getParameter("QUERYSTR"));
				}
			}
			//使用外键
			if("1".equals(map.get("TABLECOLUMN_ISCREATE")+"")){
				column.set("TABLECOLUMN_TYPE", "FOREIGNKEY");
				column.set("TABLECOLUMN_LENGTH","");
				//生成外键
				DynaBean forenignKey=new DynaBean("JE_CORE_TABLEKEY",false);
				forenignKey.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLEKEY_ID");
				String nowDateTime=DateUtils.formatDateTime(new Date());
				nowDateTime=nowDateTime.replaceAll("-", "");
				nowDateTime=nowDateTime.replaceAll(" ", "");
				nowDateTime=nowDateTime.replaceAll(":", "");
				forenignKey.set("TABLEKEY_CODE","JE_"+nowDateTime);
				forenignKey.set("TABLEKEY_COLUMNCODE",column.get("TABLECOLUMN_CODE"));
				forenignKey.set("TABLEKEY_TYPE","Foreign");
				forenignKey.set("TABLEKEY_CHECKED","1");
				forenignKey.set("TABLEKEY_LINKTABLE",targerTableCode);
				forenignKey.set("TABLEKEY_LINECOLUMNCODE",map.get("TABLECOLUMN_CODE"));
				forenignKey.set("TABLEKEY_LINETYLE","Cascade");
				forenignKey.set("TABLEKEY_ISRESTRAINT","1");
				forenignKey.set("TABLEKEY_RESOURCETABLE_ID",pkValue);
				forenignKey.set("TABLEKEY_TABLECODE",tableCode);
				forenignKey.set("TABLEKEY_ISCREATE","0");
				forenignKey.set("TABLEKEY_CLASSIFY","PRO");
				forenignKey.set("SY_ORDERINDEX",1);
				serviceTemplate.buildModelCreateInfo(forenignKey);
				serviceTemplate.insert(forenignKey);
			}
			column.set("SY_ORDERINDEX", count);
			count++;
			serviceTemplate.insert(column);
		}
		return sqlMapList.size();
	}

	/**
	 * 原子辅助添加列
	 * @param strData TODO 暂不明确
	 * @param tableCode 表编码
	 * @param pkValue 主键
	 * @return
	 */
	@Override
	public Integer addColumnByAtom(String strData,String tableCode,String pkValue) {
		// TODO Auto-generated method stub
		List<Map> sqlMapList = JsonBuilder.getInstance().fromJsonArray(strData);
		int count=1;
		List<Map> countInfos=pcServiceTemplate.queryMapBySql("SELECT MAX(SY_ORDERINDEX) ORDERINDEX FROM JE_CORE_TABLECOLUMN WHERE TABLECOLUMN_CLASSIFY='PRO' AND TABLECOLUMN_RESOURCETABLE_ID='"+pkValue+"'");
		if(countInfos!=null && countInfos.size()>0){
			String countStr=countInfos.get(0).get("ORDERINDEX")+"";
			if(StringUtil.isNotEmpty(countStr)){
				count=Integer.parseInt(countStr)+1;
			}
		}
		for(Map map:sqlMapList){
			//插入表字段
			DynaBean column=new DynaBean("JE_CORE_TABLECOLUMN",false);
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("TABLECOLUMN_NAME", map.get("ATOMCOLUMN_NAME"));
			column.set("TABLECOLUMN_CODE", map.get("ATOMCOLUMN_CONTEXT"));
			column.set("TABLECOLUMN_UNIQUE", "0");
			column.set("TABLECOLUMN_TYPE",map.get("ATOMCOLUMN_TYPE"));
			column.set("TABLECOLUMN_LENGTH",map.get("ATOMCOLUMN_LENGTH"));
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set("TABLECOLUMN_ISNULL", "1");
			column.set("TABLECOLUMN_CLASSIFY", "PRO");
			column.set("TABLECOLUMN_TABLECODE", tableCode);
			column.set("TABLECOLUMN_RESOURCETABLE_ID", pkValue);
			column.set("TABLECOLUMN_TREETYPE", "NORMAL");
			column.set("SY_ORDERINDEX", count);
			count++;
			serviceTemplate.buildModelCreateInfo(column);
			serviceTemplate.insert(column);
		}
		return sqlMapList.size();
	}

	/**
	 * 存为原子
	 * @param strData TODO 暂不明确
	 * @param pkValue 主键
	 * @return
	 */
	@Override
	public Integer addAtomByColumn(String strData,String pkValue) {
		// TODO Auto-generated method stub
		List<Map> sqlMapList = JsonBuilder.getInstance().fromJsonArray(strData);
		for(Map map:sqlMapList){
			DynaBean atomColumn=new DynaBean("JE_CORE_ATOMCOLUMN",false);
			atomColumn.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_ATOMCOLUMN");
			atomColumn.set("ATOMCOLUMN_CODE", map.get("ATOMCOLUMN_CONTEXT"));
			atomColumn.set("ATOMCOLUMN_ISNULL", map.get("ATOMCOLUMN_ISNULL"));
			atomColumn.set("ATOMCOLUMN_NAME", map.get("ATOMCOLUMN_NAME"));
			atomColumn.set("ATOMCOLUMN_ATOM_ID", pkValue);
			atomColumn.set("ATOMCOLUMN_LENGTH", map.get("ATOMCOLUMN_LENGTH"));
			atomColumn.set("ATOMCOLUMN_TYPE", map.get("ATOMCOLUMN_TYPE"));
			serviceTemplate.buildModelCreateInfo(atomColumn);
			serviceTemplate.insert(atomColumn);
		}
		return sqlMapList.size();
	}
	@Resource(name="PCDynaServiceTemplate")
	public void setServiceTemplate(PCDynaServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}
	@Resource(name="tableManager")
	public void setTableManager(TableManager tableManager) {
		this.tableManager = tableManager;
	}
	@Resource(name="PCServiceTemplateImpl")
	public void setPcServiceTemplate(PCServiceTemplate pcServiceTemplate) {
		this.pcServiceTemplate = pcServiceTemplate;
	}

	/**
	 * 删除索引
	 * @param dynaBean 自定义动态类
	 * @param ids TODO 暂不明确
	 * @return
	 */
	@Override
	public Integer removeIndex(DynaBean dynaBean,String ids) {
		// TODO Auto-generated method stub
		List<DynaBean> indexs=serviceTemplate.selectList("JE_CORE_TABLEINDEX"," and JE_CORE_TABLEINDEX_ID in ("+StringUtil.buildArrayToString(ids.split(","))+")");
		for(DynaBean index:indexs){
			if("1".equals(index.getStr("TABLEINDEX_ISCREATE"))){
				tableManager.saveTableTrace("JE_CORE_TABLEINDEX", index, null, "DELETE", index.getStr("TABLEINDEX_RESOURCETABLE_ID"));
			}
		}
		tableManager.deleteIndex(dynaBean.getStr("TABLEINDEX_TABLECODE"), indexs);
		BeanUtils.getInstance().clearCache(dynaBean.getStr("TABLEINDEX_TABLECODE"));
		DynaCacheManager.removeCache(dynaBean.getStr("TABLEINDEX_TABLECODE"));
		return indexs.size();
	}

	/**
	 * 删除键
	 * @param dynaBean 自定义动态类
	 * @param ids TODO 暂不明确
	 * @param ddl TODO 暂不明确
	 * @return
	 */
	@Override
	public Integer removeKey(DynaBean dynaBean, String ids,String ddl) {
		// TODO Auto-generated method stub
		List<DynaBean> keys=serviceTemplate.selectList("JE_CORE_TABLEKEY"," and JE_CORE_TABLEKEY_ID in ("+StringUtil.buildArrayToString(ids.split(","))+")");
		for(DynaBean key:keys){
			if("1".equals(key.getStr("TABLEKEY_ISCREATE"))){
				tableManager.saveTableTrace("JE_CORE_TABLEKEY", key, null, "DELETE", key.getStr("TABLEKEY_RESOURCETABLE_ID"));
			}
		}
		tableManager.deleteKey(dynaBean.getStr("TABLEKEY_TABLECODE"), keys,ddl);
		BeanUtils.getInstance().clearCache(dynaBean.getStr("TABLEKEY_TABLECODE"));
		DynaCacheManager.removeCache(dynaBean.getStr("TABLEKEY_TABLECODE"));
		return keys.size();
	}

	/**
	 * 更新树形表数据(路径 层次 顺序 排序字段)
	 * @param tableCode 表编码
	 * @param pkCode 主键code
	 * @param preFix TODO 暂不明确
	 */
	@Override
	public void syncTreePath(String tableCode,String pkCode,String preFix) {
		// TODO Auto-generated method stub
		syncPath(tableCode, pkCode, preFix, "ROOT");
	}
	private void syncPath(String tableCode,String pkCode,String preFix,String parentId) {
		String parentField=preFix+"PARENT";
		String pathField=preFix+"PATH";
		String parentPathField=preFix+"PARENTPATH";
		String treeOrderField=preFix+"TREEORDERINDEX";
		String layerField=preFix+"LAYER";
		String nodeTypeField=preFix+"NODETYPE";
		String orderField=preFix+"ORDERINDEX";
		String[] fields=new String[] {pkCode,parentField,pathField,parentPathField,treeOrderField,layerField,nodeTypeField,orderField};
		String querySql=" SELECT "+StringUtil.buildSplitString(fields, ",")+" FROM "+tableCode+" WHERE "+parentField+"='"+parentId+"' ORDER BY "+orderField;
		if(NodeType.ROOT.equalsIgnoreCase(parentId)) {
			pcServiceTemplate.executeSql(" UPDATE "+tableCode+" SET "+nodeTypeField+"='ROOT',"+pathField+"='/ROOT',"+layerField+"='0',"+treeOrderField+"='000001',"+parentPathField+"='' WHERE "+pkCode+"='"+parentId+"'");
		}
		List<Map> parentNodes=pcServiceTemplate.queryMapBySql(" SELECT "+StringUtil.buildSplitString(fields, ",")+" FROM "+tableCode+" WHERE "+pkCode+"='"+parentId+"'");
		if(parentNodes.size()<0)return;
		Map parent=parentNodes.get(0);
		List<Map> lists=pcServiceTemplate.queryMapBySql(querySql);
		int index=1;
		for(Map vals:lists) {
			pcServiceTemplate.executeSql(" UPDATE "+tableCode+" SET "+nodeTypeField+"='"+NodeType.LEAF+"',"+pathField+"='"+(parent.get(pathField)+"/"+vals.get(pkCode))+"',"+layerField+"='"+(Integer.parseInt(parent.get(layerField)+"")+1)+"',"+treeOrderField+"='"+(parent.get(treeOrderField)+StringUtil.preFillUp(index+"", 6, '0'))+"',"+parentPathField+"='"+parent.get(pathField)+"' WHERE "+pkCode+"='"+vals.get(pkCode)+"'");
			index++;
			String id=vals.get(pkCode)+"";
			syncPath(tableCode, pkCode, preFix, id);
		}
		if(lists.size()>0 && !NodeType.ROOT.equalsIgnoreCase(parentId)) {
			pcServiceTemplate.executeSql(" UPDATE "+tableCode+" SET "+nodeTypeField+"='"+NodeType.GENERAL+"' WHERE "+pkCode+"='"+parentId+"'");
		}
	}

}

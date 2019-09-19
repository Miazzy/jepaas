package com.je.rbac.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.je.core.constants.table.TableType;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.core.service.PCDynaBeanTemplate;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.table.service.TableManager;
@Component("sentryParamManager")
public class SentryParamManagerImpl implements SentryParamManager {
	private PCDynaServiceTemplate serviceTemplate;
	private PCDynaBeanTemplate pcDynaBeanTemplate;
	private TableManager tableManager;

	/**
	 * 保存
	 * @param dynaBean 自定义动态类
	 * @return
	 */
	@Override
	public Boolean doSave(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		String fieldCode=dynaBean.getStr("SENTRYPARAMS_FIELDCODE");
		String sentryCode=dynaBean.getStr("SENTRYPARAMS_SENTRYCODE");
		String tableCode=dynaBean.getStr("SENTRYPARAMS_TABLECODE");
		String xtype=dynaBean.getStr("SENTRYPARAMS_TYPECODE");
		Long count=serviceTemplate.selectCount("JE_CORE_SENTRYPARAMS"," AND SENTRYPARAMS_SENTRY_ID='"+dynaBean.getStr("SENTRYPARAMS_SENTRY_ID")+"' AND SENTRYPARAMS_FIELDCODE='"+fieldCode+"'");
		if(StringUtil.isNotEmpty(fieldCode) && count<=0){
			DynaBean table=serviceTemplate.selectOne("JE_CORE_RESOURCETABLE"," AND RESOURCETABLE_TYPE!='"+TableType.MODULETABLE+"' AND RESOURCETABLE_TABLECODE='"+tableCode+"'");
			DynaBean column=new DynaBean("JE_CORE_TABLECOLUMN",false);
			column.set(BeanUtils.KEY_PK_CODE, "JE_CORE_TABLECOLUMN_ID");
			column.set("TABLECOLUMN_NAME", dynaBean.getStr("SENTRYPARAMS_NAME"));
			column.set("TABLECOLUMN_CODE",sentryCode.toUpperCase()+"_"+fieldCode.toUpperCase());
			if(xtype.equals("treessareafield") || xtype.equals("gridssareafield")|| xtype.equals("textarea") || xtype.equals("ckeditor")){
				column.set("TABLECOLUMN_TYPE", "CLOB");
				column.set("TABLECOLUMN_LENGTH","");
			}else{
				column.set("TABLECOLUMN_TYPE", "VARCHAR255");
			}
			column.set("TABLECOLUMN_ISCREATE", "0");
			column.set("TABLECOLUMN_ISNULL", "1");
			column.set("TABLECOLUMN_UNIQUE", "0");
			column.set("TABLECOLUMN_CLASSIFY", "SYS");
			column.set("TABLECOLUMN_TREETYPE","NORMAL");
			column.set("TABLECOLUMN_RESOURCETABLE_ID",table.getStr("JE_CORE_RESOURCETABLE_ID"));
			column.set("TABLECOLUMN_TABLECODE", tableCode);
			serviceTemplate.buildModelCreateInfo(column);
			serviceTemplate.insert(column);
			tableManager.updateTable(table.getStr("JE_CORE_RESOURCETABLE_ID"), false);
			BeanUtils.getInstance().clearCache(table.getStr("RESOURCETABLE_TABLECODE"));
			dynaBean.set("SENTRYPARAMS_OLDFIELDCODE", fieldCode);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 列表更新
	 * @param strData 开始时间
	 * @param dynaBean 自定义动态类
	 * @return
	 */
	@Override
	public Integer doUpdateList(String strData,DynaBean dynaBean) {
		// TODO Auto-generated method stub
		Set<String> clobSet=new HashSet<String>();
		clobSet.add("treessareafield");
		clobSet.add("gridssareafield");
		clobSet.add("textarea");
		clobSet.add("ckeditor");
		//原有更新方法
		List<Map> sqlMapList = JsonBuilder.getInstance().fromJsonArray(strData);
		Boolean flag=false;
		DynaBean table=null;
		String[] idsArray=new String[sqlMapList.size()];
		for(int i=0; i<sqlMapList.size(); i++) {
			Map sqlMap = sqlMapList.get(i);
			String id = StringUtil.getDefaultValue(sqlMap.get("JE_CORE_SENTRYPARAMS_ID"), "");
			idsArray[i]=id;
			String fieldCode=StringUtil.getDefaultValue(sqlMap.get("SENTRYPARAMS_FIELDCODE"), "");
			String fieldType=StringUtil.getDefaultValue(sqlMap.get("SENTRYPARAMS_TYPECODE"), "");
			if(StringUtil.isNotEmpty(id)){
				DynaBean param=serviceTemplate.selectOneByPk("JE_CORE_SENTRYPARAMS", id);
				String sentryCode=param.getStr("SENTRYPARAMS_SENTRYCODE").toUpperCase();
				if(table==null){
					table=serviceTemplate.selectOne("JE_CORE_RESOURCETABLE"," AND RESOURCETABLE_TYPE!='"+TableType.MODULETABLE+"' AND RESOURCETABLE_TABLECODE='"+param.getStr("SENTRYPARAMS_TABLECODE")+"'");
				}
				DynaBean column=serviceTemplate.selectOne("JE_CORE_TABLECOLUMN"," AND TABLECOLUMN_RESOURCETABLE_ID='"+table.getStr("JE_CORE_RESOURCETABLE_ID")+"' AND TABLECOLUMN_CODE='"+sentryCode+"_"+param.getStr("SENTRYPARAMS_FIELDCODE").toUpperCase()+"'");
				Boolean isUpdate=false;
				if(StringUtil.isNotEmpty(fieldCode) && !column.getStr("TABLECOLUMN_CODE").equalsIgnoreCase(sentryCode+"_"+fieldCode)){
					column.set("TABLECOLUMN_CODE", sentryCode+"_"+fieldCode);
					flag=true;
					isUpdate=true;
				}
				if(StringUtil.isNotEmpty(fieldType) && !fieldType.equals(param.getStr("SENTRYPARAMS_TYPECODE"))){
					if(clobSet.contains(fieldType) && !("CLOB".equalsIgnoreCase(column.getStr("TABLECOLUMN_TYPE")) || "BIGCLOB".equalsIgnoreCase(column.getStr("TABLECOLUMN_TYPE")))){
						column.set("TABLECOLUMN_TYPE", "CLOB");
						column.set("TABLECOLUMN_LENGTH", "");
					}else{
						column.set("TABLECOLUMN_TYPE", "VARCHAR255");
					}
					flag=true;
					isUpdate=true;
				}
				column.set(BeanUtils.KEY_TABLE_CODE, "JE_CORE_TABLECOLUMN");
				if(isUpdate){
					serviceTemplate.update(column);
				}
			}
		}
		if(flag){
			tableManager.updateTable(table.getStr("JE_CORE_RESOURCETABLE_ID"), false);
			BeanUtils.getInstance().clearCache(table.getStr("RESOURCETABLE_TABLECODE"));
		}
//		String[] updateSqls =JsonBuilder.getInstance().jsonSqlToString(dynaBean,strData);
//		int rows = serviceTemplate.listUpdate(updateSqls);
		List<DynaBean> updateLists=pcDynaBeanTemplate.doUpdateList(dynaBean, strData);
		return updateLists.size();
	}

	/**
	 * 删除
	 * @param ids id
	 * @return
	 */
	@Override
	public Integer doRemove(String ids) {
		// TODO Auto-generated method stub
		String[] idArray=ids.split(",");
		List<DynaBean> columnList=new ArrayList<DynaBean>();
		DynaBean table=null;
		String tableCode="";
		for(Integer i=0;i<idArray.length;i++){
			String id=idArray[i];
			DynaBean param=serviceTemplate.selectOneByPk("JE_CORE_SENTRYPARAMS", id);
			if(table==null){
				tableCode=param.getStr("SENTRYPARAMS_TABLECODE");
				table=serviceTemplate.selectOne("JE_CORE_RESOURCETABLE"," AND RESOURCETABLE_TYPE!='"+TableType.MODULETABLE+"' AND RESOURCETABLE_TABLECODE='"+param.getStr("SENTRYPARAMS_TABLECODE")+"'");
			}
			DynaBean column=serviceTemplate.selectOne("JE_CORE_TABLECOLUMN"," AND TABLECOLUMN_RESOURCETABLE_ID='"+table.getStr("JE_CORE_RESOURCETABLE_ID")+"' AND TABLECOLUMN_CODE='"+param.getStr("SENTRYPARAMS_SENTRYCODE").toUpperCase()+"_"+param.getStr("SENTRYPARAMS_FIELDCODE").toUpperCase()+"'");
			if(column!=null){
				columnList.add(column);
			}
		}
		if(columnList.size()>0){
			tableManager.deleteColumn(tableCode, columnList,true);
			BeanUtils.getInstance().clearCache(tableCode);
		}
		return idArray.length;
	}
	@Resource(name="PCDynaServiceTemplate")
	public void setServiceTemplate(PCDynaServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}
	@Resource(name="tableManager")
	public void setTableManager(TableManager tableManager) {
		this.tableManager = tableManager;
	}
	@Resource(name="dynaBeanTemplate")
	public void setPcDynaBeanTemplate(PCDynaBeanTemplate pcDynaBeanTemplate) {
		this.pcDynaBeanTemplate = pcDynaBeanTemplate;
	}


}

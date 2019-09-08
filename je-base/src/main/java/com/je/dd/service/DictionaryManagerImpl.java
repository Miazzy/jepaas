package com.je.dd.service;

import com.je.cache.service.dic.DicCacheManager;
import com.je.cache.service.dic.DicInfoCacheManager;
import com.je.cache.service.dic.DicQuickCacheManager;
import com.je.cache.service.config.BackCacheManager;
import com.je.cache.service.config.FrontCacheManager;
import com.je.core.constants.ConstantVars;
import com.je.core.constants.dd.DDSQLListType;
import com.je.core.constants.dd.DDType;
import com.je.core.constants.tree.NodeType;
import com.je.core.constants.tree.TreeNodeType;
import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.core.util.SecurityUserHolder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.ArrayUtils;
import com.je.core.util.ReflectionUtils;
import com.je.core.util.SpringContextHolder;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.dd.vo.DicInfoVo;
import com.je.dd.vo.DictionaryItemVo;
import com.je.rbac.model.EndUser;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
/**
 * 改动，将所有纯sql语句的查询把表名更换
 * @author zhangshuaipeng
 *
 */
@Service("dictionaryManager")
public class DictionaryManagerImpl implements DictionaryManager {
	private static Logger logger = LoggerFactory.getLogger(DictionaryManagerImpl.class);
	@Autowired
	private PCDynaServiceTemplate pcDynaServiceTemplate;
	@Autowired
	private PCServiceTemplate pcServiceTemplate;

	/**
	 * 获取指定列表字典的字典项
	 * @param whereSql 查询sql
	 * @return
	 */
	public JSONObject getAllListDicItem(String whereSql){
		JSONObject returnObj=new JSONObject();
		List<DynaBean> dictionarys=pcDynaServiceTemplate.selectList("JE_CORE_DICTIONARY", whereSql);
		for(DynaBean dictionary:dictionarys){
			String ddCode=dictionary.getStr("DICTIONARY_DDCODE");
			List<DictionaryItemVo> voList=new ArrayList<DictionaryItemVo>();
			buildChildrenList(voList, dictionary,false, new QueryInfo(), "","0");
			returnObj.put(ddCode, voList);
		}


		return returnObj;
	}

	/**
	 * 为voList封装视图对象
	 * @param voList TODO 暂不明确
	 * @param dic TODO 暂不明确
	 * @param en
	 * @param queryInfo TODO 暂不明确
	 * @param itemCode TODO 暂不明确
	 * @param zwfFlag TODO 暂不明确
	 */
	@Override
	public void buildChildrenList(List<DictionaryItemVo> voList, DynaBean dic,Boolean en, QueryInfo queryInfo, String itemCode,String zwfFlag) {
		StringBuffer whereSql = new StringBuffer();
		if(StringUtil.isNotEmpty(itemCode)) {
			//whereSql.append(" and SY_STATUS='1'");
			whereSql.append(" and SY_FLAG='1' and SY_NODETYPE != 'ROOT' and DICTIONARYITEM_DICTIONARY_ID='").append(dic.getStr("JE_CORE_DICTIONARY_ID")).append("' and DICTIONARYITEM_ITEMCODE='").append(itemCode).append(StringUtil.SINGLE_QUOTES).append(queryInfo.getWhereSql());
		} else {
			//whereSql.append(" and SY_STATUS='1'");
			whereSql.append(" and SY_FLAG='1' and SY_NODETYPE != 'ROOT' and DICTIONARYITEM_DICTIONARY_ID='").append(dic.getStr("JE_CORE_DICTIONARY_ID")).append(StringUtil.SINGLE_QUOTES).append(queryInfo.getWhereSql());
		}
		if("1".equals(zwfFlag)){
			EndUser currentUser=SecurityUserHolder.getCurrentUser();
			whereSql.append(" AND SY_ZHID='"+currentUser.getZhId()+"'");
		}
//		whereSql.append(" ORDER BY SY_ORDERINDEX");
		if(StringUtil.isEmpty(queryInfo.getOrderSql())){
			queryInfo.setOrderSql(" ORDER BY SY_ORDERINDEX");
		}
		DynaBean itemBean=new DynaBean("JE_CORE_DICTIONARYITEM",false);
		itemBean.set(BeanUtils.KEY_PK_CODE, "JE_CORE_DICTIONARYITEM_ID");
		itemBean.set(BeanUtils.KEY_WHERE, whereSql.toString());
		itemBean.set(BeanUtils.KEY_ORDER, queryInfo.getOrderSql());
		if("1".equals(zwfFlag)){
			itemBean.set(BeanUtils.KEY_TABLE_CODE, "JE_JTGS_DICTIONARYITEM");
			itemBean.set(BeanUtils.KEY_PK_CODE, "JE_JTGS_DICTIONARYITEM_ID");
			itemBean.set(BeanUtils.KEY_QUERY_FIELDS, "JE_JTGS_DICTIONARYITEM_ID,"+BeanUtils.getInstance().getProQueryFields("JE_JTGS_DICTIONARYITEM"));
		}else{
			itemBean.set(BeanUtils.KEY_PK_CODE, "JE_CORE_DICTIONARYITEM_ID");
			itemBean.set(BeanUtils.KEY_QUERY_FIELDS, "JE_CORE_DICTIONARYITEM_ID,"+BeanUtils.getInstance().getProQueryFields("JE_CORE_DICTIONARYITEM"));
		}
		List<DynaBean> items=pcDynaServiceTemplate.selectList(itemBean);
		for(int i=0; i<items.size(); i++) {
			DynaBean item = items.get(i);
			DictionaryItemVo vo = new DictionaryItemVo();
			if("1".equals(zwfFlag)){
				vo.setId(item.getStr("JE_JTGS_DICTIONARYITEM_ID"));
			}else{
				vo.setId(item.getStr("JE_CORE_DICTIONARYITEM_ID"));
			}
			vo.setCode(item.getStr("DICTIONARYITEM_ITEMCODE"));
			vo.setIcon(item.getStr("DICTIONARYITEM_REFPHOTO"));
			vo.setText(item.getStr("DICTIONARYITEM_ITEMNAME"));
			if(en){
				vo.setText(item.getStr("DICTIONARYITEM_ITEMNAME_EN"));
			}
			vo.setTextColor(item.getStr("DICTIONARYITEM_FONTCOLOR"));
			vo.setIconCls(item.getStr("DICTIONARYITEM_ICONCLS"));
			vo.setBackgroundColor(item.getStr("DICTIONARYITEM_BACKGROUNDCOLOR"));
			voList.add(vo);
		}
	}

	/**
	 * 缓存所有列表字典
	 */
	@Override
	public void doProAllListDicItem() {
		// TODO Auto-generated method stub
		List<DynaBean> dictionarys=pcDynaServiceTemplate.selectList("JE_CORE_DICTIONARY", " and DICTIONARY_DDTYPE='"+DDType.LIST+"'","JE_CORE_DICTIONARY_ID,DICTIONARY_DDCODE,DICTIONARY_ITEMROOT_ID,DICTIONARY_WHERESQL,DICTIONARY_WHERESQL");
		for(DynaBean dictionary:dictionarys){
			String ddCode=dictionary.getStr("DICTIONARY_DDCODE");
			String ddWhereSql=dictionary.getStr("DICTIONARY_WHERESQL","");
//			String ddOrderSql=dictionary.getStr("DICTIONARY_ORDERSQL","");
			List<DynaBean> items=pcDynaServiceTemplate.selectList("JE_CORE_DICTIONARYITEM", ddWhereSql+" AND DICTIONARYITEM_DICTIONARY_ID='"+dictionary.getStr("JE_CORE_DICTIONARY_ID")+"' AND (SY_FLAG='1' OR SY_NODETYPE='ROOT') ORDER BY SY_LAYER ASC,SY_ORDERINDEX ASC");
			List<DictionaryItemVo> voList=new ArrayList<DictionaryItemVo>();
			for(int i=0; i<items.size(); i++) {
				DynaBean item = items.get(i);
				if(NodeType.ROOT.equalsIgnoreCase(item.getStr("SY_NODETYPE")) || !"1".equals(item.getStr("SY_FLAG"))){
					continue;
				}
				DictionaryItemVo vo = new DictionaryItemVo();
				vo.setId(item.getStr("JE_CORE_DICTIONARYITEM_ID"));
				vo.setCode(item.getStr("DICTIONARYITEM_ITEMCODE"));
				vo.setIcon(item.getStr("DICTIONARYITEM_REFPHOTO"));
				vo.setText(item.getStr("DICTIONARYITEM_ITEMNAME"));
				vo.setTextColor(item.getStr("DICTIONARYITEM_FONTCOLOR"));
				vo.setIconCls(item.getStr("DICTIONARYITEM_ICONCLS"));
				vo.setBackgroundColor(item.getStr("DICTIONARYITEM_BACKGROUNDCOLOR"));
				voList.add(vo);
			}
			String ddValueStr=JsonBuilder.getInstance().buildObjListToJson(new Long(voList.size()), voList, false);
			DicCacheManager.putCache(ddCode, ddValueStr);
			List<JSONTreeNode> jsonTreeNodeList=new ArrayList<JSONTreeNode>();
			for(DynaBean item:items){
				JSONTreeNode n=new JSONTreeNode();
				if(!"1".equals(item.getStr("SY_FLAG")) && !NodeType.ROOT.equalsIgnoreCase(item.getStr("SY_NODETYPE"))){
					continue;
				}
				n.setChecked(false);
				n.setChildren(new ArrayList<JSONTreeNode>());
				n.setCode(item.getStr("DICTIONARYITEM_ITEMCODE"));
				n.setDisabled("0");
				n.setEnField("DICTIONARYITEM_ITEMNAME_EN");
				n.setIconCls(item.getStr("DICTIONARYITEM_ICONCLS"));
				n.setId(item.getStr("JE_CORE_DICTIONARYITEM_ID"));
				n.setLayer(item.getStr("SY_LAYER","0"));
				if(NodeType.LEAF.equalsIgnoreCase(item.getStr("SY_NODETYPE"))){
					n.setLeaf(true);
				}else{
					n.setLeaf(false);
				}
				n.setNodeInfo(item.getStr("DICTIONARYITEM_NODEINFO"));
				n.setNodeInfoType(item.getStr("DICTIONARYITEM_NODEINFOTYPE"));
				n.setNodePath(item.getStr("SY_PATH"));
				n.setNodeType(item.getStr("SY_NODETYPE"));
				n.setOrderIndex(item.getStr("SY_ORDERINDEX"));
				n.setParent(item.getStr("SY_PARENT"));
				n.setParentPath(item.getStr("SY_PARENTPATH"));
				n.setText(item.getStr("DICTIONARYITEM_ITEMNAME"));
				n.setTreeOrderIndex(item.getStr("SY_TREEORDERINDEX"));
				n.setBean(new HashMap<String,Object>());
				jsonTreeNodeList.add(n);
			}
			DicQuickCacheManager.putCache(ddCode, jsonTreeNodeList);



//			buildChildrenList(voList, dictionary,false,new QueryInfo(), "","0");
//			//放入字段快速查询缓存
//			String rootId=dictionary.getStr("DICTIONARY_ITEMROOT_ID");
//			String tableName="JE_CORE_DICTIONARYITEM";
//			String ddWhereSql=dictionary.getStr("DICTIONARY_WHERESQL","");
//			String ddOrderSql=dictionary.getStr("DICTIONARY_ORDERSQL","");
//			String querySql=ddWhereSql+" and DICTIONARYITEM_DICTIONARY_ID='"+dictionary.getStr("JE_CORE_DICTIONARY_ID")+"' and SY_FLAG='1'";
//			DynaBean table=BeanUtils.getInstance().getResourceTable(tableName);
//			List<DynaBean> columns=(List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
//			JSONTreeNode template = BeanUtils.getInstance().buildJSONTreeNodeTemplate(columns);
//			List<JSONTreeNode> jsonTreeNodeList = pcServiceTemplate.getJsonTreeNodeList(rootId, tableName, template, new QueryInfo(querySql, ddOrderSql));

		}
	}

	/**
	 * 缓存所有字典信息
	 */
	@Override
	public void doProAllDicInfo() {
		// TODO Auto-generated method stub
		DicInfoCacheManager.clearAllCache();
		List<DynaBean> dics=pcDynaServiceTemplate.selectList("JE_CORE_DICTIONARY","","JE_CORE_DICTIONARY_ID,"+BeanUtils.getInstance().getProQueryFields("JE_CORE_DICTIONARY"));
		for(DynaBean dic:dics){
			DicInfoCacheManager.putCache(dic.getStr("DICTIONARY_DDCODE"), dic);
		}
	}

	/**
	 * 根据字典项Code获取字典Name值
	 * @param dicItems  TODO 暂不明确
	 * @param itemCode TODO 暂不明确
	 * @return
	 */
	@Override
	public String getItemNameByCode(List<DictionaryItemVo> dicItems,
									String itemCode) {
		// TODO Auto-generated method stub
		for(DictionaryItemVo item:dicItems){
			if(itemCode.equals(item.getCode())){
				return item.getText();
			}
		}
		return "";
	}

	/**
	 * 更新省市县的分类信息
	 */
	@Override
	public void syncSsxDic() {
		DynaBean dictionary=pcDynaServiceTemplate.selectOne("JE_CORE_DICTIONARY", " AND DICTIONARY_DDCODE='JE_COMM_SSQX'");
		DynaBean rootDic=pcDynaServiceTemplate.selectOne("JE_CORE_DICTIONARYITEM"," AND DICTIONARYITEM_DICTIONARY_ID='"+dictionary.getStr("JE_CORE_DICTIONARY_ID")+"' AND SY_NODETYPE='ROOT'");
		syncSsxDicItem(rootDic);
	}
	private void syncSsxDicItem(DynaBean rootDic){
		List<DynaBean> dicItems=pcDynaServiceTemplate.selectList("JE_CORE_DICTIONARYITEM"," AND SY_PARENT='"+rootDic.getStr("JE_CORE_DICTIONARYITEM_ID")+"'");
		for(DynaBean dicItem:dicItems){
			String code=dicItem.getStr("DICTIONARYITEM_ITEMCODE");
			String sStr=code.substring(0,2);
			String shiStr=code.substring(2,4);
			String xStr=code.substring(4);
			if(Integer.parseInt(shiStr)==0 && Integer.parseInt(xStr)==0){
				dicItem.set("DICTIONARYITEM_CLASSIFY","1");
			}else if(Integer.parseInt(xStr)==0){
				dicItem.set("DICTIONARYITEM_CLASSIFY","2");
			}else{
				dicItem.set("DICTIONARYITEM_CLASSIFY","3");
			}
			dicItem=pcDynaServiceTemplate.update(dicItem);
			syncSsxDicItem(dicItem);
		}
	}

	/**
	 * 得到列表字典信息
	 * @param ddCode TODO 暂不明确
	 * @param queryInfo  TODO 暂不明确
	 * @param en  TODO 暂不明确
	 * @return
	 */
	@Override
	public List<DictionaryItemVo> getDicList(String ddCode, QueryInfo queryInfo,Boolean en) {
		// TODO Auto-generated method stub
		DynaBean dictionary=pcDynaServiceTemplate.selectOne("JE_CORE_DICTIONARY", " AND DICTIONARY_DDCODE='"+ddCode+"' AND DICTIONARY_DDTYPE='"+DDType.LIST+"'");
		List<DictionaryItemVo> voList=new ArrayList<DictionaryItemVo>();
		buildChildrenList(voList, dictionary,en, queryInfo, "","0");
		return voList;
	}
	/**
	 * 获取字典所有项  支持所有字典
	 * @param ddCode  TODO 暂不明确
	 * @param params  TODO 暂不明确
	 * @param queryInfo  TODO 暂不明确
	 * @param en  TODO 暂不明确
	 * @return
	 */
	@Override
	public List<JSONTreeNode> getAllTyepDdListItems(String ddCode,Map params, QueryInfo queryInfo, Boolean en) {
		// TODO Auto-generated method stub
		DynaBean dictionary=DicInfoCacheManager.getCacheValue(ddCode);
		if(dictionary==null){
			dictionary=pcDynaServiceTemplate.selectOne("JE_CORE_DICTIONARY", " and DICTIONARY_DDCODE='"+ddCode+"'");
		}
		String querySql=StringUtil.getDefaultValue(queryInfo.getWhereSql(),"");
		String ddWhereSql=dictionary.getStr("DICTIONARY_WHERESQL","");
		//声明变量集合，用于解析whereSql的通配符
		Set<Entry> ddSet=new HashSet<Entry>();
		//加入登录信息
		ddSet.addAll(SecurityUserHolder.getCurrentInfo().entrySet());
		//加入用户变量
		ddSet.addAll(FrontCacheManager.getCacheValues().entrySet());
		ddSet.addAll(BackCacheManager.getCacheValues().entrySet());
		//加入系统设置
		ddSet.addAll(WebUtils.getAllSysVar().entrySet());
		if(params!=null){
			ddSet.addAll(params.entrySet());
		}
		if(StringUtil.isNotEmpty(querySql)){
			querySql=StringUtil.parseKeyWord(querySql, ddSet);
		}
		String ddType=dictionary.getStr("DICTIONARY_DDTYPE");
		String tableName=dictionary.getStr("DICTIONARY_CLASSNAME");
		String rootId=ConstantVars.TREE_ROOT;
		List<JSONTreeNode> lists=new ArrayList<JSONTreeNode>();
		if(DDType.DYNA_TREE.equalsIgnoreCase(ddType) || DDType.LIST.equalsIgnoreCase(ddType) ||  DDType.TREE.equalsIgnoreCase(ddType)){
			if(!DDType.DYNA_TREE.equalsIgnoreCase(ddType)){
				tableName="JE_CORE_DICTIONARYITEM";
				if(ConstantVars.TREE_ROOT.equals(rootId)){
					rootId=dictionary.getStr("DICTIONARY_ITEMROOT_ID");
				}
				querySql=querySql+" and DICTIONARYITEM_DICTIONARY_ID='"+dictionary.getStr("JE_CORE_DICTIONARY_ID")+"' ";
			}
			DynaBean table=BeanUtils.getInstance().getResourceTable(tableName);
			List<DynaBean> columns=(List<DynaBean>) table.get(BeanUtils.KEY_TABLE_COLUMNS);
			JSONTreeNode template = BeanUtils.getInstance().buildJSONTreeNodeTemplate(columns);
			if("JE_CORE_DICTIONARYITEM".equals(tableName) && en){
				template.setText("DICTIONARYITEM_ITEMNAME_EN");
				template.setEnField("DICTIONARYITEM_ITEMNAME");
			}
			if(StringUtil.isNotEmpty(ddWhereSql)){
				querySql+=ddWhereSql;
			}
			if(StringUtil.isNotEmpty(querySql)){
				querySql=StringUtil.parseKeyWord(querySql, ddSet);
			}
			queryInfo.setWhereSql(querySql);
			lists = pcServiceTemplate.getJsonTreeNodeList(rootId, tableName, template, queryInfo);
			List<DynaBean> items=pcDynaServiceTemplate.selectList("JE_CORE_DICTIONARYITEM", " AND DICTIONARYITEM_DICTIONARY_ID='"+dictionary.getStr("JE_CORE_DICTIONARY_ID")+"' "+querySql);
			for(DynaBean item:items){
				JSONTreeNode itemVo=new JSONTreeNode();
				itemVo.setId(item.getStr("JE_CORE_DICTIONARYITEM_ID"));
				itemVo.setText(item.getStr("DICTIONARYITEM_ITEMNAME"));
				if(en){
					itemVo.setText(item.getStr("DICTIONARYITEM_ITEMNAME_EN"));
				}
				itemVo.setCode(item.getStr("DICTIONARYITEM_ITEMCODE"));
				itemVo.setNodeType(item.getStr("SY_NODETYPE"));
				itemVo.setNodeInfo(item.getStr("DICTIONARYITEM_NODEINFO"));
				itemVo.setNodeInfoType(item.getStr("DICTIONARYITEM_NODEINFOTYPE"));
				lists.add(itemVo);
			}
		}else if(DDType.CUSTOM.equalsIgnoreCase(ddType)){
			String beanName=dictionary.getStr("DICTIONARY_CLASS");
			String beanMethod=dictionary.getStr("DICTIONARY_METHOD");
			DicInfoVo dicInfoVo=new DicInfoVo();
			dicInfoVo.setParams(params);
			dicInfoVo.setRootId(rootId);
			dicInfoVo.setDdCode(ddCode);
			if(StringUtil.isNotEmpty(querySql)){
				querySql=StringUtil.parseKeyWord(querySql, ddSet);
			}
			dicInfoVo.setWhereSql(querySql);
			Object bean = SpringContextHolder.getBean(beanName);
			JSONTreeNode rootNode=(JSONTreeNode) ReflectionUtils.getInstance().invokeMethod(bean, beanMethod, new Object[]{dicInfoVo});
			caschTreeLists(lists, rootNode);
		}else if(DDType.SQL_TREE.equalsIgnoreCase(ddType)){
			String sql=dictionary.getStr("DICTIONARY_SQL");
			if(StringUtil.isNotEmpty(ddWhereSql)){
				querySql+=ddWhereSql;
			}
			String sqlConfig=dictionary.getStr("DICTIONARY_SQLCONFIG");
			if(StringUtil.isNotEmpty(sql) && StringUtil.isNotEmpty(sqlConfig)){
				sql=StringUtil.parseKeyWord(sql, ddSet);
				if(StringUtil.isNotEmpty(querySql)){
					querySql=StringUtil.parseKeyWord(querySql, ddSet);
					sql+=querySql;
				}
				List<Map> values=pcServiceTemplate.queryMapBySql(sql);
				List<Map> sqlConfigMap=JsonBuilder.getInstance().fromJsonArray(sqlConfig);
				for(Map map:values){
					JSONTreeNode node=new JSONTreeNode();
					for(Map field:sqlConfigMap){
						String fieldType=field.get("code")+"";
						String fieldName=field.get("value")+"";
						if(StringUtil.isNotEmpty(fieldName)){
							String value=map.get(fieldName)+"";
							if(TreeNodeType.ID.toString().equals(fieldType)){
								node.setId(value);
							}else if(TreeNodeType.TEXT.toString().equals(fieldType)){
								node.setText(value);
							}else if(TreeNodeType.CODE.toString().equals(fieldType)){
								node.setCode(value);
							}else if(TreeNodeType.PARENT.toString().equals(fieldType)){
								node.setParent(value);
							}else if(TreeNodeType.NODETYPE.toString().equals(fieldType)){
								node.setNodeType(value);
								node.setLeaf(NodeType.LEAF.equals(value));
							}else if(TreeNodeType.NODEINFO.toString().equals(fieldType)){
								node.setNodeInfo(value);
							}else if(TreeNodeType.NODEINFOTYPE.toString().equals(fieldType)){
								node.setNodeInfoType(value);
							}else if(TreeNodeType.NODEPATH.toString().equals(fieldType)){
								node.setNodePath(value);
							}else if(TreeNodeType.PARENTPATH.toString().equals(fieldType)){
								node.setParentPath(value);
							}else if(TreeNodeType.DISABLED.toString().equals(fieldType)){
								node.setDisabled(value);
							}else if(TreeNodeType.ORDERINDEX.toString().equals(fieldType)){
								if(StringUtil.isNotEmpty(value)){
									node.setOrderIndex(value);
								}else{
									node.setOrderIndex("0");
								}
							}else if(TreeNodeType.ICON.toString().equals(fieldType)){
								node.setIcon(value);
							}else if(TreeNodeType.ICONCLS.toString().equals(fieldType)){
								node.setIconCls(value);
							}else if(TreeNodeType.HREF.toString().equals(fieldType)){
								node.setHref(value);
							}else if(TreeNodeType.HREFTARGET.toString().equals(fieldType)){
								node.setHrefTarget(value);
							}else if(TreeNodeType.DESCRIPTION.toString().equals(fieldType)){
								node.setDescription(value);
							}
						}
					}
					lists.add(node);
				}
			}
		}else if(DDType.SQL.equalsIgnoreCase(ddType)){
			String sql=dictionary.getStr("DICTIONARY_SQL");
			if(StringUtil.isNotEmpty(ddWhereSql)){
				querySql+=ddWhereSql;
			}
			String sqlConfig=dictionary.getStr("DICTIONARY_SQLPZXXLB");
			if(StringUtil.isNotEmpty(sql) && StringUtil.isNotEmpty(sqlConfig)){
				sql=StringUtil.parseKeyWord(sql, ddSet);
				if(StringUtil.isNotEmpty(querySql)){
					querySql=StringUtil.parseKeyWord(querySql, ddSet);
					sql+=querySql;
				}
				List<Map> values=pcServiceTemplate.queryMapBySql(sql);
				List<Map> sqlConfigMap=JsonBuilder.getInstance().fromJsonArray(sqlConfig);
				Boolean isMore=false;
				//字段标准，按照顺序的标准规则，， 比如  名称：张,李
				String fieldStandard="";
				Map<String,String> fieldSplitName=new HashMap<String,String>();
				for(Map field:sqlConfigMap){
					String fieldType=field.get("code")+"";
					String fieldName=field.get("value")+"";
					if(StringUtil.isEmpty(fieldName)){
						continue;
					}
					if(ArrayUtils.contains(new String[]{DDSQLListType.ID_S,DDSQLListType.NAME_S,DDSQLListType.CODE_S,DDSQLListType.ICONCLS_S}, fieldType)){
						isMore=true;
						fieldStandard=fieldType;
						fieldSplitName.put(fieldType, fieldName);
					}
				}

				for(Map map:values){
					JSONTreeNode ddValue=new JSONTreeNode();
					List<JSONTreeNode> ddValues=new ArrayList<JSONTreeNode>();
					Integer valueLength=-1;
					//构建字典值   多数据则值获取拆分后值的长度
					for(Map field:sqlConfigMap){
						String fieldType=field.get("code")+"";
						String fieldName=field.get("value")+"";
						String value=map.get(fieldName)+"";
						if(isMore){//将值拼接操作
							if(DDSQLListType.ID_S.equals(fieldStandard) && DDSQLListType.ID.equals(fieldType)){
								valueLength=value.split(fieldSplitName.get(fieldStandard)).length;
							}else if(DDSQLListType.NAME_S.equals(fieldStandard) && DDSQLListType.NAME.equals(fieldType)){
								valueLength=value.split(fieldSplitName.get(fieldStandard)).length;
							}else if(DDSQLListType.CODE_S.equals(fieldStandard) && DDSQLListType.CODE.equals(fieldType)){
								valueLength=value.split(fieldSplitName.get(fieldStandard)).length;
							}else if(DDSQLListType.ICONCLS_S.equals(fieldStandard) && DDSQLListType.ICONCLS.equals(fieldType)){
								valueLength=value.split(fieldSplitName.get(fieldStandard)).length;
							}
							if(valueLength>0){
								break;
							}
						}else{
							if(DDSQLListType.ID.equals(fieldType)){
								ddValue.setId(value);
							}else if(DDSQLListType.NAME.equals(fieldType)){
								ddValue.setText(value);
							}else if(DDSQLListType.CODE.equals(fieldType)){
								ddValue.setCode(value);
							}else if(DDSQLListType.ICONCLS.equals(fieldType)){
								ddValue.setIconCls(value);
							}
						}
					}
					if(isMore && valueLength>0){
						for(Integer i=0;i<valueLength;i++){
							JSONTreeNode splitValue=new JSONTreeNode();
							for(Map field:sqlConfigMap){
								String fieldType=field.get("code")+"";
								String fieldName=field.get("value")+"";
								String value=map.get(fieldName)+"";
								String[] valueArray=new String[]{};
								if(StringUtil.isNotEmpty(fieldSplitName.get(fieldType+"_S"))){
									valueArray=value.split(fieldSplitName.get(fieldType+"_S"));
									if(valueArray.length!=valueLength){
										throw new PlatformException("发现字典中字段【"+fieldType+"】值拆分长度不一致,请检查!", PlatformExceptionEnum.JE_CORE_DIC_CHECKITEM_ERROR,new Object[]{ ddCode,params,queryInfo,en});
									}
									//没有定义分割
								}
								if(DDSQLListType.ID.equals(fieldType)){
									if(StringUtil.isNotEmpty(fieldSplitName.get(DDSQLListType.ID_S))){
										splitValue.setId(valueArray[i]);
									}else{
										splitValue.setId(value);
									}
								}else if(DDSQLListType.NAME.equals(fieldType)){
									if(StringUtil.isNotEmpty(fieldSplitName.get(DDSQLListType.NAME_S))){
										splitValue.setText(valueArray[i]);
									}else{
										splitValue.setText(value);
									}
								}else if(DDSQLListType.CODE.equals(fieldType)){
									if(StringUtil.isNotEmpty(fieldSplitName.get(DDSQLListType.CODE_S))){
										splitValue.setCode(valueArray[i]);
									}else{
										splitValue.setCode(value);
									}
								}else if(DDSQLListType.ICONCLS.equals(fieldType)){
									if(StringUtil.isNotEmpty(fieldSplitName.get(DDSQLListType.ICONCLS_S))){
										splitValue.setIconCls(valueArray[i]);
									}else{
										splitValue.setIconCls(value);
									}
								}
								splitValue.setLeaf(true);
								splitValue.setBean(map);
							}
							ddValues.add(splitValue);
						}
						lists.addAll(ddValues);
					}else{
						ddValue.setParent("ROOT");
						ddValue.setBean(map);
						ddValue.setLeaf(true);
						lists.add(ddValue);
					}
				}
			}
		}
		return lists;
	}
	private void caschTreeLists(List<JSONTreeNode> lists,JSONTreeNode parentNode){
		if(parentNode.getChildren().size()>0){
			for(JSONTreeNode node:parentNode.getChildren()){
				lists.add(node);
				caschTreeLists(lists, node);
			}
		}
	}
}

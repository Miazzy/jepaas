package com.je.core.service;

import java.util.*;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.je.cache.service.config.BackCacheManager;
import com.je.cache.service.config.FrontCacheManager;
import com.je.core.ann.entity.DbFieldVo;
import com.je.core.base.MethodArgument;
import com.je.core.constants.db.DbFieldType;
import com.je.core.util.*;
import com.je.datasource.runner.SqlRunner;
import com.je.develop.vo.FuncInfo;
import com.je.develop.vo.FuncRelation;
import com.je.develop.vo.FuncRelationField;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.je.core.constants.tree.NodeType;
import com.je.core.constants.wf.AudFlagStatus;
import com.je.core.facade.extjs.JsonAssist;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.develop.service.FunInfoManager;
import com.je.document.service.DocumentManager;
import com.je.rbac.model.Department;
import com.je.rbac.model.EndUser;

/**
 * 平台默认CRUD的方法实现类
 */
@Component("dynaBeanTemplate")
public class PCDynaBeanTemplateImpl implements PCDynaBeanTemplate {
	@Autowired
	private PCDynaServiceTemplate serviceTemplate;
	@Autowired
	private PCServiceTemplate pcServiceTemplate;
	@Autowired
	private FunInfoManager funInfoManager;
	@Autowired
	private DocumentManager documentManager;
	@Autowired
	private PCDataService pcDataService;
	private static JsonAssist jsonAssist = null;
	private static BeanUtils beanUtils=null;
	static{
		jsonAssist = JsonAssist.getInstance();
		beanUtils=BeanUtils.getInstance();
	}

	/**
	 * 保存
	 * @param dynaBean
	 * @return
	 */
	@Override
	public DynaBean doSave(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		if(StringUtil.isNotEmpty(dynaBean.getStr("SY_PATH"))){
			String uuid =dynaBean.getStr(dynaBean.getStr(BeanUtils.KEY_PK_CODE),"");
			if(StringUtil.isEmpty(uuid)){
				uuid=JEUUID.uuid()+"";
			}
			dynaBean.setStr(dynaBean.getStr(BeanUtils.KEY_PK_CODE), uuid);
			dynaBean.set("SY_PARENTPATH", dynaBean.getStr("SY_PATH"));
			dynaBean.set("SY_PATH", dynaBean.getStr("SY_PATH")+"/"+uuid);
			if(StringUtil.isEmpty(dynaBean.getStr("SY_NODETYPE"))){
				dynaBean.set("SY_NODETYPE", NodeType.LEAF);
			}
			DynaBean parent=serviceTemplate.selectOneByPk(dynaBean.getStr(BeanUtils.KEY_TABLE_CODE), dynaBean.getStr("SY_PARENT"),dynaBean.getStr(BeanUtils.KEY_PK_CODE)+",SY_NODETYPE,SY_TREEORDERINDEX");
			if(NodeType.LEAF.equals(parent.getStr("SY_NODETYPE"))){
				serviceTemplate.saveTreeParentInfo(tableCode, dynaBean.getStr(BeanUtils.KEY_PK_CODE),  dynaBean.getStr("SY_PARENT"));
			}
			//构建序号
			if(dynaBean.containsKey("SY_TREEORDERINDEX") && StringUtil.isNotEmpty(dynaBean.getStr("SY_PARENT"))){
				dynaBean.set("SY_TREEORDERINDEX", parent.getStr("SY_TREEORDERINDEX"));
				generateTreeOrderIndex(dynaBean);
			}
		}
		if(StringUtil.isEmpty(dynaBean.getStr("SY_STATUS"))){
			dynaBean.set("SY_STATUS", "1");//默认为启用数据
		}
		DynaBean inserted = serviceTemplate.insert(dynaBean);
		return inserted;
	}

	/**
	 * 复制数据
	 * @param dynaBean
	 * @param funcCode 功能code
	 * @param codeGenFieldInfo TODO 暂不明确
	 * @param uploadableFields TODO 暂不明确
	 * @return
	 */
	@Override
	public DynaBean doCopy(DynaBean dynaBean,String funcCode,String codeGenFieldInfo,String uploadableFields){
		FuncInfo funcInfo=funInfoManager.getFuncInfo(funcCode);
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String pkCode=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
		String pkValue=dynaBean.getStr(pkCode);
		DynaBean oldBean=serviceTemplate.selectOneByPk(tableCode, pkValue);
		oldBean.set(BeanUtils.KEY_TABLE_CODE, tableCode);
		String newPkValue=JEUUID.uuid();
		if(StringUtil.isNotEmpty(uploadableFields)){
			for(String fieldCode:uploadableFields.split(",")){
				String docValue=documentManager.doCopyDocument(oldBean, fieldCode, newPkValue);
				oldBean.set(fieldCode, docValue);
			}
		}
		oldBean.set(pkCode, newPkValue);
		/**
		 * 如果是树形功能
		 */
		if("tree".equalsIgnoreCase(funcInfo.getFuncType())){
			oldBean.set("SY_PATH", oldBean.getStr("SY_PATH","").replace(pkValue, newPkValue));
		}
		serviceTemplate.buildModelCreateInfo(oldBean);
		//处理编号
		if(StringUtil.isNotEmpty(codeGenFieldInfo)){
			buildCode(codeGenFieldInfo, oldBean);
		}
		DynaBean inserted=serviceTemplate.insert(oldBean);
		inserted.set(BeanUtils.KEY_TABLE_CODE, tableCode);
		copyChild(funcInfo, inserted,pkValue);
		return inserted;
	}
	/**
	 * 递归复制子功能
	 * @param funcInfo
	 * @param dynaBean
	 * @param oldPkValue
	 */
	private void copyChild(FuncInfo funcInfo, DynaBean dynaBean, String oldPkValue){

		String pkName=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
//		Object childs=funInfo.get("children");
//		if(childs!=null){
//			JSONArray childrens=(JSONArray)childs;
			List<FuncRelation> childrens=funcInfo.getChildren();
			for(FuncRelation children:childrens){
//				JSONObject jsonObj=(JSONObject) obj;
				//自定义功能和不允许复制则不复制
				if(children.getCopyChild() || !ArrayUtils.contains(new String[]{"func","tree","childfuncfield"}, children.getType())){
					continue;
				}
				List<FuncRelationField> relatedFields=children.getChildFields();
				String childTableCode=children.getTableCode();
				String foreignKey=beanUtils.getForeignKeyField(childTableCode, dynaBean.getStr(BeanUtils.KEY_TABLE_CODE), pkName,relatedFields);
				//如果未找到外键不复制
				if(StringUtil.isEmpty(foreignKey)){
					continue;
				}
				String funcCode=children.getCode();
//				jsonObj.getString("FUNCRELATION_CODE");
				FuncInfo childFunInfo=funInfoManager.getFuncInfo(funcCode);
				if(childFunInfo!=null) {
					List<FuncRelation> childChildrens = childFunInfo.getChildren();
//				Object childChildrenObj=childFunInfo.get("children");
//				JSONObject childInfoObj=childFunInfo.getJSONObject("info");
//				JSONArray childChildrens=null;
//				if(childChildrenObj!=null){
//					childChildrens=(JSONArray)childChildrenObj;
//				}
					DynaBean childTable = beanUtils.getResourceTable(childTableCode);
					String childPkCode = beanUtils.getPKeyFieldNames(childTable);
					//如果子功能还含有子功能，则一条一条复制数据
					if ((childChildrens != null && childChildrens.size() > 0) || "tree".equals(children.getType())) {
						//树形表的复制
						List<DynaBean> childDatas = serviceTemplate.selectList(childTableCode, " AND " + foreignKey + "=" + "'" + oldPkValue + "' " + childFunInfo.getWhereSql());
						Map<String, String> pkMap = new HashMap<String, String>();
						for (DynaBean childData : childDatas) {
							String oldChildPkValue = childData.getStr(childPkCode);
							childData.set(BeanUtils.KEY_TABLE_CODE, childTableCode);
							childData.set(childPkCode, null);//主键设空
							childData.set(foreignKey, dynaBean.getStr(pkName));//设置新的外键
							serviceTemplate.buildModelCreateInfo(childData);
							DynaBean inserted = serviceTemplate.insert(childData);
							pkMap.put(oldChildPkValue, inserted.getStr(childPkCode));
							inserted.set(BeanUtils.KEY_TABLE_CODE, childTableCode);
							if (childChildrens != null && childChildrens.size() > 0) {
								copyChild(childFunInfo, inserted, oldChildPkValue);
							}
						}
						//如果是树形功能则替换父节点及路径 父节点路径
						if ("tree".equals(children.getType())) {
							for (Entry<String, String> entry : pkMap.entrySet()) {
								pcServiceTemplate.executeSql("UPDATE " + childTableCode + " SET SY_PARENT=REPLACE(SY_PARENT,'" + entry.getKey() + "','" + entry.getValue() + "'),SY_PARENTPATH=REPLACE(SY_PARENTPATH,'" + entry.getKey() + "','" + entry.getValue() + "'),SY_PATH=REPLACE(SY_PATH,'" + entry.getKey() + "','" + entry.getValue() + "') WHERE " + foreignKey + "=" + "'" + dynaBean.getStr(pkName) + "'");
							}
						}
					} else {
						EndUser currentUser = SecurityUserHolder.getCurrentUser();
						Department currentDept = SecurityUserHolder.getCurrentUserDept();
						String createTime = DateUtils.formatDateTime(new Date());
						String queryColumns = beanUtils.getFieldNames(childTable, new String[]{childPkCode, foreignKey, "SY_CREATETIME", "SY_CREATEORG", "SY_CREATEORGNAME", "SY_CREATEUSER", "SY_CREATEUSERNAME", "SY_AUDFLAG", "SY_PDID", "SY_PIID"});
						StringBuffer insertSql = new StringBuffer();
						String uuidStr = DBSqlUtils.getPcDBMethodManager().getGenerateUUID();
						insertSql.append(" INSERT INTO  " + childTableCode + " (" + childPkCode + "," + foreignKey + "," + queryColumns + ",SY_CREATETIME,SY_CREATEORG,SY_CREATEORGNAME,SY_CREATEUSER,SY_CREATEUSERNAME,SY_AUDFLAG,SY_PDID,SY_PIID) ");
						insertSql.append("SELECT " + uuidStr + ",'" + dynaBean.getStr(pkName) + "'," + queryColumns + ",'" + createTime + "','" + currentDept.getDeptCode() + "','" + currentDept.getDeptName() + "','" + currentUser.getUserCode() + "','" + currentUser.getUsername() + "','" + AudFlagStatus.NOSTATUS + "','','' FROM " + childTableCode + " WHERE " + foreignKey + "='" + oldPkValue + "' " + childFunInfo.getWhereSql());
						pcServiceTemplate.executeSql(insertSql.toString());
					}
					//找到信息功能执行递归操作
				}
			}
//		}
	}

	/**
	 * 列表更新
	 * @param dynaBean TODO 暂不明确
	 * @param updateStr TODO 暂不明确
	 * @return
	 */
	@Override
	public List<DynaBean> doUpdateList(DynaBean dynaBean, String updateStr) {
		// TODO Auto-generated method stub
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		List<DynaBean> updateList=new ArrayList<>();
		List<DynaBean> lists=beanUtils.buildUpdateList(updateStr, tableCode);
		for(DynaBean bean:lists){
			String pkValue=bean.getPkValue();
			if(StringUtil.isNotEmpty(pkValue)){
				serviceTemplate.buildModelModifyInfo(bean);
				bean=serviceTemplate.update(bean);
			}else{
				serviceTemplate.buildModelCreateInfo(bean);
				bean=serviceTemplate.insert(bean);
			}
			updateList.add(bean);
		}
		return updateList;
	}

	/**
	 * 列表更新
	 * @param dynaBean  TODO 暂不明确
	 * @param updateStr  TODO 暂不明确
	 * @param funcType 功能类型
	 * @param viewConfigInfo   TODO 暂不明确
	 * @return
	 */
	@Override
	public List<DynaBean> doUpdateList(DynaBean dynaBean, String updateStr,String funcType,String viewConfigInfo) {
		// TODO Auto-generated method stub
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		List<DynaBean> updateList=new ArrayList<>();
		List<DynaBean> lists=beanUtils.buildUpdateList(updateStr, tableCode);
		for(DynaBean bean:lists){
			String pkValue=bean.getPkValue();
			if("view".equals(funcType) && StringUtil.isNotEmpty(viewConfigInfo)){
				doViewData(viewConfigInfo, bean);
			}
			if(StringUtil.isNotEmpty(pkValue)){
				serviceTemplate.buildModelModifyInfo(bean);
				bean=serviceTemplate.update(bean);
			}else{
				serviceTemplate.buildModelCreateInfo(bean);
				bean=serviceTemplate.insert(bean);
			}
			updateList.add(bean);
		}
		return updateList;
	}

	/**
	 * 树形节点拖动
	 * @param dynaBean  TODO 暂不明确
	 * @return
	 */
	@Override
	public DynaBean treeMove(DynaBean dynaBean) {
		// TODO Auto-generated method stub
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String pkName=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
		DynaBean bean=serviceTemplate.selectOneByPk(tableCode, dynaBean.getStr(pkName),pkName+",SY_TREEORDERINDEX,SY_ORDERINDEX,SY_PATH,SY_PARENTPATH,SY_PARENTPATH,SY_LAYER,SY_PARENT");
		String newParentId=dynaBean.getStr("SY_PARENT");
		String oldParentId=bean.getStr("SY_PARENT");
		String oldPath=bean.getStr("SY_PATH");
		String oldParentPath=bean.getStr("SY_PARENTPATH");
		String oldTreeOrderIndex=bean.getStr("SY_TREEORDERINDEX");
		Integer chaLayer=dynaBean.getInt("SY_LAYER",0)-bean.getInt("SY_LAYER",0);
		bean.set("SY_PARENT", newParentId);
		bean.set(BeanUtils.KEY_TABLE_CODE,tableCode);
		DynaBean parent=serviceTemplate.selectOneByPk(tableCode, newParentId);
		bean.set("SY_PATH", parent.getStr("SY_PATH")+"/"+dynaBean.getStr(pkName));
		bean.set("SY_PARENTPATH", parent.getStr("SY_PATH"));
		bean.set("SY_PARENT", newParentId);
		bean.set("SY_LAYER", parent.getInt("SY_LAYER",0)+1);
		if(NodeType.LEAF.equals(parent.getStr("SY_NODETYPE"))){
			serviceTemplate.updateTreePanent4NodeType(tableCode, newParentId);
		}
		bean.set("SY_TREEORDERINDEX", parent.getStr("SY_TREEORDERINDEX"));
		generateTreeOrderIndex(bean);
		String subStringFunction=DBSqlUtils.getPcDBMethodManager().getSubString();
		String lengthFunction=DBSqlUtils.getPcDBMethodManager().getLength();
		//更新当前节点下所有孩子的路径信息
		pcServiceTemplate.executeSql("UPDATE "+tableCode+" SET SY_PATH=REPLACE(SY_PATH,'"+oldPath+"','"+bean.getStr("SY_PATH")+"'),SY_PARENTPATH=REPLACE(SY_PARENTPATH,'"+oldParentPath+"','"+bean.getStr("SY_PARENTPATH")+"'),SY_LAYER=(SY_LAYER+"+chaLayer+"),SY_TREEORDERINDEX=('"+bean.getStr("SY_TREEORDERINDEX")+"'+"+subStringFunction+"(SY_TREEORDERINDEX,"+(oldTreeOrderIndex.length()+1)+","+lengthFunction+"(SY_TREEORDERINDEX)-"+oldTreeOrderIndex.length()+")) WHERE SY_PATH LIKE '%"+oldPath+"%' AND "+pkName+"!='"+ dynaBean.getStr(pkName)+"' AND "+lengthFunction+"(SY_TREEORDERINDEX)>"+oldTreeOrderIndex.length());
		DynaBean updated=serviceTemplate.update(bean);
		serviceTemplate.updateTreePanent4NodeType(tableCode, oldParentId);
		return updated;
	}

	/**
	 * 级联删除子单
	 * @param dynaBean
	 * @param funcInfo 功能信息
	 * @param whereSql
	 */
	@Override
	public void removeChild(DynaBean dynaBean, FuncInfo funcInfo,String whereSql) {
		// TODO Auto-generated method stub
		String pkName=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
//		Object childs=funInfo.get("children");
//		if(childs!=null){
//			JSONArray childrens=(JSONArray)childs;
		List<FuncRelation> childrens=funcInfo.getChildren();
		for(FuncRelation children:childrens){
//			JSONObject jsonObj=(JSONObject) obj;
			if(ArrayUtils.contains(new String[]{"idit","chart","report"}, children.getType())){
				continue;
			}
//			JSONArray relatedFields=jsonObj.getJSONArray("relatedFields");
			List<FuncRelationField> relatedFields=children.getChildFields();
			String foreignKey="";
			//找到外键关联并构建删除条件
			for(FuncRelationField childField:relatedFields){
//				JSONObject jsObj=(JSONObject) childObj;
				//找到对应主功能的主键的子功能的字段
				if(childField.getFieldCode().equals(pkName) && childField.getDeleteChild()){
					foreignKey=childField.getChildFieldCode();
				}
			}
			//未找到外键
			if(StringUtil.isEmpty(foreignKey)){
				continue;
			}
			//递归删除子功能    递归传入查询的sql  如  一层传ids   二层传子查询
			String childTableName=children.getTableCode();
//			jsonObj.getString("FUNCRELATION_TABLENAME");
			DynaBean childBean=new DynaBean(childTableName,true);
			String funcCode=children.getCode();
			FuncInfo childFunInfo=funInfoManager.getFuncInfo(funcCode);
			if(childFunInfo!=null) {
				List<FuncRelation> childChildrens = childFunInfo.getChildren();
//				Object childChildrenObj=childFunInfo.get("children");
////				if(childChildrenObj!=null){
////					JSONArray childChildrens=(JSONArray)childChildrenObj;
				if (childChildrens.size() > 0) {
					removeChild(childBean, childFunInfo, "(SELECT " + childBean.getStr(BeanUtils.KEY_PK_CODE) + " FROM " + childTableName + " WHERE 1=1 AND " + foreignKey + " IN " + whereSql + ")");
				}
//				}
				pcServiceTemplate.executeSql("DELETE FROM " + childTableName + " WHERE 1=1 AND " + foreignKey + " IN " + whereSql);
			}
		}
//		}
	}

	/**
	 * 级联删除子单  查询出来子去删除
	 * @param dynaBean TODO 暂不明确
	 * @param funcInfo 功能信息
	 */
	@Override
	public void removeChild(DynaBean dynaBean, FuncInfo funcInfo){
		String pkName=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
//		Object childs=funInfo.get("children");
//		if(childs!=null){
//			JSONArray childrens=(JSONArray)childs;
		List<FuncRelation> childrens=funcInfo.getChildren();
		for(FuncRelation children:childrens){
//			JSONObject jsonObj=(JSONObject) obj;
			if(!ArrayUtils.contains(new String[]{"func","tree","childfuncfield"}, children.getType())){
				continue;
			}
			String childTableCode=children.getTableCode();
//			JSONArray relatedFields=jsonObj.getJSONArray("relatedFields");
			List<FuncRelationField> relatedFields=children.getChildFields();
			List<FuncRelationField> delRelatedFields=new ArrayList<>();
//			JSONArray delRelatedFields=new JSONArray();
			//找到外键关联并构建删除条件
			for(FuncRelationField childField:relatedFields){
//				JSONObject jsObj=(JSONObject) childObj;
				//找到对应主功能的主键的子功能的字段
				if(childField.getDeleteChild()){
					if(childField.getFieldCode().equals(pkName) && childField.getChildFieldCode().equals(beanUtils.getForeignKeyField(childTableCode, tableCode, pkName,relatedFields))){
						continue;
					}
					delRelatedFields.add(childField);
				}
			}
			if(delRelatedFields.size()==0){
				continue;
			}
			//构建主子SQL 并查询出子数据
			String childSql=funInfoManager.buildWhereSql4funcRelation(delRelatedFields, dynaBean);
			List<DynaBean> lists=serviceTemplate.selectList(childTableCode, childSql);
			if(lists.size()==0){
				continue;
			}

			String funcCode=children.getCode();
//			jsonObj.getString("FUNCRELATION_CODE");
			FuncInfo childFunInfo=funInfoManager.getFuncInfo(funcCode);
//			JSONObject childInfo=childFunInfo.getJSONObject("info");
			if(childFunInfo!=null) {
				String childPkCode = childFunInfo.getPkCode();
				for (DynaBean childBean : lists) {
					removeChild(childBean, childFunInfo);
				}
			    pcServiceTemplate.executeSql(" DELETE FROM "+childTableCode+" WHERE "+childPkCode+" IN ("+StringUtil.buildArrayToString(ArrayUtils.getBeanFieldArray(lists, childPkCode))+")");
			}
		}
//		}
	}

	/**
	 * 判断是否有子功能需级联删除
	 * @param funcInfo 功能json对象
	 * @return
	 */
	@Override
	public Boolean decideDeleteChildren(FuncInfo funcInfo){
//		JSONObject info=funcInfo.getJSONObject("info");
		String tableCode=funcInfo.getTableCode();
		String pkName=funcInfo.getPkCode();
//		info.getString("FUNCINFO_PKNAME");
//		Object childs=funcInfo.get("children");
//		if(childs!=null){
			List<FuncRelation> childrens=funcInfo.getChildren();
			for(FuncRelation children:childrens){
//				JSONObject jsonObj=(JSONObject) obj;
				//func,tree,idit,chart,report,industry,file,gantt,portal,childfuncfield,history
				if(ArrayUtils.contains(new String[]{"func","tree","childfuncfield"}, children.getType())){
					String childTableCode=children.getTableCode();
//					JSONArray relatedFields=jsonObj.getJSONArray("relatedFields");
					List<FuncRelationField> relatedFields=children.getChildFields();
					List<FuncRelationField> delRelatedFields=new ArrayList<>();
//					JSONArray delRelatedFields=new JSONArray();
					//找到外键关联并构建删除条件
					for(FuncRelationField childField:relatedFields){
//						JSONObject jsObj=(JSONObject) childObj;
						//找到对应主功能的主键的子功能的字段
						if(childField.getDeleteChild()){
							if(childField.getFieldCode().equals(pkName) && childField.getChildFieldCode().equals(beanUtils.getForeignKeyField(childTableCode, tableCode, pkName,relatedFields))){
								continue;
							}
							delRelatedFields.add(childField);
						}
					}
					if(delRelatedFields.size()==0){
						continue;
					}
					return true;
				}
			}
//		}
		return false;
	}

	/**
	 * 验证字段唯一
	 * @param dynaBean
	 * @param fieldCode 验证字段
	 * @return
	 */
	@Override
	public Boolean checkFieldUniquen(DynaBean dynaBean, String fieldCode) {
		// TODO Auto-generated method stub
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String whereSql=dynaBean.getStr(BeanUtils.KEY_WHERE,"");
		Long count=serviceTemplate.selectCount(tableCode," AND "+fieldCode+"='"+dynaBean.getStr(fieldCode)+"'"+whereSql);
		if(StringUtil.isEmpty(tableCode) || StringUtil.isEmpty(fieldCode)){
			return false;
		}
		return count<=0;
	}

	/**
	 * 构建排序条件
	 * @param sort
	 * @param orderSql 排序sql
	 * @param useOrderSql TODO 暂不明确
	 * @return
	 */
	@Override
	public String buildOrderSql(String sort, String orderSql,String useOrderSql) {
		// TODO Auto-generated method stub
		// 排序
		StringBuffer order = new StringBuffer();
		if(StringUtil.isNotEmpty(sort) && !StringUtil.EMPTY_JSON_ARRAY.equalsIgnoreCase(sort)) {
			String orderBy = buildSortOrder(sort,"");
			order.append(StringUtil.BLANK_SPACE);
			order.append(orderBy);
			if(StringUtil.isNotEmpty(orderSql) && "1".equals(useOrderSql)){
				order.append(orderSql.trim().replace("order by", ",").replace("ORDER BY", ","));
			}
		} else if(StringUtil.isNotEmpty(orderSql)) {
			order.append(StringUtil.BLANK_SPACE);
			order.append(orderSql);
		}
		return order.toString();
	}

	/**
	 * 构建ExtJs点击列按指定列排序
	 * @param sort  TODO 暂不明确
	 * @param excludes TODO 暂不明确
	 * @return
	 */
	@Override
	public String buildSortOrder(String sort,String excludes){
		StringBuffer orderBy = new StringBuffer();
		String[] excludesArray=excludes.split(",");
		if(StringUtil.isNotEmpty(sort)) {
			JSONArray orderArray = jsonAssist.stringToJsonArray(sort);
			if(null != orderArray && 0 != orderArray.size()) {
				Iterator<?> itor = orderArray.iterator();
				while(itor.hasNext()) {
					JSONObject obj = (JSONObject)itor.next();
					if(ArrayUtils.contains(excludesArray, obj.getString("property"))){
						continue;
					}
					orderBy.append(StringUtil.BLANK_SPACE + obj.getString("property"));
					orderBy.append(StringUtil.BLANK_SPACE + obj.getString("direction"));
					if(itor.hasNext()) {
						orderBy.append(ArrayUtils.SPLIT);
					}
				}
				if(orderBy.length()>0){
					orderBy.insert(0, " ORDER BY ");
				}
			}
		}
		return orderBy.toString();
	}

	/**
	 * 编号构建
	 * @param codeGenFieldInfo TODO 暂不明确
	 * @param dynaBean TODO 暂不明确
	 */
	@Override
	public void buildCode(String codeGenFieldInfo, DynaBean dynaBean) {
		// TODO Auto-generated method stub
		buildCode(codeGenFieldInfo, dynaBean, "");
	}

	/**
	 * 编号构建
	 * @param codeGenFieldInfo TODO 暂不明确
	 * @param dynaBean TODO 暂不明确
	 * @param zhId TODO 暂不明确
	 */
	@Override
	public void buildCode(String codeGenFieldInfo, DynaBean dynaBean,String zhId) {
		// TODO Auto-generated method stub
		JSONArray array = jsonAssist.stringToJsonArray(codeGenFieldInfo);
		for(int i=0; i<array.size(); i++) {
			JSONObject o = array.getJSONObject(i);
			String fieldName = o.getString("code");
			String funcId = o.getString("funcId");
			String funcCode="";
			if(o.containsKey("funcCode")){
				funcCode=o.getString("funcCode");
			}
			String tableCode="";
			if(o.containsKey("tableCode")){
				tableCode=o.getString("tableCode");
			}
			String tableName="";
			if(StringUtil.isNotEmpty(tableCode)){
				DynaBean table=BeanUtils.getInstance().getResourceTable(tableCode);
				tableName=table.getStr("RESOURCETABLE_TABLENAME");
			}
			String funcName=o.getString("funcName");
			JSONObject infos=new JSONObject();
			infos.put("FUNCID", funcId);
			infos.put("FUNCCODE", funcCode);
			infos.put("FUNCNAME", funcName);
			infos.put("TABLENAME", tableName);
			infos.put("TABLECODE", tableCode);
			if(StringUtil.isNotEmpty(zhId)){
				infos.put("ZHID", zhId);
			}
			JSONArray codePatterns=o.getJSONArray("configInfo");
			if(StringUtil.isNotEmpty(fieldName) && StringUtil.isNotEmpty(funcId) && codePatterns.size()>0) {
				String fieldCodeValue = serviceTemplate.codeGenerator(codePatterns, dynaBean, fieldName,infos);
				dynaBean.setStr(fieldName, fieldCodeValue);
			}
		}
	}

	/**
	 * 子功能默认添加树形ROOT节点操作
	 * @param dynaBean
	 * @param funcCode
	 */
	@Override
	public void doChildrenTree(DynaBean dynaBean, String funcCode) {
		// TODO Auto-generated method stub
		if(StringUtil.isEmpty(funcCode))return;
		FuncInfo funcInfo=funInfoManager.getFuncInfo(funcCode);
//		Object childs=funInfo.get("children");
		String mainPkCode=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
//		if(childs!=null){
//			JSONArray childrens=(JSONArray)childs;
			List<FuncRelation> childrens=funcInfo.getChildren();
			for(int i=0;i<childrens.size();i++){
//				JSONObject children=childrens.getJSONObject(i);
				FuncRelation children=childrens.get(i);
				String funcType=children.getType();
//				children.getString("SY_STATUS");
				if("tree".equalsIgnoreCase(funcType)){
					String tableName=children.getTableCode();
//					children.getString("FUNCRELATION_TABLENAME");
					String childPkCode="";
					//去除外键的名称
//					JSONArray relatedFields=children.getJSONArray("relatedFields");
					List<FuncRelationField> relatedFields=children.getChildFields();
					for(int j=0;j<relatedFields.size();j++){
//						JSONObject fieldObj=relatedFields.getJSONObject(j);
						FuncRelationField childField=relatedFields.get(j);
						if(childField.getFieldCode().equals(mainPkCode)){
							childPkCode=childField.getChildFieldCode();
							break;
						}
					}
					String childUuid=JEUUID.uuid();
					DynaBean bean=new DynaBean(tableName,true);
					//赋值外键
					bean.set(childPkCode, dynaBean.getStr(mainPkCode));
					bean.set("SY_NODETYPE", NodeType.ROOT);
					bean.set("SY_LAYER", 0);
					bean.set("SY_PARENT", null);
					bean.set("SY_PATH", "/"+childUuid);
					bean.set("SY_ORDERINDEX", 1);
					bean.set("SY_TREEORDERINDEX", StringUtil.preFillUp("1", 6, '0'));
					bean.set(bean.getStr(BeanUtils.KEY_PK_CODE), childUuid);
					serviceTemplate.buildModelCreateInfo(bean);
					serviceTemplate.insert(bean);
				}
			}
//		}
	}

	/**
	 * 构建树形的排序序号
	 * @param dynaBean
	 */
	@Override
	public void generateTreeOrderIndex(DynaBean dynaBean){
		String parent=dynaBean.getStr("SY_PARENT");
		String parentNumber=dynaBean.getStr("SY_TREEORDERINDEX","");
		List<Object> objs=(List<Object>) pcServiceTemplate.queryBySql("select MAX(SY_TREEORDERINDEX) FROM "+dynaBean.getStr(BeanUtils.KEY_TABLE_CODE)+" WHERE SY_PARENT='"+parent+"'");
		if(objs!=null && objs.size()>0 && StringUtil.isNotEmpty(objs.get(0)+"")){
			String maxValue=StringUtil.getDefaultValue((objs.get(0)+""));

			Integer value=0;
			if(maxValue.length()>parentNumber.length()){
				value=Integer.parseInt(StringUtil.getDefaultValue((objs.get(0)+"").substring(parentNumber.length()),"0"))+1;
			}else{
				value=1;
			}
			dynaBean.setStr("SY_TREEORDERINDEX",parentNumber+=StringUtil.preFillUp(value.toString(), 6, '0'));
			dynaBean.set("SY_ORDERINDEX", value);
		}else{
			dynaBean.set("SY_ORDERINDEX", 1);
			dynaBean.setStr("SY_TREEORDERINDEX",parentNumber+=StringUtil.preFillUp("1", 6, '0'));
		}
	}

	/**
	 * 保存document信息  主要用于功能上传和只上传附件
	 * @param documentInfo TODO 暂不明确
	 * @return
	 */
	@Override
	public DynaBean doSaveDocumentInfo(DynaBean documentInfo) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotEmpty(documentInfo.getStr("SY_CREATEUSER"))){
			String nowDateStr=DateUtils.formatDateTime(new Date());
			documentInfo.set("SY_CREATETIME", nowDateStr);
			documentInfo.setStr("SY_MODIFYUSERID", documentInfo.getStr("SY_CREATEUSERID"));
			documentInfo.setStr("SY_MODIFYUSER", documentInfo.getStr("SY_CREATEUSER"));
			documentInfo.setStr("SY_MODIFYUSERNAME",documentInfo.getStr("SY_CREATEUSERNAME"));
			documentInfo.setStr("SY_MODIFYORGID", documentInfo.getStr("SY_CREATEORGID"));
			documentInfo.setStr("SY_MODIFYORG", documentInfo.getStr("SY_CREATEORG"));
			documentInfo.setStr("SY_MODIFYORGNAME", documentInfo.getStr("SY_CREATEORGNAME"));
			documentInfo.setStr("SY_MODIFYTIME", nowDateStr);
		}else{
			serviceTemplate.buildModelCreateInfo(documentInfo);
			serviceTemplate.buildModelModifyInfo(documentInfo);
		}
		return documentManager.insertDoc(documentInfo);
	}

	/**
	 * 批量上传附件处理
	 * @param dynaBean TODO 暂不明确
	 * @param batchFilesFields TODO 暂不明确
	 * @param funcCode TODO 暂不明确
	 * @param doSave TODO 暂不明确
	 * @param request
	 */
	@Override
	public void doSaveBatchFiles(DynaBean dynaBean, String batchFilesFields,String funcCode,Boolean doSave,HttpServletRequest request) {
		// TODO Auto-generated method stub
		documentManager.processBatchUpload(dynaBean, batchFilesFields, funcCode,doSave,request);
	}

	/**
	 * 批量上传附件处理（旧方法，保存为了重写action的业务不出错，默认都不是平台）
	 * @param dynaBean TODO 暂不明确
	 * @param batchFilesFields TODO 暂不明确
	 * @param funcCode TODO 暂不明确
	 * @param uploadPath TODO 暂不明确
	 * @param doSave TODO 暂不明确
	 * @param jeFileType TODO 暂不明确
	 */
	@Override
	public void doSaveBatchFiles(DynaBean dynaBean, String batchFilesFields,
								 String funcCode, String uploadPath, Boolean doSave,String jeFileType) {
		documentManager.doSaveBatchFiles(dynaBean, batchFilesFields, funcCode, uploadPath, doSave, "0");
	}

	/**
	 * 删除批量附件处理
	 * @param tableCode 表code
	 * @param ids
	 */
	@Override
	public void doRemoveBatchFiles(String tableCode,String ids) {
		// TODO Auto-generated method stub
		documentManager.doRemoveBatchFiles(tableCode, ids);
	}

	/**
	 * 删除树形批量附件处理
	 * @param tableCode 表code
	 * @param ids
	 */
	@Override
	public void doRemoveTreeBatchFiles(String tableCode,String ids) {
		// TODO Auto-generated method stub
		doRemoveTreeBatchFiles(tableCode, ids);
	}
//	@Override
//	public String getQueryColumns(String funcCode) {
//		// TODO Auto-generated method stub
//		String queryColumns=FuncColumnCacheManager.getCacheValue(funcCode);
//		if(StringUtil.isEmpty(queryColumns)){
//			List<String> columns=new ArrayList<String>();
//			List<DynaBean> columnArrays=serviceTemplate.selectList("JE_CORE_RESOURCECOLUMN", " AND RESOURCECOLUMN_FUNCINFO_ID IN (SELECT JE_CORE_FUNCINFO_ID FROM JE_CORE_FUNCINFO WHERE FUNCINFO_NODEINFOTYPE IN ('FUNC','FUNCFIELD') AND FUNCINFO_FUNCCODE='"+funcCode+"')","RESOURCECOLUMN_LAZYLOAD,RESOURCECOLUMN_CODE,JE_CORE_RESOURCECOLUMN_ID");
//			for(int i =0;i<columnArrays.size();i++){
//				DynaBean column=columnArrays.get(i);
//				if("1".equals(column.getStr("RESOURCECOLUMN_LAZYLOAD"))){
//					columns.add(column.getStr("RESOURCECOLUMN_CODE"));
//				}
//			}
//			queryColumns=StringUtil.buildSplitString(ArrayUtils.getArray(columns), ",");
//			FuncColumnCacheManager.putCache(funcCode, queryColumns);
//		}
//		return queryColumns;
//	}

	/**
	 * 构建mark信息
	 * @param lists 查询结果数据信息
	 * @param tableCode 查询参数  取内部   tableCode  pkCode  whereSql
	 * @param currentUser
	 * @param funcId
	 */
	@Override
	public void buildMarkInfo(List<DynaBean> lists,String tableCode,EndUser currentUser,String funcId) {
		// TODO Auto-generated method stub
//		String tableCode=queryBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String pkCode=beanUtils.getPKeyFieldNames(tableCode);
//		String whereSql=queryBean.getStr(BeanUtils.KEY_WHERE);
		String baseSql=" AND MARK_USERID='"+currentUser.getUserId()+"' AND MARK_FUNCID='"+funcId+"'";
		String markSql="";
		int count=lists.size();
		int step=900;
		int sum=count/step;
		List<String> sqls=new ArrayList<>();
		for(int i=0;i<=sum;i++){
			int index=(i*step);
			int endIndex=index+step;
			if(count<=endIndex){
				endIndex=count;
			}
			String[] pkVals=new String[(endIndex-index)];
			int start=0;
			for(int j=index;j<endIndex;j++){
				if(j>=count)break;
				DynaBean vals=lists.get(j);
				String pkValue=vals.getStr(pkCode,"");
				pkVals[start]=pkValue;
				start++;
			}
			if(pkVals.length>0){
				sqls.add(" MARK_MODELID IN ("+StringUtil.buildArrayToString(pkVals)+")");
			}
		}
		if(sqls.size()>0){
			markSql=" AND ("+StringUtil.buildSplitString(ArrayUtils.getArray(sqls), " OR ")+")";
		}else{
			markSql=" AND 1!=1 ";
		}
		List<DynaBean> marks=serviceTemplate.selectList("JE_CORE_MARK", baseSql+markSql);
		Map<String,String> markInfos=new HashMap<String,String>();
		for(DynaBean mark:marks){
			String pkValue=mark.getStr("MARK_MODELID");
			List<String> vals=new ArrayList<>();
			for(int i=1;i<=7;i++){
				if("1".equals(mark.getStr("MARK_"+i,"0"))){
					vals.add(i+"");
				}
			}
			markInfos.put(pkValue, StringUtil.buildSplitString(ArrayUtils.getArray(vals), ","));
		}
		for(DynaBean obj:lists){
			String pkValue=obj.getStr(pkCode);
			obj.set("SY__MARK", "");
			if(markInfos.containsKey(pkValue)){
				obj.set("SY__MARK", markInfos.get(pkValue));
			}
		}
	}

	/**
	 * 构建批注信息
	 * @param lists TODO 暂不明确
	 * @param tableCode 表code
	 * @param funcId TODO 暂不明确
	 */
	@Override
	public void buildPostilInfo(List<DynaBean> lists, String tableCode,
								String funcId) {
		// TODO Auto-generated method stub
//		JE_CORE_POSTIL
		String pkCode=beanUtils.getPKeyFieldNames(tableCode);
		List<Map> counts=pcServiceTemplate.queryMapBySql("SELECT POSTIL_MODELID,COUNT(*) AS POSTIL_NUM FROM JE_CORE_POSTIL WHERE POSTIL_FUNCID='"+funcId+"' AND POSTIL_MODELID IN ("+StringUtil.buildArrayToString(ArrayUtils.getBeanFieldArray(lists, pkCode))+") GROUP BY POSTIL_MODELID");
		Map<String,Integer> valsInfo=new HashMap<String,Integer>();
		for(Map countInfo:counts){
			String modelId=countInfo.get("POSTIL_MODELID")+"";
			Integer num=Integer.parseInt(StringUtil.getDefaultValue(countInfo.get("POSTIL_NUM"),"0"));
			valsInfo.put(modelId, num);
		}
		for(DynaBean obj:lists){
			String pkValue=obj.getStr(pkCode);
			if(valsInfo.containsKey(pkValue)){
				obj.set("SY__POSTIL", valsInfo.get(pkValue));
			}else{
				obj.set("SY__POSTIL", 0);
			}
		}
	}

	/**
	 *
	 * @param lists 查询结果数据信息
	 * @param tableCode 查询参数  取内部   tableCode  pkCode  whereSql
	 * @param currentUser
	 * @param funcCode    2 未读   1  已读已修改   0 已读未修改
	 */
	@Override
	public void buildFuncEditInfo(List<DynaBean> lists,String tableCode,EndUser currentUser,String funcCode) {
		// TODO Auto-generated method stub
		String pkCode=beanUtils.getPKeyFieldNames(tableCode);
		List<Map> funcEdits=pcServiceTemplate.queryMapBySql("SELECT FUNCEDIT_PKVALUE,FUNCEDIT_USERID,FUNCEDIT_NEW FROM JE_CORE_FUNCEDIT WHERE FUNCEDIT_USERID='"+currentUser.getUserId()+"' AND FUNCEDIT_FUNCCODE='"+funcCode+"' AND FUNCEDIT_PKVALUE IN ("+StringUtil.buildArrayToString(ArrayUtils.getBeanFieldArray(lists, pkCode))+") ");
		Map<String,String> dataVals=new HashMap<>();
		for(Map funcEdit:funcEdits){
			String modelId=funcEdit.get("FUNCEDIT_PKVALUE")+"";
			dataVals.put(modelId,funcEdit.get("FUNCEDIT_NEW")+"");
		}
		for(DynaBean obj:lists){
			String pkValue=obj.getStr(pkCode);
			if(dataVals.containsKey(pkValue)){
				obj.set("SY_FUNCEDIT", dataVals.get(pkValue)+"");
			}else{
				obj.set("SY_FUNCEDIT", "2");
			}
		}
	}

	/**
	 * 对视图级联的处理
	 * @param viewConfigInfo 对视图级联的处理
	 * @param dynaBean TODO 暂不明确
	 */
	@Override
	public void doViewData(String viewConfigInfo, DynaBean dynaBean) {
		// TODO Auto-generated method stub
		JSONArray arrays=JSONArray.fromObject(viewConfigInfo);
		for(int i=0;i<arrays.size();i++){
			JSONObject obj=arrays.getJSONObject(i);
			String tableCode=obj.getString("tableCode");
			String pkCode=obj.getString("pkCode");
			String viewPkCode=obj.getString("viewFieldCode");
			String fieldCodes=obj.getString("fieldCodes");
			String updateFlag=obj.getString("updateData");
			String insertFlag="0";
			if(obj.containsKey("insertData")){
				insertFlag="insertData";
			}
			String doFieldCodes="";
			if(obj.containsKey("doFieldCodes")){
				doFieldCodes=obj.getString("doFieldCodes");
			}
			if(StringUtil.isNotEmpty(tableCode) && StringUtil.isNotEmpty(pkCode) && StringUtil.isNotEmpty(viewPkCode) && StringUtil.isNotEmpty(fieldCodes) && "1".equals(updateFlag)){
				if(!dynaBean.containsKey(viewPkCode))continue;
				String pkValue=dynaBean.getStr(viewPkCode);
				DynaBean bean=new DynaBean(tableCode,true);
				for(String fieldCode:fieldCodes.split(",")){
					if(dynaBean.containsKey(fieldCode)){
						bean.set(fieldCode, dynaBean.getStr(fieldCode));
					}
				}
				if(StringUtil.isNotEmpty(doFieldCodes) && doFieldCodes.split(",").length>1){
					String[] thisFields=doFieldCodes.split(",")[0].split("~");
					String[] targerFields=doFieldCodes.split(",")[1].split("~");
					for(int j=0;j<thisFields.length;j++){
						if(dynaBean.containsKey(thisFields[j])){
							bean.set(targerFields[j], dynaBean.getStr(thisFields[j]));
						}
					}
				}
				if(StringUtil.isNotEmpty(pkValue)){
					bean.set(pkCode, pkValue);
					bean=serviceTemplate.update(bean);
				}else if("1".equalsIgnoreCase(insertFlag)){
					serviceTemplate.buildModelCreateInfo(bean);
					bean=serviceTemplate.insert(bean);
					dynaBean.set(viewPkCode, bean.getStr(pkCode));
				}
			}
		}
	}

	/**
	 * 对视图级联的处理
	 * @param viewConfigInfo  TODO 暂不明确
	 * @param viewTableCode TODO 暂不明确
	 * @param mainPkCode TODO 暂不明确
	 * @param ids TODO 暂不明确
	 */
	public void doViewDelData(String viewConfigInfo,String viewTableCode,String mainPkCode,String ids){
		JSONArray arrays=JSONArray.fromObject(viewConfigInfo);
		List<DynaBean> lists=serviceTemplate.selectList(viewTableCode, " AND "+mainPkCode+" IN ("+StringUtil.buildArrayToString(ids.split(","))+")");
		for(int i=0;i<arrays.size();i++){
			JSONObject obj=arrays.getJSONObject(i);
			String tableCode=obj.getString("tableCode");
			String pkCode=obj.getString("pkCode");
			String viewPkCode=obj.getString("viewFieldCode");
			String fieldCodes=obj.getString("fieldCodes");
			String deleteFlag="0";
			String doFieldCodes="";
			if(obj.containsKey("doFieldCodes")){
				doFieldCodes=obj.getString("doFieldCodes");
			}
			if(obj.containsKey("deleteData")){
				deleteFlag=obj.getString("deleteData");
			}
			if(StringUtil.isNotEmpty(tableCode) && StringUtil.isNotEmpty(pkCode) && StringUtil.isNotEmpty(viewPkCode) && "1".equals(deleteFlag)){
				serviceTemplate.deleteByIds(StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(lists, viewPkCode), ","), tableCode, pkCode);
			}
		}
	}

	/**
	 * 删除数据相关 批注， 标记   编辑标记
	 * @param tableCode 表code
	 * @param pkCode
	 * @param ids id
	 * @param mark TODO 暂不明确
	 * @param funcEdit TODO 暂不明确
	 * @param postil TODO 暂不明确
	 * @param doTree TODO 暂不明确
	 */
	@Override
	public void doRemoveData(String tableCode,String pkCode, String ids, boolean mark, boolean funcEdit, boolean postil, boolean doTree) {
		if(StringUtil.isEmpty(ids))return;
		if(doTree){
			if(funcEdit){
				for(String id:ids.split(",")) {
					pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_FUNCEDIT WHERE FUNCEDIT_TABLECODE='" + tableCode + "' AND FUNCEDIT_PKVALUE IN (SELECT " + pkCode + " FROM " + tableCode + " WHERE SY_PATH LIKE '%"+id+"%')");
				}
			}
			if(mark){
				for(String id:ids.split(",")) {
					pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_MARK WHERE MARK_TABLECODE='" + tableCode + "' AND MARK_MODELID IN (SELECT " + pkCode + " FROM " + tableCode + " WHERE SY_PATH LIKE '%"+id+"%')");
				}
			}
			if(postil){
				for(String id:ids.split(",")) {
					pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_POSTIL WHERE POSTIL_TABLECODE='" + tableCode + "' AND POSTIL_MODELID IN (SELECT " + pkCode + " FROM " + tableCode + " WHERE SY_PATH LIKE '%"+id+"%')");
				}
			}
		}else{
			if(funcEdit){
				pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_FUNCEDIT WHERE FUNCEDIT_TABLECODE='" + tableCode + "' AND FUNCEDIT_PKVALUE IN ("+StringUtil.buildArrayToString(ids.split(","))+")");
			}
			if(mark){
				pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_MARK WHERE MARK_TABLECODE='" + tableCode + "' AND MARK_MODELID IN ("+StringUtil.buildArrayToString(ids.split(","))+")");
			}
			if(postil){
				pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_POSTIL WHERE POSTIL_TABLECODE='" + tableCode + "' AND POSTIL_MODELID IN ("+StringUtil.buildArrayToString(ids.split(","))+")");
			}
		}
	}

	@Override
	public Map<String,Object> doLoadOtherData(String queryType, MethodArgument param,String order) {
		String success="0";
		String returnFlag="0";
		String msg="";
		Long count=new Long(0);
		List<Map> lists=new ArrayList<>();
		Map<String,Object> returnObj=new HashMap<>();
		if ("procedure".equals(queryType)) {
			EndUser currentUser = SecurityUserHolder.getCurrentUser();
			String datasourceName = param.getDatasourceName();
			String procedureName = param.getProcedureName();
			String queryParamStr = param.getQueryParamsStr();
			String queryParamValueStr = param.getDbQueryObj();
			JSONArray arrays = JSONArray.fromObject(queryParamStr);
			JSONObject paramValues = JSONObject.fromObject(queryParamValueStr);
			Object[] params = new Object[arrays.size()];
			Set<Map.Entry> ddSet = new HashSet<Map.Entry>();
			//加入登录信息
			ddSet.addAll(SecurityUserHolder.getCurrentInfo(currentUser).entrySet());
			//加入用户变量
			ddSet.addAll(FrontCacheManager.getCacheValues().entrySet());
			ddSet.addAll(BackCacheManager.getCacheValues().entrySet());
			//加入系统设置
			ddSet.addAll(WebUtils.getAllSysVar().entrySet());
			//解析传递过来的参数值
			List<DbFieldVo> fieldVos = new ArrayList<>();
			String totalCountName = "";
			String codeName = "";
			String msgName = "";
			String orderFieldName = "";
			String orderType = "";
			for (int i = 0; i < arrays.size(); i++) {
				JSONObject obj = arrays.getJSONObject(i);
				String name = obj.getString("name");
				String fieldType = obj.getString("fieldType");
				String paramType = "1".equals(obj.getString("paramType")) ? "out" : "in";
				DbFieldVo fieldVo = new DbFieldVo(name, paramType, fieldType);
//				if(paramValues.containsKey(name)){
				if (DbFieldType.NOWPAGE.equals(fieldType)) {
					fieldVo.setValue(param.getPage());
				} else if (DbFieldType.LIMIT.equals(fieldType)) {
					fieldVo.setValue(param.getLimit());
				} else if (DbFieldType.TOTALCOUNT.equals(fieldType)) {
					totalCountName = name;
				} else if (DbFieldType.CODE.equals(fieldType)) {
					codeName = name;
				} else if (DbFieldType.MSG.equals(fieldType)) {
					msgName = name;
				} else if (DbFieldType.ORDERFIELD.equals(fieldType)) {
					orderFieldName = name;
					JSONArray sortArrays = JSONArray.fromObject(StringUtil.getDefaultValue(param.getSort(), "[]"));
					if (sortArrays.size() > 0) {
						JSONObject sortObj = sortArrays.getJSONObject(0);
						String sortCode = sortObj.getString("property");
						fieldVo.setValue(sortCode);
					}
				} else if (DbFieldType.ORDERTYPE.equals(fieldType)) {
					orderType = name;
					JSONArray sortArrays = JSONArray.fromObject(StringUtil.getDefaultValue(param.getSort(), "[]"));
					if (sortArrays.size() > 0) {
						JSONObject sortObj = sortArrays.getJSONObject(0);
						String sortType = sortObj.getString("direction");
						fieldVo.setValue(sortType);
					}
				} else if (DbFieldType.ORDER.equals(fieldType)) {
					JSONArray sortArrays = JSONArray.fromObject(StringUtil.getDefaultValue(param.getSort(), "[]"));
					String[] sortVals = new String[sortArrays.size()];
					for (int j = 0; j < sortArrays.size(); j++) {
						JSONObject sortObj = sortArrays.getJSONObject(j);
						String sortCode = sortObj.getString("property");
						String sortType = sortObj.getString("direction");
						sortVals[j] = sortCode + " " + sortType;
					}
					fieldVo.setValue(StringUtil.buildSplitString(sortVals, ","));
				}
				String defaultValue = "";
				if (obj.containsKey("defaultValue")) {
					defaultValue = obj.getString("defaultValue");
					if (StringUtil.isNotEmpty(defaultValue)) {
						defaultValue = StringUtil.parseKeyWord(defaultValue, ddSet);
						String val = StringUtil.getVarDefaultValue(defaultValue);
						if (StringUtil.isNotEmpty(val)) {
							fieldVo.setValue(val);
						}
					}
				}
				if ("out".equalsIgnoreCase(paramType)) {

				} else {
					if (paramValues.containsKey(name)) {
						JSONObject valObj = paramValues.getJSONObject(name);
						Object val = null;
						if (valObj.containsKey("value")) {
							val = valObj.get("value");
						}
						if (StringUtil.isNotEmpty(val + "")) {
							fieldVo.setValue(val);
						}
					}
				}
//				}
				fieldVos.add(fieldVo);
			}
			Map map = new HashMap();
			if (StringUtil.isEmpty(datasourceName) || "local".equals(datasourceName)) {
				map = pcServiceTemplate.queryMapProcedure("{call " + procedureName + "(" + pcDataService.getCallParams(params) + ")}", fieldVos);
			} else {
				SqlRunner sqlRunner = SqlRunner.getInstance(datasourceName);
				map = sqlRunner.queryMapProcedure("{call " + procedureName + "(" + pcDataService.getCallParams(params) + ")}", fieldVos);
			}
			if (StringUtil.isNotEmpty(codeName)) {
				Integer codeVal = Integer.parseInt(StringUtil.getDefaultValue(map.get(codeName) + "", "1"));
				if (codeVal < 0) {
					String errorMsg = "数据加载出错!";
					if (StringUtil.isNotEmpty(msgName)) {
						errorMsg = map.get(msgName) + "";
					}
					returnObj.put("returnFlag","1");
					returnObj.put("msg",errorMsg);
					returnObj.put("success","0");
					return returnObj;
				}
			}
//			List<Map> lists = new ArrayList<Map>();
			Integer allCount = 0;
			if (map.containsKey("rows")) {
				lists = (List<Map>) map.get("rows");
				allCount = lists.size();
			}
			if (map.containsKey(totalCountName)) {
				allCount = Integer.parseInt(map.get(totalCountName) + "");
			}
			success="1";
			returnFlag="1";
			count=new Long(allCount);
//			String strData = jsonBuilder.buildObjListToJson(new Long(allCount), lists, true);
//			toWrite(strData, param.getRequest(), param.getResponse());
		} else if ("sql".equals(queryType)) {
			EndUser currentUser = SecurityUserHolder.getCurrentUser();
			String datasourceName = param.getDatasourceName();
			String sql = param.getDbSql();
			String queryParamStr = param.getQueryParamsStr();
			String queryParamValueStr = param.getDbQueryObj();
			JSONArray arrays = JSONArray.fromObject(queryParamStr);
			JSONObject paramValues = JSONObject.fromObject(queryParamValueStr);

			Set<Map.Entry> ddSet = new HashSet<Map.Entry>();
			//加入登录信息
			ddSet.addAll(SecurityUserHolder.getCurrentInfo(currentUser).entrySet());
			//加入用户变量
			ddSet.addAll(FrontCacheManager.getCacheValues().entrySet());
			ddSet.addAll(BackCacheManager.getCacheValues().entrySet());
			//加入系统设置
			ddSet.addAll(WebUtils.getAllSysVar().entrySet());
			//解析传递过来的参数值
			List<DbFieldVo> fieldVos = new ArrayList<>();
			for (int i = 0; i < arrays.size(); i++) {
				JSONObject obj = arrays.getJSONObject(i);
				String name = obj.getString("name");
				String fieldType = obj.getString("fieldType");
				String paramType = "1".equals(obj.getString("paramType")) ? "out" : "in";
				String defaultValue = "";
				DbFieldVo fieldVo = new DbFieldVo(name, paramType, fieldType);
				if (obj.containsKey("defaultValue")) {
					defaultValue = obj.getString("defaultValue");
					if (StringUtil.isNotEmpty(defaultValue)) {
						defaultValue = StringUtil.parseKeyWord(defaultValue, ddSet);
						String val = StringUtil.getVarDefaultValue(defaultValue);
						if (StringUtil.isNotEmpty(val)) {
							fieldVo.setValue(val);
						}
					}
				}

				if ("out".equalsIgnoreCase(paramType)) {

				} else {
					if (paramValues.containsKey(name)) {
						JSONObject valObj = paramValues.getJSONObject(name);
						Object val = null;
						if (valObj.containsKey("value")) {
							val = valObj.get("value");
						}
						if (StringUtil.isNotEmpty(val + "")) {
							fieldVo.setValue(val);
						}
					}
				}
				fieldVos.add(fieldVo);
			}
//			List<Map> lists = new ArrayList<Map>();
			Long allCount = new Long(0);
			if (StringUtil.isEmpty(datasourceName) || "local".equals(datasourceName)) {
				sql = DbProduceUtil.buildQuerySql(sql);
				lists = pcServiceTemplate.queryMapBySql(sql + param.getWhereSql() + order, fieldVos, param.getStart(), param.getLimit());
				allCount = new Long(lists.size());
				if (param.getLimit() > 0) {
					allCount = pcServiceTemplate.countMapBySql(sql + param.getWhereSql(), fieldVos);
				}
			} else {
				SqlRunner sqlRunner = SqlRunner.getInstance(datasourceName);
				sql = DbProduceUtil.buildQuerySql(sql);
				lists = sqlRunner.queryMapBySql(sql + param.getWhereSql() + order, fieldVos, param.getStart(), param.getLimit());
				allCount = new Long(lists.size());
				if (param.getLimit() > 0) {
					allCount = sqlRunner.countMapBySql(sql + param.getWhereSql(), fieldVos);
				}
			}
			success="1";
			count=allCount;
			returnFlag="1";

		} else if ("iditprocedure".equals(queryType)) {
			String datasourceName = param.getDatasourceName();
			String procedureName = param.getProcedureName();
			String queryParamStr = param.getQueryParamsStr();
			String queryParamValueStr = param.getDbQueryObj();
			JSONArray arrays = JSONArray.fromObject(queryParamStr);
			JSONObject paramValues = JSONObject.fromObject(queryParamValueStr);
			Object[] params = new Object[arrays.size()];
			EndUser currentUser = SecurityUserHolder.getCurrentUser();
			Set<Map.Entry> ddSet = new HashSet<Map.Entry>();
			//加入登录信息
			ddSet.addAll(SecurityUserHolder.getCurrentInfo(currentUser).entrySet());
			//加入用户变量
			ddSet.addAll(FrontCacheManager.getCacheValues().entrySet());
			ddSet.addAll(BackCacheManager.getCacheValues().entrySet());
			//加入系统设置
			ddSet.addAll(WebUtils.getAllSysVar().entrySet());
			//解析传递过来的参数值
			List<DbFieldVo> fieldVos = new ArrayList<>();
			String totalCountName = "";
			String codeName = "";
			String msgName = "";
			for (int i = 0; i < arrays.size(); i++) {
				JSONObject obj = arrays.getJSONObject(i);
				String name = obj.getString("name");
				String fieldType = obj.getString("fieldType");
				String paramType = "1".equals(obj.getString("paramType")) ? "out" : "in";
				DbFieldVo fieldVo = new DbFieldVo(name, paramType, fieldType);
//				if(paramValues.containsKey(name)){
				if (DbFieldType.NOWPAGE.equals(fieldType)) {
					fieldVo.setValue(param.getPage());
				} else if (DbFieldType.LIMIT.equals(fieldType)) {
					fieldVo.setValue(param.getLimit());
				} else if (DbFieldType.TOTALCOUNT.equals(fieldType)) {
					totalCountName = name;
				} else if (DbFieldType.CODE.equals(fieldType)) {
					codeName = name;
				} else if (DbFieldType.MSG.equals(fieldType)) {
					msgName = name;
				} else if (DbFieldType.ORDER.equals(fieldType)) {
					JSONArray sortArrays = JSONArray.fromObject(StringUtil.getDefaultValue(param.getSort(), "[]"));
					String[] sortVals = new String[sortArrays.size()];
					for (int j = 0; j < sortArrays.size(); j++) {
						JSONObject sortObj = sortArrays.getJSONObject(j);
						String sortCode = sortObj.getString("property");
						String sortType = sortObj.getString("direction");
						sortVals[j] = sortCode + " " + sortType;
					}
					fieldVo.setValue(StringUtil.buildSplitString(sortVals, ","));
				}
				String defaultValue = "";
				if (obj.containsKey("defaultValue")) {
					defaultValue = obj.getString("defaultValue");
					if (StringUtil.isNotEmpty(defaultValue)) {
						defaultValue = StringUtil.parseKeyWord(defaultValue, ddSet);
						String val = StringUtil.getVarDefaultValue(defaultValue);
						if (StringUtil.isNotEmpty(val)) {
							fieldVo.setValue(val);
						}
					}
				}
				if ("out".equalsIgnoreCase(paramType)) {

				} else {
					if (paramValues.containsKey(name)) {
						JSONObject valObj = paramValues.getJSONObject(name);
						Object val = null;
						if (valObj.containsKey("value")) {
							val = valObj.get("value");
						}
						if (StringUtil.isNotEmpty(val + "")) {
							fieldVo.setValue(val);
						}
					}
				}
//				}
				fieldVos.add(fieldVo);
			}

			Map map = new HashMap();
			if (StringUtil.isEmpty(datasourceName) || "local".equals(datasourceName)) {
				map = pcServiceTemplate.queryMapProcedure("{call " + procedureName + "(" + pcDataService.getCallParams(params) + ")}", fieldVos);
			} else {
				SqlRunner sqlRunner = SqlRunner.getInstance(datasourceName);
				map = sqlRunner.queryMapProcedure("{call " + procedureName + "(" + pcDataService.getCallParams(params) + ")}", fieldVos);
			}
			if (StringUtil.isNotEmpty(codeName)) {
				Integer codeVal = Integer.parseInt(StringUtil.getDefaultValue(map.get(codeName) + "", "1"));
				if (codeVal < 0) {
					String errorMsg = "数据加载出错!";
					if (StringUtil.isNotEmpty(msgName)) {
						errorMsg = map.get(msgName) + "";
					}
					returnObj.put("returnFlag","1");
					returnObj.put("msg",errorMsg);
					returnObj.put("success","0");
					return returnObj;
				}
			}
//			List<Map> lists = new ArrayList<Map>();
			Integer allCount = 0;
			if (map.containsKey("rows")) {
				lists = (List<Map>) map.get("rows");
				allCount = lists.size();
			}
			if (map.containsKey(totalCountName)) {
				allCount = Integer.parseInt(map.get(totalCountName) + "");
			}
			returnFlag="1";
			success="1";
			count=new Long(allCount);
//			String strData = jsonBuilder.buildObjListToJson(new Long(allCount), lists, true);
//			toWrite(strData, param.getRequest(), param.getResponse());
		}
		returnObj.put("returnFlag",returnFlag);
		returnObj.put("success",success);
		returnObj.put("totalCount",count);
		returnObj.put("rows",lists);
		return returnObj;
	}

}

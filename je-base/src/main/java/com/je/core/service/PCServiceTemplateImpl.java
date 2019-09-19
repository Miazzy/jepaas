package com.je.core.service;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.je.core.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.constants.ConstantVars;
import com.je.core.constants.StatusType;
import com.je.core.constants.tree.NodeType;
import com.je.core.dao.PCDaoTemplate;
import com.je.core.dao.PCDaoTemplateImpl;
import com.je.core.entity.BaseEntity;
import com.je.core.entity.QueryInfo;
import com.je.core.entity.TreeBaseEntity;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.facade.extjs.JsonAssist;
import com.je.core.util.bean.DynaBean;
import com.je.document.service.DocumentManager;

/**
 * PCAT平台核心SERVICE接口实现类
 * @author YUNFENGCHENG
 * 2011-9-1 上午11:17:23
 */
@Component("PCServiceTemplateImpl")
public class PCServiceTemplateImpl implements PCServiceTemplate {
	private static Logger logger = LoggerFactory.getLogger(PCServiceTemplateImpl.class);
	private PCDaoTemplate daoTemplate;
	private DocumentManager documentManager;
	private JsonAssist jsonAssist=JsonAssist.getInstance();

	@Resource(name="docManager")
	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	}

	@Resource(name="PCDAOTemplateORCL")
	public void setDaoTemplate(PCDaoTemplate daoTemplate) {
		this.daoTemplate = daoTemplate;
	}

	/**
	 * 建议用在列表更新
	 * 更新实体 PCAT平台使用的更新采用拼接SQL方法而不采用HIBERNATE自带更新对象方法
	 * @param updateSqls
	 * @return
	 */
	@Override
	public int listUpdate(String[] updateSqls) {
		int rowsCount = 0;
		for(String updateSql : updateSqls) {
			rowsCount += daoTemplate.executeHql(updateSql);
		}
		return rowsCount;
	}

	/**
	 * SQL count分页方法
	 * @param sql
	 * @return
	 */
	@Override
	public Long countBySql(String sql) {
		return daoTemplate.countBySql(sql);
	}

	@Override
	public Long countBySql(String sql, Object[] params) {
		return daoTemplate.countBySql(sql,params);
	}

	@Override
	public Long countBySql(String sql, Map<String, Object> params) {
		return daoTemplate.countBySql(sql,params);
	}

	/**
	 * 调用存储过程
	 * @param procedureName
	 */
	@Override
	@Deprecated
	public void executeProcedureByName(String procedureName) {
		daoTemplate.executeProcedure(procedureName);
	}

	/**
	 * 执行SQL
	 * @param sql
	 * @return
	 */
	@Override
	public Long executeSql(String sql) {
		return daoTemplate.executeSql(sql);
	}

	@Override
	public Long executeSql(String sql, Object[] params) {
		return daoTemplate.executeSql(sql,params);
	}

	@Override
	public Long executeSql(String sql, Map<String, Object> params) {
		return daoTemplate.executeSql(sql,params);
	}

	/**
	 * 根据SQL语句查询结果(一般查询多个字段返回List<Object[]>)
	 * @param sql
	 * @return
	 */
	@Override
	public List<?> queryBySql(String sql) {
		return daoTemplate.queryBySql(sql);
	}

	@Override
	public List<?> queryBySql(String sql, Object[] params) {
		return daoTemplate.queryBySql(sql,params);
	}

	@Override
	public List<?> queryBySql(String sql, Map<String, Object> params) {
		return daoTemplate.queryBySql(sql,params);
	}

	/**
	 * 根据SQL语句查询结果(一般查询多个字段返回List<Object[]>)
	 * @param sql
	 * @return
	 */
	@Override
	public List<Map> queryMapBySql(String sql) {
		return daoTemplate.queryMapBySql(sql);
	}

	/**
	 *  执行存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 * @return
	 */
	@Override
	public Long executeProcedure(String callSql, Object[] params) {
		// TODO Auto-generated method stub
		return daoTemplate.executeProcedure(callSql, params);
	}

	@Override
	public Long executeProcedure(String callSql, Map<String, Object> params) {
		return daoTemplate.executeProcedure(callSql,params);
	}

	@Override
	public List queryProcedure(String callSql) {
		return daoTemplate.queryProcedure(callSql);
	}

	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 * @return
	 */
	@Override
	public List queryProcedure(String callSql, Object[] params) {
		// TODO Auto-generated method stub
		return daoTemplate.queryProcedure(callSql, params);
	}

	@Override
	public List queryProcedure(String callSql, Map<String, Object> params) {
		return daoTemplate.queryProcedure(callSql, params);
	}

	@Override
	public List<Map> queryMapProcedure(String callSql) {
		return daoTemplate.queryProcedure(callSql);
	}

	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 * @return
	 */
	@Override
	public List<Map> queryMapProcedure(String callSql, Object[] params) {
		// TODO Auto-generated method stub
		return daoTemplate.queryMapProcedure(callSql, params);
	}

	@Override
	public List<Map> queryMapProcedure(String callSql, Map<String, Object> params) {
		return daoTemplate.queryMapProcedure(callSql, params);
	}

	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param params 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 * @return
	 */
	@Override
	public Map queryMapOutParamProcedure(String callSql, Object[] params) {
		// TODO Auto-generated method stub
		return daoTemplate.queryMapOutParamProcedure(callSql, params);
	}

	/**
	 * 根据SQL语句查询结果
	 * @param sql
	 * @param paramValues 设定参数值
	 * @return
	 */
	@Override
	public List<Map> queryMapBySql(String sql,Object[] paramValues) {
		return daoTemplate.queryMapBySql(sql,paramValues);
	}

	@Override
	public List<Map> queryMapBySql(String sql, Map<String, Object> params) {
		return daoTemplate.queryMapBySql(sql,params);
	}

	@Override
	public List<Map> queryMapBySql(String sql, int start, int limit) {
		return daoTemplate.queryMapBySql(sql,start,limit);
	}

	/**
	 * 设定参数查询
	 * @param sql
	 * @param params
	 * @param start
	 * @param limit
	 * @return
	 */
	@Override
	public List<Map> queryMapBySql(String sql,Object[] params,int start,int limit){
		return daoTemplate.queryMapBySql(sql, params, start, limit);
	}

	@Override
	public List<Map> queryMapBySql(String sql, Map<String, Object> params, int start, int limit) {
		return daoTemplate.queryMapBySql(sql, params, start, limit);
	}

	/**
	 *  查询SQL分页查询
	 * @param sql
	 * @param fieldVos
	 * @param start
	 * @param limit
	 * @return
	 */
	@Override
	public List<Map> queryMapBySql(String sql,List<DbFieldVo> fieldVos,int start,int limit){
		return daoTemplate.queryMapBySql(sql, fieldVos, start, limit);
	}

	/**
	 * 查询SQL分页查询
	 * @param sql
	 * @param fieldVos
	 * @return
	 */
	@Override
	public Long countMapBySql(String sql,List<DbFieldVo> fieldVos){
		return daoTemplate.countMapBySql(sql, fieldVos);
	}

	@Override
	public Long executeProcedure(String callSql) {
		return daoTemplate.executeProcedure(callSql);
	}

	/**
	 * 通过SQL查询一个实体BEAN列表
	 * @param sql
	 * @param c
	 * @return
	 */
	@Override
	public List<?> queryBySql(String sql, Class<?> c) {
		return daoTemplate.queryBySql(sql, c);
	}

	@Override
	public List<?> queryBySql(String sql, Object[] params, Class<?> c) {
		return daoTemplate.queryBySql(sql,params, c);
	}

	@Override
	public List<?> queryBySql(String sql, Map<String, Object> params, Class<?> c) {
		return daoTemplate.queryBySql(sql,params, c);
	}

	/**
	 * 根据Sql分页查询集合
	 * @param sql
	 * @param start
	 * @param limit
	 * @param c
	 * @return
	 */
	@Override
	public List<?> queryBySql(String sql, int start, int limit, Class<?> c) {
		return daoTemplate.queryBySql(sql, start, limit,c);
	}

	@Override
	public List<?> queryBySql(String sql, Object[] params, int start, int limit, Class<?> c) {
		return daoTemplate.queryBySql(sql,params, start, limit,c);
	}

	@Override
	public List<?> queryBySql(String sql, Map<String, Object> params, int start, int limit, Class<?> c) {
		return daoTemplate.queryBySql(sql,params, start, limit,c);
	}

	/**
	 * 根据sql语句批量更新
	 * @param jsonSql
	 */
	@Override
	public void updateBatch(String jsonSql) {
		String[] sqls = jsonAssist.jsonSqlToString(jsonSql);
		if(sqls!=null&&sqls.length!=0){
			for(String sql:sqls){
				daoTemplate.executeSql(sql);
			}
		}
	}

	/**
	 * 获取数据库连接
	 * @return
	 */
	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return daoTemplate.getConnection();
	}

	/**
	 * 根据根ID，表名，和模板指定的表中字段名封装一棵抽象树（不带查询条件,不带包含ID）
	 * @param rootId
	 * @param tableName
	 * @param template
	 * @return
	 */
	public List<JSONTreeNode> getJsonTreeNodeList(String rootId, String tableName, JSONTreeNode template) {
		return getJsonTreeNodeList(rootId, tableName, template, null, null,null);
	}

	/**
	 * 查询树形数据集合
	 * @param rootId 根节点ID
	 * @param tableName 表名
	 * @param template 树形模版类
	 * @param queryInfo 查询信息
	 * @return
	 */
	public List<JSONTreeNode> getJsonTreeNodeList(String rootId, String tableName, JSONTreeNode template, QueryInfo queryInfo){
		return getJsonTreeNodeList(rootId, tableName, template, queryInfo, null,null);
	}

	/**
	 * 递归查询树形数据
	 * @param rootId 根节点ID
	 * @param tableName 表名
	 * @param template 模版
	 * @param queryInfo 查询对象
	 * @param includeIds 包含主键
	 * @param beanFields
	 * @return
	 */
	@Override
	public List<JSONTreeNode> getJsonTreeNodeList(String rootId, String tableName, JSONTreeNode template, QueryInfo queryInfo, List<String> includeIds,String[] beanFields) {
		List<JSONTreeNode> children = new ArrayList<JSONTreeNode>();
		if(TreeUtil.verify(template)) {
			String sql=DBSqlUtils.getPcDBMethodManager().getTreeSql(template, queryInfo, tableName, rootId);
			List<Map> treeItems=daoTemplate.queryMapBySql(sql.toString());
			if(null != treeItems && 0 != treeItems.size()) {
				for(int i=0; i<treeItems.size(); i++) {
					Map<String,Object> record=treeItems.get(i);
					JSONTreeNode node = new JSONTreeNode();
					//主键
					node.setId((String)record.get(template.getId()));
					//名称
					node.setText((String)record.get(template.getText()));
					//编码
					node.setCode((String)record.get(template.getCode()));
					//父节点
					node.setParent((String)record.get(template.getParent()));
					//节点信息
					if(StringUtil.isNotEmpty(template.getNodeInfo())){
						if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
							node.setNodeInfo(StringUtil.getClobValue(record.get(template.getNodeInfo())));
						}else{
							node.setNodeInfo(record.get(template.getNodeInfo())+"");
						}
					}
					//英文名称
					if(StringUtil.isNotEmpty(template.getEnField())){
						node.setEnField((String)record.get(template.getEnField())+"");
					}
					//父节点名称
					if(StringUtil.isNotEmpty(template.getParentText())){
						node.setParentText((String)record.get(template.getParentText())+"");
					}
					//节点信息类型
					if(StringUtil.isNotEmpty(template.getNodeInfoType())){
						node.setNodeInfoType((String)record.get(template.getNodeInfoType())+"");
					}
					//是否叶子
					if(StringUtil.isNotEmpty(template.getNodeType())){
						if(NodeType.LEAF.equalsIgnoreCase(record.get(template.getNodeType())+"")) {
							node.setLeaf(true);
						} else {
							node.setLeaf(false);
						}
						node.setNodeType((String)record.get(template.getNodeType())+"");
					}
					if(StringUtil.isNotEmpty(template.getLayer())){
						node.setLayer(record.get(template.getLayer())+"");
					}
					//图标图片地址
					if(StringUtil.isNotEmpty(template.getIcon())){
						node.setIcon(record.get(template.getIcon())+"");
					}
					//图标样式
					if(StringUtil.isNotEmpty(template.getIconCls())){
						node.setIconCls(record.get(template.getIconCls())+"");
					}
					//是否禁用
					if(StringUtil.isNotEmpty(template.getDisabled())){
						node.setDisabled(record.get(template.getDisabled())+"");
					}else{
						node.setDisabled("0");
					}
					//树形路径
					if(StringUtil.isNotEmpty(template.getNodePath())){
						node.setNodePath(record.get(template.getNodePath())+"");
					}
					//树形父节点路径
					if(StringUtil.isNotEmpty(template.getParentPath())){
						node.setParentPath(record.get(template.getParentPath())+"");
					}
					//链接
					if(StringUtil.isNotEmpty(template.getHref())){
						node.setHref(record.get(template.getHref())+"");
					}
					//链接目标
					if(StringUtil.isNotEmpty(template.getHrefTarget())){
						node.setHrefTarget(record.get(template.getHrefTarget())+"");
					}
					//描述
					if(StringUtil.isNotEmpty(template.getDescription())){
						if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
							node.setDescription(StringUtil.getClobValue(record.get(template.getDescription())));
						}else{
							node.setDescription(record.get(template.getDescription())+"");
						}
					}
					//排序
					if(StringUtil.isNotEmpty(template.getOrderIndex())){
						node.setOrderIndex(record.get(template.getOrderIndex())+"");
					}
					if(StringUtil.isNotEmpty(template.getTreeOrderIndex())){
						node.setTreeOrderIndex(record.get(template.getTreeOrderIndex())+"");
					}
					if(beanFields!=null && beanFields.length>0){
						Map<String,Object> beanValues=new HashMap<String,Object>();
						for(String fieldName:beanFields){
							beanValues.put(fieldName, record.get(fieldName));
						}
					}
					//如果是oracle则单独处理
					if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
						for(String key:record.keySet()){
							if(record.get(key)!=null && record.get(key) instanceof Clob){
								record.put(key, StringUtil.getClobValue(record.get(key)));
							}
							if(record.get(key)==null){
								record.put(key, "");
							}
						}
					}
					node.setBean(record);
					//排除指定节点
					if(includeIds != null && includeIds.size() > 0){
						String nodeId = node.getId();
						if(includeIds.contains(nodeId)){
							children.add(node);
						}
					}else{
						children.add(node);
					}
				}
			}
		}
		return children;
	}

	/*
	 * (non-Javadoc)
	 * @see com.je.core.service.PCServiceTemplate#getAsynJsonTreeNodeList(java.lang.String, java.lang.String, com.je.core.entity.extjs.JSONTreeNode, com.je.core.entity.QueryInfo)
	 */

	/**
	 * 根据parentId读取一层树元素
	 * @param rootId
	 * @param tableName
	 * @param template
	 * @param queryInfo
	 * @param isRoot
	 * @param onlyWhereSql
	 * @return
	 */
	@Override
	public List<JSONTreeNode> getAsynJsonTreeNodeList(String rootId, String tableName, JSONTreeNode template, QueryInfo queryInfo,Boolean isRoot,Boolean onlyWhereSql) {
		List<JSONTreeNode> children = new ArrayList<JSONTreeNode>();
		if(TreeUtil.verify(template)) {
			String sql =DBSqlUtils.getPcDBMethodManager().getAsynTreeSql(template, queryInfo, tableName, rootId,isRoot,onlyWhereSql);
			List<Map> treeItems = daoTemplate.queryMapBySql(sql.toString());
			if(null != treeItems && 0 != treeItems.size()) {
				for(int i=0; i<treeItems.size(); i++) {
					Map<String,Object> record=treeItems.get(i);
					JSONTreeNode node = new JSONTreeNode();
					//主键
					node.setId((String)record.get(template.getId()));
					//名称
					node.setText((String)record.get(template.getText()));
					//编码
					node.setCode((String)record.get(template.getCode()));
					//父节点
					node.setParent((String)record.get(template.getParent()));
					//节点信息
					if(StringUtil.isNotEmpty(template.getNodeInfo())){
						if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
							node.setNodeInfo(StringUtil.getClobValue(record.get(template.getNodeInfo())));
						}else{
							node.setNodeInfo(record.get(template.getNodeInfo())+"");
						}
					}
					//英文名称
					if(StringUtil.isNotEmpty(template.getEnField())){
						node.setEnField((String)record.get(template.getEnField())+"");
					}
					//父节点名称
					if(StringUtil.isNotEmpty(template.getParentText())){
						node.setParentText((String)record.get(template.getParentText())+"");
					}
					//节点信息类型
					if(StringUtil.isNotEmpty(template.getNodeInfoType())){
						node.setNodeInfoType((String)record.get(template.getNodeInfoType())+"");
					}
					//是否叶子
					if(StringUtil.isNotEmpty(template.getNodeType())){
						if(NodeType.LEAF.equalsIgnoreCase(record.get(template.getNodeType())+"")) {
							node.setLeaf(true);
						} else {
							node.setLeaf(false);
						}
						node.setNodeType((String)record.get(template.getNodeType())+"");
					}
					if(StringUtil.isNotEmpty(template.getLayer())){
						node.setLayer(record.get(template.getLayer())+"");
					}
					//图标图片地址
					if(StringUtil.isNotEmpty(template.getIcon())){
						node.setIcon(record.get(template.getIcon())+"");
					}
					//图标样式
					if(StringUtil.isNotEmpty(template.getIconCls())){
						node.setIconCls(record.get(template.getIconCls())+"");
					}
					//是否禁用
					if(StringUtil.isNotEmpty(template.getDisabled())){
						node.setDisabled(record.get(template.getDisabled())+"");
					}else{
						node.setDisabled("0");
					}
					//树形路径
					if(StringUtil.isNotEmpty(template.getNodePath())){
						node.setNodePath(record.get(template.getNodePath())+"");
					}
					//树形父节点路径
					if(StringUtil.isNotEmpty(template.getParentPath())){
						node.setParentPath(record.get(template.getParentPath())+"");
					}
					//链接
					if(StringUtil.isNotEmpty(template.getHref())){
						node.setHref(record.get(template.getHref())+"");
					}
					//链接目标
					if(StringUtil.isNotEmpty(template.getHrefTarget())){
						node.setHrefTarget(record.get(template.getHrefTarget())+"");
					}
					//描述
					if(StringUtil.isNotEmpty(template.getDescription())){
						if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
							node.setDescription(StringUtil.getClobValue(record.get(template.getDescription())));
						}else{
							node.setDescription(record.get(template.getDescription())+"");
						}
					}
					//排序
					if(StringUtil.isNotEmpty(template.getOrderIndex())){
						node.setOrderIndex(record.get(template.getOrderIndex())+"");
					}
					if(StringUtil.isNotEmpty(template.getTreeOrderIndex())){
						node.setTreeOrderIndex(record.get(template.getTreeOrderIndex())+"");
					}
//					if(beanFields!=null && beanFields.length>0){
//						Map<String,Object> beanValues=new HashMap<String,Object>();
//						for(String fieldName:beanFields){
//							beanValues.put(fieldName, record.get(fieldName));
//						}
//					}
					//如果是oracle则单独处理
					if(PCDaoTemplateImpl.DBNAME.equals(ConstantVars.STR_ORACLE)){
						for(String key:record.keySet()){
							if(record.get(key)!=null && record.get(key) instanceof Clob){
								record.put(key, StringUtil.getClobValue(record.get(key)));
							}
							if(record.get(key)==null){
								record.put(key, "");
							}
						}
					}
					node.setAsync(true);
					node.setBean(record);
					//排除指定节点
//					if(includeIds != null && includeIds.size() > 0){
//						String nodeId = node.getId();
//						if(includeIds.contains(nodeId)){
//							children.add(node);
//						}
//					}else{
					children.add(node);
//					}
				}
			}
		}
		return children;
	}

	/**
	 * 为异步树请求下一层结点的子数量
	 * @param rootId
	 * @param tableName
	 * @param template
	 * @param queryInfo
	 * @return
	 */
	@Override
	public Long getAsynJsonTreeNodeCount(String rootId, String tableName, JSONTreeNode template, QueryInfo queryInfo) {
		Long count = 0L;
//		List<AsyncJSONTreeNode> children = new ArrayList<AsyncJSONTreeNode>();
		if(TreeUtil.verify(template)) {
			String sql=DBSqlUtils.getPcDBMethodManager().getAsynTreeCount(template, queryInfo, tableName, rootId);
			count = daoTemplate.countBySql(sql.toString());
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * @see com.je.core.service.PCServiceTemplate#buildJSONTree(java.util.List)
	 */

	/**
	 * 取得JSONTreeNode
	 * chenmeng
	 * 2012-1-9 下午03:58:50
	 * @param aList
	 * @return
	 */
	@Override
	public JSONTreeNode buildJSONTree(List<JSONTreeNode> aList) {
		JSONTreeNode root = new JSONTreeNode();

		for(int i=0; i<aList.size(); i++) { //当前循环这个集合每一个元素
			JSONTreeNode node = aList.get(i);
			String parentId = node.getParent();//得到这个节点的父节点ID
			boolean isRoot = true;
			for(int j=0; j<aList.size(); j++) {//此层循环找到外层循环的元素的父亲然后添加上他
				JSONTreeNode parentNode = aList.get(j);
				if(parentNode.getId().equalsIgnoreCase(parentId)) {
					parentNode.getChildren().add(node);
					isRoot = false;
				}
			}
			if(isRoot) {//如果他没有找到则代表他是根节点
				root = node;
			}
		}
//		System.out.println(JsonAssist.getInstance().buildModelJson(root));
		return root;
	}

	@SuppressWarnings("unchecked")
	private void doRemoveDocuments(Class<BaseEntity> c, Serializable id) {
		//实体Bean  删除附件
	}

	/*
	 * 为Model保存相关的上传文档信息
	 */
	@SuppressWarnings("unchecked")
	private void doSaveDocuments(List<DynaBean> documentInfo, Object entityPo) {
		if(null != documentInfo && 0 != documentInfo.size()) {
			// 获取实体的主键值
			String domainId = EntityUtils.getInstance().getEntityIdValue(entityPo); // 主键
			// 封装上传文档信息描述 以fieldName为key ID|文件名 为value
			Map<String, String> documentDesc = new HashMap<String, String>();

			// 上传文档对象列表

			Iterator<DynaBean> itor = documentInfo.iterator();
			while(itor.hasNext()) {
				DynaBean doc = itor.next();
				doc.set("DOCUMENT_PKVALUE",domainId);
				/**
				 * 改动，张帅鹏  2013年1月10日 13:58:39  排除于formUploadFiles   因为当前作为多附件上传 则以前的附件记录保存
				 */
//				String hql = "from Document d where d.domainName = '" + entityPo.getClass().getName() +
//						"' and d.domainId = '" + domainId + "' and d.domainField = '" + doc.getDomainField() + "' and d.domainField!='formUploadFiles'";
				documentManager.doRemoveDocuments(entityPo.getClass().getName(), doc.getStr("DOCUMENT_FIELDCODE"), domainId);
				doc=documentManager.insertDoc(doc);
//				doc=pcDynaServiceTemplate.insert(doc);
				// set model field
				StringBuffer fieldValue = null;
				String domainField = documentDesc.get(doc.getStr("DOCUMENT_FIELDCODE"));
				if(StringUtils.isEmpty(domainField)) {
					// 如果文档信息描述中还没有以此字段为key的记录，则新建此key/value
					fieldValue = new StringBuffer();
					fieldValue.append(doc.getStr("JE_CORE_DOCUMENT_ID") + "|" + doc.getStr("DOCUMENT_DOCNAME")); //docId|文件名
					fieldValue.append("#");
					documentDesc.put(doc.getStr("DOCUMENT_FIELDCODE"), fieldValue.toString());
				} else {
					// 如果文档信息描述中已有以此字段为key的记录，则append
					fieldValue = new StringBuffer(domainField);
					fieldValue.append(doc.getStr("JE_CORE_DOCUMENT_ID") + "|" + doc.getStr("DOCUMENT_DOCNAME")); //docId|文件名
					fieldValue.append("#");
					documentDesc.put(doc.getStr("DOCUMENT_FIELDCODE"), fieldValue.toString());
				}
			}


			// 封装好文档信息描述map后，循环为主实体的相关字段赋值
			if(0 != documentDesc.size()) {
//				Iterator<String> fieldsKey = documentDesc.keySet().iterator();
//				while(fieldsKey.hasNext()) {
//					String fieldName = fieldsKey.next();
//					String setMethodName = EntityUtils.getInstance().getWriteMethod(fieldName);
//					String filesInfo = documentDesc.get(fieldName).substring(0, documentDesc.get(fieldName).length()-1);
//					ReflectionUtils.getInstance().invokeMethod(entityPo, setMethodName, new Object[]{filesInfo});
//				}
			}
		}
	}

	/**
	 * 获取sessionFactory
	 * @return
	 */
	@Override
	public SessionFactory getSessionFactory() {
		return daoTemplate.getSessionFactory();
	}

	/**
	 * 通过SQL获取结果集字段列表名
	 * @param sql
	 * @return
	 */
	@Override
	public List<String> getColumnListBySql(String sql) {
		return daoTemplate.getColumnListBySql(sql);
	}
	/**
	 * 将list递归成树   替换原有的for循环嵌套
	 * @param lists
	 * @return
	 * @author zhangshuaipeng
	 */
	public JSONTreeNode buildJSONNewTree(List<JSONTreeNode> lists,String rootId) {
		JSONTreeNode root = new JSONTreeNode();
		for(JSONTreeNode node:lists) { //当前循环这个集合每一个元素
			if(node.getParent()==null || node.getParent().equals("") || node.getId().equals(rootId)){
				root=node;
				lists.remove(node);
				break;
			}
		}
		createTreeChildren(lists, root);
		return root;
	}
	/**
	 * 递归方法
	 * @param childrens
	 * @param root
	 */
	public void createTreeChildren(List<JSONTreeNode> childrens,JSONTreeNode root){
		String parentId=root.getId();
		for(int i=0;i<childrens.size();i++){
			JSONTreeNode node=childrens.get(i);
//			if(node.getParent()!=null){
			if(parentId.equals(node.getParent())){
				root.getChildren().add(node);
				//当前不能删除节点，因为孩子引用与它， 递归回来，坐标失效
				if(node.isLeaf()==false){
					createTreeChildren(childrens, node);
				}
			}
//			}
			if(i==childrens.size()-1){
//				if(root.getChildren().size()==0){
//					root.setLeaf(true);
//				}else{
//					root.setLeaf(false);
//				}
				if(root.getChildren().size()>0){
					root.setLeaf(false);
				}
				return;
			}
		}
	}

	/**
	 * 执行查询存储过程
	 * @param callSql  执行存储过程SQL，如果有参数，使用?占位符  例：{Call pro_getManager(?,?) }
	 * @param fieldVos 参数   无参数传null即可，有参数传递格式为  比如  new Object[]{"zhangsan",3} 也可以为  new String[]{"2","3"}
	 * @return
	 */
	@Override
	public Map queryMapProcedure(String callSql, List<DbFieldVo> fieldVos) {
		// TODO Auto-generated method stub
		return daoTemplate.queryMapProcedure(callSql, fieldVos);
	}

	@Override
	public Map queryMapOutParamProcedure(String callSql) {
		return daoTemplate.queryMapOutParamProcedure(callSql);
	}


	/**----------------------原hibernate的实体类操作，HQL语句 后续直接删除-----------------------------**/
	/**
	 * 保存实体
	 * @param entity
	 * @return
	 */
	@Override
	public Object save(Object entity) {
		List<DynaBean> documentInfo = ((BaseEntity)entity).getDocumentInfo();
		Object entityPo = daoTemplate.save(entity);
		doSaveDocuments(documentInfo, entityPo);
		return entityPo;
	}

	/**
	 * 保存treeEntity
	 * 之前的方法中，前台不能传layer和nod
	 * 只在DictionaryItem中调用
	 * chenmeng
	 * 2012-1-6 下午01:36:40
	 * @param treeEntity
	 * @return
	 */
	@Override
	public Object saveTreeEntity(TreeBaseEntity treeEntity) {
		TreeBaseEntity parent = treeEntity.getParent();
		if(null != parent){
			Integer layer=parent.getLayer();
			if(layer==null){
				layer=0;
			}
			treeEntity.setLayer(layer + 1);
			if(!NodeType.ROOT.equalsIgnoreCase(parent.getNodeType())) {
				parent.setNodeType(NodeType.GENERAL);
			}
		}
		daoTemplate.save(treeEntity);
		return treeEntity;
	}

	/**
	 * 更新实体
	 * @param entity
	 * @return
	 */
	@Override
	public Object updateEntity(Object entity) {
		// TODO Auto-generated method stub
		return daoTemplate.updateEntity(entity);
	}

	/**
	 * 可选使用缓存
	 * @param clazz
	 * @param id
	 * @param useCache
	 * @return
	 */
	@Override
	public BaseEntity getEntityById(Class<?> clazz, Serializable id, boolean useCache) {
		return daoTemplate.getEntityById(clazz, id, useCache);
	}
	/**
	 * 建议用在表单更新
	 * 更新实体(纯HIBERNATE操作)
	 * @param entity
	 * @return
	 */
	@Override
	public Object formUpdate(Object entity) {
		List<DynaBean> documentInfo = ((BaseEntity)entity).getDocumentInfo();
		Object entityPo = daoTemplate.updateByHQL(entity);
		doSaveDocuments(documentInfo, entityPo);
		return entityPo;
	}

	/**
	 * HQL count分页方法
	 * @param hql
	 * @return
	 */
	@Override
	public Long countByHql(String hql) {
		return daoTemplate.countByHql(hql);
	}

	/**
	 * 针对某一实体类型进行指定ID删除
	 * @param c
	 * @param id
	 */
	@Override
	public void removeEntityById(Class<BaseEntity> c, Serializable id) {
		daoTemplate.removeEntityById(c, id);
		this.doRemoveDocuments(c, id);
	}

	/**
	 * 根据ID列表批量删除
	 * @param c
	 * @param ids
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void removeEntitiesByIds(Class<?> c, String[] ids) {
		for(String id : ids) {
			removeDocumentFileById(c, id);
			removeEntityById((Class<BaseEntity>)c, id);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.je.core.service.PCServiceTemplate#doDisable(java.lang.Class, java.lang.String[])
	 */

	/**
	 * 根据ids主键列表停用一组记录
	 * @param c
	 * @param ids
	 */
	@Override
	public void doDisable(Class<? extends BaseEntity> c, String[] ids) {
		for(String id : ids) {
			BaseEntity entity = daoTemplate.getEntityById(c, id);
			entity.setStatus(StatusType.DISABLED);
		}
	}

	private void removeDocumentFileById(Class<?> c, String id) {
		//实体bean删除
	}

	/**
	 * 执行HQL
	 * @param hql
	 * @return
	 */
	@Override
	public Long executeHql(String hql) {
		return daoTemplate.executeHql(hql);
	}

	/**
	 * 按一个唯一约束字段获取单个实体
	 * @param c
	 * @param columnName
	 * @param value
	 * @return
	 */
	@Override
	public Object getByUniqueValue(Class<BaseEntity> c, String columnName, Object value) {
		return daoTemplate.getByUniqueValue(c, columnName, value);
	}

	/**
	 * 针对某一实体类型进行指定ID获取
	 * @param c
	 * @param id
	 * @return
	 */
	@Override
	public BaseEntity getEntityById(Class<?> c, Serializable id) {
		return daoTemplate.getEntityById(c, id);
	}

	/**
	 * 根据实体模板like
	 * 1.不支持主键
	 * 2.不支持关联
	 * 3.不支持NULL
	 * @param example
	 * @return
	 */
	@Override
	public List<?> queryByExample(BaseEntity example) {
		return daoTemplate.queryByExample(example);
	}

	/**
	 * 根据实体模板lik
	 * 1.不支持主键
	 * 2.不支持关联
	 * 3.不支持NULL
	 * @param example
	 * @param start
	 * @param limit
	 * @return
	 */
	@Override
	public List<?> queryByExample(BaseEntity example, int start, int limit) {
		return daoTemplate.queryByExample(example, start, limit);
	}

	/**
	 * 根据HQL查询实体列表
	 * @param hql
	 * @return
	 */
	@Override
	public List<?> queryByHql(String hql) {
		return daoTemplate.queryByHql(hql);
	}

	/**
	 * 根据HQL分页查询实体列表
	 * @param hql
	 * @param start
	 * @param limit
	 * @return
	 */
	@Override
	public List<?> queryByHql(String hql, int start, int limit) {
		return daoTemplate.queryByHql(hql, start, limit);
	}
	/**
	 * 通过unique值获取一个实体，如果获取了多于一个的实体记录，则抛出DuplicatedDataException
	 * @param hql
	 * @return
	 */
	@Override
	public BaseEntity getEntityByUniqueValue(String hql) {
		return daoTemplate.getEntityByUniqueValue(hql);
	}
}

package com.je.core.service;

import java.util.List;

import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.DynaBean;

/**
 * TODO 暂不明确
 */
public  class PcDBMethodManagerImpl4Oracle implements PcDBMethodManager {

	/**
	 * 构建查询同步树的sql语句
	 * @param template TODO 暂不明确
	 * @param queryInfo TODO 暂不明确
	 * @param tableName 表名称
	 * @param rootId 根节点id
	 * @return
	 */
	@Override
	public String getTreeSql(JSONTreeNode template,QueryInfo queryInfo,String tableName,String rootId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t." + template.getId()); //主键
		sql.append(", t." + template.getText()); // 名称
		sql.append(", t." + template.getCode()); // 编码
		sql.append(", t." + template.getParent()); // 父节点
		// 英文名称
		if(StringUtil.isNotEmpty(template.getEnField())){
			sql.append(","+template.getEnField());
		}
		// 节点类型
		if(StringUtil.isNotEmpty(template.getNodeType())){
			sql.append(", t."+template.getNodeType());
		}
		if(StringUtil.isNotEmpty(template.getParentText())){
			sql.append(","+template.getParentText());
		}
		// 节点信息
		if(StringUtil.isNotEmpty(template.getNodeInfo())){
			sql.append(", t."+template.getNodeInfo());
		}
		// 节点信息
		if(StringUtil.isNotEmpty(template.getLayer())){
			sql.append(", t."+template.getLayer());
		}
		// 节点信息类型
		if(StringUtil.isNotEmpty(template.getNodeInfoType())){
			sql.append(", t."+template.getNodeInfoType());
		}
		// 图标图片地址
		if(StringUtil.isNotEmpty(template.getIcon())) {
			sql.append(", t." + template.getIcon());
		}
		// 图标样式
		if(StringUtil.isNotEmpty(template.getIconCls())){
			sql.append(", t." + template.getIconCls());
		}
		//是否禁用
		if(StringUtil.isNotEmpty(template.getDisabled())) {
			sql.append(", t." + template.getDisabled());
		}
		//树形路径
		if(StringUtil.isNotEmpty(template.getNodePath())){
			sql.append(", t." + template.getNodePath());
		}
		//树形父节点路径
		if(StringUtil.isNotEmpty(template.getParentPath())){
			sql.append(", t." + template.getParentPath());
		}
		//链接
		if(StringUtil.isNotEmpty(template.getHref())){
			sql.append(", t." + template.getHref());
		}
		//链接目标
		if(StringUtil.isNotEmpty(template.getHrefTarget())){
			sql.append(", t." + template.getHrefTarget());
		}
		//描述
		if(StringUtil.isNotEmpty(template.getDescription())){
			sql.append(", t." + template.getDescription());
		}
		if(StringUtil.isNotEmpty(template.getOrderIndex())){
			sql.append(", t." + template.getOrderIndex());
		}
		if(StringUtil.isNotEmpty(template.getTreeOrderIndex())){
			sql.append(", t." + template.getTreeOrderIndex());
		}
		if(StringUtil.isNotEmpty(template.getFieldCodes())){
			sql.append(template.getFieldCodes());
		}
		sql.append(" FROM " + tableName + " t ");
		sql.append(" where (1=1 ");
		if(null != queryInfo) {
			sql.append(queryInfo.getWhereSql());
		}
		sql.append(") OR "+template.getId()+" = '"+rootId+"' ");
		sql.append(" START WITH t." + template.getId() + " = '" + rootId + "'");
		sql.append(" CONNECT BY t."+template.getParent()+" = PRIOR " + template.getId()+" ");
		if(null != queryInfo && StringUtil.isNotEmpty(queryInfo.getOrderSql())){
			sql.append(queryInfo.getOrderSql());
		}else{
			sql.append(" ORDER BY "+template.getParent()+" ASC");
			if(StringUtil.isNotEmpty(template.getOrderIndex())){
				sql.append(", "+template.getOrderIndex()+" ASC");
			}
		}
		return sql.toString();
	}

	/**
	 * 构建查询异步树的sql语句
	 * @param template TODO 暂不明确
	 * @param queryInfo TODO 暂不明确
	 * @param tableName 表名称
	 * @param rootId 根节点id
	 * @param isRoot TODO 暂不明确
	 * @param onlyWhereSql TODO 暂不明确
	 * @return
	 */
	@Override
	public String getAsynTreeSql(JSONTreeNode template, QueryInfo queryInfo,
								 String tableName, String rootId,Boolean isRoot,Boolean onlyWhereSql) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t." + template.getId()); //主键
		sql.append(", t." + template.getText()); // 名称
		sql.append(", t." + template.getCode()); // 编码
		sql.append(", t." + template.getParent()); // 父节点
		// 英文名称
		if(StringUtil.isNotEmpty(template.getEnField())){
			sql.append(","+template.getEnField());
		}
		if(StringUtil.isNotEmpty(template.getParentText())){
			sql.append(","+template.getParentText());
		}
		// 节点类型
		if(StringUtil.isNotEmpty(template.getNodeType())){
			sql.append(", t."+template.getNodeType());
		}
		// 节点信息
		if(StringUtil.isNotEmpty(template.getNodeInfo())){
			sql.append(", t."+template.getNodeInfo());
		}
		// 节点信息类型
		if(StringUtil.isNotEmpty(template.getNodeInfoType())){
			sql.append(", t."+template.getNodeInfoType());
		}
		// 图标图片地址
		if(StringUtil.isNotEmpty(template.getIcon())) {
			sql.append(", t." + template.getIcon());
		}
		// 图标样式
		if(StringUtil.isNotEmpty(template.getIconCls())){
			sql.append(", t." + template.getIconCls());
		}
		//是否禁用
		if(StringUtil.isNotEmpty(template.getDisabled())) {
			sql.append(", t." + template.getDisabled());
		}
		//树形路径
		if(StringUtil.isNotEmpty(template.getNodePath())){
			sql.append(", t." + template.getNodePath());
		}
		//树形父节点路径
		if(StringUtil.isNotEmpty(template.getParentPath())){
			sql.append(", t." + template.getParentPath());
		}
		//链接
		if(StringUtil.isNotEmpty(template.getHref())){
			sql.append(", t." + template.getHref());
		}
		//链接目标
		if(StringUtil.isNotEmpty(template.getHrefTarget())){
			sql.append(", t." + template.getHrefTarget());
		}
		//描述
		if(StringUtil.isNotEmpty(template.getDescription())){
			sql.append(", t." + template.getDescription());
		}
		if(StringUtil.isNotEmpty(template.getOrderIndex())){
			sql.append(", t." + template.getOrderIndex());
		}
		if(StringUtil.isNotEmpty(template.getTreeOrderIndex())){
			sql.append(", t." + template.getTreeOrderIndex());
		}
		sql.append(" FROM " + tableName + " t ");
		sql.append(" where 1=1 ");
		if(null != queryInfo) {
			sql.append(queryInfo.getWhereSql());
		}
		if(!onlyWhereSql){
			if(isRoot){
				sql.append(" AND (t."+template.getParent()+"='"+rootId+"' OR t."+template.getId()+"='"+rootId+"')");
			}else{
				sql.append("AND t."+template.getParent()+"='"+rootId+"'");
			}
		}
		if(null != queryInfo && StringUtil.isNotEmpty(queryInfo.getOrderSql())){
			sql.append(queryInfo.getOrderSql());
		}else {
			sql.append(" ORDER BY " + template.getParent() + " asc");
			if (StringUtil.isNotEmpty(template.getOrderIndex())) {
				sql.append(", " + template.getOrderIndex() + " asc ");
			}
		}
		return sql.toString();
	}

	/**
	 * 查询树形的count
	 * @param template TODO 暂不明确
	 * @param queryInfo TODO 暂不明确
	 * @param tableName 表名称
	 * @param rootId 根节点id
	 * @return
	 */
	@Override
	public String getAsynTreeCount(JSONTreeNode template, QueryInfo queryInfo,
								   String tableName, String rootId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(t." + template.getId()); // 0
		sql.append(") FROM " + tableName + " t ");
		sql.append(" where 1=1 ");
		if(null != queryInfo) {
			sql.append(queryInfo.getWhereSql());
		}
		if(StringUtil.isNotEmpty(rootId)) {
			sql.append(" and t." + template.getParent() + " = '" + rootId + "'");
		} else {
			sql.append(" and t." + template.getParent() + " is null ");
		}
		return sql.toString();
	}

	/**
	 * 构建DynaBean查询同步树的sql语句
	 * @param columns
	 * @param template TODO 暂不明确
	 * @param tableName 表名称
	 * @param rootId 根节点id
	 * @param queryInfo TODO 暂不明确
	 * @return
	 */
	@Override
	public String getDynaTreeSql(List<DynaBean> columns, JSONTreeNode template, String tableName, String rootId,QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		for(DynaBean column:columns){
			sql.append(" t."+column.getStr("TABLECOLUMN_CODE")+",");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(" FROM " + tableName + " t ");
		sql.append(" where (1=1 ");
		if(null != queryInfo) {
			sql.append(queryInfo.getWhereSql());
		}
		sql.append(") OR "+template.getId()+" = '"+rootId+"' ");
		sql.append(" START WITH t." + template.getId() + " = '" + rootId + "'");
		sql.append(" CONNECT BY t."+template.getParent()+" = PRIOR " + template.getId()+" ");
		if(null != queryInfo && StringUtil.isNotEmpty(queryInfo.getOrderSql())){
			sql.append(queryInfo.getOrderSql());
		}else{
			sql.append(" ORDER BY "+template.getParent()+" ASC, SY_ORDERINDEX ASC");
		}
		return sql.toString();
	}

	/**
	 * 获取角色权限查询sql
	 * @param rootId 根节点
	 * @return
	 */
	@Override
	public String getRolePermSql(String rootId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.ROLEID"); // 0
		sql.append(", t.ROLENAME"); // 1
		sql.append(", t.ROLECODE"); // 2
		sql.append(", t.GROUPNAME"); // 3
		sql.append(", t.GROUPCODE"); // 4
		sql.append(", t.EXTENDGROUPNAME"); // 5
		sql.append(", t.EXTENDGROUPCODE"); // 6
		sql.append(", t.REJECTGROUPNAME"); // 7
		sql.append(", t.REJECTGROUPCODE"); // 8
		sql.append(", t.PATH"); // 9
		sql.append(", t.PARENT"); // 10
		sql.append(" FROM JE_CORE_ROLE t ");
		sql.append(" where 1=1 ");
		sql.append(" START WITH t.ROLEID = '" + rootId + "'");
		sql.append(" CONNECT BY t.parent = PRIOR ROLEID");
		sql.append(" ORDER BY PARENT asc, ORDERINDEX asc");
		return sql.toString();
	}

	/**
	 * 获取角色权限查询sql
	 * @param rootId 根节点
	 * @return
	 */
	@Override
	public String getRolePermLikeSql(String rootId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.ROLEID"); // 0
		sql.append(", t.ROLENAME"); // 1
		sql.append(", t.ROLECODE"); // 2
		sql.append(", t.GROUPNAME"); // 3
		sql.append(", t.GROUPCODE"); // 4
		sql.append(", t.EXTENDGROUPNAME"); // 5
		sql.append(", t.EXTENDGROUPCODE"); // 6
		sql.append(", t.REJECTGROUPNAME"); // 7
		sql.append(", t.REJECTGROUPCODE"); // 8
		sql.append(", t.PATH"); // 9
		sql.append(", t.PARENT"); // 10
		sql.append(" FROM JE_CORE_ROLE t ");
		sql.append(" where 1=1 ");
		sql.append(" AND PATH LIKE '%" + rootId + "%'");
//		sql.append(" CONNECT BY t.parent = PRIOR ROLEID");
		sql.append(" ORDER BY PARENT asc, ORDERINDEX asc");
		return sql.toString();
	}

	/**
	 * 获取角色组查询SQL
	 * @param rootId 根节点
	 * @return
	 */
	@Override
	public String getRoleGroupPermSql(String rootId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.JE_CORE_ROLEGROUP_ID"); // 0
		sql.append(", t.ROLEGROUP_TEXT"); // 1
		sql.append(", t.ROLEGROUP_CODE"); // 2
		sql.append(", t.SY_PATH"); // 9
		sql.append(", t.SY_PARENT"); // 10
		sql.append(" FROM JE_CORE_ROLEGROUP t ");
		sql.append(" where 1=1 ");
		sql.append(" START WITH t.JE_CORE_ROLEGROUP_ID = '" + rootId + "'");
		sql.append(" CONNECT BY t.SY_PARENT = PRIOR JE_CORE_ROLEGROUP_ID");
		sql.append(" ORDER BY SY_PARENT asc, SY_ORDERINDEX asc");
		return sql.toString();
	}

	/**
	 * 获取角色组查询SQL
	 * @param rootId  根节点
	 * @return
	 */
	@Override
	public String getRoleGroupPermLikeSql(String rootId) {
		// TODO Auto-generated method stub

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.JE_CORE_ROLEGROUP_ID"); // 0
		sql.append(", t.ROLEGROUP_TEXT"); // 1
		sql.append(", t.ROLEGROUP_CODE"); // 2
		sql.append(", t.SY_PATH"); // 9
		sql.append(", t.SY_PARENT"); // 10
		sql.append(" FROM JE_CORE_ROLEGROUP t ");
		sql.append(" where 1=1 ");
		sql.append(" AND SY_PATH LIKE '%" + rootId + "%'");
//		sql.append(" CONNECT BY t.SY_PARENT = PRIOR JE_CORE_ROLEGROUP_ID");
		sql.append(" ORDER BY SY_PARENT asc, SY_ORDERINDEX asc");
		return sql.toString();
	}

	/**
	 * 获取数据生成UUID函数
	 * @return
	 */
	@Override
	public String getGenerateUUID() {
		// TODO Auto-generated method stub
		return "sys_guid()";
	}

	/**
	 * 获取截取字符串函数
	 * @return
	 */
	@Override
	public String getSubString() {
		// TODO Auto-generated method stub
		return "substr";
	}

	/**
	 * 获取字符串长度函数
	 * @return
	 */
	@Override
	public String getLength() {
		// TODO Auto-generated method stub
		return "length";
	}

	/**
	 * 获取数据库修改视图语句
	 * @return
	 */
	@Override
	public String getUpdateView() {
		// TODO Auto-generated method stub
		return "CREATE OR REPLACE VIEW ";
	}

}

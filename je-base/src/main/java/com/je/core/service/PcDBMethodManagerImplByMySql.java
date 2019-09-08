package com.je.core.service;

import java.util.List;


import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.DynaBean;

/**
 * TODO 暂不明确
 */
public class PcDBMethodManagerImplByMySql implements PcDBMethodManager {
	/**
	 * 构建查询同步树的sql语句
	 * @param template TODO 暂不明确
	 * @param queryInfo TODO 暂不明确
	 * @param tableName 表名称
	 * @param rootId 根节点id
	 * @return
	 */
	@Override
	public String getTreeSql(JSONTreeNode template, QueryInfo queryInfo,
							 String tableName, String rootId) {
		// TODO Auto-generated method stub
		StringBuffer filedSql=new StringBuffer();
		filedSql.append(""+template.getId()+","+template.getCode()+","+template.getText()+","+template.getParent());
		// 英文名称
		if(StringUtil.isNotEmpty(template.getEnField())){
			filedSql.append(","+template.getEnField());
		}
		if(StringUtil.isNotEmpty(template.getParentText())){
			filedSql.append(","+template.getParentText());
		}
		// 节点类型
		if(StringUtil.isNotEmpty(template.getNodeType())){
			filedSql.append(","+template.getNodeType());
		}
		// 节点信息
		if(StringUtil.isNotEmpty(template.getNodeInfo())){
			filedSql.append(","+template.getNodeInfo());
		}
		// 节点信息类型
		if(StringUtil.isNotEmpty(template.getNodeInfoType())){
			filedSql.append(","+template.getNodeInfoType());
		}
		if(StringUtil.isNotEmpty(template.getLayer())){
			filedSql.append(","+template.getLayer());
		}
		// 图标图片地址
		if(StringUtil.isNotEmpty(template.getIcon())) {
			filedSql.append("," + template.getIcon());
		}
		// 图标样式
		if(StringUtil.isNotEmpty(template.getIconCls())){
			filedSql.append("," + template.getIconCls());
		}
		//是否禁用
		if(StringUtil.isNotEmpty(template.getDisabled())) {
			filedSql.append("," + template.getDisabled());
		}
		//树形路径
		if(StringUtil.isNotEmpty(template.getNodePath())){
			filedSql.append("," + template.getNodePath());
		}
		//树形父节点路径
		if(StringUtil.isNotEmpty(template.getParentPath())){
			filedSql.append("," + template.getParentPath());
		}
		//链接
		if(StringUtil.isNotEmpty(template.getHref())){
			filedSql.append("," + template.getHref());
		}
		//链接目标
		if(StringUtil.isNotEmpty(template.getHrefTarget())){
			filedSql.append("," + template.getHrefTarget());
		}
		//描述
		if(StringUtil.isNotEmpty(template.getDescription())){
			filedSql.append("," + template.getDescription());
		}
		if(StringUtil.isNotEmpty(template.getOrderIndex())){
			filedSql.append("," + template.getOrderIndex());
		}
		if(StringUtil.isNotEmpty(template.getTreeOrderIndex())){
			filedSql.append("," + template.getTreeOrderIndex());
		}
		if(StringUtil.isNotEmpty(template.getFieldCodes())){
			filedSql.append(template.getFieldCodes());
		}
		StringBuffer sql=new StringBuffer();
		sql.append(" select "+filedSql.toString()+" from "+tableName+" where 1=1 and ("+template.getNodePath()+" like '%"+rootId+"%'");
		if(null != queryInfo) {
			sql.append(queryInfo.getWhereSql());
		}
		sql.append(") OR "+template.getId()+" = '"+rootId+"' ");
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
		sql.append("SELECT " + template.getId()); //主键
		sql.append(", " + template.getText()); // 名称
		sql.append("," + template.getCode()); // 编码
		sql.append("," + template.getParent()); // 父节点
		// 英文名称
		if(StringUtil.isNotEmpty(template.getEnField())){
			sql.append(","+template.getEnField());
		}
		if(StringUtil.isNotEmpty(template.getParentText())){
			sql.append(","+template.getParentText());
		}
		// 节点类型
		if(StringUtil.isNotEmpty(template.getNodeType())){
			sql.append(","+template.getNodeType());
		}
		// 节点信息
		if(StringUtil.isNotEmpty(template.getNodeInfo())){
			sql.append(","+template.getNodeInfo());
		}
		// 节点信息类型
		if(StringUtil.isNotEmpty(template.getNodeInfoType())){
			sql.append(","+template.getNodeInfoType());
		}
		// 图标图片地址
		if(StringUtil.isNotEmpty(template.getIcon())) {
			sql.append("," + template.getIcon());
		}
		// 图标样式
		if(StringUtil.isNotEmpty(template.getIconCls())){
			sql.append("," + template.getIconCls());
		}
		//是否禁用
		if(StringUtil.isNotEmpty(template.getDisabled())) {
			sql.append("," + template.getDisabled());
		}
		//树形路径
		if(StringUtil.isNotEmpty(template.getNodePath())){
			sql.append("," + template.getNodePath());
		}
		//树形父节点路径
		if(StringUtil.isNotEmpty(template.getParentPath())){
			sql.append("," + template.getParentPath());
		}
		//链接
		if(StringUtil.isNotEmpty(template.getHref())){
			sql.append("," + template.getHref());
		}
		//链接目标
		if(StringUtil.isNotEmpty(template.getHrefTarget())){
			sql.append("," + template.getHrefTarget());
		}
		//描述
		if(StringUtil.isNotEmpty(template.getDescription())){
			sql.append("," + template.getDescription());
		}
		if(StringUtil.isNotEmpty(template.getOrderIndex())){
			sql.append("," + template.getOrderIndex());
		}
		if(StringUtil.isNotEmpty(template.getTreeOrderIndex())){
			sql.append("," + template.getTreeOrderIndex());
		}
		if(StringUtil.isNotEmpty(template.getFieldCodes())){
			sql.append(template.getFieldCodes());
		}
		sql.append(" FROM " + tableName + " t ");
		sql.append(" where 1=1 ");
		if(null != queryInfo) {
			sql.append(queryInfo.getWhereSql());
		}
		if(!onlyWhereSql){
			if(isRoot){
				sql.append(" AND ("+template.getParent()+"='"+rootId+"' OR "+template.getId()+"='"+rootId+"')");
			}else{
				sql.append("AND "+template.getParent()+"='"+rootId+"'");
			}
		}
//		sql.append(") OR "+template.getId()+"='"+rootId+"' ");
		if(null != queryInfo && StringUtil.isNotEmpty(queryInfo.getOrderSql())){
			sql.append(queryInfo.getOrderSql());
		}else {
			sql.append(" ORDER BY " + template.getParent() + " asc");
			if (StringUtil.isNotEmpty(template.getOrderIndex())) {
				sql.append(", " + template.getOrderIndex() + " asc");
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
		sql.append("SELECT count(" + template.getId()+""); // 0
		sql.append(") FROM " + tableName + " ");
		sql.append(" where 1=1 ");
		if(null != queryInfo) {
			sql.append(queryInfo.getWhereSql());
		}
		if(StringUtil.isNotEmpty(rootId)) {
			sql.append(" and " + template.getParent() + " = '" + rootId + "'");
		} else {
			sql.append(" and " + template.getParent() + " is null ");
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
		StringBuffer filedSql=new StringBuffer();
		for(DynaBean column:columns){
			filedSql.append(" "+column.getStr("TABLECOLUMN_CODE")+",");
		}
		filedSql.deleteCharAt(filedSql.length()-1);
		StringBuffer sql=new StringBuffer();
		sql.append(" select "+filedSql.toString()+" from "+tableName+" where 1=1 and ("+template.getNodePath()+" like '%"+rootId+"%'");
		if(null != queryInfo) {
			sql.append(queryInfo.getWhereSql());
		}
		sql.append(") OR "+template.getId()+" = '"+rootId+"' ");
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
	 * 获取角色权限查询sql
	 * @param rootId 根节点
	 * @return
	 */
	@Override
	public String getRolePermSql(String rootId) {
		// TODO Auto-generated method stub
		//PCServiceTemplate pcServiceTemplate=SpringContextHolder.getBean("PCServiceTemplateImpl");
//		pcServiceTemplate.executeSql(" call p_opt_recursion('JE_CORE_ROLE','ROLEID','PARENT','"+rootId+"','xxx_table')");
		StringBuffer filedSql=new StringBuffer();
		filedSql.append("ROLEID,ROLENAME,ROLECODE,GROUPNAME,GROUPCODE,EXTENDGROUPNAME,EXTENDGROUPCODE,REJECTGROUPNAME,REJECTGROUPCODE,PATH,PARENT,ORDERINDEX");
		StringBuffer sql=new StringBuffer();
		sql.append(" select "+filedSql.toString()+" from JE_CORE_ROLE where 1=1 and (PATH LIKE '%"+rootId+"%' OR ROLEID='"+rootId+"')");
		sql.append(" ORDER BY PARENT ASC,ORDERINDEX ASC");
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
		StringBuffer filedSql=new StringBuffer();
		filedSql.append("ROLEID,ROLENAME,ROLECODE,GROUPNAME,GROUPCODE,EXTENDGROUPNAME,EXTENDGROUPCODE,REJECTGROUPNAME,REJECTGROUPCODE,PATH,PARENT,ORDERINDEX");
		StringBuffer sql=new StringBuffer();
		sql.append(" select "+filedSql.toString()+" from JE_CORE_ROLE where 1=1 and (PATH LIKE '%"+rootId+"%' OR ROLEID='"+rootId+"')");
		sql.append(" ORDER BY PARENT ASC,ORDERINDEX ASC");
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
//		PCServiceTemplate pcServiceTemplate=SpringContextHolder.getBean("PCServiceTemplateImpl");
//		pcServiceTemplate.executeSql(" call p_opt_recursion('JE_CORE_ROLEGROUP','JE_CORE_ROLEGROUP_ID','SY_PARENT','"+rootId+"','xxx_table')");
		StringBuffer filedSql=new StringBuffer();
		filedSql.append("JE_CORE_ROLEGROUP_ID,ROLEGROUP_TEXT,ROLEGROUP_CODE,SY_PATH,SY_PARENT,SY_ORDERINDEX");
		StringBuffer sql=new StringBuffer();
		sql.append(" select "+filedSql.toString()+" from JE_CORE_ROLEGROUP where 1=1 and (SY_PATH LIKE '%"+rootId+"%' OR JE_CORE_ROLEGROUP_ID ='"+rootId+"')");
		sql.append(" ORDER BY SY_PARENT ASC,SY_PARENT ASC");
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
		StringBuffer filedSql=new StringBuffer();
		filedSql.append("JE_CORE_ROLEGROUP_ID,ROLEGROUP_TEXT,ROLEGROUP_CODE,SY_PATH,SY_PARENT,SY_ORDERINDEX");
		StringBuffer sql=new StringBuffer();
		sql.append(" select "+filedSql.toString()+" from JE_CORE_ROLEGROUP where 1=1 and (SY_PATH LIKE '%"+rootId+"%' OR JE_CORE_ROLEGROUP_ID ='"+rootId+"')");
		sql.append(" ORDER BY SY_PARENT ASC,SY_PARENT ASC");
		return sql.toString();
	}

	/**
	 * 获取数据生成UUID函数
	 * @return
	 */
	@Override
	public String getGenerateUUID() {
		// TODO Auto-generated method stub
		return "uuid()";
	}

	/**
	 * 获取截取字符串函数
	 * @return
	 */
	@Override
	public String getSubString() {
		// TODO Auto-generated method stub
		return "substring";
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

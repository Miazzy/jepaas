package com.je.core.service;

import java.util.List;

import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.DynaBean;

/**
 * TODO 暂不明确
 */
public class PcDBMethodManagerImplBySqlServer implements PcDBMethodManager {
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
		filedSql.append(""+template.getId()+","+template.getCode()+","+template.getText()+","+template.getParent()+"");
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
		sql.append("with ctr_child("+filedSql.toString()+") as (");
		sql.append(" select "+filedSql.toString()+" from "+tableName+" where 1=1 and ["+template.getId()+"] = '"+rootId+"'");
		String childSql=filedSql.toString();
		childSql="a."+childSql;
		childSql=childSql.replace(",", ",a.");
		sql.append(" union all select "+childSql+ " from "+tableName+" a inner join ctr_child b on");
		sql.append(" (a."+template.getParent()+"=b."+template.getId()+") )");
		sql.append(" select * from ctr_child where (1=1 ");
		if(null != queryInfo) {
			sql.append(queryInfo.getWhereSql());
		}
		sql.append(") or "+template.getId()+" = '"+rootId+"' ");
		if(null != queryInfo && StringUtil.isNotEmpty(queryInfo.getOrderSql())){
			sql.append(queryInfo.getOrderSql());
		}else{
			sql.append(" ORDER BY "+template.getParent()+" asc");
			if(StringUtil.isNotEmpty(template.getOrderIndex())){
				sql.append(", "+template.getOrderIndex()+" asc ");
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
		StringBuffer filedSql=new StringBuffer();
		filedSql.append(""+template.getId()+","+template.getCode()+","+template.getText()+","+template.getParent()+"");
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
//		sql.append("with ctr_child("+filedSql.toString()+") as (");
//		sql.append(" select "+filedSql.toString()+" from "+tableName+" where 1=1 and ["+template.getId()+"] = '"+rootId+"'");
//		String childSql=filedSql.toString();
//		childSql="a."+childSql;
//		childSql=childSql.replace(",", ",a.");
//		sql.append(" select "+filedSql+ " from "+tableName+" a inner join ctr_child b on");
//		sql.append(" (a."+template.getParent()+"=b."+template.getId()+") )");
		sql.append(" select "+filedSql+ " from "+tableName+" where 1=1 ");
		if(!onlyWhereSql){
			if(isRoot){
				sql.append(" AND ("+template.getParent()+"='"+rootId+"' OR "+template.getId()+"='"+rootId+"')");
			}else{
				sql.append("AND "+template.getParent()+"='"+rootId+"'");
			}
		}
		if(null != queryInfo && StringUtil.isNotEmpty(queryInfo.getWhereSql())) {
			sql.append(queryInfo.getWhereSql());
		}
		if(null != queryInfo && StringUtil.isNotEmpty(queryInfo.getOrderSql())){
			sql.append(queryInfo.getOrderSql());
		}else{
			sql.append(" ORDER BY "+template.getParent()+" asc");
			if(StringUtil.isNotEmpty(template.getOrderIndex())){
				sql.append(", "+template.getOrderIndex()+" asc ");
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
		sql.append("SELECT count([" + template.getId()+"]"); // 0
		sql.append(") FROM " + tableName + " ");
		sql.append(" where 1=1 ");
		if(null != queryInfo) {
			sql.append(queryInfo.getWhereSql());
		}
		if(StringUtil.isNotEmpty(rootId)) {
			sql.append(" and [" + template.getParent() + "] = '" + rootId + "'");
		} else {
			sql.append(" and [" + template.getParent() + "] is null ");
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
			filedSql.append(" ["+column.getStr("TABLECOLUMN_CODE")+"],");
		}
		filedSql.deleteCharAt(filedSql.length()-1);
		StringBuffer sql=new StringBuffer();
		sql.append("with ctr_child("+filedSql.toString()+") as (");
		sql.append(" select "+filedSql.toString()+" from "+tableName+" where 1=1 and ["+template.getId()+"] = '"+rootId+"'");
		String childSql=filedSql.toString();
		childSql="a."+childSql;
		childSql=childSql.replace(",", ",a.");
		sql.append(" union all select "+childSql+ " from "+tableName+" a inner join ctr_child b on");
		sql.append(" (a.["+template.getParent()+"]=b.["+template.getId()+"]) )");
		sql.append(" select * from ctr_child where (1=1 ");
		if(null != queryInfo) {
			sql.append(queryInfo.getWhereSql());
		}
		sql.append(") or "+template.getId()+" = '"+rootId+"' ");
		if(null != queryInfo && StringUtil.isNotEmpty(queryInfo.getOrderSql())){
			sql.append(queryInfo.getOrderSql());
		}else{
			sql.append("ORDER BY ["+template.getParent()+"] ASC, [SY_ORDERINDEX] ASC");
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
		StringBuffer filedSql=new StringBuffer();
		filedSql.append("[ROLEID],[ROLENAME],[ROLECODE],[GROUPNAME],[GROUPCODE],[EXTENDGROUPNAME],[EXTENDGROUPCODE],[REJECTGROUPNAME],[REJECTGROUPCODE],[PATH],[PARENT],[ORDERINDEX]");
		StringBuffer sql=new StringBuffer();
		sql.append("with ctr_child("+filedSql.toString()+") as (");
		sql.append(" select "+filedSql.toString()+" from JE_CORE_ROLE where 1=1 and [ROLEID] = '"+rootId+"'");
		String childSql=filedSql.toString();
		childSql="a."+childSql;
		childSql=childSql.replace(",", ",a.");
		sql.append(" union all select "+childSql+ " from JE_CORE_ROLE a inner join ctr_child b on");
		sql.append(" (a.[PARENT]=b.[ROLEID]) )");
		sql.append(" select * from ctr_child where (1=1 ");
		sql.append(") or [ROLEID] = '"+rootId+"' ");
		sql.append(" ORDER BY [PARENT] ASC,[ORDERINDEX] ASC");
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
		filedSql.append("[ROLEID],[ROLENAME],[ROLECODE],[GROUPNAME],[GROUPCODE],[EXTENDGROUPNAME],[EXTENDGROUPCODE],[REJECTGROUPNAME],[REJECTGROUPCODE],[PATH],[PARENT],[ORDERINDEX]");
		StringBuffer sql=new StringBuffer();
//		sql.append("with ctr_child("+filedSql.toString()+") as (");
//		sql.append(" select "+filedSql.toString()+" from JE_CORE_ROLE where 1=1 and [ROLEID] = '"+rootId+"'");
//		String childSql=filedSql.toString();
//		childSql="a."+childSql;
//		childSql=childSql.replace(",", ",a.");
//		sql.append(" union all select "+childSql+ " from JE_CORE_ROLE a inner join ctr_child b on");
//		sql.append(" (a.[PARENT]=b.[ROLEID]) )");
		sql.append(" select "+filedSql+" from JE_CORE_ROLE where (1=1 ");
		sql.append(" AND [PATH] LIKE '%"+rootId+"%' )");
		sql.append(" ORDER BY [PARENT] ASC,[ORDERINDEX] ASC");
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
		StringBuffer filedSql=new StringBuffer();
		filedSql.append("[JE_CORE_ROLEGROUP_ID],[ROLEGROUP_TEXT],[ROLEGROUP_CODE],[SY_PATH],[SY_PARENT],[SY_ORDERINDEX]");
		StringBuffer sql=new StringBuffer();
		sql.append("with ctr_child("+filedSql.toString()+") as (");
		sql.append(" select "+filedSql.toString()+" from JE_CORE_ROLEGROUP where 1=1 and [JE_CORE_ROLEGROUP_ID] = '"+rootId+"'");
		String childSql=filedSql.toString();
		childSql="a."+childSql;
		childSql=childSql.replace(",", ",a.");
		sql.append(" union all select "+childSql+ " from JE_CORE_ROLEGROUP a inner join ctr_child b on");
		sql.append(" (a.[SY_PARENT]=b.[JE_CORE_ROLEGROUP_ID]) )");
		sql.append(" select * from ctr_child where (1=1 ");
		sql.append(") or [JE_CORE_ROLEGROUP_ID] = '"+rootId+"' ");
		sql.append(" ORDER BY [SY_PARENT] ASC,[SY_ORDERINDEX] ASC");
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
		filedSql.append("[JE_CORE_ROLEGROUP_ID],[ROLEGROUP_TEXT],[ROLEGROUP_CODE],[SY_PATH],[SY_PARENT],[SY_ORDERINDEX]");
		StringBuffer sql=new StringBuffer();
//		sql.append("with ctr_child("+filedSql.toString()+") as (");
//		sql.append(" select "+filedSql.toString()+" from JE_CORE_ROLEGROUP where 1=1 and [JE_CORE_ROLEGROUP_ID] = '"+rootId+"'");		
//		String childSql=filedSql.toString();
//		childSql="a."+childSql;
//		childSql=childSql.replace(",", ",a.");
//		sql.append(" union all select "+childSql+ " from JE_CORE_ROLEGROUP a inner join ctr_child b on");
//		sql.append(" (a.[SY_PARENT]=b.[JE_CORE_ROLEGROUP_ID]) )");
		sql.append(" select "+filedSql+" from JE_CORE_ROLEGROUP where (1=1 ");
		sql.append(" AND [SY_PATH] LIKE '%"+rootId+"%') ");
		sql.append(" ORDER BY [SY_PARENT] ASC,[SY_ORDERINDEX] ASC");
		return sql.toString();
	}

	/**
	 * 获取数据生成UUID函数
	 * @return
	 */
	@Override
	public String getGenerateUUID() {
		// TODO Auto-generated method stub
		return "NewId()";
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
		return "len";
	}

	/**
	 * 获取数据库修改视图语句
	 * @return
	 */
	@Override
	public String getUpdateView() {
		// TODO Auto-generated method stub
		return "ALTER VIEW ";
	}

}

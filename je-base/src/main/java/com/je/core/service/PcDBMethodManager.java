package com.je.core.service;

import java.util.List;

import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;

/**
 * TODO 暂不明确
 */
public interface PcDBMethodManager {
	/**
	 * 构建查询同步树的sql语句  PCServiceTemplateImpl
	 * @param template TODO 暂不明确
	 * @param queryInfo TODO 暂不明确
	 * @param tableName 表名称
	 * @param rootId 根节点id
	 * @return
	 */
	public String getTreeSql(JSONTreeNode template, QueryInfo queryInfo, String tableName, String rootId);
	/**
	 * 构建DynaBean查询同步树的sql语句
	 * @param template TODO 暂不明确
	 * @param queryInfo TODO 暂不明确
	 * @param tableName 表名称
	 * @param rootId 根节点id
	 * @return
	 */
	public String getDynaTreeSql(List<DynaBean> columns, JSONTreeNode template, String tableName, String rootId, QueryInfo queryInfo);

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
	public String getAsynTreeSql(JSONTreeNode template, QueryInfo queryInfo, String tableName, String rootId, Boolean isRoot, Boolean onlyWhereSql);
	/**
	 * 查询树形的count
	 * @param template TODO 暂不明确
	 * @param queryInfo TODO 暂不明确
	 * @param tableName 表名称
	 * @param rootId 根节点id
	 * @return
	 */
	public String getAsynTreeCount(JSONTreeNode template, QueryInfo queryInfo, String tableName, String rootId);
	/**
	 * 获取角色权限查询sql
	 * @param rootId 根节点
	 * @return
	 */
	public String getRolePermLikeSql(String rootId);
	/**
	 * 获取角色权限查询sql
	 * @param rootId 根节点
	 * @return
	 */
	public String getRolePermSql(String rootId);
	/**
	 * 获取角色组查询SQL
	 * @param rootId  根节点
	 * @return
	 */
	public String getRoleGroupPermLikeSql(String rootId);
	/**
	 * 获取角色组查询SQL
	 * @param rootId 根节点
	 * @return
	 */
	public String getRoleGroupPermSql(String rootId);
	/**
	 * 获取数据生成UUID函数
	 * @return
	 */
	public String getGenerateUUID();
	/**
	 *    获取截取字符串函数
	 * @return
	 */
	public String getSubString();
	/**
	 *  获取字符串长度函数
	 * @return
	 */
	public String getLength();
	/**
	 * 获取数据库修改视图语句
	 * @return
	 */
	public String getUpdateView();
}

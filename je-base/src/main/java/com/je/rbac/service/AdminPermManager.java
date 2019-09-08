package com.je.rbac.service;

import java.util.List;
import java.util.Set;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;

public interface AdminPermManager {
	/**
	 * 获取权限树
	 */
	public JSONTreeNode getPermTree();
	/**
	 * 获取管理员权限
	 * @param adminId 管理员id
	 * @return
	 */
	public List<DynaBean> getAdminPermRes(String adminId);
	/**
	 * 更新权限
	 * @param adminInfo 管理员信息
	 * @param delPermIds 删除人员id
	 * @param addPermIds 添加人员id
	 */
	public void doUpdatePerms(DynaBean adminInfo, String delPermIds, String addPermIds);
	/**
	 * 导入人员
	 * @param ids 多个id
	 * @param pkValue 主键
	 * @return
	 */
	public Integer implUsers(String ids, String pkValue);
	/**
	 * 移除人员
	 * @param ids 多个id
	 * @param pkValue 主键
	 * @return
	 */
	public Integer delUsers(String ids, String pkValue);
	/**
	 * 获取用户可授权权限
	 * @param userId 用户id
	 * @param whereSql 查询条件sql
	 * @return
	 */
	public Set<String> getUserPerms(String userId, String whereSql);
	/**
	 * 获取角色权限
	 * @param userId 用户id
	 * @param whereSql 查询条件sql
	 * @return
	 */
	public Set<String> getUserRolePerms(String userId, String whereSql);
	/**
	 * 获取用户可授权权限
	 * @param userId 用户id
	 * @param whereSql 查询条件id
	 * @return
	 */
	public Set<String> getUserDeptPerms(String userId, String whereSql);
	/**
	 * 获取用户可授权权限
	 * @param userId 用户id
	 * @param whereSql 查询条件id
	 * @return
	 */
	public Set<String> getUserSentryPerms(String userId, String whereSql);
	/**
	 * 添加子的权限
	 * @param currentUser 当前登陆人信息
	 * @param parentId TODO 暂不明确
	 * @param type 类型
	 */
	public void addChildrenPerm(EndUser currentUser, String id, String parentId, String type);
	/**初始化管理员权限*/
	public void initAdminPerm(EndUser currentUser);
	/**
	 * 得到当前登录人可看的权限
	 * @param tableCode
	 */
	public String getQuerySql(String tableCode);
}

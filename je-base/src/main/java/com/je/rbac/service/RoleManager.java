package com.je.rbac.service;

import com.je.core.util.bean.DynaBean;

public interface RoleManager {
	/**
	 * 修改
	 * @param dynaBean 自定义动态类
	 * @param isHard TODO 暂不明确
	 * @return
	 */
	public DynaBean doUpdate(DynaBean dynaBean, Boolean isHard);

	/**
	 * 删除
	 * @param ids 操作id
	 * @param noSaas TODO 暂不明确
	 * @return
	 */
	public Integer doRemove(String ids, Boolean noSaas);
	/**
	 * 导入人员
	 * @param userIds 用户id
	 * @param roleId 角色id
	 * @return
	 */
	public Integer implUsers(String userIds, String roleId);
	/**
	 * 删除用户
	 * @param userIds 用户id
	 * @param roleId 角色id
	 * @return
	 */
	public Integer removeUsers(String userIds, String roleId);
	/**
	 * 角色移动
	 * @param dynaBean 自定义动态类
	 * @return
	 */
	public DynaBean roleMove(DynaBean dynaBean);
	/**
	 * 生成树形排序编号
	 * @param dynaBean 自定义动态类
	 */
	public void generateTreeOrderIndex(DynaBean dynaBean);
	/**
	 * 同步人员角色信息
	 * @param userIds 用户id
	 */
	public void syncUserRoleInfo(String userIds);
}

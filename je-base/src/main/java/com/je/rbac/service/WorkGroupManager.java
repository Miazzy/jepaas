package com.je.rbac.service;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.dd.vo.DicInfoVo;

/**
 * TODO 暂不明确
 */
public interface WorkGroupManager {
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public Integer doRemove(String ids);

	/**
	 * 导入人员
	 * @param userIds 用户id
	 * @param workGroupId TODO 暂不明确
	 */
	public void implUsers(String userIds, String workGroupId) ;

	/**
	 * 移除人员
	 * @param userIds 用户id
	 * @param workGroupId TODO 暂不明确
	 */
	public void removeUsers(String userIds, String workGroupId);
	/**
	 * 获取当前工作组树形
	 * @param dicInfoVo TODO 暂不明确
	 * @return
	 */
	public JSONTreeNode getWorkGroupUser(DicInfoVo dicInfoVo);
}

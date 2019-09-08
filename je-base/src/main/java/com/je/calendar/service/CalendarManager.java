package com.je.calendar.service;

import java.util.List;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;
/**
 * 日历接口
 * @author zhangshuaipeng
 * @bean 注入名称： calendarManager
 */
public interface CalendarManager {
	/**
	 * 获取我的任务树形
	 * @param currentUser
	 * @return
	 */
	public JSONTreeNode getMyTaskTree(EndUser currentUser);
	/**
	 * 获取组树形
	 * @param currentUser
	 * @return
	 */
	public JSONTreeNode getGroupTree(EndUser currentUser);
	/**
	 * 获取共享任务树形
	 * @param currentUser
	 * @return
	 */
	public JSONTreeNode getShareTaskTree(EndUser currentUser);
	/**
	 * 导入组人员
	 * @param groupId
	 * @param ids
	 * @return
	 */
	public List<DynaBean> addGroupUsers(String groupId, String ids);
}

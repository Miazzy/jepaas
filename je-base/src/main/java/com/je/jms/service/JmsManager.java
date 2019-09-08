package com.je.jms.service;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.jms.vo.JmsChannel;

public interface JmsManager {
	/**
	 * 发送消息
	 * @param channelId 通道ID
	 * @param context   内容
	 * @param moreUser  通道类型
	 */
	public void sendMsg(String channelId, String context, String type);
	/**
	 * 读取消息
	 * @param historyIds 消息ID
	 * @param type 消息类型  GROUP || USER
	 */
	public void readMsg(String historyIds, String type);
	/**
	 * 获取通道
	 * @param type 通道类型
	 * @param groupId	分组ID
	 * @param groupName 分组名称
	 * @param ids 主键
	 */
	public JmsChannel getChannel(String type, String groupId, String groupName, String ids);
	/**
	 * 保存栈部分信息  方便查询历史记录和未读信息
	 * @param userId 用户ID
	 * @param channelId
	 */
	public void saveStack(String userId, String channelId);
	/**
	 * 获取讨论组
	 */
	public JSONTreeNode getGroupTree();
	/**
	 * 获取最近联系人
	 * @param limit  分页条数
	 */
	public JSONTreeNode getRelationTree(Integer limit);
	/**
	 * 更换讨论组名称
	 * @param groupId 分组ID
	 * @param groupName 分组名称
	 */
	public void updateGroup(String groupId, String groupName);
	/**
	 * 删除讨论组
	 * @param groupId 分组ID
	 */
	public void removeGroup(String groupId);
	/**
	 * 更新讨论组人员
	 * @param groupId 分组ID
	 * @param userIds 用户ID集合
	 * @param oper TODO未处理
	 */
	public void updateGroupUser(String groupId, String userIds, String oper);
}

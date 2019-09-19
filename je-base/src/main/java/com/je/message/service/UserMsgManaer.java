package com.je.message.service;

import com.je.core.util.bean.DynaBean;
import com.je.message.vo.app.UserMsgAppInfo;

import java.util.List;

public interface UserMsgManaer {
	/**
	 * 发送消息
	 * @param userId 用户ID
	 * @param title 主题
	 * @param type 类型
	 */
//	public DynaBean sendMsg(String userId, String title, String content, String type);
	/**
	 * 发送消息
	 * @param userId 用户ID
	 * @param title 主题
	 * @param type 类型
	 */
	public DynaBean sendMsg(String userId, String title, String content, String type, String pkValue, List<UserMsgAppInfo> userMsgAppInfos);
	/**
	 * 发送消息 (该方法节省性能，不需要查询各个信息)
	 * @param username 用户名称
	 * @param userId 用户ID
	 * @param title 主题
	 * @param content 内容
	 * @param type 类型
	 * @param typeName 类型名称
	 */
	public DynaBean sendMsg(String username, String userId, String title, String content, String type, String typeName,String pkValue, List<UserMsgAppInfo> userMsgAppInfos);


}

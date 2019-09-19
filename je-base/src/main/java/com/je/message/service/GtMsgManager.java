package com.je.message.service;

import com.je.message.vo.app.PayloadInfo;
/**
 * 个推推送消息
 * @author zhangshuaipeng
 *
 */
public interface GtMsgManager {

	/**
	 * 个推发送消息
	 * @param userIds 用户ID
	 * @param title 主题
	 * @param content 内容
	 * @param onlySocket TODO未处理
	 * @param params 传入信息
	 */
	void sendMoreMsg(String userIds, String title, String content, String onlySocket, PayloadInfo params);
}

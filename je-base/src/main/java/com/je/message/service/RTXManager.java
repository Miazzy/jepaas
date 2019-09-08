package com.je.message.service;

import com.je.message.vo.RTXMsgVo;

/**
 * 腾讯通RTX  服务
 * @author zhangshuaipeng
 *
 */
public interface RTXManager {
	/**
	 * 发送消息
	 * @param msgVo 消息
	 */
	public void send(RTXMsgVo msgVo);
	/**
	 * 发送消息
	 * @param msgVo 消息
	 */
	public void send(RTXMsgVo msgVo, String jtgsId);
	/**
	 * 发送简易消息
	 * @param receiveUser RTX帐号
	 * @param title 标题
	 * @param context 内容
	 */
	public void send(String receiveUser, String title, String context);
	/**
	 * 发送消息
	 * @param receiveUser RTX帐号
	 * @param title 标题
	 * @param context 内容
	 * @param fastType 紧急状态  1 表示紧急  0表示普通。。 默认为0
	 * @param fromUserName 发送人
	 * @param fromDeptName 发送部门
	 * @param delayTime  停留 时间   默认 0
	 */
	public void send(String receiveUser, String title, String context, String fastType, String fromUserName, String fromDeptName, Long delayTime);
}

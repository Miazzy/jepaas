package com.je.message.service;

import java.util.Map;

import com.je.core.util.bean.DynaBean;
import com.je.message.vo.NoteMsgVo;

/**
 * 短信消息    服务
 * @author zhangshuaipeng
 *
 */
public interface NoteManager {
	/**
	 * 发送短信
	 * @param msgVo 消息VO实例
	 */
	public void send(NoteMsgVo msgVo);
	/**
	 * 发送短信
	 * @param msgVo 消息VO实例
	 */
	public void send(NoteMsgVo msgVo, String jtgsId);
	/**
	 * 发送短信
	 * @param phoneNumber  手机号
	 * @param fromUser     发送人
	 * @param fromUserId	TODO未处理
	 * @param toUser	     接收人
	 * @param toUserId		TODO未处理
	 * @param context      内容
	 */
	public void send(String phoneNumber, String fromUser, String fromUserId, String toUser, String toUserId, String context);
	/**
	 * 发送短信
	 * @param phoneNumber  手机号
	 * @param toUser       接收人
	 * @param toUserId		TODO未处理
	 * @param context	   内容
	 */
	public void send(String phoneNumber, String toUser, String toUserId, String context);
	/**
	 * 发送短信
	 * @param phoneNumber   手机号
	 * @param context       内容
	 */
	public void send(String phoneNumber, String context);

	/**
	 * 数据模版
	 * @param phoneNumber  电话
	 * @param tempCode TODO未处理
	 * @param params	传入信息
	 */
	public void sendTemplate(String phoneNumber, String tempCode, Map params);
	/**
	 * 同步发送短信
	 * @param msgVo
	 */
	public DynaBean sendSync(NoteMsgVo msgVo);
	/**
	 * 找到指定用户的短信剩余条数
	 * @param jtgsId
	 * @return
	 */
	public int checkNoteNum(String jtgsId);
	/**
	 * 检查所有租户的短信信息剩余条数当小于10条则提醒管理员续费
	 */
	public void doSaasNoteMsg();
}

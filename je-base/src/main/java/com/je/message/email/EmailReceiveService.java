package com.je.message.email;

import net.sf.json.JSONObject;

import com.je.message.vo.EmailInfoVo;
	
public interface EmailReceiveService {
	/**
	 * 接收指定单个邮箱索引的邮件
	 * @param emailBaseInfo 邮箱基本信息
	 * @return 共接收多少封邮件
	 */
	public void receive(EmailInfoVo emailInfoVo, JSONObject returnObj, String type, Boolean dwrFlag);
	/**
	 * 负载均衡静态变量同步     格式  type#pkValue#value
	 * @param val TODO未处理
	 */
	public void doSyncRunEmail(String val);
}

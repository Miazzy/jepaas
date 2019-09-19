package com.je.jms.util;


import com.je.message.vo.DwrMsgVo;

import com.je.jms.vo.JmsGroupInfo;
import com.je.message.util.DwrUtil;
import com.je.core.facade.extjs.JsonBuilder;
/**
 * 推送消息线程
 * @author zhangshuaipeng
 *
 */
public class JmsGroupTread extends Thread  {
	private JmsGroupInfo jmsGroupInfo;
	private String userId;
	
	public JmsGroupTread(String userId,JmsGroupInfo jmsGroupInfo) {
		super();
		this.jmsGroupInfo = jmsGroupInfo;
		this.userId = userId;
	}

	/**
	 * 线程启动
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DwrMsgVo msgVo=new DwrMsgVo(this.userId,"",JsonBuilder.getInstance().toJson(jmsGroupInfo));
		msgVo.setCallFunction("IMUtil.doGroupDwr");
		DwrUtil.sendMsg(msgVo);
	}
	
	public JmsGroupInfo getJmsGroupInfo() {
		return jmsGroupInfo;
	}
	public void setJmsGroupInfo(JmsGroupInfo jmsGroupInfo) {
		this.jmsGroupInfo = jmsGroupInfo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}

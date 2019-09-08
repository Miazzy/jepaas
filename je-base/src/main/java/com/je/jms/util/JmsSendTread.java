package com.je.jms.util;

import java.util.HashMap;

import com.je.message.vo.DwrMsgVo;

import com.je.jms.vo.JmsMsgVo;
import com.je.message.util.DwrUtil;
import com.je.core.facade.extjs.JsonBuilder;
/**
 * 推送消息线程
 * @author zhangshuaipeng
 *
 */
public class JmsSendTread extends Thread  {
	private JmsMsgVo jmsMsgVo;
	private String userId;
	
	public JmsSendTread(String userId,JmsMsgVo jmsMsgVo) {
		super();
		this.jmsMsgVo = jmsMsgVo;
		this.userId = userId;
	}

	/**
	 * 线程启动
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DwrMsgVo msgVo=new DwrMsgVo(userId,JsonBuilder.getInstance().toJson(jmsMsgVo),"","IMUtil.pushMsg","","",false,new HashMap<String, Object>());
		DwrUtil.sendMsg(msgVo);
	}
	public JmsMsgVo getJmsMsgVo() {
		return jmsMsgVo;
	}
	public void setJmsMsgVo(JmsMsgVo jmsMsgVo) {
		this.jmsMsgVo = jmsMsgVo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}

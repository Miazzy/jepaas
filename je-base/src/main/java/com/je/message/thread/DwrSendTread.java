package com.je.message.thread;

import com.je.message.util.DwrUtil;
import com.je.message.vo.DwrMsgVo;
/**
 * DWR推送发送线程
 * @author zhangshuaipeng
 *
 */
public class DwrSendTread  extends Thread {
	private DwrMsgVo msgVo;//消息
	private String userId;//用户ID
	private DwrSendTread(){};
	public DwrSendTread(DwrMsgVo msgVo) {
		this();
		this.userId=msgVo.getUserId();
		this.msgVo = msgVo;
	}
	@Override
	public void run() {
		if(msgVo==null)return;
		DwrUtil.sendMsg(msgVo);
	}
	public DwrMsgVo getMsgVo() {
		return msgVo;
	}

	public void setMsgVo(DwrMsgVo msgVo) {
		this.msgVo = msgVo;
	}
	
	
}

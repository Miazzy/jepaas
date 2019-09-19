package com.je.message.thread;

import java.util.Date;
import java.util.Map;

import rtx.RTXSvrApi;

import com.je.message.util.WxCpUtil;
import com.je.message.vo.RTXMsgVo;
import com.je.message.vo.WxMsgVo;

public class WxSendTread extends Thread {
	private WxMsgVo msgVo;//消息
	private WxSendTread(){}
	public WxSendTread(WxMsgVo msgVo){
		this();
		this.msgVo=msgVo;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(msgVo==null){
			return;
		}
		WxCpUtil.getInstance().sendWxMsg(msgVo);
	}

}

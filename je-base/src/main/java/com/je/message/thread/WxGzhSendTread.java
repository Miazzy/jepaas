package com.je.message.thread;

import com.je.message.util.WxMpUtil;
import com.je.message.vo.WxMsgVo;

public class WxGzhSendTread extends Thread {
	private WxMsgVo msgVo;//消息
	private WxGzhSendTread(){}
	public WxGzhSendTread(WxMsgVo msgVo){
		this();
		this.msgVo=msgVo;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(msgVo==null){
			return;
		}
		WxMpUtil.getInstance().sendWxMsg(msgVo);
	}

}

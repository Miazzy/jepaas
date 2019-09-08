package com.je.message.thread;

import com.je.message.util.DdCpUtil;
import com.je.message.util.WxCpUtil;
import com.je.message.vo.DdMsgVo;

public class DdSendTread extends Thread {
	private DdMsgVo msgVo;//消息
	private DdSendTread(){}
	public DdSendTread(DdMsgVo msgVo){
		this();
		this.msgVo=msgVo;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(msgVo==null){
			return;
		}
		DdCpUtil.getInstance().sendDdMsg(msgVo);
	}

}

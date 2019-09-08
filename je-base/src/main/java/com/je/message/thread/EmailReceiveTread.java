package com.je.message.thread;

import net.sf.json.JSONObject;

import com.je.core.util.SpringContextHolder;
import com.je.message.email.EmailReceiveService;
import com.je.message.vo.EmailInfoVo;

public class EmailReceiveTread extends Thread {
	private EmailInfoVo emailInfoVo;//TODO未处理
	private String type;//类型
	private EmailReceiveTread(){};
	public EmailReceiveTread(EmailInfoVo emailInfoVo,String type) {
		this();
		this.emailInfoVo=emailInfoVo;
		this.type=type;
	}
	@Override
	public void run() {
		EmailReceiveService emailReceiveService=SpringContextHolder.getBean("emailReceiveService");
		JSONObject returnObj=new JSONObject();	
		emailReceiveService.receive(emailInfoVo, returnObj, type, false);
	}
}

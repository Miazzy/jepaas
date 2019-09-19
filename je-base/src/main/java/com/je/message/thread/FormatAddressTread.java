package com.je.message.thread;

import java.util.ArrayList;
import java.util.HashMap;

import com.je.core.util.bean.DynaBean;
import com.je.message.service.EmailManager;

public class FormatAddressTread extends Thread{
	private HashMap<String,String> userAddress ;//用户地址
	private EmailManager emailManager ;//邮件消息
	private String EMAILDATA_FJR;//TODO未处理
	private ArrayList<DynaBean> list;//业务数据集合
	public FormatAddressTread(HashMap<String,String> userAddress,EmailManager emailManager, String EMAILDATA_FJR,ArrayList<DynaBean> list){
		super();
		this.userAddress = userAddress;
		this.emailManager = emailManager;
		this.EMAILDATA_FJR = EMAILDATA_FJR;
		this.list = list;
		
	}
	@Override
	public void run() {
		for(DynaBean email:list){
			email.set(EMAILDATA_FJR, emailManager.formatAddress(email.getStr(EMAILDATA_FJR), userAddress));
		}
	}
}

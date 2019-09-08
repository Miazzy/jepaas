package com.je.message.thread;

import com.je.message.util.AppMsgUtil;
import com.je.message.vo.app.PayloadInfo;

/**
 * 个推线程
 * @author zhangshuaipeng
 *
 */
public class AppSendTread extends Thread {
	private String userIds;//用户ID
	private String title;//主题
	private String content;//内容
	private String onlySocket;//TODO未处理
	private PayloadInfo params;//业务数据

	public AppSendTread(String userIds, String title, String content,String onlySocket, PayloadInfo params) {
		this.userIds = userIds;
		this.title = title;
		this.content = content;
		this.onlySocket=onlySocket;
		this.params = params;
	}

	@Override
	public void run() {
		AppMsgUtil.sendMsg(title,content,"USERIDS",userIds,onlySocket,params);
	}

}

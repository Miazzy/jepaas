package com.je.message.vo.app;
/**
 * 点击通知打开应用
 * @author zhangshuaipeng
 *
 */
public class InFormMsgVo {
	/**标题*/
	private String title;
	/**内容*/
	private String context;
	/**图标*/
	private String logo;
	/**是否响铃*/
	private Boolean ring=true;
	/**是否震动*/
	private Boolean vibrate=true;
	/**是否等待启动*/
	private Boolean waitStart=true;
	/**透视消息内容*/
	private String msgContext;
	
	public InFormMsgVo(String title, String context, String logo, Boolean ring,Boolean vibrate, Boolean waitStart, String msgContext) {
		super();
		this.title = title;
		this.context = context;
		this.logo = logo;
		this.ring = ring;
		this.vibrate = vibrate;
		this.waitStart = waitStart;
		this.msgContext = msgContext;
	}
	
	public InFormMsgVo(String title, String context, String logo,String msgContext) {
		super();
		this.title = title;
		this.context = context;
		this.logo = logo;
		this.msgContext = msgContext;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public Boolean getWaitStart() {
		return waitStart;
	}
	public void setWaitStart(Boolean waitStart) {
		this.waitStart = waitStart;
	}
	public String getMsgContext() {
		return msgContext;
	}
	public void setMsgContext(String msgContext) {
		this.msgContext = msgContext;
	}
	public Boolean getRing() {
		return ring;
	}
	public void setRing(Boolean ring) {
		this.ring = ring;
	}
	public Boolean getVibrate() {
		return vibrate;
	}
	public void setVibrate(Boolean vibrate) {
		this.vibrate = vibrate;
	}
	
}

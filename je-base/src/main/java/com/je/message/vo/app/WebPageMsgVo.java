package com.je.message.vo.app;

public class WebPageMsgVo {
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
	/**打开的url地址*/
	private String url;
	
	public WebPageMsgVo(String title, String context, String logo,Boolean ring, Boolean vibrate, String url) {
		super();
		this.title = title;
		this.context = context;
		this.logo = logo;
		this.ring = ring;
		this.vibrate = vibrate;
		this.url = url;
	}
	
	public WebPageMsgVo(String title, String context, String logo, String url) {
		super();
		this.title = title;
		this.context = context;
		this.logo = logo;
		this.url = url;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}

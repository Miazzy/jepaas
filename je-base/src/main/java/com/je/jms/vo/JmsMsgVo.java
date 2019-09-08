package com.je.jms.vo;

public class JmsMsgVo {
	/**通道ID*/
	private String channelId;
	/**通道类型*/
	private String channelType;
	/**发送内容*/
	private String context;
	/**记录ID*/
	private String id;
	/**发送时间*/
	private String sendTime;
	/**发送人ID*/
	private String sendUserId;
	/**发送人*/
	private String sendUserName;
	/**发送人头像*/
	private String sendUserPhoto;
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getSendUserId() {
		return sendUserId;
	}
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getSendUserName() {
		return sendUserName;
	}
	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}
	public String getSendUserPhoto() {
		return sendUserPhoto;
	}
	public void setSendUserPhoto(String sendUserPhoto) {
		this.sendUserPhoto = sendUserPhoto;
	}
	
}

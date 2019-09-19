package com.je.message.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.rbac.model.EndUser;

public class RTXMsgVo {
	/**
	 * 接受帐号
	 */
	private String rtxUser;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String context;
	/**
	 * 是否紧急
	 */
	private String fastType="0";
	/**
	 * 停留时间
	 */
	private Long delayTime=(long) 0;
	/**
	 * 发送人
	 */
	private String fromUserName;
	/**
	 * 发送人所在部门
	 */
	private String fromDeptName;
	/**
	 * 当前登录人
	 */
	private EndUser currentUser;
	/**
	 * 公司ID
	 */
	private String jtgsId;
	public RTXMsgVo(String rtxUser, String title, String context) {
		super();
		this.rtxUser = rtxUser;
		this.title = title;
		this.context = context;
	}
	
	public RTXMsgVo(String rtxUser, String title, String context,
			String fastType, String fromUserName, String fromDeptName,Long delayTime) {
		super();
		this.rtxUser = rtxUser;
		this.title = title;
		this.context = context;
		this.fastType = fastType;
		this.fromUserName = fromUserName;
		this.fromDeptName = fromDeptName;
		this.delayTime=delayTime;
	}

	public String getRtxUser() {
		return rtxUser;
	}
	public void setRtxUser(String rtxUser) {
		this.rtxUser = rtxUser;
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
	public String getFastType() {
		return fastType;
	}
	public void setFastType(String fastType) {
		this.fastType = fastType;
	}
	public Long getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(Long delayTime) {
		this.delayTime = delayTime;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getFromDeptName() {
		return fromDeptName;
	}
	public void setFromDeptName(String fromDeptName) {
		this.fromDeptName = fromDeptName;
	}

	public String getJtgsId() {
		return jtgsId;
	}

	public void setJtgsId(String jtgsId) {
		this.jtgsId = jtgsId;
	}
	@JsonIgnore
	public EndUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(EndUser currentUser) {
		this.currentUser = currentUser;
	}
	
	
}

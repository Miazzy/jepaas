package com.je.message.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.rbac.model.EndUser;

/**
 * 短信消息VO
 * @author zhangshuaipeng
 *
 */
public class NoteMsgVo {
	/**手机号*/
	private String phoneNumber;
	/**发送人*/
	private String fromUser;
	/**发送人ID*/
	private String fromUserId;
	/**接受人*/
	private String toUser;
	/**接受人ID*/
	private String toUserId;
	/**发送内容*/
	private String context;
	/**分类内容*/
	private String fl;
	/**
	 * 公司ID
	 */
	private String jtgsId;
	/**
	 * 租户ID
	 */
	private String zhId;
	/**
	 * 费用
	 */
	private String moneyZhId;
	/**
	 * 费用
	 */
	private String moneyJtgsId;
	/**
	 * 当前登录人
	 */
	private EndUser currentUser;

	public NoteMsgVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * 发送短信
	 * @param phoneNumber  手机号
	 * @param fromUser     发送人
	 * @param fromUserId
	 * @param toUser	          接收人
	 * @param toUserId
	 * @param context      内容
	 */
	public NoteMsgVo(String phoneNumber, String fromUser, String fromUserId,String toUser, String toUserId, String context) {
		super();
		this.phoneNumber = phoneNumber;
		this.fromUser = fromUser;
		this.fromUserId = fromUserId;
		this.toUser = toUser;
		this.toUserId = toUserId;
		this.context = context;
	}
	/**
	 * 发送短信
	 * @param phoneNumber  手机号
	 * @param toUser       接收人
	 * @param toUserId
	 * @param context	          内容
	 */
	public NoteMsgVo(String phoneNumber, String toUser, String toUserId,
					 String context) {
		super();
		this.phoneNumber = phoneNumber;
		this.toUser = toUser;
		this.toUserId = toUserId;
		this.context = context;
	}
	/**
	 * 发送短信
	 * @param phoneNumber   手机号
	 * @param context       内容
	 */
	public NoteMsgVo(String phoneNumber, String context) {
		super();
		this.phoneNumber = phoneNumber;
		this.context = context;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getFl() {
		return fl;
	}
	public void setFl(String fl) {
		this.fl = fl;
	}
	public String getJtgsId() {
		return jtgsId;
	}
	public void setJtgsId(String jtgsId) {
		this.jtgsId = jtgsId;
	}
	public String getMoneyJtgsId() {
		return moneyJtgsId;
	}
	public void setMoneyJtgsId(String moneyJtgsId) {
		this.moneyJtgsId = moneyJtgsId;
	}
	@JsonIgnore
	public EndUser getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(EndUser currentUser) {
		this.currentUser = currentUser;
	}
	public String getZhId() {
		return zhId;
	}
	public void setZhId(String zhId) {
		this.zhId = zhId;
	}
	public String getMoneyZhId() {
		return moneyZhId;
	}
	public void setMoneyZhId(String moneyZhId) {
		this.moneyZhId = moneyZhId;
	}


}

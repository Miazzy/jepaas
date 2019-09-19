package com.je.message.vo;

import java.util.Date;

/**
 * 邮箱收件的信息
 * @author zhangshuaipeng
 *
 */
public class EmailInfoVo {
	/**主键*/
	private String id;
	/**收件协议*/
	private String pop3;
	/**Imap协议*/
	private String imap;
	/**Imap端口*/
	private Integer imapPort;
	/**收件协议类型*/
	private String type;
	/**收件端口*/
	private Integer port;
	/**发件协议*/
	private String smtp;
	/**发件端口*/
	private Integer sendPort;
	/**邮箱地址*/
	private String address;
	/**邮箱密码*/
	private String password;
	/**用户名*/
	private String userName;
	/**用户编码*/
	private String userCode;
	/**用户ID*/
	private String userId;
	/**开始时间*/
	private Date startDate;
	/**结束时间*/
	private Date endDate;
	/**
	 * 发送验证
	 */
	private String sendAuth="1";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPop3() {
		return pop3;
	}
	public void setPop3(String pop3) {
		this.pop3 = pop3;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getSmtp() {
		return smtp;
	}
	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}
	public Integer getSendPort() {
		return sendPort;
	}
	public void setSendPort(Integer sendPort) {
		this.sendPort = sendPort;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getImap() {
		return imap;
	}
	public void setImap(String imap) {
		this.imap = imap;
	}
	public Integer getImapPort() {
		return imapPort;
	}
	public void setImapPort(Integer imapPort) {
		this.imapPort = imapPort;
	}
	public String getSendAuth() {
		return sendAuth;
	}
	public void setSendAuth(String sendAuth) {
		this.sendAuth = sendAuth;
	}
	
	
}

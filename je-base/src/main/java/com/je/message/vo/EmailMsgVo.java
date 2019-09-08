package com.je.message.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.core.constants.message.SendContextType;
import com.je.core.util.SecurityUserHolder;
import com.je.rbac.model.EndUser;

/**
 * Email信息
 * @author zhangshuaipeng
 *
 */
public class EmailMsgVo {
	/**
	 * 接受邮箱
	 */
	private String receiveEmail;
	/**
	 * 密送
	 */
	private String ms;
	/**
	 * 抄送
	 */
	private String cs;
	/**
	 * 分别发送
	 */
	private String fb;
	/**
	 * 主题
	 */
	private String subject;
	/**
	 * 内容类型   文本与HTML
	 */
	private String contextType=SendContextType.HTML;
	/**
	 * 内容
	 */
	private String context;
	/**
	 * 紧急状态     1.紧急     2.普通   3.缓慢
	 */
	private String faster;
	/**
	 * 需要回执   1 回执    0 不回执
	 */
	private String replySign;
	/**
	 * 文件名称
	 */
	private List<String> fileNames=new ArrayList<String>();
	/**
	 * 文件地址
	 */
	private List<String> addresses=new ArrayList<String>();
	/**
	 * 分类内容
	 */
	private String fl;
	/**
	 * 公司ID
	 */
	private String jtgsId;
	/**
	 * 当前用户
	 */
	private EndUser currentUser;
	/**
	 * 不带附件的邮件
	 * @param receiveEmail
	 * @param subject
	 * @param contextType
	 * @param context
	 */
	public EmailMsgVo(String receiveEmail, String subject, String contextType,
			String context) {
		super();
		this.receiveEmail = receiveEmail;
		this.subject = subject;
		this.contextType = contextType;
		this.context = context;
		this.currentUser=SecurityUserHolder.getCurrentUser();
	}
	/**
	 * 带有文件的邮件
	 * @param receiveEmail
	 * @param subject
	 * @param contextType
	 * @param context
	 * @param fileNames
	 * @param addresses
	 */
	public EmailMsgVo(String receiveEmail, String subject, String contextType,
			String context, List<String> fileNames, List<String> addresses) {
		super();
		this.receiveEmail = receiveEmail;
		this.subject = subject;
		this.contextType = contextType;
		this.context = context;
		this.fileNames = fileNames;
		this.addresses = addresses;
		this.currentUser=SecurityUserHolder.getCurrentUser();
	}

	public String getReceiveEmail() {
		return receiveEmail;
	}
	public void setReceiveEmail(String receiveEmail) {
		this.receiveEmail = receiveEmail;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContextType() {
		return contextType;
	}
	public void setContextType(String contextType) {
		this.contextType = contextType;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public List<String> getFileNames() {
		return fileNames;
	}
	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}
	public List<String> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}
	public String getMs() {
		return ms;
	}
	public void setMs(String ms) {
		this.ms = ms;
	}
	public String getCs() {
		return cs;
	}
	public void setCs(String cs) {
		this.cs = cs;
	}
	public String getFaster() {
		return faster;
	}
	public void setFaster(String faster) {
		this.faster = faster;
	}
	public String getReplySign() {
		return replySign;
	}
	public void setReplySign(String replySign) {
		this.replySign = replySign;
	}
	public String getFb() {
		return fb;
	}
	public void setFb(String fb) {
		this.fb = fb;
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
	@JsonIgnore
	public EndUser getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(EndUser currentUser) {
		this.currentUser = currentUser;
	}
	
}

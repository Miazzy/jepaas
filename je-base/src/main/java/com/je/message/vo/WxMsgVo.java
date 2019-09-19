package com.je.message.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.rbac.model.EndUser;

/**
 * 微信企业号消息内容
 * @author zhangshuaipeng
 *
 */
public class WxMsgVo {
	/**
	 * 接受用户 如果值为@all 则发送给全部的人
	 */
	private String userIds;
	/**
	 * 消息类型    文字：WZ  图文：TW 图片：TP 语音：YY 视频：SP 文件：WJ
	 */
	private String type;
	/**
	 * 图文类型  UEditor 编辑器内容 || HTML || URL 链接 
	 */
	private String twType;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 标题
	 */
	private String author;
	/**
	 * 作者
	 */
	private String remark;
	/**
	 * 内容
	 */
	private String context;
	/**
	 * 附件地址
	 */
	private String filePath;
	/**
	 * 集团公司ID
	 */
	private String jtgsId;
	/**链接*/
	private String url;
	/**原文链接地址*/
	private String sourceUrl;
	/**
	 * 当前登录人
	 */
	private EndUser currentUser;
	public WxMsgVo(){};
	public WxMsgVo(String userIds, String type,String context){
		this.userIds = userIds;
		this.type = type;
		this.context=context;
	}
	public WxMsgVo(String userIds, String type,String context,String filePath){
		this.userIds = userIds;
		this.type = type;
		this.context=context;
		this.filePath=filePath;
	}
	public WxMsgVo(String userIds, String type, String twType, String title,
			String author, String remark, String context, String filePath) {
		super();
		this.userIds = userIds;
		this.type = type;
		this.twType = twType;
		this.title = title;
		this.author = author;
		this.remark = remark;
		this.context = context;
		this.filePath = filePath;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getJtgsId() {
		return jtgsId;
	}
	public void setJtgsId(String jtgsId) {
		this.jtgsId = jtgsId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getTwType() {
		return twType;
	}
	public void setTwType(String twType) {
		this.twType = twType;
	}
	@JsonIgnore
	public EndUser getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(EndUser currentUser) {
		this.currentUser = currentUser;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}

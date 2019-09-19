package com.je.message.vo;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.rbac.model.EndUser;

import java.util.List;

/**
 * 微信企业号消息内容
 * @author zhangshuaipeng
 *
 */
public class DdMsgVo {
	/**
	 * 接收用户 如果值为@all 则发送给全部的人
	 */
	private String userIds;
	/**
	 * 接收部门
	 */
	private String deptIds;
	/**
	 * 消息类型    文字：WZ  图片：TP 语音：YY  文件：WJ markdown:MD OA:OA 卡片：KP 链接LJ
	 */
	private String type;
	/**
	 * 附件地址
	 */
	private String filePath;
	/**
	 * 附件名称
	 */
	private String fileName;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 原文链接地址
	 */
	private String sourceUrl;
	/**
	 * 作者
	 */
	private String author;

	/**
	 * OA消息PCURL
	 */
	private String pcUrl;
	/**
	 * OA移动端URL
	 */
	private  String mobileUrl;

	/**
	 * OA消息头部的背景颜色
	 */
	private String bgColor;

	/**
	 * OA消息表单内容-最多显示6个
	 */
	private List<OapiMessageCorpconversationAsyncsendV2Request.Form> listFrom;

	/**
	 * OA消息体的标题
	 */
	private String bodyTitle;

	/**
	 * OA消息自定义的附件数目。此数字仅供显示，钉钉不作验证
	 */
	private String fileCount;
	/**
	 * OA消息单行富文本信息的数目
	 */
	private String richNum;
	/**
	 * OA消息单行富文本信息的单位
	 */
	private String richUnit;
	/**
	 * 消息体的关键字
	 */
	private String formKey;
	/**
	 * 消息体的关键字对应的值
	 */
	private String formValue;

	/**
	 * 使用独立跳转ActionCard样式时的按钮排列方式，竖直排列(0)，横向排列(1)；
	 */
	private String btnOrientation;

	/**
	 * 卡片按钮集合
	 */
	private List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonList;

	/**
	 * 集团公司ID
	 */
	private String jtgsId;

	/**
	 * 当前登录人
	 */
	private EndUser currentUser;

	public DdMsgVo(){}
	/**
	 * 图片/语音/MD/文件
	 * @param userIds 用户
	 * @param type 类型
	 * @param filePathOrMdText  附件地址或者MD内容
	 * @param fileNameOrMdName 附件名称或者MD标题
	 */
	public DdMsgVo(String userIds, String type, String filePathOrMdText, String fileNameOrMdName){
		this.userIds = userIds;
		this.type = type;
		if(type.equals("MD")){
			this.content=filePathOrMdText;
			this.title=fileNameOrMdName;
		}else{
			this.filePath=filePathOrMdText;
			this.fileName=fileNameOrMdName;
		}
	}

	/**
	 * 卡片
	 * @param userIds 用户
	 * @param type 类型
	 * @param cardTitle 标题
	 * @param markDownText 内容
	 * @param btnOrientation 按钮排列方式
	 * @param btnJsonList 按钮集合
	 */
	public DdMsgVo(String userIds,String type,String cardTitle, String markDownText,String btnOrientation,
				   List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonList){
		this.userIds = userIds;
		this.type = type;
		this.title=cardTitle;
		this.content=markDownText;
		this.btnOrientation=btnOrientation;
		this.btnJsonList=btnJsonList;
	}

	/**
	 * 文字
	 * @param userIds 用户
	 * @param type 类型
	 * @param context 内容
	 */
	public DdMsgVo(String userIds, String type, String context){
		this.userIds = userIds;
		this.type = type;
		this.content=context;
	}

	/**
	 * 链接
	 * @param userIds 用户
	 * @param type 类型
	 * @param filePath 附件地址
	 * @param fileName 附件名称
	 * @param title 标题
	 * @param text 内容
	 * @param url 地址
	 */
	public DdMsgVo(String userIds, String type, String filePath, String fileName, String title, String text, String url){
		this.userIds = userIds;
		this.type = type;
		this.filePath=filePath;
		this.fileName=fileName;
		this.title=title;
		this.content=text;
		this.sourceUrl=url;
	}

	/**
	 * OA
	 * @param userIds 用户
	 * @param type 类型
	 * @param filePath 附件地址
	 * @param fileName 附件名称
	 * @param url 移动端url
	 * @param pcUrl pcURL
	 * @param headText 表头标题
	 * @param headColor 表头背景色
	 * @param listFrom 表单内容 key：value
	 * @param richNum rich数目
	 * @param richUnit rich单位
	 * @param bodyContent 内容
	 * @param bodyTitle 表单标题
	 * @param fileCount 附件数量
	 * @param author 作者
	 */
	public DdMsgVo(String userIds, String type, String filePath, String fileName, String url, String pcUrl, String headText,
				   String headColor, List<OapiMessageCorpconversationAsyncsendV2Request.Form> listFrom, String richNum, String richUnit, String bodyContent, String bodyTitle, String fileCount, String author){
		this.userIds = userIds;
		this.type = type;
		this.filePath=filePath;
		this.fileName=fileName;
		this.mobileUrl=url;
		this.pcUrl=pcUrl;
		this.title=headText;
		this.bgColor=headColor;
		this.listFrom=listFrom;
		this.richNum=richNum;
		this.richUnit=richUnit;
		this.content=bodyContent;
		this.bodyTitle=bodyTitle;
		this.fileCount=fileCount;
		this.author=author;
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

	@JsonIgnore
	public EndUser getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(EndUser currentUser) {
		this.currentUser = currentUser;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getFileCount() {
		return fileCount;
	}

	public void setFileCount(String fileCount) {
		this.fileCount = fileCount;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getPcUrl() {
		return pcUrl;
	}

	public void setPcUrl(String pcUrl) {
		this.pcUrl = pcUrl;
	}

	public String getMobileUrl() {
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	public List<OapiMessageCorpconversationAsyncsendV2Request.Form> getListFrom() {
		return listFrom;
	}

	public void setListFrom(List<OapiMessageCorpconversationAsyncsendV2Request.Form> listFrom) {
		this.listFrom = listFrom;
	}

	public String getBodyTitle() {
		return bodyTitle;
	}

	public void setBodyTitle(String bodyTitle) {
		this.bodyTitle = bodyTitle;
	}

	public String getRichNum() {
		return richNum;
	}

	public void setRichNum(String richNum) {
		this.richNum = richNum;
	}

	public String getRichUnit() {
		return richUnit;
	}

	public void setRichUnit(String richUnit) {
		this.richUnit = richUnit;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getFormValue() {
		return formValue;
	}

	public void setFormValue(String formValue) {
		this.formValue = formValue;
	}

	public String getBtnOrientation() {
		return btnOrientation;
	}

	public void setBtnOrientation(String btnOrientation) {
		this.btnOrientation = btnOrientation;
	}

	public List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> getBtnJsonList() {
		return btnJsonList;
	}

	public void setBtnJsonList(List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonList) {
		this.btnJsonList = btnJsonList;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
}

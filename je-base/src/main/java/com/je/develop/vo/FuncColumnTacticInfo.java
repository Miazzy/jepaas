package com.je.develop.vo;

public class FuncColumnTacticInfo {
	//-----------------------字典渲染器---------------------
	/**字典项配置*/
	private String DDCode;
	/**颜色策略配置[通用]*/
	private String color;
	/**是否是从数据字典表中取得的数据*/
	private Boolean ISDD;
	/**日期格式化控件*/
	private String format;
	private String linkMethod;
	//----------------------高亮显示器---------------------
	/**关键字组建名称*/
	private String keyWord;
	public String getDDCode() {
		return DDCode;
	}
	public void setDDCode(String dDCode) {
		DDCode = dDCode;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Boolean getISDD() {
		return ISDD;
	}
	public void setISDD(Boolean iSDD) {
		ISDD = iSDD;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getLinkMethod() {
		return linkMethod;
	}
	public void setLinkMethod(String linkMethod) {
		this.linkMethod = linkMethod;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
}

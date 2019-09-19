package com.je.dd.vo;


/**
 * 为了EXT-JS生成JSON串而创建的VO
 * @author chenmeng
 *
 */
public class DictionaryItemVo {
	private String text;
	private String code;
	private String id;
	private String icon;//图标
	private String backgroundColor;//单元格颜色
	private String textColor;//字体颜色
	private String iconCls;//图标
	private String iconClsImage;
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getTextColor() {
		return textColor;
	}
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getIconClsImage() {
		return iconClsImage;
	}
	public void setIconClsImage(String iconClsImage) {
		this.iconClsImage = iconClsImage;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}

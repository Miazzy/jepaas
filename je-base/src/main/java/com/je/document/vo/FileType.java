package com.je.document.vo;

import java.io.Serializable;

/**
 * 文件类型VO
 * @author zhangshuaipeng
 *
 */
public class FileType implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -4432998823352761456L;
	private String name;//名称
	private String code;//编码
	private String iconCls;//图片
//	private String thumbnailInfo;//缩略图信息
//	private String bigIconCls;//TODO未处理
//	private String thumbnailCls;//TODO未处理
//	private String format;//TODO未处理
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
//	public String getBigIconCls() {
//		return bigIconCls;
//	}
//	public void setBigIconCls(String bigIconCls) {
//		this.bigIconCls = bigIconCls;
//	}
//	public String getThumbnailCls() {
//		return thumbnailCls;
//	}
//	public void setThumbnailCls(String thumbnailCls) {
//		this.thumbnailCls = thumbnailCls;
//	}
//	public String getFormat() {
//		return format;
//	}
//	public void setFormat(String format) {
//		this.format = format;
//	}
//
//	public String getThumbnailInfo() {
//		return thumbnailInfo;
//	}
//
//	public void setThumbnailInfo(String thumbnailInfo) {
//		this.thumbnailInfo = thumbnailInfo;
//	}
}

package com.je.wf.processVo;

import java.io.Serializable;

/**
 * 流程按钮信息
 * @author zhangshuaipeng
 *
 */
public class ButtonInfo implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -3814546171448861074L;
	private String text;
	private String code;
	private String zdyText;
	private String comment;
	private String events;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getZdyText() {
		return zdyText;
	}
	public void setZdyText(String zdyText) {
		this.zdyText = zdyText;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getEvents() {
		return events;
	}
	public void setEvents(String events) {
		this.events = events;
	}


}

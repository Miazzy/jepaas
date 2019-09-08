package com.je.wf.processVo;

import java.io.Serializable;

public class WarnInfoVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6856918425967850684L;
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 名称
	 */
	private String text;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 天数
	 */
	private int day;
	/**
	 * 时间类型     时 || 天
	 */
	private String timeType;
	/**
	 * 业务执行
	 */
	private String config;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
}

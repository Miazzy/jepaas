package com.je.core.entity.extjs;

public class DbModel {
	/**字段编码*/
	private String code;
	/**类型*/
	private String type;
	/**长度*/
	private String length;
	/**是否为空*/
	private boolean isNull;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public boolean isNull() {
		return isNull;
	}
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

}

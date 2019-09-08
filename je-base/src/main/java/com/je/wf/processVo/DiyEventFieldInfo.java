package com.je.wf.processVo;

import java.io.Serializable;

/**
 * 结束事件赋值字段
 * @author zhangshuaipeng
 *
 */
public class DiyEventFieldInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2141615861613029016L;
	public String field;
	public String type;
	public String value;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}

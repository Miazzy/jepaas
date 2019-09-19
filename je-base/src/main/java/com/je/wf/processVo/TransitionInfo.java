package com.je.wf.processVo;

import java.io.Serializable;

/**
 * 流转线的vo
 * @author zhangshuaipeng
 *
 */
public class TransitionInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8503896978139260443L;
	private String label;
	private String name;
	private String info;
	private String to;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
}

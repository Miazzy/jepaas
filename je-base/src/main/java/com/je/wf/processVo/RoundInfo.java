package com.je.wf.processVo;

import java.io.Serializable;

public class RoundInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3032657377146899518L;
	private String type;
	private String names;
	private String codes;
	private String ids;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}
	public String getCodes() {
		return codes;
	}
	public void setCodes(String codes) {
		this.codes = codes;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
}

package com.je.core.entity.extjs;
/**
 * Extjs4.0的Model
 * @author YUNFENCGHENG
 * 2011-12-30 下午01:19:33
 */
public class Model {
	private String name;
	private String type;
	
	
	public Model(String name, String type) {
		this.name = name;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

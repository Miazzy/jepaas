package com.je.core.ann.entity;

public class DbFieldVo {
	/**
	 * 字段名称
	 */
	private String name;
	/**
	 * 参数类型   in 入参   out 出参
	 */
	private String paramType="in";
	/**
	 * 数据类型   使用 DbProduceUtil getTypeName 来获取数据库的字段类型  DbFieldType.
	 */
	private String fieldType;
	/**
	 *  值
	 */
	private Object value;


	public DbFieldVo(String name, String paramType, String fieldType) {
		super();
		this.name = name;
		this.paramType = paramType;
		this.fieldType = fieldType;
	}
	public DbFieldVo(String name, String paramType, String fieldType,
					 Object value) {
		super();
		this.name = name;
		this.paramType = paramType;
		this.fieldType = fieldType;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

}

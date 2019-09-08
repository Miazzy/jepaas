package com.je.dd.vo;

import java.util.HashMap;
import java.util.Map;
/**
 * 自定义字典信息VO
 * @author zhangshuaipeng
 *
 */
public class DicInfoVo {
	private String ddCode;
	private String rootId;
	private String fieldName;
	private String fieldCode;
	private String idSuffix;
	private String whereSql;
	private String orderSql;
	/**字典联动时使用*/
	private String parentId;
	/**字典联动时使用*/
	private String parentCode;
	private Map<String,String> params=new HashMap<String,String>();
	public String getDdCode() {
		return ddCode;
	}
	public void setDdCode(String ddCode) {
		this.ddCode = ddCode;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldCode() {
		return fieldCode;
	}
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}
	public String getIdSuffix() {
		return idSuffix;
	}
	public void setIdSuffix(String idSuffix) {
		this.idSuffix = idSuffix;
	}
	public String getWhereSql() {
		return whereSql;
	}
	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}
	public String getOrderSql() {
		return orderSql;
	}
	public void setOrderSql(String orderSql) {
		this.orderSql = orderSql;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String getRootId() {
		return rootId;
	}
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
}

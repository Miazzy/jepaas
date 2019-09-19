package com.je.wf.processVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 结束业务处理事件
 * @author zhangshuaipeng
 *
 */
public class DiyEventInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3554361142496631586L;
	/**
	 * 表名称
	 */
	public String tableName;
	/**
	 * 表编码
	 */
	public String tableCode;
	/**
	 * 流程状态
	 */
	public String audFlag;
	/**
	 * 条件
	 */
	public String whereSql;
	/**
	 * 赋值字段配置
	 */
	public List<DiyEventFieldInfo> fieldConfigs=new ArrayList<DiyEventFieldInfo>();
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	public String getAudFlag() {
		return audFlag;
	}
	public void setAudFlag(String audFlag) {
		this.audFlag = audFlag;
	}
	public String getWhereSql() {
		return whereSql;
	}
	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}
	public List<DiyEventFieldInfo> getFieldConfigs() {
		return fieldConfigs;
	}
	public void setFieldConfigs(List<DiyEventFieldInfo> fieldConfigs) {
		this.fieldConfigs = fieldConfigs;
	}
	
}

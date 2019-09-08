package com.je.table.vo;
/**
 * 表的主表、子表展示的VO
 * @author zhangshuaipeng
 *
 */
public class TableDataView {
	private String id;
	private String tableCode;
	private String tableName;
	private String tableType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	
}

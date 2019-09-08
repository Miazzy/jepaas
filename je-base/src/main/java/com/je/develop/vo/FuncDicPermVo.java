package com.je.develop.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
/**
 * 字典权限信息
 * @author zhangshuaipeng
 *
 */
public class FuncDicPermVo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8179460634172249845L;
	private Boolean allItem=false;
	private String querySql="";
	private Boolean sqlOverwrite=false;
	private Boolean sqlQuery=true;
	private Set<String> itemIds=new HashSet<String>();
	public Boolean getAllItem() {
		return allItem;
	}
	public void setAllItem(Boolean allItem) {
		this.allItem = allItem;
	}
	public String getQuerySql() {
		return querySql;
	}
	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}
	public Boolean getSqlOverwrite() {
		return sqlOverwrite;
	}
	public void setSqlOverwrite(Boolean sqlOverwrite) {
		this.sqlOverwrite = sqlOverwrite;
	}
	public Set<String> getItemIds() {
		return itemIds;
	}
	public void setItemIds(Set<String> itemIds) {
		this.itemIds = itemIds;
	}
	public Boolean getSqlQuery() {
		return sqlQuery;
	}
	public void setSqlQuery(Boolean sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
	
}

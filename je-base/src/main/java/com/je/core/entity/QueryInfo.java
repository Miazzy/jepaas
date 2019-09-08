package com.je.core.entity;

public class QueryInfo {
	private String whereSql = "";
	private String orderSql = "";
	private int start;
	private int limit;
	private long count = 0;
	
	public QueryInfo() {
		super();
	}
	public QueryInfo(String whereSql, String orderSql) {
		super();
		this.whereSql = whereSql;
		this.orderSql = orderSql;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
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
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}

}

package com.je.phone.vo;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

/**
 * TODO未处理
 */
public class DsInfoVo {
	private JSONObject params;
	private HttpServletRequest request;
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
}

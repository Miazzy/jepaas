package com.je.core.security.vo;

import javax.servlet.http.HttpServletRequest;

public class TokenLoginVo {
	private HttpServletRequest request;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
}

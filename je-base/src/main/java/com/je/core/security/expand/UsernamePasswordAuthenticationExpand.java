package com.je.core.security.expand;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

//TODO 张帅鹏 该类可删除
public class UsernamePasswordAuthenticationExpand extends UsernamePasswordAuthenticationToken {
	/**
	 *
	 */
	private static final long serialVersionUID = 8154149782598319471L;
	public UsernamePasswordAuthenticationExpand(Object principal,
												Object credentials) {
		super(principal, credentials);
		// TODO Auto-generated constructor stub
	}
	public UsernamePasswordAuthenticationExpand(Object principal, Object credentials, GrantedAuthority[] authorities) {
		super(principal, credentials,authorities);
	}
	private HttpServletRequest request;
	private Map params;
	public Map getParams() {
		return params;
	}
	public void setParams(Map params) {
		this.params = params;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


}

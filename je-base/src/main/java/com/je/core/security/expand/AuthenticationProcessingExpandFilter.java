package com.je.core.security.expand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.Authentication;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;
import org.springframework.security.util.TextUtils;



//TODO 张帅鹏 该类可删除
public class AuthenticationProcessingExpandFilter  extends AuthenticationProcessingFilter {

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request) {
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

//	        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		UsernamePasswordAuthenticationExpand authRequest= new UsernamePasswordAuthenticationExpand(username, password);
		authRequest.setParams(request.getParameterMap());
		authRequest.setRequest(request);
		// Place the last username attempted into HttpSession for views
		HttpSession session = request.getSession(false);

		if (session != null || getAllowSessionCreation()) {
			request.getSession().setAttribute(SPRING_SECURITY_LAST_USERNAME_KEY, TextUtils.escapeEntities(username));
		}

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

}

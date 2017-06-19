package com.xunxintech.ruyue.coach.base.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.UUID;


public class OAuthAnonymousFilter extends AnonymousAuthenticationFilter {
	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	
	public OAuthAnonymousFilter(String key) {
		super(key);
	}

	@Override
	protected Authentication createAuthentication(HttpServletRequest request) {
		String principal = request.getParameter("token");
		if(StringUtils.isEmpty(principal)){
			throw new RuntimeException("token is empty");
		}
		String requestID = UUID.randomUUID().toString();//request.getHeader("RequestID");
		if(StringUtils.isEmpty(requestID)){
			throw new RuntimeException("RequestID is empty");
		}
		AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(requestID,
				principal, getAuthorities());
		auth.setDetails(authenticationDetailsSource.buildDetails(request));
		auth.setAuthenticated(false);
		return auth;
	}
	
}

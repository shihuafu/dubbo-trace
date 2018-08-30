package com.fushihua.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class MyLogoutHandler implements LogoutHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(MyLogoutHandler.class);
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		
		String sessionId = request.getSession(false).getId();
		
		logger.info("{}系统退出", sessionId);
		
	}

}

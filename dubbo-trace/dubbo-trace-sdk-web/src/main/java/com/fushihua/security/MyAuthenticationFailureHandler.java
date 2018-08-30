package com.fushihua.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MyAuthenticationFailureHandler 
 * @Description: 认证失败 
 * @author fushihua
 * @date 2017年3月2日 上午9:32:44
 */
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler implements
		AuthenticationFailureHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(MyAuthenticationFailureHandler.class);
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		logger.info("认证失败, reason is {}", exception.toString(), exception);
		
		super.onAuthenticationFailure(request, response, exception);
	}
}

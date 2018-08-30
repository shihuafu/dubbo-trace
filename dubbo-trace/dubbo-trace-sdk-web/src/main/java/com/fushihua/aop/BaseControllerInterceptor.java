package com.fushihua.aop;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fushihua.domain.SysConst;
import com.fushihua.util.EmptyUtils;
import com.fushihua.util.LogUtils;
import com.fushihua.util.UUIDUtils;
import com.fushihua.util.WebUtils;


/**
 * @ClassName: BaseControllerInterceptor 
 * @Description: 访问日志 
 * @author fushihua
 * @date 2017年3月1日 下午3:44:12
 */
public class BaseControllerInterceptor extends HandlerInterceptorAdapter {
	
	private Logger statisticsLogger = LoggerFactory.getLogger("Statistics"); // 统计类日志
	
	private final static String BASE_CONTROLLER_START_TIME = "BASE_CONTROLLER_START_TIME";
	
	private String appCode;
	
	private Set<String> ignoreUrls = new HashSet<>();
	private Set<String> ignoreParameters = new HashSet<>();
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//统一设置入参为访问开始时间
		long startTime = System.currentTimeMillis();
		request.setAttribute(BASE_CONTROLLER_START_TIME, startTime);
		
		String username = "fushihua";
		if (EmptyUtils.isEmpty(username)) {
			username = "anonymous";
		}
		String traceId = UUIDUtils.getUUID();
		
		MDC.put(SysConst.LOGIN_USER_ID, username);
		MDC.put(SysConst.TRACE_ID, traceId);
		MDC.put(SysConst.SPAN_ID, String.valueOf(startTime));
		MDC.put(SysConst.APP_CODE, appCode);
		
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
		String url = request.getServletPath();
		
		StringBuffer parameter = new StringBuffer();
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterMap != null && !ignoreUrls.contains(url)) {
			for (Entry<String, String[]> entry : parameterMap.entrySet()) {
				String parameterName = entry.getKey();
				if (!ignoreParameters.contains(parameterName)) {
					String[] parameterValues = entry.getValue();
					if (parameterValues != null) {
						for (String parameterValue : parameterValues) {
							parameter.append(parameterName + "=" + parameterValue + "&");
						}
					}
				}
			}
		}

		String arguments = parameter.toString();
		String remoteHost = WebUtils.getIpAddr(request);
		
		long startTime = 0;
		long endTime = System.currentTimeMillis();
		try {
			startTime = (long)request.getAttribute(BASE_CONTROLLER_START_TIME);
			request.removeAttribute(BASE_CONTROLLER_START_TIME);
		} catch (Exception e) {}
		
		/** 记录所有url调用情况 统计类日志 */
		statisticsLogger.info("{}", LogUtils.getStatisticsInfo(remoteHost, url, startTime, endTime, arguments, null));
		
		super.postHandle(request, response, handler, modelAndView);
		MDC.clear();
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (EmptyUtils.isNotEmpty(ex)) {
			String url = request.getServletPath();
			String remoteHost = WebUtils.getIpAddr(request);
			
			long startTime = 0;
			long endTime = System.currentTimeMillis();
			try {
				startTime = (long)request.getAttribute(BASE_CONTROLLER_START_TIME);
				request.removeAttribute(BASE_CONTROLLER_START_TIME);
			} catch (Exception e) {}
			/** 记录所有url调用情况 统计类日志 */
			statisticsLogger.info("{}", LogUtils.getStatisticsInfo(remoteHost, url, startTime, endTime, null, ex.toString()), ex);
			
		}
		super.afterCompletion(request, response, handler, ex);
		MDC.clear();
	}

	public Set<String> getIgnoreUrls() {
		return ignoreUrls;
	}

	public void setIgnoreUrls(Set<String> ignoreUrls) {
		this.ignoreUrls = ignoreUrls;
	}

	public Set<String> getIgnoreParameters() {
		return ignoreParameters;
	}

	public void setIgnoreParameters(Set<String> ignoreParameters) {
		this.ignoreParameters = ignoreParameters;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

}

package com.fushihua.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.MDC;

import com.fushihua.domain.SysConst;

public class LogUtils {

	/** 定义日志分隔符 */
	private final static String LOG_SEPARATOR = "|";

	/** 获取本机IP */
	private final static String LOCALHOST = NetWorkUtils.getLocalIP();

	public static String getStatisticsInfo(String remoteHost, String url, long startTime, long endTime,
			String arguments, String error) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

		String parentId = MDC.get(SysConst.PARENT_ID);
		StringBuffer sb = new StringBuffer();
		// 第一部分为集团规范统计类日志要求部分
		// 调用者IP
		sb.append(parentId == null ? "" : parentId);
		// 调用者IP
		sb.append(LOG_SEPARATOR).append(LOCALHOST);
		// 调用者IP
		sb.append(LOG_SEPARATOR).append(remoteHost);
		// 服务端接口标识
		sb.append(LOG_SEPARATOR).append(url);
		// 耗时
		sb.append(LOG_SEPARATOR).append(endTime - startTime);
		// startTime
		sb.append(LOG_SEPARATOR).append(formatter.format(new Date(startTime)));
		// endTime
		sb.append(LOG_SEPARATOR).append(formatter.format(new Date(endTime)));
		sb.append(LOG_SEPARATOR).append(arguments == null ? "" : arguments);
		// 异常信息
		sb.append(LOG_SEPARATOR).append(error == null ? "" : error);
		sb.append(LOG_SEPARATOR);
		return sb.toString();
	}
}

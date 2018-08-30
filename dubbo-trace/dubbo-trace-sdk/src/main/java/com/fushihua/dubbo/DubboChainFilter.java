package com.fushihua.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.fushihua.domain.SysConst;
import com.fushihua.util.JsonUtils;
import com.fushihua.util.LogUtils;
import com.fushihua.util.UUIDUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @ClassName: DubboChainFilter 
 * @Description: 调用链
 * @author fushihua
 * @date 2018年1月11日 下午7:04:56
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class DubboChainFilter implements Filter {

    private final static Logger statisticsLogger = LoggerFactory.getLogger("Statistics"); // 统计类日志
    
    private final static Logger logger = LoggerFactory.getLogger(DubboChainFilter.class); // 统计类日志
    
    /**
     * 调用过程拦截
     */
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    	
        long startTime = System.currentTimeMillis();
        
        RpcContext context = RpcContext.getContext();
        String className = context.getUrl().getServiceInterface();
        
    	/*
    	 * 若是dubbo自带接口，则直接返回
    	 */
    	if (className.startsWith("com.alibaba.dubbo")) {
    		return invoker.invoke(invocation);
    	}
        
    	String appCode = null;
    	String methodName = null;
        String url = null;
        String remoteHost = null;
        String arguments = null;
        boolean isConsumerSide = false;
        boolean isProviderSide = false;
        
        String loginUserId = null;
        String traceId = null;
        String spanId = null;
        String parentId = null;
        
        try {
        	
        	appCode = context.getUrl().getParameter(Constants.APPLICATION_KEY);
        	methodName = context.getMethodName();
            url = className+"."+methodName;
        	isConsumerSide = context.isConsumerSide();
        	isProviderSide = context.isProviderSide();
        	
        	/*
        	 * 若是消费者, 获取MDC调用链信息, 赋值给RpcContext以传值给下游调用者
        	 * 若是提供者, 获取RpcContext调用链信息, 赋值MDC供使用
        	 */
        	if (isConsumerSide) {
        		//获取MDC调用链信息
        		loginUserId = MDC.get(SysConst.LOGIN_USER_ID);
        		traceId = MDC.get(SysConst.TRACE_ID);
        		spanId = MDC.get(SysConst.SPAN_ID);
        		//若MDC调用链为空, 则生成调用链信息
        		if (traceId == null || traceId.isEmpty()) {
        			traceId = UUIDUtils.getUUID();
        			spanId = String.valueOf(startTime);
				}
        		//赋值给RpcContext以传值给下游调用者
        		RpcContext.getContext().setAttachment(SysConst.LOGIN_USER_ID, loginUserId);
        		RpcContext.getContext().setAttachment(SysConst.TRACE_ID, traceId);
        		RpcContext.getContext().setAttachment(SysConst.SPAN_ID, spanId);
			}
        	else if (isProviderSide) {
        		//获取RpcContext调用链信息, 重新生成调用序号
        		loginUserId = context.getAttachment(SysConst.LOGIN_USER_ID);
        		traceId = context.getAttachment(SysConst.TRACE_ID);
        		parentId = context.getAttachment(SysConst.SPAN_ID);
        		spanId = String.valueOf(startTime);
        		remoteHost = context.getRemoteHost();
        		//赋值MDC供使用
        		MDC.put(SysConst.APP_CODE, appCode);
        		MDC.put(SysConst.TRACE_ID, traceId);
        		MDC.put(SysConst.PARENT_ID, parentId);
        		MDC.put(SysConst.SPAN_ID, spanId);
        		MDC.put(SysConst.LOGIN_USER_ID, loginUserId);
        		
        		//获取入参并加密
        		arguments = JsonUtils.toJson(context.getArguments());
			}
		} catch (Exception ex) {
			logger.error("dubbo调用链获取异常", ex);
		}
        
        try {
            Result result = invoker.invoke(invocation);
            
            //若是提供者，调用完毕则清除MDC 并输出访问日志
            if (isProviderSide) {
            	long endTime = System.currentTimeMillis();
            	//记录所有url调用情况及入参, 预警分析平台使用
            	statisticsLogger.info("{}", LogUtils.getStatisticsInfo(remoteHost, url, startTime, endTime, arguments, null));
            	//清除MDC
            	MDC.clear();
			}
            return result;
        } catch (RpcException ex) {
        	
        	//若是提供者，调用完毕则清除MDC 并输出访问日志
            if (isProviderSide) {
            	long endTime = System.currentTimeMillis();
            	//记录所有url调用情况及入参, 预警分析平台使用
            	statisticsLogger.error("{}", LogUtils.getStatisticsInfo(remoteHost, url, startTime, endTime, arguments, ex.toString()), ex);
            	//清除MDC
            	MDC.clear();
			}
            throw ex;
        }
    }

}
package com.fushihua.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: AbstractService 
 * @Description: service实现均需继承 
 * @author fushihua
 * @date 2016年9月7日 上午11:44:30
 */
public class AbstractService {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected final static Logger noticeLogger = LoggerFactory.getLogger("Notice");
}

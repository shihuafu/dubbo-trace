package com.fushihua.service.impl;

import org.springframework.stereotype.Service;

import com.fushihua.service.SysIdService;
import com.fushihua.util.SnowflakeIdWorker;

/**
 * @ClassName: SysIdServiceImpl 
 * @Description: SysIdService实现
 * @author fushihua
 * @date 2016年9月7日 下午12:01:54
 */
@Service
public class SysIdServiceImpl extends AbstractService implements SysIdService {
	
	private static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0L, 0L);
	
	@Override
	public Long findNextId() {
		long id = idWorker.nextId();
		logger.info("findNextId is {}", id);
		return id;
	}
}

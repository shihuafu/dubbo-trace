package com.fushihua.soa.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.fushihua.service.SysIdService;
import com.fushihua.soa.SysIdSoaService;

@Service(interfaceClass = SysIdSoaService.class)
@Component
public class SysIdSoaServiceImpl extends AbstractSoaService implements SysIdSoaService {

	@Autowired
	private SysIdService sysIdService;
	
	@Override
	public Long findNextId() {
		return sysIdService.findNextId();
	}

}

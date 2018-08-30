package com.fushihua.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fushihua.soa.SysIdSoaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "系统", tags = { "系统-序列号生成" })
@RestController
@RequestMapping(value = "/sys/id")
public class SysIdController extends BaseController {
	
	@Reference
	private SysIdSoaService SysIdSoaService;
	
	@ApiOperation(value = "获取流水号", notes = "获取流水号")
	@GetMapping(value = "/findNextId")
	public Long findNextId(@ApiIgnore ModelMap model, @ApiIgnore RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		long id = SysIdSoaService.findNextId();
		logger.info("findNextId is {}", id);
		return id;
	}

}

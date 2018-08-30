package com.fushihua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;

/**
 * @ClassName: Application 
 * @Description: 启动类 
 * @author fushihua
 * @date 2017年12月8日 下午3:51:06
 */
@SpringBootApplication
@EnableDubboConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

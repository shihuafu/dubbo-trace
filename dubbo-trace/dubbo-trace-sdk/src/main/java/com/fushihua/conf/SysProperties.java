package com.fushihua.conf;


import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SysProperties implements EnvironmentAware {

	private Environment environment;

	@Override
	public void setEnvironment(Environment env) {
		this.environment = env;
	}

	/**
	 * 获取参数值
	 * 
	 * @param key
	 *            参数
	 * @return
	 * @throws UserException
	 */
	public String getProperty(String key) {
		return environment.getProperty(key);
	}

}

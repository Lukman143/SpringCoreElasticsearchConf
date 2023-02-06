package com.zetcode;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@PropertySource(value = "application.properties", ignoreResourceNotFound = true)
public class DataConfig implements InitializingBean {

	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.username}")
	private String username;

	@Value("${jdbc.password}")
	private String password;

	@Value("${app.name}")
	private String appName;

	@Override
	public void afterPropertiesSet() {
		System.out.println(url);
		System.out.println(username);
		System.out.println(password);
		System.out.println(appName);
	}

	public String getUrl() {
		return this.url;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getAppName() {
		return this.appName;
	}
}

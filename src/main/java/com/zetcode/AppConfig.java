package com.zetcode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "application.properties", ignoreResourceNotFound = true)
public class AppConfig {
	
	@Bean
	public ElasticsearchProperties applicationProperties() {
		return new ElasticsearchProperties();
	}
}
package com.zetcode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchProperties {

	@Value("${elasticsearch.host}")
	private String host;

	@Value("${elasticsearch.port}")
	private Integer port;

	public String getHost() {
		return this.host;
	}

	public Integer getPort() {
		return this.port;
	}

}

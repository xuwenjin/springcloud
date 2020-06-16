package com.xwj;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 可以把相同前缀的配置信息通过配置项名称映射成实体类
 */
@ConfigurationProperties(prefix = "hello")
public class HelloProperties {

	private String prefix;

	private String suffix;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}

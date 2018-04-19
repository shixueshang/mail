package com.coolgua.mail.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "anti")
public class AntiInjectConfig {

	private String sql;

	private String xss;
	
	private String ignore;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getXss() {
		return xss;
	}

	public void setXss(String xss) {
		this.xss = xss;
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

}

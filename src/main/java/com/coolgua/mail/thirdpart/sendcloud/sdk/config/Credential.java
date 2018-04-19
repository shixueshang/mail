package com.coolgua.mail.thirdpart.sendcloud.sdk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 发件身份认证
 */
@Component
@PropertySource("classpath:mail.properties")
public class Credential {

	@Value("${server}")
	private String server;
	@Value("${send_api}")
	private String sendAPI;
	private String apiUser;
	private String apiKey;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getSendAPI() {
		return sendAPI;
	}

	public void setSendAPI(String sendAPI) {
		this.sendAPI = sendAPI;
	}

	public String getApiUser() {
		return apiUser;
	}

	public void setApiUser(String apiUser) {
		this.apiUser = apiUser;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

}
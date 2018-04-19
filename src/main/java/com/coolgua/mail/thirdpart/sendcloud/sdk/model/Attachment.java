package com.coolgua.mail.thirdpart.sendcloud.sdk.model;

import java.io.InputStream;

/**
 * 附件
 */
public class Attachment {

	public InputStream content;
	public String name;

	public InputStream getContent() {
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Attachment(InputStream content, String name) {
		this.content = content;
		this.name = name;
	}
}
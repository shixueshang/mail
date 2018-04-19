package com.coolgua.mail.thirdpart.sendcloud.sdk.model;

import com.coolgua.mail.thirdpart.sendcloud.sdk.exception.BodyException;
import com.coolgua.mail.thirdpart.sendcloud.sdk.exception.ContentException;
import java.util.List;
import org.apache.http.util.Asserts;

/**
 * 邮件主体，包含邮件、收件人、邮件内容
 *
 * <P>
 * <OL>
 * <LI><B>body</B> 邮件，必须
 * <LI><B>content</B> 邮件内容，必须
 * <LI><B>to</B> 收件人，非必须(可以在xsmtpapi中添加)
 * </OL>
 * <P>
 * 
 * @author Administrator
 *
 */
public class SendCloudMail {
	public MailBody body;
	public List<String> to;
	public Content content;

	public MailBody getBody() {
		return body;
	}

	public void setBody(MailBody body) {
		this.body = body;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public void validate() throws BodyException, ContentException {
		Asserts.notNull(body, "body");
		body.validate();
		Asserts.notNull(content, "content");
		content.validate();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String address : to) {
			if (sb.length() > 0)
				sb.append(";");
			sb.append(address);
		}
		return sb.toString();
	}

}
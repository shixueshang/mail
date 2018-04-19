package com.coolgua.mail;

import com.coolgua.mail.thirdpart.sendcloud.sdk.config.Credential;
import com.coolgua.mail.thirdpart.sendcloud.sdk.core.SendCloud;
import com.coolgua.mail.thirdpart.sendcloud.sdk.exception.BodyException;
import com.coolgua.mail.thirdpart.sendcloud.sdk.exception.ContentException;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.MailBody;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.SendCloudMail;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.TextContent;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.TextContent.ScContentType;
import com.coolgua.mail.thirdpart.sendcloud.sdk.util.ResponseData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSendCloud {

	@Resource
	private Credential credential;

	@Test
	public void testSend() throws Throwable {
		send_common();
	}

	@Test
	public void testSendAdvance() throws IOException, ContentException, BodyException {
		send_common_advanced();
	}


	public void send_common() throws IOException, BodyException, ContentException {

		credential.setApiUser("postmaster@coolgua-demo.sendcloud.org");
		credential.setApiKey("JdKvObyYo3RDa3Rr");

		List<String> to = new ArrayList<>();
		to.add("12345@qq.com");
		to.add("dsedddd@coolgua.com");

		MailBody body = new MailBody();
		// 设置 From
		body.setFrom("sendcloud@sendcloud.org");
		// 设置 FromName
		body.setFromName("SendCloud");
		// 设置 ReplyTo
		body.setReplyTo("reply@sendcloud.org");
		// 设置标题
		body.setSubject("来自 SendCloud SDK 的邮件");
		// 创建文件附件
	/*	body.addAttachments(new File("D:\\home\\picture\\558.png"));
		body.addAttachments(new File("D:\\home\\picture\\58c961f2-1c3e-4c19-8bb7-4df2487ff03f.jpg"));*/

		TextContent content = new TextContent();
		content.setContent_type(ScContentType.html);
		content.setText("<html><p>helo world</p></html>");

		SendCloudMail mail = new SendCloudMail();
		mail.setTo(to);
		mail.setBody(body);
		mail.setContent(content);

		SendCloud sc = SendCloud.getInstance();
		ResponseData res = sc.sendMail(mail, credential);
		System.out.println(res.getResult());
		System.out.println(res.getStatusCode());
		System.out.println(res.getMessage());
		System.out.println(res.getInfo());
	}

	public void send_common_advanced() throws IOException, BodyException, ContentException {

		credential.setApiUser("postmaster@coolgua-demo.sendcloud.org");
		credential.setApiKey("JdKvObyYo3RDa3Rr");

		List<String> to = new ArrayList<>();
		to.add("460847871@qq.com");
		to.add("lihongde@coolgua.com");

		MailBody body = new MailBody();
		// 设置 From
		body.setFrom("sendcloud@sendcloud.org");
		// 设置 FromName
		body.setFromName("SendCloud");
		// 设置 ReplyTo
		body.setReplyTo("reply@sendcloud.org");
		// 设置标题
		body.setSubject("来自 SendCloud SDK 的邮件");
		// 创建文件附件
		body.addAttachments(new File("D:\\home\\picture\\558.png"));

		// 配置 Xsmtpapi 扩展字段
		List<String> toList = new ArrayList<String>();
		toList.add("lihongde@coolgua.com");
		toList.add("lihongde@coolgua.com");
		List<String> moneyList = new ArrayList<String>();
		moneyList.add("1000");
		moneyList.add("2000");
		List<String> nameList = new ArrayList<String>();
		nameList.add("a");
		nameList.add("b");
		Map<String, List<String>> sub = new HashMap<String, List<String>>();
		sub.put("%name%", nameList);
		sub.put("%money%", moneyList);
		// 此时, receiver 中添加的 to, cc, bcc 均会失效
		body.addXsmtpapi("to", toList);
		body.addXsmtpapi("sub", sub);
		body.addHeader("SC-Custom-test_key1", "test1");
		body.addHeader("NO-SC-Custom-test_key1", "test2");

		TextContent content = new TextContent();
		content.setContent_type(ScContentType.html);
		content.setText("<html><p>亲爱的 %name%: </p> 您本月的支出为: %money% 元.</p></html>");

		SendCloudMail mail = new SendCloudMail();
		mail.setTo(to);
		mail.setBody(body);
		mail.setContent(content);

		SendCloud sc = SendCloud.getInstance();
		ResponseData res = sc.sendMail(mail, credential);
		System.out.println(res.getResult());
		System.out.println(res.getStatusCode());
		System.out.println(res.getMessage());
		System.out.println(res.getInfo());
	}

}

package com.coolgua.mail.thirdpart.sendcloud;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coolgua.mail.SpringContextHolder;
import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.service.MailDetailService;
import com.coolgua.mail.thirdpart.MailProvider;
import com.coolgua.mail.thirdpart.sendcloud.sdk.config.Credential;
import com.coolgua.mail.thirdpart.sendcloud.sdk.core.SendCloud;
import com.coolgua.mail.thirdpart.sendcloud.sdk.exception.BodyException;
import com.coolgua.mail.thirdpart.sendcloud.sdk.exception.ContentException;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.MailBody;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.SendCloudMail;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.TextContent;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.TextContent.ScContentType;
import com.coolgua.mail.thirdpart.sendcloud.sdk.util.ResponseData;
import com.coolgua.mail.util.Constant.MAIL_PROVIDER;
import com.coolgua.mail.util.Constant.MailDetailStatus;
import com.coolgua.mail.util.Constant.MailEvent;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by lihongde on 2018/1/10 17:05.
 */
public class SendCloudMailProvider implements MailProvider {

  @Value("${MailRecipientMaxLength}")
  private int maxAddressLength;

  private static Logger logger = LoggerFactory.getLogger(SendCloudMailProvider.class);
  private Credential credential = SpringContextHolder.getBean(Credential.class);

  private static MailDetailService mailDetailService = SpringContextHolder.getBean("mailDetailService");

  /**
   * 单例
   */
  private static final SendCloudMailProvider sendCloudMailProvider = new SendCloudMailProvider();
  private SendCloudMailProvider() {

  }
  public static SendCloudMailProvider getInstance() {
    return sendCloudMailProvider;
  }

  @Override
  public int getProviderId() {
    return MAIL_PROVIDER.SENDCLOUD;
  }

  @Override
  public Object sendMail(Mail mail) throws Exception {
    credential.setApiUser(mail.getAccountName());
    credential.setApiKey(mail.getAccountPass());
    MailBody body = new MailBody(mail.getSender(), mail.getSenderName(), mail.getReplyAddress(), mail.getSubject());
    TextContent content = new TextContent();
    content.setContent_type(ScContentType.html);
    content.setText("<html>"+ mail.getContent() +"</html>");

    List<String> recipients = mail.getToList();
    SendCloudMail smail = new SendCloudMail();
    smail.setTo(recipients);
    smail.setBody(body);
    smail.setContent(content);
    SendCloud sc = SendCloud.getInstance();
    ResponseData res = null;
    try {
       res = sc.sendMail(smail, credential);
    } catch (ContentException e) {
      logger.error("邮件内容错误", e.getMessage());
    } catch (BodyException e) {
      logger.error("邮件参数错误", e.getMessage());
    }
    if(res.getResult()){
      JSONObject emailIdList = JSONObject.parseObject(res.getInfo());
      JSONArray emailArr = emailIdList.getJSONArray("emailIdList");
      for(Object obj : emailArr){
        String emailId =  (String)obj;
        String recipient = emailId.split("\\$")[1];
        for(MailDetail detail : mail.getDetails()){
          if(detail.getRecipient().equals(recipient)){
            detail.setEmailId(emailId);
            detail.setStatus(MailDetailStatus.DETAIL_REQUEST);
            detail.setEvent(MailEvent.EVENT_REQUEST);
          }else{
            detail.setStatus(MailDetailStatus.DETAIL_SUBMIT_FAILURE);
            detail.setEvent(MailEvent.EVENT_SUBMIT_FAILURE);
          }
        }
      }
      return  res.getResult();
    }

    return res.getMessage();
  }

  @Override
  public Map<String, String> getMailTrackData(Mail mail) {
    return null;
  }

}

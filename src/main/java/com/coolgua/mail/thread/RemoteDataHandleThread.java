package com.coolgua.mail.thread;

import com.coolgua.mail.SpringContextHolder;
import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.exception.FailedReqeustException;
import com.coolgua.mail.service.MailDetailService;
import com.coolgua.mail.service.MailService;
import com.coolgua.mail.util.Constant.MailDetailStatus;
import com.coolgua.mail.util.Constant.MailEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * Created by lihongde on 2018/1/16 16:10.
 */
public class RemoteDataHandleThread implements Runnable {

  private static Logger log = LoggerFactory.getLogger(RemoteDataHandleThread.class);

  private MailService mailService = SpringContextHolder.getBean("mailService");
  private MailDetailService mailDetailService = SpringContextHolder.getBean("mailDetailService");
  private Map<String, String> params;

  public RemoteDataHandleThread(Map<String, String> params){
    this.params = params;
  }

  @Override
  public void run() {

    String emailId = params.get("emailId");
    String recipient = params.get("recipient");
    Map<String, Object> queryParam = new HashMap<>();
    queryParam.put("emaiLId", emailId);
    queryParam.put("recipient", recipient);
    List<MailDetail> details = mailDetailService.findMailDetailsByCondition(queryParam);
    if(CollectionUtils.isEmpty(details)){
      throw new FailedReqeustException("没有找到对应邮址的记录, recipient=" + recipient);
    }
    if(details.size() > 1){
      throw new FailedReqeustException("找到多条记录, recipient=" + recipient);
    }

    MailDetail detail = details.get(0);
    detail.setUpdateTime(new Date());
    String event = params.get("event");
    detail.setEvent(event);

    Mail mail = mailService.getMail(detail.getMailId());
    if(event.equals(MailEvent.EVENT_DELIVER)){
      detail.setStatus(MailDetailStatus.DETAIL_DELIVER);
      mail.setDeliver(mail.getDeliver() + 1);
    }
    if(event.equals(MailEvent.EVENT_BOUNCE)){
      detail.setStatus(MailDetailStatus.DETAIL_BOUNCE);
      mail.setBounce(mail.getBounce() + 1);
    }
    if(event.equals(MailEvent.EVENT_CLICK)){
      mail.setClickTotal(mail.getClickTotal() + 1);
    }
    if(event.equals(MailEvent.EVENT_UNSUBSCRIBE)){
      detail.setUnsubscribe(true);
      detail.setUnsubscribeTime(new Date(params.get("timestamp")));
      mail.setUnsubscribeCount(mail.getUnsubscribeCount() + 1);
    }
    if(event.equals(MailEvent.EVENT_SPAM)){
      detail.setStatus(MailDetailStatus.DETAIL_SPAM);
      mail.setSpam(mail.getSpam() + 1);
    }
    if(event.equals(MailEvent.EVENT_INVALID_ADDRESS)){
      detail.setStatus(MailDetailStatus.DETAIL_INVALID_ADDRESS);
      mail.setInvalidAddress(mail.getInvalidAddress() + 1);
    }

    mailService.updateMail(mail);
    mailDetailService.updateMailDetail(detail);

  }
}

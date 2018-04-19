package com.coolgua.mail.thirdpart.webpower;

import com.coolgua.mail.SpringContextHolder;
import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.service.MailDetailService;
import com.coolgua.mail.thirdpart.MailProvider;
import com.coolgua.mail.util.Constant.MAIL_PROVIDER;
import com.coolgua.mail.util.Constant.MailDetailStatus;
import com.coolgua.mail.util.Constant.MailEvent;
import com.coolgua.mail.util.Constant.UnsubscribeLanguage;
import com.coolgua.mail.util.Constant.WebpowerMethod;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lihongde on 2018/1/10 17:06.
 */
public class WebpowerMailProvider implements MailProvider {

  private static Logger log = LoggerFactory.getLogger(WebpowerMailProvider.class);
  private Config config = SpringContextHolder.getBean(Config.class);

  //private static MailService mailService = SpringContextHolder.getBean("mailService");
  private static MailDetailService mailDetailService = SpringContextHolder.getBean("mailDetailService");

  private static final WebpowerMailProvider webpowerMailProvider = new WebpowerMailProvider();
  private WebpowerMailProvider() {

  }
  public static WebpowerMailProvider getInstance() {
    return webpowerMailProvider;
  }


  @Override
  public int getProviderId() {
    return MAIL_PROVIDER.WEBPOWER;
  }

  @Override
  public Object sendMail(Mail mail) throws Exception {
    DynamicHttpClientCall createMailingCall = new DynamicHttpClientCall(config.getNamespace(), WebpowerMethod.createMailing, config.getLocation());
    String mailingId = (String) handleResult(createMailingCall, buildCreateMailingMap(mail), WebpowerMethod.createMailing);
    if(mailingId.indexOf("<faultstring>") > -1){  //创建邮件失败
      String faultString = mailingId.substring(mailingId.indexOf("<faultstring>") + 13, mailingId.indexOf("</faultstring>"));
      return faultString;
    }
    mail.setMailingId(mailingId);
    String[] toUsers = new String[mail.getToList().size()];
    toUsers = mail.getToList().toArray(toUsers);
    DynamicHttpClientCall addGroupCall = new DynamicHttpClientCall(config.getNamespace(), WebpowerMethod.addGroup, config.getLocation());
    String groupId = (String) handleResult(addGroupCall, buildAddGroupMap(mail), WebpowerMethod.addGroup);
    mail.setGroupIds(new int[] {Integer.parseInt(groupId)});
    try{
    if (toUsers.length == 1) {
      if(mail.isScheduled()){
        sendMailingScheduled(mail, toUsers);
      }else{
        sendSingleMail(mail);
      }
    } else {
      int count = toUsers.length;
      if (count <= 1000) {
        if(mail.isScheduled()){
          sendMailingScheduled(mail, toUsers);
        }else{
          sendMailing(mail, toUsers);
        }
      } else {
        int capacity = 1000;
        int batch = count % capacity == 0 ? count / capacity : count / capacity + 1;
        for (int i = 0; i < batch; i++) {
          String[] tempToUsers = null;
          if ((count - i * capacity) <= capacity)
            tempToUsers = Arrays.copyOfRange(toUsers, (batch - 1) * capacity, toUsers.length);
          else
            tempToUsers = Arrays.copyOfRange(toUsers, i * capacity, capacity * (i + 1));
          try{
            Thread.sleep(3000);   //避免频繁发送
            if(mail.isScheduled()){
              sendMailingScheduled(mail, tempToUsers);
            }else{
              sendMailing(mail, tempToUsers);
            }
          }catch (Exception e) {
          }
        }
      }
    }
    }catch (Exception e){
      String message = "发送邮件失败";
      log.error(message, e);
      return message;
    }
    return true;
  }

  @Override
  public Map<String, String> getMailTrackData(Mail mail) {
    DynamicHttpClientCall getMailingStatsSummaryCall = new DynamicHttpClientCall(config.getNamespace(), WebpowerMethod.getMailingStatsSummary, config.getLocation());
    Map<String, String> patameterMap = new LinkedHashMap<String, String>();
    patameterMap.put("login", "<username>" + mail.getAccountName() + "</username><password>" + mail.getAccountPass() + "</password>");
    patameterMap.put("campaignID", mail.getCampaignId());
    patameterMap.put("mailing", mail.getMailingId());
    Map<String, String> resultMap = (Map<String, String>) handleResult(getMailingStatsSummaryCall, patameterMap, WebpowerMethod.getMailingStatsSummary);

    return resultMap;
  }

  /**
   * 发送系统邮件，即单个收件人
   *
   * @param mail
   * @return
   */
  private void sendSingleMail(Mail mail) {
    DynamicHttpClientCall addRecipientCall = new DynamicHttpClientCall(config.getNamespace(), WebpowerMethod.addRecipient, config.getLocation());
    String recipientID = (String) handleResult(addRecipientCall, buildAddRecipientMap(mail), WebpowerMethod.addRecipient);
    DynamicHttpClientCall sendSingleMailingCall = new DynamicHttpClientCall(config.getNamespace(), WebpowerMethod.sendSingleMailing, config.getLocation());
    handleResult(sendSingleMailingCall, buildSendSingleMailingMap(mail, recipientID), WebpowerMethod.sendSingleMailing);
  }

  /**
   * 发送营销类邮件，大量收件人
   *
   * @param mail
   * @param mailingID
   * @param toUsers
   * @return
   */
  private void sendMailing(Mail mail, String[] toUsers) {
    DynamicHttpClientCall addRecipientsCall = new DynamicHttpClientCall(config.getNamespace(), WebpowerMethod.addRecipientsSendMailing, config.getLocation());
    try {
      List<Map<String, String>> errors = (List<Map<String, String>>) handleResult(addRecipientsCall,
          buildAddRecipientsSendMailingMap(mail, toUsers), WebpowerMethod.addRecipientsSendMailing);
      List<String> excludes = new ArrayList<>();
      if (!errors.isEmpty()) {
        StringBuilder message = new StringBuilder();
        for (Map<String, String> map : errors) {
          String recipient = map.get("value");
          excludes.add(recipient);
          message.append(map.get("DMDmessage")).append(",");
        }
        log.error("以下邮址被标记为无效邮址:" + message.substring(0, message.length() - 1));
      }
    } catch (Exception e) {
      log.error("发送邮件失败，请检查相关参数");
    }
  }

  /**
   * 发送定时邮件
   * @param mail
   * @param toUsers
   */
  public void sendMailingScheduled(Mail mail, String[] toUsers){
    DynamicHttpClientCall addRecipientsCall = new DynamicHttpClientCall(config.getNamespace(), WebpowerMethod.addRecipients, config.getLocation());
    List<Map<String, String>> errors = (List<Map<String, String>>) handleResult(addRecipientsCall, buildAddRecipientsMap(mail, toUsers) ,WebpowerMethod.addRecipients);
    List<String> excludes = new ArrayList<>();
    if (!errors.isEmpty()) {
      StringBuilder message = new StringBuilder();
      for (Map<String, String> map : errors) {
        String recipient = map.get("value");
        excludes.add(recipient);
        message.append(map.get("DMDmessage")).append(",");
      }
      log.error("以下邮址被标记为无效邮址:" + message.substring(0, message.length() - 1));
    }
    DynamicHttpClientCall sendMailingScheduledCall = new DynamicHttpClientCall(config.getNamespace(), WebpowerMethod.sendMailingScheduled, config.getLocation());
    handleResult(sendMailingScheduledCall, buildSendMailingScheduledMap(mail), WebpowerMethod.sendMailingScheduled);

  }

  /**
   * 获取本次通道寄出的邮件   同时也能获取本次退订或垃圾投诉的邮件
   *
   * @param mail
   * @param status
   * @param recipients  通道寄出邮址
   */
  public void getMailingStatsDetail(Mail mail, int status, List<String> recipients) {
    DynamicHttpClientCall getRecipientsCall = new DynamicHttpClientCall(config.getNamespace(), WebpowerMethod.getRecipients, config.getLocation());
    Map<String, String> getRecipientsMap = new LinkedHashMap<>();
    getRecipientsMap.put("login", "<username>" + mail.getAccountName() + "</username><password>" + mail.getAccountPass() + "</password>");
    getRecipientsMap.put("campaignID", mail.getCampaignId());
    getRecipientsMap.put("fields", "<string>email</string>");
    String event = MailEvent.EVENT_REQUEST;
    int[] groupIds = null;
    if(status != 0){   // status = 0 查询通道寄出邮件
      switch (status) {
        case MailDetailStatus.DETAIL_UNSUBSCRIBE: // 退订
          groupIds = new int[] {49, 50};
          event = MailEvent.EVENT_UNSUBSCRIBE;
          break;
        case MailDetailStatus.DETAIL_SPAM: // 垃圾邮件举报
          groupIds = new int[] {52};
          event = MailEvent.EVENT_SPAM;
          break;
        default:
          groupIds = new int[] {60};
      }
      StringBuilder sb = new StringBuilder();
      for (int groupId : groupIds) {
        sb.append("<int>").append(groupId).append("</int>");
      }
      getRecipientsMap.put("inGroupIDs", sb.toString());
    }
    getRecipientsMap.put("notInGroupIDs", "<int></int>");
    getRecipientsMap.put("mailingIDs", "<int>" + mail.getMailingId() + "</int>");
    List<Map<String, String>> resultList = (List<Map<String, String>>) handleResult(getRecipientsCall, getRecipientsMap, WebpowerMethod.getRecipients);
    for (Map<String, String> resultMap : resultList) {
      for (Map.Entry<String, String> entry : resultMap.entrySet()) {
        if ("value".equals(entry.getKey())) {
          String email = entry.getValue();
          Map<String, Object> params = new ConcurrentHashMap<>();
          params.put("mailId", mail.getId());
          params.put("recipient", email);
          List<MailDetail> list = mailDetailService.findMailDetailsByCondition(params);
          if (!list.isEmpty()) {
            for (MailDetail md : list) {
              // 防止重复邮箱被错误标记为垃圾邮件或退订
              if (!md.getEvent().equals(MailEvent.EVENT_REPEAT_ADDRESS)) {
                if (status == 0) {
                  recipients.add(email);
                  md.setEvent(MailEvent.EVENT_DELIVER);
                  md.setStatus(MailDetailStatus.DETAIL_DELIVER);
                } else {
                  md.setEvent(event);
                  md.setStatus(status);
                  if (status == MailDetailStatus.DETAIL_UNSUBSCRIBE) {
                    md.setUnsubscribe(true);
                    md.setUnsubscribeTime(new Date());
                  }
                }
                mailDetailService.updateMailDetail(md);
              }
            }
          }
        }
      }
    }
  }

  /**
   * 获得本次邮件的软硬弹数据, 只能获取前一天的数据
   * @param mail
   * @param type
   * @param date
   */
  public void getMailingBounce(Mail mail, String type, Date date){
    DynamicHttpClientCall getMailingBounceCall = new DynamicHttpClientCall(config.getNamespace(), WebpowerMethod.getMailingBounce, config.getLocation());
    Map<String, String> getMailingBounceMap = new LinkedHashMap<>();
    getMailingBounceMap.put("login", "<username>" + mail.getAccountName() + "</username><password>" + mail.getAccountPass() + "</password>");
    getMailingBounceMap.put("campaignID", mail.getCampaignId());
    getMailingBounceMap.put("mailingID ", mail.getMailingId());
    getMailingBounceMap.put("field", "email");
    getMailingBounceMap.put("types", type);
    getMailingBounceMap.put("date", new SimpleDateFormat("yyyy-MM-dd").format(date));
    List<Map<String, String>> resultList = (List<Map<String, String>>)handleResult(getMailingBounceCall, getMailingBounceMap, WebpowerMethod.getMailingBounce);
    String event = null;
    Integer status = null;
    if(type.equals("soft")){
      event = MailEvent.EVENT_SOFT_BOUNCE;
      status = MailDetailStatus.DETAIL_SOFT_BOUNCE;
    }else {
      event = MailEvent.EVENT_BOUNCE;
      status = MailDetailStatus.DETAIL_BOUNCE;
    }
    for(Map<String, String> resultMap : resultList){
      String email = resultMap.get("field");
      Map<String, Object> params = new HashMap<>();
      params.put("mailId", mail.getId());
      params.put("recipient", email);
      List<MailDetail> list = mailDetailService.findMailDetailsByCondition(params);
      if(!list.isEmpty()){
        for(MailDetail md : list ){
          //防止重复邮箱被错误标记为退信或退订
          if(md.getEvent().equals(MailEvent.EVENT_DELIVER) || md.getEvent().equals(MailEvent.EVENT_REQUEST)){
            md.setEvent(event);
            md.setStatus(status);
            mailDetailService.updateMailDetail(md);
          }
        }
      }
    }
  }

  /**
   * 获得打开以及点击明细数据
   *
   * @param mail
   * @param types open,click
   */
  public void getOpenClickDetail(Mail mail, String types) {
    DynamicHttpClientCall getMailingResponseCall = new DynamicHttpClientCall(config.getNamespace(), WebpowerMethod.getMailingResponse, config.getLocation());
    Map<String, String> getMailingResponseMap = new LinkedHashMap<>();
    getMailingResponseMap.put("login", "<username>" + mail.getAccountName() + "</username><password>" + mail.getAccountPass() + "</password>");
    getMailingResponseMap.put("campaignID", mail.getCampaignId());
    getMailingResponseMap.put("mailingID", mail.getMailingId());
    getMailingResponseMap.put("types", types);
    getMailingResponseMap.put("field", "email");
    List<Map<String, String>> resultMap = (List<Map<String, String>>) handleResult(getMailingResponseCall, getMailingResponseMap, WebpowerMethod.getMailingResponse);
    for (Map<String, String> map : resultMap) {
      Map<String, Object> params = new HashMap<>();
      params.put("mailId", mail.getId());
      params.put("recipient", map.get("field"));
      if (MailEvent.EVENT_OPEN.equals(map.get("type"))) { // 打开
        String logDate = map.get("log_date");
        Date openTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
          openTime = sdf.parse(logDate);
        } catch (ParseException e) {
          e.printStackTrace();
        }
        List<MailDetail> details = mailDetailService.findMailDetailsByCondition(params);
        if (!details.isEmpty()) {
          for(MailDetail detail : details){
            if(MailEvent.EVENT_REPEAT_ADDRESS.equals(detail.getEvent())){
              continue;
            }
            if(detail.getOpen() == 0){  //还未打开
              detail.setOpen(1);
              detail.setLastOpenTime(openTime);
              detail.setUpdateTime(new Date());
              mailDetailService.updateMailDetail(detail);
            }else if(detail.getOpen() > 0 && detail.getLastOpenTime().getTime() < openTime.getTime()){
              detail.setOpen(detail.getOpen() + 1);
              detail.setLastOpenTime(openTime);
              detail.setUpdateTime(new Date());
              mailDetailService.updateMailDetail(detail);
            }
          }
        }
      } else if (MailEvent.EVENT_CLICK.equals(map.get("type"))) {
        List<MailDetail> details = mailDetailService.findMailDetailsByCondition(params);
        if (!details.isEmpty()) {
          /*MailDetail detail = details.get(0);
          MailUrlClick muc = new MailUrlClick(detail.getMailId(), detail.getMailDetailId(), null, detail.getRecipient(), 1, null, null);
          List<MailUrlClick> list = mailService.getMailUrlClickListByCondition(muc);
          if (list.isEmpty()) {
            mailService.addMailUrlClick(muc);
          }*/
        }
      }
    }
  }


  /**
   * 构造AddRecipient方法的map
   *
   * @param mail
   * @return
   */
  private Map<String, String> buildAddRecipientMap(Mail mail) {
    Map<String, String> addRecipientMap = new LinkedHashMap<String, String>();
    addRecipientMap.put("login", "<username>" + mail.getAccountName() + "</username><password>" + mail.getAccountPass() + "</password>");
    addRecipientMap.put("campaignID", mail.getCampaignId());
    int[] groupIds = mail.getGroupIds();
    StringBuilder sb = new StringBuilder();
    for (int groupId : groupIds) {
      sb.append("<int>").append(groupId).append("</int>");
    }
    addRecipientMap.put("groupIDs", sb.toString());
    StringBuilder recipient = new StringBuilder();
    /*if (StringUtil.htmlRemoveTag(mail.getContent()).contains("{$")) {
      Map<String, String> replaceData = getReplaceModule(mail, mail.getTo());
      if (!replaceData.isEmpty()) {
        for (Map.Entry<String, String> entry : replaceData.entrySet()) {
          recipient.append("<fields><name>" + entry.getKey() + "</name><value>" + entry.getValue() + "</value></fields>");
        }
      }
    }*/
    recipient.append("<fields><name>email</name><value>" + mail.getSendTo() + "</value></fields>");
    recipient.append("<fields><name>lang</name><value>" + mail.getUnsubscribeLanguage() + "</value></fields>");
    addRecipientMap.put("recipientData", recipient.toString());
    addRecipientMap.put("addDuplisToGroups", "true");
    addRecipientMap.put("overwrite", "true");

    return addRecipientMap;
  }

  private Map<String, String> buildAddRecipientsMap(Mail mail, String[] toUsers){
    Map<String, String> addRecipientsMap = new LinkedHashMap<String, String>();
    addRecipientsMap.put("login", "<username>" + mail.getAccountName() +"</username><password>" + mail.getAccountPass()+ "</password>");
    addRecipientsMap.put("campaignID", mail.getCampaignId());
    int[] groupIds = mail.getGroupIds();
    StringBuilder sb = new StringBuilder();
    for (int groupId : groupIds) {
      sb.append("<int>").append(groupId).append("</int>");
    }
    addRecipientsMap.put("groupIDs", sb.toString());
    String s = "lihongde@coolgua.com";
    StringBuffer buffers = new StringBuffer();
    for(String toUser : toUsers){
      buffers.append("<recipients><fields><name>email</name><value>" + toUser + "</value></fields></recipients>");
    }
    addRecipientsMap.put("recipientDatas", buffers.toString());
    addRecipientsMap.put("addDuplisToGroups", "true");
    addRecipientsMap.put("overwrite", "true");
    return addRecipientsMap;
  }

  /**
   * @param mail
   * @param toUsers
   * @return
   */
  private Map<String, String> buildAddRecipientsSendMailingMap(Mail mail, String[] toUsers) {
    Map<String, String> addRecipientsMap = new LinkedHashMap<String, String>();
    addRecipientsMap.put("login", "<username>" + mail.getAccountName() + "</username><password>" + mail.getAccountPass() + "</password>");
    addRecipientsMap.put("campaignID", mail.getCampaignId());
    addRecipientsMap.put("mailingID", mail.getMailingId());
    int[] groupIds = mail.getGroupIds();
    StringBuilder sb = new StringBuilder();
    for (int groupId : groupIds) {
      sb.append("<int>").append(groupId).append("</int>");
    }
    addRecipientsMap.put("groupIDs", sb.toString());
    StringBuffer buffers = new StringBuffer();
    for (String toUser : toUsers) {
      buffers.append("<recipients>");
     /* if (StringUtil.htmlRemoveTag(mail.getContent()).contains("{$")) {
        Map<String, String> replaceData = getReplaceModule(mail, toUser);
        if (!replaceData.isEmpty()) {
          for (Map.Entry<String, String> entry : replaceData.entrySet()) {
            buffers.append("<fields><name>" + entry.getKey() + "</name><value>" + entry.getValue() + "</value></fields>");
          }
        }
      }*/
      buffers.append("<fields><name>email</name><value>" + toUser + "</value></fields><fields><name>lang</name><value>" + mail.getUnsubscribeLanguage() + "</value></fields></recipients>");
    }
    addRecipientsMap.put("recipientDatas", buffers.toString());
    addRecipientsMap.put("addDuplisToGroups", "true");
    addRecipientsMap.put("overwrite", "true");
    return addRecipientsMap;
  }

  private Map<String, String> buildSendMailingScheduledMap(Mail mail){
    Map<String, String> sendMailingScheduledMap = new LinkedHashMap<String, String>();
    sendMailingScheduledMap.put("login", "<username>"+ mail.getAccountName() +"</username><password>" +mail.getAccountPass()+ "</password>");
    sendMailingScheduledMap.put("campaignID", mail.getCampaignId());
    sendMailingScheduledMap.put("mailingID", mail.getMailingId());
    sendMailingScheduledMap.put("sendDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mail.getSendTime()));
    sendMailingScheduledMap.put("isTest", "false");
    sendMailingScheduledMap.put("resultsEmail", "");
    int[] groupIds = mail.getGroupIds();
    StringBuilder sb = new StringBuilder();
    for (int groupId : groupIds) {
      sb.append("<int>").append(groupId).append("</int>");
    }
    sendMailingScheduledMap.put("groupIDs", sb.toString());
    sendMailingScheduledMap.put("langs", "<string>cn</string>");
    return sendMailingScheduledMap;
  }

  /**
   * 构造SendSingleMailing方法的map
   *
   * @param mail
   * @param mailingID
   * @param recipientID
   * @return
   */
  private Map<String, String> buildSendSingleMailingMap(Mail mail, String recipientID) {
    Map<String, String> sendSingleMailingMap = new LinkedHashMap<String, String>();
    sendSingleMailingMap.put("login", "<username>" + mail.getAccountName() + "</username><password>" + mail.getAccountPass() + "</password>");
    sendSingleMailingMap.put("campaignID", mail.getCampaignId());
    sendSingleMailingMap.put("mailingID", mail.getMailingId());
    sendSingleMailingMap.put("recipientID", recipientID);
    return sendSingleMailingMap;
  }

  /**
   * 构造createMailing方法的map
   *
   * @param mail
   * @return
   */
  private Map<String, String> buildCreateMailingMap(Mail mail) {
    Map<String, String> createMailingMap = new LinkedHashMap<String, String>();
    createMailingMap.put("login", "<username>" + mail.getAccountName() + "</username><password>" + mail.getAccountPass() + "</password>");
    createMailingMap.put("campaignID", mail.getCampaignId());
    createMailingMap.put("mailingName", mail.getSubject() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
    createMailingMap.put("subject", mail.getSubject());
    createMailingMap.put("fromName", mail.getSenderName());
    createMailingMap.put("senderID", "0");
    String content = mail.getContent();
    String unsubscribeLanguage = UnsubscribeLanguage.LANGUAGE_CN;
    if (mail.isUnsubscribe()) {
      if (mail.getUnsubscribeLanguage().equals(UnsubscribeLanguage.LANG_EN)){
        unsubscribeLanguage = UnsubscribeLanguage.LANGUAGE_EN;
      }else if (mail.getUnsubscribeLanguage().equals(UnsubscribeLanguage.LANG_JA)){
        unsubscribeLanguage = UnsubscribeLanguage.LANGUAGE_JA;
      }
      content += " <a href={$PLUGINLINK=unsubscribe} >" + unsubscribeLanguage + "</a>";
    }
    String html = "<![CDATA[<html><body>" + content + "</body></html>]]>";
    createMailingMap.put("lang", mail.getUnsubscribeLanguage()== null ? UnsubscribeLanguage.LANG_CN : mail.getUnsubscribeLanguage());
    createMailingMap.put("html", html);
    createMailingMap.put("preheader", "");
    return createMailingMap;
  }

  /**
   * @param mail
   * @return
   */
  private Map<String, String> buildAddGroupMap(Mail mail) {
    Map<String, String> addGroupMap = new LinkedHashMap<String, String>();
    addGroupMap.put("login", "<username>" + mail.getAccountName() + "</username><password>" + mail.getAccountPass() + "</password>");
    addGroupMap.put("campaignID", mail.getCampaignId());
    String groupName = mail.getTemplateId() == null ? "" : mail.getTemplateId();
    addGroupMap.put("group", "<name>" + groupName + "</name><is_test>" + config.isTest() + "</is_test><remarks></remarks>");
    return addGroupMap;
  }


  /**
   * 处理httpclient调用并返回结果
   *
   * @param dynamicHttpclientCall
   * @param patameterMap
   * @param method
   * @return
   * @throws SOAPException
   */
  private synchronized static Object handleResult(DynamicHttpClientCall dynamicHttpclientCall, Map<String, String> patameterMap, String method) {
    dynamicHttpclientCall.buildRequestData(patameterMap);
    Map<String, String> soapResult = new HashMap<>();
    int statusCode = dynamicHttpclientCall.invoke(patameterMap);
    String responseString = dynamicHttpclientCall.soapResponseData;
    if (statusCode == HttpStatus.SC_OK) {
      log.info("方法" + method + "调用成功！");
      responseString = responseString.replaceAll("(?<=>)\\s+(?=<)", "");
      SOAPMessage msg = SoapUtil.formatSoapString(responseString);
      SOAPBody body = null;
      try {
        body = msg.getSOAPBody();
      } catch (SOAPException e) {
        log.error("获得SOAPBody失败", e);
      }
      @SuppressWarnings("unchecked")
      Iterator<SOAPElement> iterator = body.getChildElements();
      if (method.equals(WebpowerMethod.addRecipientsSendMailing) || method.equals(WebpowerMethod.addRecipients)) {
        List<Map<String, String>> addRecipientsResult = new ArrayList<>();
        List<Map<String, String>> addRecipients = SoapUtil.printBody(iterator, null, addRecipientsResult);
        return addRecipients;
      }
      if (method.equals(WebpowerMethod.getMailingResponse) || method.equals(WebpowerMethod.getMailingBounce)) {
        List<Map<String, String>> responsesResult = new ArrayList<>();
        List<Map<String, String>> responses = SoapUtil.printBody(iterator, null, responsesResult);
        return responses;
      }
      if (method.equals(WebpowerMethod.getRecipients)) {
        List<Map<String, String>> recipientsResult = new ArrayList<>();
        List<Map<String, String>> result = SoapUtil.printBody(iterator, method, recipientsResult);
        return result;
      }
      Map<String, String> result = SoapUtil.printBody(iterator, null, soapResult);
      if (method.equals(WebpowerMethod.addGroup) || method.equals(WebpowerMethod.addRecipient) || method.equals(WebpowerMethod.createMailing)) {
        return result.get("id");
      }
    } else {
      log.error("方法" + method + "调用失败 ,错误代码： " + statusCode +  "\r\n参数：" + patameterMap + "\r\n结果:" + responseString);
      return responseString;
    }
    return soapResult;
  }
}

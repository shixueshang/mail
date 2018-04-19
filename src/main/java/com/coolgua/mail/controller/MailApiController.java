package com.coolgua.mail.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coolgua.common.util.CryptUtil;
import com.coolgua.mail.domain.Attachment;
import com.coolgua.mail.domain.BlackList;
import com.coolgua.mail.domain.DataSource;
import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.domain.ProviderConfig;
import com.coolgua.mail.domain.Template;
import com.coolgua.mail.exception.BadRequestException;
import com.coolgua.mail.service.BlackListService;
import com.coolgua.mail.service.DataSourceService;
import com.coolgua.mail.service.MailDetailService;
import com.coolgua.mail.service.MailService;
import com.coolgua.mail.service.ProviderConfigService;
import com.coolgua.mail.service.TemplateService;
import com.coolgua.mail.thirdpart.MailProvider;
import com.coolgua.mail.thirdpart.MailProviderFactory;
import com.coolgua.mail.util.AjaxJson;
import com.coolgua.mail.util.Constant;
import com.coolgua.mail.util.Constant.JSON_RESULT;
import com.coolgua.mail.util.Constant.MAIL_PROVIDER;
import com.coolgua.mail.util.Constant.MailDetailStatus;
import com.coolgua.mail.util.Constant.MailEvent;
import com.coolgua.mail.util.Constant.MailStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihongde on 2018/1/17 10:51.
 */
@RestController
@RequestMapping(value = "/mail")
public class MailApiController extends BaseController {

  private Logger logger = LoggerFactory.getLogger(MailApiController.class);
  @Value("${max_direct_number}")
  private int maxDirectNum;

  @Resource
  private TemplateService templateService;
  @Resource
  private MailService mailService;
  @Resource
  private MailDetailService mailDetailService;
  @Resource
  private BlackListService blackListService;
  @Resource
  private ProviderConfigService providerConfigService;
  @Resource
  private DataSourceService dataSourceService;

  /**
   * 获得某个组织下的邮件模板
   *
   * @param code 加密后的组织id
   */
  @RequestMapping(value = "/getTemplatesByOrgId", method = RequestMethod.GET)
  public AjaxJson getTemplatesByOrgId(@RequestParam String code) {
    String orgId = CryptUtil.getInstance().decryptAES(code);
    if (StringUtils.isEmpty(orgId)) {
      throw new BadRequestException("请指定组织机构");
    }
    Map<String, Object> params = new HashMap<>();
    params.put("orgId", orgId);
    List<Template> templates = templateService.findTemplatesByCondition(params);
    Map<String, Object> map = new HashMap<>();
    map.put("templates", templates);
    Map<String, Object> datasourceMap = new HashMap<>();
    datasourceMap.put("orgId", orgId);
    List<DataSource> dataSources = dataSourceService.findPageDataSources(datasourceMap, Constant.DEFAULT_PAGE, Integer.MAX_VALUE).getList();
    map.put("dataSources", dataSources);
    ProviderConfig config = providerConfigService.findConfigByOrgId(Integer.parseInt(orgId));
    map.put("providerId", config.getProviderId());
    return new AjaxJson(JSON_RESULT.OK, map);
  }

  /**
   * 获得模板
   */
  @RequestMapping(value = "/getTemplateById", method = RequestMethod.GET)
  public AjaxJson getTemplateById(@RequestParam String id) {
    id = CryptUtil.getInstance().decryptAES(id);
    if (StringUtils.isEmpty(id)) {
      throw new BadRequestException("参数错误,请检查之后重试");
    }
    Template template = templateService.getTemplate(id);
    List<Attachment> attachments = templateService.findAttachsByTemplateId(template.getId());
    template.setAttachments(attachments);
    return new AjaxJson(JSON_RESULT.OK, template);
  }


  /**
   * 邮件发送接口
   */
  @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
  public AjaxJson sendMail(@RequestBody String json) {
    AjaxJson result = new AjaxJson(JSON_RESULT.OK);
    JSONObject jsonObject = JSON.parseObject(json);
    Template template = null;
    String templateId = jsonObject.getString("templateId");
    String sendTo = jsonObject.getString("sendTo");
    if (StringUtils.isBlank(templateId)) {   //没有选择任何模版
      ObjectMapper mapper = new ObjectMapper();
      try {
        template = mapper.readValue(json, Template.class);
        sendTo = template.getSendTo();
      } catch (IOException e) {
        logger.error("读取参数失败", e);
        e.printStackTrace();
      }
    } else {
      template = templateService.getTemplate(templateId);
      List<Attachment> attachments = templateService.findAttachsByTemplateId(templateId);
      template.setAttachments(attachments);
    }

    Mail mail = new Mail();
    mail.setChannelRequest(0);
    mail.setChannelExclusion(0);
    sendTo = this.getSendTo(sendTo);
    mail.setSendTo(this.getSendTo(sendTo));
    mail.setTotal(sendTo.split(",").length);
    mail = super.templateToMail(template, mail);

    ProviderConfig config = providerConfigService.findConfigByOrgId(template.getOrgId());
    mail.setAccountName(config.getAccountName());
    mail.setAccountPass(config.getAccountPass());

    //获得黑名单列表
    List<String> blackList = new ArrayList<>();
    List<BlackList> bls = blackListService.findPageBlackList(template.getOrgId(), null, Constant.DEFAULT_PAGE, Integer.MAX_VALUE).getList();
    for (BlackList bl : bls) {
      blackList.add(bl.getRecipient());
    }
    mail.setStatus(MailStatus.STATUS_TIMED);
    if(template.getSendTime() != null){
      mail.setSendTime(template.getSendTime());
    }else{
      mail.setSendTime(new Date());
    }
    mail.setCreateTime(new Date());
    mailService.addMail(mail);
    List<String> validAddress = super.filterInvalidAddress(sendTo.split(","), mail, blackList);
    mail.setToList(validAddress);

    Object sendResult = true;

    int submitFails = 0;

    List<MailDetail> mailDetails = mail.getDetails();
    //如果有效邮址大于可直接发送的最大邮址数,则通过任务调度来发送,否则直接发送
    if (validAddress.size() > maxDirectNum) {
      mail.setSendTime(template.getSendTime());
      for(MailDetail mailDetail : mailDetails){
        mailDetailService.addMailDetail(mailDetail);
      }
    } else {
      MailProvider webpowerMailProvider = MailProviderFactory.getMailProvider(MAIL_PROVIDER.WEBPOWER);
      MailProvider sendCloudMailProvider = MailProviderFactory.getMailProvider(MAIL_PROVIDER.SENDCLOUD);
      try {
        if (mail.getProviderId() == MAIL_PROVIDER.WEBPOWER) {
          sendResult = webpowerMailProvider.sendMail(mail);
          for (MailDetail detail : mailDetails) {
            if (sendResult instanceof Boolean) {
              detail.setStatus(MailDetailStatus.DETAIL_REQUEST);
              detail.setEvent(MailEvent.EVENT_REQUEST);
            } else {
              detail.setStatus(MailDetailStatus.DETAIL_SUBMIT_FAILURE);
              detail.setEvent(MailEvent.EVENT_SUBMIT_FAILURE);
            }
          }
        } else {
          sendResult = sendCloudMailProvider.sendMail(mail);
        }
      } catch (Exception e) {
        String message = "发送邮件失败";
        logger.error(message, e);
        result.setCode(JSON_RESULT.SERVER_ERROR);
        result.setMessage(message);
        return result;
      }
      if (sendResult instanceof Boolean) {
        mail.setSendTime(new Date());
        mail.setStatus(MailStatus.STATUS_SENT);
      }
     for(MailDetail detail : mailDetails){
       if(detail.getStatus() == MailDetailStatus.DETAIL_SUBMIT_FAILURE){
         submitFails ++;
       }
       mailDetailService.addMailDetail(detail);
     }
    }
    if (sendResult instanceof String) {
      result.setCode(JSON_RESULT.SERVER_ERROR);
      result.setMessage(String.valueOf(sendResult));
      return result;
    }
    mail.setUpdateTime(new Date());
    mail.setRequest(mail.getTotal() - submitFails);
    mailService.updateMail(mail);
    return result;
  }

  private String getSendTo(String to) {
    String[] mails = to.split("[; ,，；　]|(\r\n)");
    int toSize = mails.length;
    StringBuilder sb = new StringBuilder();
    for (String email : mails) {
      if (StringUtils.isBlank(email)) {
        toSize--;
        continue;
      }
      sb.append(email);
      sb.append(",");
    }
    sb.deleteCharAt(sb.length() - 1);

    return sb.toString();
  }

}

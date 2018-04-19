package com.coolgua.mail.controller;

import static com.coolgua.mail.util.Constant.DEFAULT_PAGE;
import static com.coolgua.mail.util.Constant.LANGUAGE;
import static com.coolgua.mail.util.Constant.MAIL_FIELD;
import static com.coolgua.mail.util.Constant.mailTypeMap;

import com.coolgua.common.domain.User;
import com.coolgua.common.service.RedisService;
import com.coolgua.common.util.ExcelUtil;
import com.coolgua.common.util.SessionUtil;
import com.coolgua.mail.domain.Attachment;
import com.coolgua.mail.domain.BlackList;
import com.coolgua.mail.domain.DataSource;
import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.domain.ProviderConfig;
import com.coolgua.mail.domain.Template;
import com.coolgua.mail.enums.DataSourceType;
import com.coolgua.mail.enums.TemplateStatus;
import com.coolgua.mail.exception.BadRequestException;
import com.coolgua.mail.exception.FailedReqeustException;
import com.coolgua.mail.service.BlackListService;
import com.coolgua.mail.service.DataSourceService;
import com.coolgua.mail.service.MailDetailService;
import com.coolgua.mail.service.MailService;
import com.coolgua.mail.service.ProviderConfigService;
import com.coolgua.mail.service.TemplateService;
import com.coolgua.mail.util.AjaxJson;
import com.coolgua.mail.util.Constant;
import com.coolgua.mail.util.Constant.JSON_RESULT;
import com.coolgua.mail.util.Constant.MailDetailStatus;
import com.coolgua.mail.util.Constant.MailStatus;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihongde on 2018/1/26 17:15.
 */
@RestController
@RequestMapping(value = "/interface/template/{fp}/{orgId}/{userId}")
public class TemplateController extends BaseController {

  Logger logger = LoggerFactory.getLogger(TemplateController.class);

  @Value("${REDIS_USER_SESSION_KEY:REDIS_USER_SESSION}")
  private String REDIS_USER_SESSION_KEY;
  @Value("${SSO_BASE_URL}")
  private String SSO_BASE_URL;
  @Resource
  private TemplateService templateService;
  @Resource
  private MailService mailService;
  @Resource
  private MailDetailService mailDetailService;
  @Resource
  private ProviderConfigService providerConfigService;
  @Resource
  private DataSourceService dataSourceService;
  @Resource
  private RedisService redisService;
  @Resource
  private BlackListService blackListService;

  /**
   * 获得历史邮件模板,数据源类型,退订语言,邮件类型
   * @param orgId
   * @return
   */
  @RequestMapping(value = "/list/history", method = RequestMethod.GET)
  public AjaxJson listHistoryTemplate(@PathVariable Integer orgId){
    Map<String, Object> params = new HashMap<>();
    params.put("orgId", orgId);
    Map<String, Object> result = new HashMap<>();
    result.put("historyTemplate", templateService.findTemplatesByCondition(params));
    result.put("dataSourceType", DataSourceType.getDataSourceType());
    ProviderConfig config = providerConfigService.findConfigByOrgId(orgId);
    if(config == null){
      throw new BadRequestException("还未配置邮件帐号信息");
    }
    result.put("unsubscribeLanguage", LANGUAGE);
    result.put("mailType", mailTypeMap);
    return new AjaxJson(JSON_RESULT.OK, result);
  }

  /**
   * 获得外部数据源列表
   * @param orgId
   * @return
   */
  @RequestMapping(value = "/getExternalDataSources", method = RequestMethod.GET)
  public AjaxJson getExternalDataSources(@PathVariable Integer orgId){
    Map<String, Object> params = new HashMap<>();
    params.put("orgId", orgId);
    return new AjaxJson(JSON_RESULT.OK, dataSourceService.findPageDataSources(params, DEFAULT_PAGE, Integer.MAX_VALUE));
  }

  /**
   * 获取数据源中邮箱字段
   * @param id
   * @return
   */
  @RequestMapping(value = "/getDataSource/{id}", method = RequestMethod.GET)
  public AjaxJson getDataSource(@PathVariable String id){
    DataSource dataSource = dataSourceService.getDataSource(id);
    if(dataSource == null){
      throw new BadRequestException("未找到邮箱关键字");
    }
    String fields = dataSource.getFields();
    if(StringUtils.isBlank(fields)){
      throw new BadRequestException("未找到邮箱关键字");
    }
    String[] filedArr = fields.split(",");
    List<String> list = new ArrayList<>();
    for(String field : filedArr){
      if(field.indexOf(MAIL_FIELD) > -1){
        list.add(field);
      }
    }
    if(CollectionUtils.isEmpty(list)){
      throw new BadRequestException("未找到邮箱关键字");
    }
    return new AjaxJson(JSON_RESULT.OK, list);
  }

  /**
   * 获得模板信息
   * @param id
   * @return
   */
  @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
  public AjaxJson getTemplate(@PathVariable String id){
    return new AjaxJson(JSON_RESULT.OK, templateService.getTemplate(id));
  }

  /**
   * 获得模板列表
   * @return
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public AjaxJson listTemplate(@PathVariable Integer orgId, Template template, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime){
    Map<String, Object> result = new HashMap<>();
    Map<String, Object> params = new HashMap<>();
    params.put("orgId", orgId);
    Integer status = template.getStatus();
    if(status == null || TemplateStatus.getTemplateStatus(status) == null){
      status = TemplateStatus.ONGOING.ordinal();
      template.setStatus(status);
    }
    params.put("status", status);
    String name = template.getName();
    if(StringUtils.isNotBlank(name)){
      params.put("name", name);
    }
    Integer mailType = template.getMailType();
    if(mailType != null){
      params.put("mailType", mailType);
    }
    Integer dsType = template.getDsType();
    if(dsType != null){
      params.put("dsType", dsType);
    }
    if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
      params.put("startTime", startTime);
      params.put("endTime", endTime);
    }
    result.put("page", templateService.findPageTemplates(params, page, size));
    result.put("template", template);
    result.put("startTime", startTime);
    result.put("endTime", endTime);
    List<DataSourceType> dataSourceTypes = new ArrayList<DataSourceType>();
    dataSourceTypes.add(DataSourceType.LOCAL);
    dataSourceTypes.add(DataSourceType.EXTERNAL);
    result.put("dataSourceTypes", dataSourceTypes);

    return new AjaxJson(JSON_RESULT.OK, result);
  }

  /**
   * 添加模板
   * @param template
   * @param attachments
   * @return
   */
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public AjaxJson addTemplate(@PathVariable Integer orgId, @PathVariable String userId, @PathVariable String fp, Template template,
      @RequestParam(value = "attachs[]", required = false) String[] attachments, BindingResult bindingResult){
    try{
      if(bindingResult.hasErrors()){
        logger.error("there has error : {}", bindingResult);
      }

      String token = SessionUtil.createToken(request, fp);
      String userJson = redisService.get(REDIS_USER_SESSION_KEY + ":" + token);
      User user = SessionUtil.getUserByToken(request, SSO_BASE_URL, token);

      ProviderConfig config = providerConfigService.findConfigByOrgId(orgId);
      template.setProviderId(config.getProviderId());
      template.setStatus(TemplateStatus.ONGOING.ordinal());
      template.setOrgId(orgId);
      template.setCreator(userId);
      template.setCreatorName(user.getAccountName());
      template.setCreateTime(new Date());
      templateService.addTemplate(template);

      if(attachments != null && attachments.length > 0){
        for(String attact : attachments){
          Attachment attachment = new Attachment();
          attachment.setTemplateId(template.getId());
          attachment.setFileName(attact.split(",")[0]);
          attachment.setFilePath(attact.split(",")[1]);
          templateService.addAttachment(attachment);
        }
      }

      //如果是上传本地数据源，则需要解析文件并发送邮件
      if(template.getDsType() == DataSourceType.LOCAL.ordinal()){
        parseFileAndSendMail(template);
      }

    }catch (Exception e){
      String message = "添加邮件模板失败";
      logger.error(message, e);
      throw new FailedReqeustException(message);
    }
    return new AjaxJson(JSON_RESULT.OK);
  }


  /**
   * 解析excel拿到邮箱数据。发邮件
   * @param template
   */
  private void parseFileAndSendMail(Template template) throws Exception{
    List<String> excelCols = ExcelUtil.getExcelTitles(template.getDsFilePath());
    List<Integer> lens = new ArrayList<Integer>();
    for (String string : excelCols) {
      lens.add(2000);
    }
    List<List<String>> excelData = ExcelUtil.getExcelData(template.getDsFilePath(), excelCols, lens);
    DataSource dataSource = new DataSource();
    String fields = StringUtils.join(excelCols, ",");
    dataSource.setOrgId(template.getOrgId());
    dataSource.setName(template.getName());
    dataSource.setDsType(DataSourceType.LOCAL.ordinal());
    dataSourceService.addDataSource(dataSource);

    String mobileField = template.getMailField();
    List<String> sendTo = new ArrayList<>();
    Mail mail = new Mail();
    mail.setChannelRequest(0);
    mail.setChannelExclusion(0);
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
    mail.setSendTime(new Date());
    mail.setCreateTime(new Date());

    int mailFieldIndex = 0;
    for (int i = 0; i < excelCols.size(); i++) {
      String string = excelCols.get(i);
      if(mobileField.equals(string)){
        mailFieldIndex = i;
        break;
      }
    }
    for(List<String> list : excelData){
      String mailFieldValue = list.get(mailFieldIndex);
      sendTo.add(mailFieldValue);
    }
    mail.setSendTo(StringUtils.join(sendTo, ","));

    mailService.addMail(mail);
    List<String> validAddress = super.filterInvalidAddress(mail.getSendTo().split(","), mail, blackList);
    mail.setToList(validAddress);
    List<MailDetail> mailDetails = mail.getDetails();
    for(MailDetail detail : mailDetails){
      mailDetailService.addMailDetail(detail);
    }
  }

  /**
   * 任务概览
   * @param templateId
   * @return
   */
  @RequestMapping(value = "/overview/{id}", method = RequestMethod.GET)
  public AjaxJson overview(@PathVariable(value = "id") String templateId){
    Map<String, Object> result = new HashMap<>();
    try{
      List<Map<String, Object>> mails = mailService.findMailsByTemplate(templateId);
      int templateTotal = 0, notSubmit = 0, submitFailure = 0, request = 0, deliver = 0, invalidAddress = 0, openTotal = 0, uniqueOpen = 0;
      DecimalFormat df = new DecimalFormat("0.00");
      for(Map<String, Object> map : mails){
        int _notSubmit = 0, _submitFailure = 0, _submitSuccess = 0;
        String _notSubmitPer, _submitFailurePer, _submitSuccessPer;

        Map<String, Object> notSubmitParam = new HashMap<>();
        notSubmitParam.put("templateId", templateId);
        notSubmitParam.put("mailId", map.get("mail_id"));
        notSubmitParam.put("detailStatus", MailDetailStatus.DETAIL_INIT);
        _notSubmit = mailDetailService.findMailDetailsByCondition(notSubmitParam).size();

        Map<String, Object> submitFailureParam = new HashMap<>();
        submitFailureParam.put("templateId", templateId);
        submitFailureParam.put("mailId", map.get("mail_id"));
        submitFailureParam.put("detailStatus", MailDetailStatus.DETAIL_SUBMIT_FAILURE);
        _submitFailure = mailDetailService.findMailDetailsByCondition(submitFailureParam).size();

        Map<String, Object> submitSuccessParam = new HashMap<>();
        submitSuccessParam.put("templateId", templateId);
        submitSuccessParam.put("mailId", map.get("mail_id"));
        int _total = mailDetailService.findMailDetailsByCondition(submitSuccessParam).size();
        _submitSuccess = _total - _notSubmit - _submitFailure;

        templateTotal += Integer.valueOf(map.get("total").toString());
        notSubmit += _notSubmit;
        submitFailure += _submitFailure;
        request += Integer.valueOf(map.get("request").toString());
        deliver += Integer.valueOf(map.get("deliver").toString());
        invalidAddress += Integer.valueOf(map.get("invalid_address").toString());
        openTotal += Integer.valueOf(map.get("open_total").toString());
        uniqueOpen += Integer.valueOf(map.get("unique_open").toString());
        map.put("not_submit", _notSubmit);
        map.put("not_submit_percent", _total == 0 ? "0.00%" : (df.format((float)_notSubmit / _total * 100) + "%"));
        map.put("submit_failure", _submitFailure);
        map.put("submit_failure_percent", _total == 0 ? "0.00%" : (df.format((float)_submitFailure / _total * 100) + "%"));
        map.put("submit_success", _submitSuccess);
        map.put("submit_success_percent", _total == 0 ? "0.00%" : (df.format((float)_submitSuccess / _total * 100) + "%"));
      }
      result.put("mails", mails);
      String notSubmitPer = templateTotal == 0 ? "0.00%" : (df.format((float)notSubmit / templateTotal * 100) + "%");
      String submitFailurePer = templateTotal == 0 ? "0.00%" : (df.format((float)submitFailure / templateTotal * 100) + "%");
      Map<String, Object> templateTotalMap = new HashMap<>();
      templateTotalMap.put("templateTotal", templateTotal);
      templateTotalMap.put("notSubmit", notSubmit);
      templateTotalMap.put("notSubmitPer", notSubmitPer);
      templateTotalMap.put("submitSuccess", templateTotal - notSubmit - submitFailure);
      String submitSuccessPer = templateTotal == 0 ? "0.00%" : (df.format((float)(templateTotal - notSubmit - submitFailure) / templateTotal * 100) + "%");
      templateTotalMap.put("submitSuccessPer", submitSuccessPer);
      templateTotalMap.put("submitFailure", submitFailure);
      templateTotalMap.put("submitFailurePer", submitFailurePer);
      templateTotalMap.put("request", request);
      templateTotalMap.put("deliver", deliver);
      templateTotalMap.put("invalidAddress", invalidAddress);
      templateTotalMap.put("openTotal", openTotal);
      templateTotalMap.put("uniqueOpen", uniqueOpen);
      result.put("templateTotalMap", templateTotalMap);

      List<Map<String, Object>> chartData = mailService.getChartData(templateId);
      List<Map<String, Object>> notSubmission = mailService.getNotSubmit(templateId);
      List<Map<String, Object>> submitssionFailure = mailService.getSubmitFailure(templateId);
      List<Map<String, Object>> submissionSuccess = mailService.getSubmitSuccess(templateId);
      for(Map<String, Object> cdata : chartData){
        String sendDate = cdata.get("send_date").toString();
        for(Map<String, Object> nsmap : notSubmission){
          if(sendDate.equals(nsmap.get("send_date"))){
            cdata.put("not_submit", nsmap.get("submission"));
          }
        }
        for(Map<String, Object> sfmap : submitssionFailure){
          if(sendDate.equals(sfmap.get("send_date"))){
            cdata.put("submit_failure", sfmap.get("submission"));
          }
        }
        for(Map<String, Object> ssmap : submissionSuccess){
          if(sendDate.equals(ssmap.get("send_date"))){
            cdata.put("submit_success", ssmap.get("submission"));
          }
        }
      }
      result.put("chartData", chartData);
    }catch (Exception e){
      String message = "获取邮件概览失败";
      logger.error(message, e);
      throw new FailedReqeustException(message);
    }

    return new AjaxJson(JSON_RESULT.OK, result);
  }

  /**
   * 复制模板
   * @param template
   * @param orgId
   * @param userId
   * @return
   */
  @RequestMapping(value = "/copy", method = RequestMethod.POST)
  public AjaxJson copy(Template template, @PathVariable Integer orgId, @PathVariable String userId){
      try{
        template.setId(null);
        template.setStatus(TemplateStatus.ONGOING.ordinal());
        template.setOrgId(orgId);
        template.setCreator(userId);
        template.setCreateTime(new Date());
        template.setMender(userId);
        template.setUpdateTime(new Date());
        templateService.addTemplate(template);
      }catch (Exception e){
        throw new FailedReqeustException("复制邮件模板失败");
      }

      return new AjaxJson(JSON_RESULT.OK);
  }

  /**
   * 关闭 开启 删除任务
   * @param id
   * @param status
   * @return
   */
  @RequestMapping(value = "/single/{id}", method = RequestMethod.POST)
  public AjaxJson single(@PathVariable String id, @RequestParam(value = "status") int status){
    Template template = templateService.getTemplate(id);
    template.setStatus(status);
    try{
      templateService.updateTemplate(template);
    }catch (Exception e){
      if(status == TemplateStatus.ONGOING.ordinal())
        throw new FailedReqeustException("开启邮件任务失败");
      if(status == TemplateStatus.CLOSED.ordinal())
        throw new FailedReqeustException("关闭邮件任务失败");
      if(status == TemplateStatus.DELETED.ordinal())
        throw new FailedReqeustException("删除邮件任务失败");
    }
    return new AjaxJson(JSON_RESULT.OK);
  }

  /**
   * 批量 关闭、开启、删除 任务
   * @param ids
   * @param status
   * @param userId
   * @return
   */
  @RequestMapping(value = "/batch", method = RequestMethod.POST)
  public AjaxJson batch(@RequestParam(value = "ids[]") String[] ids, @RequestParam(value = "status") int status, @PathVariable String userId){
    Map<String, Object> params = new HashMap<>();
    params.put("ids", Arrays.asList(ids));
    params.put("status", status);
    params.put("updateTime", new Date());
    params.put("mender", userId);
    try{
      templateService.batchUpdateTemplates(params);
    }catch (Exception e){
      if(status == TemplateStatus.ONGOING.ordinal())
        throw new FailedReqeustException("批量开启邮件任务失败");
      if(status == TemplateStatus.CLOSED.ordinal())
        throw new FailedReqeustException("批量关闭邮件任务失败");
      if(status == TemplateStatus.DELETED.ordinal())
        throw new FailedReqeustException("批量删除邮件任务失败");
    }
    return new AjaxJson(JSON_RESULT.OK);
  }

}

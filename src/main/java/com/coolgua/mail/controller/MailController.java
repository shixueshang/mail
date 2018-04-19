package com.coolgua.mail.controller;

import com.coolgua.mail.service.MailDetailService;
import com.coolgua.mail.service.MailService;
import com.coolgua.mail.util.AjaxJson;
import com.coolgua.mail.util.Constant;
import com.coolgua.mail.util.Constant.JSON_RESULT;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihongde on 2018/1/17 10:37.
 */
@RestController
@RequestMapping(value = "/interface/mail/{fp}/{orgId}/{userId}")
public class MailController extends BaseController {

  Logger logger = LoggerFactory.getLogger(MailController.class);

  @Value("${shardingCategory.shardingTotalCount}")
  private int shardingCount;

  @Resource
  private MailService mailService;
  @Resource
  private MailDetailService mailDetailService;

  /**
   * 发送中的邮件
   * @param name
   * @param mailType
   * @param recipient
   * @param status 详细状态
   * @return
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public AjaxJson list(@PathVariable Integer orgId, @RequestParam(required = false) String templateName, @RequestParam(required = false) String mailType,
      @RequestParam(required = false) String recipient, @RequestParam(required = false) String status, @RequestParam(required = false) String templateId, @RequestParam(required = false) String mailId){
    Map<String ,Object> params = new HashMap<>();
    params.put("orgId", orgId);
    if(StringUtils.isNotBlank(templateName)){
      params.put("templateName", templateName);
    }
    if(StringUtils.isNotBlank(mailType)){
      params.put("mailType", mailType);
    }
    if(StringUtils.isNotBlank(recipient)){
      params.put("recipient", recipient);
    }
    if(StringUtils.isNotBlank(status)){
      params.put("detailStatus", status);
    }
    if(StringUtils.isNotBlank(templateId)){
      params.put("templateId", templateId);
    }
    if(StringUtils.isNotBlank(mailId)){
      params.put("mailId", mailId);
    }

    Map<String, Object> result = new HashMap<>();
    result.put("page", mailDetailService.findPageMailDetails(params, page, size));
    result.put("mailDetailStatus", Constant.mailDetailStatusMap);
    return new AjaxJson(JSON_RESULT.OK, result);
  }



}

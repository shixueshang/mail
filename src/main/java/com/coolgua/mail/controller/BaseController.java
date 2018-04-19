package com.coolgua.mail.controller;

import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.domain.Template;
import com.coolgua.mail.util.Constant;
import com.coolgua.mail.util.Constant.MAIL_PROVIDER;
import com.coolgua.mail.util.Constant.MailDetailStatus;
import com.coolgua.mail.util.Constant.MailEvent;
import com.coolgua.mail.util.DateEditor;
import com.coolgua.mail.util.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by lihongde on 2018/1/24 22:21.
 */
public class BaseController {

  @Value("${MailRecipientMaxLength}")
  private int maxAddressLength;
  @Value("${shardingCategory.shardingTotalCount}")
  private int shardingCount;

  protected HttpServletRequest request;
  protected HttpServletResponse response;
  protected HttpSession session;
  protected String basePath;
  protected Integer page;
  protected Integer size;

  public BaseController() {
    super();
  }


  @ModelAttribute
  protected void initRequestResponseSession(HttpServletRequest request, HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    this.request = request;
    this.response = response;
    this.session = request.getSession();

    basePath = request.getScheme() + "://" + request.getServerName() + (80 == request.getServerPort() || 443 == request.getServerPort() ? "" : ":" + request.getServerPort());
    String page_str = request.getParameter("page");
    String size_str = request.getParameter("size");


    if (StringUtils.isNotEmpty(page_str)) {
      page = Integer.parseInt(page_str);
    }else{
      page = Constant.DEFAULT_PAGE;
    }
    if (StringUtils.isNotEmpty(size_str)) {
      size = Integer.parseInt(size_str);
    }else{
      size = Constant.DEFAULT_PAGE_SIZE;
    }

  }

  /**
   * 定义日期类型的数据绑定
   *
   * @param binder
   * @throws Exception
   */
  @InitBinder
  public void initBinder(WebDataBinder binder) throws Exception {
    binder.registerCustomEditor(Date.class, new DateEditor());
  }


  protected Mail templateToMail(Template template, Mail mail) {
    mail.setOrgId(template.getOrgId());
    mail.setTemplateId(template.getId());
    mail.setSender(template.getSenderAddress());
    mail.setSenderName(template.getSenderName());
    mail.setReplyAddress(template.getReplyAddress());
    mail.setSubject(template.getSubject());
    mail.setContent(template.getContent());
    mail.setProviderId(template.getProviderId());
    if (!CollectionUtils.isEmpty(template.getAttachments())) {
      mail.setHasAttachment(true);
    }
    if (template.isScheduled()) {
      mail.setScheduled(true);
      mail.setSendTime(template.getSendTime());
    }
    if (template.getProviderId() == MAIL_PROVIDER.WEBPOWER) {
      mail.setCampaignId(template.getCampaignId());
    }
    return mail;
  }


  /**
   * 过滤无效以及重复邮址, 过滤黑名单, 数据分片
   *
   * @return 过滤之后的邮址
   */
  protected List<String> filterInvalidAddress(String[] recipients, Mail mail, List<String> blackLists) {
    List<MailDetail> details = new ArrayList<>();
    Set<String> recipientList = new HashSet<String>(recipients.length);
    int repeatAddress = 0, invalidAddress = 0;
    for (int i = 0; i < recipients.length; i++) {
      int shardingItem = i % shardingCount;
      String recipient = recipients[i].trim();
      String event = null;
      Integer status = null;
      MailDetail detail = new MailDetail(mail.getId(), mail.getProviderId(), MailDetailStatus.DETAIL_INIT, MailEvent.EVENT_INIT, mail.isScheduled(), shardingItem, recipient, null, 0);
      // 将超长的邮件地址截取并归为错误
      if (recipient.length() > maxAddressLength || !StringUtil.isValidEmail(recipient) || blackLists.contains(recipient)) {
        event = MailEvent.EVENT_INVALID_ADDRESS;
        status = MailDetailStatus.DETAIL_INVALID_ADDRESS;
        invalidAddress++;
        recipient = recipient.substring(0, maxAddressLength);
      } else if (!recipientList.add(recipient)) {
        event = MailEvent.EVENT_REPEAT_ADDRESS;
        status = MailDetailStatus.DETAIL_REPEAT_ADDRESS;
        repeatAddress++;
      }
      if (event != null) {
        detail.setEvent(event);
        detail.setStatus(status);
      }
      details.add(detail);
    }
    mail.setInvalidAddress(invalidAddress);
    mail.setRepeatAddress(repeatAddress);
    mail.setRequest(recipientList.size());  //寄出数 = 总数 - 格式错误 - 邮址重复
    mail.setDetails(details);
    return new ArrayList<>(recipientList);
  }


}

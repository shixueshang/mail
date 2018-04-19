package com.coolgua.mail.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by lihongde on 2018/1/10 17:01.
 */
public class Mail {

  private String id;
  private Integer orgId;
  private String sender;
  private String senderName;
  private String replyAddress;
  private String sendTo;
  private String subject;
  private String content;
  private boolean hasAttachment;
  private String templateId;
  private boolean scheduled;
  private Integer status;
  private Integer total;
  private Integer request;
  private Integer channelRequest;
  private Integer deliver;
  private Integer bounce;
  private Integer softBounce;
  private Integer invalidAddress;
  private Integer spam;
  private Integer repeatAddress;
  private Integer openTotal;
  private Integer uniqueOpen;
  private Integer clickTotal;
  private Integer clickUnique;
  private Integer channelExclusion;
  private boolean hasExclusion;
  private boolean unsubscribe;
  private String unsubscribeLanguage;
  private Integer unsubscribeCount;
  private String replaceModule;
  private Integer providerId;
  private String campaignId;
  private String mailingId;
  private Date sendTime;
  private String creator;
  private Date createTime;
  private Date updateTime;

  private int[] groupIds;
  private String accountName;
  private String accountPass;
  private List<String> toList;
  private List<MailDetail> details;

  private String templateName;
  private String mailType;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getOrgId() {
    return orgId;
  }

  public void setOrgId(Integer orgId) {
    this.orgId = orgId;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public String getReplyAddress() {
    return replyAddress;
  }

  public void setReplyAddress(String replyAddress) {
    this.replyAddress = replyAddress;
  }

  public String getSendTo() {
    return sendTo;
  }

  public void setSendTo(String sendTo) {
    this.sendTo = sendTo;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public boolean isHasAttachment() {
    return hasAttachment;
  }

  public void setHasAttachment(boolean hasAttachment) {
    this.hasAttachment = hasAttachment;
  }

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public boolean isScheduled() {
    return scheduled;
  }

  public void setScheduled(boolean scheduled) {
    this.scheduled = scheduled;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public Integer getRequest() {
    return request;
  }

  public void setRequest(Integer request) {
    this.request = request;
  }

  public Integer getChannelRequest() {
    return channelRequest;
  }

  public void setChannelRequest(Integer channelRequest) {
    this.channelRequest = channelRequest;
  }

  public Integer getDeliver() {
    return deliver;
  }

  public void setDeliver(Integer deliver) {
    this.deliver = deliver;
  }

  public Integer getBounce() {
    return bounce;
  }

  public void setBounce(Integer bounce) {
    this.bounce = bounce;
  }

  public Integer getSoftBounce() {
    return softBounce;
  }

  public void setSoftBounce(Integer softBounce) {
    this.softBounce = softBounce;
  }

  public Integer getInvalidAddress() {
    return invalidAddress;
  }

  public void setInvalidAddress(Integer invalidAddress) {
    this.invalidAddress = invalidAddress;
  }

  public Integer getSpam() {
    return spam;
  }

  public void setSpam(Integer spam) {
    this.spam = spam;
  }

  public Integer getRepeatAddress() {
    return repeatAddress;
  }

  public void setRepeatAddress(Integer repeatAddress) {
    this.repeatAddress = repeatAddress;
  }

  public Integer getOpenTotal() {
    return openTotal;
  }

  public void setOpenTotal(Integer openTotal) {
    this.openTotal = openTotal;
  }

  public Integer getUniqueOpen() {
    return uniqueOpen;
  }

  public void setUniqueOpen(Integer uniqueOpen) {
    this.uniqueOpen = uniqueOpen;
  }

  public Integer getClickTotal() {
    return clickTotal;
  }

  public void setClickTotal(Integer clickTotal) {
    this.clickTotal = clickTotal;
  }

  public Integer getClickUnique() {
    return clickUnique;
  }

  public void setClickUnique(Integer clickUnique) {
    this.clickUnique = clickUnique;
  }

  public Integer getChannelExclusion() {
    return channelExclusion;
  }

  public void setChannelExclusion(Integer channelExclusion) {
    this.channelExclusion = channelExclusion;
  }

  public boolean isUnsubscribe() {
    return unsubscribe;
  }

  public void setUnsubscribe(boolean unsubscribe) {
    this.unsubscribe = unsubscribe;
  }

  public String getUnsubscribeLanguage() {
    return unsubscribeLanguage;
  }

  public void setUnsubscribeLanguage(String unsubscribeLanguage) {
    this.unsubscribeLanguage = unsubscribeLanguage;
  }

  public Integer getUnsubscribeCount() {
    return unsubscribeCount;
  }

  public void setUnsubscribeCount(Integer unsubscribeCount) {
    this.unsubscribeCount = unsubscribeCount;
  }

  public String getReplaceModule() {
    return replaceModule;
  }

  public void setReplaceModule(String replaceModule) {
    this.replaceModule = replaceModule;
  }

  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  public String getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }

  public String getMailingId() {
    return mailingId;
  }

  public void setMailingId(String mailingId) {
    this.mailingId = mailingId;
  }

  public Date getSendTime() {
    return sendTime;
  }

  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public int[] getGroupIds() {
    return groupIds;
  }

  public void setGroupIds(int[] groupIds) {
    this.groupIds = groupIds;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public String getAccountPass() {
    return accountPass;
  }

  public void setAccountPass(String accountPass) {
    this.accountPass = accountPass;
  }

  public List<String> getToList() {
    return toList;
  }

  public void setToList(List<String> toList) {
    this.toList = toList;
  }

  public boolean isHasExclusion() {
    return hasExclusion;
  }

  public void setHasExclusion(boolean hasExclusion) {
    this.hasExclusion = hasExclusion;
  }

  public List<MailDetail> getDetails() {
    return details;
  }

  public void setDetails(List<MailDetail> details) {
    this.details = details;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getMailType() {
    return mailType;
  }

  public void setMailType(String mailType) {
    this.mailType = mailType;
  }
}

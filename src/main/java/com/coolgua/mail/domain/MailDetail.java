package com.coolgua.mail.domain;

import java.util.Date;

/**
 * Created by lihongde on 2018/1/13 14:34.
 */
public class
MailDetail {
  private String id;
  private String mailId;
  private Integer providerId;
  private Integer status;
  private String event;
  private boolean scheduled;
  private Integer shardingCategory;
  private String recipient;
  private String emailId;
  private String replaceModule;
  private String remotedata;
  private Integer open;
  private Date lastOpenTime;
  private String replaceJson;
  private boolean unsubscribe;
  private Date unsubscribeTime;
  private Date createTime;
  private Date updateTime;
  private boolean deleted;

  private String templateName;
  private String mailType;
  private String subject;
  private Date sendTime;

  public MailDetail(){}

  public MailDetail(String mailId, Integer providerId, Integer status, String event, boolean scheduled, Integer shardingCategory, String recipient, String emailId, int open){
    this.mailId = mailId;
    this.providerId = providerId;
    this.status = status;
    this.event = event;
    this.scheduled = scheduled;
    this.shardingCategory = shardingCategory;
    this.recipient = recipient;
    this.emailId = emailId;
    this.createTime = new Date();
    this.updateTime = new Date();
    this.open = open;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMailId() {
    return mailId;
  }

  public void setMailId(String mailId) {
    this.mailId = mailId;
  }

  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public boolean isScheduled() {
    return scheduled;
  }

  public void setScheduled(boolean scheduled) {
    this.scheduled = scheduled;
  }

  public Integer getShardingCategory() {
    return shardingCategory;
  }

  public void setShardingCategory(Integer shardingCategory) {
    this.shardingCategory = shardingCategory;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getReplaceModule() {
    return replaceModule;
  }

  public void setReplaceModule(String replaceModule) {
    this.replaceModule = replaceModule;
  }

  public String getRemotedata() {
    return remotedata;
  }

  public void setRemotedata(String remotedata) {
    this.remotedata = remotedata;
  }

  public Integer getOpen() {
    return open;
  }

  public void setOpen(Integer open) {
    this.open = open;
  }

  public Date getLastOpenTime() {
    return lastOpenTime;
  }

  public void setLastOpenTime(Date lastOpenTime) {
    this.lastOpenTime = lastOpenTime;
  }

  public String getReplaceJson() {
    return replaceJson;
  }

  public void setReplaceJson(String replaceJson) {
    this.replaceJson = replaceJson;
  }

  public boolean isUnsubscribe() {
    return unsubscribe;
  }

  public void setUnsubscribe(boolean unsubscribe) {
    this.unsubscribe = unsubscribe;
  }

  public Date getUnsubscribeTime() {
    return unsubscribeTime;
  }

  public void setUnsubscribeTime(Date unsubscribeTime) {
    this.unsubscribeTime = unsubscribeTime;
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

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
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

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public Date getSendTime() {
    return sendTime;
  }

  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }
}

package com.coolgua.mail.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by lihongde on 2018/1/13 15:12.
 */
public class Template {

  private String id;
  private Integer orgId;
  private String name;
  private String dsId;
  private Integer dsType;
  private String dsFilePath;
  private String mailField;
  private Integer mailType;
  private String replaceField;
  private String senderName;
  private String senderAddress;
  private String replyAddress;
  private String subject;
  private String content;
  private Integer status;
  private boolean scheduled;
  private Date sendTime;
  private boolean unsubscribe;
  private String unsubscribeLanguage;
  private String campaignId;
  private String creator;
  private String creatorName;
  private Date createTime;
  private String mender;
  private Date updateTime;

  private Integer providerId;
  private List<Attachment> attachments;
  private String sendTo;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDsId() {
    return dsId;
  }

  public void setDsId(String dsId) {
    this.dsId = dsId;
  }

  public Integer getDsType() {
    return dsType;
  }

  public void setDsType(Integer dsType) {
    this.dsType = dsType;
  }

  public String getMailField() {
    return mailField;
  }

  public void setMailField(String mailField) {
    this.mailField = mailField;
  }

  public Integer getMailType() {
    return mailType;
  }

  public void setMailType(Integer mailType) {
    this.mailType = mailType;
  }

  public String getReplaceField() {
    return replaceField;
  }

  public void setReplaceField(String replaceField) {
    this.replaceField = replaceField;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public String getSenderAddress() {
    return senderAddress;
  }

  public void setSenderAddress(String senderAddress) {
    this.senderAddress = senderAddress;
  }

  public String getReplyAddress() {
    return replyAddress;
  }

  public void setReplyAddress(String replyAddress) {
    this.replyAddress = replyAddress;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public boolean isScheduled() {
    return scheduled;
  }

  public void setScheduled(boolean scheduled) {
    this.scheduled = scheduled;
  }

  public Date getSendTime() {
    return sendTime;
  }

  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
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

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getMender() {
    return mender;
  }

  public void setMender(String mender) {
    this.mender = mender;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }

  public List<Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<Attachment> attachments) {
    this.attachments = attachments;
  }

  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  public String getDsFilePath() {
    return dsFilePath;
  }

  public void setDsFilePath(String dsFilePath) {
    this.dsFilePath = dsFilePath;
  }

  public String getSendTo() {
    return sendTo;
  }

  public void setSendTo(String sendTo) {
    this.sendTo = sendTo;
  }
}

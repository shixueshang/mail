package com.coolgua.mail.service.impl;

import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.mapper.MailMapper;
import com.coolgua.mail.service.MailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lihongde on 2018/1/13 15:24.
 */
@Service("mailService")
public class MailServiceImpl implements MailService {

  @Resource
  private MailMapper mailMapper;

  @Override
  public PageInfo<Mail> findPageMails(Map<String, Object> params, int page, int size) {
    PageHelper.startPage(page, size);
    List<Mail> list = mailMapper.findMailsByCondition(params);
    return new PageInfo<Mail>(list);
  }

  @Override
  public List<Mail> findMailsByCondition(Map<String, Object> params) {
    return mailMapper.findMailsByCondition(params);
  }

  @Override
  public void addMail(Mail mail) {
    mailMapper.addMail(mail);
  }

  @Override
  @Transactional
  public void updateMail(Mail mail) {
    mailMapper.updateMail(mail);
  }

  @Override
  public Mail getMail(String mailId) {
    return mailMapper.getMail(mailId);
  }

  @Override
  public List<Map<String, Object>> findMailsByTemplate(String templateId) {
    return mailMapper.findMailsByTemplate(templateId);
  }

  @Override
  public List<Map<String, Object>> getChartData(String templateId) {
    return mailMapper.getChartData(templateId);
  }

  @Override
  public List<Map<String, Object>> getNotSubmit(String templateId) {
    return mailMapper.getNotSubmit(templateId);
  }

  @Override
  public List<Map<String, Object>> getSubmitFailure(String templateId) {
    return mailMapper.getSubmitFailure(templateId);
  }

  @Override
  public List<Map<String, Object>> getSubmitSuccess(String templateId) {
    return mailMapper.getSubmitSuccess(templateId);
  }
}

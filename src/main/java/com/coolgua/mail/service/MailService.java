package com.coolgua.mail.service;

import com.coolgua.mail.domain.Mail;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;

/**
 * Created by lihongde on 2018/1/13 15:13.
 */
public interface MailService {

  PageInfo<Mail> findPageMails(Map<String, Object> params, int page, int size);

  List<Mail> findMailsByCondition(Map<String, Object> params);

  void addMail(Mail mail);

  void updateMail(Mail mail);

  Mail getMail(String mailId);

  List<Map<String, Object>> findMailsByTemplate(String templateId);

  List<Map<String, Object>> getChartData(String templateId);

  List<Map<String, Object>> getNotSubmit(String templateId);

  List<Map<String, Object>> getSubmitFailure(String templateId);

  List<Map<String, Object>> getSubmitSuccess(String templateId);

}

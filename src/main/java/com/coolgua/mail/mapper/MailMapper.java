package com.coolgua.mail.mapper;

import com.coolgua.mail.domain.Mail;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lihongde on 2018/1/13 15:14.
 */
@Mapper
public interface MailMapper {

  List<Mail> findMailsByCondition(Map<String, Object> params);

  void addMail(Mail mail);

  void updateMail(Mail mail);

  Mail getMail(@Param("id") String mailId);

  List<Map<String, Object>> findMailsByTemplate(@Param("templateId") String templateId);

  List<Map<String, Object>> getChartData(@Param("templateId") String templateId);

  List<Map<String, Object>> getNotSubmit(@Param("templateId") String templateId);

  List<Map<String, Object>> getSubmitFailure(@Param("templateId") String templateId);

  List<Map<String, Object>> getSubmitSuccess(@Param("templateId") String templateId);

}

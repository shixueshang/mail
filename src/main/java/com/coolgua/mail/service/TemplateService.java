package com.coolgua.mail.service;

import com.coolgua.mail.domain.Attachment;
import com.coolgua.mail.domain.Template;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;

/**
 * Created by lihongde on 2018/1/13 15:48.
 */
public interface TemplateService{

  void addTemplate(Template template);

  Template getTemplate(String templateId);

  PageInfo<Template> findPageTemplates(Map<String, Object> params, int page, int size);

  List<Template> findTemplatesByCondition(Map<String, Object> params);

  void updateTemplate(Template template);

  void batchUpdateTemplates(Map<String, Object> params);

  void addAttachment(Attachment attachment);

  List<Attachment> findAttachsByTemplateId(String templateId);

  void deleteAttach(String id);

}

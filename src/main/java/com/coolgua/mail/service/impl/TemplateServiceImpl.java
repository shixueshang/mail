package com.coolgua.mail.service.impl;

import com.coolgua.mail.domain.Attachment;
import com.coolgua.mail.domain.Template;
import com.coolgua.mail.mapper.TemplateMapper;
import com.coolgua.mail.service.TemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Created by lihongde on 2018/1/13 15:49.
 */
@Service
public class TemplateServiceImpl implements TemplateService {

  @Resource
  private TemplateMapper templateMapper;

  @Override
  public void addTemplate(Template template) {
    templateMapper.addTemplate(template);
  }

  @Override
  public Template getTemplate(String templateId) {
    return templateMapper.getTemplate(templateId);
  }

  @Override
  public PageInfo<Template> findPageTemplates(Map<String, Object> params, int page, int size) {
    PageHelper.startPage(page, size);
    List<Template> list = templateMapper.findTemplatesByCondition(params);
    return new PageInfo<Template>(list);
  }

  @Override
  public List<Template> findTemplatesByCondition(Map<String, Object> params) {
    return templateMapper.findTemplatesByCondition(params);
  }

  @Override
  public void updateTemplate(Template template) {
    templateMapper.updateTemplate(template);
  }

  @Override
  public void batchUpdateTemplates(Map<String, Object> params) {
    templateMapper.batchUpdateTemplates(params);
  }

  @Override
  public void addAttachment(Attachment attachment) {
    templateMapper.addAttachment(attachment);
  }

  @Override
  public List<Attachment> findAttachsByTemplateId(String templateId) {
    return templateMapper.findAttachsByTemplateId(templateId);
  }

  @Override
  public void deleteAttach(String id) {
    templateMapper.deleteAttach(id);
  }
}

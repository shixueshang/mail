package com.coolgua.mail.mapper;

import com.coolgua.mail.domain.Attachment;
import com.coolgua.mail.domain.Template;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lihongde on 2018/1/13 15:46.
 */
@Mapper
public interface TemplateMapper {

  void addTemplate(Template template);

  Template getTemplate(String id);

  List<Template> findTemplatesByCondition(Map<String, Object> params);

  void updateTemplate(Template template);

  void batchUpdateTemplates(Map<String, Object> params);

  void addAttachment(Attachment attachment);

  List<Attachment> findAttachsByTemplateId(@Param("templateId") String templateId);

  void deleteAttach(String id);
}

package com.coolgua.mail.service.impl;

import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.mapper.MailDetailMapper;
import com.coolgua.mail.mapper.MailMapper;
import com.coolgua.mail.service.MailDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Created by lihongde on 2018/1/13 15:46.
 */
@Service("mailDetailService")
public class MailDetailServiceImpl implements MailDetailService {

  @Resource
  private MailDetailMapper mailDetailMapper;

  @Resource
  private MailMapper mailMapper;

  @Override
  public PageInfo<MailDetail> findPageMailDetails(Map<String, Object> params, int page, int size) {
    PageHelper.startPage(page, size);
    List<MailDetail> list = findMailDetailsByCondition(params);
    return new PageInfo<>(list);
  }

  @Override
  public List<MailDetail> findMailDetailsByCondition(Map<String, Object> params) {
    List<MailDetail> list = new ArrayList<>();
    List<Mail> mails = mailMapper.findMailsByCondition(params);
    for(Mail m : mails){
      params.put("mailId", m.getId());
      List<MailDetail> details = mailDetailMapper.findMailDetailsByCondition(params);
      for(MailDetail md : details){
        md.setTemplateName(m.getTemplateName());
        md.setSubject(m.getSubject());
        md.setMailType(m.getMailType());
      }
      list.addAll(details);
    }

    return list;
  }

  @Override
  public void batchAddMailDetail(List<MailDetail> details) {
    mailDetailMapper.batchAddMailDetails(details);
  }

  @Override
  public void batchUpdateMailDetail(List<MailDetail> details) {
    mailDetailMapper.batchUpdateMailDetails(details);
  }

  @Override
  public void addMailDetail(MailDetail detail) {
    mailDetailMapper.addMailDetail(detail);
  }

  @Override
  public void updateMailDetail(MailDetail detail) {
    mailDetailMapper.updateMailDetail(detail);
  }
}

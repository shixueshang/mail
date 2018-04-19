package com.coolgua.mail.service;


import com.coolgua.mail.domain.MailDetail;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;

/**
 * Created by lihongde on 2018/1/13 15:30.
 */
public interface MailDetailService {

  PageInfo<MailDetail> findPageMailDetails(Map<String, Object> params, int page, int size);

  List<MailDetail> findMailDetailsByCondition(Map<String, Object> params);

  void addMailDetail(MailDetail detail);

  void updateMailDetail(MailDetail detail);

  void batchAddMailDetail(List<MailDetail> details);

  void batchUpdateMailDetail(List<MailDetail> details);

}

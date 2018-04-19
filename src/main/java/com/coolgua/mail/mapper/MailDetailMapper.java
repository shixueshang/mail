package com.coolgua.mail.mapper;

import com.coolgua.mail.domain.MailDetail;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lihongde on 2018/1/13 15:28.
 */
@Mapper
public interface MailDetailMapper {

  List<MailDetail> findMailDetailsByCondition(Map<String, Object> params);

  void addMailDetail(MailDetail detail);

  void updateMailDetail(MailDetail detail);

  void batchAddMailDetails(@Param("mailDetails") List<MailDetail> mailDetails);

  void batchUpdateMailDetails(@Param("details") List<MailDetail> mailDetails);

}

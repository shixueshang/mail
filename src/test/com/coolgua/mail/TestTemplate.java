package com.coolgua.mail;

import com.alibaba.fastjson.JSON;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.domain.Template;
import com.coolgua.mail.service.MailDetailService;
import com.coolgua.mail.service.TemplateService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by lihongde on 2018/1/25 11:57.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTemplate {

  @Resource
  private TemplateService templateService;

  @Resource
  private MailDetailService mailDetailService;

  @Test
  public void findTemplates(){
    Map<String, Object> params = new HashMap<>();
    params.put("orgId", 96);
    List<Template> templates = templateService.findTemplatesByCondition(params);
    String json = JSON.toJSONString(templates);

    System.out.println("json : " + json);
  }

  @Test
  public void batchAddDetail(){
    MailDetail detail1 = new MailDetail("1", 1, 1, "rf", false, 0, "fsefe", "fde", 0);
    MailDetail detail2 = new MailDetail("1", 1, 1, "rf", false, 0, "fsefe", "fde", 0);
    List<MailDetail> list = new ArrayList<>();
    list.add(detail1);
    list.add(detail2);
    mailDetailService.batchAddMailDetail(list);
  }

  @Test
  public void batchUpdateDetail(){
    MailDetail detail1 = new MailDetail("1", 1, 1, "rf", false, 0, "fsefe", "fde", 0);
    MailDetail detail2 = new MailDetail("1", 1, 1, "rf", false, 0, "fsefe", "fde", 0);
    List<MailDetail> list = new ArrayList<>();
    list.add(detail1);
    list.add(detail2);
    mailDetailService.batchUpdateMailDetail(list);
  }

}

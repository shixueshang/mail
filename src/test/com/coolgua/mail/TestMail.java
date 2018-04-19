package com.coolgua.mail;

import com.coolgua.mail.domain.BlackList;
import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.service.BlackListService;
import com.coolgua.mail.service.MailDetailService;
import com.coolgua.mail.service.MailService;
import com.coolgua.mail.util.Constant.MailDetailStatus;
import com.coolgua.mail.util.Constant.MailStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by lihongde on 2018/1/14 17:38.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = true)
public class TestMail {

  private MockMvc mockMvc;

  @Resource
  private WebApplicationContext webApplicationContext;

  @Resource
  private MailService mailService;

  @Resource
  private MailDetailService mailDetailService;

  @Resource
  private BlackListService blackListService;

  @Before
  public void before() {
    //获取mockmvc对象实例
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }


  @Test
  public void testAddMail(){
    Mail mail = new Mail();
    mail.setOrgId(96);
    mail.setSender("lihongde@coolgua.com");
    mail.setSenderName("lihongde");
    mail.setReplyAddress("lihongde@coolgua.com");
    mail.setProviderId(1);
    mail.setScheduled(true);
    mail.setSendTo("hejia@coolgua.com");
    mail.setSubject("测试邮件");
    mail.setContent("<html>hello world</html>");
    mail.setHasAttachment(false);
    mailService.addMail(mail);
  }

  @Test
  public void testFindMail(){
    Map<String, Object> params = new ConcurrentHashMap<>();
    params.put("status", MailStatus.STATUS_TIMED);
    params.put("scheduled", true);
    params.put("sendTime", new Date());
    List<Mail> mails = mailService.findPageMails(params, 0, 10).getList();
    for(Mail m : mails){
      System.out.println("account :" + m.getAccountName() + " pass : " + m.getAccountPass());
      m.setUpdateTime(new Date());
      mailService.updateMail(m);
    }
  }

  @Test
  public void testBatchAddMailDetails(){
    MailDetail md1 = new MailDetail("1", 2, 1, "request", false, 0, "lihongde@qq.com", null, 0);
    MailDetail md2 = new MailDetail("2", 2, 1, "deliver", true, 0, "4233234534@qq.com", null, 0);
    List<MailDetail> list = new ArrayList<>();
    list.add(md1);
    list.add(md2);
    mailDetailService.batchAddMailDetail(list);
  }

  @Test
  public void testFindMailDetails(){
    Map<String, Object> params = new HashMap<>();
    mailDetailService.findMailDetailsByCondition(params);
  }

  @Test
  public void testAddBlackList(){
    BlackList bl = new BlackList();
    bl.setOrgId(96);
    bl.setRecipient("liongde@oolgua.com");
    bl.setCreator("1");
    bl.setCreateTime(new Date());
    blackListService.addBlackList(bl);
  }

  @Test
  public void testRemoveBlackList() throws Exception{
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/interface/blacklist/remove").param("ids[]", "1", "2"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(200))
        .andReturn();
    result.getResponse().setCharacterEncoding("UTF-8");
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void findMails(){
    List<Map<String, Object>> mails = mailService.findMailsByTemplate("0e8d7ac2067311e8bb1b902b343c99fd");
    for(Map<String, Object> mail : mails){
      System.out.println("sendTime : " + mail.get("send_time") + " subject : " + mail.get("subject") + " total : " + mail.get("total"));
    }

  }

  @Test
  public void getChartData(){
    List<Map<String, Object>> chartData = mailService.getChartData("0e8d7ac2067311e8bb1b902b343c99fd");
    for(Map<String, Object> map : chartData){
      System.out.println("date : " + map.get("send_date") + " total : " + map.get("total") + " request : " + map.get("request"));
    }
  }

  @Test
  public void testSubmitFail(){
    Map<String, Object> submitFailureParam = new HashMap<>();
    submitFailureParam.put("templateId", "313ec9350cc111e897400021ccb88b0d");
    submitFailureParam.put("mailId", "9bf2f3530cc811e897400021ccb88b0d");
    submitFailureParam.put("detailStatus", MailDetailStatus.DETAIL_SUBMIT_FAILURE);
    int _submitFailure = mailDetailService.findMailDetailsByCondition(submitFailureParam).size();
    System.out.println("_submitFailure : " + _submitFailure);
  }


}

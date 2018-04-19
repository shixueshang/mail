package com.coolgua.mail;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.alibaba.fastjson.JSON;
import com.coolgua.mail.domain.Attachment;
import com.coolgua.mail.domain.Template;
import com.coolgua.mail.enums.DataSourceType;
import com.coolgua.mail.service.DataSourceService;
import com.coolgua.mail.service.MailService;
import com.coolgua.mail.service.ProviderConfigService;
import com.coolgua.mail.service.TemplateService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by lihongde on 2018/1/27 15:53.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
public class TestSend {

  /**
   * 模拟mvc测试对象
   */
  private MockMvc mockMvc;

  /**
   * web项目上下文
   */
  @Autowired
  private WebApplicationContext webApplicationContext;

  /**
   * 所有测试方法执行之前执行该方法
   */
  @Before
  public void before() {
    //获取mockmvc对象实例
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Resource
  private ProviderConfigService providerConfigService;
  @Resource
  private TemplateService templateService;
  @Resource
  private MailService mailService;
  @Resource
  private DataSourceService dataSourceService;

  @Test
  public void addOrUpdateConfig() throws Exception{
    MvcResult result = mockMvc.perform(post("/interface/config/addOrUpdate")
        .param("orgId", "96")
        .param("providerId", "1")
        .param("accountName", "postmaster@coolgua-demo.sendcloud.org")
        .param("accountPass", "JdKvObyYo3RDa3Rr")
    )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(200))
        .andReturn();
    result.getResponse().setCharacterEncoding("UTF-8");
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void addDataSource() throws Exception{
    MvcResult result = mockMvc.perform(post("/interface/config/datasource/add")
        .param("orgId", "96")
        .param("name", "测试数据源")
        .param("fields", "姓名,邮箱")
    )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(200))
        .andReturn();
    result.getResponse().setCharacterEncoding("UTF-8");
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void send() throws Exception{
    Map<String, Object> params = new HashMap<>();
    params.put("templateId", "2a386a0721e911e8acc20021ccb88b0d");
    params.put("orgId", 96);
    params.put("sendTo", "hejia@coolgua.com,lihongde@coolgua.com, 460847871@qq.com");
    String requestJson = JSON.toJSONString(params);
    String responseString = mockMvc.perform(post("/mail/sendMail").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
    System.out.println("response : " + responseString);
  }

  @Test
  public void sendWithTemplate() throws Exception{
    Template template = new Template();
    template.setSendTo("hejia@coolgua.com,lihongde@coolgua.com, 460847871@qq.com");
    template.setName("测试自定义邮件");
    template.setOrgId(96);
    template.setProviderId(1);
    template.setSendTime(new Date());
    template.setDsId("6c180b76058311e8bb1b902b343c99fd");
    template.setSubject("测试邮件");
    template.setContent("这是测试邮件，请忽略");
    template.setDsType(DataSourceType.EXTERNAL.ordinal());
    List<Attachment> attachmentList = templateService.findAttachsByTemplateId("0e8d7ac2067311e8bb1b902b343c99fd");
    template.setAttachments(attachmentList);
    String requestJson = JSON.toJSONString(template);
    String responseString = mockMvc.perform(post("/mail/sendMail").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJson)).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
    System.out.println("response : " + responseString);
  }


}

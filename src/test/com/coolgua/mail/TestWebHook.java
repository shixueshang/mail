package com.coolgua.mail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * Created by lihongde on 2018/1/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWebHook {

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


  /**
   * 表单参数形式
   * @throws Exception
   */
  @Test
  public void testWebHook() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/mail/track")
        .param("category", "postmaster@test.sendcloud.org")
        .param("labelId", "0")
        .param("timestamp", "1429257328759")
        .param("recipientArray", "['lihongde@coolgua.com']")
        .param("signature", "1f5481ddc9f2204a45295dcc1675b473b2d41df10578c0a451f410cdb1c9fb79")
        .param("emailIds", "['1429257328759_15_200_900.test-inbound.sendcloud.sohu.com0$lihongde@coolgua.com']")
        .param("token", "iSXtPWbCNO5qiBrLhTRX48dbRujd3t0lL8RLg7ocJbhiDh6WxJ")
        .param("messageId", "1429257328759_15_200_900.test-inbound.sendcloud.sohu.com")
        .param("message", "successfully request")
        .param("event", "request")
        .param("recipientSize", "1")
    )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(200))
        .andReturn();
    result.getResponse().setCharacterEncoding("UTF-8");
    System.out.println(result.getResponse().getContentAsString());
  }

}

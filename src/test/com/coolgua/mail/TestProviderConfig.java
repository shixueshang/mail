package com.coolgua.mail;

import com.coolgua.mail.domain.ProviderConfig;
import com.coolgua.mail.service.ProviderConfigService;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lihongde on 2018/1/15 13:21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class TestProviderConfig {

  @Resource
  private ProviderConfigService providerConfigService;

  @Test
  public void testAddConfig(){
    ProviderConfig config = new ProviderConfig();
    config.setOrgId(96);
    config.setProviderId(1);
    config.setAccountName("postmaster@coolgua-demo.sendcloud.org");
    config.setAccountPass("JdKvObyYo3RDa3Rr");
    providerConfigService.addProviderConfig(config);
  }

}

package com.coolgua.mail.service.impl;

import com.coolgua.mail.domain.ProviderConfig;
import com.coolgua.mail.mapper.ProviderConfigMapper;
import com.coolgua.mail.service.ProviderConfigService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Created by lihongde on 2018/1/15 13:19.
 */
@Service
public class ProviderConfigServiceImpl implements ProviderConfigService{

  @Resource
  private ProviderConfigMapper providerConfigMapper;

  @Override
  public void addProviderConfig(ProviderConfig config) {
    providerConfigMapper.addProviderConfig(config);
  }

  @Override
  public ProviderConfig findConfigByOrgId(Integer orgId) {
    return providerConfigMapper.findConfigByOrgId(orgId);
  }

  @Override
  public void updateProviderConfig(ProviderConfig config) {
    providerConfigMapper.updateProviderConfig(config);
  }
}

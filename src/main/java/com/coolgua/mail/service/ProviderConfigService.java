package com.coolgua.mail.service;

import com.coolgua.mail.domain.ProviderConfig;

/**
 * Created by lihongde on 2018/1/15 13:19.
 */
public interface ProviderConfigService {

  void addProviderConfig(ProviderConfig config);

  ProviderConfig findConfigByOrgId(Integer orgId);

  void updateProviderConfig(ProviderConfig config);

}

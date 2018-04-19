package com.coolgua.mail.mapper;

import com.coolgua.mail.domain.ProviderConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by lihongde on 2018/1/15 13:16.
 */
@Mapper
public interface ProviderConfigMapper {

  void addProviderConfig(ProviderConfig config);

  ProviderConfig findConfigByOrgId(Integer orgId);

  void updateProviderConfig(ProviderConfig config);
}

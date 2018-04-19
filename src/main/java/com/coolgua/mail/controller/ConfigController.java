package com.coolgua.mail.controller;

import static com.coolgua.mail.util.Constant.mailProviderMap;

import com.coolgua.mail.domain.DataSource;
import com.coolgua.mail.domain.ProviderConfig;
import com.coolgua.mail.enums.DataSourceType;
import com.coolgua.mail.exception.FailedReqeustException;
import com.coolgua.mail.service.DataSourceService;
import com.coolgua.mail.service.ProviderConfigService;
import com.coolgua.mail.util.AjaxJson;
import com.coolgua.mail.util.Constant.JSON_RESULT;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihongde on 2018/1/25 17:44.
 */
@RestController
@RequestMapping(value = "/interface/config/{fp}/{orgId}/{userId}")
public class ConfigController extends BaseController {

  private Logger logger = LoggerFactory.getLogger(ConfigController.class);

  @Resource
  private ProviderConfigService providerConfigService;

  @Resource
  private DataSourceService dataSourceService;

  @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
  public AjaxJson getConfig(@PathVariable Integer orgId) {
    ProviderConfig config = providerConfigService.findConfigByOrgId(orgId);
    Map<String, Object> result = new HashMap<>();
    result.put("mailProviders", mailProviderMap);
    result.put("config", config);
    return new AjaxJson(JSON_RESULT.OK, result);
  }

  @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
  public AjaxJson addOrUpdateConfig(ProviderConfig providerConfig, @PathVariable Integer orgId, @PathVariable String userId) {
    providerConfig.setOrgId(orgId);
    ProviderConfig config = providerConfigService.findConfigByOrgId(orgId);
    try {
      if (config == null) {
        providerConfig.setCreator(userId);
        providerConfig.setCreateTime(new Date());
        providerConfigService.addProviderConfig(providerConfig);
      } else {
        providerConfig.setId(config.getId());
        providerConfigService.updateProviderConfig(providerConfig);
      }
    } catch (Exception e) {
      String message = "新增或修改配置失败,请重试";
      logger.error(message, e);
      throw new FailedReqeustException(message);
    }
    return new AjaxJson(JSON_RESULT.OK);
  }

  @RequestMapping(value = "/datasource/list", method = RequestMethod.GET)
  public AjaxJson listDataSource(@PathVariable Integer orgId) {
    Map<String, Object> params = new HashMap<>();
    params.put("orgId", orgId);
    Map<String, Object> result = new ConcurrentHashMap<String, Object>();
    result.put("type", DataSourceType.EXTERNAL.ordinal());
    result.put("page", dataSourceService.findPageDataSources(params, page, size));
    return new AjaxJson(JSON_RESULT.OK, result);
  }

  @RequestMapping(value = "/datasource/add", method = RequestMethod.POST)
  public AjaxJson addDataSource(DataSource dataSource, @PathVariable Integer orgId){
    dataSource.setOrgId(orgId);
    dataSource.setDsType(DataSourceType.EXTERNAL.ordinal());
    try{
      dataSourceService.addDataSource(dataSource);
    }catch (Exception e){
      String message = "添加数据源失败";
      logger.error(message, e);
      throw new FailedReqeustException(message);
    }

    return new AjaxJson(JSON_RESULT.OK);
  }

}

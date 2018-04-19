package com.coolgua.mail.service.impl;

import com.coolgua.mail.domain.DataSource;
import com.coolgua.mail.mapper.DataSourceMapper;
import com.coolgua.mail.service.DataSourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Created by lihongde on 2018/1/25 17:54.
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

  @Resource
  private DataSourceMapper dataSourceMapper;

  @Override
  public void addDataSource(DataSource dataSource) {
    dataSourceMapper.addDataSource(dataSource);
  }

  @Override
  public void updateDataSource(DataSource dataSource) {
    dataSourceMapper.updateDataSource(dataSource);
  }

  @Override
  public DataSource getDataSource(String id) {
    return dataSourceMapper.getDataSource(id);
  }

  @Override
  public PageInfo<DataSource> findPageDataSources(Map<String, Object> params, int page, int size) {
    PageHelper.startPage(page, size);
    List<DataSource> list = dataSourceMapper.findDataSourcesByCondition(params);
    return new PageInfo<DataSource>(list);
  }
}

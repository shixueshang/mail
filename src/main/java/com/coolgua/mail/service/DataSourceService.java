package com.coolgua.mail.service;

import com.coolgua.mail.domain.DataSource;
import com.github.pagehelper.PageInfo;
import java.util.Map;

/**
 * Created by lihongde on 2018/1/25 17:47.
 */
public interface DataSourceService {

  void addDataSource(DataSource dataSource);

  void updateDataSource(DataSource dataSource);

  DataSource getDataSource(String id);

  PageInfo<DataSource> findPageDataSources(Map<String, Object> params, int page, int size);



}

package com.coolgua.mail.mapper;

import com.coolgua.mail.domain.DataSource;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by lihongde on 2018/1/25 17:55.
 */
@Mapper
public interface DataSourceMapper {

  void addDataSource(DataSource dataSource);

  void updateDataSource(DataSource dataSource);

  DataSource getDataSource(String id);

  List<DataSource> findDataSourcesByCondition(Map<String, Object> params);

}

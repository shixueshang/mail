package com.coolgua.mail.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.coolgua.mail.algorithm.ModuloShardingTableAlgorithm;
import com.github.pagehelper.PageHelper;
import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingjdbc.core.api.config.strategy.StandardShardingStrategyConfiguration;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 配置数据源, 集成sharding-jdbc
 * Created by lihongde on 2018/2/6 11:47.
 */
@Configuration
@MapperScan(basePackages = "com.coolgua.mail.mapper")
public class DataSourceConfig {

  @Autowired
  private Environment env;

  @Bean
  public DataSource getDataSource() throws SQLException{
    return getShardingDataSource();
  }

  private DataSource getShardingDataSource() throws SQLException {
    ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
    shardingRuleConfig.getTableRuleConfigs().add(getMailDetailTableRuleConfiguration());
    shardingRuleConfig.setDefaultDataSourceName("coolgua_mail_0");
    shardingRuleConfig.getBindingTableGroups().add("m_template,m_mail,m_mail_detail,m_provider_config,m_attachment,m_black_list,m_data_source,m_mail_url_click");
    shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("sharding_category", "coolgua_mail_${sharding_category % 2}"));
    shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("sharding_category", ModuloShardingTableAlgorithm.class.getName()));
    return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new HashMap<String, Object>(), new Properties());
  }


  private TableRuleConfiguration getMailDetailTableRuleConfiguration() {
    TableRuleConfiguration mailDetailTableRuleConfig = new TableRuleConfiguration();
    mailDetailTableRuleConfig.setLogicTable("m_mail_detail");
    mailDetailTableRuleConfig.setActualDataNodes("coolgua_mail_${0..1}.m_mail_detail_${[0, 1, 2, 3, 4]}");
    return mailDetailTableRuleConfig;
  }

  private Map<String, DataSource> createDataSourceMap() {
    Map<String, DataSource> result = new HashMap<>(2, 1);
    result.put("coolgua_mail_0", createDataSource("coolgua_mail_0"));
    result.put("coolgua_mail_1", createDataSource("coolgua_mail_1"));
    return result;
  }

  /**
   * 创建druid数据源
   * @param dataSourceName
   * @return
   */
  public DataSource createDataSource(final String dataSourceName) {
    // 使用Druid连接池连接数据库
    DruidDataSource druidDataSource = new DruidDataSource();
    druidDataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
    druidDataSource.setUrl(String.format(env.getProperty("jdbc.url"), dataSourceName));
    druidDataSource.setUsername(env.getProperty("jdbc.username"));
    druidDataSource.setPassword(env.getProperty("jdbc.password"));
    return druidDataSource;
  }


  /**
   * 根据数据源创建SqlSessionFactory
   */
  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource);//指定数据源(这个必须有，否则报错)
    //下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
    sqlSessionFactoryBean.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));
    sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));//指定xml文件位置
    return sqlSessionFactoryBean.getObject();
  }

  @Bean
  public PageHelper pageHelper() {
    PageHelper pageHelper = new PageHelper();
    Properties p = new Properties();
    p.setProperty("offsetAsPageNum", "true");
    p.setProperty("rowBoundsWithCount", "true");
    p.setProperty("reasonable", env.getProperty("pagehelper.reasonable"));
    pageHelper.setProperties(p);
    return pageHelper;
  }



}

package com.coolgua.mail.config;

import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.job.CommonMailJob;
import com.coolgua.mail.job.PollMailStatusJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lihongde on 2018/1/19 13:49.
 */
@Configuration
public class DataflowJobConfig {

  @Resource
  private ZookeeperRegistryCenter regCenter;

  @Bean
  public DataflowJob<MailDetail> commonMailJob(){
    return new CommonMailJob();
  }

  @Bean
  public DataflowJob<Mail> pollMailStatusJob(){
    return new PollMailStatusJob();
  }

  /**
   * 普通邮件定时配置
   * @param commonMailJob
   * @param cron
   * @param shardingTotalCount
   * @param shardingItemParameters
   * @return
   */
  @Bean(initMethod = "init")
  public JobScheduler commonJobScheduler(final DataflowJob commonMailJob, @Value("${commonMailJob.cron}") final String cron,
      @Value("${shardingCategory.shardingTotalCount}") final int shardingTotalCount,
      @Value("${shardingCategory.shardingItemParameters}") final String shardingItemParameters){
    return new SpringJobScheduler(commonMailJob, regCenter, getLiteJobConfiguration(commonMailJob.getClass(), cron, shardingTotalCount, shardingItemParameters));
  }

  /**
   * 获取邮件状态配置
   * @param pollMailStatusJob
   * @param cron
   * @param shardingTotalCount
   * @param shardingItemParameters
   * @return
   */
  @Bean(initMethod = "init")
  public JobScheduler pollMailStatusJobScheduler(final DataflowJob pollMailStatusJob, @Value("${pollMailStatusJob.cron}") final String cron,
      @Value("${shardingCategory.shardingTotalCount}") final int shardingTotalCount,
      @Value("${shardingCategory.shardingItemParameters}") final String shardingItemParameters){
    return new SpringJobScheduler(pollMailStatusJob, regCenter, getLiteJobConfiguration(pollMailStatusJob.getClass(), cron, shardingTotalCount, shardingItemParameters));
  }

  private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends DataflowJob> jobClass, final String cron, final int shardingTotalCount, final String shardingItemParameters) {
    return LiteJobConfiguration.newBuilder(new DataflowJobConfiguration(JobCoreConfiguration.newBuilder(
        jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build(), jobClass.getCanonicalName(), true)).overwrite(true).build();
  }

}

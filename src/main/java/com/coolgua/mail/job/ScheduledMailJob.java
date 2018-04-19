package com.coolgua.mail.job;

import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.exception.FailedReqeustException;
import com.coolgua.mail.service.MailDetailService;
import com.coolgua.mail.service.MailService;
import com.coolgua.mail.thirdpart.MailProvider;
import com.coolgua.mail.thirdpart.MailProviderFactory;
import com.coolgua.mail.util.Constant.MAIL_PROVIDER;
import com.coolgua.mail.util.Constant.MailDetailStatus;
import com.coolgua.mail.util.Constant.MailEvent;
import com.coolgua.mail.util.Constant.MailStatus;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;

/**
 * 定时邮件任务(废弃, sedcloud不提供定时邮件, webpower定时邮件交给webpower发送)
 * Created by lihongde on 2018/1/17 17:09.
 */
public class ScheduledMailJob implements DataflowJob<MailDetail> {

  @Resource
  private MailService mailService;

  @Resource
  private MailDetailService mailDetailService;

  private MailProvider webpowerMailProvider = MailProviderFactory.getMailProvider(MAIL_PROVIDER.WEBPOWER);
  private MailProvider sendCloudMailProvider = MailProviderFactory.getMailProvider(MAIL_PROVIDER.SENDCLOUD);

  @Override
  public List<MailDetail> fetchData(ShardingContext shardingContext) {
   /* Map<String, Object> params = new ConcurrentHashMap<>();
    params.put("status", MailDetailStatus.DETAIL_INIT);
    params.put("scheduled", true);
    params.put("shardingCategory", shardingContext.getShardingItem());*/
    List<MailDetail> list = Collections.emptyList();
    return list;
  }

  @Override
  public void processData(ShardingContext shardingContext, List<MailDetail> list) {
    try{
      Set<String> mailIds = new HashSet<>();
      for(MailDetail mailDetail : list){
        mailIds.add(mailDetail.getMailId());
      }
      for(String mailId : mailIds){
        Mail mail = mailService.getMail(mailId);
        if(mail.getSendTime().getTime() < new Date().getTime()){
          Object result = null;
          List<String> toList = new ArrayList<>();
          for(MailDetail md : list){
            md.setStatus(MailDetailStatus.DETAIL_REQUEST);
            md.setEvent(MailEvent.EVENT_REQUEST);
            md.setUpdateTime(new Date());
            toList.add(md.getRecipient());
          }
          mail.setToList(toList);
          if(mail.getProviderId() == sendCloudMailProvider.getProviderId()){
            result = sendCloudMailProvider.sendMail(mail);
          }else{
            result = webpowerMailProvider.sendMail(mail);
          }
          if(result instanceof Boolean){
            for(MailDetail detail : list){
              mailDetailService.updateMailDetail(detail);
            }
            mail.setStatus(MailStatus.STATUS_SENT);
            mailService.updateMail(mail);
          }else{
            throw new FailedReqeustException("发送定时邮件失败, reason : " + result);
          }
        }
      }

    }catch (Exception e){
      throw new FailedReqeustException("发送定时邮件失败");
    }
  }
}

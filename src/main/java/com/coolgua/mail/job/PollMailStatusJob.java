package com.coolgua.mail.job;

import static java.util.Calendar.*;

import com.coolgua.mail.domain.Mail;
import com.coolgua.mail.domain.MailDetail;
import com.coolgua.mail.service.MailDetailService;
import com.coolgua.mail.service.MailService;
import com.coolgua.mail.thirdpart.MailProviderFactory;
import com.coolgua.mail.thirdpart.webpower.WebpowerMailProvider;
import com.coolgua.mail.util.Constant.MAIL_PROVIDER;
import com.coolgua.mail.util.Constant.MailDetailStatus;
import com.coolgua.mail.util.Constant.MailEvent;
import com.coolgua.mail.util.Constant.MailStatus;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 轮询邮件状态任务
 * Created by lihongde on 2018/1/19 14:32.
 */
public class PollMailStatusJob implements DataflowJob<Mail> {

  private Logger logger = LoggerFactory.getLogger(PollMailStatusJob.class);
  private WebpowerMailProvider webpowerMailProvider = (WebpowerMailProvider) MailProviderFactory.getMailProvider(MAIL_PROVIDER.WEBPOWER);

  @Value("${polling_status_late_month}")
  private int pollingStatusLateMontn;

  @Resource
  private MailService mailService;

  @Resource
  private MailDetailService mailDetailService;

  @Override
  public List<Mail> fetchData(ShardingContext shardingContext) {

    logger.info(String.format("start polling mail status : Item: %s | Time: %s | %s", shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), shardingContext.getJobName()));

    Map<String, Object> params = new HashMap<>();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.MONTH, -pollingStatusLateMontn);
    params.put("providerId", webpowerMailProvider.getProviderId());
    params.put("sendTime", calendar.getTime());
    params.put("status", MailStatus.STATUS_SENT);
    List<Mail> mails = mailService.findMailsByCondition(params);
    return mails;
  }

  @Override
  public void processData(ShardingContext shardingContext, List<Mail> list) {
    for (Mail mail : list) {
      try {
        Map<String, String> trackData = webpowerMailProvider.getMailTrackData(mail);
        String updateDate = new SimpleDateFormat("yyyy-MM-dd").format(mail.getUpdateTime());
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Calendar calendar = getInstance();
        calendar.setTime(new Date());
        calendar.add(DAY_OF_MONTH, -1);
        Date beforeDay = calendar.getTime();
        List<Date> dates = this.getBetweenDates(mail.getUpdateTime(), beforeDay);
        if (!updateDate.equals(nowDate)) {
          dates.add(mail.getUpdateTime());
        }
        int channelRequest = Integer.parseInt(trackData.get("total_sent")); // 通道寄出总数
        if (!mail.isHasExclusion()) {
          List<String> channelRecipients = new ArrayList<>();
          webpowerMailProvider.getMailingStatsDetail(mail, 0, channelRecipients);
          Set<String> cr = new HashSet<String>(channelRecipients);
          if (channelRequest == cr.size()) {
            mail.setHasExclusion(true);
            String sendTo = mail.getSendTo();
            String[] toUsers = sendTo.split(",");
            for (String toUser : toUsers) {
              if (!channelRecipients.contains(toUser)) {
                Map<String, Object> params = new HashMap<>();
                params.put("mailId", mail.getId());
                params.put("recipient", toUser);
                params.put("shardingCategory", shardingContext.getShardingItem());
                List<MailDetail> mailDetails = mailDetailService.findMailDetailsByCondition(params);
                for (MailDetail md : mailDetails) {
                  if (md.getStatus() != MailDetailStatus.DETAIL_REPEAT_ADDRESS && md.getStatus() != MailDetailStatus.DETAIL_INVALID_ADDRESS) {
                    md.setStatus(MailDetailStatus.DETAIL_EXCLUSION);
                    md.setEvent(MailEvent.EVENT_EXCLUSION);
                    mailDetailService.updateMailDetail(md);
                  }
                }
              }
            }
          }
        }

        mail.setChannelRequest(channelRequest);
        mail.setChannelExclusion(mail.getRequest() - channelRequest);

        int deliver = Integer.parseInt(trackData.get("total_accepted")); // 寄出
        mail.setDeliver(deliver);

        int bounce = Integer.parseInt(trackData.get("hardbounces")); // 硬退
        mail.setBounce(bounce);
        if (!updateDate.equals(nowDate)) {
          for (Date date : dates) {
            webpowerMailProvider.getMailingBounce(mail, "hard", date);
          }
        }

        int softBounce = Integer.parseInt(trackData.get("softbounces")); // 软退
        mail.setSoftBounce(softBounce);
        if (!updateDate.equals(nowDate)) {
          for (Date date : dates) {
            webpowerMailProvider.getMailingBounce(mail, "soft", date);
          }
        }
        int unsubscribe = Integer.parseInt(trackData.get("unsubscribers")); // 退订
        if (unsubscribe > mail.getUnsubscribeCount()) {
          mail.setUnsubscribeCount(unsubscribe);
          webpowerMailProvider.getMailingStatsDetail(mail, MailDetailStatus.DETAIL_UNSUBSCRIBE, null);
        }

        int reportSpam = Integer.parseInt(trackData.get("spamcomplaints")); // 垃圾邮件举报
        if (reportSpam > mail.getSpam()) {
          mail.setSpam(reportSpam);
          webpowerMailProvider.getMailingStatsDetail(mail, MailDetailStatus.DETAIL_SPAM, null);
        }

        int openTotal = Integer.parseInt(trackData.get("total_renders")); // 呈现总数
        if (openTotal > mail.getOpenTotal()) {
          mail.setOpenTotal(openTotal);
          webpowerMailProvider.getOpenClickDetail(mail, "open");
        }
        int uniqueOpen = Integer.parseInt(trackData.get("unique_opens")); // 独立打开数
        mail.setUniqueOpen(uniqueOpen);

        int clickUnique = Integer.parseInt(trackData.get("unique_clickthroughs")); // 独立点击数
        mail.setClickUnique(clickUnique);

        int clickTotal = Integer.parseInt(trackData.get("total_clickthroughs")); // 点击总数
        if (clickTotal > mail.getClickTotal()) {
          mail.setClickTotal(clickTotal);
          webpowerMailProvider.getOpenClickDetail(mail, "click");
        }
        mail.setUpdateTime(new Date());
        mailService.updateMail(mail);

        logger.info(String.format("end polling mail status : Item: %s | Time: %s | %s", shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), shardingContext.getJobName()));


      } catch (Exception e) {
        logger.error("获取邮件状态失败", e);
      }
    }
  }

  /**
   * 获取两个日期之间的日期
   *
   * @param start 开始日期
   * @param end 结束日期
   * @return 日期集合
   */
  private List<Date> getBetweenDates(Date start, Date end) {
    List<Date> result = new ArrayList<Date>();
    Calendar tempStart = Calendar.getInstance();
    tempStart.setTime(start);
    tempStart.add(Calendar.DAY_OF_YEAR, 1);

    Calendar tempEnd = Calendar.getInstance();
    tempEnd.setTime(end);
    while (tempStart.before(tempEnd)) {
      result.add(tempStart.getTime());
      tempStart.add(Calendar.DAY_OF_YEAR, 1);
    }
    return result;
  }
}

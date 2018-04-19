package com.coolgua.mail.thirdpart;

import com.coolgua.mail.domain.Mail;
import java.util.Map;

/**
 * Created by lihongde on 2018/1/10 17:03.
 */
public interface MailProvider {

  int getProviderId();

  Object sendMail(Mail mail) throws Exception;

  Map<String, String> getMailTrackData(Mail mail);
}

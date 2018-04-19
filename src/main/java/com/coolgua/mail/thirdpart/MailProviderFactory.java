package com.coolgua.mail.thirdpart;


import com.coolgua.mail.thirdpart.sendcloud.SendCloudMailProvider;
import com.coolgua.mail.thirdpart.webpower.WebpowerMailProvider;
import com.coolgua.mail.util.Constant.MAIL_PROVIDER;

/**
 * Created by lihongde on 2018/1/10.
 */
public class MailProviderFactory {

  private MailProviderFactory() {
  }

  private static SendCloudMailProvider sendCloudMailProvider;
  private static WebpowerMailProvider webpowerMailProvider;

  public static MailProvider getMailProvider(int providerId) {
    if (providerId == MAIL_PROVIDER.SENDCLOUD) {
      if (sendCloudMailProvider == null) {
        sendCloudMailProvider = SendCloudMailProvider.getInstance();
      }
      return sendCloudMailProvider;
    }
    if (providerId == MAIL_PROVIDER.WEBPOWER) {
      if (webpowerMailProvider == null) {
        webpowerMailProvider = WebpowerMailProvider.getInstance();
      }
      return webpowerMailProvider;
    }
    return null;
  }
}

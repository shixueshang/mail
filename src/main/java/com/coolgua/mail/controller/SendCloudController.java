package com.coolgua.mail.controller;

import com.coolgua.mail.exception.BadRequestException;
import com.coolgua.mail.thread.RemoteDataHandleThread;
import com.coolgua.mail.thread.ThreadPool;
import com.coolgua.mail.util.AjaxJson;
import com.coolgua.mail.util.Constant.JSON_RESULT;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihongde on 2018/1/11 15:20.
 */
@RestController
@RequestMapping(value = "/mail")
public class SendCloudController extends BaseController {

  private static Logger logger = LoggerFactory.getLogger(SendCloudController.class);

  @Value("${app_key}")
  private String appKey;

  /**
   * sendcloud WebHook请求的服务，用于异步回传邮件发送结果
   *
   * @param token 随机产生的长度为50的字符串
   * @param signature 签名字符串
   */
  @RequestMapping(value = "/track", method = {RequestMethod.GET, RequestMethod.POST})
  public AjaxJson webHook(@RequestParam(value = "token", required = false) String token, @RequestParam(value = "timestamp", required = false) Long timestamp,
      @RequestParam(value = "signature", required = false) String signature,
      @RequestParam(required = false) Map<String, String> params)
      throws InvalidKeyException, NoSuchAlgorithmException {

    if (null == token || null == timestamp || null == signature) {
      logger.error("缺少安全参数, token=" + token + ", timestamp=" + timestamp + ", signature=" + signature);
      throw new BadRequestException("缺少参数, token=" + token + " timestamp=" + timestamp + " signature=" + signature);
    }
    if (!verify(appKey, token, timestamp, signature)) {
      logger.error("appKey校验失败");
      throw new BadRequestException("appKey校验失败");
    }
    logger.info("===============SendCloud Request Data==========" + params);
    ThreadPool.getInstance().handleRemoteData(new RemoteDataHandleThread(params));
    return new AjaxJson(JSON_RESULT.OK);
  }

  private boolean verify(String appkey, String token, long timestamp, String signature) throws NoSuchAlgorithmException, InvalidKeyException {
    Mac sha256HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secretKey = new SecretKeySpec(appkey.getBytes(), "HmacSHA256");
    sha256HMAC.init(secretKey);
    StringBuffer buf = new StringBuffer();
    buf.append(timestamp).append(token);
    String signatureCal = new String(Hex.encodeHex(sha256HMAC.doFinal(buf.toString().getBytes())));
    return signatureCal.equals(signature);
  }

}

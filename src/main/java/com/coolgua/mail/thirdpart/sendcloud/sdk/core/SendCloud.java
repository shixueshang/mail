package com.coolgua.mail.thirdpart.sendcloud.sdk.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coolgua.mail.thirdpart.sendcloud.sdk.config.Credential;
import com.coolgua.mail.thirdpart.sendcloud.sdk.exception.BodyException;
import com.coolgua.mail.thirdpart.sendcloud.sdk.exception.ContentException;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.Attachment;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.SendCloudMail;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.TextContent;
import com.coolgua.mail.thirdpart.sendcloud.sdk.model.TextContent.ScContentType;
import com.coolgua.mail.thirdpart.sendcloud.sdk.util.ResponseData;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;


/**
 * @author SendCloud
 */
public class SendCloud{

  private static final SendCloud sendCloud = new SendCloud();

  public SendCloud() {

  }

  public static SendCloud getInstance() {
    return sendCloud;
  }

  /**
   * 发送邮件
   *
   * @param mail 邮件
   * @param credential 身份认证
   */
  public ResponseData sendMail(SendCloudMail mail, Credential credential) throws IOException, ContentException, BodyException {
    mail.validate();
    mail.toString();
    if (CollectionUtils.isEmpty(mail.getBody().getAttachments()))
      return post(credential, mail);
    else
      return multipartPost(credential, mail);
  }

  /**
   * 普通方式发送
   */
  private ResponseData post(Credential credential, SendCloudMail mail) throws ClientProtocolException, IOException {
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("apiUser", credential.getApiUser()));
    params.add(new BasicNameValuePair("apiKey", credential.getApiKey()));
    params.add(new BasicNameValuePair("from", mail.getBody().getFrom()));
    params.add(new BasicNameValuePair("fromName", mail.getBody().getFromName()));
    params.add(new BasicNameValuePair("subject", mail.getBody().getSubject()));
    params.add(new BasicNameValuePair("replyTo", mail.getBody().getReplyTo()));
    params.add(new BasicNameValuePair("to", mail.toString()));
    if (mail.getBody().getLabelId() != null) {
      params.add(new BasicNameValuePair("labelId", mail.getBody().getLabelId().toString()));
    }

    TextContent content = (TextContent) mail.getContent();
    if (content.getContent_type().equals(ScContentType.html)) {
      params.add(new BasicNameValuePair("html", content.getText()));
    } else {
      params.add(new BasicNameValuePair("plain", content.getText()));
    }

    if (mail.getBody().getXsmtpapi() != null && !mail.getBody().getXsmtpapi().containsKey("to")) {
      mail.getBody().addXsmtpapi("to", JSONArray.toJSONString(mail.getTo()));
    }
    if (MapUtils.isNotEmpty(mail.getBody().getHeaders())) {
      params.add(new BasicNameValuePair("headers", mail.getBody().getHeadersString()));
    }
    if (MapUtils.isNotEmpty(mail.getBody().getXsmtpapi())) {
      params.add(new BasicNameValuePair("xsmtpapi", mail.getBody().getXsmtpapiString()));
    }
    params.add(new BasicNameValuePair("respEmailId", "true"));
    params.add(new BasicNameValuePair("useNotification", "false"));

    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
    HttpPost httpPost = new HttpPost(credential.getSendAPI());
    httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
    HttpResponse response = httpclient.execute(httpPost);
    ResponseData result = validate(response);
    httpPost.releaseConnection();
    httpclient.close();
    return result;
  }

  /**
   * multipart方式发送
   */
  private ResponseData multipartPost(Credential credential, SendCloudMail mail) throws ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost(credential.getSendAPI());
    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
    MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    entity.setCharset(Charset.forName("UTF-8"));
    ContentType TEXT_PLAIN = ContentType.create("text/plain", Charset.forName("UTF-8"));
    entity.addTextBody("apiUser", credential.getApiUser(), TEXT_PLAIN);
    entity.addTextBody("apiKey", credential.getApiKey(), TEXT_PLAIN);
    entity.addTextBody("from", mail.getBody().getFrom(), TEXT_PLAIN);
    if (StringUtils.isNotEmpty(mail.getBody().getFromName())) {
      entity.addTextBody("fromName", mail.getBody().getFromName(), TEXT_PLAIN);
    }
    entity.addTextBody("subject", mail.getBody().getSubject(), TEXT_PLAIN);
    if (StringUtils.isNotEmpty(mail.getBody().getReplyTo())) {
      entity.addTextBody("replyTo", mail.getBody().getReplyTo(), TEXT_PLAIN);
    }
    if (mail.getBody().getLabelId() != null) {
      entity.addTextBody("labelId", mail.getBody().getLabelId().toString(), TEXT_PLAIN);
    }

    TextContent content = (TextContent) mail.getContent();
    if (content.getContent_type().equals(ScContentType.html)) {
      entity.addTextBody("html", content.getText(), TEXT_PLAIN);
    } else {
      entity.addTextBody("plain", content.getText(), TEXT_PLAIN);
    }

    if (mail.getBody().getXsmtpapi() == null || !mail.getBody().getXsmtpapi().containsKey("to")) {
      mail.getBody().addXsmtpapi("to", JSONArray.toJSONString(mail.getTo()));
    }
    if (MapUtils.isNotEmpty(mail.getBody().getHeaders())) {
      entity.addTextBody("headers", mail.getBody().getHeadersString(), TEXT_PLAIN);
    }
    if (MapUtils.isNotEmpty(mail.getBody().getXsmtpapi())) {
      entity.addTextBody("xsmtpapi", mail.getBody().getXsmtpapiString(), TEXT_PLAIN);
    }
    entity.addTextBody("respEmailId", "true", TEXT_PLAIN);
    entity.addTextBody("useNotification", "false", TEXT_PLAIN);

    ContentType OCTEC_STREAM = ContentType.create("application/octet-stream", Charset.forName("UTF-8"));
    for (Object o : mail.getBody().getAttachments()) {
      if (o instanceof File) {
        entity.addBinaryBody("attachments", (File) o, OCTEC_STREAM, ((File) o).getName());
      } else if (o instanceof Attachment) {
        entity.addBinaryBody("attachments", ((Attachment) o).getContent(), OCTEC_STREAM,
            ((Attachment) o).getName());
      } else {
        entity.addBinaryBody("attachments", (InputStream) o, OCTEC_STREAM, UUID.randomUUID().toString());
      }
    }
    httpPost.setEntity(entity.build());
    HttpResponse response = httpclient.execute(httpPost);
    ResponseData result = validate(response);
    httpPost.releaseConnection();
    httpclient.close();
    return result;
  }

  /**
   * 解析返回结果
   */
  private ResponseData validate(HttpResponse response) throws ParseException, IOException {
    String s = EntityUtils.toString(response.getEntity());
    ResponseData result = new ResponseData();
    JSONObject json = JSON.parseObject(s);
    if (json.containsKey("statusCode")) {
      result.setStatusCode(json.getIntValue("statusCode"));
      result.setMessage(json.getString("message"));
      result.setResult(json.getBoolean("result"));
      result.setInfo(json.getJSONObject("info").toString());
    } else {
      result.setStatusCode(500);
      result.setMessage(json.toString());
    }
    return result;
  }

}
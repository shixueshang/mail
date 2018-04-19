package com.coolgua.mail;

import com.coolgua.mail.thirdpart.webpower.Config;
import com.coolgua.mail.thirdpart.webpower.DynamicHttpClientCall;
import com.coolgua.mail.thirdpart.webpower.SoapUtil;
import com.coolgua.mail.util.Constant.WebpowerMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by lihongde on 2018/1/15 22:05.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWebpower {

  private static final String namespace = "\"http://dmdelivery.com/webservice/\"";
  private static final String location = "http://cndemo.webpower.asia/x/soap-v5.1/server.php";

  @Test
  public void test() throws Exception {
    String createMailingMethod = "createMailing";
    DynamicHttpClientCall createMailingCall = new DynamicHttpClientCall(namespace, createMailingMethod, location);

    Map<String, String> createMailingMap = new LinkedHashMap<String, String>();
    createMailingMap.put("login", "<username>coolgua1</username><password>Coolgua@123456</password>");
    createMailingMap.put("campaignID", "4213");
    createMailingMap.put("mailingName", "邮件主题201802031416583");
    createMailingMap.put("lang", "cn");
    createMailingMap.put("subject", "邮件主题");
    createMailingMap.put("fromName", "hejia");
    createMailingMap.put("senderID", "0");
    //String html = "<![CDATA[<html>this is the second testing mailing</html>]]>";
    //退订 <a herf={$PLUGINLINK=unsubscribe} >退订</a>
    String html = "<![CDATA[<html><body>您好,这是测试邮件，请忽略</body></html>]]>";
    createMailingMap.put("html", html);
    createMailingMap.put("preheader", "");
    String mailingId = handleResult(createMailingCall, createMailingMap, createMailingMethod);
    System.out.println("mailingId : " + mailingId);
  }

  private static String handleResult(DynamicHttpClientCall dynamicHttpclientCall, Map<String, String> patameterMap, String method) throws SOAPException {
    int statusCode = dynamicHttpclientCall.invoke(patameterMap);
    String responseString = dynamicHttpclientCall.soapResponseData;
    System.out.println("rrr : " + responseString);
    if (statusCode == HttpStatus.SC_OK) {
      //System.out.println("方法" + method + "调用成功！");
      SOAPMessage msg = SoapUtil.formatSoapString(responseString);
      SOAPBody body = msg.getSOAPBody();
      @SuppressWarnings("unchecked")
      Iterator<SOAPElement> iterator = body.getChildElements();

      if(method.equals("addRecipientsSendMailing")){
        List<Map<String, String>> recipientsResult = new ArrayList<>();
        List<Map<String, String>> responses = SoapUtil.printBody(iterator, null, recipientsResult);
        return responses.toString();
      }

      Map<String, String> m = new HashMap<>();
      Map<String, String> result = SoapUtil.printBody(iterator, null, m);
      if(method.equals("addGroup") || method.equals("addRecipient") || method.equals("createMailing")){
        return result.get("id");
      }
    } else {
      System.out.println("方法" + method + "调用失败 ,错误代码： " + statusCode);
    }
    return null;
  }


  @Test
  public void sendMailingScheduled() throws Exception{
    DynamicHttpClientCall addGroupCall = new DynamicHttpClientCall(namespace, WebpowerMethod.addGroup, location);
    Map<String, String> patameterMap = new LinkedHashMap<String, String>();
    patameterMap.put("login", "<username>coolgua1</username><password>Coolgua@123456</password>");
    patameterMap.put("campaignID", "4213");
    patameterMap.put("group", "<name>gna23me293</name><is_test>false</is_test><remarks></remarks>");
    addGroupCall.buildRequestData(patameterMap);
    String groupId = handleResult(addGroupCall, patameterMap, WebpowerMethod.addGroup);

    String method = "addRecipients";
    DynamicHttpClientCall adRecipientsCall = new DynamicHttpClientCall(namespace, method, location);
    Map<String, String> addRecipientsMap = new LinkedHashMap<String, String>();
    addRecipientsMap.put("login", "<username>coolgua1</username><password>Coolgua@123456</password>");
    addRecipientsMap.put("campaignID", "4213");
    addRecipientsMap.put("groupIDs", "<int>"+ groupId +"</int>");
    String s = "lihongde@coolgua.com";
    String[] toUsers = s.split(",");
    StringBuffer buffers = new StringBuffer();
    for(String toUser : toUsers){
      buffers.append("<recipients><fields><name>email</name><value>" + toUser + "</value></fields></recipients>");
    }
    addRecipientsMap.put("recipientDatas", buffers.toString());
    addRecipientsMap.put("addDuplisToGroups", "true");
    addRecipientsMap.put("overwrite", "true");
    adRecipientsCall.buildRequestData(addRecipientsMap);
    handleResult(adRecipientsCall, addRecipientsMap, method);


    String createMailingMethod = "createMailing";
    DynamicHttpClientCall createMailingCall = new DynamicHttpClientCall(namespace, createMailingMethod, location);

    Map<String, String> createMailingMap = new LinkedHashMap<String, String>();
    createMailingMap.put("login", "<username>coolgua1</username><password>Coolgua@123456</password>");
    createMailingMap.put("campaignID", "4213");
    createMailingMap.put("mailingName", "邮件主题2035r034541658323");
    createMailingMap.put("lang", "cn");
    createMailingMap.put("subject", "邮件主题");
    createMailingMap.put("fromName", "hejia");
    createMailingMap.put("senderID", "0");
    //String html = "<![CDATA[<html>this is the second testing mailing</html>]]>";
    //退订 <a herf={$PLUGINLINK=unsubscribe} >退订</a>
    String html = "<![CDATA[<html><body>您好,这是测试邮件，请忽略</body></html>]]>";
    createMailingMap.put("html", html);
    createMailingMap.put("preheader", "");
    String mailingId = handleResult(createMailingCall, createMailingMap, createMailingMethod);

    String sendScheduledMail = "sendMailingScheduled";
    DynamicHttpClientCall sendScheduledMailCall = new DynamicHttpClientCall(namespace, sendScheduledMail, location);
    Map<String, String> sendScheduledMailMap = new LinkedHashMap<String, String>();
    sendScheduledMailMap.put("login", "<username>coolgua1</username><password>Coolgua@123456</password>");
    sendScheduledMailMap.put("campaignID", "4213");
    sendScheduledMailMap.put("mailingID", mailingId);
    sendScheduledMailMap.put("sendDate", "2018-03-07 14:33:00");
    sendScheduledMailMap.put("isTest", "false");
    sendScheduledMailMap.put("resultsEmail", "");
    sendScheduledMailMap.put("groupIDs", "<int>" + groupId +"</int>");
    sendScheduledMailMap.put("langs", "<string>cn</string>");
    handleResult(sendScheduledMailCall, sendScheduledMailMap, sendScheduledMail);
  }

}

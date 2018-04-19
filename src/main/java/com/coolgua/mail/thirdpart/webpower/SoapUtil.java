package com.coolgua.mail.thirdpart.webpower;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

public class SoapUtil {

	/**
	 * 把soap字符串格式化为SOAPMessage
	 * 
	 * @param soapString
	 * @return
	 */
	public static SOAPMessage formatSoapString(String soapString) {
		SOAPMessage reqMsg = null;
		MessageFactory msgFactory;
		try {
			msgFactory = MessageFactory.newInstance();
			reqMsg = msgFactory.createMessage(new MimeHeaders(),new ByteArrayInputStream(soapString.getBytes("UTF-8")));
			reqMsg.saveChanges();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqMsg;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> printBody(Iterator<SOAPElement> iterator, String side, Map<String, String> resultMap) {
		while (iterator.hasNext()) {
			Object element = iterator.next();
			if(element instanceof SOAPElement){
				SOAPElement e = (SOAPElement)element;
				resultMap.put(e.getTagName(), e.getValue());
				if (null == e.getValue() && e.getChildElements().hasNext()) {
					printBody(e.getChildElements(), side, resultMap);
				}
			}
		}
		return resultMap;
	}

	public static List<Map<String, String>> printBody(Iterator<SOAPElement> iterator, String side, List<Map<String, String>> list) {
		while (iterator.hasNext()) {
			Object element = iterator.next();
			if (element instanceof SOAPElement) {
				SOAPElement e = (SOAPElement) element;
				if ("recipients".equals(e.getNodeName()) && "getRecipients".equals(side)) {
					Iterator<SOAPElement> it = e.getChildElements();
					Map<String, String> infoMap = new HashMap<String, String>();
					while (it.hasNext()) {
						SOAPElement el = it.next();
						if ("fields".equals(el.getNodeName())) {
							Iterator<SOAPElement> cit = el.getChildElements();
							while (cit.hasNext()) {
								SOAPElement cel = cit.next();
								if ("name".equals(cel.getNodeName())) {
									infoMap.put("name", cel.getValue());
								}
								if ("value".equals(cel.getNodeName())) {
									infoMap.put("value", cel.getValue());
								}
							}
						}
					}
					list.add(infoMap);
				}
				if("response".equals(e.getNodeName())){
				  Iterator<SOAPElement> it = e.getChildElements();
				  Map<String, String> infoMap = new HashMap<String, String>();
                  while (it.hasNext()) {
                    SOAPElement ele = it.next();
                    if ("field".equals(ele.getNodeName())) {
                      infoMap.put("field", ele.getValue());
                    }
                    if ("type".equals(ele.getNodeName())) {
                      infoMap.put("type", ele.getValue());
                    }
                    if("log_date".equals(ele.getNodeName())){
                      infoMap.put("log_date", ele.getValue().split("\\+")[0]);
                    }
                  }
                  list.add(infoMap);
				}
				if("addRecipientsSendMailing_result".equals(e.getNodeName())){
				  Iterator<SOAPElement> it = e.getChildElements();
				  while(it.hasNext()){
				    SOAPElement ele = it.next();
				    if("errors".equals(ele.getNodeName())){
				      Iterator<SOAPElement> cit = ele.getChildElements();
				      while(cit.hasNext()){
				        SOAPElement cele = cit.next();
				        if("recipient".equals(cele.getNodeName())){
				          Map<String, String> infoMap = new HashMap<String, String>();
				          Iterator<SOAPElement> ccit = cele.getChildElements();
				          while(ccit.hasNext()){
				            SOAPElement ccele = ccit.next();
				            if("fields".equals(ccele.getNodeName())){
				              Iterator<SOAPElement> cccit = ccele.getChildElements();
				              while(cccit.hasNext()){
				                SOAPElement cccele = cccit.next();
				                if("value".equals(cccele.getNodeName())){
				                  infoMap.put("value", cccele.getValue());
				                }
				              }
				            }
				            if("DMDmessage".equals(ccele.getNodeName())){
				              infoMap.put("DMDmessage", ccele.getValue());
				            }
				          }
				          list.add(infoMap);
				        }
				      }
				    }
				  }
				}
				if (null == e.getValue() && e.getChildElements().hasNext()) {
					printBody(e.getChildElements(), side, list);
				}
			}

		}
		return list;
	}

}
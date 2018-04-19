package com.coolgua.mail.thirdpart.webpower;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DynamicHttpClientCall {

	private Logger logger = LoggerFactory.getLogger(DynamicHttpClientCall.class);

	private String namespace;
	private String methodName;
	private String wsdlLocation;
	public String soapResponseData;

	public DynamicHttpClientCall(String namespace, String methodName, String wsdlLocation) {
		this.namespace = namespace;
		this.methodName = methodName;
		this.wsdlLocation = wsdlLocation;
	}

	/**
	 * @param patameterMap
	 * @return
	 */
	public int invoke(Map<String, String> patameterMap){
		String soapRequestData = buildRequestData(patameterMap);
		CloseableHttpClient client = null;
		HttpPost httpPost = null;
		int statusCode = 0;
		try {
			httpPost = new HttpPost(wsdlLocation);
			StringEntity stringEntity = new StringEntity(soapRequestData, "UTF-8");
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-type", "text/xml; charset=UTF-8");
			client = new DefaultHttpClient();
			HttpResponse response = client.execute(httpPost);
			soapResponseData = EntityUtils.toString(response.getEntity());
			statusCode = response.getStatusLine().getStatusCode();
		} catch (IOException e) {
			logger.error("发送请求报文失败, 请检查相关参数");
			e.printStackTrace();
		}finally{
			if(httpPost != null){
				httpPost.abort();
			}
			if(client != null){
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return statusCode;
	}
	
	public String buildRequestData(Map<String, String> patameterMap) {
		StringBuilder soapRequestData = new StringBuilder();
		soapRequestData.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapRequestData.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web="
						+ namespace + ">");
		soapRequestData.append("<soapenv:Header/>");
		soapRequestData.append("<soapenv:Body>");
		soapRequestData.append("<web:" + methodName + ">");

		Set<String> keySet = patameterMap.keySet();
		for (String key : keySet) {
			soapRequestData.append("<" + key + ">" + patameterMap.get(key) + "</" + key + ">");
		}

		soapRequestData.append("</web:" + methodName + ">");
		soapRequestData.append("</soapenv:Body>");
		soapRequestData.append("</soapenv:Envelope>");

		return soapRequestData.toString();
	}

}

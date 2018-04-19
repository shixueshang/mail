package com.coolgua.mail.util;


import static com.coolgua.mail.util.Constant.MAIL_PROVIDER.SENDCLOUD;
import static com.coolgua.mail.util.Constant.MAIL_PROVIDER.WEBPOWER;
import static com.coolgua.mail.util.Constant.MailStatus.STATUS_DELETED;
import static com.coolgua.mail.util.Constant.MailStatus.STATUS_SENT;
import static com.coolgua.mail.util.Constant.MailStatus.STATUS_TIMED;
import static com.coolgua.mail.util.Constant.UnsubscribeLanguage.LANG_CN;
import static com.coolgua.mail.util.Constant.UnsubscribeLanguage.LANG_EN;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constant {
	
	/**
	 * 默认分页起始页
	 */
	public static final Integer DEFAULT_PAGE = 1;
	/**
	 * 默认页面大小
	 */
	public static final Integer DEFAULT_PAGE_SIZE = 10;

	public static final String MAIL_FIELD = "邮箱";


	public static final String URLS = "urls";

	public static final String REG_MAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	public static final String REG_FILEEXT = "^.*?\\.(jpg|jpeg|bmp|gif|png|docx|doc|xlsx|xls)$";

	/**
	 * 服务器返回状态
	 * @author lihongde
	 *
	 */
	public static class JSON_RESULT{
		/**
		 * 成功
		 */
		public static int OK = 200;

		/**
		 * 客户端请求无效
		 */
		public static int BAD_REQUEST = 400;

		/**
		 * token失效或非法，需要重新登录
		 */
		public static int NEED_LOGIN = 401;

		/**
		 * 普通失败，message中带原因
		 */
		public static int FAILED = 403;

		/**
		 * 未找到请求页面
		 */
		public static int NOT_FOUND = 404;

		/**
		 * 服务器错误
		 */
		public static int SERVER_ERROR = 500;

	}

	/**
	 * 邮件提供商
	 */
	public static class MAIL_PROVIDER{

		/**
		 * sendcloud
		 */
		public static final int SENDCLOUD = 1;

		/**
		 * webpower
		 */
		public static final int WEBPOWER = 2;
	}

	public static final Map<Integer, String> mailProviderMap = new ConcurrentHashMap<>();
	static {
		mailProviderMap.put(SENDCLOUD, "sendcloud");
		mailProviderMap.put(WEBPOWER, "webpower");
	}

	/**
	 * 是否
	 */
	public static class  CommonStatus{

		public static final Integer NO = 0;

		public static final Integer YES = 1;
	}


	/**
	 * webpower接口
	 */
	public static class WebpowerMethod{
		public static final String createMailing = "createMailing";
		public static final String addGroup = "addGroup";
		public static final String addRecipient = "addRecipient";
		public static final String sendSingleMailing = "sendSingleMailing";
		public static final String addRecipientsSendMailing = "addRecipientsSendMailing";
		public static final String getMailingResponse = "getMailingResponse";
		public static final String getMailingBounce = "getMailingBounce";
		public static final String getRecipients = "getRecipients";
		public static final String getMailingStatsSummary = "getMailingStatsSummary";
		public static final String addRecipients = "addRecipients";
		public static final String sendMailingScheduled = "sendMailingScheduled";
	}

	/**
	 * 退订语言
	 */
	public static class UnsubscribeLanguage{

		/**
		 * 中文
		 */
		public static final String LANGUAGE_CN = "退订";
		public static final String LANG_CN = "cn";
		/**
		 * 日文
		 */
		public static final String LANGUAGE_JA = "を取り消す";
		public static final String LANG_JA = "ja";
		public static final String LANG_JP = "jp";
		/**
		 * 英文
		 */
		public static final String LANGUAGE_EN = "unsubscribe";
		public static final String LANG_EN = "en";
	}

	public static final Map<String, String> LANGUAGE = new HashMap<String, String>();
	static{
		LANGUAGE.put(LANG_CN, "中文");
		//LANGUAGE.put(LANG_JP, "日文");
		LANGUAGE.put(LANG_EN, "英文");
	}


	/**
	 * 邮件状态
	 */
	public static class MailStatus{
		// 等待发送
		public static final int STATUS_TIMED = 1;
		// 已发送
		public static final int STATUS_SENT = 2;
		// 已删除
		public static final int STATUS_DELETED = 3;
	}

	public static Map<Integer, String> statusMap = new HashMap<Integer, String>();
	static {
		statusMap.put(STATUS_TIMED, "等待发送");
		statusMap.put(STATUS_SENT, "已发送");
		statusMap.put(STATUS_DELETED, "已删除");
	}

	/**
	 * 详细状态
	 */
	public static class MailDetailStatus{
		//待发送(未提交)
		public static final int DETAIL_INIT = 0;
		//已提交
		public static final int DETAIL_REQUEST = 1;
		//提交失败
		public static final int DETAIL_SUBMIT_FAILURE = 2;
		//已送达
		public static final int DETAIL_DELIVER = 3;
		//无效邮址
		public static final int DETAIL_INVALID_ADDRESS = 4;
		//重复邮址
		public static final int DETAIL_REPEAT_ADDRESS = 5;
		//硬退
		public static final int DETAIL_BOUNCE = 6;
		//软退
		public static final int DETAIL_SOFT_BOUNCE = 7;
		//垃圾邮件
		public static final int DETAIL_SPAM = 8;
		//退订
		public static final int DETAIL_UNSUBSCRIBE = 9;
		//已打开
		public static final int DETAIL_OPEN = 10;
		//已点击
		public static final int DETAIL_CLICK = 11;
		//排除列表
		public static final int DETAIL_EXCLUSION = 12;
		//通道寄出
		public static final int DETAIL_CHANNEL_REQUEST = 13;

	}

	public static class MailEvent{
		//待发送
		public static final String EVENT_INIT = "init";
		//已提交
		public static final String EVENT_REQUEST = "request";
		//提交失败
		public static final String EVENT_SUBMIT_FAILURE = "submitFailure";
		//已送达
		public static final String EVENT_DELIVER = "deliver";
		//无效邮址
		public static final String EVENT_INVALID_ADDRESS = "invalid";
		//重复邮址
		public static final String EVENT_REPEAT_ADDRESS = "repeatAddress";
		//硬退
		public static final String EVENT_BOUNCE = "bounce";
		//软退
		public static final String EVENT_SOFT_BOUNCE = "softBounce";
		//垃圾邮件
		public static final String EVENT_SPAM = "spam";
		//退订
		public static final String EVENT_UNSUBSCRIBE = "unsubscribe";
		//已打开
		public static final String EVENT_OPEN = "open";
		//已点击
		public static final String EVENT_CLICK = "click";
		//排除列表
		public static final String EVENT_EXCLUSION = "exclusion";
		//通道寄出
		public static final String EVENT_CHANNEL_REQUEST = "channelRequest";
	}

	public static final Map<Integer, String> mailDetailStatusMap = new HashMap<>();
	static {
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_INIT, "未提交");
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_REQUEST, "提交成功");
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_SUBMIT_FAILURE, "提交失败");
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_DELIVER, "已送达");
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_INVALID_ADDRESS, "无效邮址");
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_REPEAT_ADDRESS, "重复邮址");
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_BOUNCE, "退信");
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_SOFT_BOUNCE, "软退信");
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_SPAM, "垃圾邮箱");
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_UNSUBSCRIBE, "已退订");
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_OPEN, "已打开");
		mailDetailStatusMap.put(MailDetailStatus.DETAIL_CLICK, "已点击");
	}

	public static final Map<String, String> mailEventMap = new HashMap<>();
	static {
		mailEventMap.put(MailEvent.EVENT_INIT, "未提交");
		mailEventMap.put(MailEvent.EVENT_REQUEST, "提交成功");
		mailEventMap.put(MailEvent.EVENT_SUBMIT_FAILURE, "提交失败");
		mailEventMap.put(MailEvent.EVENT_DELIVER, "已送达");
		mailEventMap.put(MailEvent.EVENT_INVALID_ADDRESS, "无效邮址");
		mailEventMap.put(MailEvent.EVENT_REPEAT_ADDRESS, "重复邮址");
		mailEventMap.put(MailEvent.EVENT_BOUNCE, "退信");
		mailEventMap.put(MailEvent.EVENT_SOFT_BOUNCE, "软退信");
		mailEventMap.put(MailEvent.EVENT_SPAM, "垃圾邮箱");
		mailEventMap.put(MailEvent.EVENT_UNSUBSCRIBE, "已退订");
		mailEventMap.put(MailEvent.EVENT_OPEN, "已打开");
		mailEventMap.put(MailEvent.EVENT_CLICK, "已点击");
	}

	/**
	 * 标准邮件
	 */
	public static final Integer STANDARD = 0;
	/**
	 * 个性邮件
	 */
	public static final Integer PERSONALITY = 1;

	public static final  Map<Integer, String> mailTypeMap = new ConcurrentHashMap<>();
	static {
		mailTypeMap.put(STANDARD, "标准邮件");
		mailTypeMap.put(PERSONALITY, "个性邮件");
	}

}

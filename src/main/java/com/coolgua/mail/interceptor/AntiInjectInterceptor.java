package com.coolgua.mail.interceptor;

import com.coolgua.mail.config.AntiInjectConfig;
import com.coolgua.mail.exception.FailedReqeustException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AntiInjectInterceptor extends HandlerInterceptorAdapter {

  private static final Logger log = Logger.getLogger(AntiInjectInterceptor.class);

  @Resource
  private AntiInjectConfig antiInjectConfig;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.debug("------------AntiInjectInterceptor begin--------------");
    String servletPath = request.getServletPath();
    String url = request.getPathInfo() == null ? servletPath : servletPath + request.getPathInfo();

    // 获取请求所有参数，校验防止SQL注入，防止XSS漏洞
    checkParams(request, url, request.getParameterNames());
    // 请求头参数防注入
    checkParams(request, url, request.getHeaderNames());
    return super.preHandle(request, response, handler);
  }

  private void checkParams(HttpServletRequest request, String url, Enumeration<?> params) {
    String paramName = null;
    String paramVale = null;
    while (params.hasMoreElements()) {
      paramName = (String) params.nextElement();
      paramVale = xssFilter(request.getParameter(paramName));
      // 校验是否存在SQL/xss注入信息
      if (checkInject(paramName, paramVale, url)) {
        throw new FailedReqeustException("非法内容：" + paramVale);
      }
    }
  }

  /**
   * 检查是否存在非法字符，防止SQL注入
   *
   * @param str 被检查的字符串
   * @return ture-字符串中存在非法字符，false-不存在非法字符
   */
  private boolean checkInject(String paramName, String str, String url) {
    if (StringUtils.isEmpty(str)) {
      return false;// 如果传入空串则认为不存在非法字符
    }

    String value = str.toLowerCase(); // 不区分大小写
    Set<String> words = getWords(value);
    if (words.size() > 0) {
      String sql = antiInjectConfig.getSql();
      if (sql != null && sql.trim().length() > 0) {
        List<String> antiSqlArray = Arrays.asList(sql.split("\\|"));
        for (String word : words) {
          if (antiSqlArray.contains(word)) {
            log.info("xss防攻击拦截url:" + url + "，原因：" + paramName + "包含特殊字符" + word + "，传入数据为=" + str);
            return true;
          }
        }
      }

      String xss = antiInjectConfig.getXss();
      if (xss != null && xss.trim().length() > 0) {
        String ignore = antiInjectConfig.getIgnore();
        List<String> ignoreArray = Arrays.asList(ignore.split("\\|"));
        if (!ignoreArray.contains(paramName)) { //如果该属性忽略xss攻击, 则不检查
          List<String> antiXssArray = Arrays.asList(xss.split("\\|"));
          for (String word : words) {
            if (antiXssArray.contains(word)) {
              log.info("xss防攻击拦截url:" + url + "，原因：" + paramName + "包含特殊字符" + word + "，传入数据为=" + str);
              return true;
            }
          }
        }
      }
    }

    return false;
  }


  /**
   * 过滤文本的内容, 将容易引起xss漏洞的半角字符直接替换成全角字符
   */
  private String xssFilter(String str) {
    if (StringUtils.isEmpty(str)) {
      return str;
    }

    StringBuilder sb = new StringBuilder(str.length() + 16);
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      switch (c) {
        case '>':
          sb.append('＞');// 全角大于号
          break;
        case '<':
          sb.append('＜');// 全角小于号
          break;
        case '\'':
          sb.append('‘');// 全角单引号
          break;
        case '\"':
          sb.append('“');// 全角双引号
          break;
        case '&':
          sb.append('＆');// 全角
          break;
        case '\\':
          sb.append('＼');// 全角斜线
          break;
        case '#':
          sb.append('＃');// 全角井号
          break;
        case '(':
          sb.append('（');//
          break;
        case ')':
          sb.append('）');//
          break;
        default:
          sb.append(c);
          break;
      }
    }
    return sb.toString();
  }


  private Set<String> getWords(String str) {
    String matcher = "(?:('.+?')|([a-zA-Z]+))"; // 正则表达式, 提取单词
    Set<String> list = new HashSet<String>();
    Matcher m = Pattern.compile(matcher).matcher(str);
    while (m.find()) {
      String val = m.group(0);
      list.add(val);
    }
    return list;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    super.postHandle(request, response, handler, modelAndView);
  }
}

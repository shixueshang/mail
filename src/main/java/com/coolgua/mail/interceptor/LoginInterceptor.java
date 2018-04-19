package com.coolgua.mail.interceptor;

import com.coolgua.common.domain.User;
import com.coolgua.common.service.RedisService;
import com.coolgua.common.util.JsonUtils;
import com.coolgua.common.util.SessionUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by lihongde on 2018/1/23 11:53.
 */
public class LoginInterceptor implements HandlerInterceptor {

  @Resource
  private RedisService redisService;
  @Value("${SSO_BASE_URL}")
  private String SSO_BASE_URL;
  @Value("${REDIS_USER_SESSION_KEY:REDIS_USER_SESSION}")
  private String REDIS_USER_SESSION_KEY;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
    String[] params = request.getRequestURI().split("/");
    String fp = params[3];
    String token = SessionUtil.createToken(request, fp);
    String userJson = redisService.get(REDIS_USER_SESSION_KEY + ":" + token);
    if(StringUtils.isBlank(userJson)){
      User user = SessionUtil.getUserByToken(request, SSO_BASE_URL, token);
      if(user == null){
        response.sendRedirect(this.SSO_BASE_URL + "?redirect=" + this.getBaseURL(request));
        return false;
      }
      userJson = JsonUtils.objectToJson(user);
      redisService.set(REDIS_USER_SESSION_KEY + ":" + token, userJson);
    }
    redisService.expire(REDIS_USER_SESSION_KEY + ":" + token, 18000);
    return true;
  }

  private String getBaseURL(HttpServletRequest request) {
    StringBuilder sb = new StringBuilder(request.getScheme());
    sb.append("://").append(request.getServerName());
    int port = request.getServerPort();
    if(port != 80 && port != 443) {
      sb.append(":").append(port);
    }

    sb.append(request.getContextPath());
    return sb.toString();
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

  }
}

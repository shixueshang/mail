package com.coolgua.mail.controller;

import static com.coolgua.mail.util.Constant.mailEventMap;

import com.coolgua.common.domain.User;
import com.coolgua.common.service.RedisService;
import com.coolgua.common.util.SessionUtil;
import com.coolgua.mail.domain.BlackList;
import com.coolgua.mail.exception.FailedReqeustException;
import com.coolgua.mail.service.BlackListService;
import com.coolgua.mail.util.AjaxJson;
import com.coolgua.mail.util.Constant.JSON_RESULT;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihongde on 2018/1/26 21:23.
 */
@RestController
@RequestMapping(value = "/interface/blacklist/{fp}/{orgId}/{userId}")
public class BlackListController extends BaseController {

  @Value("${REDIS_USER_SESSION_KEY:REDIS_USER_SESSION}")
  private String REDIS_USER_SESSION_KEY;
  @Value("${SSO_BASE_URL}")
  private String SSO_BASE_URL;
  @Resource
  private BlackListService blackListService;
  @Resource
  private RedisService redisService;

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public AjaxJson addBlackList(BlackList blackList, @PathVariable Integer orgId, @PathVariable String userId, @PathVariable String fp){
    try{

      String token = SessionUtil.createToken(request, fp);
      String userJson = redisService.get(REDIS_USER_SESSION_KEY + ":" + token);
      User user = SessionUtil.getUserByToken(request, SSO_BASE_URL, token);

      blackList.setOrgId(orgId);
      blackList.setCreator(userId);
      blackList.setCreatorName(user.getAccountName());
      blackList.setCreateTime(new Date());
      blackListService.addBlackList(blackList);
    }catch (Exception e){
      throw new FailedReqeustException("添加黑名单失败");
    }
    return new AjaxJson(JSON_RESULT.OK);
  }

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public AjaxJson list(@RequestParam(required = false) String recipient, @PathVariable Integer orgId){
    Map<String, Object> result = new HashMap<>();
    result.put("page", blackListService.findPageBlackList(orgId, recipient, page, size));
    result.put("mailEventMap", mailEventMap);
    return new AjaxJson(JSON_RESULT.OK, result);
  }

  @RequestMapping(value = "/remove", method = RequestMethod.POST)
  public AjaxJson remove(@RequestParam(value = "ids[]") String[] ids){
    try{
      blackListService.remove(Arrays.asList(ids));
    }catch (Exception e){
      throw new FailedReqeustException("移除黑名单失败");
    }

    return new AjaxJson(JSON_RESULT.OK);
  }

}

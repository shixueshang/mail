package com.coolgua.mail.exception;

import com.coolgua.mail.util.AjaxJson;
import com.coolgua.mail.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BusinessExceptionHandler {

  private Logger logger = LoggerFactory.getLogger(BusinessExceptionHandler.class);


  @ExceptionHandler(FailedReqeustException.class)
  @ResponseBody
  public AjaxJson handleInvalidRequestError(FailedReqeustException ex) {
    return ex.getResult();
  }


  @ExceptionHandler(BadRequestException.class)
  @ResponseBody
  public AjaxJson handleInvalidRequestError(BadRequestException ex) {
    return ex.getResult();
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseBody
  public AjaxJson handleInvalidRequestError(RuntimeException ex) {
    AjaxJson result = new AjaxJson();
    result.setCode(Constant.JSON_RESULT.SERVER_ERROR);
    result.setMessage("server error");
    logger.error(ex + "");
    return result;
  }

}

package com.coolgua.mail.exception;

import com.coolgua.mail.util.AjaxJson;
import com.coolgua.mail.util.Constant.JSON_RESULT;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "The request you send to Server was failed")
public class FailedReqeustException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private AjaxJson result;

	public FailedReqeustException(String message) {
		super(message);
		this.result = new AjaxJson();
		this.result.setCode(JSON_RESULT.SERVER_ERROR);
		this.result.setMessage(message);
	}

	public AjaxJson getResult() {
		return result;
	}

	public void setResult(AjaxJson result) {
		this.result = result;
	}
}



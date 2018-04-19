package com.coolgua.mail.exception;

import com.coolgua.mail.util.AjaxJson;
import com.coolgua.mail.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The request you send to Server was bad.")
public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private AjaxJson result;

	public BadRequestException(String message) {
		super(message);
		this.result = new AjaxJson();
		this.result.setCode(Constant.JSON_RESULT.BAD_REQUEST);
		this.result.setMessage(message);
	}

	public AjaxJson getResult() {
		return result;
	}

	public void setResult(AjaxJson result) {
		this.result = result;
	}
}

package com.coolgua.mail.thirdpart.sendcloud.sdk.model;


import com.coolgua.mail.thirdpart.sendcloud.sdk.exception.ContentException;

public interface Content {

	public boolean validate() throws ContentException;
}
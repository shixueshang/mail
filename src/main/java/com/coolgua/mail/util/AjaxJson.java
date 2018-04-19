package com.coolgua.mail.util;

import java.util.List;
import java.util.Map;

public class AjaxJson {
	private int code;
	private List<Map<String, Object>> list;
	private Map<String, Object> map;
	private String message = "";
	private Object data;

	public AjaxJson() {
		super();
	}

	public AjaxJson(int code) {
		this.code = code;
	}

	public AjaxJson(int code, List<Map<String, Object>> list) {
		this.code = code;
		this.list = list;
	}

	public AjaxJson(int code, Map<String, Object> map) {
		this.code = code;
		this.map = map;
	}

	public AjaxJson(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public AjaxJson(int code, Object data) {
		this.code = code;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
}

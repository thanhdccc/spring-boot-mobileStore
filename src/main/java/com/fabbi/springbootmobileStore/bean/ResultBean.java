package com.fabbi.springbootmobileStore.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResultBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Object data;

	private String message;

	public ResultBean() {
	}

	public ResultBean(Object data) {
		this.data = data;
	}

	public ResultBean(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

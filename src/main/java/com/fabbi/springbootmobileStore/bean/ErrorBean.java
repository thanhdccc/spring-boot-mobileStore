package com.fabbi.springbootmobileStore.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("field")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String field;
	@JsonProperty("message")
	private String message;
	@JsonProperty("errorCode")
	private String errorCode;

	public ErrorBean() {
	}

	public ErrorBean(String field, String message, String errorCode) {
		this.field = field;
		this.message = message;
		this.errorCode = errorCode;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}

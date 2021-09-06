package com.fabbi.springbootmobileStore.exception;

import org.springframework.http.HttpStatus;

public class CommonException extends RuntimeException {

	private static final long serialVersionUID = 5269283481236191340L;

	private String message;
	private HttpStatus httpStatus;
	private String errorCode;

	/**
	 * @return the message
	 */

	public CommonException(String message, HttpStatus httpStatus, String errorCode) {
		this.message = message;
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
	}

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
	 * @return the httpStatus
	 */
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	/**
	 * @param httpStatus the httpStatus to set
	 */
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}

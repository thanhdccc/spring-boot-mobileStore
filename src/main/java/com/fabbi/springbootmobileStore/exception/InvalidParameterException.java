/*
 * 
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 * 
 */
package com.fabbi.springbootmobileStore.exception;

/**
 *
 * @author QuangND
 */
public class InvalidParameterException extends RuntimeException {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String field;
	private String message;
	private String errorCode;

	public InvalidParameterException() {
		super();
	}

	public InvalidParameterException(String field, String message, String errorCode) {
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

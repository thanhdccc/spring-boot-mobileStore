package com.fabbi.springbootmobileStore.exception;

import java.util.ArrayList;
import java.util.List;

import com.fabbi.springbootmobileStore.bean.ErrorBean;

public class LogicErrorException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ErrorBean> errorList;

	public LogicErrorException() {
		super();
	}

	public LogicErrorException(String message, String errorCode) {
		this.errorList = new ArrayList<>();
		ErrorBean error = new ErrorBean();
		error.setMessage(message);
		error.setErrorCode(errorCode);
		this.errorList.add(error);
	}

	public LogicErrorException(String field, String message, String errorCode) {
		this.errorList = new ArrayList<>();
		this.errorList.add(new ErrorBean(field, message, errorCode));
	}

	public LogicErrorException(List<ErrorBean> errorList) {
		this.errorList = errorList;
	}

	public List<ErrorBean> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<ErrorBean> errorList) {
		this.errorList = errorList;
	}

}

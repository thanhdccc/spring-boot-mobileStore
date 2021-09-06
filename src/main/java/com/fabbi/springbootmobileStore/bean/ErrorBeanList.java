package com.fabbi.springbootmobileStore.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ErrorBeanList implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private List<ErrorBean> errorList;

	public ErrorBeanList() {
		errorList = new ArrayList<ErrorBean>();
	}

	public ErrorBeanList(List<ErrorBean> errorList) {
		this.errorList = errorList;
	}

	/**
	 * @return the errorList
	 */
	public List<ErrorBean> getErrorList() {
		return errorList;
	}

	/**
	 * @param errorList the errorList to set
	 */
	public void setErrorList(List<ErrorBean> errorList) {
		this.errorList = errorList;
	}

}

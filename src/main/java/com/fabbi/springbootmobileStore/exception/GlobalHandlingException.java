package com.fabbi.springbootmobileStore.exception;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fabbi.springbootmobileStore.bean.ErrorBean;
import com.fabbi.springbootmobileStore.bean.ErrorBeanList;
import com.fasterxml.jackson.databind.JsonMappingException;

@ControllerAdvice
public class GlobalHandlingException {

	private Logger logger;

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorBeanList> handleException(MethodArgumentNotValidException exception) {
		ErrorBeanList errorList = new ErrorBeanList();
		List<ErrorBean> errors = new ArrayList<ErrorBean>();
		exception.getBindingResult().getFieldErrors().stream().forEach(p -> {
			ErrorBean error = new ErrorBean();
			error.setErrorCode(p.getDefaultMessage());
			error.setField(p.getField());
			error.setMessage(p.getDefaultMessage());
			errors.add(error);

		});
		errorList.setErrorList(errors);
		return new ResponseEntity<ErrorBeanList>(errorList, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = LogicErrorException.class)
	public ResponseEntity<ErrorBeanList> handleException(LogicErrorException exception) {
		ErrorBeanList errorList = new ErrorBeanList();
		List<ErrorBean> errors = new ArrayList<ErrorBean>();
		exception.getErrorList().stream().forEach(p -> {
			ErrorBean error = new ErrorBean();
			error.setField(p.getField());
			error.setMessage(p.getMessage());
			error.setErrorCode(p.getErrorCode());
			errors.add(error);

		});
		errorList.setErrorList(errors);
		return new ResponseEntity<ErrorBeanList>(errorList, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorBeanList> handleException(Exception exception) {
		logger = LogManager.getLogger(exception.getClass());
		logger.error("An error has occur: " + exception);
		ErrorBeanList errorList = new ErrorBeanList();
		List<ErrorBean> errors = new ArrayList<ErrorBean>();
		ErrorBean error = new ErrorBean();
		error.setMessage(exception.getMessage());
		error.setErrorCode("common.error");
		errors.add(error);
		errorList.setErrorList(errors);
		return new ResponseEntity<ErrorBeanList>(errorList, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(value = InvalidParameterException.class)
	public ResponseEntity<ErrorBeanList> handleException(InvalidParameterException exception) {
		ErrorBeanList errorList = new ErrorBeanList();
		List<ErrorBean> errors = new ArrayList<ErrorBean>();
		ErrorBean error = new ErrorBean();
		error.setField(exception.getField());
		error.setMessage(exception.getMessage());
		error.setErrorCode(exception.getErrorCode());
		errors.add(error);
		errorList.setErrorList(errors);
		return new ResponseEntity<ErrorBeanList>(errorList, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CommonException.class)
	public ResponseEntity<ErrorBeanList> handleCommonException(CommonException exception) {
		ErrorBeanList errorList = new ErrorBeanList();
		List<ErrorBean> errors = new ArrayList<ErrorBean>();
		ErrorBean errorBean = new ErrorBean();
		errorBean.setMessage(exception.getMessage());
		errorBean.setErrorCode(exception.getErrorCode());
		errors.add(errorBean);
		errorList.setErrorList(errors);
		return new ResponseEntity<ErrorBeanList>(errorList, exception.getHttpStatus());
	}

	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorBeanList> handleCommonException(MethodArgumentTypeMismatchException exception) {
		ErrorBeanList errorList = new ErrorBeanList();
		List<ErrorBean> errors = new ArrayList<ErrorBean>();
		ErrorBean errorBean = new ErrorBean();
		errorBean.setField(exception.getName());
		errorBean.setErrorCode("param." + exception.getRequiredType().getSimpleName().toLowerCase() + ".type.error");
		errorBean.setMessage(exception.getName() + " should be of type " + exception.getRequiredType().getName());
		errors.add(errorBean);
		errorList.setErrorList(errors);
		return new ResponseEntity<ErrorBeanList>(errorList, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorBeanList> handleCommonException(HttpMessageNotReadableException exception) {
		Throwable throwable = exception.getCause();
		JsonMappingException jsonMappingException = ((JsonMappingException) throwable);
		List<JsonMappingException.Reference> references = jsonMappingException.getPath();
		ErrorBeanList errorList = new ErrorBeanList();
		List<ErrorBean> errors = new ArrayList<ErrorBean>();
		ErrorBean errorBean = new ErrorBean();
		for (JsonMappingException.Reference reference : references) {
			if (reference.getFieldName() != null) {
				errorBean.setField(reference.getFieldName());
				errorBean.setErrorCode("json.mapping.type.error");
				errorBean.setMessage(jsonMappingException.getOriginalMessage());

			}
		}
		errors.add(errorBean);
		errorList.setErrorList(errors);
		return new ResponseEntity<ErrorBeanList>(errorList, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = NoHandlerFoundException.class)
	public ResponseEntity<ErrorBeanList> handleCommonException(NoHandlerFoundException exception) {
		ErrorBeanList errorList = new ErrorBeanList();
		List<ErrorBean> errors = new ArrayList<ErrorBean>();
		ErrorBean errorBean = new ErrorBean();
		errorBean.setErrorCode("no.handler.error");
		errorBean.setMessage(exception.getMessage());
		errors.add(errorBean);
		errorList.setErrorList(errors);
		return new ResponseEntity<ErrorBeanList>(errorList, HttpStatus.BAD_REQUEST);
	}
}

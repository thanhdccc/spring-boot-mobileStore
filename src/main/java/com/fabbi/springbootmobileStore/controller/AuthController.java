package com.fabbi.springbootmobileStore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.constant.Constants;
import com.fabbi.springbootmobileStore.dto.SigninDTO;
import com.fabbi.springbootmobileStore.dto.SignupDTO;
import com.fabbi.springbootmobileStore.service.AuthService;

@CrossOrigin
@RestController
@RequestMapping(value = Constants.API_PATH + Constants.AUTH_PATH)
public class AuthController {

	@Autowired
	AuthService authService;

	@RequestMapping(value = Constants.AUTH_API_SIGNIN, method = RequestMethod.POST)
	public ResponseEntity<ResultBean> authenticateUser(@Valid @RequestBody SigninDTO loginRequest) {
		ResultBean resultBean = authService.login(loginRequest);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = Constants.AUTH_API_SIGNUP, method = RequestMethod.POST)
	public ResponseEntity<ResultBean> registerUser(@Valid @RequestBody SignupDTO signupRequest) {
		ResultBean resultBean = authService.register(signupRequest);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}
}

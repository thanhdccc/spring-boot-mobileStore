package com.fabbi.springbootmobileStore.service;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.dto.SigninDTO;
import com.fabbi.springbootmobileStore.dto.SignupDTO;

public interface AuthService {

	ResultBean login(SigninDTO request);
	
	ResultBean register(SignupDTO request);
}

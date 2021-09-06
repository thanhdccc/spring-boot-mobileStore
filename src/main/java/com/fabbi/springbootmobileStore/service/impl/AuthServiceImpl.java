package com.fabbi.springbootmobileStore.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.constant.Constants;
import com.fabbi.springbootmobileStore.dto.JwtDTO;
import com.fabbi.springbootmobileStore.dto.SigninDTO;
import com.fabbi.springbootmobileStore.dto.SignupDTO;
import com.fabbi.springbootmobileStore.dto.UserDetailsImpl;
import com.fabbi.springbootmobileStore.entity.RoleEntity;
import com.fabbi.springbootmobileStore.entity.UserEntity;
import com.fabbi.springbootmobileStore.exception.LogicErrorException;
import com.fabbi.springbootmobileStore.repository.RoleRepository;
import com.fabbi.springbootmobileStore.repository.UserRepository;
import com.fabbi.springbootmobileStore.service.AuthService;
import com.fabbi.springbootmobileStore.util.ERole;
import com.fabbi.springbootmobileStore.util.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtUtils jwtUtils;

	private ResultBean resultBean;

	@Override
	public ResultBean login(SigninDTO request) {
		resultBean = new ResultBean();
		Authentication authentication = null;
		log.info("######## Begin get user ########");
		
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (Exception e) {
			log.error("Cannot login because username or password is incorrect");
			throw new LogicErrorException(Constants.USER_PROP_USERNAME_PASSWORD, Constants.LOGIN_FAIL,
					Constants.LOGIN_FAIL);
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(r -> r.getAuthority())
				.collect(Collectors.toList());

		resultBean.setMessage(Constants.LOGIN_SUCCESS);
		resultBean.setData(new JwtDTO(jwt, userDetails.getId(), userDetails.getUsername(), roles));
		
		log.info("######## End insert user ########");

		return resultBean;
	}

	@Override
	public ResultBean register(SignupDTO request) {
		resultBean = new ResultBean();
		log.info("######## Begin insert user ########");
		
		if (userRepository.existsByUsername(request.getUsername())) {
			log.error("Cannot insert because username is already taken");
			throw new LogicErrorException(Constants.USER_PROP_USERNAME, Constants.USERNAME_EXIST_ERROR,
					Constants.REGISTER_FAIL);
		}

		UserEntity user = new UserEntity(request.getUsername(), passwordEncoder.encode(request.getPassword()));
		Set<String> strRoles = request.getRole();
		Set<RoleEntity> roles = new HashSet<>();

		if (strRoles == null) {
			RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new LogicErrorException(Constants.USER_PROP_ROLE, Constants.ROLE_NOT_EXIST_ERROR,
							Constants.REGISTER_FAIL));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new LogicErrorException(Constants.USER_PROP_ROLE,
									Constants.ROLE_NOT_EXIST_ERROR, Constants.REGISTER_FAIL));
					roles.add(adminRole);
					break;
				default:
					RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new LogicErrorException(Constants.USER_PROP_ROLE,
									Constants.ROLE_NOT_EXIST_ERROR, Constants.REGISTER_FAIL));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		resultBean.setMessage(Constants.REGISTER_SUCCESS);
		
		log.info("######## End insert user ########");

		return resultBean;
	}

}

package com.fabbi.springbootmobileStore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.constant.Constants;
import com.fabbi.springbootmobileStore.dto.OrderDTO;
import com.fabbi.springbootmobileStore.service.OrderService;

@CrossOrigin
@RestController
@RequestMapping(value = Constants.API_PATH + Constants.ORDER_PATH)
public class OrderController {

	@Autowired
	OrderService orderService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResultBean> getAllOrder() {
		ResultBean resultBean = orderService.getAllOrder();
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = Constants.ORDER_USER_PATH, method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResultBean> getOrderByUserId() {
		ResultBean resultBean = orderService.getOrderByUserId();
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResultBean> getOrderById(@PathVariable(Constants.ORDER_PROP_ID) Integer id) {
		ResultBean resultBean = orderService.getOrderById(id);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResultBean> createOrder(@Valid @RequestBody OrderDTO request) {
		ResultBean resultBean = orderService.insertOrder(request);
		return new ResponseEntity<>(resultBean, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResultBean> updateOrderById(@Valid @RequestBody OrderDTO request,
			@PathVariable(Constants.ORDER_PROP_ID) Integer id) {
		ResultBean resultBean = orderService.updateOrder(id, request);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResultBean> deleteOrderById(@PathVariable(Constants.ORDER_PROP_ID) Integer id) {
		ResultBean resultBean = orderService.deleteOrder(id);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}
}

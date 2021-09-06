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
import com.fabbi.springbootmobileStore.dto.ProductOrderDTO;
import com.fabbi.springbootmobileStore.service.OrderDetailService;
import com.fabbi.springbootmobileStore.service.OrderService;

@CrossOrigin
@RestController
@RequestMapping(value = Constants.API_PATH + Constants.ORDER_PATH)
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	OrderDetailService orderDetailService;

	@RequestMapping(value = "", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResultBean> createOrder() {
		ResultBean resultBean = orderService.insertOrder();
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = Constants.ID_PATH, method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResultBean> updateOrder(@PathVariable(Constants.ORDER_PROP_ID) Integer id) {
		ResultBean resultBean = orderService.updateOrder(id);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = Constants.ID_PATH, method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<ResultBean> deleteOrder(@PathVariable(Constants.ORDER_PROP_ID) Integer id) {
		ResultBean resultBean = orderService.deleteOrder(id);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = Constants.ID_PATH + Constants.ORDER_DETAIL_PATH, method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResultBean> insertItem(@Valid @RequestBody ProductOrderDTO request,
			@PathVariable(Constants.ORDER_PROP_ID) int id) {
		request.setOrderId(id);
		ResultBean resultBean = orderDetailService.insertItem(request);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = Constants.ID_PATH + Constants.ORDER_DETAIL_PATH
			+ Constants.ORDER_DETAIL_ID_PATH, method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResultBean> removeItem(@PathVariable(Constants.ORDER_PROP_ID) Integer id,
			@PathVariable(Constants.ORDER_DETAIL_ID) int itemId) {
		ResultBean resultBean = orderDetailService.removeItem(id, itemId);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}

	@RequestMapping(value = Constants.ID_PATH + Constants.ORDER_DETAIL_PATH, method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResultBean> updateItem(@PathVariable(Constants.ORDER_PROP_ID) Integer id,
			@Valid @RequestBody ProductOrderDTO request) {
		ResultBean resultBean = orderDetailService.updateItem(id, request);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}
	
	@RequestMapping(value = Constants.ID_PATH, method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ResultBean> getOrderById(@PathVariable(Constants.ORDER_PROP_ID) Integer id) {
		ResultBean resultBean = orderService.getOrderById(id);
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResultBean> getAllOrder() {
		ResultBean resultBean = orderService.getAllOrder();
		return new ResponseEntity<>(resultBean, HttpStatus.OK);
	}
}

package com.fabbi.springbootmobileStore.service;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.dto.OrderDTO;

public interface OrderService {

	ResultBean insertOrder(OrderDTO request);
	
	ResultBean updateOrder(Integer id, OrderDTO request);
	
	ResultBean deleteOrder(Integer id);
	
	ResultBean getAllOrder();
	
	ResultBean getOrderByUserId();
	
	ResultBean getOrderById(Integer id);
}

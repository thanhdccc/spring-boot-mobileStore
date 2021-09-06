package com.fabbi.springbootmobileStore.service;

import com.fabbi.springbootmobileStore.bean.ResultBean;

public interface OrderService {
	
	ResultBean insertOrder();
	
	ResultBean updateOrder(Integer id);
	
	ResultBean deleteOrder(Integer id);
	
	ResultBean getAllOrder();
	
	ResultBean getOrderById(Integer id);
}

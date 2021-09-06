package com.fabbi.springbootmobileStore.service;

import com.fabbi.springbootmobileStore.bean.ResultBean;
import com.fabbi.springbootmobileStore.dto.ProductOrderDTO;

public interface OrderDetailService {

	ResultBean insertItem(ProductOrderDTO request);
	
	ResultBean updateItem(Integer id, ProductOrderDTO request);
	
	ResultBean removeItem(Integer orderId, Integer itemId);
}

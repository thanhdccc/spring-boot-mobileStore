package com.fabbi.springbootmobileStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabbi.springbootmobileStore.entity.OrderDetailEntity;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Integer> {
	
	List<OrderDetailEntity> findAllByOrderId(Integer id);
	
	OrderDetailEntity findOneById(Integer id);
	
	OrderDetailEntity findOneByOrderIdAndProductId(Integer orderId, Integer productId);
}

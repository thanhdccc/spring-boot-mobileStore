package com.fabbi.springbootmobileStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabbi.springbootmobileStore.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
	
	List<OrderEntity> findAllByUserId(Integer id);
	
	OrderEntity findOneByIdAndUserId(Integer id, Integer userId);
}

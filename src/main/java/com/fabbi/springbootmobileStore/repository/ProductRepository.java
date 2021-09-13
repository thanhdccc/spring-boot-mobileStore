package com.fabbi.springbootmobileStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabbi.springbootmobileStore.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

	Boolean existsByNameAndCategoryId(String name, Integer id);

	ProductEntity findOneByCategoryId(Integer id);
	
	ProductEntity findOneById(Integer id);
	
	Boolean existsByNameAndCategoryIdAndIdNot(String name, Integer categoryId, Integer id);
}

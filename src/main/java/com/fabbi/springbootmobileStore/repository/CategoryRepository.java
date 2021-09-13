package com.fabbi.springbootmobileStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabbi.springbootmobileStore.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

	Boolean existsByName(String name);
	
	CategoryEntity findOneById(Integer id);
	
	Boolean existsByNameAndIdNot(String name, Integer id);
}
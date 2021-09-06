package com.fabbi.springbootmobileStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fabbi.springbootmobileStore.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

	Boolean existsByName(String name);
	
	CategoryEntity findOneById(Integer id);
	
	@Query(value = "SELECT c.id, c.name FROM category c WHERE c.name = ?1 AND c.id <> ?2", nativeQuery = true)
	CategoryEntity getCategoryByName(String name, Integer id);
}
package com.fabbi.springbootmobileStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fabbi.springbootmobileStore.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

	Boolean existsByNameAndCategoryId(String name, Integer id);

	ProductEntity findOneByCategoryId(Integer id);
	
	ProductEntity findOneById(Integer id);

	@Query(value = "SELECT * FROM product p WHERE p.name = ?1 AND p.category_id = ?2 AND p.id <> ?3", nativeQuery = true)
	ProductEntity getByNameAndCategoryAndId(String name, Integer categoryId, Integer id);
}

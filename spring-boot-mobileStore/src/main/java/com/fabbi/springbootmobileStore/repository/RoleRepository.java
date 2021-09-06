package com.fabbi.springbootmobileStore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabbi.springbootmobileStore.entity.RoleEntity;
import com.fabbi.springbootmobileStore.util.ERole;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	
	Optional<RoleEntity> findByName(ERole name);
}

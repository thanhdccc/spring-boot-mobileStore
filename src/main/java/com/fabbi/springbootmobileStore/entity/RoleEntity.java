package com.fabbi.springbootmobileStore.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.fabbi.springbootmobileStore.util.ERole;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
public class RoleEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 431747385147568722L;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20, unique = true)
	private ERole name;

	public RoleEntity() {
	}

	public RoleEntity(ERole name) {
		this.name = name;
	}
}

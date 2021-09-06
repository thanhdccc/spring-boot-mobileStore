package com.fabbi.springbootmobileStore.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6985084309343724978L;
	
	@Column(name = "amount")
	private Float amount;
	
	@Column(name = "is_process")
	private Boolean isProcess;
}

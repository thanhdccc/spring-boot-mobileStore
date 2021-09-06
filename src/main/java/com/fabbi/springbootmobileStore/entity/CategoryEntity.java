package com.fabbi.springbootmobileStore.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@Setter
public class CategoryEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3320775040102365048L;

	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "category")
	private List<ProductEntity> products = new ArrayList<>();
}

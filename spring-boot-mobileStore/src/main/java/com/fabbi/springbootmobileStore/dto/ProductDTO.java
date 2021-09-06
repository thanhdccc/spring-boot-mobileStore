package com.fabbi.springbootmobileStore.dto;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	
	@Id
	private Integer id;

	@NotBlank(message = "Product name is mandatory")
	@Size(min = 2, max = 200, message = "Product name must be between 2 and 200")
	private String name;
	
	@NotNull(message = "Product quantity is mandatory")
	@Range(min=0, max=9999)
	private Integer quantity;
	
	@NotNull(message = "Product price is mandatory")
	@Range(min=1, max=999999999)
	private Float price;
	
	@NotNull(message = "Category id is mandatory")
	@Range(min=0, max=9999)
	private Integer categoryId;
}
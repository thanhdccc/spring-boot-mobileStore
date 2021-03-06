package com.fabbi.springbootmobileStore.dto;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
	
	@Id
	private Integer id;

	@NotNull(message = "Product id is mandatory")
	@Range(min=0, max=999999)
	private Integer productId;
	
	private String productName;
	
	@NotNull(message = "Product quantity is mandatory")
	@Range(min=1, max=9999)
	private Integer quantity;
	
	private Float price;
	
	private Float amount;
}

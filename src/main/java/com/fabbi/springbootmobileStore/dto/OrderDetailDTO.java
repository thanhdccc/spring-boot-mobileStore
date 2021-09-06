package com.fabbi.springbootmobileStore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

	private Integer id;
	private Integer orderId;
	private Integer productId;
	private Integer quantity;
	private Float price;
	private Float amount;
}

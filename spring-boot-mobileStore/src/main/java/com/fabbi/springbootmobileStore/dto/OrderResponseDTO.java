package com.fabbi.springbootmobileStore.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
	
	private Integer id;
	private Float amount;
	private Boolean isProcess;
	private List<OrderDetailDTO> orderItems;
}

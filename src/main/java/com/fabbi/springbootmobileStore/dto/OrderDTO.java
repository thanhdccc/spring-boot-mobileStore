package com.fabbi.springbootmobileStore.dto;

import java.util.List;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

	@Id
	private Integer id;
	private Float amount;
	private Boolean isProcess;
	private Integer userId;
	private List<OrderItemDTO> orderItems;
}

package com.fabbi.springbootmobileStore.dto;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
	
	@Id
	private Integer id;

	@NotBlank(message = "Category name is mandatory")
	@Size(min = 2, max = 50, message = "Category name must be between 2 and 50")
	private String name;
}

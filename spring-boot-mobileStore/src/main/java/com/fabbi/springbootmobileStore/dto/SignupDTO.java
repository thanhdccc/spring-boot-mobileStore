package com.fabbi.springbootmobileStore.dto;

import java.util.Set;

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
public class SignupDTO {
	
	@NotBlank(message = "Username is mandatory")
	@Size(min = 6, max = 20, message = "Username must be between 8 and 20")
	private String username;
	
	@NotBlank(message = "Password is mandatory")
	@Size(min = 6, max = 20, message = "Password must be between 8 and 20")
	private String password;
	
	private Set<String> role;
}

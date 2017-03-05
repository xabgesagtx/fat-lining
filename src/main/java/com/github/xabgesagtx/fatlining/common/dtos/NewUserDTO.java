package com.github.xabgesagtx.fatlining.common.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * DTO for a request to create a new user
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName="of")
public class NewUserDTO {
	
	private String name;
	private String password;
	private String repeatedPassword;
	private String email;
	private BigDecimal goalInKg;

}

package com.github.xabgesagtx.fatlining.common.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * DTO for a request to update the details of a user
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UserDetailsDTO {

	private Long id;
	private String name;
	private String email;
	private BigDecimal goalInKg;

}

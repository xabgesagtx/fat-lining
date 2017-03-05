package com.github.xabgesagtx.fatlining.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * DTO for a request to change the password of a user
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName="of")
public class UserPasswordDTO {
	
	private String currentPassword;
	private String password;
	private String repeatedPassword;

}

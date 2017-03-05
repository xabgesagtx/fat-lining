package com.github.xabgesagtx.fatlining.common.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.github.xabgesagtx.fatlining.common.dtos.UserPasswordDTO;

/**
 * 
 * Validate 
 *
 */
@Component
public class UserPasswordDTOValidator implements Validator {
	
	@Autowired
	private ValidationUtils utils;

	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(UserPasswordDTO.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserPasswordDTO dto = (UserPasswordDTO) target;
		utils.validateCurrentPassword(dto.getCurrentPassword(), errors);
		utils.validatePasswords(dto.getPassword(), dto.getRepeatedPassword(), errors);
	}
		


}

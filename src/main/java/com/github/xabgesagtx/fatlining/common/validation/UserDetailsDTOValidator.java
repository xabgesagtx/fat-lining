package com.github.xabgesagtx.fatlining.common.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.github.xabgesagtx.fatlining.common.dtos.UserDetailsDTO;

/**
 * 
 * Validate user details change
 *
 */
@Component
public class UserDetailsDTOValidator implements Validator {
	
	@Autowired
	private ValidationUtils utils;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(UserDetailsDTO.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserDetailsDTO dto = (UserDetailsDTO) target;
		utils.validateEmail(dto.getEmail(), dto.getId(), errors);
		utils.validateGoalInKg(dto.getGoalInKg(), errors);
	}

}

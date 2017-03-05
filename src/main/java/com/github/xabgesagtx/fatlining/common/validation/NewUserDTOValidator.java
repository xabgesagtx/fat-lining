package com.github.xabgesagtx.fatlining.common.validation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.github.xabgesagtx.fatlining.common.dtos.NewUserDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Validate new user 
 *
 */
@Component
@Slf4j
public class NewUserDTOValidator implements Validator {
	
	@Autowired
	private ValidationUtils utils;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(NewUserDTO.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.debug("Validating {}", target);
		NewUserDTO dto = (NewUserDTO) target;
		utils.validateName(dto.getName(), errors);
		utils.validateEmail(dto.getEmail(), null, errors);
		utils.validatePasswords(dto.getPassword(), dto.getRepeatedPassword(), errors);
		utils.validateGoalInKg(dto.getGoalInKg(), errors);
	}
	
}

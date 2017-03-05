package com.github.xabgesagtx.fatlining.common.validation;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import com.github.xabgesagtx.fatlining.common.dtos.UserDetailsDTO;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsDTOValidatorTest {

	@Mock
	private ValidationUtils utils;

	@Mock
	private Errors errors;
	
	@InjectMocks
	private UserDetailsDTOValidator validator;

	@Test
	public void testValidate() {
		Long id = 1l;
		String name = "2";
		String email = "3";
		BigDecimal goalInKg = BigDecimal.ONE;
		UserDetailsDTO dto = UserDetailsDTO.of(id, name, email, goalInKg);
		validator.validate(dto, errors);
		verify(utils).validateEmail(email, id, errors);
		verify(utils).validateGoalInKg(goalInKg, errors);
	}

}

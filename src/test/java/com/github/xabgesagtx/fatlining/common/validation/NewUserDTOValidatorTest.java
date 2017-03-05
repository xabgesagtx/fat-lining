package com.github.xabgesagtx.fatlining.common.validation;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import com.github.xabgesagtx.fatlining.common.dtos.NewUserDTO;

@RunWith(MockitoJUnitRunner.class)
public class NewUserDTOValidatorTest {
	
	@Mock
	private ValidationUtils utils;
	
	@Mock
	private Errors errors;
	
	@InjectMocks
	private NewUserDTOValidator validator;

	@Test
	public void testValidate() {
		String name = "1";
		String password = "2";
		String repeatedPassword = "3";
		String email = "4";
		BigDecimal goalInKg = BigDecimal.ONE;
		NewUserDTO dto = NewUserDTO.of(name, password, repeatedPassword, email, goalInKg);
		validator.validate(dto, errors);
		verify(utils).validateName(name, errors);
		verify(utils).validateEmail(email, null, errors);
		verify(utils).validatePasswords(password, repeatedPassword, errors);
		verify(utils).validateGoalInKg(goalInKg, errors);
	}

}

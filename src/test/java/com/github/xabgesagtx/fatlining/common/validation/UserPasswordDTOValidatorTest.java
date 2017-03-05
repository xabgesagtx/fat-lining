package com.github.xabgesagtx.fatlining.common.validation;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import com.github.xabgesagtx.fatlining.common.dtos.UserPasswordDTO;

@RunWith(MockitoJUnitRunner.class)
public class UserPasswordDTOValidatorTest {

	@Mock
	private ValidationUtils utils;

	@Mock
	private Errors errors;
	
	@InjectMocks
	private UserPasswordDTOValidator validator;

	@Test
	public void testValidate() {
		String currentPassword = "1";
		String password = "2";
		String repeatedPassword = "3";
		UserPasswordDTO dto = UserPasswordDTO.of(currentPassword, password, repeatedPassword);
		validator.validate(dto, errors);
		verify(utils).validateCurrentPassword(currentPassword, errors);
		verify(utils).validatePasswords(password, repeatedPassword, errors);
	}

}

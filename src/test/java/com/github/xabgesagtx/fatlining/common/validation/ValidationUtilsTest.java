package com.github.xabgesagtx.fatlining.common.validation;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;

import com.github.xabgesagtx.fatlining.common.UsersManager;
import com.github.xabgesagtx.fatlining.config.MainConfig;
import com.github.xabgesagtx.fatlining.model.AppUser;

@RunWith(MockitoJUnitRunner.class)
public class ValidationUtilsTest {
	
	@Spy
	private MainConfig config = new MainConfig();
	
	@Spy
	private PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Mock
	private UsersManager manager;
	
	@InjectMocks
	private ValidationUtils utils;

	@Mock
	private Errors errors;
	
	@Before
	public void setUp() {
		config.setPasswordLength(4l);
	}

	@Test
	public void testValidatePasswordsTooShort() {
		String password = "10";
		String repeatedPassword = "10";
		utils.validatePasswords(password, repeatedPassword, errors);
		verify(errors).rejectValue(eq("password"), anyString(), any(), anyString());
	}
	
	@Test
	public void testValidatePasswordsNoMatch() {
		String password = "1000";
		String repeatedPassword = "1001";
		utils.validatePasswords(password, repeatedPassword, errors);
		verify(errors).rejectValue(eq("repeatedPassword"), anyString());
	}
	
	@Test
	public void testValidatePasswordsEmpty() {
		String password = "";
		String repeatedPassword = "";
		utils.validatePasswords(password, repeatedPassword, errors);
		verify(errors).rejectValue(eq("password"), anyString());
	}
	
	@Test
	public void testValidatePasswordsNull() {
		String password = null;
		String repeatedPassword = null;
		utils.validatePasswords(password, repeatedPassword, errors);
		verify(errors).rejectValue(eq("password"), anyString());
	}
	
	@Test
	public void testValidatePasswordsSuccess() {
		String password = "1000";
		String repeatedPassword = "1000";
		utils.validatePasswords(password, repeatedPassword, errors);
		verify(errors, never()).rejectValue(eq("password"), anyString());
		verify(errors, never()).rejectValue(eq("password"), anyString(), any(), anyString());
	}

	@Test
	public void testValidateNameEmpty() {
		String name = "";
		utils.validateName(name, errors);
		verify(errors).rejectValue(eq("name"), anyString());
	}
	
	
	@Test
	public void testValidateNameAlreadyInUse() {
		String name = "maxmustermann";
		AppUser user = new AppUser();
		when(manager.findByName(name)).thenReturn(Optional.of(user));
		utils.validateName(name, errors);
		verify(errors).rejectValue(eq("name"), anyString());
	}

	@Test
	public void testValidateNameSucess() {
		String name = "maxmustermann";
		when(manager.findByName(name)).thenReturn(Optional.empty());
		utils.validateName(name, errors);
		verify(errors, never()).rejectValue(eq("name"), anyString());
	}
	
	@Test
	public void testValidateEmailEmpty() {
		Long id = 1l;
		String email = "";
		utils.validateEmail(email, id, errors);
		verify(errors).rejectValue(eq("email"), anyString());
	}
	
	@Test
	public void testValidateEmailAlreadyInUseSuccess() {
		Long id = 1l;
		String email = "maxmustermann";
		AppUser user = new AppUser();
		user.setId(id);
		when(manager.findByEmail(email)).thenReturn(Optional.of(user));
		utils.validateEmail(email, id, errors);
		verify(errors, never()).rejectValue(eq("email"), anyString());
	}
	
	@Test
	public void testValidateEmailAlreadyInUseNewUser() {
		Long id = null;
		String email = "maxmustermann";
		AppUser user = new AppUser();
		user.setId(1l);
		when(manager.findByEmail(email)).thenReturn(Optional.of(user));
		utils.validateEmail(email, id, errors);
		verify(errors).rejectValue(eq("email"), anyString());
	}
	
	@Test
	public void testValidateEmailAlreadyInUse() {
		Long id = 1l;
		String email = "maxmustermann";
		AppUser user = new AppUser();
		user.setId(2l);
		when(manager.findByEmail(email)).thenReturn(Optional.of(user));
		utils.validateEmail(email, id, errors);
		verify(errors).rejectValue(eq("email"), anyString());
	}

	@Test
	public void testValidateEmailSucess() {
		Long id = null;
		String email = "maxmustermann";
		when(manager.findByEmail(email)).thenReturn(Optional.empty());
		utils.validateEmail(email, id, errors);
		verify(errors, never()).rejectValue(eq("email"), anyString());
	}
	
	@Test
	public void testValidateEmailExistingUserSucess() {
		Long id = 1l;
		String email = "maxmustermann";
		when(manager.findByEmail(email)).thenReturn(Optional.empty());
		utils.validateEmail(email, id, errors);
		verify(errors, never()).rejectValue(eq("email"), anyString());
	}

	@Test
	public void testValidateGoalInKgNull() {
		utils.validateGoalInKg(null, errors);
		verify(errors, never()).rejectValue(eq("goalInKg"), anyString());
	}
	
	@Test
	public void testValidateGoalInKgNegative() {
		utils.validateGoalInKg(BigDecimal.valueOf(-10, 1), errors);
		verify(errors).rejectValue(eq("goalInKg"), anyString());
	}
	
	@Test
	public void testValidateGoalInKgSuccess() {
		utils.validateGoalInKg(BigDecimal.valueOf(10, 1), errors);
		verify(errors, never()).rejectValue(eq("goalInKg"), anyString());
	}

	@Test
	public void testValidateCurrentPasswordSuccess() {
		String password = "100";
		AppUser user = new AppUser();
		user.setHash(encoder.encode(password));
		when(manager.getCurrentUser()).thenReturn(user);
		utils.validateCurrentPassword(password, errors);
		verify(errors, never()).rejectValue(eq("currentPassword"), anyString());
	}
	
	@Test
	public void testValidateCurrentPasswordNoMatch() {
		String password = "100";
		AppUser user = new AppUser();
		user.setHash(encoder.encode("101"));
		when(manager.getCurrentUser()).thenReturn(user);
		utils.validateCurrentPassword(password, errors);
		verify(errors).rejectValue(eq("currentPassword"), anyString());
	}

}

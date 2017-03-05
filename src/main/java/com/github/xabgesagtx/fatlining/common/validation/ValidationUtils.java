package com.github.xabgesagtx.fatlining.common.validation;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.github.xabgesagtx.fatlining.common.UsersManager;
import com.github.xabgesagtx.fatlining.config.MainConfig;
import com.github.xabgesagtx.fatlining.model.AppUser;

/**

 * Validation utils
 *
 */
@Component
public class ValidationUtils {
	
	@Autowired
	protected MainConfig config;
	
	@Autowired
	protected UsersManager manager;
	
	@Autowired
	private PasswordEncoder encoder; 

	/**
	 * Validate passwords input
	 * <ul>
	 *  <li>password is not empty</li>
	 *  <li>password has appropriate length</li>
	 *  <li>password and repeated password match</li>
	 * </ul>
	 * @param password to validate
	 * @param repeatedPassword to validate
	 * @param errors resulting to validation
	 */
	public void validatePasswords(String password, String repeatedPassword, Errors errors) {
		String cleanPassword = StringUtils.trimToEmpty(password);
		if (StringUtils.isBlank(cleanPassword)) {
			errors.rejectValue("password", "validation.error.password.blank");
		} else if (StringUtils.length(cleanPassword) < config.getPasswordLength()) {
			errors.rejectValue("password", "validation.error.password.tooShort", new Object[]{config.getPasswordLength()}, StringUtils.EMPTY);
		} else {
			String cleanRepeatedPassword = StringUtils.trimToEmpty(repeatedPassword);
			if (!StringUtils.equals(cleanPassword, cleanRepeatedPassword)) {
				errors.rejectValue("repeatedPassword", "validation.error.password.noMatch");
			}
		}
		
	}
	
	/**
	 * Validate name
	 * <ul>
	 * 	<li>cannot be blank</li>
	 *  <li>no user can exist that already has this name</li>
	 * </ul>
	 * @param name to validate
	 * @param errors resulting to validation
	 */
	public void validateName(String name, Errors errors) {
		String cleanName = StringUtils.trimToEmpty(name);
		if (StringUtils.isBlank(cleanName)) {
			errors.rejectValue("name", "validation.error.name.blank");
		} else {
			manager.findByName(cleanName).ifPresent(user -> errors.rejectValue("name", "validation.error.name.inUse"));
		}
	}
	
	/**
	 * Validate email
	 * <ul>
	 * 	<li>cannot be blank</li>
	 * 	<li>no user can already use this email</li>
	 * </ul>
	 * @param email to validate
	 * @param id of user to validate data for
	 * @param errors resulting to validation
	 */
	public void validateEmail(String email, Long id, Errors errors) {
		String cleanEmail = StringUtils.trimToEmpty(email);
		if (StringUtils.isBlank(cleanEmail)) {
			errors.rejectValue("email", "validation.error.email.blank");
		} else {
			manager.findByEmail(cleanEmail).filter(user -> !user.getId().equals(id)).ifPresent(user -> errors.rejectValue("email", "validation.error.email.inUse"));
		}
	}
	
	/**
	 * Validate goal
	 * <ul>
	 * 	<li>can be null</li>
	 * 	<li>can not be zero or less</li>
	 * </ul>
	 * @param goal
	 * @param errors resulting to validation
	 */
	public void validateGoalInKg(BigDecimal goal, Errors errors) {
		if (goal != null && goal.compareTo(BigDecimal.ZERO) < 0) {
			errors.rejectValue("goalInKg", "validation.error.goal.negative");
		}
	}
	
	/**
	 * Validate current password
	 * <ul>
	 * 	<li>current password and entered password have to match</li>
	 * </ul>
	 * @param currentPassword
	 * @param errors
	 */
	public void validateCurrentPassword(String currentPassword, Errors errors) {
		String cleanCurrentPassword = StringUtils.trimToEmpty(currentPassword);
		AppUser currentUser = manager.getCurrentUser();
		if (!encoder.matches(cleanCurrentPassword, currentUser.getHash())) {
			errors.rejectValue("currentPassword", "validation.error.password.wrongPassword");
		}
	}
}

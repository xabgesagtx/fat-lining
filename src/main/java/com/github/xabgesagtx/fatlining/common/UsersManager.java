package com.github.xabgesagtx.fatlining.common;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.xabgesagtx.fatlining.common.dtos.NewUserDTO;
import com.github.xabgesagtx.fatlining.common.dtos.UserDetailsDTO;
import com.github.xabgesagtx.fatlining.common.dtos.UserPasswordDTO;
import com.github.xabgesagtx.fatlining.config.MainConfig;
import com.github.xabgesagtx.fatlining.model.AppUser;
import com.github.xabgesagtx.fatlining.model.GraphConfig;
import com.github.xabgesagtx.fatlining.persistence.AppUserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Manager for all user stuff
 *
 */
@Component
@Slf4j
public class UsersManager {
	
	@Autowired
	private AppUserRepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private MainConfig config;
	
	/**
	 * Get user of current request
	 * @return current user
	 */
	public AppUser getCurrentUser() {
		return userRepo.getCurrentUser();
	}
	
	/**
	 * Find all users. Only admin users can perform this task
	 * @return all users
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	public Iterable<AppUser> findAll() {
		return userRepo.findAll(new Sort(new Order(Direction.ASC, "name")));
	}
	
	/**
	 * Find user by id. Only admin users can perform this task
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	public Optional<AppUser> findById(Long id) {
		return userRepo.findById(id);
	}
	
	/**
	 * Update the details of a user
	 * @param user the user to update
	 * @param dto the details that hold the data for the update
	 */
	public void update(AppUser user, UserDetailsDTO dto) {
		user.setEmail(StringUtils.trimToEmpty(dto.getEmail()));
		GraphConfig graphConfig = user.getGraphConfig();
		if (graphConfig == null) {
			graphConfig = new GraphConfig();
		}
		graphConfig.setGoalInKg(dto.getGoalInKg());
		user.setGraphConfig(graphConfig);
		userRepo.saveSecured(user);
	}
	
	/**
	 * Find user by email
	 * @param email to find the user by
	 * @return user if found
	 */
	public Optional<AppUser> findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	/**
	 * Find user by name
	 * @param name name of the user
	 * @return user if found
	 */
	public Optional<AppUser> findByName(String name) {
		return userRepo.findByName(name);
	}
	
	/**
	 * Change password of a user
	 * @param user user to change password for
	 * @param dto data for the changed password
	 */
	public void changePassword(AppUser user, UserPasswordDTO dto) {
		user.setHash(encoder.encode(StringUtils.trimToEmpty(dto.getPassword())));
		userRepo.saveSecured(user);
	}
	
	/**
	 * Save user
	 * @param user to be saved
	 */
	public void save(AppUser user) {
		userRepo.saveSecured(user);
	}
	
	/**
	 * Create new user
	 * @param dto data for new user
	 */
	public void create(NewUserDTO dto) {
		GraphConfig graphConfig = new GraphConfig();
		graphConfig.setGoalInKg(dto.getGoalInKg());
		AppUser newUser = AppUser.of(StringUtils.trimToEmpty(dto.getName()), StringUtils.trimToEmpty(dto.getEmail()), encoder.encode(StringUtils.trimToEmpty(dto.getPassword())), new String[]{ FatLiningConstants.ROLE_USER}, graphConfig);
		userRepo.saveSecured(newUser);
	}
	
	/**
	 * On startup ensure the admin user is set up
	 */
	@PostConstruct
	public void start() {
		log.info("Setting up admin user");
		Optional<AppUser> adminOptional = userRepo.findByName("admin");
		String[] adminRoles = { FatLiningConstants.ROLE_ADMIN, FatLiningConstants.ROLE_USER };
		AppUser admin;
		if(adminOptional.isPresent()) {
			admin = adminOptional.get();
			admin.setRoles(adminRoles);
			admin.setActivated(true);
		} else {
			admin = AppUser.of("admin", config.getAdminEmail(), encoder.encode(config.getAdminPassword()), adminRoles, new GraphConfig());
			admin.setActivated(true);
		}
		userRepo.save(admin);
		log.info("Admin user setup is done");
	}

}

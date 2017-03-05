package com.github.xabgesagtx.fatlining.persistence;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import com.github.xabgesagtx.fatlining.model.AppUser;

/**
 * 
 * Repository interface for app users
 *
 */
public interface AppUserRepository extends CrudRepository<AppUser, Long> {

	/**
	 * Find user by id
	 * @param id of the user
	 * @return user if present
	 */
	Optional<AppUser> findById(long id);

	/**
	 * Find user by name
	 * @param name of the user
	 * @return user if present
	 */
	Optional<AppUser> findByName(String name);

	/**
	 * Find user by email
	 * @param email of the user
	 * @return user if present
	 */
	Optional<AppUser> findByEmail(String email);

	/**
	 * Save a user with security mechanisms on. Only, admin user or the user
	 * her-/himself can save the user object
	 * 
	 * @param user to save
	 * @return the saved user
	 */
	@PreAuthorize("hasAuthority('ADMIN') or #user?.name == authentication?.name")
	default <S extends AppUser> AppUser saveSecured(@Param("user") S user) {
		return save(user);
	}

	/**
	 * Find all users
	 * @param sort sorting parameter
	 * @return all users in the repository
	 */
	Iterable<AppUser> findAll(Sort sort);

	/**
	 * User object of the user of the request
	 * @return the current user
	 */
	@Query("Select user from AppUser user where user.name = ?#{ principal?.username }")
	AppUser getCurrentUser();
}

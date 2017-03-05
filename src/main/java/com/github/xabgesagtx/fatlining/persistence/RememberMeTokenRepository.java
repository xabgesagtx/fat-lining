package com.github.xabgesagtx.fatlining.persistence;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.xabgesagtx.fatlining.model.RememberMeToken;

/**
 * 
 * Repository for remember me tokens
 *
 */
public interface RememberMeTokenRepository extends CrudRepository<RememberMeToken, String> {

	/**
	 * Find token by series
	 * @param series
	 * @return token if present
	 */
    Optional<RememberMeToken> findBySeries(String series);
    
    /**
     * Delete users by username
     * @param username of the tokens to delte
     * @return number of tokens deleted
     */
	int deleteByUsername(String username);
	
}

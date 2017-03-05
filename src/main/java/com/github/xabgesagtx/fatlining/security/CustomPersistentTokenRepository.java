package com.github.xabgesagtx.fatlining.security;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import com.github.xabgesagtx.fatlining.model.RememberMeToken;
import com.github.xabgesagtx.fatlining.persistence.RememberMeTokenRepository;

/**
 * 
 * Implementation of a repository for remember-me tokens
 *
 */
@Component
public class CustomPersistentTokenRepository implements PersistentTokenRepository {

	@Autowired
	private RememberMeTokenRepository repo;

	@Override
	@Transactional
	public void createNewToken(PersistentRememberMeToken token) {
		repo.save(RememberMeToken.of(token.getSeries(), token.getUsername(), token.getTokenValue(), token.getDate()));
	}

	@Override
	@Transactional
	public void updateToken(String series, String value, Date lastUsed) {
		repo.findBySeries(series).ifPresent(token -> repo.save(RememberMeToken.of(series, token.getUsername(), value, lastUsed)));
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		return repo.findBySeries(seriesId).map(RememberMeToken::toPersistentRememberMeToken).orElse(null);
	}

	@Override
	@Transactional
	public void removeUserTokens(String username) {
		repo.deleteByUsername(username);
	}

}

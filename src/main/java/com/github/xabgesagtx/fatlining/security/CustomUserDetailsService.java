package com.github.xabgesagtx.fatlining.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.github.xabgesagtx.fatlining.model.AppUser;
import com.github.xabgesagtx.fatlining.persistence.AppUserRepository;

/**
 * 
 * Custom user details service for this application. Checks login and provides the current user's details
 *
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private AppUserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		return userRepo.findByName(name)
				.filter(AppUser::isActivated)
				.map(user -> {
					List<GrantedAuthority> authoritiesList = AuthorityUtils.createAuthorityList(user.getRoles());
					return new AppUserDetails(user.getId(), user.getName(), user.getHash(), authoritiesList, user);
				})
				.orElseThrow(() -> new UsernameNotFoundException(String.format("No active user with name %s",name)));
	}
	
	
}

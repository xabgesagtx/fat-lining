package com.github.xabgesagtx.fatlining.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.xabgesagtx.fatlining.model.AppUser;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * Custom implementation with {@link UserDetails}
 *
 */
@Getter
@AllArgsConstructor
public class AppUserDetails implements UserDetails {

	private static final long serialVersionUID = -7779204424481626876L;
	
	private Long id;
	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private AppUser user;

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

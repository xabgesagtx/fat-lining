package com.github.xabgesagtx.fatlining.persistence;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.github.xabgesagtx.fatlining.model.AppUser;
import com.github.xabgesagtx.fatlining.security.AppUserDetails;

/**
 * 
 * Service that provides the current auditor as {@link AppUser}
 *
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<AppUser> {

	@Override
	public AppUser getCurrentAuditor() {
		AppUser result = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			result = ((AppUserDetails) authentication.getPrincipal()).getUser();
		}
		return result;
	}

}

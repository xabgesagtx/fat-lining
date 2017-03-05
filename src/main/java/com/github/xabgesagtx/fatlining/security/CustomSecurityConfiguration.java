package com.github.xabgesagtx.fatlining.security;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.github.xabgesagtx.fatlining.common.FatLiningConstants;

/**
 * 
 * Security configuration class. All configuration for security should be done here.
 *
 */
@Configuration
@EnableWebSecurity
public class CustomSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		 http
         .authorizeRequests()
         	.antMatchers("/admin/**").hasAuthority(FatLiningConstants.ROLE_ADMIN) 
         	.antMatchers("/webjars/**").permitAll()
         	.antMatchers("/css/**").permitAll()
         	.antMatchers("/js/**").permitAll()
         	.anyRequest().authenticated()
            .and()
         .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
         .csrf()
         	.and()
         .rememberMe()
         	.rememberMeParameter("remember-me")
         	.tokenRepository(tokenRepository)
         	.tokenValiditySeconds(Long.valueOf(TimeUnit.SECONDS.convert(4, TimeUnit.DAYS)).intValue())
         	.and()
   		.exceptionHandling().accessDeniedPage("/403")
    		.and()
         .logout()
         	.permitAll();
	}
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private PersistentTokenRepository tokenRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
	}
	
}

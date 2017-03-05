package com.github.xabgesagtx.fatlining.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Main configuration class
 *
 */
@Configuration
@ConfigurationProperties(prefix="config")
@Getter
@Setter
public class MainConfig {
	
	/**
	 * The password that will be set for the admin user on every start
	 */
	private String adminPassword = "admin";
	
	/**
	 * E-Mail that will be set for the admin user on first start
	 */
	private String adminEmail = "test@example.com";
	
	/**
	 * Minimum password length
	 */
	private Long passwordLength = 8l;
	
}

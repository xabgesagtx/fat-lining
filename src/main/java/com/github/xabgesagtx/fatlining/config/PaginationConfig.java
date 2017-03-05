package com.github.xabgesagtx.fatlining.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;


/**
 * 
 * Configuration of pagination
 *
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="config.pagination")
public class PaginationConfig {
	
	/**
	 * The size for pages
	 */
	private int pageSize = 10;
	
	/**
	 * Maximum number of pagination items
	 */
	private int maxPaginationItems = 5;

}

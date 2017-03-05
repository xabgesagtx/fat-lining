package com.github.xabgesagtx.fatlining.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Configuration for charts
 *
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="config.charts")
public class ChartsConfig {
	
	/**
	 * Maximum number of days allowed for a chart
	 */
	private int maxDays = 365;
	
	/**
	 * The days available as option for chart generation
	 */
	private List<Integer> daysOptions = Arrays.asList(7,30,90,365); 

}
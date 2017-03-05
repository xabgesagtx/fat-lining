package com.github.xabgesagtx.fatlining.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Configuration for the weight graph of a user
 *
 */
@Entity
@Getter
@Setter
public class GraphConfig {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private BigDecimal goalInKg;

}

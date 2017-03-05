package com.github.xabgesagtx.fatlining.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Model for weight measurement data point for a certain date and time
 *
 */
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class WeightMeasurement {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@CreatedDate
	private LocalDateTime dateTime;
	
	@CreatedBy
	@ManyToOne
	private AppUser createdBy;
	
	private BigDecimal weightInKg;

}

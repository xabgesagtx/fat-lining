package com.github.xabgesagtx.fatlining.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 
 * Model for the users of the application
 *
 */
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(staticName="of")
@Table(uniqueConstraints = 
	{
		@UniqueConstraint(columnNames="name"),
		@UniqueConstraint(columnNames="email")
	}
)
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NonNull
	private String name;
	@NonNull
	private String email;
	@NonNull
	private String hash;
	@NonNull
	private String[] roles;
	private boolean activated = true;
	
	@CreatedDate
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	
	@CreatedBy
	@ManyToOne
	private AppUser createdBy;
	
	@LastModifiedBy
	@ManyToOne
	private AppUser lastModifiedBy;
	
	
	@OneToOne(cascade=CascadeType.ALL)
	@NonNull
	private GraphConfig graphConfig;
	
}

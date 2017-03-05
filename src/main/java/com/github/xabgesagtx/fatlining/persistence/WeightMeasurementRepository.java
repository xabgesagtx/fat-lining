package com.github.xabgesagtx.fatlining.persistence;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import com.github.xabgesagtx.fatlining.model.WeightMeasurement;

/**
 * Weight measurement repository
 * @author egon
 *
 */
public interface WeightMeasurementRepository extends CrudRepository<WeightMeasurement, Long> {

	
	@Query("select wm from WeightMeasurement wm where wm.createdBy.name = ?#{ principal?.username }")
	Iterable<WeightMeasurement> findAllForUser(Sort sort);
	
	@Query("select wm from WeightMeasurement wm where wm.createdBy.name = ?#{ principal?.username } and wm.dateTime >= ?1")
	Iterable<WeightMeasurement> findAllForUserAndDays(LocalDateTime limit, Sort sort);
	
	@Query("select wm from WeightMeasurement wm where wm.createdBy.name = ?#{ principal?.username }")
	Page<WeightMeasurement> findAllPagedForUser(Pageable request);
	
	
	@Override
	@PreAuthorize("hasAuthority('ADMIN') or #wm?.createdBy?.name == authentication?.name")
	void delete(@Param("wm") WeightMeasurement wm);

	Optional<WeightMeasurement> findById(long id);
}

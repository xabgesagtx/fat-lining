package com.github.xabgesagtx.fatlining.common;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.github.xabgesagtx.fatlining.model.WeightMeasurement;
import com.github.xabgesagtx.fatlining.persistence.WeightMeasurementRepository;

/**
 * 
 * Manager of all measurements related tasks
 *
 */
@Component
public class WeightMeasurementManager {
	
	@Autowired
	private WeightMeasurementRepository repo;
	
	/**
	 * Find all for current user
	 * @return all measurments
	 */
	public Iterable<WeightMeasurement> findAllForUser() {
		return repo.findAllForUser(new Sort(new Order(Direction.ASC, "dateTime")));
	}
	
	/**
	 * Find all for current user of the last days
	 * @return all measurments
	 */
	public Iterable<WeightMeasurement> findAllForUserAndDays(int days) {
		return repo.findAllForUserAndDays(LocalDate.now().minusDays(days).atStartOfDay(), new Sort(new Order(Direction.ASC, "dateTime")));
	}
	
	/**
	 * Find a page of measurements sorted by the time of creation descending
	 * @param pageNumber index of the page
	 * @param pageSize size of the page
	 * @return page of measurements
	 */
	public Page<WeightMeasurement> findAsPageDesc(int pageNumber, int pageSize) {
		return repo.findAllPagedForUser(new PageRequest(pageNumber, pageSize, new Sort(new Order(Direction.DESC, "dateTime"))));
	}
	
	/**
	 * Delete single measurement by id. Only admin or user who created that measurement can do that
	 * @param id of measurement
	 * @return true if measurment could be found, false otherwise
	 */
	public boolean delete(long id) {
		Optional<WeightMeasurement> wm = repo.findById(id);
		if (wm.isPresent()) {
			repo.delete(wm.get());
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Save a measurement
	 * @param wm to be saved
	 * @return saved measurement
	 */
	public WeightMeasurement save(WeightMeasurement wm) {
		return repo.save(wm);
	}
	
}

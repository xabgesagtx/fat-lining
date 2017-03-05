package com.github.xabgesagtx.fatlining.web.charts;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.stereotype.Component;

import com.github.xabgesagtx.fatlining.model.WeightMeasurement;
import com.google.common.collect.Lists;

/**
 * 
 * Factory to create charts
 *
 */
@Component
public class ChartFactory {
	
	/**
	 * Create a graph
	 * @param goalInKg the goal set
	 * @param measurements the measurement data
	 * @return a chart 
	 */
	public Chart create(BigDecimal goalInKg, Iterable<WeightMeasurement> measurements) {
		BigDecimal minValue = goalInKg;
		BigDecimal maxValue = goalInKg;
		List<List<Object>> dataPoints = new ArrayList<>();
		for (WeightMeasurement wm : measurements) {
			if (minValue == null) {
				minValue = wm.getWeightInKg();
			} else {
				minValue = minValue.min(wm.getWeightInKg());
			}
			if (maxValue == null) {
				maxValue = wm.getWeightInKg();
			} else {
				maxValue = maxValue.max(wm.getWeightInKg());
			}
			dataPoints.add(Lists.newArrayList(wm.getDateTime().toInstant(ZoneOffset.UTC).toEpochMilli(),wm.getWeightInKg()));
		}
		if (minValue != null) {
			minValue = minValue.subtract(BigDecimal.valueOf(1));
		} 
		if (maxValue != null) {
			maxValue = maxValue.add(BigDecimal.valueOf(1));
		}
		return Chart.of(minValue, maxValue, goalInKg, dataPoints, calculareTrend(dataPoints));
	}
	
	private List<List<Object>> calculareTrend(List<List<Object>> measurements) {
		SimpleRegression regression = new SimpleRegression(true);
		Long firstX = null;
		Long lastX = null;
		for(int i = 0; i < measurements.size(); i++) {
			List<Object> measurement = measurements.get(i);
			Long x = (Long) measurement.get(0);
			BigDecimal y = (BigDecimal) measurement.get(1);
			regression.addData(x.doubleValue(), y.doubleValue());
			if (i == 0) {
				firstX = x;
			} else if (i + 1 == measurements.size()) {
				lastX = x;
			}
		}
		double slope = regression.getSlope();
		if (Double.isNaN(slope)) {
			return new ArrayList<>();
		} else {
			List<Object> start = Lists.newArrayList(firstX, regression.predict(firstX));
			List<Object> end = Lists.newArrayList(lastX, regression.predict(lastX));
			return Lists.newArrayList(start, end);
		}
	}

}

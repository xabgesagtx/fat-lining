package com.github.xabgesagtx.fatlining.web.charts;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * Model to hold all data to draw a chart
 *
 */
@Getter
@AllArgsConstructor(staticName="of")
public class Chart {
	
	private BigDecimal minValue;
	private BigDecimal maxValue;
	private BigDecimal goal;
	private List<List<Object>> measurements;
	private List<List<Object>> trend;
	
	/**
	 * @return true if the chart is valid and can be displayed
	 */
	public boolean isValid() {
		return minValue != null && maxValue != null && !measurements.isEmpty();
	}
	
	/**
	 * @return true if a trend line should be displayed
	 */
	public boolean isWithTrend() {
		return !trend.isEmpty();
	}

}

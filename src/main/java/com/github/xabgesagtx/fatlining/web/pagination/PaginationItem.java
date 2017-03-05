package com.github.xabgesagtx.fatlining.web.pagination;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 * Model for the item of a pagination
 *
 */
@Getter
@AllArgsConstructor(staticName="of")
@ToString
@EqualsAndHashCode
public class PaginationItem {
	
	private final int number;
	private final String label;
	private final boolean active;
	
}
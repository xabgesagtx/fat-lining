package com.github.xabgesagtx.fatlining.web.pagination;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 * Wrapper for a page of items with pagination
 *
 * @param <T> type of the items of the page
 */
@Getter
@AllArgsConstructor(access=AccessLevel.PACKAGE)
@ToString
@EqualsAndHashCode
public class PageWrapper<T> {
	
	private final int number;
    
    private final int totalPages;
    
    private final long totalResults;
    
    private final List<T> entries;
    
    private final List<PaginationItem> pagination;

	public boolean isEmpty() {
		return entries.isEmpty();
	}
	
}

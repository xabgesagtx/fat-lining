package com.github.xabgesagtx.fatlining.web.pagination;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.github.xabgesagtx.fatlining.common.Messages;
import com.github.xabgesagtx.fatlining.common.MessagesService;
import com.github.xabgesagtx.fatlining.config.PaginationConfig;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

/**
 * 
 * Factory to create {@link PageWrapper} according to configuration
 *
 */
@Component
public class PageWrapperFactory {
	
	@Autowired
	private MessagesService messages;
	
	@Autowired
	private PaginationConfig config;
	
	@VisibleForTesting
	List<PaginationItem> createPagination(int number, int totalPages) {
		List<PaginationItem> result = Lists.newArrayList();
		result.add(PaginationItem.of(0, messages.getMessage(Messages.PAGINATION_FIRST), !isFirstPage(number)));
		int previous = number <= 0 ? 0 : number-1;
		result.add(PaginationItem.of(previous, messages.getMessage(Messages.PAGINATION_PREVIOUS),hasPreviousPage(number)));
		result.addAll(createRegularItems(number, totalPages));
		int next = number+1 >= totalPages ? totalPages-1 : number+1;
		result.add(PaginationItem.of(next, messages.getMessage(Messages.PAGINATION_NEXT), hasNextPage(number, totalPages)));
		result.add(PaginationItem.of(totalPages-1, messages.getMessage(Messages.PAGINATION_LAST), !isLastPage(number, totalPages)));
        return result;
	}
	
	private static boolean isFirstPage(int number) {
		return number == 0;
	}
	
	private static boolean hasPreviousPage(int number) {
		return number > 0;
	}
	
	private static boolean hasNextPage(int number, int totalPages) {
		return number + 1 < totalPages;
	}
	
	private static boolean isLastPage(int number, int totalPages) {
		return number + 1 == totalPages;
	}
	
	
	private final List<PaginationItem> createRegularItems(int number, int totalPages) {
		int start;
		int size;
        if (totalPages <= config.getMaxPaginationItems()){
            start = 0;
            size = totalPages;
        } else {
            if (number <= config.getMaxPaginationItems() - config.getMaxPaginationItems()/2){
                start = 0;
                size = config.getMaxPaginationItems();
            } else if (number >= totalPages - config.getMaxPaginationItems()/2){
                start = totalPages - config.getMaxPaginationItems();
                size = config.getMaxPaginationItems();
            } else {
                start = number - config.getMaxPaginationItems()/2;
                size = config.getMaxPaginationItems();
            }
        }
        List<PaginationItem> result = Lists.newArrayList();
        for (int i = 0; i<size; i++){
            result.add(PaginationItem.of(start+i, String.valueOf(start+i+1), (start+i)!=number));
        }
        return result;
	}

	/**
	 * Create a page wrapper
	 * @param number of the page
	 * @param totalPages total number of pages
	 * @param totalResults total number of results
	 * @param entries the entries of the page
	 * @return page wrapper
	 */
	public <T> PageWrapper<T> create(int number, int totalPages, long totalResults, List<T> entries) {
		return new PageWrapper<>(number, totalPages, totalResults, entries, createPagination(number, totalPages));
	}
	
	/**
	 * Create a page wrapper
	 * @param page to wrap
	 * @return page wrapper
	 */
	public <T> PageWrapper<T> create(Page<T> page) {
		return create(page.getNumber(), page.getTotalPages(), page.getTotalElements(), page.getContent());
	}
	
}

package com.github.xabgesagtx.fatlining.web.pagination;

import static com.github.xabgesagtx.fatlining.common.Messages.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.AdditionalAnswers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.xabgesagtx.fatlining.common.MessagesService;
import com.github.xabgesagtx.fatlining.config.PaginationConfig;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class PageWrapperFactoryTest {
	
	@Mock
	private MessagesService messages;
	
	@Spy
	private PaginationConfig config;
	
	@InjectMocks
	private PageWrapperFactory factory;
	
	@Before
	public void setUp() {
		when(messages.getMessage(anyString())).then(returnsFirstArg());
		config.setMaxPaginationItems(5);
	}

	@Test
	public void testCreatePaginationOnePage() {
		List<PaginationItem> expected = Lists.newArrayList(
				PaginationItem.of(0, PAGINATION_FIRST, false),
				PaginationItem.of(0, PAGINATION_PREVIOUS, false),
				PaginationItem.of(0, "1", false),
				PaginationItem.of(0, PAGINATION_NEXT, false),
				PaginationItem.of(0, PAGINATION_LAST, false)
				);
		List<PaginationItem> was = factory.createPagination(0, 1);
		assertThat(was).isEqualTo(expected);
	}
	
	@Test
	public void testCreatePaginationTwoPages() {
		List<PaginationItem> expected = Lists.newArrayList(
				PaginationItem.of(0, PAGINATION_FIRST, true),
				PaginationItem.of(0, PAGINATION_PREVIOUS, true),
				PaginationItem.of(0, "1", true),
				PaginationItem.of(1, "2", false),
				PaginationItem.of(1, PAGINATION_NEXT, false),
				PaginationItem.of(1, PAGINATION_LAST, false)
				);
		List<PaginationItem> was = factory.createPagination(1, 2);
		assertThat(was).isEqualTo(expected);
	}
	
	@Test
	public void testCreatePaginationThreePages() {
		List<PaginationItem> expected = Lists.newArrayList(
				PaginationItem.of(0, PAGINATION_FIRST, true),
				PaginationItem.of(0, PAGINATION_PREVIOUS, true),
				PaginationItem.of(0, "1", true),
				PaginationItem.of(1, "2", false),
				PaginationItem.of(2, "3", true),
				PaginationItem.of(2, PAGINATION_NEXT, true),
				PaginationItem.of(2, PAGINATION_LAST, true)
				);
		List<PaginationItem> was = factory.createPagination(1, 3);
		assertThat(was).isEqualTo(expected);
	}
	
	@Test
	public void testCreatePaginationTenPagesBeginning() {
		List<PaginationItem> expected = Lists.newArrayList(
				PaginationItem.of(0, PAGINATION_FIRST, true),
				PaginationItem.of(0, PAGINATION_PREVIOUS, true),
				PaginationItem.of(0, "1", true),
				PaginationItem.of(1, "2", false),
				PaginationItem.of(2, "3", true),
				PaginationItem.of(3, "4", true),
				PaginationItem.of(4, "5", true),
				PaginationItem.of(2, PAGINATION_NEXT, true),
				PaginationItem.of(9, PAGINATION_LAST, true)
				);
		List<PaginationItem> was = factory.createPagination(1, 10);
		assertThat(was).isEqualTo(expected);
	}
	
	@Test
	public void testCreatePaginationTenPagesMiddle() {
		List<PaginationItem> expected = Lists.newArrayList(
				PaginationItem.of(0, PAGINATION_FIRST, true),
				PaginationItem.of(3, PAGINATION_PREVIOUS, true),
				PaginationItem.of(2, "3", true),
				PaginationItem.of(3, "4", true),
				PaginationItem.of(4, "5", false),
				PaginationItem.of(5, "6", true),
				PaginationItem.of(6, "7", true),
				PaginationItem.of(5, PAGINATION_NEXT, true),
				PaginationItem.of(9, PAGINATION_LAST, true)
				);
		List<PaginationItem> was = factory.createPagination(4, 10);
		assertThat(was).isEqualTo(expected);
	}
	
	@Test
	public void testCreatePaginationTenPagesEnd() {
		List<PaginationItem> expected = Lists.newArrayList(
				PaginationItem.of(0, PAGINATION_FIRST, true),
				PaginationItem.of(8, PAGINATION_PREVIOUS, true),
				PaginationItem.of(5, "6", true),
				PaginationItem.of(6, "7", true),
				PaginationItem.of(7, "8", true),
				PaginationItem.of(8, "9", true),
				PaginationItem.of(9, "10", false),
				PaginationItem.of(9, PAGINATION_NEXT, false),
				PaginationItem.of(9, PAGINATION_LAST, false)
				);
		List<PaginationItem> was = factory.createPagination(9, 10);
		assertThat(was).isEqualTo(expected);
	}

}

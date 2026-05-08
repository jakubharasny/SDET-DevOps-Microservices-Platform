package com.example.api.unit;

import com.example.api.repository.QueryRepository;
import com.example.api.repository.QueryRecord;
import com.example.api.service.QueryEventPublisher;
import com.example.api.service.QueryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class QueryServiceTest {

	@Test
	void createsPendingRecordAndPublishesEvent() {
		QueryRepository queryRepository = Mockito.mock(QueryRepository.class);
		QueryEventPublisher queryEventPublisher = Mockito.mock(QueryEventPublisher.class);
		QueryService queryService = new QueryService(queryRepository, queryEventPublisher);

		QueryRecord created = queryService.create("hello");

		Mockito.verify(queryRepository).insert(Mockito.eq(created));
		Mockito.verify(queryEventPublisher).publishCreated(Mockito.eq(created));
		org.junit.jupiter.api.Assertions.assertEquals("hello", created.message());
		org.junit.jupiter.api.Assertions.assertEquals(com.example.api.model.QueryStatus.PENDING, created.status());
	}
}

package com.example.kafkaconsumer.service;

import com.example.kafkaconsumer.model.QueryCreatedEvent;
import com.example.kafkaconsumer.repository.QueryProcessingRepository;
import java.time.Instant;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QueryProcessingService implements QueryEventProcessor {

	private final QueryProcessingRepository queryProcessingRepository;
	private final long delayMs;

	public QueryProcessingService(QueryProcessingRepository queryProcessingRepository,
			@Value("${app.query-processor.delay-ms:1200}") long delayMs) {
		this.queryProcessingRepository = queryProcessingRepository;
		this.delayMs = delayMs;
	}

	@Override
	public void process(QueryCreatedEvent event) {
		Instant now = Instant.now();
		// First transition to PROCESSING so UI polling can reflect worker pickup
		// quickly.
		queryProcessingRepository.markProcessing(event.id(), now);
		try {
			// Intentional delay: makes async behavior visible during demo/polling.
			Thread.sleep(delayMs);
			String resultText = "Processed by Kafka worker: " + event.message().toUpperCase(Locale.ROOT);
			queryProcessingRepository.markDone(event.id(), resultText, Instant.now());
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			queryProcessingRepository.markFailed(event.id(), "worker interrupted", Instant.now());
		} catch (RuntimeException ex) {
			queryProcessingRepository.markFailed(event.id(), ex.getMessage(), Instant.now());
		}
	}
}

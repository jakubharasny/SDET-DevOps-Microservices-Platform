package com.example.kafkaconsumer.consumer;

import com.example.kafkaconsumer.model.QueryCreatedEvent;
import com.example.kafkaconsumer.service.QueryEventProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class QueryCreatedConsumer {

	private static final Logger log = LoggerFactory.getLogger(QueryCreatedConsumer.class);

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final QueryEventProcessor queryEventProcessor;

	public QueryCreatedConsumer(QueryEventProcessor queryEventProcessor) {
		this.queryEventProcessor = queryEventProcessor;
	}

	@KafkaListener(topics = "${app.query-events.topic:query.created.v1}")
	public void onMessage(String payload) {
		try {
			// Listener receives raw JSON; we map it manually to keep transport explicit.
			QueryCreatedEvent event = objectMapper.readValue(payload, QueryCreatedEvent.class);
			// Processing and DB status transitions are delegated to a separate service.
			queryEventProcessor.process(event);
		} catch (JsonProcessingException ex) {
			log.error("Failed to parse query-created event: {}", payload, ex);
		}
	}
}

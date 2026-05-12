package com.example.api.service;

import com.example.api.repository.QueryRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class QueryEventPublisher {

	private static final Logger log = LoggerFactory.getLogger(QueryEventPublisher.class);

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	private final boolean enabled;
	private final String topic;

	public QueryEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper,
			@Value("${app.query-events.enabled:false}") boolean enabled,
			@Value("${app.query-events.topic:query.created.v1}") String topic) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
		this.enabled = enabled;
		this.topic = topic;
	}

	public void publishCreated(QueryRecord record) {
		if (!enabled) {
			return;
		}
		// Keep the event payload tiny and stable for a beginner-friendly async flow.
		QueryCreatedEvent event = new QueryCreatedEvent(record.id(), record.message(), record.createdAt().toString());
		try {
			String payload = Objects.requireNonNull(objectMapper.writeValueAsString(event),
					"Serialized query-created event payload must not be null");
			String key = Objects.requireNonNull(record.id(), "Query id must not be null for Kafka message key");
			// Use query id as key so all updates for the same query keep partition order.
			kafkaTemplate.send(Objects.requireNonNull(topic, "Kafka topic must not be null"), key, payload);
		} catch (JsonProcessingException ex) {
			log.error("Failed to serialize query-created event for id={}", record.id(), ex);
		}
	}
}

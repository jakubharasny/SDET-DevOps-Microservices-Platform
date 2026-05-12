package com.example.api.component.kafka;

import com.example.api.model.QueryStatus;
import com.example.api.repository.QueryRecord;
import com.example.api.service.QueryCreatedEvent;
import com.example.api.service.QueryEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"app.query-events.enabled=true", "app.query-events.topic=query.created.v1"})
@EmbeddedKafka(partitions = 1, topics = "query.created.v1")
class QueryEventPublisherKafkaIntegrationTest {

	@Autowired
	private QueryEventPublisher queryEventPublisher;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Autowired
	private ObjectMapper objectMapper;

	private Consumer<String, String> consumer;

	@AfterEach
	void tearDown() {
		if (consumer != null) {
			consumer.close();
		}
	}

	@Test
	void publishesSerializableEventToKafkaTopic() throws Exception {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("publisher-int-test", "true",
				embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumer = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new StringDeserializer())
				.createConsumer();
		embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "query.created.v1");

		QueryRecord record = new QueryRecord("q-123", "hello kafka", QueryStatus.PENDING, null, null, Instant.now(),
				Instant.now());
		queryEventPublisher.publishCreated(record);

		ConsumerRecord<String, String> kafkaRecord = KafkaTestUtils.getSingleRecord(consumer, "query.created.v1",
				Duration.ofSeconds(10));
		assertEquals("q-123", kafkaRecord.key());

		QueryCreatedEvent payload = objectMapper.readValue(kafkaRecord.value(), QueryCreatedEvent.class);
		assertEquals("q-123", payload.id());
		assertEquals("hello kafka", payload.message());
	}
}

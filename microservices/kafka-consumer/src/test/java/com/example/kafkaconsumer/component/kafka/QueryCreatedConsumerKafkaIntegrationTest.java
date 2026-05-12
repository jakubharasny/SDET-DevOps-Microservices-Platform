package com.example.kafkaconsumer.component.kafka;

import com.example.kafkaconsumer.consumer.QueryCreatedConsumer;
import com.example.kafkaconsumer.model.QueryCreatedEvent;
import com.example.kafkaconsumer.service.QueryEventProcessor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = QueryCreatedConsumerKafkaIntegrationTest.KafkaListenerTestConfig.class)
@EmbeddedKafka(partitions = 1, topics = "query.created.v1")
@DirtiesContext
class QueryCreatedConsumerKafkaIntegrationTest {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private RecordingProcessor recordingProcessor;

	@Test
	void consumesKafkaMessageAndDelegatesParsedEvent() throws Exception {
		kafkaTemplate.send("query.created.v1", "query-42",
				"{\"id\":\"query-42\",\"message\":\"hello\",\"createdAt\":\"2026-05-08T10:15:30Z\"}");
		kafkaTemplate.flush();

		assertTrue(recordingProcessor.awaitEvent(5), "Expected listener to delegate event within timeout");
		QueryCreatedEvent event = recordingProcessor.lastEvent();
		assertEquals("query-42", event.id());
		assertEquals("hello", event.message());
		assertEquals("2026-05-08T10:15:30Z", event.createdAt());
	}

	@Configuration
	@EnableKafka
	static class KafkaListenerTestConfig {

		@Bean
		RecordingProcessor recordingProcessor() {
			return new RecordingProcessor();
		}

		@Bean
		QueryCreatedConsumer queryCreatedConsumer(QueryEventProcessor queryEventProcessor) {
			return new QueryCreatedConsumer(queryEventProcessor);
		}

		@Bean
		ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker embeddedKafkaBroker) {
			Map<String, Object> props = new HashMap<>();
			props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString());
			props.put(ConsumerConfig.GROUP_ID_CONFIG, "query-created-consumer-int-test");
			props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
			props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
			props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
			return new DefaultKafkaConsumerFactory<>(props);
		}

		@Bean
		ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
				ConsumerFactory<String, String> consumerFactory) {
			ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
			factory.setConsumerFactory(consumerFactory);
			return factory;
		}

		@Bean
		ProducerFactory<String, String> producerFactory(EmbeddedKafkaBroker embeddedKafkaBroker) {
			Map<String, Object> props = new HashMap<>();
			props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString());
			props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			return new DefaultKafkaProducerFactory<>(props);
		}

		@Bean
		KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
			return new KafkaTemplate<>(producerFactory);
		}
	}

	static class RecordingProcessor implements QueryEventProcessor {

		private final CountDownLatch latch = new CountDownLatch(1);
		private QueryCreatedEvent lastEvent;

		@Override
		public void process(QueryCreatedEvent event) {
			this.lastEvent = event;
			latch.countDown();
		}

		boolean awaitEvent(int timeoutSeconds) throws InterruptedException {
			return latch.await(timeoutSeconds, TimeUnit.SECONDS);
		}

		QueryCreatedEvent lastEvent() {
			return lastEvent;
		}
	}
}

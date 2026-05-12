package com.example.kafkaconsumer.integration.kafka;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = KafkaBrokerRoundTripIntegrationTest.TestConfig.class)
@EmbeddedKafka(partitions = 1, topics = "broker.roundtrip.v1")
class KafkaBrokerRoundTripIntegrationTest {

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	private Producer<String, String> producer;
	private Consumer<String, String> consumer;

	@AfterEach
	void tearDown() {
		if (producer != null) {
			producer.close();
		}
		if (consumer != null) {
			consumer.close();
		}
	}

	@Test
	void storesAndReturnsRecordsFromTopicLog() {
		Map<String, Object> producerProps = Objects.requireNonNull(KafkaTestUtils.producerProps(embeddedKafkaBroker),
				"producerProps must not be null");
		producer = new DefaultKafkaProducerFactory<>(producerProps, new StringSerializer(), new StringSerializer())
				.createProducer();

		Map<String, Object> consumerProps = Objects.requireNonNull(
				KafkaTestUtils.consumerProps("broker-roundtrip-group", "true", embeddedKafkaBroker),
				"consumerProps must not be null");
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumer = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new StringDeserializer())
				.createConsumer();
		embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "broker.roundtrip.v1");

		producer.send(new ProducerRecord<>("broker.roundtrip.v1", "query-7", "payload-7"));
		producer.flush();

		ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "broker.roundtrip.v1",
				Duration.ofSeconds(10));
		assertEquals("query-7", record.key());
		assertEquals("payload-7", record.value());
	}

	@Configuration
	static class TestConfig {
	}
}

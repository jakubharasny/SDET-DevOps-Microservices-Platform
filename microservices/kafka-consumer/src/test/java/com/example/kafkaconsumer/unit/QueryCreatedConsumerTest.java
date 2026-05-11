package com.example.kafkaconsumer.unit;

import com.example.kafkaconsumer.consumer.QueryCreatedConsumer;
import com.example.kafkaconsumer.model.QueryCreatedEvent;
import com.example.kafkaconsumer.service.QueryEventProcessor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class QueryCreatedConsumerTest {

	@Test
	void parsesPayloadAndDelegatesToProcessingService() throws Exception {
		RecordingProcessor processor = new RecordingProcessor();
		QueryCreatedConsumer consumer = new QueryCreatedConsumer(processor);

		String payload = """
				{"id":"id-42","message":"demo","createdAt":"2026-05-08T18:00:00Z"}
				""";

		consumer.onMessage(payload);

		assertNotNull(processor.captured);
		assertEquals(new QueryCreatedEvent("id-42", "demo", "2026-05-08T18:00:00Z"), processor.captured);
	}

	private static final class RecordingProcessor implements QueryEventProcessor {

		private QueryCreatedEvent captured;

		@Override
		public void process(QueryCreatedEvent event) {
			this.captured = event;
		}
	}
}

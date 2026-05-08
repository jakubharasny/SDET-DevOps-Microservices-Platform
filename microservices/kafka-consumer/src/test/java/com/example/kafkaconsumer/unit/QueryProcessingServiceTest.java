package com.example.kafkaconsumer.unit;

import com.example.kafkaconsumer.model.QueryCreatedEvent;
import com.example.kafkaconsumer.repository.QueryProcessingRepository;
import com.example.kafkaconsumer.service.QueryProcessingService;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class QueryProcessingServiceTest {

	@Test
	void marksQueryAsDoneAfterProcessing() {
		RecordingRepository repository = new RecordingRepository();
		QueryProcessingService service = new QueryProcessingService(repository, 0);
		QueryCreatedEvent event = new QueryCreatedEvent("id-1", "hello kafka", Instant.now().toString());

		service.process(event);

		assertEquals("id-1", repository.processingId);
		assertEquals("id-1", repository.doneId);
		assertEquals("Processed by Kafka worker: HELLO KAFKA", repository.doneResult);
		assertNull(repository.failedId);
	}

	private static final class RecordingRepository extends QueryProcessingRepository {

		private String processingId;
		private String doneId;
		private String doneResult;
		private String failedId;

		private RecordingRepository() {
			super(null);
		}

		@Override
		public void markProcessing(String id, Instant updatedAt) {
			this.processingId = id;
		}

		@Override
		public void markDone(String id, String resultText, Instant updatedAt) {
			this.doneId = id;
			this.doneResult = resultText;
		}

		@Override
		public void markFailed(String id, String errorText, Instant updatedAt) {
			this.failedId = id;
		}
	}
}

package com.example.kafkaconsumer.repository;

import java.sql.Timestamp;
import java.time.Instant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QueryProcessingRepository {

	private final JdbcTemplate jdbcTemplate;

	public QueryProcessingRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void markProcessing(String id, Instant updatedAt) {
		jdbcTemplate.update("""
				UPDATE query_request
				SET status = ?, updated_at = ?
				WHERE id = ?
				""", "PROCESSING", Timestamp.from(updatedAt), id);
	}

	public void markDone(String id, String resultText, Instant updatedAt) {
		jdbcTemplate.update("""
				UPDATE query_request
				SET status = ?, result_text = ?, error_text = NULL, updated_at = ?
				WHERE id = ?
				""", "DONE", resultText, Timestamp.from(updatedAt), id);
	}

	public void markFailed(String id, String errorText, Instant updatedAt) {
		jdbcTemplate.update("""
				UPDATE query_request
				SET status = ?, error_text = ?, updated_at = ?
				WHERE id = ?
				""", "FAILED", errorText, Timestamp.from(updatedAt), id);
	}
}

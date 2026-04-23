package com.example.api.repository;

import com.example.api.model.QueryStatus;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class QueryRepository {

	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<QueryRecord> rowMapper = (rs, rowNum) -> new QueryRecord(
			rs.getString("id"),
			rs.getString("message"),
			QueryStatus.valueOf(rs.getString("status")),
			rs.getString("result_text"),
			rs.getString("error_text"),
			toInstant(rs.getTimestamp("created_at")),
			toInstant(rs.getTimestamp("updated_at")));

	public QueryRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void insert(QueryRecord record) {
		jdbcTemplate.update(
				"""
						INSERT INTO query_request
						    (id, message, status, result_text, error_text, created_at, updated_at)
						VALUES
						    (?, ?, ?, ?, ?, ?, ?)
						""",
				record.id(),
				record.message(),
				record.status().name(),
				record.resultText(),
				record.errorText(),
				Timestamp.from(record.createdAt()),
				Timestamp.from(record.updatedAt()));
	}

	@SuppressWarnings("null")
	public Optional<QueryRecord> findById(String id) {
		return jdbcTemplate.query("SELECT * FROM query_request WHERE id = ?", rowMapper, id).stream().findFirst();
	}

	private Instant toInstant(Timestamp timestamp) {
		return timestamp == null ? null : timestamp.toInstant();
	}
}

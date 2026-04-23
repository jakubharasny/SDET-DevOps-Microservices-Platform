package com.example.api.service;

import com.example.api.model.QueryStatus;
import com.example.api.repository.QueryRecord;
import com.example.api.repository.QueryRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

	private final QueryRepository queryRepository;

	public QueryService(QueryRepository queryRepository) {
		this.queryRepository = queryRepository;
	}

	public QueryRecord create(String message) {
		Instant now = Instant.now();
		QueryRecord record = new QueryRecord(
				UUID.randomUUID().toString(),
				message,
				QueryStatus.PENDING,
				null,
				null,
				now,
				now);
		queryRepository.insert(record);
		return record;
	}

	public Optional<QueryRecord> findById(String id) {
		return queryRepository.findById(id);
	}
}

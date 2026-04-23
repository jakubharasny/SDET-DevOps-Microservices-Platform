package com.example.api.repository;

import com.example.api.model.QueryStatus;
import java.time.Instant;

public record QueryRecord(
		String id,
		String message,
		QueryStatus status,
		String resultText,
		String errorText,
		Instant createdAt,
		Instant updatedAt) {
}

package com.example.api.model;

import java.time.Instant;

public record QueryDetailsResponse(
		String id,
		QueryStatus status,
		String message,
		String result,
		String error,
		Instant createdAt,
		Instant updatedAt) {
}

package com.example.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QueryCreateRequest(
		@NotBlank(message = "message is required") @Size(max = 120, message = "message must be <= 120 chars") String message) {
}

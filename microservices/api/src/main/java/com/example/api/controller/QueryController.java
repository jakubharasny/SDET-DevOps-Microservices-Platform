package com.example.api.controller;

import com.example.api.model.QueryCreateRequest;
import com.example.api.model.QueryCreatedResponse;
import com.example.api.model.QueryDetailsResponse;
import com.example.api.repository.QueryRecord;
import com.example.api.service.QueryService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/queries")
public class QueryController {

	private final QueryService queryService;

	public QueryController(QueryService queryService) {
		this.queryService = queryService;
	}

	@PostMapping
	public ResponseEntity<QueryCreatedResponse> create(@Valid @RequestBody QueryCreateRequest request) {
		QueryRecord created = queryService.create(request.message().trim());
		return ResponseEntity.status(HttpStatus.CREATED).body(new QueryCreatedResponse(created.id(), created.status()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) {
		return queryService.findById(id).<ResponseEntity<?>>map(record -> ResponseEntity.ok(toResponse(record)))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("error", "query not found", "id", id)));
	}

	private QueryDetailsResponse toResponse(QueryRecord record) {
		return new QueryDetailsResponse(
				record.id(),
				record.status(),
				record.message(),
				record.resultText(),
				record.errorText(),
				record.createdAt(),
				record.updatedAt());
	}
}

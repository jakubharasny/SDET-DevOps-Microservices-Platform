package com.example.frontend.component.http;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthEndpointTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void healthEndpointIsUp() {
		ResponseEntity<java.util.Map<String, Object>> response = restTemplate.exchange("/actuator/health",
				HttpMethod.GET, null, new ParameterizedTypeReference<java.util.Map<String, Object>>() {
				});

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isNotNull().containsEntry("status", "UP");
	}
}

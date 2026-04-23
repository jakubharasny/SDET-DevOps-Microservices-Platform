package com.example.api.component.mockmvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("null")
class QueryControllerMockMvcTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void createsAndReadsAsyncQuery() throws Exception {
		MvcResult createResult = mockMvc
				.perform(post("/api/queries").contentType(MediaType.APPLICATION_JSON).content("""
						{"message":"run async demo"}
						"""))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("PENDING")).andReturn();

		JsonNode createJson = objectMapper.readTree(createResult.getResponse().getContentAsString());
		String id = createJson.get("id").asText();

		mockMvc.perform(get("/api/queries/{id}", id)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.message").value("run async demo")).andExpect(jsonPath("$.status").value("PENDING"));
	}

	@Test
	void rejectsBlankMessage() throws Exception {
		mockMvc.perform(post("/api/queries").contentType(MediaType.APPLICATION_JSON).content("""
				{"message":"   "}
				""")).andExpect(status().isBadRequest());
	}

	@Test
	void returnsNotFoundForUnknownId() throws Exception {
		mockMvc.perform(get("/api/queries/{id}", "missing-id")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("query not found"));
	}
}

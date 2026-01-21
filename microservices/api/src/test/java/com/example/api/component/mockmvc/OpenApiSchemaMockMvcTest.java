package com.example.api.component.mockmvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiSchemaMockMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void writesOpenApiSchemaToTarget() throws Exception {
		MvcResult result = mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk()).andReturn();

		String schema = result.getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode schemaJson = mapper.readTree(schema);
		String prettySchema = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaJson);

		assertThat(schema).isNotBlank();
		assertThat(schema).contains("\"/api/countries\"");

		Path outputPath = Path.of("..", "..", "docs", "openapi", "api.json");
		Files.createDirectories(outputPath.getParent());
		Files.writeString(outputPath, prettySchema + System.lineSeparator());
	}
}

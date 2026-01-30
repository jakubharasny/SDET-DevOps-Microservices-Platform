package com.example.api.component.mockmvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CountriesControllerMockMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@SuppressWarnings("null")
	@Test
	void returnsCountryCurrencyList() throws Exception {
		mockMvc.perform(get("/api/countries")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(27)))
				.andExpect(jsonPath("$[0].country").value("Austria"))
				.andExpect(jsonPath("$[0].currencyCode").value("EUR"));
	}
}

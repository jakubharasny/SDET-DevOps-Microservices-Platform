package com.example.frontend.component.mockmvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("null")
@SpringBootTest(properties = "api.base-url=http://127.0.0.1:9")
@AutoConfigureMockMvc
class HomePageMockMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void rendersButtonAndCountriesSection() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content()
						.string(org.hamcrest.Matchers.containsString("Click this button to show all the countries")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("countries-panel")));
	}
}

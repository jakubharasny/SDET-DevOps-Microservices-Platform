package com.example.api.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.example.api.model.CountryCurrency;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class CountryCatalog {

	private final List<CountryCurrency> countries;

	public CountryCatalog(ObjectMapper objectMapper) throws IOException {
		try (InputStream inputStream = new ClassPathResource("countries.json").getInputStream()) {
			List<CountryCurrency> loaded = objectMapper.readValue(inputStream,
					new TypeReference<List<CountryCurrency>>() {
					});
			this.countries = List.copyOf(loaded);
		}
	}

	public List<CountryCurrency> getCountries() {
		return countries;
	}
}

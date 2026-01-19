package com.example.frontend.client;

import java.util.List;

import com.example.frontend.model.CountryCurrency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CountryApiClient {

    private final RestClient restClient;

    @SuppressWarnings("null")
    public CountryApiClient(RestClient.Builder restClientBuilder,
                            @Value("${api.base-url:http://localhost:8081}") String apiBaseUrl) {
        this.restClient = restClientBuilder.baseUrl(apiBaseUrl).build();
    }

    public List<CountryCurrency> fetchCountries() {
        List<CountryCurrency> response = restClient.get()
                .uri("/api/countries")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CountryCurrency>>() {
                });
        return response == null ? List.of() : response;
    }
}

package com.example.api.unit;

import com.example.api.service.CountryCatalog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountryCatalogTest {

    @Test
    void loadsCountriesFromResource() throws Exception {
        CountryCatalog catalog = new CountryCatalog(new ObjectMapper());

        assertThat(catalog.getCountries()).isNotEmpty();
        assertThat(catalog.getCountries().get(0).country()).isEqualTo("Austria");
    }
}

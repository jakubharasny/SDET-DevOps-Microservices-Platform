package com.example.api.controller;

import java.util.List;

import com.example.api.model.CountryCurrency;
import com.example.api.service.CountryCatalog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountriesController {

    private final CountryCatalog countryCatalog;

    public CountriesController(CountryCatalog countryCatalog) {
        this.countryCatalog = countryCatalog;
    }

    @GetMapping("/api/countries")
    public List<CountryCurrency> countries() {
        return countryCatalog.getCountries();
    }
}

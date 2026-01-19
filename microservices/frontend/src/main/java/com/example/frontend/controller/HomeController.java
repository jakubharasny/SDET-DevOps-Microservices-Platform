package com.example.frontend.controller;

import java.time.LocalDateTime; // captures the current timestamp so the UI shows when it was refreshed
import java.time.format.DateTimeFormatter; // formats the timestamp into a readable "MMM d, yyyy HH:mm" pattern
import java.util.List;

import com.example.frontend.client.CountryApiClient;
import com.example.frontend.model.CountryCurrency;
import org.springframework.stereotype.Controller; // marks this class as a Spring MVC controller
import org.springframework.ui.Model; // carries values from the controller to the Thymeleaf template
import org.springframework.web.bind.annotation.GetMapping; // maps HTTP GET requests to handler methods
import org.springframework.web.client.RestClientException;

@Controller
/**
 * Simple UI controller that responds to "/" and pushes placeholder environment
 * data into the Thymeleaf template so we can iterate on a DevOps dashboard.
 */
public class HomeController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm");
    private final CountryApiClient countryApiClient;

    public HomeController(CountryApiClient countryApiClient) {
        this.countryApiClient = countryApiClient;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<CountryCurrency> countries = List.of();
        String apiStatus = "unavailable";
        String apiError = null;

        try {
            countries = countryApiClient.fetchCountries();
            apiStatus = "ok";
            if (countries.isEmpty()) {
                apiError = "API returned no data.";
            }
        } catch (RestClientException ex) {
            apiError = "API request failed: " + ex.getMessage();
        }

        model.addAttribute("environment", "Preview");
        model.addAttribute("message", "Ready to plug into the DevOps platform");
        model.addAttribute("timestamp", LocalDateTime.now().format(FORMATTER));
        model.addAttribute("countries", countries);
        model.addAttribute("apiStatus", apiStatus);
        model.addAttribute("apiError", apiError);
        return "home";
    }
}

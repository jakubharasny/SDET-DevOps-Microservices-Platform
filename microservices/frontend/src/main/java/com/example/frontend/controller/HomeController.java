package com.example.frontend.controller;

import java.time.LocalDateTime; // captures the current timestamp so the UI shows when it was refreshed
import java.time.format.DateTimeFormatter; // formats the timestamp into a readable "MMM d, yyyy HH:mm" pattern
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller; // marks this class as a Spring MVC controller
import org.springframework.ui.Model; // carries values from the controller to the Thymeleaf template
import org.springframework.web.bind.annotation.GetMapping; // maps HTTP GET requests to handler methods

@Controller
/**
 * Simple UI controller that responds to "/" and pushes placeholder environment
 * data into the Thymeleaf template so we can iterate on a DevOps dashboard.
 */
public class HomeController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm");
    private final String apiBaseUrl;

    public HomeController(@Value("${api.base-url:http://localhost:8081}") String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("environment", "Preview");
        model.addAttribute("message", "Ready to plug into the DevOps platform");
        model.addAttribute("timestamp", LocalDateTime.now().format(FORMATTER));
        model.addAttribute("apiBaseUrl", apiBaseUrl);
        return "home";
    }
}

package com.example.e2e;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Route;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class CountriesE2ETest {

    @Test
    void loadsCountriesFromMockedApiAndRendersCards() {
        String baseUrl = System.getenv().getOrDefault("E2E_BASE_URL", "http://localhost:8080");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();
            page.route("**/api/countries", route -> route.fulfill(jsonResponse("""
                    [
                      {"country":"Austria","currencyName":"Euro","currencyCode":"EUR"},
                      {"country":"Poland","currencyName":"Polish Zloty","currencyCode":"PLN"}
                    ]
                    """)));

            page.navigate(baseUrl + "/");
            page.locator("#toggle-countries").click();

            assertThat(page.locator("#api-status")).hasText("ok");
            assertThat(page.locator(".country-card h3").first()).hasText("Austria");
            assertThat(page.locator(".country-card span").first()).hasText("EUR");
            assertThat(page.locator(".country-card h3").nth(1)).hasText("Poland");
            assertThat(page.locator("#toggle-countries")).hasText("Hide countries");
            browser.close();
        }
    }

    @Test
    void runsAsyncFlowWithMockedApiAndShowsDoneResult() {
        String baseUrl = System.getenv().getOrDefault("E2E_BASE_URL", "http://localhost:8080");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();

            page.route("**/api/queries", route -> {
                if ("POST".equalsIgnoreCase(route.request().method())) {
                    route.fulfill(jsonResponse("{\"id\":\"query-123\",\"status\":\"QUEUED\"}"));
                    return;
                }
                route.fallback();
            });

            final int[] pollCount = {0};
            page.route("**/api/queries/query-123", route -> {
                pollCount[0]++;
                if (pollCount[0] < 2) {
                    route.fulfill(jsonResponse("{\"id\":\"query-123\",\"status\":\"PROCESSING\"}"));
                    return;
                }
                route.fulfill(jsonResponse("""
                        {
                          "id":"query-123",
                          "status":"DONE",
                          "result":"processed: hello from playwright"
                        }
                        """));
            });

            page.navigate(baseUrl + "/");
            page.locator("#query-message").fill("hello from playwright");
            page.locator("#simulate-kafka").click();

            assertThat(page.locator("#kafka-timeline")).containsText("API accepted job query-123");
            assertThat(page.locator("#kafka-timeline")).containsText("Poll 1: status=PROCESSING");
            assertThat(page.locator("#kafka-timeline")).containsText("Poll 2: status=DONE");
            assertThat(page.locator("#kafka-timeline")).containsText("Worker finished. Result: processed: hello from playwright");
            browser.close();
        }
    }

    private Route.FulfillOptions jsonResponse(String body) {
        return new Route.FulfillOptions()
                .setStatus(200)
                .setContentType("application/json")
                .setBody(body);
    }
}

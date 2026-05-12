package com.example.e2e;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountriesE2ETest {

    @Test
    void loadsCountriesFromRealApiAndRendersCards() {
        String baseUrl = System.getenv().getOrDefault("E2E_BASE_URL", "http://localhost:8080");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();

            page.navigate(baseUrl + "/");
            page.locator("#toggle-countries").click();

            assertThat(page.locator("#api-status")).hasText("ok");
            int renderedCards = page.locator(".country-card").count();
            assertTrue(renderedCards > 0, "Expected at least one country card from real API");
            assertThat(page.locator(".country-card h3").first()).isVisible();
            assertThat(page.locator("#toggle-countries")).hasText("Hide countries");
            browser.close();
        }
    }

    @Test
    void runsAsyncKafkaFlowAgainstRealBackendAndShowsDoneResult() {
        String baseUrl = System.getenv().getOrDefault("E2E_BASE_URL", "http://localhost:8080");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();
            page.navigate(baseUrl + "/");
            page.locator("#query-message").fill("hello from real e2e");
            page.locator("#simulate-kafka").click();

            assertThat(page.locator("#kafka-timeline")).containsText("UI sent POST /api/queries with message: \"hello from real e2e\"");
            assertThat(page.locator("#kafka-timeline")).containsText("Kafka worker is now processing asynchronously...");
            assertThat(page.locator("#kafka-timeline")).containsText("status=DONE");
            assertThat(page.locator("#kafka-timeline"))
                    .containsText("Worker finished. Result: Processed by Kafka worker: HELLO FROM REAL E2E");
            browser.close();
        }
    }
}

package com.example.e2e;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.options.AriaRole.CELL;
import static com.microsoft.playwright.options.AriaRole.ROW;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class CountriesE2ETest {

    @Test
    void loadsCountriesAfterClick() {
        String baseUrl = System.getenv().getOrDefault("E2E_BASE_URL", "http://localhost:8080");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();

            page.navigate(baseUrl + "/");
            page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Click this button to show all the countries"))
                    .click();

            Locator austriaRow = page.getByRole(ROW, new Page.GetByRoleOptions().setName("Austria Euro EUR"));
            assertThat(austriaRow.getByRole(CELL, new Locator.GetByRoleOptions().setName("Austria"))).isVisible();
            assertThat(austriaRow.getByRole(CELL, new Locator.GetByRoleOptions().setName("EUR").setExact(true)))
                    .isVisible();
        }
    }
}

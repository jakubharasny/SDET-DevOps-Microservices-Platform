package com.example.frontend.component.playwright.tests;

import com.example.frontend.component.playwright.core.FrontendUiPlaywrightBaseTest;
import com.example.frontend.component.playwright.pages.FrontendHomePage;
import com.example.frontend.component.playwright.support.FrontendApiMockRoutes;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class MockedCountriesFlowPlaywrightTest extends FrontendUiPlaywrightBaseTest {

	@Test
	void loadsCountriesFromMockedApiAndRendersCards() {
		FrontendApiMockRoutes.mockCountriesSuccess(page);

		FrontendHomePage homePage = new FrontendHomePage(page);
		homePage.open(config.baseUrl());
		homePage.clickLoadCountries();

		assertThat(page.locator("#api-status")).hasText("ok");
		assertThat(page.locator(".country-card h3").first()).hasText("Austria");
		assertThat(page.locator(".country-card span").first()).hasText("EUR");
		assertThat(page.locator(".country-card h3").nth(1)).hasText("Poland");
		assertThat(page.locator("#toggle-countries")).hasText("Hide countries");
	}
}

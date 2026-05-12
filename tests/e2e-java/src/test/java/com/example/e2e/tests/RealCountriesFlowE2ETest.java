package com.example.e2e.tests;

import com.example.e2e.core.PlatformE2eBaseTest;
import com.example.e2e.pages.MeshOpsHomePage;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RealCountriesFlowE2ETest extends PlatformE2eBaseTest {

	@Test
	void loadsCountriesFromRealApiAndRendersCards() {
		MeshOpsHomePage homePage = new MeshOpsHomePage(page);
		homePage.open(config.baseUrl());
		homePage.clickLoadCountries();

		assertThat(page.locator("#api-status")).hasText("ok");
		assertTrue(homePage.renderedCountryCards() > 0, "Expected at least one country card from real API");
		assertThat(page.locator(".country-card h3").first()).isVisible();
		assertThat(page.locator("#toggle-countries")).hasText("Hide countries");
	}
}

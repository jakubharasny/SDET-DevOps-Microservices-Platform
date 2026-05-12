package com.example.e2e.pages;

import com.microsoft.playwright.Page;

public class MeshOpsHomePage {

	private final Page page;

	public MeshOpsHomePage(Page page) {
		this.page = page;
	}

	public void open(String baseUrl) {
		page.navigate(baseUrl + "/");
	}

	public void clickLoadCountries() {
		page.locator("#toggle-countries").click();
	}

	public String apiStatusText() {
		return page.locator("#api-status").textContent();
	}

	public int renderedCountryCards() {
		return page.locator(".country-card").count();
	}

	public void submitAsyncMessage(String message) {
		page.locator("#query-message").fill(message);
		page.locator("#simulate-kafka").click();
	}

	public String timelineText() {
		return page.locator("#kafka-timeline").textContent();
	}

	public String countriesToggleText() {
		return page.locator("#toggle-countries").textContent();
	}
}

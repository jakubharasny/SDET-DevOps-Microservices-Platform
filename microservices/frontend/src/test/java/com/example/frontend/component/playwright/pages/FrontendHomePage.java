package com.example.frontend.component.playwright.pages;

import com.microsoft.playwright.Page;

public class FrontendHomePage {

	private final Page page;

	public FrontendHomePage(Page page) {
		this.page = page;
	}

	public void open(String baseUrl) {
		page.navigate(baseUrl + "/");
	}

	public void clickLoadCountries() {
		page.locator("#toggle-countries").click();
	}

	public void submitAsyncMessage(String message) {
		page.locator("#query-message").fill(message);
		page.locator("#simulate-kafka").click();
	}

	public Page page() {
		return page;
	}
}

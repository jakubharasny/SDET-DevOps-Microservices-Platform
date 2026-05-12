package com.example.e2e.core;

import com.example.e2e.config.PlatformE2eConfig;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class PlatformE2eBaseTest {

	protected PlatformE2eConfig config;
	protected Playwright playwright;
	protected Browser browser;
	protected BrowserContext browserContext;
	protected Page page;

	@BeforeEach
	void setUpBrowser() {
		config = PlatformE2eConfig.fromEnvironment();
		playwright = Playwright.create();
		browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(config.headless()));
		browserContext = browser.newContext();
		page = browserContext.newPage();
		PlaywrightAssertions.setDefaultAssertionTimeout(config.assertionTimeoutMs());
	}

	@AfterEach
	void tearDownBrowser() {
		if (browserContext != null) {
			browserContext.close();
		}
		if (browser != null) {
			browser.close();
		}
		if (playwright != null) {
			playwright.close();
		}
	}
}

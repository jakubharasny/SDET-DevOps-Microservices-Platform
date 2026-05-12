package com.example.frontend.component.playwright.core;

import com.example.frontend.component.playwright.config.FrontendUiPlaywrightConfig;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public abstract class FrontendUiPlaywrightBaseTest {

	protected FrontendUiPlaywrightConfig config;
	protected Playwright playwright;
	protected Browser browser;
	protected BrowserContext browserContext;
	protected Page page;

	@BeforeEach
	void setUpBrowser() {
		config = FrontendUiPlaywrightConfig.fromEnvironment();
		assumeTrue(config.runPlaywrightUi(), "Set RUN_PLAYWRIGHT_UI=true to execute frontend Playwright mocked tests.");

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

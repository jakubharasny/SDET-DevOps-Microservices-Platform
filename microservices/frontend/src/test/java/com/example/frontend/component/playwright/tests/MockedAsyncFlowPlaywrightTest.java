package com.example.frontend.component.playwright.tests;

import com.example.frontend.component.playwright.core.FrontendUiPlaywrightBaseTest;
import com.example.frontend.component.playwright.pages.FrontendHomePage;
import com.example.frontend.component.playwright.support.FrontendApiMockRoutes;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class MockedAsyncFlowPlaywrightTest extends FrontendUiPlaywrightBaseTest {

	@Test
	void runsAsyncFlowWithMockedApiAndShowsDoneResult() {
		FrontendApiMockRoutes.mockAsyncQueryDoneFlow(page);

		FrontendHomePage homePage = new FrontendHomePage(page);
		homePage.open(config.baseUrl());
		homePage.submitAsyncMessage("hello from playwright");

		assertThat(page.locator("#kafka-timeline")).containsText("API accepted job query-123");
		assertThat(page.locator("#kafka-timeline")).containsText("Poll 1: status=PROCESSING");
		assertThat(page.locator("#kafka-timeline")).containsText("Poll 2: status=DONE");
		assertThat(page.locator("#kafka-timeline")).containsText("Worker finished. Result: processed: hello from playwright");
	}
}

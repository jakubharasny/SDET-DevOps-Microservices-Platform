package com.example.e2e.tests;

import com.example.e2e.core.PlatformE2eBaseTest;
import com.example.e2e.pages.MeshOpsHomePage;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class RealAsyncKafkaFlowE2ETest extends PlatformE2eBaseTest {

	@Test
	void runsAsyncKafkaFlowAgainstRealBackendAndShowsDoneResult() {
		MeshOpsHomePage homePage = new MeshOpsHomePage(page);
		homePage.open(config.baseUrl());
		homePage.submitAsyncMessage("hello from real e2e");

		assertThat(page.locator("#kafka-timeline"))
				.containsText("UI sent POST /api/queries with message: \"hello from real e2e\"");
		assertThat(page.locator("#kafka-timeline")).containsText("Kafka worker is now processing asynchronously...");
		assertThat(page.locator("#kafka-timeline")).containsText("status=DONE");
		assertThat(page.locator("#kafka-timeline"))
				.containsText("Worker finished. Result: Processed by Kafka worker: HELLO FROM REAL E2E");
	}
}

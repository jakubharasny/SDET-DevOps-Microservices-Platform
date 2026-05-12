package com.example.frontend.component.playwright.config;

public final class FrontendUiPlaywrightConfig {

	private final String baseUrl;
	private final boolean headless;
	private final int assertionTimeoutMs;
	private final boolean runPlaywrightUi;

	private FrontendUiPlaywrightConfig(String baseUrl, boolean headless, int assertionTimeoutMs,
			boolean runPlaywrightUi) {
		this.baseUrl = baseUrl;
		this.headless = headless;
		this.assertionTimeoutMs = assertionTimeoutMs;
		this.runPlaywrightUi = runPlaywrightUi;
	}

	public static FrontendUiPlaywrightConfig fromEnvironment() {
		String baseUrl = System.getenv().getOrDefault("E2E_BASE_URL", "http://localhost:8080");
		boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("E2E_HEADLESS", "true"));
		int assertionTimeoutMs = Integer.parseInt(System.getenv().getOrDefault("E2E_ASSERT_TIMEOUT_MS", "10000"));
		boolean runPlaywrightUi = Boolean.parseBoolean(System.getenv().getOrDefault("RUN_PLAYWRIGHT_UI", "false"));
		return new FrontendUiPlaywrightConfig(baseUrl, headless, assertionTimeoutMs, runPlaywrightUi);
	}

	public String baseUrl() {
		return baseUrl;
	}

	public boolean headless() {
		return headless;
	}

	public int assertionTimeoutMs() {
		return assertionTimeoutMs;
	}

	public boolean runPlaywrightUi() {
		return runPlaywrightUi;
	}
}

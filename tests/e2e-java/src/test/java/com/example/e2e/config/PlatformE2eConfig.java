package com.example.e2e.config;

public final class PlatformE2eConfig {

	private final String baseUrl;
	private final boolean headless;
	private final int assertionTimeoutMs;

	private PlatformE2eConfig(String baseUrl, boolean headless, int assertionTimeoutMs) {
		this.baseUrl = baseUrl;
		this.headless = headless;
		this.assertionTimeoutMs = assertionTimeoutMs;
	}

	public static PlatformE2eConfig fromEnvironment() {
		String baseUrl = System.getenv().getOrDefault("E2E_BASE_URL", "http://localhost:8080");
		boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("E2E_HEADLESS", "true"));
		int assertionTimeoutMs = Integer.parseInt(System.getenv().getOrDefault("E2E_ASSERT_TIMEOUT_MS", "10000"));
		return new PlatformE2eConfig(baseUrl, headless, assertionTimeoutMs);
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
}

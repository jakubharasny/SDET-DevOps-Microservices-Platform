package com.example.frontend.component.playwright.support;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;

public class FrontendApiMockRoutes {

	private FrontendApiMockRoutes() {
	}

	public static void mockCountriesSuccess(Page page) {
		page.route("**/api/countries", route -> route.fulfill(jsonResponse("""
				[
				  {"country":"Austria","currencyName":"Euro","currencyCode":"EUR"},
				  {"country":"Poland","currencyName":"Polish Zloty","currencyCode":"PLN"}
				]
				""")));
	}

	public static void mockAsyncQueryDoneFlow(Page page) {
		page.route("**/api/queries", route -> {
			if ("POST".equalsIgnoreCase(route.request().method())) {
				route.fulfill(jsonResponse("{\"id\":\"query-123\",\"status\":\"QUEUED\"}"));
				return;
			}
			route.fallback();
		});

		final int[] pollCount = {0};
		page.route("**/api/queries/query-123", route -> {
			pollCount[0]++;
			if (pollCount[0] < 2) {
				route.fulfill(jsonResponse("{\"id\":\"query-123\",\"status\":\"PROCESSING\"}"));
				return;
			}
			route.fulfill(jsonResponse("""
					{
					  "id":"query-123",
					  "status":"DONE",
					  "result":"processed: hello from playwright"
					}
					"""));
		});
	}

	private static Route.FulfillOptions jsonResponse(String body) {
		return new Route.FulfillOptions().setStatus(200).setContentType("application/json").setBody(body);
	}
}

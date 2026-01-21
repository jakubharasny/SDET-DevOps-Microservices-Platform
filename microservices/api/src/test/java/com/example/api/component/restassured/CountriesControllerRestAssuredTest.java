package com.example.api.component.restassured;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CountriesControllerRestAssuredTest {

	@LocalServerPort
	private int port;

	@Test
	void returnsCountryCurrencyList() {
		given().when().get("/api/countries").then().statusCode(200).body("$", hasSize(27))
				.body("[0].country", equalTo("Austria")).body("[0].currencyCode", equalTo("EUR"));
	}

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}
}

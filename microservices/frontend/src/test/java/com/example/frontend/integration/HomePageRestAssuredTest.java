package com.example.frontend.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "api.base-url=http://localhost:8081"
)
class HomePageRestAssuredTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void rendersButtonAndCountriesPanel() {
        given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body(containsString("Click this button to show all the countries"))
                .body(containsString("countries-panel"));
    }
}

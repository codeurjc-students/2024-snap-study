package com.snapstudy.backend.tests.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class PaginationApiTest {

    @BeforeAll
    public static void setUp() {
        // Configure RestAssured for SSL validation
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void testGetSubjects_Success() {
        given()
                .pathParam("degreeId", 52)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("https://localhost:8443/api/subjects/degrees/{degreeId}")
                .then()
                .log().ifValidationFails()
                .statusCode(200) // Verify that the response is 200 OK
                .body("content.size()", greaterThan(0)); // Verify that the response contains content
    }

    @Test
    public void testGetSubjects_NotFound() {
        given()
                .pathParam("degreeId", 99999) // Non-existent degree ID
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("https://localhost:8443/api/subjects/degrees/{degreeId}")
                .then()
                .log().ifValidationFails()
                .statusCode(404); // Verify that the response is 404 Not Found
    }

}

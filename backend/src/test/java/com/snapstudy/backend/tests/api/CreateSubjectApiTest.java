package com.snapstudy.backend.tests.api;

import java.util.Map;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class CreateSubjectApiTest {

    private static Map<String, String> cookies;
    private static LoginApiTestService loginApiTestService;

    @BeforeAll
    public static void setUp() {
        // Configure RestAssured for SSL validation
        RestAssured.useRelaxedHTTPSValidation();
        loginApiTestService = new LoginApiTestService();
        cookies = loginApiTestService.loginAndGetCookies("admin@admin.com", "admin");
    }

    @Test
    public void testCreateSubject_Success() {
        given()
                .cookies(cookies)
                .pathParam("degreeId", 52)
                .contentType("application/json")
                .body("Subject Test") // Subject name
                .when()
                .post("https://localhost:8443/api/subjects/{degreeId}")
                .then()
                .log().ifValidationFails()
                .statusCode(200) // Verify that it is successfully created
                .body("name", equalTo("Subject Test")); // Verify that the subject's name is as expected
    }

    @Test
    public void testCreateSubject_DegreeNotFound() {
        given()
                .cookies(cookies)
                .pathParam("degreeId", 999) // Non-existent degree ID
                .contentType("application/json")
                .body("Physics")
                .when()
                .post("https://localhost:8443/api/subjects/{degreeId}")
                .then()
                .log().ifValidationFails()
                .statusCode(404); // Verify that the response is 404 Not Found
    }

    @Test
    public void testCreateSubject_AlreadyExists() {
        given()
                .cookies(cookies)
                .pathParam("degreeId", 52)
                .contentType("application/json")
                .body("Cloud Computing") // Subject name
                .when()
                .post("https://localhost:8443/api/subjects/{degreeId}")
                .then()
                .log().ifValidationFails()
                .statusCode(409); // Verify that the response is 409 Conflict
    }
}

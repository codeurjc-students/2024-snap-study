package com.snapstudy.backend.tests.api;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

public class DownloadDocumentApiTest {

    private static Map<String, String> cookies;
    private static LoginApiTestService loginApiTestService;

    @BeforeAll
    public static void setUp() {
        // Configure RestAssured for SSL validation
        RestAssured.useRelaxedHTTPSValidation();
        loginApiTestService = new LoginApiTestService();
        cookies = loginApiTestService.loginAndGetCookies("javiisalaas97@gmail.com", "hola");
    }

    @Test
    public void testDownloadDocument_Success() {

        given()
                .cookies(cookies)
                .pathParam("id", 6) // Valid document ID
                .when()
                .get("https://localhost:443/api/documents/download/{id}")
                .then()
                .log().ifValidationFails()
                .statusCode(200) // Verify that the response is 200 OK
                .header("Content-Disposition", containsString("attachment;"))
                // Verify that the headers include download information
                .contentType("application/octet-stream"); // Verify that the content type is as expected
    }

    @Test
    public void testDownloadDocument_NotFound() throws Exception {

        given()
                .cookies(cookies)
                .pathParam("id", 6654) // Valid document ID
                .when()
                .get("https://localhost:443/api/documents/download/{id}")
                .then()
                .log().ifValidationFails()
                .statusCode(404); // Verify that the response is 404 Not Found
    }

}
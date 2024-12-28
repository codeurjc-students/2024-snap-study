package com.snapstudy.backend.tests.api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProfileImageUploadApiTest {

    private static Map<String, String> cookies;
    private static LoginApiTestService loginApiTestService;
    private static String API_URL = "https://localhost:8443/api/users/image";

    @BeforeAll
    public static void setUp() {
        // Configure RestAssured for SSL validation
        RestAssured.useRelaxedHTTPSValidation();
        loginApiTestService = new LoginApiTestService();
        cookies = loginApiTestService.loginAndGetCookies("javiisalaas97@gmail.com", "hola");
    }

    @Test
    public void testUploadProfileImageSuccess() throws IOException {

        File testImage = new File("D:/DESCARGAS/mia.jpg"); // local

        if (!testImage.exists()) {
            // Create a temporary file to simulate an image
            testImage = File.createTempFile("profile", ".jpg");
            FileUtils.writeByteArrayToFile(testImage, new byte[] { 0, 1, 2, 3 }); // Mock content
        }

        // Make the PUT request to upload the image, including the session cookies
        RequestSpecification request = given()
                .cookies(cookies)
                .header("Content-Type", "multipart/form-data")
                .multiPart("file", testImage);
        request.when()
                .put(API_URL)
                .then()
                .log().ifError() // Log errors if any occur
                .statusCode(201); // Verify that the response is 201 Created
    }

    @Test
    public void testUploadProfileImageSFailure() throws IOException {
        // Empty
        File testImage = File.createTempFile("profile", ".jpg");

        RequestSpecification request = given()
                .cookies(cookies)
                .header("Content-Type", "multipart/form-data")
                .multiPart("file", testImage);
        request.when()
                .put(API_URL)
                .then()
                .log().ifError()
                .statusCode(400); // Verify that the response is 400 Bad Request
    }
}

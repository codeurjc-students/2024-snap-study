package com.snapstudy.backend.tests.api;

import java.io.File;
import java.util.Map;
import org.apache.commons.io.FileUtils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

public class UploadDocumentApiTest {
    
    private static Map<String, String> cookies;
    private static LoginApiTestService loginApiTestService;
    private static String API_URL = "https://localhost:8443/api/documents/{degreeId}/{subjectId}";

    @BeforeAll
    public static void setUp() {
        // Configure RestAssured for SSL validation
        RestAssured.useRelaxedHTTPSValidation();
        loginApiTestService = new LoginApiTestService();
        cookies = loginApiTestService.loginAndGetCookies("admin@admin.com", "admin");
    }

    @Test
    public void testSaveDocument_Success() throws Exception {

        File testFile = new File("D:/DESCARGAS/Chuleta_SQL.pdf"); // Path

        if (!testFile.exists()) {
            // Create a temporary file to simulate an image
            testFile = File.createTempFile("Chuleta_SQL", ".pdf");
            FileUtils.writeByteArrayToFile(testFile, new byte[] { 0, 1, 2, 3 }); // Mock content
        }

        given()
                .cookies(cookies)
                .pathParam("degreeId", 52) // Valid degree ID
                .pathParam("subjectId", 1) // Valid subject ID
                .multiPart("file", testFile) // File to upload
                .when()
                .post(API_URL)
                .then()
                .log().ifValidationFails()
                .statusCode(200) // Verify that the response is 200 OK
                .body("name", equalTo("Chuleta_SQL"));
        // Verify that the document's name is as expected
    }

    @Test
    public void testSaveDocument_Conflict() throws Exception{
        // Assuming a document with the same name already exists
        File testFile = new File("D:/DESCARGAS/cubo.pdf"); // Path

        if (!testFile.exists()) {
            // Create a temporary file to simulate an image
            testFile = File.createTempFile("cubo", ".pdf");
            FileUtils.writeByteArrayToFile(testFile, new byte[] { 0, 1, 2, 3 }); // Mock content
        }

        given()
                .cookies(cookies)
                .pathParam("degreeId", 52) // Valid degree ID
                .pathParam("subjectId", 1) // Valid subject ID
                .multiPart("file", testFile)
                .when()
                .post(API_URL)
                .then()
                .log().ifValidationFails()
                .statusCode(409); // Verify that the response is 409 Conflict
    }

    @Test
    public void testSaveDocument_BadRequest() throws Exception{
        
        File testFile = new File("D:/DESCARGAS/Chuleta_SQL.pdf"); // Path

        if (!testFile.exists()) {
            // Create a temporary file to simulate an image
            testFile = File.createTempFile("Chuleta_SQL", ".pdf");
            FileUtils.writeByteArrayToFile(testFile, new byte[] { 0, 1, 2, 3 }); // Mock content
        }

        given()
                .cookies(cookies)
                .pathParam("degreeId", 52) // Valid degree ID
                .pathParam("subjectId", 78796) // Invalid subject ID
                .multiPart("file", testFile)
                .when()
                .post(API_URL)
                .then()
                .log().ifValidationFails()
                .statusCode(400); // Verify that the response is 400 Bad Request
    }

    @Test
    public void testSaveDocument_NotFound() throws Exception{
        
        File testFile = new File("D:/DESCARGAS/Chuleta_SQL.pdf"); // Path

        if (!testFile.exists()) {
            // Create a temporary file to simulate an image
            testFile = File.createTempFile("Chuleta_SQL", ".pdf");
            FileUtils.writeByteArrayToFile(testFile, new byte[] { 0, 1, 2, 3 }); // Mock content
        }

        given()
                .cookies(cookies)
                .pathParam("degreeId", 5243) // Invalid degree ID
                .pathParam("subjectId", 1) // Valid subject ID
                .multiPart("file", testFile)
                .when()
                .post(API_URL)
                .then()
                .log().ifValidationFails()
                .statusCode(404); // Verify that the response is 404 Not Found
    }

}

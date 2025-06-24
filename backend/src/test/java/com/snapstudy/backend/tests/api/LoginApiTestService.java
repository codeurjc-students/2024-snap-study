package com.snapstudy.backend.tests.api;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class LoginApiTestService {

    public Map<String, String> loginAndGetCookies(String email, String password) {
        // We make the POST request to log in and obtain the cookies
        return given()
                .contentType("application/json")
                .body("{\"username\":\"" + email + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("https://localhost:8443/api/auth/login")
                .then()
                .statusCode(200) // We verify that the login was successful
                .extract()
                .cookies(); // We extract the session cookie
    }

}
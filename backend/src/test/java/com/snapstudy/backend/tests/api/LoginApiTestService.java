package com.snapstudy.backend.tests.api;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class LoginApiTestService {

    public Map<String, String> loginAndGetCookies(String email, String password) {
        // Realizamos la solicitud POST para hacer login y obtener las cookies
        return given()
                .contentType("application/json")
                .body("{\"username\":\"" + email + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("https://localhost:8443/api/auth/login")
                .then()
                .statusCode(200) // Verificamos que el login haya sido exitoso
                .extract()
                .cookies(); // Extraemos la cookie de sesión
    }

}
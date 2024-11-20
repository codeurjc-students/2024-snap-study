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

    @BeforeAll
    public static void setUp() {
        // Configurar RestAssured para la validación de SSL
        RestAssured.useRelaxedHTTPSValidation();

        cookies = loginAndGetCookies();
    }

    private static Map<String, String> loginAndGetCookies() {
        // Realiza el login con credenciales de prueba
        String username = "javiisalaas97@gmail.com";
        String password = "hola";

        // Realizamos la solicitud POST para hacer login y obtener las cookies
        return given()
                .contentType("application/json")
                .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("https://localhost:443/api/auth/login")
                .then()
                .statusCode(200) // Verificamos que el login haya sido exitoso
                .extract()
                .cookies(); // Extraemos la cookie de sesión
    }

    @Test
    public void testUploadProfileImage_WithAuthToken() throws IOException {

        File testImage = new File("D:/DESCARGAS/miaa.jpg"); // local

        if (!testImage.exists()) {
            // Crear un archivo temporal para simular una imagen
            testImage = File.createTempFile("profile", ".jpg");
            FileUtils.writeByteArrayToFile(testImage, new byte[] { 0, 1, 2, 3 }); // Contenido ficticio
        }

        // Realizar la petición PUT para cargar la imagen, incluyendo las cookies de
        // sesión
        RequestSpecification request = given()
                .cookies(cookies)
                .header("Content-Type", "multipart/form-data")
                .multiPart("file", testImage);
        request.when()
                .put("https://localhost:443/api/users/image")
                .then()
                .log().ifError() // Registra errores si los hay
                .statusCode(201); // Verifica que la respuesta sea 201 Created
    }
}

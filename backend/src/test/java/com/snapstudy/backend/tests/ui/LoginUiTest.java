package com.snapstudy.backend.tests.ui;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginUiTest {

    private WebDriver driver;
    private static String API_LOGIN_URL = "http://localhost:8443/login";
    private static String API_MAIN_URL = "http://localhost:8443/";
    private static String API_ERROR_URL = "http://localhost:8443/error";

    @BeforeAll
    public static void setupDriverManager() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginSuccess() {
        driver.get(API_LOGIN_URL);

        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Sign in!')]"));

        emailField.sendKeys("javiisalaas97@gmail.com");
        passwordField.sendKeys("hola");

        loginButton.click();

        // Wait for the URL to change to the expected one
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe(API_MAIN_URL));

        // Verify that the login has been successful by navigating to the main page
        Assertions.assertEquals(API_MAIN_URL, driver.getCurrentUrl(),
                "The user was correctly redirected.");
    }

    @Test
    public void testLoginFailure() {

        driver.get(API_LOGIN_URL);

        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Sign in!')]"));

        emailField.sendKeys("admin@admin.com");
        passwordField.sendKeys("wrongpassword"); // Incorrect password redirects to /error

        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe(API_ERROR_URL));

        // Verify that the login was not successful and the user was redirected to the
        // error page
        String expectedUrl = API_ERROR_URL;
        Assertions.assertEquals(expectedUrl, driver.getCurrentUrl(),
                "The user was correctly redirected to the error page due to incorrect password.");
    }
}

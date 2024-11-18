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

    @BeforeAll
    public static void setupDriverManager() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
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
        driver.get("http://localhost:4200/login");

        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Sign in!')]"));

        emailField.sendKeys("javiisalaas97@gmail.com");
        passwordField.sendKeys("hola");

        loginButton.click();

        // Wait for the URL to change to the expected one
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/"));

        // Verify that the login has been successful by navigating to the main page
        Assertions.assertEquals("http://localhost:4200/", driver.getCurrentUrl(),
                "The user was correctly redirected.");
    }

    @Test
    public void testLoginFailure() {

        driver.get("http://localhost:4200/login");

        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Sign in!')]"));

        emailField.sendKeys("admin@admin.com");
        passwordField.sendKeys("wrongpassword"); // Incorrect password redirects to /error

        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/error"));

        // Verify that the login was not successful and the user was redirected to the
        // error page
        String expectedUrl = "http://localhost:4200/error";
        Assertions.assertEquals(expectedUrl, driver.getCurrentUrl(),
                "The user was correctly redirected to the error page due to incorrect password.");
    }
}

package com.snapstudy.backend.tests.ui;

import java.time.Duration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginUiTestService {

    public void login(WebDriver driver, String email, String password, String successUrl) {
        try {
            System.out.println("Navigating to the login page...");
            driver.get("https://localhost:8443/login");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            System.out.println("Waiting for email field...");
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));

            System.out.println("Waiting for password field...");
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));

            System.out.println("Waiting for login button...");
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Sign in!')]")));

            System.out.println("Entering email and password...");
            emailField.sendKeys(email);
            passwordField.sendKeys(password);

            System.out.println("Clicking the login button...");
            loginButton.click();

            System.out.println("Waiting for successful redirect...");
            wait.until(ExpectedConditions.urlContains(successUrl));

            System.out.println("Verifying the current URL...");
            Assertions.assertTrue(driver.getCurrentUrl().contains(successUrl),
                    "The user was successfully redirected to the expected URL.");

            System.out.println("Login successful!");
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
            throw new RuntimeException("Login failed", e);
        }
    }
    
}

package com.snapstudy.backend.tests.ui;

import java.time.Duration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginUiTestService {

    public void login(WebDriver driver) {
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
    
}

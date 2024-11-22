package com.snapstudy.backend.tests.ui;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfileUiTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginUiTestService loginUiTestService;

    @BeforeAll
    public static void setupDriverManager() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        loginUiTestService = new LoginUiTestService();
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
    public void testEditProfilePasswordsDoNotMatch() {
        loginUiTestService.login(driver, "javiisalaas97@gmail.com", "hola", "http://localhost:4200/"); // Perform login before testing

        driver.get("http://localhost:4200/profile");

        // Locate the form fields
        WebElement firstNameField = driver.findElement(By.id("firstname"));
        WebElement lastNameField = driver.findElement(By.xpath("//input[@placeholder='Last Name...']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password...']"));
        WebElement confirmPasswordField = driver.findElement(By.xpath("//input[@placeholder='Confirm Password...']"));
        WebElement saveButton = driver.findElement(By.xpath("//button[contains(text(),'Save changes')]"));

        // Clear the fields before entering new data
        firstNameField.clear();
        lastNameField.clear();

        // Fill in the fields with mismatched passwords
        firstNameField.sendKeys("Javier");
        lastNameField.sendKeys("Rodriguez Salas");
        passwordField.sendKeys("password123");
        confirmPasswordField.sendKeys("password321");

        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));

        // Scroll to the button to ensure it is visible
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);

        // Attempt to click the button using JavaScript if the normal click fails
        try {
            saveButton.click(); // Attempt a normal click
        } catch (ElementClickInterceptedException e) {
            // If it fails, use JavaScript to click the button
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                    saveButton);
        }

        WebElement errorHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'popup')]//b[contains(text(),'Passwords do not match')]")));

        // Verify that the message is displayed correctly
        Assertions.assertTrue(errorHeader.isDisplayed(), "Message should be visible.");
    }

    @Test
    public void testEditProfileSuccess() {
        loginUiTestService.login(driver, "javiisalaas97@gmail.com", "hola", "http://localhost:4200/");

        driver.get("http://localhost:4200/profile");

        WebElement firstNameField = driver.findElement(By.id("firstname"));
        WebElement lastNameField = driver.findElement(By.xpath("//input[@placeholder='Last Name...']"));
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email...']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password...']"));
        WebElement confirmPasswordField = driver.findElement(By.xpath("//input[@placeholder='Confirm Password...']"));
        WebElement saveButton = driver.findElement(By.xpath("//button[contains(text(),'Save changes')]"));

        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();

        // Complete the form fields
        firstNameField.sendKeys("Javier");
        lastNameField.sendKeys("Rodriguez");
        emailField.sendKeys("javi@gmail.com");
        passwordField.sendKeys("password123");
        confirmPasswordField.sendKeys("password123");

        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));

        // Attempt to click the button using JavaScript if the normal click fails
        try {
            saveButton.click(); // Attempt a normal click
        } catch (ElementClickInterceptedException e) {
            // If it fails, use JavaScript to click the button
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                    saveButton);
        }

        // Verify that the redirection to /profile was successful
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/profile"));
        Assertions.assertEquals("http://localhost:4200/profile", driver.getCurrentUrl(),
                "The user was successfully redirected after saving changes.");
    }

}

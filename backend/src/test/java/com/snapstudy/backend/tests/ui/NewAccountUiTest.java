package com.snapstudy.backend.tests.ui;

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

import io.github.bonigarcia.wdm.WebDriverManager;

public class NewAccountUiTest {
    private WebDriver driver;
    private static String API_SIGNUP_URL = "http://localhost:4200/signup";
    private static String API_LOGIN_URL = "http://localhost:4200/login";

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
    public void testSignupSuccess() {
        driver.get(API_SIGNUP_URL);

        WebElement firstNameField = driver.findElement(By.xpath("//input[@placeholder='First Name...']"));
        WebElement lastNameField = driver.findElement(By.xpath("//input[@placeholder='Last Name...']"));
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email...']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password...']"));
        WebElement confirmPasswordField = driver.findElement(By.xpath("//input[@placeholder='Confirm Password...']"));
        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(),'Create account!')]"));

        // Fill the fields with valid data
        firstNameField.sendKeys("Pablo");
        lastNameField.sendKeys("Motos");
        emailField.sendKeys("pablomotos@gmail.com");
        passwordField.sendKeys("MotoCross");
        confirmPasswordField.sendKeys("MotoCross");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(signupButton));

        // Scroll to the button to ensure it is visible
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signupButton);

        // Attempt to click the button using JavaScript if the normal click fails
        try {
            signupButton.click(); // Attempt a normal click
        } catch (ElementClickInterceptedException e) {
            // If it fails, use JavaScript to click the button
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                    signupButton);
        }

        // Wait for the page to redirect to the login page
        wait.until(ExpectedConditions.urlToBe(API_LOGIN_URL));

        // Verify that the redirection was successful
        Assertions.assertEquals(API_LOGIN_URL,
                driver.getCurrentUrl(),
                "The user was correctly redirected to the login page after successful account creation.");
    }

    @Test
    public void testSignupWithEmptyFields() {
        driver.get(API_SIGNUP_URL);

        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(),'Create account!')]"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        wait.until(ExpectedConditions.elementToBeClickable(signupButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signupButton);

        try {
            signupButton.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", signupButton);
        }

        WebElement errorHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'popup')]//b[contains(text(),'Some field is incomplete')]")));

        // Verify that the message is displayed correctly
        Assertions.assertTrue(errorHeader.isDisplayed(), "Message should be visible.");
    }

    @Test
    public void testSignupWithNonMatchingPasswords() {
        driver.get(API_SIGNUP_URL);

        WebElement firstNameField = driver.findElement(By.xpath("//input[@placeholder='First Name...']"));
        WebElement lastNameField = driver.findElement(By.xpath("//input[@placeholder='Last Name...']"));
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email...']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password...']"));
        WebElement confirmPasswordField = driver.findElement(By.xpath("//input[@placeholder='Confirm Password...']"));
        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(),'Create account!')]"));

        // Fill the fields with mismatched passwords
        firstNameField.sendKeys("Pablo");
        lastNameField.sendKeys("Motos");
        emailField.sendKeys("pablomotos@gmail.com");
        passwordField.sendKeys("MotoCross");
        confirmPasswordField.sendKeys("FormulaUno"); // Passwords do not match

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(signupButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signupButton);

        try {
            signupButton.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                    signupButton);
        }

        WebElement errorHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'popup')]//b[contains(text(),'Passwords do not match')]")));

        // Verify that the message is displayed correctly
        Assertions.assertTrue(errorHeader.isDisplayed(), "Message should be visible.");
    }

    @Test
    public void testSignupWithExistingEmail() {
        driver.get(API_SIGNUP_URL);

        WebElement firstNameField = driver.findElement(By.xpath("//input[@placeholder='First Name...']"));
        WebElement lastNameField = driver.findElement(By.xpath("//input[@placeholder='Last Name...']"));
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email...']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password...']"));
        WebElement confirmPasswordField = driver.findElement(By.xpath("//input[@placeholder='Confirm Password...']"));
        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(),'Create account!')]"));

        // Fill in with an email that already exists
        firstNameField.sendKeys("Javi");
        lastNameField.sendKeys("Salas");
        emailField.sendKeys("javiisalaas97@gmail.com"); // This email should already exist
        passwordField.sendKeys("Password123");
        confirmPasswordField.sendKeys("Password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(signupButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signupButton);

        try {
            signupButton.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                    signupButton);
        }

        WebElement errorHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'popup')]//b[contains(text(),'The email is already in use')]")));

        Assertions.assertTrue(errorHeader.isDisplayed(), "Message should be visible.");
    }

}

package com.snapstudy.backend.tests.ui;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.Random;

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

public class AddDegreeUiTest {

    private WebDriver driver;
    private LoginUiTestService loginUiTestService;
    private static String API_URL = "https://localhost:8443/admin";

    @BeforeAll
    public static void setupDriverManager() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        loginUiTestService = new LoginUiTestService();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.setAcceptInsecureCerts(true);
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testAddDegreeSuccess() throws InterruptedException {
        loginUiTestService.login(driver, "admin@admin.com", "admin", API_URL); // Perform login
                                                                                                     // before testing

        driver.get(API_URL + "/add/1/null");
        Thread.sleep(1000);

        Random random = new Random();
        String randomNumber = String.valueOf(random.nextInt(100000) + 1);

        // Locate the form fields
        WebElement nameField = driver.findElement(By.xpath("//input[@placeholder='Name...']"));
        WebElement degreeTypeDropdown = driver
                .findElement(By.xpath("//option[contains(text(),'Agricultural and veterinary sciences')]"));
        WebElement createDegreeButton = driver.findElement(By.xpath("//button[contains(text(),'Create degree!')]"));

        // Fill in the fields
        nameField.sendKeys(randomNumber);
        Thread.sleep(1000);
        degreeTypeDropdown.click(); // Select the dropdown option
        createDegreeButton.click();

        // Verify that the redirection to /admin was successful
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe(API_URL));

        Assertions.assertEquals(API_URL, driver.getCurrentUrl(),
                "The user was redirected back after successfully adding a degree.");
    }

    @Test
    public void testAddDegreeFailure() throws InterruptedException {
        loginUiTestService.login(driver, "admin@admin.com", "admin", API_URL); // Perform login
                                                                                                     // before testing

        driver.get(API_URL + "/add/1/null");
        Thread.sleep(1000);
        // Locate the form fields
        WebElement nameField = driver.findElement(By.xpath("//input[@placeholder='Name...']"));
        WebElement degreeTypeDropdown = driver
                .findElement(By.xpath("//option[contains(text(),'Engineering and architecture')]"));
        WebElement createDegreeButton = driver.findElement(By.xpath("//button[contains(text(),'Create degree!')]"));

        // Fill in the fields (degree already exists)
        nameField.sendKeys("Software Engineering");
        Thread.sleep(1000);

        degreeTypeDropdown.click(); // Select the dropdown option

        // Attempt to click the button using JavaScript if the normal click fails
        try {
            createDegreeButton.click(); // Attempt a normal click
        } catch (ElementClickInterceptedException e) {
            // If it fails, use JavaScript to click the button
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                    createDegreeButton);
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'popup')]//b[contains(text(),'This degree')]")));

        // Verify that the message is displayed correctly
        Assertions.assertTrue(errorHeader.isDisplayed(), "Message should be visible.");
    }
}

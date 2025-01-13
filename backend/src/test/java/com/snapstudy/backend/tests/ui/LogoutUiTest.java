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

public class LogoutUiTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginUiTestService loginUiTestService;
    private static String API_PROFILE_URL = "http://localhost:4200/profile";
    private static String API_MAIN_URL = "http://localhost:4200/";

    @BeforeAll
    public static void setupDriverManager() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        loginUiTestService = new LoginUiTestService();
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
    public void testLogout() {
        // Perform login before testing
        loginUiTestService.login(driver, "javiisalaas97@gmail.com", "hola", API_MAIN_URL);

        driver.get(API_PROFILE_URL);

        WebElement logoutButton = driver.findElement(By.xpath("//a[contains(text(), 'Log out!')]"));

        logoutButton.click();

        // Verify that the logout action redirects the user to the main page
        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlToBe(API_MAIN_URL));
        Assertions.assertEquals(API_MAIN_URL, driver.getCurrentUrl(),
                "The user was successfully redirected after logout.");
    }

}

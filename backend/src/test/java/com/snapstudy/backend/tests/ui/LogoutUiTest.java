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
    public void testLogout() {
        // Perform login before testing
        loginUiTestService.login(driver, "javiisalaas97@gmail.com", "hola", "http://localhost:4200/");

        driver.get("http://localhost:4200/profile");

        WebElement logoutButton = driver.findElement(By.xpath("//a[contains(text(), 'Log out!')]"));

        logoutButton.click();

        // Verify that the logout action redirects the user to the main page
        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/"));
        Assertions.assertEquals("http://localhost:4200/", driver.getCurrentUrl(),
                "The user was successfully redirected after logout.");
    }

}

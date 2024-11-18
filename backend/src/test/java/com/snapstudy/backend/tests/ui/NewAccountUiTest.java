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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class NewAccountUiTest {
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
    public void testSignupSuccess() {
        driver.get("http://localhost:4200/signup");

        WebElement firstNameField = driver.findElement(By.xpath("//input[@placeholder='First Name...']"));
        WebElement lastNameField = driver.findElement(By.xpath("//input[@placeholder='Last Name...']"));
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email...']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password...']"));
        WebElement confirmPasswordField = driver.findElement(By.xpath("//input[@placeholder='Confirm Password...']"));
        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(),'Create account!')]"));

        // Llenar los campos con datos válidos
        firstNameField.sendKeys("Pablo");
        lastNameField.sendKeys("Motos");
        emailField.sendKeys("pablomotos@gmail.com");
        passwordField.sendKeys("MotoCross");
        confirmPasswordField.sendKeys("MotoCross");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(signupButton));

        // Hacer scroll hasta el botón para asegurarse de que sea visible
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signupButton);

        // Intentar hacer clic en el botón usando JavaScript si el clic normal falla
        try {
            signupButton.click(); // Intentar clic normal
        } catch (ElementClickInterceptedException e) {
            // Si falla, usar JavaScript para hacer clic en el botón
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                    signupButton);
        }

        // Esperar a que se redirija a la página de login
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/login"));

        // Verificar que se ha redirigido correctamente
        Assertions.assertEquals("http://localhost:4200/login",
                driver.getCurrentUrl(),
                "The user was correctly redirected to the login page after successful account creation.");
    }

    @Test
    public void testSignupWithEmptyFields() {
        driver.get("http://localhost:4200/signup");

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

        // Verificar que el mensaje se muestra correctamente
        Assertions.assertTrue(errorHeader.isDisplayed(), "Message should be visible.");
    }

    @Test
    public void testSignupWithNonMatchingPasswords() {
        driver.get("http://localhost:4200/signup");

        WebElement firstNameField = driver.findElement(By.xpath("//input[@placeholder='First Name...']"));
        WebElement lastNameField = driver.findElement(By.xpath("//input[@placeholder='Last Name...']"));
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email...']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password...']"));
        WebElement confirmPasswordField = driver.findElement(By.xpath("//input[@placeholder='Confirm Password...']"));
        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(),'Create account!')]"));

        // Llenar los campos con contraseñas que no coinciden
        firstNameField.sendKeys("Pablo");
        lastNameField.sendKeys("Motos");
        emailField.sendKeys("pablomotos@gmail.com");
        passwordField.sendKeys("MotoCross");
        confirmPasswordField.sendKeys("FormulaUno"); // Contraseñas no coinciden

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

        // Verificar que el mensaje se muestra correctamente
        Assertions.assertTrue(errorHeader.isDisplayed(), "Message should be visible.");
    }

    @Test
    public void testSignupWithExistingEmail() {
        driver.get("http://localhost:4200/signup");

        WebElement firstNameField = driver.findElement(By.xpath("//input[@placeholder='First Name...']"));
        WebElement lastNameField = driver.findElement(By.xpath("//input[@placeholder='Last Name...']"));
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email...']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password...']"));
        WebElement confirmPasswordField = driver.findElement(By.xpath("//input[@placeholder='Confirm Password...']"));
        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(),'Create account!')]"));

        // Completar con un correo que ya existe
        firstNameField.sendKeys("Javi");
        lastNameField.sendKeys("Salas");
        emailField.sendKeys("javiisalaas97@gmail.com"); // Este correo ya debería existir
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

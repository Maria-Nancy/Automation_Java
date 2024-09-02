package com.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.*;

public class Main {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final int WAIT_TIMEOUT = 10;
    private static final String JUMIA_URL = "https://www.jumia.co.ke";
    private static final String GOOGLE_ACCOUNT_URL = "https://accounts.google.com/signup";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Starting the automated test script...");
        setupWebDriver();

        // Execute test steps and collect results-have modified the test order to demonstrate jumia search, add to card and navigate to cart automation
        String[] results = {
                createGoogleAccount(),
                searchProduct(),
                addToCart(),
                navigateToCart(),
                createJumiaAccount()
        };

        // Send email report with test results
        sendSummaryReport(results);

        // Clean up resources
        driver.quit();
        scanner.close();
        System.out.println("Test script execution completed.");
    }

    // Set up WebDriver for browser automation
    private static void setupWebDriver() {
        System.out.println("Setting up WebDriver...");
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT));
        } catch (Exception e) {
            System.out.println("WebDriver setup failed");
            throw new RuntimeException("WebDriver setup failed", e);
        }
    }

    // Create a Google account
    private static String createGoogleAccount() {
        System.out.println("Starting Google Account creation process...");
        try {
            driver.get(GOOGLE_ACCOUNT_URL);
            fillPersonalInfo();
            Thread.sleep(2000);     // simulates delays
            selectBirthDateAndGender();
            Thread.sleep(2000);
            chooseEmail();
            Thread.sleep(2000);
            setPassword();
            Thread.sleep(2000);
            enterPhoneNumber();
            Thread.sleep(2000);
            enterVerificationCode();
            Thread.sleep(2000);
            System.out.println("Google Account creation process completed successfully.");
            return "Google Account Creation: SUCCESS";
        } catch (Exception e) {
            System.out.println("Google Account creation failed" );
            return "Google Account Creation: FAILED - " + e.getMessage();
        }
    }


    // Fill in personal information for Google account using made up names- Johnkdoe nm
    private static void fillPersonalInfo() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstName"))).sendKeys("Johnkdoe");
            driver.findElement(By.id("lastName")).sendKeys("nm");
            clickNext();
        } catch (Exception e) {
            System.out.println("Failed to fill personal information");
            throw e;
        }
    }

    // inputing random but valid birth date and gender for Google account
    private static void selectBirthDateAndGender() {
        try {
            selectRandomMonth();
            enterRandomDay();
            enterRandomyear();
            selectGender();
            clickNext();
        } catch (Exception e) {
            System.out.println("Failed to select birth date and gender");
            throw e;
        }
    }

    // Selecting a random month for birth date
    private static void selectRandomMonth() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("month"))).click();
            List<WebElement> monthOptions = driver.findElements(By.xpath("//select[@id='month']/option"));
            int randomMonthIndex = new Random().nextInt(12) + 1;
            monthOptions.get(randomMonthIndex).click();
        } catch (Exception e) {
            System.out.println("Failed to select random month: ");
            throw e;
        }
    }
    // Entering a random day for birth date
    private static void enterRandomDay() {
        try {
            WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("day")));
            dayElement.clear();
            dayElement.sendKeys(String.valueOf(new Random().nextInt(1, 28)));
        } catch (Exception e) {
            System.out.println("Failed to enter random day: ");
            throw e;
        }
    }
    // Entering a random year for birth date
    private static void enterRandomyear() {
        try {
            WebElement yearElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("year")));
            yearElement.clear();
            yearElement.sendKeys(String.valueOf(new Random().nextInt(1990, 2000)));
        } catch (Exception e) {
            System.out.println("Failed to enter random year");
            throw e;
        }
    }
    // Selecting gender for Google account
    private static void selectGender() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("gender"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@id='gender']/option[@value='3']"))).click();
        } catch (Exception e) {
            System.out.println("Failed to select gender" );
            throw e;
        }
    }
    // Choosing the email address google suggest for the account
    private static void chooseEmail() {
        try {
            WebElement firstSuggestedEmail = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@role='radio'][1]")));
            firstSuggestedEmail.click();
            clickNext();
        } catch (Exception e) {
            System.out.println("Failed to choose email");
            throw e;
        }
    }

    // Setting up a random password for the  account
    private static void setPassword() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Passwd"))).sendKeys("RandomPassword123!");
            driver.findElement(By.name("PasswdAgain")).sendKeys("RandomPassword123!");
            clickNext();
            System.out.println("Password set successfully.");
        } catch (Exception e) {
            System.out.println("Failed to set password: " + e.getMessage());
            throw e;
        }
    }

    private static volatile String phoneNumber = null;
    // Requiting manual phone number input for account verification
    private static void enterPhoneNumber() {

        try {
            System.out.println("Please enter the phone number for Google verification:");
// Start a separate thread to get user input
            Thread inputThread = new Thread(() -> {
                phoneNumber = scanner.nextLine().trim();
            });
            inputThread.start();

            // Wait for the input thread to finish or timeout after 7 seconds
            inputThread.join(7000);

            // Check if input was provided within the timeout
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                System.out.println("No input received in 7 seconds. Proceeding without input.");
                phoneNumber = ""; // Default action if no input is provided
            }
            WebElement phoneNumberInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phoneNumberId")));
            phoneNumberInput.clear();
            phoneNumberInput.sendKeys(phoneNumber);
            clickNext();
        } catch (Exception e) {
            System.out.println("Failed to enter phone number" );
        }
    }

    private static void clickNext() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'VfPpkd-LgbsSe')]//span[text()='Next']"))).click();
        } catch (Exception e) {
            System.out.println("Failed to click 'Next' button");
            throw e;
        }
    }

    // Requesting verification code for Google account sent to the entred number
    private static void enterVerificationCode() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='tel' and @pattern='[0-9 ]*']")));

            System.out.println("Please enter the verification code you received:");
            final String[] verificationCode = new String[1];
            Thread inputThread = new Thread(() -> verificationCode[0] = scanner.nextLine().trim());

            // Start the input thread
            inputThread.start();

            // Wait for 7 seconds or until the user enters input, whichever comes first
            inputThread.join(7000);

            if (verificationCode[0] == null || verificationCode[0].isEmpty()) {
                System.out.println("No input received in 7 seconds. Proceeding without input.");
                verificationCode[0] = ""; // Default action if no input is provided
            }

            WebElement codeInput = driver.findElement(By.xpath("//input[@type='tel' and @pattern='[0-9 ]*']"));
            codeInput.sendKeys(verificationCode);

            WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class, 'VfPpkd-LgbsSe') and .//span[text()='Verify']]")));
            verifyButton.click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'VfPpkd-P5QLlc')]")));
        } catch (Exception e) {
            System.out.println("Failed to enter or verify verification code");
        }
    }

    // Creating a Jumia account
    private static String createJumiaAccount() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='dpdw-login' and contains(@class, 'trig')]"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/customer/account/index/' and contains(@class, '-df')]"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input_identifierValue"))).sendKeys("your-email@example.com");
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.mdc-button.mdc-button--touch.mdc-button--raised.access-btn"))).click();
            System.out.println("Jumia Account creation process completed successfully.");
            return "Jumia Account Creation: SUCCESS";
        } catch (Exception e) {
            System.out.println("Jumia Account creation failed");
            return "Jumia Account Creation: FAILED - " + e.getMessage();
        }
    }

    // Search for a product on Jumia
    private static String searchProduct() {
        try {
            driver.get(JUMIA_URL);
            wait.until(ExpectedConditions.elementToBeClickable(By.className("cls"))).click();
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
            searchBox.sendKeys("ailyons hd-199a electric dry iron box silver & black (1 yr wrty)");
            searchBox.submit();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("prd")));
            System.out.println("Product search completed successfully.");
            return "Product Search: SUCCESS";
        } catch (Exception e) {
            System.out.println("Product search failed");
            return "Product Search: FAILED - " + e.getMessage();
        }
    }

    // Adding the product to the cart on Jumia
    private static String addToCart() {
        try {
            WebElement itemElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//footer[@class='ft']")));
            Actions actions = new Actions(driver);
            actions.moveToElement(itemElement).perform();
            WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//footer[@class='ft']//button[@class='add btn _prim -pea _md']")));
            addToCartButton.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cart-modal")));
            System.out.println("Product added to cart successfully.");
            return "Add to Cart: SUCCESS";
        } catch (Exception e) {
            System.out.println("Failed to add product to cart" );
            return "Add to Cart: FAILED - " + e.getMessage();
        }
    }

    private static String navigateToCart() {
        try {
            WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/cart/']")));
            cartButton.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cart-overview")));
            System.out.println("Successfully navigated to cart.");
            return "Navigate to Cart: SUCCESS";
        } catch (Exception e) {
            System.out.println("Failed to navigate to cart");
            return "Navigate to Cart: FAILED - " + e.getMessage();
        }
    }
    // Sending a summary report via email
    private static void sendSummaryReport(String[] results) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailConfig.getEmailUsername(), EmailConfig.getEmailPassword());
            }
        });

        try {
            System.out.println("Please enter the recipient email address for the report:");
            String recipientEmail = scanner.nextLine().trim();

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EmailConfig.getEmailUsername()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Automation Test Results");
            message.setText(String.join("\n", results));

            Transport.send(message);
            System.out.println("Summary report sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Failed to send summary report: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
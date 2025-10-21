import org.junit.Test;
import org.junit.Assert;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;

import org.openqa.selenium.By;

public class ThirdTest {
    public static void main(String[] args) {
        ThirdTest test = new ThirdTest();
        test.practiceTest();
    }

    @Test
    public void practiceTest() {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://demoqa.com/automation-practice-form");

        // Locators
        By submitButton = By.id("submit");
        By firstNameField = By.id("firstName");
        By maleGenderRadioLabel = By.xpath("//label[@for='gender-radio-1']");
        By maleGenderRadioButton = By.xpath("//input[@id='gender-radio-1']");
        By sportsCheckboxLabel = By.xpath("//label[@for='hobbies-checkbox-1']");
        By sportsCheckbox = By.xpath("//input[@id='hobbies-checkbox-1']");
        By attachButton = By.id("uploadPicture");
        By stateDropdown = By.id("state");
        By stateOptionNCR = By.xpath("//div[text()='NCR']");

        // Variables
        String filePath = "/Users/calebbyrne/Documents/QA Projects/ehealth_selenium_java_practice/selenium-java/README.md";

        // Test Case 01
        WebElement validateSubmitOnPage = wait.until(ExpectedConditions.visibilityOfElementLocated(submitButton));
        Assert.assertTrue(validateSubmitOnPage.isDisplayed());

        WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        firstName.sendKeys("John");

        Assert.assertTrue(firstName.isDisplayed());
        Assert.assertEquals("John", firstName.getAttribute("value"));

        WebElement gender = wait.until(ExpectedConditions.elementToBeClickable(maleGenderRadioLabel));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", gender);

        WebElement genderButton = wait.until(ExpectedConditions.presenceOfElementLocated(maleGenderRadioButton));
        Assert.assertTrue(genderButton.isSelected());

        WebElement sportsLabel = wait.until(ExpectedConditions.presenceOfElementLocated(sportsCheckboxLabel));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", sportsLabel);

        WebElement sports = wait.until(ExpectedConditions.presenceOfElementLocated(sportsCheckbox));
        Assert.assertTrue(sports.isSelected());

        WebElement attach = wait.until(ExpectedConditions.presenceOfElementLocated(attachButton));
        attach.sendKeys(filePath);

        WebElement scrollElement = wait.until(ExpectedConditions.visibilityOfElementLocated(submitButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView()", scrollElement);

        WebElement state = wait.until(ExpectedConditions.elementToBeClickable(stateDropdown));
        state.click();

        WebElement ncrOption = wait.until(ExpectedConditions.elementToBeClickable(stateOptionNCR));
        ncrOption.click();

        WebElement ncrSelected = wait.until(ExpectedConditions.presenceOfElementLocated(stateOptionNCR));
        Assert.assertTrue(ncrSelected.isDisplayed());

        validateSubmitOnPage.click();

        driver.quit();
    }
}
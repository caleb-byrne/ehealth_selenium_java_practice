import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;

public class DemoQa {
    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor - needs proper syntax
    public DemoQa(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Locators
    private By firstNameField = By.id("firstName");
    private By genderFieldLabel = By.xpath("//label[@for='gender-radio-1']");
    private By genderFieldInput = By.id("gender-radio-1");
    private By hobbiesField = By.id("hobbies-checkbox-1");
    private By uploadPictureField = By.id("uploadPicture");
    private By stateField = By.xpath("//div[contains(text(),'Select State')]");
    private By submitButton = By.id("submit");

    // Methods
    public boolean validateSubmitButtonExists() {
        WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(submitButton));
        return submit.isDisplayed();
    }

    public void enterFirstName(String firstName) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(firstNameField));
        element.sendKeys(firstName);
    }

    public void selectGender() {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(genderFieldLabel));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    public String validateFirstNameIsEntered() {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(firstNameField));
        return element.getAttribute("value");
    }

    public boolean validateMaleGenderIsSelected() {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(genderFieldInput));
        return element.isSelected();
    }
}
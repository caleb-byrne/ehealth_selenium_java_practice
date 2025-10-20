import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SecondTestSimple {
    public static void main(String[] args) {
        SecondTestSimple test = new SecondTestSimple();
        test.simpleSeleniumTest();
    }

    public void simpleSeleniumTest() {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://demoqa.com/automation-practice-form");

        By firstName = By.id("firstName");

        WebElement elmnt = wait.until(ExpectedConditions.presenceOfElementLocated(firstName));
        elmnt.sendKeys("John");

        driver.quit();
    }
}
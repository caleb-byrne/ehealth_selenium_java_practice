import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.Test;
import org.junit.Assert;

public class FirstTest {
    public static void main(String[] args) {
        FirstTest test = new FirstTest();
        test.fillOutForm();
    }

    @Test
    public void fillOutForm() {
        WebDriver driver = new ChromeDriver();
        DemoQa demoQa = new DemoQa(driver);

        driver.get("https://demoqa.com/automation-practice-form");
        Assert.assertTrue(demoQa.validateSubmitButtonExists());
        demoQa.enterFirstName("John");
        Assert.assertEquals("John", demoQa.validateFirstNameIsEntered());
        demoQa.selectGender();
        Assert.assertTrue(demoQa.validateMaleGenderIsSelected());

        driver.quit();
    }

}

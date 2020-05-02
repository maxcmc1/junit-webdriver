import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

import static org.assertj.core.api.Assertions.assertThat;


public class WebDriverTest {

    private WebDriver driver;

    @Before
    public void before() {
        WebDriverManager.chromedriver().setup();
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
        driver = new ChromeDriver();
        driver.get("https://skryabin.com/market/quote.html");
    }

    @Test
    public void verifyTitle() {
        String actualTitle = driver.getTitle();
        assertThat(actualTitle).isEqualTo("Get a Quote");
    }

    @Test
    public void fieldsTest() {
        driver.findElement(By.xpath("//input[@name='email']")).sendKeys("jdoe@example.com");
        String actualValue = driver.findElement(By.xpath("//input[@name='email']")).getAttribute("value");
        assertThat(actualValue).isEqualTo("jdoe@example.com");
    }

    @After
    public void after() {
        driver.quit();
    }


}

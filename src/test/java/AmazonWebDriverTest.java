import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;

public class AmazonWebDriverTest {

    private static WebDriver driver;

    @BeforeClass
    public static void before() {
        WebDriverManager.chromedriver().setup();
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        driver = new ChromeDriver(chromeOptions);
        driver.get("https://www.amazon.com/");
    }

    @Before
    public void beforeScenario() {
        
        driver.manage().deleteAllCookies();
    }

    @Test
    public void fieldsTest() {
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("keyboard");
        driver.findElement(By.id("nav-search-submit-button")).click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("s-refinements"))));

    }

    @AfterClass
    public static void after() {
        driver.quit();
    }
}

//        driver.findElement(By.xpath("//input[@name='email']")).sendKeys("johndoe@example.com");
//        String actualValue = driver.findElement(By.xpath("//input[@name='email']")).getAttribute("value");
//        assertThat(actualValue).isEqualTo("johndoe@example.com");
//        // Username is a required field
//        driver.findElement(By.name("username")).sendKeys("jdoe");
//        String userName = driver.findElement(By.xpath("//input[@name='username']")).getAttribute("value");
//        assertThat(userName).isEqualTo("jdoe");
//        //Password and confirmPassword should match
//       driver.findElement(By.xpath("//input[@name='password']")).sendKeys("welcome");
//        driver.findElement(By.xpath("//input[@name='confirmPassword']")).sendKeys("Welcome"+ Keys.ENTER);
//        String expectedErrorMessage = "Passwords do not match!";
//        String actualErrorMessage =driver.findElement(By.xpath("//label[@id='confirmPassword-error']")).getText();
//        assertThat(expectedErrorMessage).isEqualTo(actualErrorMessage);
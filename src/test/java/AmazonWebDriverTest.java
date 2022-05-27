import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @AfterClass
    public static void after() {
        driver.quit();
    }

    @Before
    public void beforeScenario() {
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void addMultipleItemsIntoCart() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        Actions actions = new Actions(driver);
        WebElement searchInput = driver.findElement(By.xpath("//form[@id='nav-search-bar-form']//input[@type='text']"));
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        //searching with 'keyboard' keyword
        searchInput.sendKeys("keyboard");
        driver.findElement(By.id("nav-search-submit-button")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("s-refinements"))));
        //xpath only for prime items
        List<WebElement> listOfPrimeItems = driver.findElements(By.xpath("//span[contains(@class,'prime')]//ancestor::div[@data-component-type='s-search-result']//div[@class='aok-relative']"));
        List<String> listOfItemTitles = new ArrayList<>();
        //looping: opening the item, saving the item title into the list, adding it to the cart, closing the item tab and going back to the parent tab. Adding to the cart only first three prime items
        for (int i = 0; i <= listOfPrimeItems.size(); i++) {
            // if condition for only first three items
            if (i == 3) {
                break;
            }
            // opening item into separate tab
            WebElement item = listOfPrimeItems.get(i);
            actions.moveToElement(item)
                    .keyDown(Keys.LEFT_CONTROL)
                    .click(item)
                    .keyUp(Keys.LEFT_CONTROL)
                    .build()
                    .perform();
            ArrayList tabs = new ArrayList(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1).toString());
            //get item title into the list
            WebElement itemTitle = driver.findElement(By.xpath("//div[@id='titleSection']//span"));
            wait.until(ExpectedConditions.visibilityOf(itemTitle));
            listOfItemTitles.add(itemTitle.getText());
            // adding to cart
            driver.findElement(By.id("add-to-cart-button")).click();
            WebElement rightSidePanel = driver.findElement(By.id("attach-desktop-sideSheet"));
            wait.until(ExpectedConditions.visibilityOf(rightSidePanel));
            WebElement noThanksButton = driver.findElement(By.id("attachSiNoCoverage"));
            wait.until(ExpectedConditions.elementToBeClickable(noThanksButton));
            noThanksButton.click();
            //verifying 'added to cart'
            WebElement addedToCart = driver.findElement(By.xpath("//div[contains(@id,'added-to-cart-alert')]//span"));
            wait.until(ExpectedConditions.visibilityOf(addedToCart));
            assertThat(addedToCart.getText()).contains("Added to Cart");
            driver.close();
            driver.switchTo().window(tabs.get(0).toString());
        }
        driver.findElement(By.id("nav-cart-count-container")).click();
        WebElement subtotalItems = driver.findElement(By.id("gutterCartViewForm"));
        wait.until(ExpectedConditions.visibilityOf(subtotalItems));
        //verify exact item's number added into te cart
        assertThat(subtotalItems.getText()).contains("3 items");
        // verify exact item's titles
        By cartListOfItemsLocator = By.id("sc-active-cart");
        WebElement cartListOfItems = driver.findElement(cartListOfItemsLocator);
        wait.until(ExpectedConditions.presenceOfElementLocated(cartListOfItemsLocator));
        for (String itemTitle : listOfItemTitles) {
            assertThat(cartListOfItems.getText()).contains(itemTitle);
        }
    }
}
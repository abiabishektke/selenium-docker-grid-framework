package utils;

import java.io.FileInputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BrowserUtil {

    WebDriver driver;
    Properties prop;

    public WebDriver openBrowser() throws Exception {

        prop = new Properties();
        FileInputStream fis =
                new FileInputStream("src/test/java/config/config.properties");
        prop.load(fis);

        String browser = prop.getProperty("browser");
        String url = prop.getProperty("url");
        String execution = prop.getProperty("execution");
        String gridUrl = prop.getProperty("gridUrl");

        // 🔥 LOCAL EXECUTION
        if (execution.equalsIgnoreCase("local")) {

            if (browser.equalsIgnoreCase("chrome")) {
                driver = new ChromeDriver();

            } else if (browser.equalsIgnoreCase("firefox")) {
                driver = new FirefoxDriver();

            } else if (browser.equalsIgnoreCase("edge")) {
                driver = new EdgeDriver();
            }
        }

        // 🔥 REMOTE EXECUTION (DOCKER GRID)
        else if (execution.equalsIgnoreCase("remote")) {

            if (browser.equalsIgnoreCase("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--no-sandbox");
                options.addArguments("--window-size=1920,1080");

                driver = new RemoteWebDriver(new URL(gridUrl), options);

            } else if (browser.equalsIgnoreCase("firefox")) {
                FirefoxOptions options = new FirefoxOptions();

                driver = new RemoteWebDriver(new URL(gridUrl), options);

            } else if (browser.equalsIgnoreCase("edge")) {
                EdgeOptions options = new EdgeOptions();

                driver = new RemoteWebDriver(new URL(gridUrl), options);
            }
        }

        driver.manage().window().maximize();
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        return driver;
    }

    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ✅ FIX: Add this method
    public static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }
}
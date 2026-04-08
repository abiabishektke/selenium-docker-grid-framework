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

    private WebDriver driver;
    private Properties prop;

    public WebDriver openBrowser() {

        try {
            // 🔹 Load config
            prop = new Properties();
            FileInputStream fis = new FileInputStream("src/test/java/config/config.properties");
            prop.load(fis);

            String browser = prop.getProperty("browser").trim();
            String url = prop.getProperty("url").trim();
            String execution = prop.getProperty("execution").trim();
            String gridUrl = prop.getProperty("gridUrl").trim();

            System.out.println("Execution: " + execution);
            System.out.println("Browser: " + browser);
            System.out.println("Grid URL: " + gridUrl);

            // =========================
            // 🔥 LOCAL EXECUTION
            // =========================
            if (execution.equalsIgnoreCase("local")) {

                switch (browser.toLowerCase()) {
                    case "chrome":
                        driver = new ChromeDriver();
                        break;

                    case "firefox":
                        driver = new FirefoxDriver();
                        break;

                    case "edge":
                        driver = new EdgeDriver();
                        break;

                    default:
                        throw new RuntimeException("Invalid browser: " + browser);
                }
            }

            // =========================
            // 🔥 REMOTE EXECUTION (GRID)
            // =========================
            else if (execution.equalsIgnoreCase("remote")) {

                URL grid = new URL(gridUrl);

                switch (browser.toLowerCase()) {
                    case "chrome":
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--window-size=1920,1080");

                        driver = new RemoteWebDriver(grid, chromeOptions);
                        break;

                    case "firefox":
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        driver = new RemoteWebDriver(grid, firefoxOptions);
                        break;

                    case "edge":
                        EdgeOptions edgeOptions = new EdgeOptions();
                        driver = new RemoteWebDriver(grid, edgeOptions);
                        break;

                    default:
                        throw new RuntimeException("Invalid browser for remote: " + browser);
                }
            }

            else {
                throw new RuntimeException("Invalid execution type: " + execution);
            }

            // 🔴 CRITICAL SAFETY CHECK
            if (driver == null) {
                throw new RuntimeException("Driver initialization FAILED");
            }

            // =========================
            // 🔥 COMMON SETUP
            // =========================
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

            driver.get(url);

            return driver;

        } catch (Exception e) {
            throw new RuntimeException("Error while launching browser", e);
        }
    }

    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ✅ Scroll Utility
    public static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }
}
package hooks;

import static com.assertthat.selenium_shutterbug.core.Capture.FULL_SCROLL;

import java.io.File;

import org.openqa.selenium.WebDriver;

import com.assertthat.selenium_shutterbug.core.Shutterbug;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.BrowserUtil;

public class Hooks {

    private static WebDriver driver;
    private BrowserUtil browser;

    @Before
    public void setUp() {
        try {
            browser = new BrowserUtil();
            driver = browser.openBrowser();

            if (driver == null) {
                throw new RuntimeException("Driver is NULL after initialization");
            }

        } catch (Exception e) {
            throw new RuntimeException("Browser setup failed", e);
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    @After
    public void tearDown(Scenario scenario) {

        if (scenario.isFailed() && driver != null) {

            String fileName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
            String screenshotPath = "ExtentReports/Screenshots/";

            new File(screenshotPath).mkdirs();

            Shutterbug.shootPage(driver, FULL_SCROLL)
                      .save(screenshotPath + fileName + ".png");

            try {
                byte[] screenshot = Shutterbug.shootPage(driver).getBytes();
                scenario.attach(screenshot, "image/png", fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 🔴 ALWAYS CLOSE DRIVER SAFELY
        if (driver != null) {
            driver.quit();
        }
    }
}
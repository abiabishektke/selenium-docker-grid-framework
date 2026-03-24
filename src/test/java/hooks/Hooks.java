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

    @Before
    public void setUp() throws Exception {
        BrowserUtil browser = new BrowserUtil();
        driver = browser.openBrowser();
    }

    public static WebDriver getDriver() {
        return driver;
    }

    @After
    public void tearDown(Scenario scenario) {

        if (scenario.isFailed() && driver != null) {

            // Clean file name (VERY IMPORTANT)
            String fileName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");

            // Folder path
            String screenshotPath = "ExtentReports/Screenshots/";

            // Create folder if not exists
            new File(screenshotPath).mkdirs();

            // FINAL LINE (Your Style)
            Shutterbug.shootPage(driver, FULL_SCROLL)
                      .save(screenshotPath + fileName + ".png");

            // OPTIONAL (Attach to Cucumber report)
            try {
                byte[] screenshot = Shutterbug.shootPage(driver).getBytes();
                scenario.attach(screenshot, "image/png", fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (driver != null) {
            driver.quit();
        }
    }
}
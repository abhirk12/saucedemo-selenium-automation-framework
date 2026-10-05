package tests;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.ConfigReader;
import utils.DriverFactory;
import utils.LoggerUtil;
import utils.ScreenshotUtil;

public class BaseTest {

    protected WebDriver driver;
    protected ConfigReader config;

    private static final Logger logger = LoggerUtil.getLogger(BaseTest.class);

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        logger.info("Initializing configuration");
        config = new ConfigReader();
    }

    @Parameters({"browser", "headless"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("") String browserParam, @Optional("") String headlessParam) {
        String browser = (browserParam != null && !browserParam.trim().isEmpty())
                ? browserParam
                : config.getProperty("browser");

        boolean headless = false;
        if (headlessParam != null && !headlessParam.trim().isEmpty()) {
            headless = Boolean.parseBoolean(headlessParam.trim());
        } else {
            String headlessConfig = config.getProperty("headless");
            if (headlessConfig != null && !headlessConfig.trim().isEmpty()) {
                headless = Boolean.parseBoolean(headlessConfig.trim());
            }
        }

        logger.info("Setting up WebDriver for browser: {} (headless: {})", browser, headless);
        driver = DriverFactory.createDriver(browser, headless);
        driver.manage().window().maximize();

        String baseUrl = config.getProperty("baseUrl");
        logger.info("Navigating to Base URL: {}", baseUrl);
        driver.get(baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            logger.warn("Test failed: {}. Capturing screenshot...", testName);
            String path = ScreenshotUtil.takeScreenshot(driver, testName);
            result.setAttribute("screenshotPath", path);
            logger.info("Screenshot saved at: {}", path);
        }

        if (driver != null) {
            logger.info("Closing browser session for test: {}", testName);
            driver.quit();
            DriverFactory.unload();
        }
    }
}
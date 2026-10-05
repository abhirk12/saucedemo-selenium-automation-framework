package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;

public class DriverFactory {

    private static final Logger logger = LoggerUtil.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static WebDriver createDriver(String browser) {
        return createDriver(browser, false);
    }

    public static WebDriver createDriver(String browser, boolean headless) {
        WebDriver driver;
        String browserName = (browser != null) ? browser.toLowerCase().trim() : "chrome";

        logger.info("Initializing WebDriver for browser: {} (Headless: {})", browserName, headless);

        switch (browserName) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                driver = new ChromeDriver(chromeOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        tlDriver.set(driver);
        return driver;
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void unload() {
        tlDriver.remove();
    }
}
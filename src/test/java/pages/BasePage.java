package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import utils.ConfigReader;
import utils.LoggerUtil;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final Logger logger = LoggerUtil.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        ConfigReader config = new ConfigReader();
        int timeout = 10;
        try {
            String timeoutProp = config.getProperty("timeout");
            if (timeoutProp != null && !timeoutProp.trim().isEmpty()) {
                timeout = Integer.parseInt(timeoutProp.trim());
            }
        } catch (Exception e) {
            timeout = 10;
        }

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected void click(By locator) {
        logger.debug("Clicking element: {}", locator);
        waitForClickability(locator).click();
    }

    protected void clickWithJs(By locator) {
        logger.debug("Clicking element via JavaScript: {}", locator);
        WebElement element = waitForVisibility(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void clickWithJs(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void type(By locator, String text) {
        logger.debug("Typing '{}' into element: {}", text, locator);
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        String text = waitForVisibility(locator).getText().trim();
        logger.debug("Retrieved text '{}' from element: {}", text, locator);
        return text;
    }

    protected boolean isDisplayed(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            return !elements.isEmpty() && elements.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void selectByVisibleText(By locator, String text) {
        logger.debug("Selecting option '{}' from dropdown: {}", text, locator);
        Select select = new Select(waitForVisibility(locator));
        select.selectByVisibleText(text);
    }

    protected List<String> getElementTexts(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        List<String> texts = new ArrayList<>();
        for (WebElement el : elements) {
            texts.add(el.getText().trim());
        }
        return texts;
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
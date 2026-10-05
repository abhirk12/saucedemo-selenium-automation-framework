package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class LoginPage extends BasePage {

    private static final Logger logger = LoggerUtil.getLogger(LoginPage.class);

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By loginLogo = By.className("login_logo");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoginPageLoaded() {
        try {
            waitForVisibility(loginButton);
            return isDisplayed(loginButton);
        } catch (Exception e) {
            return false;
        }
    }

    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        logger.info("Entering password");
        type(passwordField, password);
    }

    public void clickLogin() {
        logger.info("Clicking login button");
        click(loginButton);
    }

    public ProductsPage login(String username, String password) {
        logger.info("Logging in with user: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new ProductsPage(driver);
    }

    public String getErrorMessage() {
        String msg = getText(errorMessage);
        logger.info("Error message displayed: {}", msg);
        return msg;
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
}
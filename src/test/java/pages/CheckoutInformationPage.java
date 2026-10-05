package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class CheckoutInformationPage extends BasePage {

    private static final Logger logger = LoggerUtil.getLogger(CheckoutInformationPage.class);

    private final By pageTitle = By.className("title");
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle) && getText(pageTitle).equalsIgnoreCase("Checkout: Your Information");
    }

    public void enterFirstName(String firstName) {
        logger.info("Entering first name: {}", firstName);
        type(firstNameField, firstName);
    }

    public void enterLastName(String lastName) {
        logger.info("Entering last name: {}", lastName);
        type(lastNameField, lastName);
    }

    public void enterPostalCode(String postalCode) {
        logger.info("Entering postal code: {}", postalCode);
        type(postalCodeField, postalCode);
    }

    public void fillInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    public CheckoutOverviewPage clickContinue() {
        logger.info("Clicking Continue on Checkout Information page");
        click(continueButton);
        return new CheckoutOverviewPage(driver);
    }

    public CartPage clickCancel() {
        logger.info("Clicking Cancel on Checkout Information page");
        click(cancelButton);
        return new CartPage(driver);
    }

    public String getErrorMessage() {
        String msg = getText(errorMessage);
        logger.info("Checkout error message: {}", msg);
        return msg;
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
}

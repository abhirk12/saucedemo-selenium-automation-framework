package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class CheckoutCompletePage extends BasePage {

    private static final Logger logger = LoggerUtil.getLogger(CheckoutCompletePage.class);

    private final By pageTitle = By.className("title");
    private final By completeHeader = By.className("complete-header");
    private final By completeText = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle) && getText(pageTitle).equalsIgnoreCase("Checkout: Complete!");
    }

    public String getOrderConfirmationHeader() {
        return getText(completeHeader);
    }

    public String getOrderConfirmationMessage() {
        return getText(completeText);
    }

    public boolean isOrderSuccessful() {
        return getOrderConfirmationHeader().equalsIgnoreCase("Thank you for your order!");
    }

    public ProductsPage clickBackHome() {
        logger.info("Clicking Back Home button");
        click(backHomeButton);
        return new ProductsPage(driver);
    }
}

package pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class CheckoutOverviewPage extends BasePage {

    private static final Logger logger = LoggerUtil.getLogger(CheckoutOverviewPage.class);

    private final By pageTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By cartItemNames = By.className("inventory_item_name");
    private final By itemSubtotal = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle) && getText(pageTitle).equalsIgnoreCase("Checkout: Overview");
    }

    public int getItemCount() {
        List<WebElement> items = driver.findElements(cartItems);
        return items.size();
    }

    public List<String> getItemNames() {
        return getElementTexts(cartItemNames);
    }

    public double getItemSubtotal() {
        String text = getText(itemSubtotal);
        // Format: "Item total: $29.99"
        String amountStr = text.replaceAll("[^0-9.]", "").trim();
        return Double.parseDouble(amountStr);
    }

    public double getTax() {
        String text = getText(taxLabel);
        // Format: "Tax: $2.40"
        String amountStr = text.replaceAll("[^0-9.]", "").trim();
        return Double.parseDouble(amountStr);
    }

    public double getTotal() {
        String text = getText(totalLabel);
        // Format: "Total: $32.39"
        String amountStr = text.replaceAll("[^0-9.]", "").trim();
        return Double.parseDouble(amountStr);
    }

    public CheckoutCompletePage clickFinish() {
        logger.info("Clicking Finish on Checkout Overview page");
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }

    public ProductsPage clickCancel() {
        logger.info("Clicking Cancel on Checkout Overview page");
        click(cancelButton);
        return new ProductsPage(driver);
    }
}

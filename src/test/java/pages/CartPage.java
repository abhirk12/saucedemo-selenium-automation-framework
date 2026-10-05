package pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class CartPage extends BasePage {

    private static final Logger logger = LoggerUtil.getLogger(CartPage.class);

    private final By pageTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By cartItemNames = By.className("inventory_item_name");
    private final By cartItemPrices = By.className("inventory_item_price");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle) && getText(pageTitle).equalsIgnoreCase("Your Cart");
    }

    public int getCartItemCount() {
        List<WebElement> items = driver.findElements(cartItems);
        return items.size();
    }

    public List<String> getCartItemNames() {
        return getElementTexts(cartItemNames);
    }

    public boolean isProductInCart(String productName) {
        List<String> items = getCartItemNames();
        return items.contains(productName);
    }

    public void removeItem(String productName) {
        logger.info("Removing item from cart: {}", productName);
        By removeButton = By.xpath(
                "//div[@class='cart_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]//button[contains(text(),'Remove') or contains(@id,'remove')]"
        );
        click(removeButton);
        By itemRow = By.xpath(
                "//div[@class='cart_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]"
        );
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(itemRow));
    }

    public ProductsPage clickContinueShopping() {
        logger.info("Clicking Continue Shopping");
        click(continueShoppingButton);
        return new ProductsPage(driver);
    }

    public CheckoutInformationPage clickCheckout() {
        logger.info("Clicking Checkout");
        click(checkoutButton);
        return new CheckoutInformationPage(driver);
    }
}

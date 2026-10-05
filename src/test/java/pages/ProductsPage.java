package pages;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class ProductsPage extends BasePage {

    private static final Logger logger = LoggerUtil.getLogger(ProductsPage.class);

    private final By pageTitle = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    private final By inventoryItemNames = By.className("inventory_item_name");
    private final By inventoryItemPrices = By.className("inventory_item_price");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By sortDropdown = By.className("product_sort_container");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle) && getText(pageTitle).equalsIgnoreCase("Products");
    }

    public String getTitleText() {
        return getText(pageTitle);
    }

    public int getProductCount() {
        List<WebElement> items = driver.findElements(inventoryItems);
        return items.size();
    }

    public List<String> getAllProductNames() {
        return getElementTexts(inventoryItemNames);
    }

    public List<Double> getAllProductPrices() {
        List<WebElement> priceElements = driver.findElements(inventoryItemPrices);
        List<Double> prices = new ArrayList<>();
        for (WebElement element : priceElements) {
            String text = element.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(text));
        }
        return prices;
    }

    public void addProductToCart(String productName) {
        logger.info("Adding product to cart: {}", productName);
        By addToCartButton = By.xpath(
                "//div[@class='inventory_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]//button[contains(text(),'Add to cart') or contains(@id,'add-to-cart')]"
        );
        click(addToCartButton);
        By removeButton = By.xpath(
                "//div[@class='inventory_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]//button[contains(text(),'Remove') or contains(@id,'remove')]"
        );
        waitForVisibility(removeButton);
    }

    public void removeProductFromCart(String productName) {
        logger.info("Removing product from cart: {}", productName);
        By removeButton = By.xpath(
                "//div[@class='inventory_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]//button[contains(text(),'Remove') or contains(@id,'remove')]"
        );
        click(removeButton);
        By addToCartButton = By.xpath(
                "//div[@class='inventory_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]//button[contains(text(),'Add to cart') or contains(@id,'add-to-cart')]"
        );
        waitForVisibility(addToCartButton);
    }

    public void addProductByIndex(int index) {
        logger.info("Adding product at index {} to cart", index);
        List<WebElement> buttons = driver.findElements(By.cssSelector(".inventory_item button.btn_inventory"));
        if (index >= 0 && index < buttons.size()) {
            buttons.get(index).click();
        } else {
            throw new IndexOutOfBoundsException("Invalid product index: " + index);
        }
    }

    public int getCartBadgeCount() {
        if (!isDisplayed(cartBadge)) {
            return 0;
        }
        String countText = getText(cartBadge);
        return countText.isEmpty() ? 0 : Integer.parseInt(countText);
    }

    public boolean isCartBadgeDisplayed() {
        return isDisplayed(cartBadge);
    }

    public void selectSortOption(String visibleText) {
        logger.info("Sorting products by: {}", visibleText);
        selectByVisibleText(sortDropdown, visibleText);
    }

    public ProductDetailsPage clickProductByName(String productName) {
        logger.info("Opening product details for: {}", productName);
        By productLink = By.xpath("//div[contains(@class,'inventory_item_name') and text()='" + productName + "']");
        click(productLink);
        return new ProductDetailsPage(driver);
    }

    public CartPage clickCart() {
        logger.info("Navigating to Cart page");
        click(cartLink);
        return new CartPage(driver);
    }

    public LoginPage logout() {
        logger.info("Logging out from application");
        click(menuButton);
        WebElement logout = waitForVisibility(logoutLink);
        clickWithJs(logout);
        return new LoginPage(driver);
    }
}

package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class ProductDetailsPage extends BasePage {

    private static final Logger logger = LoggerUtil.getLogger(ProductDetailsPage.class);

    private final By backButton = By.id("back-to-products");
    private final By productName = By.className("inventory_details_name");
    private final By productDesc = By.className("inventory_details_desc");
    private final By productPrice = By.className("inventory_details_price");
    private final By addToCartButton = By.xpath("//button[contains(text(),'Add to cart') or contains(@id,'add-to-cart')]");
    private final By removeButton = By.xpath("//button[contains(text(),'Remove') or contains(@id,'remove')]");
    private final By cartLink = By.className("shopping_cart_link");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        return isDisplayed(productName) && isDisplayed(backButton);
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductDescription() {
        return getText(productDesc);
    }

    public double getProductPrice() {
        String priceStr = getText(productPrice).replace("$", "").trim();
        return Double.parseDouble(priceStr);
    }

    public void addToCart() {
        logger.info("Adding product from details page to cart");
        click(addToCartButton);
        waitForVisibility(removeButton);
    }

    public void removeFromCart() {
        logger.info("Removing product from details page");
        click(removeButton);
        waitForVisibility(addToCartButton);
    }

    public boolean isRemoveButtonDisplayed() {
        return isDisplayed(removeButton);
    }

    public ProductsPage clickBackToProducts() {
        logger.info("Clicking back to products");
        click(backButton);
        return new ProductsPage(driver);
    }

    public CartPage clickCart() {
        logger.info("Clicking cart from product details");
        click(cartLink);
        return new CartPage(driver);
    }
}

package tests;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutInformationPage;
import pages.LoginPage;
import pages.ProductsPage;

public class CartTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeCartTest() {
        LoginPage loginPage = new LoginPage(driver);
        productsPage = loginPage.login("standard_user", "secret_sauce");
    }

    @Test(
            priority = 1,
            groups = {"smoke", "regression"},
            description = "Verify items added to cart are displayed correctly in the cart view"
    )
    public void testCartDisplaysAddedItems() {
        String item1 = "Sauce Labs Backpack";
        String item2 = "Sauce Labs Bike Light";

        productsPage.addProductToCart(item1);
        productsPage.addProductToCart(item2);

        CartPage cartPage = productsPage.clickCart();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain exactly 2 items");

        List<String> cartItems = cartPage.getCartItemNames();
        Assert.assertTrue(cartItems.contains(item1), "Cart should contain " + item1);
        Assert.assertTrue(cartItems.contains(item2), "Cart should contain " + item2);
    }

    @Test(
            priority = 2,
            groups = {"regression"},
            description = "Verify removing an item directly from the cart page"
    )
    public void testRemoveItemFromCart() {
        String item1 = "Sauce Labs Backpack";
        String item2 = "Sauce Labs Bolt T-Shirt";

        productsPage.addProductToCart(item1);
        productsPage.addProductToCart(item2);

        CartPage cartPage = productsPage.clickCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2);

        cartPage.removeItem(item1);
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart item count should be 1 after removing an item");
        Assert.assertFalse(cartPage.isProductInCart(item1), "Removed item should not be in the cart");
        Assert.assertTrue(cartPage.isProductInCart(item2), "Remaining item should still be in the cart");
    }

    @Test(
            priority = 3,
            groups = {"regression"},
            description = "Verify clicking Continue Shopping navigates back to Products page"
    )
    public void testContinueShoppingNavigatesBack() {
        CartPage cartPage = productsPage.clickCart();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");

        ProductsPage returnedProductsPage = cartPage.clickContinueShopping();
        Assert.assertTrue(returnedProductsPage.isPageLoaded(), "Should return to Products page");
    }

    @Test(
            priority = 4,
            groups = {"smoke", "regression"},
            description = "Verify clicking Checkout button navigates to Checkout Information page"
    )
    public void testNavigateToCheckout() {
        productsPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = productsPage.clickCart();

        CheckoutInformationPage checkoutInfoPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutInfoPage.isPageLoaded(), "Checkout Information page should be loaded");
    }
}

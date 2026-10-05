package tests;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutCompletePage;
import pages.CheckoutInformationPage;
import pages.CheckoutOverviewPage;
import pages.LoginPage;
import pages.ProductsPage;

public class EndToEndCheckoutTest extends BaseTest {

    @Test(
            priority = 1,
            groups = {"smoke", "e2e"},
            description = "Execute full end-to-end e-commerce order workflow from login to order completion"
    )
    public void testCompleteOrderWorkflow() {
        // Step 1: Login
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be displayed");

        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productsPage.isPageLoaded(), "Products page should be loaded");

        // Step 2: Add multiple products to cart
        String product1 = "Sauce Labs Backpack";
        String product2 = "Sauce Labs Fleece Jacket";
        productsPage.addProductToCart(product1);
        productsPage.addProductToCart(product2);

        Assert.assertEquals(productsPage.getCartBadgeCount(), 2, "Cart badge should display 2");

        // Step 3: View Cart & Verify items
        CartPage cartPage = productsPage.clickCart();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain 2 items");

        List<String> cartItems = cartPage.getCartItemNames();
        Assert.assertTrue(cartItems.contains(product1), "Cart should contain " + product1);
        Assert.assertTrue(cartItems.contains(product2), "Cart should contain " + product2);

        // Step 4: Proceed to Checkout Information
        CheckoutInformationPage checkoutInfoPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutInfoPage.isPageLoaded(), "Checkout Information page should be loaded");

        // Step 5: Fill Checkout Information
        checkoutInfoPage.fillInformation("Alex", "Morgan", "10001");
        CheckoutOverviewPage overviewPage = checkoutInfoPage.clickContinue();

        // Step 6: Verify Checkout Overview & Pricing
        Assert.assertTrue(overviewPage.isPageLoaded(), "Checkout Overview page should be loaded");
        Assert.assertEquals(overviewPage.getItemCount(), 2, "Overview should list 2 items");

        double subtotal = overviewPage.getItemSubtotal();
        double tax = overviewPage.getTax();
        double total = overviewPage.getTotal();

        double expectedTotal = Math.round((subtotal + tax) * 100.0) / 100.0;
        Assert.assertEquals(total, expectedTotal, "Total price should equal item subtotal plus tax");

        // Step 7: Finish Checkout and Verify Order Completion
        CheckoutCompletePage completePage = overviewPage.clickFinish();
        Assert.assertTrue(completePage.isPageLoaded(), "Checkout Complete page should be loaded");
        Assert.assertTrue(completePage.isOrderSuccessful(), "Order confirmation message should be displayed");
        Assert.assertTrue(
                completePage.getOrderConfirmationHeader().equalsIgnoreCase("Thank you for your order!"),
                "Confirmation header should say 'Thank you for your order!'"
        );

        // Step 8: Return to Products Page
        ProductsPage returnedProductsPage = completePage.clickBackHome();
        Assert.assertTrue(returnedProductsPage.isPageLoaded(), "Should return to Products page");
        Assert.assertEquals(returnedProductsPage.getCartBadgeCount(), 0, "Cart should be empty after completing order");
    }
}

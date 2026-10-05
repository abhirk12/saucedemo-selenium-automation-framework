package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutInformationPage;
import pages.CheckoutOverviewPage;
import pages.LoginPage;
import pages.ProductsPage;

public class CheckoutTest extends BaseTest {

    private CheckoutInformationPage checkoutInfoPage;

    @BeforeMethod(alwaysRun = true)
    public void prepareCheckoutTest() {
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");
        productsPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = productsPage.clickCart();
        checkoutInfoPage = cartPage.clickCheckout();
    }

    @Test(
            priority = 1,
            groups = {"regression"},
            description = "Verify validation error when submitting empty checkout information"
    )
    public void testCheckoutWithEmptyFieldsValidation() {
        checkoutInfoPage.clickContinue();
        Assert.assertTrue(checkoutInfoPage.isErrorMessageDisplayed(), "Error message should be displayed for empty fields");
        Assert.assertTrue(
                checkoutInfoPage.getErrorMessage().contains("First Name is required"),
                "Error message should require First Name"
        );
    }

    @Test(
            priority = 2,
            groups = {"regression"},
            description = "Verify validation error when Last Name is missing"
    )
    public void testCheckoutMissingLastNameValidation() {
        checkoutInfoPage.enterFirstName("John");
        checkoutInfoPage.clickContinue();

        Assert.assertTrue(checkoutInfoPage.isErrorMessageDisplayed(), "Error message should be displayed for missing last name");
        Assert.assertTrue(
                checkoutInfoPage.getErrorMessage().contains("Last Name is required"),
                "Error message should require Last Name"
        );
    }

    @Test(
            priority = 3,
            groups = {"regression"},
            description = "Verify validation error when Postal Code is missing"
    )
    public void testCheckoutMissingPostalCodeValidation() {
        checkoutInfoPage.enterFirstName("John");
        checkoutInfoPage.enterLastName("Doe");
        checkoutInfoPage.clickContinue();

        Assert.assertTrue(checkoutInfoPage.isErrorMessageDisplayed(), "Error message should be displayed for missing postal code");
        Assert.assertTrue(
                checkoutInfoPage.getErrorMessage().contains("Postal Code is required"),
                "Error message should require Postal Code"
        );
    }

    @Test(
            priority = 4,
            groups = {"regression"},
            description = "Verify cancelling checkout on information step returns to Cart page"
    )
    public void testCancelCheckoutReturnsToCart() {
        CartPage returnedCartPage = checkoutInfoPage.clickCancel();
        Assert.assertTrue(returnedCartPage.isPageLoaded(), "User should be returned to Cart page upon cancelling checkout");
    }

    @Test(
            priority = 5,
            groups = {"smoke", "regression"},
            description = "Verify price calculation in Checkout Overview (Item Total + Tax = Total)"
    )
    public void testCheckoutOverviewPriceCalculation() {
        checkoutInfoPage.fillInformation("Jane", "Smith", "90210");
        CheckoutOverviewPage overviewPage = checkoutInfoPage.clickContinue();

        Assert.assertTrue(overviewPage.isPageLoaded(), "Checkout Overview page should be loaded");

        double itemSubtotal = overviewPage.getItemSubtotal();
        double tax = overviewPage.getTax();
        double total = overviewPage.getTotal();

        double expectedTotal = Math.round((itemSubtotal + tax) * 100.0) / 100.0;
        Assert.assertEquals(total, expectedTotal, "Total should equal Subtotal + Tax");
    }

    @Test(
            priority = 6,
            groups = {"regression"},
            description = "Verify cancelling on Checkout Overview returns to Products page"
    )
    public void testCancelCheckoutOverviewReturnsToProducts() {
        checkoutInfoPage.fillInformation("Jane", "Smith", "90210");
        CheckoutOverviewPage overviewPage = checkoutInfoPage.clickContinue();

        ProductsPage returnedProductsPage = overviewPage.clickCancel();
        Assert.assertTrue(returnedProductsPage.isPageLoaded(), "Cancelling overview should return to Products page");
    }
}

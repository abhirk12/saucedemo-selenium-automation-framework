package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductDetailsPage;
import pages.ProductsPage;

public class ProductsTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeProductsTest() {
        LoginPage loginPage = new LoginPage(driver);
        productsPage = loginPage.login("standard_user", "secret_sauce");
    }

    @Test(
            priority = 1,
            groups = {"smoke", "regression"},
            description = "Verify product catalog displays items with valid names and prices"
    )
    public void testProductListDisplayed() {
        Assert.assertTrue(productsPage.isPageLoaded(), "Products page must be loaded");
        int count = productsPage.getProductCount();
        Assert.assertTrue(count > 0, "Products count should be greater than 0");

        List<String> names = productsPage.getAllProductNames();
        Assert.assertEquals(names.size(), count, "Product name count must match item count");
        Assert.assertTrue(names.contains("Sauce Labs Backpack"), "Product list should contain Sauce Labs Backpack");
    }

    @Test(
            priority = 2,
            groups = {"smoke", "regression"},
            description = "Verify adding a single product updates the shopping cart badge count to 1"
    )
    public void testAddSingleProductToCart() {
        productsPage.addProductToCart("Sauce Labs Backpack");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart badge count should be 1 after adding 1 item");
    }

    @Test(
            priority = 3,
            groups = {"regression"},
            description = "Verify adding multiple products updates the shopping cart badge count accordingly"
    )
    public void testAddMultipleProductsToCart() {
        productsPage.addProductToCart("Sauce Labs Backpack");
        productsPage.addProductToCart("Sauce Labs Bike Light");
        productsPage.addProductToCart("Sauce Labs Bolt T-Shirt");

        Assert.assertEquals(productsPage.getCartBadgeCount(), 3, "Cart badge count should be 3 after adding 3 items");
    }

    @Test(
            priority = 4,
            groups = {"regression"},
            description = "Verify removing a product from the products catalog updates the cart badge"
    )
    public void testRemoveProductFromProductsPage() {
        productsPage.addProductToCart("Sauce Labs Backpack");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1);

        productsPage.removeProductFromCart("Sauce Labs Backpack");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 0, "Cart badge count should be 0 after removing item");
    }

    @Test(
            priority = 5,
            groups = {"regression"},
            description = "Verify sorting products by Name (A to Z)"
    )
    public void testSortProductsByNameAscending() {
        productsPage.selectSortOption("Name (A to Z)");
        List<String> actualNames = productsPage.getAllProductNames();
        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames);

        Assert.assertEquals(actualNames, expectedNames, "Products should be sorted alphabetically from A to Z");
    }

    @Test(
            priority = 6,
            groups = {"regression"},
            description = "Verify sorting products by Name (Z to A)"
    )
    public void testSortProductsByNameDescending() {
        productsPage.selectSortOption("Name (Z to A)");
        List<String> actualNames = productsPage.getAllProductNames();
        List<String> expectedNames = new ArrayList<>(actualNames);
        expectedNames.sort(Collections.reverseOrder());

        Assert.assertEquals(actualNames, expectedNames, "Products should be sorted in reverse alphabetical order");
    }

    @Test(
            priority = 7,
            groups = {"regression"},
            description = "Verify sorting products by Price (low to high)"
    )
    public void testSortProductsByPriceLowToHigh() {
        productsPage.selectSortOption("Price (low to high)");
        List<Double> actualPrices = productsPage.getAllProductPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        Assert.assertEquals(actualPrices, expectedPrices, "Products should be sorted from lowest to highest price");
    }

    @Test(
            priority = 8,
            groups = {"regression"},
            description = "Verify sorting products by Price (high to low)"
    )
    public void testSortProductsByPriceHighToLow() {
        productsPage.selectSortOption("Price (high to low)");
        List<Double> actualPrices = productsPage.getAllProductPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        expectedPrices.sort(Collections.reverseOrder());

        Assert.assertEquals(actualPrices, expectedPrices, "Products should be sorted from highest to lowest price");
    }

    @Test(
            priority = 9,
            groups = {"regression"},
            description = "Verify clicking a product navigates to the Product Details page"
    )
    public void testNavigateToProductDetails() {
        ProductDetailsPage detailsPage = productsPage.clickProductByName("Sauce Labs Fleece Jacket");
        Assert.assertTrue(detailsPage.isPageLoaded(), "Product Details page should be loaded");
        Assert.assertEquals(detailsPage.getProductName(), "Sauce Labs Fleece Jacket");
        Assert.assertTrue(detailsPage.getProductPrice() > 0, "Product price should be greater than 0");

        ProductsPage returnedPage = detailsPage.clickBackToProducts();
        Assert.assertTrue(returnedPage.isPageLoaded(), "Should return to Products page when back button is clicked");
    }
}

package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import utils.CsvReader;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return CsvReader.readLoginData("testdata/loginData.csv");
    }

    @Test(
            priority = 1,
            groups = {"smoke", "regression"},
            dataProvider = "loginData",
            description = "Verify successful login with valid credentials from CSV"
    )
    public void testValidLogin(String username, String password) {
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login(username, password);

        Assert.assertTrue(productsPage.isPageLoaded(), "Products page should be loaded upon valid login");
        Assert.assertEquals(productsPage.getTitleText(), "Products", "Page title text should be 'Products'");
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "URL should contain 'inventory.html'");
    }

    @Test(
            priority = 2,
            groups = {"regression"},
            description = "Verify error message when logging in with invalid password"
    )
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "invalid_password");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid credentials");
        Assert.assertTrue(
                loginPage.getErrorMessage().contains("Username and password do not match"),
                "Error message should mention mismatched credentials"
        );
    }

    @Test(
            priority = 3,
            groups = {"regression"},
            description = "Verify error message when logging in with locked out user"
    )
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("locked_out_user", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for locked out user");
        Assert.assertTrue(
                loginPage.getErrorMessage().contains("Sorry, this user has been locked out"),
                "Error message should indicate user is locked out"
        );
    }

    @Test(
            priority = 4,
            groups = {"regression"},
            description = "Verify validation error when username is empty"
    )
    public void testEmptyUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for missing username");
        Assert.assertTrue(
                loginPage.getErrorMessage().contains("Username is required"),
                "Error message should require username"
        );
    }

    @Test(
            priority = 5,
            groups = {"regression"},
            description = "Verify validation error when password is empty"
    )
    public void testEmptyPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("standard_user");
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for missing password");
        Assert.assertTrue(
                loginPage.getErrorMessage().contains("Password is required"),
                "Error message should require password"
        );
    }

    @Test(
            priority = 6,
            groups = {"smoke", "regression"},
            description = "Verify user can successfully logout and return to login page"
    )
    public void testLogout() {
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productsPage.isPageLoaded(), "Products page should be loaded");

        LoginPage returnedLoginPage = productsPage.logout();
        Assert.assertTrue(returnedLoginPage.isLoginPageLoaded(), "User should be returned to login page upon logout");
    }
}
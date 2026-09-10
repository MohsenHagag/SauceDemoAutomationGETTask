package tests;

import base.BaseTest;
import utils.ConfigReader;
import dataProviders.DataProviders;
import pages.InventoryPage;
import pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;


public class LoginTest extends BaseTest {

    @Test(description = "Login with valid credentials should land the user on the Inventory page")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.loginAs(ConfigReader.validUsername(), ConfigReader.validPassword());

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"),
                "Expected to be redirected to the inventory page after a valid login");
        Assert.assertTrue(inventoryPage.isDisplayed(),
                "Inventory page title 'Products' should be visible after a valid login");
    }

    @Test(description = "Login with invalid/locked-out credentials should show the correct error message",
            dataProvider = "invalidLoginData", dataProviderClass = DataProviders.class)
    public void testInvalidLogin(String username, String password, String expectedErrorMessage) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.attemptLogin(username, password);

        Assert.assertTrue(loginPage.isErrorDisplayed(), "An error message should be displayed for invalid login");
        Assert.assertEquals(loginPage.getErrorMessage(), expectedErrorMessage,
                "Error message text should match the expected validation message");
        Assert.assertFalse(driver.getCurrentUrl().contains("inventory.html"),
                "User must not be navigated to the inventory page on a failed login");
    }
}

package tests;

import base.BaseTest;
import pages.InventoryPage;
import pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;


public class LogoutTest extends BaseTest {

    @Test(description = "Logging out via the application menu should return the user to the Login page")
    public void testLogout() {
        InventoryPage inventoryPage = loginAsStandardUser();
        Assert.assertTrue(inventoryPage.isDisplayed(), "Precondition: user should be on the Inventory page");

        LoginPage loginPage = inventoryPage.logout();

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(),
                "Login button should be visible after logging out");
        Assert.assertFalse(driver.getCurrentUrl().contains("inventory.html"),
                "User should no longer be on the inventory page after logout");
    }
}
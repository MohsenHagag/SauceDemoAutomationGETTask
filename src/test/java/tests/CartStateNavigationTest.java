package tests;

import base.BaseTest;
import utils.ConfigReader;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;


public class CartStateNavigationTest extends BaseTest {

    private static final String PRODUCT_NAME = "Sauce Labs Bike Light";

    @Test(description = "A product added to the cart should remain there while navigating between "
            + "Inventory and Cart pages")
    public void testCartPersistsDuringNavigationWithinSession() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCartByName(PRODUCT_NAME);
        Assert.assertEquals(inventoryPage.getCartItemCount(), 1, "Cart badge should show 1 item after adding a product");

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertTrue(cartPage.getCartItemNames().contains(PRODUCT_NAME),
                "Product should still be in the cart after navigating to the Cart page");

        InventoryPage backToInventory = cartPage.continueShopping();
        Assert.assertEquals(backToInventory.getCartItemCount(), 1,
                "Cart badge should still show 1 item after navigating back to Inventory");

        CartPage cartPageAgain = backToInventory.openCart();
        Assert.assertTrue(cartPageAgain.getCartItemNames().contains(PRODUCT_NAME),
                "Product should remain in the cart across repeated navigation");
    }

    @Test(description = "Documents the application's cart/session behavior across logout and re-login: "
            + "SauceDemo persists the cart in local storage, so a fresh login keeps items added before logout")
    public void testCartStateAfterLogoutAndReLogin() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCartByName(PRODUCT_NAME);
        Assert.assertEquals(inventoryPage.getCartItemCount(), 1,
                "Cart should contain 1 item before logging out");

        LoginPage loginPage = inventoryPage.logout();

        InventoryPage inventoryAfterReLogin = loginPage.loginAs(
                ConfigReader.validUsername(), ConfigReader.validPassword());

        Assert.assertEquals(inventoryAfterReLogin.getCartItemCount(), 1,
                "SauceDemo persists the cart across logout/login: the previously added item should still be there");

        CartPage cartPageAfterReLogin = inventoryAfterReLogin.openCart();
        Assert.assertTrue(cartPageAfterReLogin.getCartItemNames().contains(PRODUCT_NAME),
                "The specific product added before logout should still be present in the cart after re-login");
    }
}
package tests;

import base.BaseTest;
import pages.CartPage;
import pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


public class CartTest extends BaseTest {

    private static final String PRODUCT_ONE = "Sauce Labs Backpack";
    private static final String PRODUCT_TWO = "Sauce Labs Bike Light";

    @Test(description = "Adding two products then removing one should leave only the remaining product in the cart")
    public void testAddAndRemoveProducts() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.addProductToCartByName(PRODUCT_ONE);
        inventoryPage.addProductToCartByName(PRODUCT_TWO);
        Assert.assertEquals(inventoryPage.getCartItemCount(), 2, "Cart badge should show 2 items after adding two products");

        CartPage cartPage = inventoryPage.openCart();
        List<String> itemsInCart = cartPage.getCartItemNames();
        Assert.assertTrue(itemsInCart.contains(PRODUCT_ONE), "Cart should contain product one");
        Assert.assertTrue(itemsInCart.contains(PRODUCT_TWO), "Cart should contain product two");
        Assert.assertEquals(cartPage.getCartItemsCount(), 2, "Cart page should list exactly two items");

        cartPage.removeProductByName(PRODUCT_ONE);

        List<String> remainingItems = cartPage.getCartItemNames();
        Assert.assertEquals(remainingItems.size(), 1, "Only one product should remain in the cart");
        Assert.assertEquals(remainingItems.get(0), PRODUCT_TWO,
                "The remaining product should be the one that was not removed");
        Assert.assertEquals(cartPage.getCartItemsCount(), 1, "Cart page item count should reflect the removal");
    }
}
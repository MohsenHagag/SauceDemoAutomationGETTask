package tests;

import base.BaseTest;
import pages.InventoryPage;
import pages.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


public class ProductDetailsTest extends BaseTest {

    @Test(description = "Opening a product from the inventory should show matching name/price and a description, "
            + "and the user should be able to return to the inventory page")
    public void testProductDetailsMatchInventoryAndBackNavigation() {
        InventoryPage inventoryPage = loginAsStandardUser();

        List<String> namesOnInventory = inventoryPage.getProductNames();
        List<Double> pricesOnInventory = inventoryPage.getProductPrices();
        Assert.assertFalse(namesOnInventory.isEmpty(), "Inventory should have at least one product to open");

        String expectedName = namesOnInventory.get(0);
        double expectedPrice = pricesOnInventory.get(0);

        ProductDetailsPage detailsPage = inventoryPage.openProductByName(expectedName);

        Assert.assertEquals(detailsPage.getProductName(), expectedName,
                "Product name on details page should match the inventory page");
        Assert.assertEquals(detailsPage.getProductPrice(), expectedPrice, 0.01,
                "Product price on details page should match the inventory page");
        Assert.assertFalse(detailsPage.getProductDescription().isBlank(),
                "Product description should not be empty");

        InventoryPage backOnInventory = detailsPage.backToProducts();
        Assert.assertTrue(backOnInventory.isDisplayed(),
                "User should be back on the Inventory page after clicking Back to products");
    }
}
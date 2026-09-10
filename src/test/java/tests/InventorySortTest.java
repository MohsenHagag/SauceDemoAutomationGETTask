package tests;

import base.BaseTest;
import pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class InventorySortTest extends BaseTest {

    @Test(description = "Inventory page should list products with both names and prices")
    public void testProductsDisplayedWithNamesAndPrices() {
        InventoryPage inventoryPage = loginAsStandardUser();

        List<String> names = inventoryPage.getProductNames();
        List<Double> prices = inventoryPage.getProductPrices();

        Assert.assertFalse(names.isEmpty(), "Inventory page should display at least one product");
        Assert.assertEquals(prices.size(), names.size(),
                "Every product should have a corresponding price");
        prices.forEach(price -> Assert.assertTrue(price > 0, "Product price should be a positive number"));
    }

    @Test(description = "Sorting by Name (A to Z) should display products in alphabetical order")
    public void testSortByNameAToZ() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.sortBy("Name (A to Z)");
        List<String> actualOrder = inventoryPage.getProductNames();

        List<String> expectedOrder = new ArrayList<>(actualOrder);
        Collections.sort(expectedOrder);

        Assert.assertEquals(actualOrder, expectedOrder, "Products should be sorted alphabetically A to Z");
    }

    @Test(description = "Sorting by Price (low to high) should display products in ascending price order")
    public void testSortByPriceLowToHigh() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.sortBy("Price (low to high)");
        List<Double> actualOrder = inventoryPage.getProductPrices();

        List<Double> expectedOrder = new ArrayList<>(actualOrder);
        Collections.sort(expectedOrder);

        Assert.assertEquals(actualOrder, expectedOrder, "Products should be sorted by ascending price");
    }

    @Test(description = "Sorting by Price (high to low) should display products in descending price order")
    public void testSortByPriceHighToLow() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.sortBy("Price (high to low)");
        List<Double> actualOrder = inventoryPage.getProductPrices();

        List<Double> expectedOrder = new ArrayList<>(actualOrder);
        expectedOrder.sort(Collections.reverseOrder());

        Assert.assertEquals(actualOrder, expectedOrder, "Products should be sorted by descending price");
    }
}

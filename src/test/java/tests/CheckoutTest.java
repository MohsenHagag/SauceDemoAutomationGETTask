package tests;

import base.BaseTest;
import dataProviders.DataProviders;
import pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


public class CheckoutTest extends BaseTest {

    private static final String PRODUCT_NAME = "Sauce Labs Backpack";

    @Test(description = "Completing checkout with valid customer info should show correct subtotal/tax/total "
            + "and a final order confirmation", dataProvider = "checkoutCustomers", dataProviderClass = DataProviders.class)
    public void testEndToEndCheckout(String firstName, String lastName, String postalCode) {
        InventoryPage inventoryPage = loginAsStandardUser();

        double productPrice = inventoryPage.getProductPrices().get(
                inventoryPage.getProductNames().indexOf(PRODUCT_NAME));
        inventoryPage.addProductToCartByName(PRODUCT_NAME);

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertTrue(cartPage.getCartItemNames().contains(PRODUCT_NAME),
                "Cart should contain the added product before checkout");

        CheckoutStepOnePage stepOnePage = cartPage.clickCheckout();
        stepOnePage.fillCustomerInformation(firstName, lastName, postalCode);
        CheckoutStepTwoPage stepTwoPage = stepOnePage.clickContinue();

        List<String> overviewItems = stepTwoPage.getItemNames();
        Assert.assertEquals(overviewItems.size(), 1, "Overview page should list exactly one product");
        Assert.assertEquals(overviewItems.get(0), PRODUCT_NAME,
                "Overview page should show the product that was added to the cart");

        double subtotal = stepTwoPage.getSubtotal();
        double tax = stepTwoPage.getTax();
        double total = stepTwoPage.getTotal();

        Assert.assertEquals(subtotal, productPrice, 0.01,
                "Subtotal should match the price of the single product added to the cart");
        Assert.assertEquals(total, round2(subtotal + tax), 0.01,
                "Total should equal subtotal plus tax");

        CheckoutCompletePage completePage = stepTwoPage.clickFinish();

        Assert.assertEquals(completePage.getConfirmationHeader(), "Thank you for your order!",
                "Order confirmation header should be displayed after clicking Finish");
        Assert.assertFalse(completePage.getConfirmationText().isBlank(),
                "Order confirmation text should not be empty");
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
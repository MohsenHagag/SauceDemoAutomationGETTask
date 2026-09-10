package pages;

import pages.common.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartItems = By.className("cart_item");
    private final By cartItemNames = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return driver.getCurrentUrl().contains("cart.html");
    }

    public List<String> getCartItemNames() {
        return getTextOfAll(cartItemNames);
    }

    public int getCartItemsCount() {
        return findAllQuietly(cartItems).size();
    }

    public void removeProductByName(String productName) {
        WebElement item = findAllQuietly(cartItems).stream()
                .filter(i -> i.findElement(cartItemNames).getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new InventoryPage.NoSuchElementFoundException(productName));
        item.findElement(By.xpath(".//button[contains(text(),'Remove')]")).click();
    }

    private List<WebElement> findAllQuietly(By locator) {
        return driver.findElements(locator);
    }

    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage(driver);
    }

    public CheckoutStepOnePage clickCheckout() {
        click(checkoutButton);
        return new CheckoutStepOnePage(driver);
    }
}

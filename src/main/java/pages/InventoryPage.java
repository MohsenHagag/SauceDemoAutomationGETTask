package pages;

import utils.WaitUtils;
import pages.common.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By sortDropdown = By.className("product_sort_container");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By burgerMenuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        WaitUtils.waitForVisible(driver, pageTitle);
        return isDisplayed(pageTitle) && "PRODUCTS".equalsIgnoreCase(getText(pageTitle));
    }

    public List<String> getProductNames() {
        return getTextOfAll(itemNames);
    }

    /** Prices as doubles, e.g. "$29.99" -> 29.99, in the order shown on the page. */
    public List<Double> getProductPrices() {
        return getTextOfAll(itemPrices).stream()
                .map(price -> Double.parseDouble(price.replace("$", "").trim()))
                .collect(Collectors.toList());
    }

    public void sortBy(String visibleOptionText) {
        selectByVisibleText(sortDropdown, visibleOptionText);
    }

    public ProductDetailsPage openProductByName(String productName) {
        WebElement item = findItemContainer(productName);
        item.findElement(itemNames).click();
        return new ProductDetailsPage(driver);
    }

    public void addProductToCartByName(String productName) {
        WebElement item = findItemContainer(productName);
        item.findElement(By.xpath(".//button[contains(text(),'Add to cart')]")).click();
    }

    public void removeProductFromCartByName(String productName) {
        WebElement item = findItemContainer(productName);
        item.findElement(By.xpath(".//button[contains(text(),'Remove')]")).click();
    }

    private WebElement findItemContainer(String productName) {
        return findAll(inventoryItems).stream()
                .filter(item -> item.findElement(itemNames).getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementFoundException(productName));
    }

    public int getCartItemCount() {
        if (!isDisplayed(cartBadge)) {
            return 0;
        }
        return Integer.parseInt(getText(cartBadge).trim());
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public void openMenu() {
        click(burgerMenuButton);
        WaitUtils.waitForClickable(driver, logoutLink);
    }

    public LoginPage logout() {
        openMenu();
        click(logoutLink);
        return new LoginPage(driver);
    }

    /** Thrown when a product name used in a test cannot be located on the inventory page. */
    public static class NoSuchElementFoundException extends RuntimeException {
        public NoSuchElementFoundException(String productName) {
            super("Product not found on inventory page: " + productName);
        }
    }
}
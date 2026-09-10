package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.common.BasePage;

public class ProductDetailsPage extends BasePage {

    private final By productName = By.className("inventory_details_name");
    private final By productPrice = By.className("inventory_details_price");
    private final By productDescription = By.className("inventory_details_desc");
    private final By backButton = By.id("back-to-products");
    private final By addToCartButton = By.xpath("//button[contains(text(),'Add to cart')]");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return getText(productName);
    }

    public double getProductPrice() {
        return Double.parseDouble(getText(productPrice).replace("$", "").trim());
    }

    public String getProductDescription() {
        return getText(productDescription);
    }

    public void addToCart() {
        click(addToCartButton);
    }

    public InventoryPage backToProducts() {
        click(backButton);
        return new InventoryPage(driver);
    }
}

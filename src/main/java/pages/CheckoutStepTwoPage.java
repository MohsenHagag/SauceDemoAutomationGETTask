package pages;

import pages.common.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CheckoutStepTwoPage extends BasePage {

    private final By cartItemNames = By.className("inventory_item_name");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getItemNames() {
        return getTextOfAll(cartItemNames);
    }

    public double getSubtotal() {
        return extractAmount(getText(subtotalLabel));
    }


    public double getTax() {
        return extractAmount(getText(taxLabel));
    }

    public double getTotal() {
        return extractAmount(getText(totalLabel));
    }

    private double extractAmount(String labelText) {
        String amount = labelText.substring(labelText.indexOf('$') + 1).trim();
        return Double.parseDouble(amount);
    }

    public CheckoutCompletePage clickFinish() {
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }

    public CartPage clickCancel() {
        click(cancelButton);
        return new CartPage(driver);
    }
}

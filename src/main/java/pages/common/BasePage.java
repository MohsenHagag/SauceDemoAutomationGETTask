package pages.common;

import utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected void click(By locator) {
        WaitUtils.waitForClickable(driver, locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return WaitUtils.waitForVisible(driver, locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    protected List<WebElement> findAll(By locator) {
        WaitUtils.waitForVisible(driver, locator);
        return driver.findElements(locator);
    }

    protected List<String> getTextOfAll(By locator) {
        return findAll(locator).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    protected void selectByVisibleText(By locator, String visibleText) {
        WebElement dropdown = WaitUtils.waitForVisible(driver, locator);
        new Select(dropdown).selectByVisibleText(visibleText);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitleText(By titleLocator) {
        return getText(titleLocator);
    }



}
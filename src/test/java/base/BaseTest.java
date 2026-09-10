package base;

import utils.ConfigReader;
import driver.DriverFactory;
import pages.InventoryPage;
import pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import listeners.TestListener;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public abstract class BaseTest{

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver();
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.baseUrl());


    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected InventoryPage loginAsStandardUser() {
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.loginAs(ConfigReader.validUsername(), ConfigReader.validPassword());
    }

    protected InventoryPage loginAs(String username, String password) {
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.loginAs(username, password);
    }
}

package listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import driver.DriverFactory;
import utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription() != null
                ? result.getMethod().getDescription() : name;
        ExtentReportManager.startTest(name, description);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        WebDriver driver = safeGetDriver();

        String screenshotPath = ScreenshotUtils.captureScreenshot(driver, testName);
        String base64Screenshot = ScreenshotUtils.captureScreenshotBase64(driver);

        ExtentReportManager.getTest().log(Status.FAIL, "Test failed: " + result.getThrowable());
        if (screenshotPath != null) {
            ExtentReportManager.getTest().log(Status.INFO, "Screenshot saved at: " + screenshotPath);
        }
        if (base64Screenshot != null) {
            ExtentReportManager.getTest().fail(
                    "Screenshot on failure",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        }
        System.err.println("[FAILURE] " + testName + " -> " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "Test skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flush();
    }

    private WebDriver safeGetDriver() {
        try {
            return DriverFactory.getDriver();
        } catch (IllegalStateException e) {
            return null;
        }
    }
}
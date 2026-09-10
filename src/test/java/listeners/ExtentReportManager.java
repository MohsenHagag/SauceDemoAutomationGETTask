package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;


public final class ExtentReportManager {

    private static ExtentReports extentReports;
    private static final Map<Long, ExtentTest> TEST_MAP = new ConcurrentHashMap<>();
    private static String reportPath;

    private ExtentReportManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            reportPath = "test-output" + java.io.File.separator + "ExtentReport_" + timestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("SauceDemo Automation Report");
            sparkReporter.config().setReportName("Mohsen Haggag - Test Execution Report");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Application", "https://www.saucedemo.com/");
            extentReports.setSystemInfo("Framework", "Selenium 4 + TestNG");
        }
        return extentReports;
    }

    public static void startTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        TEST_MAP.put(Thread.currentThread().getId(), test);
    }

    public static ExtentTest getTest() {
        return TEST_MAP.get(Thread.currentThread().getId());
    }

    public static void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    public static String getReportPath() {
        return reportPath;
    }
}

package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class ScreenshotUtils {

    private static final Path SCREENSHOT_DIR = Paths.get("test-output", "screenshots");

    private ScreenshotUtils() {
    }


    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            return null;
        }
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS"));
            String fileName = testName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";
            Path destination = SCREENSHOT_DIR.resolve(fileName);

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), destination);
            return destination.toAbsolutePath().toString();
        } catch (IOException | ClassCastException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    public static String captureScreenshotBase64(WebDriver driver) {
        if (driver == null) {
            return null;
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (ClassCastException e) {
            return null;
        }
    }
}

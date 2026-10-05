package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    public static String takeScreenshot(WebDriver driver, String testName) {

        String folderPath = "screenshots/";

        Path folder = Paths.get(folderPath);

        try {
            Files.createDirectories(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File source =
                ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);

        String filePath =
                folderPath + testName + "_" + System.currentTimeMillis() + ".png";

        try {
            Files.copy(source.toPath(), Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }
}
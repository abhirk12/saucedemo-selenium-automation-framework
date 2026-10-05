package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

public class ExtentManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");
            spark.config().setDocumentTitle("SauceDemo Test Automation Report");
            spark.config().setReportName("SauceDemo Functional & Regression Test Results");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Application", "SauceDemo E-Commerce");
            extent.setSystemInfo("Environment", "QA / Stage");
            extent.setSystemInfo("Framework", "Selenium POM with TestNG");
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
        }
        return extent;
    }

    public static ExtentTest getTest() {
        return testNode.get();
    }

    public static void setTest(ExtentTest test) {
        testNode.set(test);
    }

    public static void unloadTest() {
        testNode.remove();
    }
}
package listeners;

import java.util.Arrays;

import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import utils.ExtentManager;
import utils.LoggerUtil;

public class TestListener implements ITestListener {

    private static final Logger logger = LoggerUtil.getLogger(TestListener.class);
    private static final ExtentReports extent = ExtentManager.getInstance();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String[] groups = result.getMethod().getGroups();

        ExtentTest extentTest = extent.createTest(
                testName,
                (description != null && !description.isEmpty()) ? description : testName
        );

        if (groups != null && groups.length > 0) {
            extentTest.assignCategory(groups);
        }

        extentTest.info("Test execution started: " + testName);
        if (result.getParameters().length > 0) {
            extentTest.info("Test parameters: " + Arrays.toString(result.getParameters()));
        }

        ExtentManager.setTest(extentTest);
        logger.info(">>> Test Started: {} <<<", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "Test passed successfully");
        }
        logger.info("Test PASSED: {}", result.getMethod().getMethodName());
        ExtentManager.unloadTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        String testName = result.getMethod().getMethodName();

        if (test != null) {
            test.log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
            test.fail(result.getThrowable());

            String screenshotPath = (String) result.getAttribute("screenshotPath");
            if (screenshotPath != null) {
                try {
                    test.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
                } catch (Exception e) {
                    test.warning("Unable to attach screenshot: " + e.getMessage());
                }
            }
        }
        logger.error("Test FAILED: {} - Exception: {}", testName, result.getThrowable().getMessage());
        ExtentManager.unloadTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + (result.getThrowable() != null ? result.getThrowable().getMessage() : "No reason provided"));
        }
        logger.warn("Test SKIPPED: {}", result.getMethod().getMethodName());
        ExtentManager.unloadTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        logger.info("Extent report flushed to reports/ExtentReport.html");
    }
}
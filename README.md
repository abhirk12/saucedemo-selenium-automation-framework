# SauceDemo E-Commerce Selenium Automation Framework

A production-grade Web Automation Testing Framework designed and developed using **Java 17**, **Selenium WebDriver 4**, **TestNG**, **Maven**, and the **Page Object Model (POM)** pattern.

Automates critical e-commerce workflows on [SauceDemo](https://www.saucedemo.com/) including authentication, product catalog browsing, cart operations, multi-step checkout, pricing computations, error validations, and order fulfillment.

---

## 🌟 Key Features

* **Page Object Model (POM)**: Strict separation of UI element locators and page actions from test assertions.
* **Thread-Safe Architecture**: `ThreadLocal<WebDriver>` and thread-safe `ExtentTest` management enabling parallel and cross-browser test execution.
* **Synchronization Strategy**: Selenium explicit waits (`WebDriverWait` & `ExpectedConditions`) to eliminate flaky tests without hardcoded `Thread.sleep()`.
* **Data-Driven Testing (DDT)**: Dynamic test data parameterized via TestNG `@DataProvider`, CSV readers (`CsvReader`), and Excel readers (`ExcelReader` with Apache POI).
* **Rich ExtentReports 5 Reporting**: Real-time HTML test report generation with execution timeline, system attributes, test categorization, and embedded failure screenshots.
* **Automated Screenshot Capture**: Custom TestNG listener capturing full-page screenshots automatically upon test failure.
* **Comprehensive Logging**: SLF4J with Logback routing formatted logs simultaneously to console and rolling daily log files (`logs/automation.log`).
* **Cross-Browser & Headless Support**: Configurable execution for Google Chrome, Microsoft Edge, and Mozilla Firefox in normal or headless mode.
* **CI/CD Ready**: Configured for automated Jenkins pipeline builds and headless Docker/Selenium Grid environments.

---

## 🏗️ Framework Architecture

```
                                  +-------------------+
                                  |    TestNG Suites  |
                                  |  (testng*.xml)    |
                                  +---------+---------+
                                            |
                                  +---------v---------+
                                  |    Test Classes   |
                                  |  (Login, Products,|
                                  |   Cart, Checkout) |
                                  +----+---------+----+
                                       |         |
                  +--------------------+         +--------------------+
                  |                                                   |
        +---------v---------+                               +---------v---------+
        |    Page Objects   |                               |  Utilities & Data |
        | (LoginPage, etc.) |                               |  (Config, CSV,    |
        +---------+---------+                               |   POI, Extent)    |
                  |                                         +-------------------+
        +---------v---------+
        |     BasePage      |
        |  (Explicit Waits) |
        +---------+---------+
                  |
        +---------v---------+
        | Selenium WebDriver|
        +---------+---------+
                  |
        +---------v---------+
        | SauceDemo Web App |
        +-------------------+
```

---

## 📁 Project Directory Structure

```text
saucedemo-selenium-automation-framework
│
├── docs
│   └── PROJECT_DOCUMENTATION.md
│
├── logs
│   └── automation.log
│
├── reports
│   └── ExtentReport.html
│
├── screenshots
│   └── *.png
│
├── src
│   └── test
│       ├── java
│       │   ├── listeners
│       │   │   └── TestListener.java
│       │   ├── pages
│       │   │   ├── BasePage.java
│       │   │   ├── LoginPage.java
│       │   │   ├── ProductsPage.java
│       │   │   ├── ProductDetailsPage.java
│       │   │   ├── CartPage.java
│       │   │   ├── CheckoutInformationPage.java
│       │   │   ├── CheckoutOverviewPage.java
│       │   │   └── CheckoutCompletePage.java
│       │   ├── tests
│       │   │   ├── BaseTest.java
│       │   │   ├── LoginTest.java
│       │   │   ├── ProductsTest.java
│       │   │   ├── CartTest.java
│       │   │   ├── CheckoutTest.java
│       │   │   └── EndToEndCheckoutTest.java
│       │   └── utils
│       │       ├── ConfigReader.java
│       │       ├── CsvReader.java
│       │       ├── DriverFactory.java
│       │       ├── ExcelReader.java
│       │       ├── ExtentManager.java
│       │       ├── LoggerUtil.java
│       │       └── ScreenshotUtil.java
│       │
│       └── resources
│           ├── config
│           │   └── config.properties
│           ├── testdata
│           │   ├── loginData.csv
│           │   └── checkoutData.csv
│           └── logback.xml
│
├── pom.xml
├── testng.xml
├── testng-smoke.xml
├── testng-regression.xml
├── testng-crossbrowser.xml
└── README.md
```

---

## 💻 Tech Stack & Dependencies

| Component | Library / Tool | Version | Purpose |
| :--- | :--- | :--- | :--- |
| Language | Java JDK | 17 | Core programming language |
| Automation Core | Selenium Java | 4.35.0 | Browser automation engine |
| Test Runner | TestNG | 7.11.0 | Test structure, annotations, groupings |
| Build Tool | Apache Maven | 3.9+ | Build and dependency lifecycle |
| Reporting | ExtentReports | 5.1.2 | Rich interactive HTML reports |
| Logging | SLF4J + Logback | 2.0.17 / 1.5.18 | Structured application logging |
| Data Management | Apache POI OOXML | 5.4.1 | Reading external Excel test data |
| Test Execution | Maven Surefire Plugin | 3.5.3 | CLI test execution integration |

---

## 🚀 Getting Started & Test Execution

### Prerequisites
* **Java Development Kit (JDK) 17+** installed and set in `JAVA_HOME`.
* **Apache Maven 3.8+** installed and set in `PATH`.
* Google Chrome, Microsoft Edge, or Mozilla Firefox browser installed.

### 1. Run All Tests
```bash
mvn clean test
```

### 2. Run Smoke Test Suite
```bash
mvn clean test -Dsurefire.suiteXmlFiles=testng-smoke.xml
```

### 3. Run Regression Test Suite
```bash
mvn clean test -Dsurefire.suiteXmlFiles=testng-regression.xml
```

### 4. Run Cross-Browser Suite (Parallel Execution)
```bash
mvn clean test -Dsurefire.suiteXmlFiles=testng-crossbrowser.xml
```

### 5. Run with Custom Browser / Headless Mode
Edit `src/test/resources/config/config.properties`:
```properties
baseUrl=https://www.saucedemo.com/
browser=chrome
headless=false
timeout=10
```
Or pass properties dynamically via Maven CLI:
```bash
mvn clean test -Dbrowser=edge -Dheadless=true
```

---

## 📊 Reports & Artifacts

After test execution finishes:
* **ExtentReports HTML Report**: Open `reports/ExtentReport.html` in any web browser.
* **Failure Screenshots**: Generated under `screenshots/<testName>_<timestamp>.png` and linked directly inside the Extent report.
* **Logs**: Check `logs/automation.log` for execution traces and debugging statements.

---

## 🔄 CI/CD Jenkins Pipeline Integration

In Jenkins, create a Maven project or Pipeline job executing:
```groovy
pipeline {
    agent any
    tools {
        maven 'Maven 3.9.x'
        jdk 'JDK 17'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Execute Tests') {
            steps {
                sh 'mvn clean test -Dheadless=true'
            }
        }
    }
    post {
        always {
            publishHTML(target: [
                reportName: 'Extent Test Report',
                reportDir: 'reports',
                reportFiles: 'ExtentReport.html',
                keepAll: true,
                alwaysLinkToLastBuild: true
            ])
            archiveArtifacts artifacts: 'screenshots/*.png, logs/*.log', allowEmptyArchive: true
        }
    }
}
```

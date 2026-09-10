# SauceDemo Selenium Automation Framework

Selenium + TestNG automation framework built for the **GET Group – Software
Quality Control Engineer** technical assessment. It automates the end-to-end
workflows of [saucedemo.com](https://www.saucedemo.com/) using the Page
Object Model, and produces an HTML execution report with screenshots on
failure.

**Author:** [Mohsen Haggag](https://github.com/MohsenHagag)

## Tech stack

| Concern            | Choice                                   |
|---------------------|-------------------------------------------|
| Language / build    | Java 17, Maven                            |
| Browser automation  | Selenium 4                                |
| Driver management   | WebDriverManager (auto-downloads drivers) |
| Test framework      | TestNG                                    |
| Reporting           | ExtentReports (HTML)                      |
| Design pattern      | Page Object Model + reusable base classes |

## Project structure

```
src/main/java/
  driver/DriverFactory.java       -> thread-safe driver factory (chrome/firefox/edge, headless)
  pages/common/BasePage.java      -> shared, reusable Selenium actions for all pages
  pages/                          -> Page Objects (LoginPage, InventoryPage, CartPage,
                                      CheckoutStepOnePage, CheckoutStepTwoPage,
                                      CheckoutCompletePage, ProductDetailsPage)
  pages/utils/                    -> ConfigReader, CsvDataReader, ScreenshotUtils, WaitUtils

src/test/java/
  base/BaseTest.java              -> per-test driver lifecycle + reusable login helper
  listeners/                      -> ExtentReportManager + TestListener (report/screenshots)
  dataProviders/                  -> CSV-backed TestNG @DataProvider methods
  tests/                          -> LoginTest, LogoutTest, InventorySortTest, ProductDetailsTest,
                                      CartTest, CartStateNavigationTest, CheckoutTest

src/test/resources/
  config.properties               -> browser/url/timeouts (overridable via -D flags)
  testdata/*.csv                  -> data-driven test data

testng.xml                        -> suite definition, listener registration, parallel execution
.github/workflows/ci.yml          -> GitHub Actions CI pipeline (if present)
```

## How each requirement is covered

| Requirement | Where |
|---|---|
| Maven project | `pom.xml` |
| Page Objects | `pages/*` |
| Configurable URL/browser | `config.properties` + `ConfigReader`, overridable with `-Dbrowser=`, `-Durl=`, `-Dheadless=` |
| Selenium waits | `WaitUtils` (explicit waits), no `Thread.sleep()` anywhere |
| Meaningful assertions | Every test asserts specific, business-relevant outcomes |
| Reusable components | `BasePage` (common actions), `BaseTest` (setup/teardown/login helper), `DriverFactory` |
| Independent tests | Each `@Test` gets a fresh `WebDriver` in `@BeforeMethod`/`@AfterMethod`; no shared state |
| Execution report | ExtentReports HTML report generated per run under `test-output/` |
| Screenshot/log on failure | `TestListener.onTestFailure` auto-captures a screenshot and attaches it to the report |

### Capabilities implemented (assessment asked for at least 4)

1. **Data-driven testing** – CSV-backed `@DataProvider`s (`invalid_login_data.csv`, `checkout_customers.csv`)
2. **Configurable application/environment settings** – `config.properties`, overridable via system properties
3. **Automatic screenshots on test failure** – `TestListener` + `ScreenshotUtils`
4. **Test reporting** – ExtentReports HTML report with pass/fail/screenshots
5. **Cross-browser execution** – `DriverFactory` supports Chrome / Firefox / Edge via `-Dbrowser=`
6. **Parallel test execution** – `testng.xml` (`parallel="classes"`, multiple threads), safe because `DriverFactory` uses a `ThreadLocal<WebDriver>`
7. **Reusable driver/factory + test-base architecture** – `DriverFactory`, `BaseTest`, `BasePage`
8. **CI/CD execution readiness** – GitHub Actions workflow runs the suite headlessly on every push/PR
9. **Additional negative/edge-case scenarios** – extra checkout validation tests, extra rows in `invalid_login_data.csv`

## Prerequisites

- JDK 17+
- Maven 3.8+ (or IntelliJ's bundled Maven)
- Google Chrome installed (default browser; Firefox/Edge also supported)
- Internet access (WebDriverManager downloads the correct driver binary automatically)

## Opening in IntelliJ

1. `File > Open...` and select the project's root folder (the one containing `pom.xml`).
2. IntelliJ will detect it as a Maven project and auto-import dependencies.
3. Make sure **Project SDK** is set to Java 17: `File > Project Structure > Project`.
4. Install/enable the **TestNG** plugin if prompted (bundled with IntelliJ Ultimate; for Community Edition, install "TestNG" from `Settings > Plugins`).

## Running the tests

**From IntelliJ:** right-click `testng.xml` (project root) → `Run 'testng.xml'`. You can also right-click any single test class/method under `src/test/java` and run it individually.

**From the command line:**
```powershell
mvn clean test
```

**Overriding configuration** (no code changes needed):
```powershell
mvn clean test -Dbrowser=firefox -Dheadless=true
mvn clean test -Durl=https://www.saucedemo.com/
```

## Viewing results

- **HTML report:** `test-output/ExtentReport_<timestamp>.html` — open in any browser. Failed tests include an embedded screenshot.
- **Screenshots:** `test-output/screenshots/*.png`
- **Raw Surefire results:** `target/surefire-reports/`

## Notes on test data

- Valid demo credentials (`standard_user` / `secret_sauce`) are SauceDemo's own public, intentionally-published demo credentials — not a real secret.
- The cart/session behavior test documents observed SauceDemo behavior: the cart **persists** across logout/login (kept in browser local storage). If the application's behavior changes, that is the test to revisit.

# SauceDemo Selenium Automation Framework

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.18.1-43B02A.svg)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.15.0-23D96C.svg)](https://cucumber.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.9.0-yellow.svg)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9.x-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

A hybrid BDD test automation framework for [SauceDemo](https://www.saucedemo.com), built with Selenium WebDriver, Cucumber, and TestNG, following the Page Object Model design pattern.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Framework Design](#framework-design)
- [Configuration](#configuration)
- [Continuous Integration](#continuous-integration)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

This repository contains an end-to-end UI test automation suite for the SauceDemo e-commerce application. The framework validates core user journeys including login, product cart management, checkout, and order confirmation, using a hybrid approach that combines:

- **Behavior-Driven Development (BDD)** with Cucumber for human-readable test specifications
- **Page Object Model (POM)** for maintainable, reusable UI abstractions
- **Data-driven testing** via Excel-based test data using Apache POI
- **Cross-browser execution** (Chrome, Firefox, Edge) with headless support for CI pipelines

## Tech Stack

| Category | Tool / Library | Version |
|---|---|---|
| Language | Java | 21 |
| Browser Automation | Selenium WebDriver | 4.18.1 |
| BDD Framework | Cucumber (Java, TestNG) | 7.15.0 |
| Test Runner | TestNG | 7.9.0 |
| Build Tool | Maven | 3.9.x |
| Driver Management | WebDriverManager | 5.7.0 |
| Data-Driven Testing | Apache POI | 5.2.5 |
| Reporting | ExtentReports / Cucumber Reporting | 5.1.1 / 5.7.7 |
| Logging | Log4j2 | 2.22.1 |
| Assertions | AssertJ | 3.25.3 |
| Test Data Generation | JavaFaker | 1.0.2 |
| CI/CD | GitHub Actions | — |

## Project Structure

```
sauce-demo/
├── src/
│   ├── main/
│   │   ├── java/com/saucedemo/
│   │   │   ├── base/            # BasePage — shared WebDriver actions
│   │   │   ├── config/          # ConfigReader — environment & properties
│   │   │   ├── constants/       # Centralized error messages & page titles
│   │   │   ├── pages/           # Page Objects (LoginPage, AddToCartPage, etc.)
│   │   │   └── utils/           # DriverManager, WaitUtils, ExcelReader, ScreenshotUtils
│   │   └── resources/
│   │       ├── config.properties
│   │       └── log4j2.properties
│   └── test/
│       ├── java/com/saucedemo/
│       │   ├── hooks/           # Cucumber @Before/@After lifecycle hooks
│       │   ├── runners/         # TestNG-Cucumber runners (full / smoke / regression)
│       │   └── stepdefinitions/ # Step definitions mapped to feature files
│       └── resources/
│           ├── features/        # Gherkin .feature files
│           ├── testdata/        # Excel test data files
│           └── testng.xml       # TestNG suite configuration
├── pom.xml
└── README.md
```

## Prerequisites

Ensure the following are installed before running the suite:

- [Java JDK 21](https://www.oracle.com/java/technologies/downloads/)
- [Apache Maven 3.9+](https://maven.apache.org/download.cgi)
- A supported browser: Google Chrome, Mozilla Firefox, or Microsoft Edge
- Git

> Browser drivers are managed automatically at runtime via **WebDriverManager** — no manual driver downloads or `PATH` configuration required.

## Getting Started

Clone the repository and navigate to the project directory:

```bash
git clone https://github.com/<your-username>/selenium-automation.git
cd selenium-automation/sauce-demo
```

Install dependencies:

```bash
mvn clean install -DskipTests
```

## Running Tests

Run the full test suite:

```bash
mvn test
```

Run with a specific browser:

```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

Run in headless mode (recommended for CI):

```bash
mvn test -Dheadless=true
```

Run a specific tagged subset:

```bash
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@regression"
```

Run against a specific environment:

```bash
mvn test -Denv=qa
```

## Test Reports

After execution, test reports are generated under the `target/` directory:

| Report Type | Location |
|---|---|
| Cucumber HTML Report | `target/cucumber-reports/report.html` |
| Cucumber JSON (raw) | `target/cucumber.json` |
| TestNG Surefire Report | `target/surefire-reports/index.html` |
| Execution Logs | `target/logs/automation.log` |

Open `report.html` in any browser to view a step-by-step breakdown of each scenario, including screenshots attached automatically on failure.

## Framework Design

**Page Object Model (POM)**
Each application page is represented by a dedicated class under `pages/`, encapsulating its locators and actions. All page classes extend `BasePage`, which provides reusable wait-aware methods (`click`, `type`, `getText`, `isDisplayed`, etc.) so that no test logic ever interacts with `WebDriver` directly.

**Driver Management**
`DriverManager` uses a `ThreadLocal<WebDriver>` to guarantee thread-safe, isolated browser sessions during parallel execution, with lifecycle fully controlled via Cucumber `@Before` / `@After` hooks.

**Explicit Waits Only**
`WaitUtils` centralizes all synchronization logic using `WebDriverWait` and `ExpectedConditions`, avoiding flaky, mixed implicit/explicit wait strategies.

**BDD with Cucumber**
Test scenarios are written in Gherkin under `src/test/resources/features/`, keeping business-readable specifications decoupled from implementation. Step definitions translate Gherkin steps into Page Object calls.

**Data-Driven Testing**
Test data (e.g., cart items, checkout details) is externalized into Excel files and read via `ExcelReader`, keeping test data separate from test logic.

**Tagging Strategy**

| Tag | Purpose |
|---|---|
| `@smoke` | Critical-path scenarios for quick validation |
| `@regression` | Full regression coverage |

## Configuration

All environment and execution settings are managed centrally in:

```
src/main/resources/config.properties
```

| Property | Description | Default |
|---|---|---|
| `browser` | Browser to launch | `chrome` |
| `baseUrl` | Application URL under test | `https://www.saucedemo.com` |
| `implicitWait` | Implicit wait (seconds) | `10` |
| `explicitWait` | Explicit wait timeout (seconds) | `15` |
| `headless` | Run browser in headless mode | `false` |
| `screenshotOnFailure` | Capture screenshot on scenario failure | `true` |

Properties can be overridden at runtime via Maven system properties (e.g., `-Dbrowser=firefox`, `-Dheadless=true`) without modifying the source file.

## Continuous Integration

This project includes a GitHub Actions workflow (`.github/workflows/ci.yml`) that runs the test suite automatically on every push and pull request, executing in headless mode to support unattended CI execution.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m "Add your feature"`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

Please ensure new step definitions and page objects follow the existing POM structure and naming conventions, and that all tests pass locally before submitting a PR.

## License

This project is licensed under the terms of the [MIT License](LICENSE).


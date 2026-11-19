# Toolshop QA Framework

End-to-end test automation for the [Practice Software Testing](https://github.com/testsmith-io/practice-software-testing) (Toolshop) web shop, running locally in Docker.
One framework, five layers: UI, API, database, email and (coming) performance and security.

## What it covers

| Layer | Tools | Examples |
|---|---|---|
| UI | Selenium, Java, TestNG, Page Object Model | Search, sort, cart, login, checkout |
| API | REST Assured, Postman and Newman | Auth, products, cart, invoices, lockout, password rules, JSON schema |
| Database | JDBC, SQL (MariaDB) | Integrity checks, API to DB cross-checks for users and invoices |
| Email | MailCatcher API, polling | Registration email content |
| Reporting | Allure | Screenshots, page text, requests and responses attached to failures |

Planned: JMeter load tests, GitHub Actions CI, OWASP ZAP security scan.

## How the work was organized

Requirements, test strategy and test cases come first (`docs/`), then automation. Every requirement has an ID, priority and
the layers that cover it. Where the app differs from its spec, the difference is recorded and handled through a
`known-issue` test group, not hidden.

## Findings worth noting

- The welcome email contains the user's stored password hash (a `known-issue` test documents it).
- The payment step needs two Confirm clicks (the spec says one).
- Registration rejects passwords found in a data leak, a rule not in the written spec.
- Address validation on invoices is strict and lookup-based, so test data must be a known-good address.
- Account lockout: 3 failed attempts lock the account (HTTP 423); a successful login resets the counter.
- The bundled Swagger UI defaults to the public demo server, not the local one.

## Prerequisites

Java 21, Maven 3.9, Chrome, Docker. Start the shop (clone the app repo, adjust the MariaDB host port to 3307 in
its `docker-compose.yml`, then `docker compose up -d`). The tests expect the UI on `localhost:4200`, the API on
`localhost:8091`, MailCatcher on `localhost:1080`.

## Project layout

    src/main/java/.../config, driver, pages, listeners   framework code
    src/test/java/.../api, db                            API and DB helpers
    src/test/java/.../tests/{ui,api,db,mail,e2e}         the tests
    src/test/resources                                   config, suite files, JSON schemas
    postman/                                             Postman collection and environment
    docs/                                                requirements, strategy, test cases

## Running the tests

Pick a suite with `-Dsuite.file`:

| Suite | Command | What it covers |
|---|---|---|
| UI regression | `mvn clean test` | Selenium tests (parallel by class) |
| UI smoke | `mvn clean test -Dsuite.file=src/test/resources/smoke.xml` | Critical UI paths |
| API regression | `mvn clean test -Dsuite.file=src/test/resources/api.xml` | REST Assured tests |
| API smoke | `mvn clean test -Dsuite.file=src/test/resources/api-smoke.xml` | Critical API checks |
| Database | `mvn clean test -Dsuite.file=src/test/resources/db.xml` | SQL checks and API to DB cross-checks |
| Mail | `mvn clean test -Dsuite.file=src/test/resources/mail.xml` | Email content (slow: asynchronous delivery) |
| Full stack | `mvn clean test -Dsuite.file=src/test/resources/e2e.xml` | One customer journey across API, DB, UI and mail |
| Known issues | `mvn clean test -Dsuite.file=src/test/resources/known-issues.xml` | Tests that fail on purpose to document defects |
| Cleanup | `mvn clean test -Dsuite.file=src/test/resources/cleanup.xml -Dcleanup.confirm=yes` | Deletes test-created data (local DB only) |

Postman collection: `newman run postman/toolshop-api.postman_collection.json -e postman/toolshop-local.postman_environment.json`

Reports: `mvn allure:serve` after any run.

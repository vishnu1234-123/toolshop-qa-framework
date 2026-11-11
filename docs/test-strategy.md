# Test Strategy: Toolshop (Sprint 5, local Docker)

## 1. Objective
Provide fast, reliable, traceable regression coverage of the Toolshop web shop across UI, API,
database, performance and security, runnable locally and in CI.

## 2. Scope
In scope and out of scope items, with IDs and priorities, are in `requirements.md`.
Every test case references a requirement ID (REQ-xxx). P1 requirements are automated first.

## 3. System under test
Angular UI (localhost:4200), Laravel REST API (localhost:8091), MariaDB (host port 3307),
MailCatcher (localhost:1080). Auth uses JWT bearer tokens that expire after 300 seconds.

## 4. Approach by layer (test pyramid)
| Layer | Tool | Role |
|---|---|---|
| API | REST Assured, Postman/Newman | Bulk of functional, negative and authorization tests |
| UI | Selenium 4, Java, TestNG, Page Objects | Critical journeys only: search, cart, checkout, login, register |
| DB | JDBC + SQL | Confirm data was really saved (users, invoices, brands) |
| Performance | JMeter | Product listing, login, checkout under load |
| Security | OWASP ZAP | Baseline and authenticated API scan; plus API authorization tests |
| Reporting | Allure | Steps, screenshots, trends |
| CI | GitHub Actions | Start stack, run suites, publish reports |

Rule of thumb: if a rule can be checked through the API, test it there and keep the UI
for what only the UI can prove (layout, flow, messages).

## 5. Test design techniques
- Boundary value analysis: search length (2, 3, 40, 41), quantity (0, 1, max, max+1),
  message length (49, 50), email length (256, 257).
- Equivalence partitioning: payment methods, valid and invalid card data, user roles.
- State transition: account lock after 3 failed logins (HTTP 423), admin exempt.
- Decision table: combination discount (rental and non-rental items in cart).
- Error guessing and exploratory notes feed negative tests.

## 6. Test data and environment
- Docker Compose stack. Reset with `php artisan migrate:fresh --seed` before full runs.
- Tests create their own users with unique emails (timestamp) and clean up where possible.
- Account-lock tests use a throwaway user, never the shared demo accounts.
- URLs, DB host/port and credentials come from configuration, not from test code.
- DB is exposed on host port 3307 because a local MySQL occupies 3306.
- Tokens are requested at the start of each run and refreshed when they expire.

## 7. Entry and exit criteria
- Entry: all containers up, GET /status returns 200, database seeded.
- Exit: all P1 tests pass, no open high-severity defects, Allure report published.

## 8. Defect management
Each defect records requirement ID, steps, expected vs actual, environment and evidence.
Before logging, check the acceptance criteria to separate defects from design decisions.

## 9. Risks and mitigations
| Risk | Mitigation |
|---|---|
| Flaky UI timing (lists update late) | Explicit waits on conditions; verify state via API |
| 5-minute token expiry | Fetch fresh token per suite; helper refreshes on 401 |
| Shared database state | Unique data per test; reset script; independent tests |
| Container start order (nginx needs phpMyAdmin) | Health checks and `docker compose up --wait` in CI |
| Port clashes on developer machines | Configurable ports |

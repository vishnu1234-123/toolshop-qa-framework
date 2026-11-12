# Test Strategy: Toolshop (Sprint 5, local Docker)

## 1. Objective
Provide fast, reliable, traceable regression coverage of the Toolshop web shop across UI, API,
database, performance and security, runnable locally and in CI.

## 2. Scope and priorities
Requirements, priorities, layers and exclusions are in `requirements.md`. Every test case
references a requirement ID (REQ-xxx). Priority rule: P1 blocks the purchase path, charges a wrong
amount, or exposes accounts or data. P1 is automated first.

## 3. System under test
Angular UI (localhost:4200), Laravel REST API (localhost:8091), MariaDB (host port 3307),
MailCatcher (localhost:1080). Auth uses JWT bearer tokens that expire after 300 seconds.

## 4. Approach by layer (test pyramid)
| Layer | Tool | Role |
|---|---|---|
| API | REST Assured, Postman/Newman | Bulk of functional, validation, negative and authorization tests |
| UI | Selenium 4, Java, TestNG, Page Objects | Critical journeys and what only the UI can prove (messages, flow, display) |
| DB | JDBC + SQL | Confirm persistence (users, invoices, password hash, brands) |
| Email | MailCatcher HTTP API | Verify registration, contact and password-reset emails |
| Performance | JMeter | Product listing, login, checkout under load |
| Security | OWASP ZAP + API authorization tests | Baseline and authenticated scans; access control |
| Reporting | Allure | Steps, screenshots, REQ and TC tags |
| CI | GitHub Actions | Start stack, run suites, publish reports |

Rule of thumb: if a rule can be checked through the API, test it there and keep the UI for what
only the UI can show. Server-side validation is tested via the API even when the UI also validates.

## 5. Test design techniques
- Boundary value analysis: search length (2, 3, 40, 41), quantity (0, 1, 99, 100), message length
  (49, 50), email length (256, 257).
- Equivalence partitioning: payment methods, valid and invalid card data, user roles.
- State transition: account lock after 3 failed logins (HTTP 423), admin exempt.
- Decision table: combination discount (rental and non-rental items in the cart).
- Data-driven tests: one password-rules data set reused on Registration and Change Password.
- Error guessing and exploratory notes feed negative tests.

## 6. Spec vs observed behavior
Where the app differs from the acceptance criteria, the deviation is recorded in `requirements.md`.
Tests assert the documented behavior and are tagged as known issues (Allure @Issue) until the
behavior is clarified, so the report shows the gap without hiding it. Defects are logged only
after checking the acceptance criteria.

## 7. Test data and environment
- Docker Compose stack. Reset with `php artisan migrate:fresh --seed` before full runs.
- Tests create their own users with unique emails (timestamp) and do not depend on each other.
- Account-lock tests use a throwaway user, never the shared demo accounts.
- Discount tests are data dependent: they first check that suitable products exist, and are
  skipped with a clear reason when the seed data has none.
- URLs, DB host/port and credentials come from configuration, not from test code.
- DB is exposed on host port 3307 because a local MySQL occupies 3306.
- Tokens are requested at the start of each run and refreshed when they expire.

## 8. Entry and exit criteria
- Entry: all containers up, GET /status returns 200, database seeded.
- Exit: all P1 tests pass or are tagged known issues, no unexplained failures, Allure report published.

## 9. Defect management
Each defect records requirement ID, steps, expected vs actual, environment and evidence.

## 10. Risks and mitigations
| Risk | Mitigation |
|---|---|
| Flaky UI timing (lists update late) | Explicit waits on conditions; verify state via API |
| 5-minute token expiry | Fetch a fresh token per suite; refresh on 401 |
| Shared database state | Unique data per test; reset script; independent tests |
| Spec and app disagree | Known-issue tagging; clarify before logging defects |
| Container start order (nginx needs phpMyAdmin) | Health checks and `docker compose up --wait` in CI |
| Port clashes on developer machines | Configurable ports |

# Toolshop QA Framework

End-to-end test automation for the [Practice Software Testing](https://github.com/testsmith-io/practice-software-testing) (Toolshop) web shop, running locally in Docker.
One framework, five layers: UI, API, database, email, performance and (coming) security.

## What it covers

| Layer | Tools | Examples |
|---|---|---|
| UI | Selenium, Java, TestNG, Page Object Model | Search, sort, cart, login, checkout |
| API | REST Assured, Postman and Newman | Auth, products, cart, invoices, lockout, password rules, JSON schema |
| Database | JDBC, SQL (MariaDB) | Integrity checks, API to DB cross-checks for users and invoices |
| Email | MailCatcher API, polling | Registration email content |
| Performance | JMeter, NeoLoad | Load, spike, breakpoint, soak; JMeter vs NeoLoad comparison |
| Reporting | Allure | Screenshots, page text, requests and responses attached to failures |

Planned: GitHub Actions CI, OWASP ZAP security scan.

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

## Performance testing (JMeter)

One parameterized plan, `jmeter/toolshop-load.jmx`, simulates a shopping session per virtual user:
list products (extracts a product id), search, open that product, log in (extracts the token), then fetch the profile with
the token. Every step asserts HTTP 200. The plan is read-only, so it does not use up product stock. Users, ramp-up, loops
and think time are properties, so one file covers every test type:

    jmeter -n -t jmeter/toolshop-load.jmx -Jthreads=50 -Jrampup=2 -Jloops=5 \
      -l target/jmeter/spike/results.jtl -e -o target/jmeter/spike/report

(Login uses the seeded demo customer; override with `-Jemail=... -Jpassword=...`.)

### Results

JMeter, the app and its database all ran on one laptop with nothing else using the CPU, so these numbers describe this
setup and not the application's true capacity. No SLA exists, so I assumed p95 under 500 ms and errors under 1%.

| Test | Users / ramp-up | Requests | Errors | Avg | p95 | p99 | Throughput |
|---|---|---|---|---|---|---|---|
| Load | 20 / 20 s | 2,000 | 0% | 25 ms | 75 ms | 105 ms | 27.9/s |
| Spike | 50 / 2 s | 1,250 | 0% | 36 ms | 109 ms | 185 ms | 84.5/s |
| Breakpoint step 1 | 100 / 30 s | 5,000 | 0% | 32 ms | 79 ms | 324 ms | 90.1/s |
| Breakpoint step 2 | 200 / 30 s | 10,000 | 0% | 161 ms | 395 ms | 447 ms | 164.5/s |
| Breakpoint step 3 | 400 / 30 s | 20,000 | 0% | 920 ms | 1,472 ms | 1,544 ms | 202.5/s |
| Soak | 50 / 30 s, about 10 min | 50,000 | 0% | 28 ms | 78 ms | 117 ms | 89.4/s |

### What the numbers show

- The p95 target is met up to 200 users and missed at 400. The breakpoint under the assumed target lies between 200 and 400 users.
- Throughput flattens at about 200 requests/s: doubling users from 200 to 400 raised it only about 23% while response times
  rose several times over. Requests queue and are not rejected, so errors stay at 0% even where the app is clearly too slow.
- At saturation every endpoint slows by the same amount, so the limit is shared. The API container was at about 330% CPU
  while the database was at about 3%.
- The soak run had no errors and response times matched the short load run.
- At light load, login is the slowest request (about 75 ms against about 15 ms for the GETs).

### Lesson: check the environment before trusting numbers

An unrelated container from another project was using about 2.3 CPU cores at idle. With it running, the 200-user step
gave p95 886 ms; after stopping it, p95 was 395 ms. All results above were taken after stopping it.

### Not covered

Volume testing (the dataset is small) and scalability testing (needs an environment that can add instances) are out of
scope here. Stress testing beyond 400 users was not needed once the breakpoint and saturation were found.

## Performance testing (NeoLoad)

The same five-step shopping journey is also built in NeoLoad (project in `performance/neoload`, imported from
`postman/toolshop-api.neoload.postman_collection.json`). Login extracts the token with a JSONPath variable extractor,
500 ms delays model think time, and one population runs the path at 100%. The free trial allows 50 concurrent users, so
these are smaller tests than the JMeter breakpoint runs, and the trial showed no p90/p95/p99, so percentiles come from JMeter.

| Test | Users | Duration | Requests | Errors | Avg | Max |
|---|---|---|---|---|---|---|
| Baseline | 1 | 1 min | 111 | 0 | 32 ms | 366 ms |
| Load (ramp 2 to 20) | 20 | 5 min | 9,512 | 0 | 32 ms | 444 ms |
| Spike | 50 | 2 min | 10,230 | 0 | 77 ms | 1.76 s |
| Soak | 20 | 10 min | 22,398 | 0 | 31 ms | 979 ms |

At 20 users both tools agree (about 25-32 ms average). During the 50-user spike the API container was the busiest part
(around 70% CPU at the first reading), while MariaDB stayed under 4%. Memory stayed flat during the soak.
Full comparison: [docs/performance-results.md](docs/performance-results.md).

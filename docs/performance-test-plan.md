# Performance test plan

## Goal
Check that the Toolshop API stays responsive under expected and peak shopping traffic, find the point where it saturates,
and check that it stays stable over time.

## Assumptions (no real SLA or production data exists)
These are assumed targets, chosen for this project. In a real project they come from the business and from production analytics.

| Item | Assumed value |
|---|---|
| Normal load | 100 concurrent users |
| Peak load (sale day) | 200 concurrent users |
| Response time | p95 under 500 ms for every request |
| Errors | under 1% |
| Think time between requests | 500 ms |

## Workload model (assumed traffic mix)
| Journey | Share of users | Requests |
|---|---|---|
| Browse | 35% | GET /products |
| Search | 25% | GET /products/search?q=... |
| Product page | 25% | GET /products, then GET /products/{id} |
| Logged-in visit | 15% | POST /users/login, then GET /users/me |

Read-only traffic only: no checkout, so product stock is never used up.

## Environment
Local laptop, app and database in Docker, load tool on the same machine. Results describe this setup, not true capacity.
Nothing else may use the CPU during a run (an unrelated container once doubled response times).

## Test types
| Type | Purpose |
|---|---|
| Baseline | 1 user, to get the best-case response time |
| Load | Expected user count, steady |
| Breakpoint | Step users up until p95 or errors miss the target |
| Spike | Sudden jump to a high user count |
| Soak | Moderate load for about 10 minutes, watch for drift |

## Tools
JMeter (full-scale runs, see README for results) and NeoLoad (same scenario rebuilt, scaled down to the trial's user limit).

## Pass criteria
p95 under 500 ms and errors under 1% at the load level under test.

# Performance results: JMeter vs NeoLoad

Toolshop (Docker, local Mac). The load generator ran on the same machine as the app, so absolute numbers are indicative, not production-grade.
All runs below had 0 errors.

## JMeter (percentiles available)

| Run | Samples | Avg ms | p95 ms | p99 ms | Throughput/s |
|---|---|---|---|---|---|
| load | 2,000 | 25 | 75 | 105 | 27.9 |
| spike | 1,250 | 36 | 109 | 185 | 84.5 |
| soak | 50,000 | 28 | 78 | 117 | 89.4 |
| step-100 | 5,000 | 32 | 79 | 324 | 90.1 |
| step-200 | 10,000 | 161 | 395 | 447 | 164.5 |
| step-200b | 10,000 | 320 | 692 | 770 | 146.4 |
| step-400 | 20,000 | 920 | 1,472 | 1,544 | 202.5 |

## NeoLoad (trial: max 50 concurrent users; percentiles not shown)

| Run | Users | Duration | Requests | Avg ms | Max ms | Throughput/s (approx) |
|---|---|---|---|---|---|---|
| Baseline | 1 | 1 min | 111 | 32 | 366 | 1.9 |
| Load (ramp 2 to 20) | 20 | 5 min | 9,512 | 32 | 444 | 32 |
| Spike | 50 | 2 min | 10,230 | 77 | 1,760 | 85 |
| Soak | 20 | 10 min | 22,398 | 31 | 979 | 37 |

## Findings

- JMeter and NeoLoad agree at 20 users (about 25-32 ms average). The app is comfortable at that load.
- At 50 users held for 2 minutes, NeoLoad average latency rose to 77 ms and the API container reached about 70% CPU.
- JMeter step tests show the knee between 100 and 200 threads. At 400 threads the average reached 920 ms, still with 0 errors, so the app slows down before it fails.
- Soak (10 min in NeoLoad): API memory flat at about 67 MiB, MariaDB flat at about 97 MiB, no latency creep. A real soak would run for hours.
- The NeoLoad trial did not show p90/p95/p99, so JMeter is the source for percentiles.

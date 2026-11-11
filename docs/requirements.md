# Requirements (derived from Sprint 5 user stories)

Source: toolshop-app/docs/user-stories/v5.md (AC = acceptance criterion), the Swagger API contract,
and exploratory notes. Priority: P1 critical path, P2 important, P3 nice to have.

| ID | Requirement | Source | Pri | Layers |
|---|---|---|---|---|
| REQ-PO-01 | Product grid shows image, name and price per card | Overview AC1-2 | P2 | UI, API |
| REQ-PO-02 | Pagination updates the grid | Overview AC3 | P2 | UI, API |
| REQ-PO-03 | Search accepts 3-40 characters, returns matches, resets filters | Overview AC4 | P1 | UI, API |
| REQ-PO-04 | Category, brand and combined filters work; parent category selects children | Overview AC5-8 | P2 | UI, API |
| REQ-PO-05 | Sort by name A-Z/Z-A and price high-low/low-high | Overview AC9 | P1 | UI, API |
| REQ-PO-06 | Price slider default 1-100, max 200, filters products | Overview AC10-11 | P2 | UI, API |
| REQ-PO-07 | Out-of-stock indicator on card | Overview AC13 | P3 | UI |
| REQ-PD-01 | Product detail shows image, name, description, price, category, brand | Detail AC1 | P2 | UI, API |
| REQ-PD-02 | Quantity: default 1, plus/minus, minimum 1, manual entry clamped 1-999,999,999 | Detail AC3-7 | P1 | UI |
| REQ-PD-03 | Add to cart shows "Product added to shopping cart." | Detail AC8 | P1 | UI, API |
| REQ-PD-04 | Out-of-stock product cannot be added | Detail AC9 | P3 | UI |
| REQ-PD-05 | Favorites: success, duplicate and unauthorized messages | Detail AC11-13 | P2 | UI, API |
| REQ-CO-01 | Cart table, quantity update recalculates totals | Cart AC1-2 | P1 | UI, API |
| REQ-CO-02 | Delete cart item; empty cart message | Cart AC3-4 | P1 | UI, API |
| REQ-CO-03 | Guest sees login step at checkout; logged-in user sees signed-in message | Sign In AC1,4,5 | P1 | UI |
| REQ-CO-04 | Billing address: required fields, max lengths, prefill for logged-in user | Billing AC1-4 | P1 | UI |
| REQ-CO-05 | Payment methods: bank transfer, cash on delivery, credit card, BNPL, gift card | Payment AC1 | P1 | UI |
| REQ-CO-06 | Per-method field validation (card format, future expiry, installments 3/6/9/12) | Payment AC2-7 | P1 | UI, API |
| REQ-CO-07 | Switching payment method resets the form | Payment AC8 | P3 | UI |
| REQ-CO-08 | Successful order shows invoice number, clears cart, creates invoice in DB | Payment AC9 | P1 | UI, API, DB |
| REQ-RN-01 | Rentals list; detail has 1-10 hour slider; total = hourly rate x hours | Rental AC1-3 | P2 | UI |
| REQ-DS-01 | 15% combination discount when cart has rental and non-rental; removed otherwise; shown on invoice | Cart AC7-8, Combination AC1-4 | P2 | UI, API, DB |
| REQ-REG-01 | Registration form: required fields, email max 256 and valid format, success redirects to login | Register AC1,6 | P1 | UI, API, DB |
| REQ-REG-02 | Password rules (8+ chars, upper+lower, number, special); live feedback and strength bar | Register AC2-4 | P1 | UI, API |
| REQ-REG-03 | Duplicate email gives "Email is already in use." | Register AC5 | P1 | UI, API |
| REQ-AUTH-01 | Login success; redirect /account for users, /admin/dashboard for admins | Login AC2 | P1 | UI, API |
| REQ-AUTH-02 | Invalid credentials show "Invalid email or password" | Login AC3 | P1 | UI, API |
| REQ-AUTH-03 | Account locks after 3 failed attempts (HTTP 423); admins exempt | Login AC4-5 | P1 | API, UI |
| REQ-AUTH-04 | Disabled account cannot log in ("Account disabled.") | Login AC6, Admin AC7 | P2 | API, UI |
| REQ-AUTH-05 | JWT expires after 300 s; protected endpoints return 401 without a token | Observed, Swagger | P1 | API |
| REQ-FP-01 | Forgot password emails a new password (check MailCatcher); unknown email gives error | Forgot AC1-4 | P2 | UI, API |
| REQ-ACC-01 | Profile view and update; email read-only | Profile AC1-4 | P2 | UI, API |
| REQ-ACC-02 | Change password: mismatch, wrong current and same-as-current errors; logout after success | Change Password AC3-6 | P2 | UI, API |
| REQ-FAV-01 | Favorites list, empty state, remove | Favorites AC1-3 | P2 | UI, API |
| REQ-INV-01 | Invoice list and detail (items, address, payment); other users' invoices not found | Invoices AC1-3 | P1 | UI, API, DB |
| REQ-CON-01 | Contact form: six subjects, message min 50 chars, guest fields required, logged-in autofill | Contact AC1-3 | P2 | UI, API |
| REQ-CON-02 | Attachment must be .txt and exactly 0 KB, with specific error messages | Contact AC4-6 | P3 | UI, API |
| REQ-MSG-01 | My messages list, detail and reply | Messages AC1-3 | P3 | UI, API |
| REQ-ADM-01 | Admin dashboard: sales chart and recent invoices | Admin AC1 | P3 | UI |
| REQ-ADM-02 | Brands: list, create, edit, delete | Admin AC4 | P2 | UI, API, DB |
| REQ-ADM-03 | Categories: list, create, edit, delete (optional parent) | Admin AC3 | P2 | UI, API |
| REQ-ADM-04 | Products: list, create, edit, delete | Admin AC2 | P2 | API, UI |
| REQ-ADM-05 | Orders: list, details, status values | Admin AC5 | P2 | UI, API |
| REQ-ADM-06 | Users: list, view, edit, delete, enable/disable | Admin AC6-7 | P2 | UI, API |
| REQ-ADM-07 | Reports: monthly, weekly, statistics | Admin AC9 | P3 | UI, API |
| REQ-SEC-01 | Authorization: customers cannot call admin endpoints; users cannot read others' invoices or data | Derived | P1 | API |
| REQ-NFR-01 | Product listing, login and checkout stay responsive under load (thresholds set in JMeter) | Derived | P2 | Perf |
| REQ-NFR-02 | No high-severity findings in an OWASP ZAP baseline scan | Derived | P2 | Security |

## Out of scope (for now)
Google/GitHub social login, TOTP two-factor setup (test accounts are blocked), chat widget,
geo-location discount (needs browser location), multi-language beyond a smoke check, PDF download,
privacy page, mobile apps, GraphQL.

## Open questions and defect candidates (to verify)
- Password hint says 8 characters; empty-password error says 6 (REQ-REG-02).
- Admin product create/edit does not save in UI (REQ-ADM-04): confirm via API.
- Brand and category delete/edit appear delayed in lists (REQ-ADM-02/03): affects automation waits.
- Registration via Swagger once did not appear in the DB (REQ-REG-01): retest.
- Should the cart be cleared or merged on login/logout? (carts are tied to the browser session).

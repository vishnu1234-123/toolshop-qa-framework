# Requirements (derived from Sprint 5 user stories)

Source: toolshop-app/docs/user-stories/v5.md (AC = acceptance criterion), the Swagger API contract,
and exploratory notes. IDs are stable: never renumber, only add.

Priority rule:
- P1: failure blocks the purchase path, charges a wrong amount, or exposes accounts or data.
- P2: important feature with a workaround. P3: minor, cosmetic or rarely used.

| ID | Requirement | Source | Pri | Layers |
|---|---|---|---|---|
| REQ-PO-01 | Product grid shows image, name and price per card; clicking a card opens that product's detail page | Overview AC1-2 | P1 | UI, API data check |
| REQ-PO-02 | Pagination controls update the grid | Overview AC3 | P2 | UI, API |
| REQ-PO-03 | Search accepts 3-40 characters, returns matching products, resets filters | Overview AC4 | P2 | UI, API |
| REQ-PO-04 | Category, brand and combined filters work; parent category selects its children | Overview AC5-8 | P2 | UI, API |
| REQ-PO-05 | Sorting by name A-Z/Z-A and price low-high/high-low orders the grid | Overview AC9 | P2 | UI, API |
| REQ-PO-06 | Price slider defaults to 1-100 (max 200) and filters products by the selected range | Overview AC10-11 | P2 | UI, API |
| REQ-PO-07 | Out-of-stock products show an Out of stock indicator | Overview AC13 | P3 | UI |
| REQ-PO-08 | Discounted products show the original price struck through and the discounted price (data dependent) | Overview AC12 | P2 | UI, API data check |
| REQ-PO-09 | Category page lists that category's products, titled with the category name, with the same filters | Browse by Category AC1-2 | P2 | UI, API |
| REQ-PD-01 | Detail page shows image, name, description, price, category and brand | Detail AC1 | P2 | UI, API data check |
| REQ-PD-02 | Quantity selector: default 1, plus/minus, minimum 1, manual entry limited to the allowed range (spec: 1-999,999,999) | Detail AC3-7 | P1 | UI |
| REQ-PD-03 | Add to cart shows "Product added to shopping cart." and the cart holds the chosen quantity | Detail AC8 | P1 | UI, API |
| REQ-PD-04 | Out-of-stock (non-rental) product cannot be added; button disabled | Detail AC9 | P3 | UI |
| REQ-PD-05 | Favorites: success, duplicate and unauthorized messages | Detail AC11-13 | P2 | UI, API |
| REQ-PD-06 | Discounted product shows struck-through original price, discounted price and percentage badge (data dependent) | Detail AC2 | P2 | UI, API data check |
| REQ-PD-07 | Related products are shown below the product details | Detail AC14 | P3 | UI |
| REQ-RN-01 | Rentals list; rental detail has a 1-10 hour slider; total = hourly rate x hours | Rental AC1-3, Detail AC10 | P2 | UI |
| REQ-RN-02 | Rental items are marked "This is a rental item" in the checkout cart | Rental AC4 | P3 | UI |
| REQ-CO-01 | Cart table shows items with quantity, price and total (spec also lists Actions); changing quantity recalculates totals and shows "Product quantity updated." | Cart AC1-2 | P1 | UI, API |
| REQ-CO-02 | Delete removes an item and recalculates totals; empty cart shows "Your shopping cart is empty" | Cart AC3-4 | P1 | UI, API |
| REQ-CO-09 | Proceed with at least one item advances to the next checkout step | Cart AC5 | P1 | UI |
| REQ-CO-10 | Discounted cart items show a discount badge with original and discounted price (data dependent) | Cart AC6 | P3 | UI |
| REQ-CO-03 | Guest sees a sign-in step (email, password, submit); valid credentials continue to billing | Sign In AC1-2,4 | P1 | UI, API |
| REQ-CO-11 | Logged-in user sees "You are already signed in as ..." and continues straight to billing | Sign In AC5 | P2 | UI |
| REQ-CO-04 | Billing address: street, house number (observed), city, state, country, postal code required with max lengths; empty or invalid fields block Proceed; server rejects invalid data; prefilled for logged-in users | Billing AC1-4, observed | P1 | UI, API |
| REQ-CO-05 | Payment dropdown offers bank transfer, cash on delivery, credit card, buy now pay later, gift card | Payment AC1 | P1 | UI |
| REQ-CO-06 | Per-method validation: card format and future expiry, installments 3/6/9/12, bank and gift card fields | Payment AC2-7 | P1 | UI, API |
| REQ-CO-07 | Switching payment method resets the form | Payment AC8 | P3 | UI |
| REQ-CO-08 | Successful order shows the invoice number, clears the cart and saves the invoice with correct totals | Payment AC9 | P1 | UI, API, DB |
| REQ-DS-01 | Cart with rental and non-rental items gets 15% extra discount; removed when only one type remains; shown on the invoice | Cart AC7-8, Combination AC1-4 | P1 | UI, API, DB |
| REQ-REG-01 | Registration form with required fields (email max 256, valid format); success redirects to login and sends a confirmation email | Register AC1,6 | P1 | UI, API, DB |
| REQ-REG-02 | Password rules (8+ chars, upper and lower case, number, special) with live feedback and strength indicator; same component on Change Password | Register AC2-4, Change Password AC2 | P1 | UI, API |
| REQ-REG-03 | Duplicate email shows "Email is already in use." | Register AC5 | P1 | UI, API |
| REQ-AUTH-01 | Valid login redirects users to /account and admins to /admin/dashboard | Login AC2 | P1 | UI, API |
| REQ-AUTH-02 | Invalid credentials show "Invalid email or password" | Login AC3 | P1 | UI, API |
| REQ-AUTH-03 | Account locks after 3 failed attempts (HTTP 423); admins exempt | Login AC4-5 | P1 | API, UI |
| REQ-AUTH-04 | Disabled account cannot log in ("Account disabled.") | Login AC6, Admin AC7 | P2 | API, UI |
| REQ-AUTH-05 | JWT expires after 300 s; protected endpoints return 401 without a valid token | Observed, Swagger | P1 | API |
| REQ-AUTH-06 | Login page shows email and password fields and a Sign in with Google button | Login AC1 | P3 | UI |
| REQ-FP-01 | Forgot password emails a new password (check MailCatcher); unknown email gives an error | Forgot AC1-4 | P2 | UI, API |
| REQ-ACC-01 | Profile shows current data and saves updates (email read-only); changes persist in the database | Profile AC1-4 | P2 | UI, API, DB |
| REQ-ACC-02 | Change password form (current, new, confirm) shows errors for mismatch, wrong current password and same-as-current | Change Password AC1,3-5 | P2 | UI, API |
| REQ-ACC-03 | Successful change shows success, logs out after 5 seconds; new password works, old fails; hash changes in DB; other profile fields unchanged | Change Password AC6 | P2 | UI, API, DB |
| REQ-FAV-01 | Favorites list, empty state, remove | Favorites AC1-3 | P2 | UI, API |
| REQ-INV-01 | Invoice list (paginated) and detail show the user's invoices with items, address and payment method | Invoices AC1-2 | P2 | UI, API, DB |
| REQ-INV-02 | An invoice that does not exist or belongs to another user returns not found (API) and shows a not found message when opened by URL (UI) | Invoices AC3 | P1 | API, UI |
| REQ-INV-03 | Invoice with discounts shows subtotal, discount percentage and amount, final total and struck-through line prices | Invoices AC4-5 | P2 | UI, API, DB |
| REQ-INV-04 | PDF: download button shown, status endpoint reports readiness, download returns a PDF | Invoices AC6-8 | P3 | API, UI |
| REQ-CON-01 | Contact form: six subjects, message min 50 chars, guest fields required, logged-in autofill | Contact AC1-3 | P2 | UI, API |
| REQ-CON-02 | Attachment must be .txt and exactly 0 KB, with specific error messages | Contact AC4-6 | P3 | UI, API |
| REQ-CON-03 | Successful submission shows a confirmation and sends an email (check MailCatcher) | Contact AC7 | P2 | UI, API |
| REQ-MSG-01 | My messages list, detail and reply | Messages AC1-3 | P3 | UI, API |
| REQ-ADM-01 | Admin dashboard: sales chart and recent invoices | Admin AC1 | P3 | UI |
| REQ-ADM-02 | Brands: list, create, edit, delete | Admin AC4 | P2 | UI, API, DB |
| REQ-ADM-03 | Categories: list, create, edit, delete (optional parent) | Admin AC3 | P2 | UI, API |
| REQ-ADM-04 | Products: list, create, edit, delete | Admin AC2 | P2 | API, UI |
| REQ-ADM-05 | Orders: list, details, status values | Admin AC5 | P2 | UI, API |
| REQ-ADM-06 | Users: list, view, edit, delete, enable/disable | Admin AC6-7 | P2 | UI, API |
| REQ-ADM-07 | Reports: monthly, weekly, statistics | Admin AC9 | P3 | UI, API |
| REQ-SEC-01 | Authorization: customers cannot call admin endpoints; users cannot read others' data | Derived | P1 | API |
| REQ-NFR-01 | Product listing, login and checkout stay responsive under load (thresholds set in JMeter) | Derived | P2 | Perf |
| REQ-NFR-02 | No high-severity findings in an OWASP ZAP baseline scan | Derived | P2 | Security |

## Out of scope (with reasons)
- Social login (Google/GitHub) and TOTP 2FA: external providers; test accounts are blocked from TOTP.
- Location discounts (Geo AC1-4, Rental AC5): need browser geolocation; could be emulated later with a Chrome DevTools override.
- Chat widget, privacy page, multi-language beyond a smoke check, mobile apps, GraphQL, HTTP QUERY method.

## Spec vs observed deviations (verify, then log as defect or clarification)
- Quantity: spec says typed entry clamped to 1-999,999,999; observed no free typing and a maximum of 99 (PD-02).
- Cart table: spec lists an Actions column; none visible (CO-01).
- Billing: spec omits house number but the app requires it; address not prefilled for a logged-in user (check the profile has an address first); empty required fields are not highlighted (check after leaving the field) (CO-04).
- Registration: strength indicator (AC4) lags one change behind: Abcd@124 shows nothing, deleting the A then shows Excellent; live rules (AC3) work (REG-02); hint says 8 characters while the empty-password error says 6.
- Admin product create/edit does not save in the UI (ADM-04): confirm via API.
- Brand and category delete/edit appear delayed in lists (ADM-02/03): automation must wait on conditions.
- Registration via Swagger once did not appear in the DB (REG-01): retest.
- Guest checkout exists in the UI but is not in the Sprint 5 ACs.
- Cart is tied to the browser session, not the user: clarify expected behavior on login and logout.

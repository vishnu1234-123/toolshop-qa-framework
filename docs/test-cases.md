# Test Cases (P1 requirements)

Types: Pos = positive, Neg = negative, Bnd = boundary, Sec = security, E2E = end to end.

| TC | REQ | Test case (expected result) | Layer | Type |
|---|---|---|---|---|
| TC-001 | REQ-PO-03 | Search with 3 characters returns matching products | UI, API | Bnd |
| TC-002 | REQ-PO-03 | Search with 2 characters is rejected (no search / validation) | UI, API | Bnd |
| TC-003 | REQ-PO-03 | Search with 40 characters is accepted | API | Bnd |
| TC-004 | REQ-PO-03 | Search with 41 characters is rejected | API | Bnd |
| TC-005 | REQ-PO-05 | Sort price low-high returns ascending prices | UI, API | Pos |
| TC-006 | REQ-PO-05 | Sort name Z-A returns descending names | API | Pos |
| TC-007 | REQ-PD-02 | Default quantity is 1 | UI | Pos |
| TC-008 | REQ-PD-02 | Plus button increases quantity by 1 | UI | Pos |
| TC-009 | REQ-PD-02 | Minus at quantity 1 keeps quantity at 1 | UI | Bnd |
| TC-010 | REQ-PD-02 | Typing 0 is clamped to 1 | UI | Bnd |
| TC-011 | REQ-PD-02 | Typing above 999,999,999 is clamped to the maximum | UI | Bnd |
| TC-012 | REQ-PD-03 | Add to cart shows "Product added to shopping cart." and cart count updates | UI | Pos |
| TC-013 | REQ-PD-03 | API: create cart, add item, GET cart shows item and quantity | API | Pos |
| TC-014 | REQ-CO-01 | Changing quantity recalculates line total and cart total | UI, API | Pos |
| TC-015 | REQ-CO-02 | Deleting an item removes it and recalculates the total | UI, API | Pos |
| TC-016 | REQ-CO-02 | Empty cart shows "Your shopping cart is empty" | UI | Pos |
| TC-017 | REQ-CO-03 | Guest sees login step after Proceed | UI | Pos |
| TC-018 | REQ-CO-03 | Logged-in user sees "already signed in as" message | UI | Pos |
| TC-019 | REQ-CO-04 | Empty required address field disables Proceed | UI | Neg |
| TC-020 | REQ-CO-04 | Address is pre-filled for a logged-in user | UI | Pos |
| TC-021 | REQ-CO-05 | Payment dropdown offers exactly the five methods | UI | Pos |
| TC-022 | REQ-CO-06 | Credit card with past expiry shows "Expiration date must be in the future." | UI | Neg |
| TC-023 | REQ-CO-06 | Credit card with wrong number format is rejected | UI | Neg |
| TC-024 | REQ-CO-06 | Bank transfer account number accepts digits only | UI | Neg |
| TC-025 | REQ-CO-06 | Buy Now Pay Later offers 3, 6, 9, 12 installments | UI | Pos |
| TC-026 | REQ-CO-08 | Full checkout with Cash on Delivery shows an invoice number | UI | E2E |
| TC-027 | REQ-CO-08 | DB: invoice row exists with correct total and line items after order | DB | Pos |
| TC-028 | REQ-CO-08 | Cart is empty after a successful order | UI, API | Pos |
| TC-029 | REQ-REG-01 | API: valid registration returns 201 and the user exists in DB | API, DB | Pos |
| TC-030 | REQ-REG-01 | UI: valid registration redirects to login | UI | Pos |
| TC-031 | REQ-REG-03 | Registering an existing email shows "Email is already in use." | UI, API | Neg |
| TC-032 | REQ-REG-02 | Passwords failing each rule are rejected: under 8 chars, no uppercase, no digit, no special | API | Neg |
| TC-033 | REQ-REG-01 | Email of 256 characters accepted, 257 rejected | API | Bnd |
| TC-034 | REQ-AUTH-01 | Customer login redirects to /account | UI, API | Pos |
| TC-035 | REQ-AUTH-01 | Admin login redirects to /admin/dashboard | UI | Pos |
| TC-036 | REQ-AUTH-02 | Wrong password shows "Invalid email or password" | UI, API | Neg |
| TC-037 | REQ-AUTH-03 | Third failed attempt locks a throwaway user; API returns HTTP 423 | API | Neg |
| TC-038 | REQ-AUTH-03 | Admin is never locked after repeated failures | API | Neg |
| TC-039 | REQ-AUTH-05 | Protected endpoint without token returns 401 | API | Sec |
| TC-040 | REQ-AUTH-05 | Invalid or expired token returns 401 | API | Sec |
| TC-041 | REQ-INV-01 | Invoice list shows the user's invoices | UI, API | Pos |
| TC-042 | REQ-INV-01 | Invoice detail shows items, address and payment method | UI, API | Pos |
| TC-043 | REQ-INV-01 | Another user's invoice returns "not found" | API | Sec |
| TC-044 | REQ-SEC-01 | Customer token calling an admin endpoint is rejected (401 or 403) | API | Sec |
| TC-045 | REQ-SEC-01 | Customer cannot read another user's data by changing an ID | API | Sec |

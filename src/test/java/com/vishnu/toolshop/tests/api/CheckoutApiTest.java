package com.vishnu.toolshop.tests.api;

import com.vishnu.toolshop.api.AuthApi;
import com.vishnu.toolshop.api.CartApi;
import com.vishnu.toolshop.api.CheckoutApi;
import com.vishnu.toolshop.api.ProductsApi;
import com.vishnu.toolshop.api.TestData;
import com.vishnu.toolshop.config.ConfigReader;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.oneOf;

public class CheckoutApiTest {


    private String customerToken(){
        return AuthApi.token(ConfigReader.get("customer.email"),ConfigReader.get("customer.password"));
    }


    private String cartWithOneItem(){
        String cartId= CartApi.create().then().statusCode(201).extract().path("id");
        String productId= ProductsApi.list(Map.of()).then().statusCode(200).extract().path("data[0].id");
        CartApi.addItem(cartId,productId,1).then().statusCode(oneOf(200,201));
        return cartId;
    }

    @Test(groups={"api","smoke"})
    public void loggedInCustomerCanCreateInvoice() {
        String cartId = cartWithOneItem();

        CheckoutApi.createInvoice(customerToken(), TestData.invoice(cartId, "cash-on-delivery"))
                .then()
                .statusCode(oneOf(200, 201))
                .body("invoice_number", notNullValue());
    }

    @Test(groups="api")
    public void invoiceWithoutLoginIsRejected(){
        String cartId=cartWithOneItem();
        CheckoutApi.createInvoiceWithoutToken(TestData.invoice(cartId,"cash-on-delivery"))
                .then()
                .statusCode(401);
    }

    @Test(groups = "api")
    public void invoiceMissingBillingCityIsRejected() {
        String cartId = cartWithOneItem();
        Map<String, Object> body = new HashMap<>(TestData.invoice(cartId, "cash-on-delivery"));
        body.remove("billing_city");

        CheckoutApi.createInvoice(customerToken(), body)
                .then()
                .statusCode(422);
    }

    @Test(groups = "api")
    public void invoiceWithUnknownPaymentMethodIsRejected() {
        String cartId = cartWithOneItem();

        CheckoutApi.createInvoice(customerToken(), TestData.invoice(cartId, "bitcoin"))
                .then()
                .statusCode(422);
    }

    @Test(groups = "api")
    public void cashOnDeliveryPaymentCheckSucceeds() {
        CheckoutApi.checkPayment(Map.of(
                        "payment_method", "cash-on-delivery",
                        "payment_details", Map.of()))
                .then()
                .statusCode(200);
    }



}

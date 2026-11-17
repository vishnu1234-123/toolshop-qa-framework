package com.vishnu.toolshop.api;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;


public class CheckoutApi {

    public static Response checkPayment(Map<String,Object> body){
        return given().spec(ApiSpec.base())
                .body(body)
                .when().post("/payment/check");
    }

    public static Response createInvoice(String token,Map<String,Object> body){
        return given().spec(ApiSpec.base())
                .auth().oauth2(token)
                .body(body)
                .when().post("/invoices");
    }

    public static Response createInvoiceWithoutToken(Map<String,Object> body){
        return given().spec(ApiSpec.base())
                .body(body)
                .when().post("/invoices");
    }
}

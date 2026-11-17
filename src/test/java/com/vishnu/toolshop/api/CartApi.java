package com.vishnu.toolshop.api;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class CartApi {

    public static Response create(){
        return given().spec(ApiSpec.base()).when().post("/carts");
    }

    public static Response addItem(String cartId,String productId,int quantity){
        return given().spec(ApiSpec.base()).body(Map.of("product_id",productId,"quantity",quantity))
                .when().post("/carts/{id}",cartId);

    }

    public static Response get(String cartId){
        return given().spec(ApiSpec.base()).when().get("/carts/{cartId}",cartId);
    }

    public static Response updateQuantity(String cartId,String productId,int quantity){
        return given().spec(ApiSpec.base())
                .body(Map.of("product_id",productId,"quantity",quantity))
                .when().put("/carts/{cartId}/product/quantity",cartId);
    }

    public static Response removeProduct(String cartId,String productId){
        return given().spec(ApiSpec.base())
                .when().delete("/carts/{cartId}/product/{productId}",cartId,productId);
    }
}

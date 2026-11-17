package com.vishnu.toolshop.api;


import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProductsApi {

    public static Response list(Map<String,Object> queryParams){
        return given().spec(ApiSpec.base())
                .queryParams(queryParams)
                .when().get("/products");
    }

    public static Response search(String term){
        return given().spec(ApiSpec.base())
                .queryParams("q",term)
                .when().get("/products/search");
    }

    public static Response byId(String id){
        return given().spec(ApiSpec.base())
                .when().get("/products/{id}",id);
    }
}

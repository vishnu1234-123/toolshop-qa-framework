package com.vishnu.toolshop.api;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthApi {

    public static Response login(String email, String password){
        return given().spec(ApiSpec.base()).body(Map.of("email",email,"password",password)).when().post("/users/login");
    }

    public static String token(String email,String password){
        return login(email,password).then().statusCode(200).extract().path("access_token");
    }
}

package com.vishnu.toolshop.api;

import io.restassured.response.Response;
import org.openqa.selenium.devtools.v130.fetch.model.AuthChallengeResponse;

import static io.restassured.RestAssured.given;

public class UsersApi {

    public static Response me(String token){
        return given().spec(ApiSpec.base())
                .auth().oauth2(token)
                .when().get("/users/me");
    }

    public static Response meWithoutToken(){
        return given().spec(ApiSpec.base())
                .when().get("/users/me");
    }

    public static Response list(String token){
        return given().spec(ApiSpec.base())
                .auth().oauth2(token)
                .when().get("/users");
    }

    public static Response register(java.util.Map<String,Object> body){
        return given().spec(ApiSpec.base())
                .body(body)
                .when().post("users/register");
    }
}

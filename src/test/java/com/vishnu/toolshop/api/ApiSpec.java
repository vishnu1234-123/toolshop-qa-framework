package com.vishnu.toolshop.api;

import com.vishnu.toolshop.config.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;


public class ApiSpec {

    public static RequestSpecification base(){
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("api.base.url"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}

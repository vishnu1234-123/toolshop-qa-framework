package com.vishnu.toolshop.tests.api;

import com.vishnu.toolshop.api.AuthApi;
import com.vishnu.toolshop.api.ProductsApi;
import com.vishnu.toolshop.config.ConfigReader;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SchemaApiTest {

    @Test(groups = "api")
    public void loginResponseMatchesSchema(){
        AuthApi.login(ConfigReader.get("customer.email"),ConfigReader.get("customer.password"))
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/login-schema.json"));

    }

    @Test(groups = "api")
    public void productListMatchesSchema(){
        ProductsApi.list(Map.of())
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/product-list-schema.json"));

    }

    @Test(groups = "api")
    public void singleProductMatchesSchema(){
        String id=ProductsApi.list(Map.of())
                .then().statusCode(200)
                .extract().path("data[0].id");

        ProductsApi.byId(id)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/product-schema.json"));
    }
}

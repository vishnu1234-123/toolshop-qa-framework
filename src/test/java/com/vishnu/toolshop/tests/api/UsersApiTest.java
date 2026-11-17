package com.vishnu.toolshop.tests.api;

import com.vishnu.toolshop.api.AuthApi;
import com.vishnu.toolshop.api.UsersApi;
import com.vishnu.toolshop.config.ConfigReader;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class UsersApiTest {

    private String customerToken(){
        return AuthApi.token(ConfigReader.get("customer.email"),ConfigReader.get("customer.password"));
    }

    @Test(groups = "api")
    public void validTokenReturnsOwnProfile(){
        UsersApi.me(customerToken()).then().statusCode(200)
                .body("email",equalTo(ConfigReader.get("customer.email")));
    }

    @Test(groups = "api")
    public void missingTokenIsRejected(){
        UsersApi.meWithoutToken()
                .then()
                .statusCode(401);
    }

    @Test(groups = "api")
    public void garbageTokenIsRejected(){
        UsersApi.me("not.a.real.token")
                .then()
                .statusCode(401);
    }

    @Test(groups = "api")
    public void customerCannotListUsers(){
        UsersApi.list(customerToken())
                .then()
                .statusCode(403)
                .body("message",equalTo("Forbidden"));
    }

    @Test(groups = "api")
    public void adminCanListUsers(){
        String adminToken=AuthApi.token(ConfigReader.get("admin.email"),ConfigReader.get("admin.password"));

        UsersApi.list(adminToken).then().statusCode(200);
    }




}

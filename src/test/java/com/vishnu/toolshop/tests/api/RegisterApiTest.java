package com.vishnu.toolshop.tests.api;

import com.vishnu.toolshop.api.AuthApi;
import com.vishnu.toolshop.api.TestData;
import com.vishnu.toolshop.api.UsersApi;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.notNullValue;

public class RegisterApiTest {

    @Test(groups = "api")
    public void newUserCanRegisterAndLogin(){
        String email=TestData.uniqueEmail();

        UsersApi.register(TestData.newUser(email,TestData.VALID_PASSWORD))
                .then()
                .statusCode(201)
                .body("id",notNullValue());

        AuthApi.login(email,TestData.VALID_PASSWORD)
                .then()
                .statusCode(200)
                .body("access_token",notNullValue());
    }
}

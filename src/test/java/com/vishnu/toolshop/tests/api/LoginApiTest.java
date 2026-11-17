package com.vishnu.toolshop.tests.api;
import com.vishnu.toolshop.api.AuthApi;
import com.vishnu.toolshop.config.ConfigReader;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class LoginApiTest {

    @Test(groups = {"api","smoke"})
    public void validLoginReturnBearerToken(){
        AuthApi.login(ConfigReader.get("customer.email"),ConfigReader.get("customer.password"))
                .then()
                .statusCode(200)
                .body("access_token",not(emptyOrNullString()))
                .body("token_type",equalTo("bearer"))
                .body("expires_in",equalTo(300));

    }

    @Test(groups = "api")
    public void unknownUserIsRejected(){
        AuthApi.login("nobody@example.com","whatever")
                .then()
                .statusCode(401)
                .body("error",equalTo("Unauthorized"));
    }


}

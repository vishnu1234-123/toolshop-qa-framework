package com.vishnu.toolshop.tests.api;

import com.vishnu.toolshop.api.AuthApi;
import com.vishnu.toolshop.api.TestData;
import com.vishnu.toolshop.api.UsersApi;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;


public class LockoutApiTest {

    private String throwawayUser() {
        String email = TestData.uniqueEmail();
        UsersApi.register(TestData.newUser(email, TestData.VALID_PASSWORD))
                .then().statusCode(201);
        return email;
    }

    private void failLogins(String email, int times) {
        for (int i = 1; i <= times; i++) {
            AuthApi.login(email, "wrong-password").then().statusCode(401);
        }
    }

    @Test(groups = "api")
    public void twoFailedLoginsDoNotLockTheAccount() {
        String email = throwawayUser();
        failLogins(email, 2);
        AuthApi.login(email, TestData.VALID_PASSWORD).then().statusCode(200).body("access_token", notNullValue());
    }

    @Test(groups = "api")
    public void fourthFailedLoginIsLocked() {
        String email = throwawayUser();
        failLogins(email, 3);

        AuthApi.login(email, "wrong-password")
                .then()
                .statusCode(423);
    }

    @Test(groups = "api")
    public void lockedAccountRejectsTheCorrectPassword() {
        String email = throwawayUser();
        failLogins(email, 3);

        AuthApi.login(email, TestData.VALID_PASSWORD)
                .then()
                .statusCode(423)
                .body("error", containsString("Account locked"));

    }


}

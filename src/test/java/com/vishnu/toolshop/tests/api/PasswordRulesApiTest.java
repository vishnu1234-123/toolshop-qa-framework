package com.vishnu.toolshop.tests.api;

import com.vishnu.toolshop.api.TestData;
import com.vishnu.toolshop.api.UsersApi;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;



public class PasswordRulesApiTest {

    @DataProvider(name="weakPasswords")
    public Object[][] weakPasswords(){
        return new Object[][]{
                {"7 characters (just below the minimum)", "Abcde1!", "at least 8 characters"},
                {"no uppercase letter", "abcdefg1!", "uppercase and one lowercase"},
                {"no lowercase letter", "ABCDEFG1!", "uppercase and one lowercase"},
                {"no number", "Abcdefgh!!", "at least one number"},
                {"no symbol", "Abcdefgh11", "at least one symbol"},
                {"empty password", "", "required"},
                {"password found in a data leak", "Abcdef1!", "data leak"}
        };
    }

    @DataProvider(name="validPasswords")
    public Object[][] validPasswords(){
        return new Object[][]{
                {"exactly 8 characters (the minimum)", "Kx9#mQ2v"},
                {"long password", "Welcome@2026x-with-extra-length"}
        };
    }

    @Test(groups = "api", dataProvider = "weakPasswords")
    public void weakPasswordIsRejected(String rule,String password,String expectedMessage){
        UsersApi.register(TestData.newUser(TestData.uniqueEmail(),password))
                .then()
                .statusCode(422)
                .body("password",hasItem(containsString(expectedMessage)));
    }

    @Test(groups = "api", dataProvider = "validPasswords")
    public void validPasswordIsAccepted(String rule, String password) {
        UsersApi.register(TestData.newUser(TestData.uniqueEmail(), password))
                .then()
                .statusCode(201);
    }

}

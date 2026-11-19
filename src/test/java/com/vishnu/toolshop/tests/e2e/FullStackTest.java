package com.vishnu.toolshop.tests.e2e;

import com.vishnu.toolshop.api.MailApi;
import com.vishnu.toolshop.api.TestData;
import com.vishnu.toolshop.api.UsersApi;
import com.vishnu.toolshop.db.DbClient;
import com.vishnu.toolshop.pages.LoginPage;
import com.vishnu.toolshop.tests.BaseTest;
import com.vishnu.toolshop.driver.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class FullStackTest extends BaseTest{


    @Test(groups = "e2e")
    public void newCustomerIsRegisteredStoredCanLogInAndIsEmailed(){
        String email= TestData.uniqueEmail();

        UsersApi.register(TestData.newUser(email,TestData.VALID_PASSWORD)).then().statusCode(201);

        List<Map<String,Object>> rows= DbClient.query(
                "SELECT role,failed_login_attempts,password FROM users WHERE email=?",email
        );

        Assert.assertEquals(rows.size(),1,"customer row misssing");
        Assert.assertEquals(rows.get(0).get("role"), "user");
        Assert.assertEquals(((Number) rows.get(0).get("failed_login_attempts")).intValue(), 0);
        Assert.assertTrue(String.valueOf(rows.get(0).get("password")).startsWith("$2y$"));

        LoginPage login=new LoginPage(DriverFactory.getDriver());
        login.open();
        login.login(email,TestData.VALID_PASSWORD);
        Assert.assertTrue(login.redirectedToAccount(), "new customer was not taken to the account page");

        Map<String,Object> message= MailApi.waitForMessage(email,"Register",180);
        Assert.assertTrue(String.valueOf(message.get("sender")).contains("hello@practicesoftwaretesting.com"));
    }
}

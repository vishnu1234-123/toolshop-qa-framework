package com.vishnu.toolshop.tests.mail;

import com.vishnu.toolshop.api.MailApi;
import com.vishnu.toolshop.api.TestData;
import com.vishnu.toolshop.api.UsersApi;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class MailApiTest {

    private String email;
    private Map<String,Object> message;
    private String body;

    @BeforeClass(alwaysRun = true)
    public void registerAndWaitForTheEmail(){
        email=TestData.uniqueEmail();
        UsersApi.register(TestData.newUser(email,TestData.VALID_PASSWORD)).then().statusCode(201);

        message=MailApi.waitForMessage(email,"Register",600);
        body=MailApi.plainBody(message.get("id"));
    }

    @Test(groups = "mail")
    public void registrationEmailComesFromTheShop(){
        Assert.assertTrue(String.valueOf(message.get("sender")).contains("hello@practicesoftwaretesting.com"),"unexpected sender : "+message.get("sender"));
    }

    @Test(groups = "mail")
    public void emailGreetsTheNewUserByName(){
        Assert.assertTrue(body.contains("Hello Test User,"),"greeting not found in:\n"+body);
    }

    @Test(groups = "mail")
    public void emailRepeatsTheLoginProcess(){
        Assert.assertTrue(body.contains(email),"login address missing from the email");
    }

    @Test(groups = {"mail","known-issue"})
    public void emailDoesNotLeakThePasswordHash(){
        Assert.assertFalse(body.contains("$2y$"),"the email contains the password hash:\n"+body);
    }
}

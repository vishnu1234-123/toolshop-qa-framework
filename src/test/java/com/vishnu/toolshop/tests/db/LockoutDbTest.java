package com.vishnu.toolshop.tests.db;

import com.vishnu.toolshop.api.AuthApi;
import com.vishnu.toolshop.api.TestData;
import com.vishnu.toolshop.api.UsersApi;
import com.vishnu.toolshop.db.DbClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LockoutDbTest {

    private String throwawayUser(){
        String email=TestData.uniqueEmail();
        UsersApi.register(TestData.newUser(email,TestData.VALID_PASSWORD)).then().statusCode(201);
        return email;
    }

    private int failedAttempts(String email){
        Object value=DbClient.scalar("SELECT failed_login_attempts FROM users WHERE email =?",email);
        return ((Number)value).intValue();
    }

    @Test(groups = "db")
    public void newUserStartsWithZeroFailedAttempts(){
        String email=throwawayUser();
        Assert.assertEquals(failedAttempts(email),0);
    }

    @Test(groups = "db")
    public void eachWrongPasswordIsCountedInTheDatabase(){
        String email=throwawayUser();
        AuthApi.login(email,"wrong-password").then().statusCode(401);
        Assert.assertEquals(failedAttempts(email),1);

        AuthApi.login(email,"wrong-password").then().statusCode(401);
        Assert.assertEquals(failedAttempts(email),2);
    }

    @Test(groups = "db")
    public void counterStopsAtThreeOnceTheAccountIsLocked(){
        String email=throwawayUser();
        for(int i=0;i<3;i++){
            AuthApi.login(email,"wrong-password").then().statusCode(401);
        }
        Assert.assertEquals(failedAttempts(email),3);
        AuthApi.login(email,"wrong-password").then().statusCode(423);
        Assert.assertEquals(failedAttempts(email), 3, "counter kept growing after the lock");
    }
}

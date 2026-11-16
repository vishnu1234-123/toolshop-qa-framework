package com.vishnu.toolshop.tests.ui;

import com.vishnu.toolshop.config.ConfigReader;
import com.vishnu.toolshop.pages.LoginPage;
import com.vishnu.toolshop.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest{

    @Test(description = "TC-034 | REQ-AUTH-01 | Customer login redirects to /account",groups = "smoke")
    public void customerLoginRedirectsToAccount(){
        LoginPage login=new LoginPage(driver).open().login(ConfigReader.get("customer.email"),ConfigReader.get("customer.password"));
        Assert.assertTrue(login.redirectedToAccount());
    }

    @Test(description = "TC-036 | REQ-AUTH-02 | Wrong credentials show 'Invalid email or password'")
    public void invalidCredentialsShowError(){
        LoginPage login=new LoginPage(driver).open().login("nobody@example.com", "Wrong-Pass-123");
        Assert.assertTrue(login.errorMessage().contains("Invalid email or password"));
    }
}

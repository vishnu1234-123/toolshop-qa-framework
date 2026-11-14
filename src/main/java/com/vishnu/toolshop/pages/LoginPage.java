package com.vishnu.toolshop.pages;

import com.vishnu.toolshop.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage{
    private static final By FORM = byTest("login-form");
    private static final By EMAIL = byTest("email");
    private static final By PASSWORD = byTest("password");
    private static final By SUBMIT = byTest("login-submit");
    private static final By ERROR = By.xpath("//*[contains(text(),'Invalid email or password')]");

    public LoginPage(WebDriver driver){
        super(driver);
    }

    public LoginPage open(){
        driver.get(ConfigReader.get("ui.base.url")+"/auth/login");
        visible(FORM);
        return this;
    }

    public LoginPage login(String email,String password){
        type(EMAIL,email);
        type(PASSWORD,password);
        click(SUBMIT);
        return this;
    }

    public boolean redirectedToAccount(){
        return wait.until(ExpectedConditions.urlContains("/account"));
    }

    public String errorMessage(){
        return text(ERROR);
    }

}

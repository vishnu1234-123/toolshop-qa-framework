package com.vishnu.toolshop.tests;

import com.vishnu.toolshop.config.ConfigReader;
import com.vishnu.toolshop.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.service.DriverCommandExecutor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.sql.Driver;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        driver= DriverFactory.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        DriverFactory.quitDriver();
    }

    protected void openApp(){
        driver.get(ConfigReader.get("ui.base.url"));
    }

}

package com.vishnu.toolshop.listeners;

import com.vishnu.toolshop.driver.DriverFactory;
import org.openqa.selenium.By;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


public class TestListener implements ITestListener {

    public void onTestFailure(ITestResult result){
        try{
            WebDriver driver=DriverFactory.getDriver();
            byte[] png=((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment("Screenshot on failure","image/png",new ByteArrayInputStream(png),"png");
            Allure.addAttachment("Page URL",driver.getCurrentUrl());
            String bodyText=driver.findElement(By.tagName("body")).getText();
            Allure.addAttachment("Page text",bodyText);
            Path dir=Path.of("target","screenshots");
            Files.createDirectories(dir);
            Files.write(dir.resolve(result.getName()+".png"),png);
            Files.writeString(dir.resolve(result.getName()+".txt"),driver.getCurrentUrl()+"\n\n"+bodyText);
        }catch (Exception e){
            System.err.println("Could not capture screenshot: "+e.getMessage());
        }

    }
}

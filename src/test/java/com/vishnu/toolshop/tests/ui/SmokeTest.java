package com.vishnu.toolshop.tests.ui;
import com.vishnu.toolshop.config.ConfigReader;
import com.vishnu.toolshop.tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class SmokeTest extends BaseTest {

    @Test(description = "Home page loads and shows product cards (REQ-PO-01)",groups = "smoke")
    public void homePageShowsProducts(){
        openApp();

        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(ConfigReader.getInt("timeout.seconds")));
        List<WebElement> productNames=wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("[data-test='product-name']"),0));

        Assert.assertFalse(productNames.isEmpty(),"Expected at least one product card");
    }
}

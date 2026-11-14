package com.vishnu.toolshop.pages;
import com.vishnu.toolshop.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected  BasePage(WebDriver driver){
        this.driver=driver;
        this.wait=new WebDriverWait(driver,Duration.ofSeconds(ConfigReader.getInt("timeout.seconds")));
        this.wait.ignoring(StaleElementReferenceException.class);

    }

    protected static By byTest(String name){
        return By.cssSelector("[data-test='"+name+"']");
    }

    protected static BigDecimal money(String raw){
        return new BigDecimal(raw.replaceAll("[^0-9.]",""));
    }

    protected WebElement visible(By locator){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> allVisible(By locator){
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    protected void click(By locator){
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator,String text){
        WebElement element=visible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String text(By locator){
        return visible(locator).getText().trim();
    }
}



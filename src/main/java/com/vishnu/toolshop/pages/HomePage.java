package com.vishnu.toolshop.pages;

import com.vishnu.toolshop.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;
import java.util.List;

public class HomePage extends BasePage {

    private static final By CARD= By.cssSelector("[data-test^='product-0']");
    private static final By NAME= byTest("product-name");
    private static final By PRICE=byTest("product-price");

    public HomePage(WebDriver driver){
        super(driver);
    }

    private List<WebElement> cards(){
        return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(CARD,0));
    }


    public HomePage open(){
        driver.get(ConfigReader.get("ui.base.url"));
        cards();
        return this;
    }

    public int cardCount(){
        return cards().size();
    }

    public String nameOfProduct(int index){
        return cards().get(index).findElement(NAME).getText().trim();
    }

    public BigDecimal priceOfProduct(int index){
        String raw= cards().get(index).findElement(PRICE).getText();
        return new BigDecimal(raw.replaceAll("[^0-9.]", ""));
    }

    public ProductPage openProduct(int index){
        cards().get(index).findElement(NAME).click();
        return new ProductPage(driver);
    }

}

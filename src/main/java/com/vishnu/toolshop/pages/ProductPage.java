package com.vishnu.toolshop.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;

import java.math.BigDecimal;

public class ProductPage extends BasePage{

    private static final By NAME=byTest("product-name");
    private static final By UNIT_PRICE=byTest("unit-price");
    private static final By QUANTITY=byTest("quantity");
    private static final By INCREASE=byTest("increase-quantity");
    private static final By DECREASE=byTest("decrease-quantity");
    private static final By ADD_TO_CART=byTest("add-to-cart");
    private static final By ADDED_MESSAGE=By.xpath("//*[contains(text(),'Product added to shopping cart')]");

    public ProductPage(WebDriver driver) {
        super(driver);
        visible(NAME);
    }

    public String name(){
        return text(NAME);
    }

    public BigDecimal unitPrice(){
        return new BigDecimal(text(UNIT_PRICE).replaceAll("[^0-9.]",""));
    }

    public int quantity(){
        return Integer.parseInt(visible(QUANTITY).getAttribute("value"));
    }

    public ProductPage increaseQuantity(){
        click(INCREASE);
        return this;
    }

    public ProductPage decreaseQuantity(){
        click(DECREASE);
        return this;
    }

    public ProductPage addToCart(){
        click(ADD_TO_CART);
        return this;
    }

    public String addedMessage(){
        return text(ADDED_MESSAGE);
    }
}

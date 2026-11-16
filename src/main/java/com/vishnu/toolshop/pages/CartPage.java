package com.vishnu.toolshop.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import com.vishnu.toolshop.pages.CheckoutPage;

import java.math.BigDecimal;


public class CartPage extends BasePage {

    private static final By TITLE=byTest("product-title");
    private static final By QUANTITY=byTest("product-quantity");
    private static final By UNIT_PRICE=byTest("product-price");
    private static final By LINE_PRICE=byTest("line-price");
    private static final By CART_TOTAL=byTest("cart-total");
    private static final By DELETE=By.cssSelector("tr a.btn-danger");

    public CartPage(WebDriver driver) {
        super(driver);
        visible(CART_TOTAL);
    }

    public int itemCount(){
        return allVisible(TITLE).size();
    }

    public String titleAt(int index){
        return allVisible(TITLE).get(index).getText().trim();
    }

    public int quantityAt(int index){
        return Integer.parseInt(allVisible(QUANTITY).get(index).getAttribute("value"));
    }

    public BigDecimal unitPriceAt(int index){
        return money(allVisible(UNIT_PRICE).get(index).getText());
    }

    public BigDecimal linePriceAt(int index){
        return money(allVisible(LINE_PRICE).get(index).getText());
    }

    public BigDecimal total(){
        return money(text(CART_TOTAL));
    }

    public CartPage setQuantity(int index,int quantity){
        BigDecimal lineBefore=linePriceAt(index);
        BigDecimal totalBefore=total();
        WebElement input=allVisible(QUANTITY).get(index);
        input.click();
        input.sendKeys(Keys.END);
        int length=input.getAttribute("value").length();
        for (int i=0;i<length;i++){
            input.sendKeys(Keys.BACK_SPACE);
        }
        input.sendKeys(String.valueOf(quantity),Keys.TAB);
        wait.until(d->linePriceAt(index).compareTo(lineBefore)!=0 && total().compareTo(totalBefore)!=0);
        return this;

    }

    public CartPage deleteItem(int index){
        int countBefore=itemCount();
        BigDecimal totalBefore = total();
        wait.until(d->d.findElements(DELETE).size()>index);
        driver.findElements(DELETE).get(index).click();
        wait.until(d->itemCount()<countBefore && (itemCount()==0 || total().compareTo(totalBefore)!=0));
        return this;
    }

    public CheckoutPage proceedToCheckout(){
        click(byTest("proceed-1"));
        return new CheckoutPage(driver);
    }

}

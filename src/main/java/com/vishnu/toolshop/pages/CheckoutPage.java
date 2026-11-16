package com.vishnu.toolshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage{

    private static final By PROCEED_SIGN_IN=byTest("proceed-2");
    private static final By STREET=byTest("street");
    private static final By HOUSE_NUMBER=byTest("house_number");
    private static final By CITY=byTest("city");
    private static final By STATE=byTest("state");
    private static final By COUNTRY=byTest("country");
    private static final By POSTAL_CODE=byTest("postal_code");
    private static final By PROCEED_BILLING = byTest("proceed-3");
    private static final By PAYMENT_METHOD = byTest("payment-method");
    private static final By FINISH = byTest("finish");
    private static final By PAYMENT_SUCCESSFUL=By.xpath("//*[contains(text(),'success')]");
    private static final By CONFIRMATION =
            By.xpath("//*[contains(text(),'invoice number')]");

    public CheckoutPage(WebDriver driver){
        super(driver);
    }

    public CheckoutPage continueFromSignIn(){
        click(PROCEED_SIGN_IN);
        return this;
    }

    public CheckoutPage fillBillingAddress(String street,String houseNumber,String city,String state,String postalCode){
        type(STREET,street);
        type(HOUSE_NUMBER,houseNumber);
        type(CITY,city);
        type(STATE,state);
        chooseCountry();
        type(POSTAL_CODE,postalCode);
        return this;
    }

    public void chooseCountry(){
        WebElement country=visible(COUNTRY);
        if("select".equalsIgnoreCase(country.getTagName())){
            new Select(country).selectByValue("US");
        }else{
            country.clear();
            country.sendKeys("United States");
        }
    }
    public CheckoutPage continueToPayment(){
        click(PROCEED_BILLING);
        return this;
    }

    public CheckoutPage payWith(String methodText){
        new Select(visible(PAYMENT_METHOD)).selectByVisibleText(methodText);
        return this;
    }

    public CheckoutPage validateOrder(){
        click(FINISH);
        visible(PAYMENT_SUCCESSFUL);
        return this;
    }
    public CheckoutPage placeOrder(){
        click(FINISH);
        return this;
    }
    public String confirmationMessage(){
        return text(CONFIRMATION);
    }

    public String invoiceNumber(){
        String message=confirmationMessage();
        String marker="invoice number is ";
        int at = message.toLowerCase().indexOf(marker);
        if (at<0){
            throw new IllegalStateException("No invoice number in message: "+message);
        }
        return message.substring(at + marker.length()).replace(".", "").trim();
    }

}

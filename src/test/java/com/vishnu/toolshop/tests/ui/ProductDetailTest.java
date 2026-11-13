package com.vishnu.toolshop.tests.ui;

import com.vishnu.toolshop.pages.HomePage;
import com.vishnu.toolshop.pages.ProductPage;
import com.vishnu.toolshop.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;


public class ProductDetailTest extends BaseTest {

    private ProductPage openFirstProduct(){
       return new HomePage(driver).open().openProduct(0);
    }

    @Test(description = "TC-007 | REQ-PD-02 | Default quantity is 1")
    public void defaultQuantityIsOne(){
        Assert.assertEquals(openFirstProduct().quantity(),1);
    }

    @Test(description = "TC-008 | REQ-PD-02 | Plus button increases quantity by 1")
    public void plusIncreaseQuantity(){
        ProductPage product=openFirstProduct().increaseQuantity();
        Assert.assertEquals(product.quantity(),2);
    }

    @Test(description = "TC-009 | REQ-PD-02 | Minus at quantity 1 keeps quantity at 1")
    public void minusAtOneKeepsOne(){
        ProductPage product = openFirstProduct().decreaseQuantity();
        Assert.assertEquals(product.quantity(),1);
    }

    public void addToCartShowsSuccessMessage() {
        ProductPage product=openFirstProduct().addToCart();
        Assert.assertTrue(product.addedMessage().contains("Product added to shopping cart"));
    }





}

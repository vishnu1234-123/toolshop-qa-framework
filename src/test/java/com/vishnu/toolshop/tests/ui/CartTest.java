package com.vishnu.toolshop.tests.ui;

import com.vishnu.toolshop.pages.CartPage;
import com.vishnu.toolshop.pages.HomePage;
import com.vishnu.toolshop.pages.ProductPage;
import com.vishnu.toolshop.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class CartTest extends  BaseTest{

    @Test(description = "TC-014 | REQ-CO-01 | Changing quantity recalculates line and cart totals")
    public void changingQuantityRecalculatesTotals(){
        ProductPage product=new HomePage(driver).open().openProduct(0).addToCart();
        product.addedMessage();
        CartPage cart=product.goToCart();

        BigDecimal unit=cart.unitPriceAt(0);
        cart.setQuantity(0,3);
        BigDecimal expected = unit.multiply(BigDecimal.valueOf(3));

        Assert.assertEquals(cart.linePriceAt(0).compareTo(expected), 0, "Line price is wrong");
        Assert.assertEquals(cart.total().compareTo(expected), 0, "Cart total is wrong");

    }
}

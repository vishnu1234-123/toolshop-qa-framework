package com.vishnu.toolshop.tests.ui;

import com.vishnu.toolshop.api.StockedProducts;
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
        ProductPage product=new HomePage(driver).openProductById(StockedProducts.ids().get(0)).addToCart();
        product.addedMessage();
        CartPage cart=product.goToCart();

        BigDecimal unit=cart.unitPriceAt(0);
        cart.setQuantity(0,3);
        BigDecimal expected = unit.multiply(BigDecimal.valueOf(3));

        Assert.assertEquals(cart.linePriceAt(0).compareTo(expected), 0, "Line price is wrong");
        Assert.assertEquals(cart.total().compareTo(expected), 0, "Cart total is wrong");

    }

    @Test(description = "TC-015 | REQ-CO-02 | Deleting an item removes it and recalculates the total")
    public void deletingItemRemovesItAndRecalculatesTotal(){
        HomePage page=new HomePage(driver).open();
        String secondName=page.nameOfProduct(1);

        ProductPage firstProduct=page.openProductById(StockedProducts.ids().get(0)).addToCart();
        firstProduct.addedMessage();
        ProductPage secondProduct=new HomePage(driver).openProductById(StockedProducts.ids().get(1)).addToCart();
        secondProduct.addedMessage();

        CartPage cart= secondProduct.goToCart();
        Assert.assertEquals(cart.itemCount(),2,"Cart should hold two items before deleting");
        BigDecimal remainingUnit=cart.unitPriceAt(1);

        cart.deleteItem(0);

        Assert.assertEquals(cart.itemCount(), 1, "One item should remain");
        Assert.assertEquals(cart.titleAt(0), secondName, "The wrong item was removed");
        Assert.assertEquals(cart.total().compareTo(remainingUnit), 0, "Cart total was not recalculated");

    }
}

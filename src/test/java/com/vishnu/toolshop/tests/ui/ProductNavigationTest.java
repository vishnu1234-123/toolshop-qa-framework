package com.vishnu.toolshop.tests.ui;

import com.vishnu.toolshop.pages.HomePage;
import com.vishnu.toolshop.pages.ProductPage;
import com.vishnu.toolshop.tests.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class ProductNavigationTest extends BaseTest {
    @Test(description = "TC-046 | REQ-PO-01 | Clicking a product card opens that product's detail page")
    @Feature("Product browsing")
    @Story("REQ-PO-01 Product grid and navigation")
    @Severity(SeverityLevel.BLOCKER)
    public void clickingCardOpensMatchingDetailPage(){
        HomePage home=new HomePage(driver).open();
        String cardName=home.nameOfProduct(0);
        BigDecimal cardPrice=home.priceOfProduct(0);
        ProductPage product=home.openProduct(0);

        Assert.assertEquals(product.name(),cardName,"Detail page shows a different product");
        Assert.assertEquals(product.unitPrice().compareTo(cardPrice),0,"Detail price differs from the card price");
    }
}

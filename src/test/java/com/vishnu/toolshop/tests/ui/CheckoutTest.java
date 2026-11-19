package com.vishnu.toolshop.tests.ui;

import com.vishnu.toolshop.api.StockedProducts;
import com.vishnu.toolshop.config.ConfigReader;
import com.vishnu.toolshop.pages.CheckoutPage;
import com.vishnu.toolshop.pages.HomePage;
import com.vishnu.toolshop.pages.LoginPage;
import com.vishnu.toolshop.pages.ProductPage;
import com.vishnu.toolshop.tests.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;


public class CheckoutTest extends BaseTest {
    @Test(description = "TC-026 | REQ-CO-08 | Full checkout with Cash on Delivery shows an invoice number",groups = "smoke")
    @Feature("Checkout")
    @Story("REQ-CO-08 Successful order")
    @Severity(SeverityLevel.BLOCKER)
    public void CheckoutWithCashOnDelivery(){
        LoginPage login =new LoginPage(driver).open().login(ConfigReader.get("customer.email"),ConfigReader.get("customer.password"));
        Assert.assertTrue(login.redirectedToAccount(),"Login did not reach the account page");

        ProductPage product = new HomePage(driver).openProductById(StockedProducts.ids().get(0)).addToCart();
        product.addedMessage();

        CheckoutPage checkout=product.goToCart().proceedToCheckout();

        checkout.continueFromSignIn().fillBillingAddress("Test Street", "12", "Test City", "Test State", "12345").continueToPayment().payWith("Cash on Delivery").validateOrder().placeOrder();

        Assert.assertFalse(checkout.invoiceNumber().isBlank(),"Expected an invoice number");
    }
}

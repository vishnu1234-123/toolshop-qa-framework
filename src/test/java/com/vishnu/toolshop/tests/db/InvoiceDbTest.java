package com.vishnu.toolshop.tests.db;

import com.vishnu.toolshop.api.AuthApi;
import com.vishnu.toolshop.api.CartApi;
import com.vishnu.toolshop.api.CheckoutApi;
import com.vishnu.toolshop.api.ProductsApi;
import com.vishnu.toolshop.api.TestData;
import com.vishnu.toolshop.config.ConfigReader;
import com.vishnu.toolshop.db.DbClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.oneOf;


public class InvoiceDbTest {

    @Test(groups = "db")
    public void invoiceCreatedThroughTheApiIsStoredCorrectly(){
        String email= ConfigReader.get("customer.email");
        String token= AuthApi.token(email,ConfigReader.get("customer.password"));

        String productId= ProductsApi.list(Map.of()).then().statusCode(200).extract().path("data[0].id");
        BigDecimal price=new BigDecimal(ProductsApi.byId(productId).then().statusCode(200).extract().jsonPath().getString("price"));
        String cartId= CartApi.create().then().statusCode(201).extract().path("id");
        CartApi.addItem(cartId,productId,2).then().statusCode(oneOf(200,201));

        String  invoiceNumber= CheckoutApi.createInvoice(token, TestData.invoice(cartId,"cash-on-delivery")).then().statusCode(oneOf(200,201))
                .extract().path("invoice_number");

        List<Map<String, Object>> invoices= DbClient.query(
                "SELECT * FROM invoices WHERE invoice_number=?",invoiceNumber
        );
        Assert.assertEquals(invoices.size(),1,"expected one invoice for " + invoiceNumber);
        Map<String,Object> invoice=invoices.get(0);

        Object customerId=DbClient.scalar("SELECT id FROM users WHERE email =?",email);
        Assert.assertEquals(invoice.get("user_id"),customerId,"invoice belongs to a different user");

        BigDecimal expectedTotal=price.multiply(BigDecimal.valueOf(2));
        Assert.assertEquals(new BigDecimal(String.valueOf(invoice.get("subtotal"))).compareTo(expectedTotal),0,"subtotal is not price x quantity");
        Assert.assertEquals(new BigDecimal(String.valueOf(invoice.get("total"))).compareTo(expectedTotal),0,"total is not price x quantity");

        List<Map<String,Object>> lines=DbClient.query(
                "SELECT * FROM invoice_items WHERE invoice_id=?",invoice.get("id")
        );
        Assert.assertEquals(lines.size(), 1, "expected one invoice line");
        Map<String,Object> line=lines.get(0);
        Assert.assertEquals(line.get("product_id"), productId);
        Assert.assertEquals(((Number) line.get("quantity")).intValue(), 2);
        Assert.assertEquals(new BigDecimal(String.valueOf(line.get("unit_price"))).compareTo(price), 0,
                "line unit price differs from the product price");

    }
}

package com.vishnu.toolshop.tests.api;

import com.vishnu.toolshop.api.CartApi;
import com.vishnu.toolshop.api.ProductsApi;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.oneOf;


public class CartApiTest {

    private String newCartId(){
        return CartApi.create().then().statusCode(201).extract().path("id");
    }

    private String anyProductId(){
        return ProductsApi.list(Map.of()).then().statusCode(200).extract().path("data[0].id");
    }
    @Test(groups = "api")
    public void newCartHasAnId(){
        CartApi.create().then().statusCode(201).body("id",notNullValue());
    }
    @Test(groups = "api")
    public void addedItemAppearsInCart(){
        String cartId=newCartId();
        String productId=anyProductId();

        CartApi.addItem(cartId, productId, 2)
                .then().statusCode(oneOf(200, 201));

        CartApi.get(cartId).then().statusCode(200)
                .body("cart_items",hasSize(1))
                .body("cart_items[0].product_id", equalTo(productId))
                .body("cart_items[0].quantity", equalTo(2));
    }
    @Test(groups = "api")
    public void updatingQuantityChangesTheCart(){
        String cartId=newCartId();
        String productId=anyProductId();

        CartApi.addItem(cartId,productId,1).then().statusCode(oneOf(200,201));

        CartApi.updateQuantity(cartId,productId,3)
                .then().statusCode(oneOf(200,204));

        CartApi.get(cartId)
                .then()
                .statusCode(200)
                .body("cart_items[0].quantity", equalTo(3));

    }
    @Test(groups = "api")
    public void removingTheProductEmptiesTheCart(){
        String cartId=newCartId();
        String productId=anyProductId();

        CartApi.addItem(cartId,productId,1).then().statusCode(oneOf(200,201));

        CartApi.removeProduct(cartId,productId)
                .then().statusCode(oneOf(200,204));
        CartApi.get(cartId)
                .then()
                .statusCode(200)
                .body("cart_items",empty());
    }
}

package com.vishnu.toolshop.tests.api;

import com.vishnu.toolshop.api.ApiSpec;
import com.vishnu.toolshop.api.ProductsApi;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;


public class ProductsApiTest {

    @Test(groups = "api")
    public void listReturnsProducts(){
        ProductsApi.list(Map.of())
                .then()
                .body("current_page",equalTo(1))
                .body("data",not(empty()))
                .body("data.id",everyItem(notNullValue()))
                .body("data.name",everyItem(notNullValue()))
                .body("data.price",everyItem(notNullValue()));
    }

    @Test(groups = "api")
    public void searchReturnMatchingProducts(){
        List<String> names=ProductsApi.search("pli").then().statusCode(200)
                .extract().jsonPath().getList("data.name");

        Assert.assertFalse(names.isEmpty(),"search returns no products");
        Assert.assertTrue(names.stream().anyMatch(n->n.toLowerCase().contains("pli")),"no product name contains 'pli' : "+names);
    }

    @Test(groups = "api")
    public void searchWithNoMatchProducts(){
        ProductsApi.search("zzzzzz").then().statusCode(200).body("data",empty());

    }

    @Test(groups = "api")
    public void productByIdReturnsThatProduct(){
        String id=ProductsApi.list(Map.of())
                .then().statusCode(200)
                .extract().path("data[0].id");

        ProductsApi.byId(id)
                .then()
                .statusCode(200)
                .body("id",equalTo(id))
                .body("data",not(empty()))
                .body("name",notNullValue())
                .body("price",notNullValue());
    }

    @Test(groups = "api")
    public void unknownProductIdReturns404(){
        ProductsApi.byId("INVALID-ID").then().statusCode(404).body("message",equalTo("Requested item not found"));
    }

    @Test(groups = "api")
    public void sortByPriceAscendingReturnsIncreasingPrices(){
        List<Double> prices=ProductsApi.list(Map.of("sort","price,asc"))
                .then().statusCode(200)
                .extract().jsonPath().getList("data.price",Double.class);

        List<Double> expected=new ArrayList<>(prices);
        Collections.sort(expected);
        Assert.assertEquals(prices,expected,"prices are not in ascending order");

    }

    @Test(groups = "api")
    public void secondPageDiffersFromFirstPage(){
        List<String> names=ProductsApi.list(Map.of("page",1))
                .then()
                .statusCode(200)
                .extract().path("data.id");

        List<String> names2=ProductsApi.list(Map.of("page",2))
                .then()
                .statusCode(200)
                .body("current_page",equalTo(2))
                .extract().path("data.id");

        Assert.assertNotEquals(names,names2,"page not chnaged");

    }


}

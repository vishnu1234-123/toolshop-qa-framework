package com.vishnu.toolshop.api;

import java.util.List;
import java.util.Map;

public class StockedProducts {

    public static List<String> ids(){
        List<String> ids=ProductsApi.list(Map.of()).jsonPath().getList("data.findAll {it.in_stock == true}.id");
        if(ids.size()<2){
            throw new IllegalStateException("Need at least 2 in-stock products, found "+ids.size());
        }
        return ids;
    }
}

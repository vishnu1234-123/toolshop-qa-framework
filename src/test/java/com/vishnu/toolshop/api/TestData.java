package com.vishnu.toolshop.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TestData {

    public static final String VALID_PASSWORD="Welcome@2026x";

    public static String uniqueEmail(){
        return "qa."+UUID.randomUUID().toString().substring(0,8)+"@example.com";
    }

    public static Map<String, Object> newUser(String email,String password){
        Map<String, Object> address = new HashMap<>();
        address.put("street", "Test Street 12");
        address.put("city", "Test City");
        address.put("state", "Test State");
        address.put("country", "US");
        address.put("postal_code", "12345");

        Map<String, Object> user = new HashMap<>();
        user.put("first_name", "Test");
        user.put("last_name", "User");
        user.put("dob", "1990-01-01");
        user.put("phone", "0987654321");
        user.put("email", email);
        user.put("password", password);
        user.put("address", address);
        return user;
    }

    public static Map<String,Object> invoice(String cartId,String paymentMethod){
        Map<String, Object> invoice = new HashMap<>();
        invoice.put("billing_street", "Alan Summit");
        invoice.put("billing_city", "Altenwerthburgh");
        invoice.put("billing_state", "Tennessee");
        invoice.put("billing_country", "AS");
        invoice.put("billing_postal_code", "1");
        invoice.put("payment_method", paymentMethod);
        invoice.put("payment_details", new HashMap<String, Object>());
        invoice.put("cart_id", cartId);
        return invoice;
    }

}

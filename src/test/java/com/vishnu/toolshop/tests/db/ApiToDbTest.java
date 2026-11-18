package com.vishnu.toolshop.tests.db;

import com.vishnu.toolshop.api.TestData;
import com.vishnu.toolshop.api.UsersApi;
import com.vishnu.toolshop.db.DbClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class ApiToDbTest {

    @SuppressWarnings("unchecked")
    @Test(groups = "db")
    public void registeredUserIsStoredCorrectly(){
        String email=TestData.uniqueEmail();
        Map<String,Object> user=TestData.newUser(email,TestData.VALID_PASSWORD);
        Map<String,Object> address=(Map<String, Object>) user.get("address");

        UsersApi.register(user).then().statusCode(201);

        List<Map<String,Object>> rows=DbClient.query("SELECT * FROM users WHERE email=?",email);
        Assert.assertEquals(rows.size(),1,"expected exactly one row for "+email);
        Map<String,Object> row=rows.get(0);

        Assert.assertEquals(row.get("first_name"), user.get("first_name"));
        Assert.assertEquals(row.get("last_name"), user.get("last_name"));
        Assert.assertEquals(String.valueOf(row.get("dob")), user.get("dob"));
        Assert.assertEquals(row.get("phone"), user.get("phone"));
        Assert.assertEquals(row.get("street"), address.get("street"));
        Assert.assertEquals(row.get("city"), address.get("city"));
        Assert.assertEquals(row.get("state"), address.get("state"));
        Assert.assertEquals(row.get("country"), address.get("country"));
        Assert.assertEquals(row.get("postal_code"), address.get("postal_code"));

        Assert.assertEquals(row.get("role"),"user");
        Assert.assertEquals(((Number)(row.get("failed_login_attempts"))).intValue(),0);

        String stored=String.valueOf(row.get("password"));
        Assert.assertNotEquals(stored,TestData.VALID_PASSWORD,"password stored in plain text");
        Assert.assertTrue(stored.startsWith("$2y$"), "password is not a bcrypt hash");

    }

    @Test(groups = "db")
    public void duplicateEmailDoesNotCreateASecondRow(){
        String email=TestData.uniqueEmail();

        UsersApi.register(TestData.newUser(email, TestData.VALID_PASSWORD)).then().statusCode(201);
        UsersApi.register(TestData.newUser(email, TestData.VALID_PASSWORD)).then().statusCode(409);

        Object rows=DbClient.scalar("SELECT COUNT(*) FROM users WHERE email = ?", email);
        Assert.assertEquals(((Number) rows).intValue(), 1, "duplicate registration created another row");
    }
}

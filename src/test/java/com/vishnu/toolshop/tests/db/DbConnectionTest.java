package com.vishnu.toolshop.tests.db;

import com.vishnu.toolshop.db.DbClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class DbConnectionTest {

    @Test(groups = "db")
    public void databaseAnswersAsSimpleQuery(){
        Object one=DbClient.scalar("SELECT 1");
        Assert.assertEquals(((Number) one).intValue(),1);
    }

    @Test(groups = "db")
    public void expectedTablesExist(){

        List<Map<String,Object>> rows=DbClient.query(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = ?","toolshop"
        );

        List<String> names=rows.stream()
                .map(r->String.valueOf(r.get("table_name")).toLowerCase()).toList();

        Assert.assertTrue(names.containsAll(List.of("users","products","invoices")),"missing tables, found: "+names);
    }



}

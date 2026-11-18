package com.vishnu.toolshop.tests.db;

import com.vishnu.toolshop.db.DbClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class DbIntegrityTest {

    private long count(String sql,Object... params){
        return ((Number) DbClient.scalar(sql,params)).longValue();
    }

    @Test(groups = "db")
    public void seedUsersExistWithExpectedRoles(){
        List<Map<String,Object>> admin=DbClient.query(
                "SELECT role FROM users WHERE email= ?","admin@practicesoftwaretesting.com"
        );

        List<Map<String,Object>> customer=DbClient.query(
                "SELECT role FROM users WHERE email= ?","customer@practicesoftwaretesting.com"
        );

        Assert.assertEquals(admin.size(),1,"admin user missing");
        Assert.assertEquals(admin.get(0).get("role"),"admin");
        Assert.assertEquals(customer.size(),1,"customer size missing");
        Assert.assertEquals(customer.get(0).get("role"),"user");

    }

    @Test(groups = "db")
    public void productsAreSeeded(){
        Assert.assertTrue(count("SELECT COUNT(*) FROM products")>0,"no products in the database");
    }

    @Test(groups = "db")
    public void noUserHasAnEmptyEmail(){
        Assert.assertEquals(count("SELECT COUNT(*) FROM users WHERE users.email IS NULL or TRIM(email)=''"),0L);
    }

    @Test(groups = "db")
    public void everyPasswordIsHashedNotPlainText(){
        long notHashed=count("SELECT COUNT(*) FROM users WHERE password IS NOT NULL AND password NOT LIKE '$2y$%'");
        Assert.assertEquals(notHashed,0L,"found passwords that don't look like bcrypt hashes");
    }

    @Test(groups = "db")
    public void everyPasswordLoginUserHasAPassword(){
        long missing=count("SELECT COUNT(*) FROM users WHERE password IS NULL AND provider IS NULL");
        Assert.assertEquals(missing,0L,"users with neither a password nor an external provider");
    }

    @Test(groups = "db")
    public void noInvoiceItemIsOrphaned(){
        long orphans=count("SELECT COUNT(*) FROM invoice_items ii LEFT JOIN invoices i ON i.id=ii.invoice_id WHERE i.id IS NULL");
        Assert.assertEquals(orphans,0L,"invoice items without an invoice");
    }

    @Test(groups = "db")
    public void noInvoiceHasANegativeTotal(){
        Assert.assertEquals(count("SELECT COUNT(*) FROM invoices WHERE total<0"),0L);
    }

    @Test(groups = "db")
    public void noInvoiceItemHasANonPositiveQuantity(){
        Assert.assertEquals(count("SELECT COUNT(*) FROM invoice_items WHERE quantity<=0"),0L);
    }
}

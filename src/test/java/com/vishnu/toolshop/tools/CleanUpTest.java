package com.vishnu.toolshop.tools;

import com.vishnu.toolshop.db.DbClient;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;


public class CleanUpTest {

    private static final String TEST_USERS=
            "SELECT id FROM users WHERE email LIKE '%@example.com'";
    private static final String TEST_INVOICES=
            "SELECT id FROM invoices WHERE billing_street = 'Alan Summit'"+"OR user_id IN ("+ TEST_USERS+")";

    @Test(groups = "cleanup")
    public void removeTestData(){
        if (!"yes".equals(System.getProperty("cleanup.confirm"))){
            throw new SkipException("Cleanup skipped. Run with -Dcleanup.confirm-yes to delete test data.");
        }

        int payments=DbClient.execute("DELETE FROM payments WHERE invoice_id IN ("+TEST_INVOICES+")");
        int items= DbClient.execute("DELETE FROM invoice_items WHERE invoice_id IN ("+TEST_INVOICES+")");
        int invoices = DbClient.execute("DELETE FROM invoices WHERE id IN (" + TEST_INVOICES + ")");
        int favorites = DbClient.execute("DELETE FROM favorites WHERE user_id IN (" + TEST_USERS + ")");
        int replies = DbClient.execute("DELETE FROM contact_request_replies WHERE user_id IN (" + TEST_USERS + ")");
        int users = DbClient.execute("DELETE FROM users WHERE email LIKE '%@example.com'");

        System.out.println("CLEANUP removed: payments="+ payments+" invoice_items="+items
        +" invoices="+invoices+" favorites="+favorites+"contact_replies="+replies+"users="+users);

        Assert.assertEquals(((Number) DbClient.scalar("SELECT COUNT(*) FROM users WHERE email LIKE '%@example.com'")).intValue(),0);
        Assert.assertEquals(((Number) DbClient.scalar("SELECT COUNT(*) FROM users WHERE email LIKE '%@practicesoftwaretesting.com'")).intValue(),3,"seed users are missing after cleanup");
    }
}

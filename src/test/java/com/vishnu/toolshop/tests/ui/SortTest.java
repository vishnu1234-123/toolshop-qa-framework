package com.vishnu.toolshop.tests.ui;

import com.vishnu.toolshop.tests.BaseTest;
import com.vishnu.toolshop.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SortTest extends BaseTest {
    @Test(description = "TC-005 | REQ-PO-05 | Sort price low-high shows ascending prices")
    public void SortPriceLowToHigh(){
        HomePage home=new HomePage(driver).open().sortBy("Price (Low - High)");
        List<BigDecimal> prices=home.productPrices();
        List<BigDecimal> expected=new ArrayList<>(prices);
        expected.sort(null);

        Assert.assertEquals(prices,expected,"Prices are not in ascending order");
    }

    @Test(description = "TC-006 | REQ-PO-05 | Sort name Z-A shows descending names")
    public void sortNameZToA(){
        HomePage home=new HomePage(driver).open().sortBy("Name (Z - A)");
        List<String> names=home.productNames();
        List<String> expected=new ArrayList<>(names);
        expected.sort(String.CASE_INSENSITIVE_ORDER.reversed());

        Assert.assertEquals(names,expected,"Names are not in descending order");
    }

}

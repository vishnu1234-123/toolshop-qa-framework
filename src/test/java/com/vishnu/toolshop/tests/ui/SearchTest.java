package com.vishnu.toolshop.tests.ui;

import com.vishnu.toolshop.pages.HomePage;
import com.vishnu.toolshop.tests.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

import org.testng.Assert;
import org.testng.annotations.Test;


public class SearchTest extends BaseTest{

    @Test(description = "TC-001 | REQ-PO-03 | Search with 3 characters returns matching products")
    @Feature("Product browsing")
    @Story("REQ-PO-3 Search")
    @Severity(SeverityLevel.NORMAL)
    public void searchWithThreeCharacterReturnsMatches(){
        HomePage home=new HomePage(driver).open().searchFor("pli");

        Assert.assertTrue(home.SearchTerm().toLowerCase().contains("pli"),"Search term not shown");
        Assert.assertTrue(home.resultCount()>0,"Expected at least one result");
        Assert.assertTrue(home.productNames().stream().anyMatch(n->n.toLowerCase().contains("pli")),"No product name contains the search term");
    }
}

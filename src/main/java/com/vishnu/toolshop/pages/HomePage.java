package com.vishnu.toolshop.pages;

import com.vishnu.toolshop.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.List;

public class HomePage extends BasePage {

    private static final By CARD= By.cssSelector("[data-test^='product-0']");
    private static final By NAME= byTest("product-name");
    private static final By PRICE=byTest("product-price");
    private static final By SEARCH_QUERY=byTest("search-query");
    private static final By SEARCH_SUBMIT=byTest("search-submit");
    private static final By SEARCH_COMPLETED=byTest("search_completed");
    private static final By SEARCH_TERM=byTest("search-term");
    private static final By SEARCH_RESULT_COUNT=byTest("search-result-count");

    public HomePage(WebDriver driver){
        super(driver);
    }

    public HomePage searchFor(String term){
        type(SEARCH_QUERY,term);
        click(SEARCH_SUBMIT);
        visible(SEARCH_COMPLETED);
        return this;
    }

    public String SearchTerm(){
        return text(SEARCH_TERM);
    }

    public int resultCount(){
        return Integer.parseInt(text(SEARCH_RESULT_COUNT).replaceAll("[^0-9]", ""));
    }

    private List<WebElement> cards(){
        return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(CARD,0));
    }

    public List<String> productNames(){
        return cards().stream().map(card ->card.findElement(NAME).getText().trim()).toList();
    }

    public List<BigDecimal> productPrices(){
        return cards().stream().map(card->money(card.findElement(PRICE).getText().trim())).toList();
    }

    public HomePage sortBy(String optionText){
        List<String> before=productNames();
        new Select(visible(byTest("sort"))).selectByVisibleText(optionText);
        wait.until(d->!productNames().equals(before));
        return this;
    }

    public HomePage open(){
        driver.get(ConfigReader.get("ui.base.url"));
        cards();
        return this;
    }

    public int cardCount(){
        return cards().size();
    }

    public String nameOfProduct(int index){
        return cards().get(index).findElement(NAME).getText().trim();
    }

    public BigDecimal priceOfProduct(int index){
        String raw= cards().get(index).findElement(PRICE).getText();
        return new BigDecimal(raw.replaceAll("[^0-9.]", ""));
    }

    public ProductPage openProduct(int index){
        cards().get(index).findElement(NAME).click();
        return new ProductPage(driver);
    }

}

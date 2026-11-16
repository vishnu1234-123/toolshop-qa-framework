package com.vishnu.toolshop.driver;

import  com.vishnu.toolshop.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.PageLoadStrategy;

import java.time.Duration;

public final class DriverFactory {
    private static  final ThreadLocal<WebDriver> DRIVER=new ThreadLocal<>();
    private DriverFactory(){

    }
    public static WebDriver initDriver() {
        String browser=ConfigReader.get("browser").toLowerCase();
        boolean headless= ConfigReader.getBoolean("headless");
        WebDriver driver;
        switch(browser){
            case "chrome" -> {
                ChromeOptions options=new ChromeOptions();
                options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                options.addArguments("--window-size=1920,1080","--disable-notifications");
                if (headless){
                    options.addArguments("--headless=new");
                }
                driver=new ChromeDriver(options);
            }

            case "firefox" -> {
                FirefoxOptions options=new FirefoxOptions();
                if (headless){
                    options.addArguments("-headless");
                }
                driver= new FirefoxDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: "+browser);
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        DRIVER.set(driver);
        return driver;
    }

    public static WebDriver getDriver(){
        WebDriver driver=DRIVER.get();
        if (driver==null){
            throw new IllegalStateException("Driver not intialised for this thread");

        }
        return driver;
    }

    public static  void quitDriver(){
        WebDriver driver=DRIVER.get();
        if (driver!=null){
            driver.quit();
            DRIVER.remove();
        }
    }
}

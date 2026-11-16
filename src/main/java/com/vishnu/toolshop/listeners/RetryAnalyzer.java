package com.vishnu.toolshop.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer  implements IRetryAnalyzer{

    public static final int MAX_RETRIES=1;
    private int attempt=0;

    @Override
    public boolean retry(ITestResult result){
        if(attempt<MAX_RETRIES){
            attempt++;
            System.out.println("RETRY "+attempt+" for "+result.getName());
            return true;
        }
        return false;
    }

}

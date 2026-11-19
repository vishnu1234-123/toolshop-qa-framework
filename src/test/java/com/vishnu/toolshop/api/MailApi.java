package com.vishnu.toolshop.api;

import com.vishnu.toolshop.config.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class MailApi {

    private static RequestSpecification spec(){
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("mailcatcher.url"))
                .addFilter(new AllureRestAssured())
                .build();
    }

    public static List<Map<String,Object>> messages(){
        return given().spec(spec())
                .when().get("/messages")
                .then().statusCode(200)
                .extract().jsonPath().getList("$");
    }

    @SuppressWarnings("unchecked")
    public static Map<String,Object> waitForMessage(String recipient,String subject,int timeoutSeconds){
        long deadline=System.currentTimeMillis()+timeoutSeconds*1000L;
        while(System.currentTimeMillis()<deadline){
            for(Map<String,Object> message:messages()) {
                List<String> recipients = (List<String>) message.get("recipients");
                if (subject.equals(message.get("subject")) && recipients.contains("<" + recipient + ">")) {
                    return message;
                }
            }
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }
            }

        throw new AssertionError("No '" + subject + "' email for " + recipient
                + " arrived within " + timeoutSeconds + " seconds");
    }

    public static String plainBody(Object messageId){
        return given().spec(spec())
                .when().get("/messages/{id}.plain",messageId)
                .then().statusCode(200)
                .extract().asString();
    }

}

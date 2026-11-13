package com.vishnu.toolshop.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties FILE=load();
    private ConfigReader(){

    }

    private static Properties load(){
        Properties props= new Properties();
        try(InputStream in= ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")){
            if(in == null){
                throw  new IllegalStateException("config.properties not found on class path");

            }
            props.load(in);

        }catch (IOException e){
            throw new IllegalStateException("Could not read config.properties");
        }
        return props;
    }

    public static String get(String key){
        String value=System.getProperty(key);
        if (value==null){
            value=System.getenv(key.toUpperCase().replace('.','_'));


        }
        if (value==null){
            value=FILE.getProperty(key);
        }
        if(value==null){
            throw new IllegalArgumentException("Missing config value: "+key);

        }
        return value.trim();

    }

    public static int getInt(String key){
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key){
        return Boolean.parseBoolean(get(key));
    }
}



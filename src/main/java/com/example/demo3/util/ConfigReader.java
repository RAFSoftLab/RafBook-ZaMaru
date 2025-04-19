package com.example.demo3.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                // Umesto return, postavi property na default vrednost ako fajl nije pronađen
                properties.setProperty("api.url", "http://default-url.com"); // Default URL
            } else {
                properties.load(input);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getApiUrl() {
        return properties.getProperty("api.url");
    }
}

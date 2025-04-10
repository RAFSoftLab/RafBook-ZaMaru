package com.example.demo3.repository;

import java.util.HashMap;
import java.util.Map;

public class MainRepository {
    private static MainRepository instance;
    private Map<String, String> storage;


    private MainRepository() {
        storage = new HashMap<>();
    }


    public static synchronized MainRepository getInstance() {
        if (instance == null) {
            instance = new MainRepository();
        }
        return instance;
    }


    public void saveJWT(String userId, String token) {
        storage.put(userId, token);
    }


    public String getJWT(String userId) {
        return storage.get(userId);
    }


    public void removeJWT(String userId) {
        storage.remove(userId);
    }

    public void put(String key, String value) {
        storage.put(key, value);
    }

    public String get(String key) {
        return storage.get(key);
    }

}
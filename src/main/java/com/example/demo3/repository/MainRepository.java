package com.example.demo3.repository;

import java.util.HashMap;
import java.util.Map;

public class MainRepository {
    private static MainRepository instance;
    private Map<String, String> storage;

    // Private constructor to prevent instantiation
    private MainRepository() {
        storage = new HashMap<>();
    }

    // Public method to provide access to the singleton instance
    public static synchronized MainRepository getInstance() {
        if (instance == null) {
            instance = new MainRepository();
        }
        return instance;
    }

    // Save a JWT
    public void saveJWT(String userId, String token) {
        storage.put(userId, token);
    }

    // Retrieve a JWT
    public String getJWT(String userId) {
        return storage.get(userId);
    }

    // Remove a JWT
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
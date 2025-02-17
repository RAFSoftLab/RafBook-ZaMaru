package com.example.demo3.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class ApiClientCategory {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8080/api/text-channel"; // Endpoint za kanale
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<String> getCategories() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/categories/names")
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                return objectMapper.readValue(jsonResponse, new TypeReference<List<String>>() {});
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
}}

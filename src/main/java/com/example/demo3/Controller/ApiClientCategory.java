package com.example.demo3.Controller;

import com.example.demo3.Model.NewCategoryDTO;
import com.example.demo3.util.ConfigReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ApiClientCategory {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = ConfigReader.getApiUrl();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<String> getCategories() throws IOException {
        HttpUrl url = HttpUrl.parse(BASE_URL + "/categories/names");

        Request request = new Request.Builder()
                .url(url)
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
    }

    public static boolean addCategory(String name, String description, String studyProgram, String studies) throws IOException {
        String json = String.format(
                "{\"name\":\"%s\",\"description\":\"%s\",\"studyProgram\":\"%s\",\"studies\":\"%s\"}",
                name, description, studyProgram, studies
        );

        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        HttpUrl url = HttpUrl.parse(BASE_URL + "/categories");

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                System.out.println(responseBody);
                return true;
            } else {
                System.out.println("Failed to add category. HTTP code: " + response.code());
                return false;
            }
        }
    }


    public static List<String> getFilteredCategories(String filter) throws IOException {
        HttpUrl url = HttpUrl.parse(BASE_URL + "/categories/filter")
                .newBuilder()
                .addQueryParameter("filter", filter)
                .build();

        Request request = new Request.Builder()
                .url(url)
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
    }
}

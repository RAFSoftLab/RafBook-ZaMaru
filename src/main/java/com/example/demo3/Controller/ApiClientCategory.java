package com.example.demo3.Controller;

import com.example.demo3.Model.NewCategoryDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ApiClientCategory {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8080/api"; // Endpoint za kanale
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<String> getCategories() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL+"/categories/names")
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                return objectMapper.readValue(jsonResponse, new TypeReference<List<String>>() {});
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }}
    public static boolean addCategory(NewCategoryDTO newCategoryData) throws IOException {
        String url = BASE_URL + "/categories";
        String authToken = AuthClient.getToken();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", newCategoryData.getName());
        jsonObject.put("description", newCategoryData.getDescription());

        String json = jsonObject.toString();
        System.out.println("Request URL: " + url);
        System.out.println("Request JSON: " + json);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println("Response Code: " + response.code());
            System.out.println("Response Body: " + responseBody);

            if (!response.isSuccessful()) {
                System.out.println("Error Headers: " + response.headers());
                return false;
            }

            return responseBody.contains("Category successfully added");
        }
    }


}

package com.example.demo3;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;
import java.util.List;

public class ApiClientUser {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://192.168.124.28:8080/api/users";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<HelloApplication.Person> getUsers() throws IOException {

        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                return objectMapper.readValue(jsonResponse, new TypeReference<List<HelloApplication.Person>>() {});
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

}


package com.example.demo3;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;

public class ApiClientAddUser {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://192.168.124.28:8080/api/user";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static boolean addUser(int userId, Object newUserData) throws IOException {
        // URL sa ID-em
        String url = BASE_URL + "/" + userId;


        String json = objectMapper.writeValueAsString(newUserData);


        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));


        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }
}


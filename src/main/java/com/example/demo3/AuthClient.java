package com.example.demo3;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class AuthClient {
    private static final String BASE_URL = "http://192.168.124.28:8080/api/users/auth/login"; // API URL
    private static final OkHttpClient client = new OkHttpClient();
    private static String token;




    public static String authenticate(String username, String password) throws IOException {

        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);


        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();




        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);


                token = jsonResponse.optString("token", null);


                return token;
            } else {

                return null;
            }
        }
    }
    public static String getToken() {
        return token;
    }
}

package com.example.demo3.Controller;

import com.example.demo3.Model.NewVoiceChannelDTO;
import com.example.demo3.util.ConfigReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class ApiVoiceChannel {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = ConfigReader.getApiUrl()+ "/voice-channel";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean addVoiceChannel(NewVoiceChannelDTO newVoiceChannelDTO) throws IOException {
        String url = BASE_URL;

        String json = objectMapper.writeValueAsString(newVoiceChannelDTO);

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
                .addHeader("Authorization", "Bearer " + AuthClient.getToken()) // Dodaj validan JWT token
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println("Response Code: " + response.code());
            System.out.println("Response Body: " + responseBody);

            return response.code() == 200;
        }
    }
}

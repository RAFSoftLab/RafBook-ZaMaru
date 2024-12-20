package com.example.demo3.Controller;
import java.io.IOException;
import java.util.List;

import com.example.demo3.Model.Channel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;


public class ApiChannel {
        private static final OkHttpClient client = new OkHttpClient();
        private static final String BASE_URL = "http://192.168.124.28:8080/api/text-channel"; // Endpoint za kanale
        private static final ObjectMapper objectMapper = new ObjectMapper();

        public static List<Channel> getChannels() throws IOException {
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    return objectMapper.readValue(jsonResponse, new TypeReference<List<Channel>>() {});
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            }
        }

    public static boolean addChannelById(int channelId, Object newChannelData) throws IOException {

        String url = BASE_URL + "/" + channelId;


        String json = objectMapper.writeValueAsString(newChannelData);


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




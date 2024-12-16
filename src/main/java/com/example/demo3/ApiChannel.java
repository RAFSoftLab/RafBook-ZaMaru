package com.example.demo3;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



    public class ApiChannel {
        private static final OkHttpClient client = new OkHttpClient();
        private static final String BASE_URL = "http://192.168.124.28:8080/api/text-channel"; // Endpoint za kanale
        private static final ObjectMapper objectMapper = new ObjectMapper();

        public static List<HelloApplication.Channel> getChannels() throws IOException {
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    return objectMapper.readValue(jsonResponse, new TypeReference<List<HelloApplication.Channel>>() {});
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            }
        }



    }




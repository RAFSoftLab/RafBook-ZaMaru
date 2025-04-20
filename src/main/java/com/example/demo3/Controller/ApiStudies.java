package com.example.demo3.Controller;
import com.example.demo3.Controller.AuthClient;
import com.example.demo3.Model.StudiesDTO;
import com.example.demo3.util.ConfigReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;
import java.util.List;


public class ApiStudies {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = ConfigReader.getApiUrl();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<StudiesDTO> getStudies() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/studies")
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                return objectMapper.readValue(jsonResponse, new TypeReference<List<StudiesDTO>>() {});
            } else {
                throw new IOException("Unexpected response code: " + response.code());
            }
        }
    }

    public static String addStudies(StudiesDTO studiesDTO) throws IOException {
        String json = objectMapper.writeValueAsString(studiesDTO);

        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/studies")
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                return objectMapper.readTree(jsonResponse).get("message").asText();
            } else {
                throw new IOException("Unexpected response code: " + response.code());
            }
        }
    }
}

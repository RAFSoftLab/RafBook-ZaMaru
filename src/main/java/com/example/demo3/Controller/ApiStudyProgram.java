package com.example.demo3.Controller;

import com.example.demo3.Model.StudyProgramDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;
import java.util.List;

public class ApiStudyProgram {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<StudyProgramDTO> getStudyPrograms(String studies) throws IOException {
        if (studies == null || studies.isEmpty()) {
            throw new IllegalArgumentException("Studies parameter is required and cannot be null or empty.");
        }

        String url = BASE_URL + "/study-programs/by-studies?studies=" + studies;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + AuthClient.getToken()) // proveri token
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response Code: " + response.code());  // Logovanje HTTP status koda

            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                System.out.println("Response Body: " + jsonResponse); // Logovanje odgovora

                return objectMapper.readValue(jsonResponse, new TypeReference<List<StudyProgramDTO>>() {});
            } else {
                throw new IOException("Unexpected response code: " + response.code());
            }
        }
    }

    public static String addStudyProgram(StudyProgramDTO studyProgramDTO) throws IOException {
        String json = objectMapper.writeValueAsString(studyProgramDTO);

        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/study-programs")
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
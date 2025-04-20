package com.example.demo3.Controller;

import com.example.demo3.Model.Channel;
import com.example.demo3.Model.NewChannelDTO;
import com.example.demo3.repository.MainRepository;
import com.example.demo3.util.ConfigReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ApiChannel {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = ConfigReader.getApiUrl() + "/text-channel";
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

    public static boolean addChannel(NewChannelDTO newChannelData) throws IOException {
        String url = BASE_URL;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", newChannelData.getName());
        jsonObject.put("description", newChannelData.getDescription());
        jsonObject.put("categoryName", newChannelData.getCategoryName());
        jsonObject.put("studiesName", newChannelData.getStudiesName());
        jsonObject.put("studyProgramName", newChannelData.getStudyProgramName());
        jsonObject.put("folderId", "");

        jsonObject.put("roles", newChannelData.getRoles() != null
                ? new JSONArray(newChannelData.getRoles())
                : new JSONArray());

        String json = jsonObject.toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            return response.code() == 200;
        }
    }

    public static boolean deleteChannel(int channelId) throws IOException {
        String url = BASE_URL + "/" + channelId;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            if (response.isSuccessful()) {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                System.out.println("Message: " + jsonNode.get("message").asText());
                return true;
            } else if (response.code() == 404) {
                System.err.println("Channel not found with ID: " + channelId);
                return false;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    public static boolean addRolesToChannel(long channelId, List<String> roles) throws IOException {
        String url = BASE_URL + "/add-roles/" + channelId;

        String json = objectMapper.writeValueAsString(roles);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            return response.code() == 200;
        }
    }

    public static boolean removeRolesFromChannel(long channelId, List<String> roles) throws IOException {
        String url = BASE_URL + "/remove-roles/" + channelId;

        String json = objectMapper.writeValueAsString(roles);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );

        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            return response.code() == 200;
        }
    }

    public static boolean editChannel(int id, String name, String description) throws IOException {
        String url = BASE_URL + "/" + id + "?id=" + id + "&name=" + name + "&description=" + description;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        jsonObject.put("description", description);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonObject.toString()
        );

        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            return response.code() == 200;
        }
    }
}

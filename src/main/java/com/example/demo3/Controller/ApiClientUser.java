package com.example.demo3.Controller;
import com.example.demo3.Model.NewUserDTO;
import com.example.demo3.Model.Person;
import com.example.demo3.repository.MainRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiClientUser {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8080/api/users";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Person> getUsers() throws IOException {

        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                return objectMapper.readValue(jsonResponse, new TypeReference<List<Person>>() {});
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    public static Person getUserById(int id) throws IOException {
        String url = BASE_URL + "/" + id;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                return objectMapper.readValue(jsonResponse, Person.class);
            } else if (response.code() == 404) {
                System.out.println("User not found");
                return null;
            } else {
                throw new IOException("Unexpected response code: " + response.code());
            }
        }
    }

    public static boolean addUser(NewUserDTO newUserData) throws IOException {
        String url = BASE_URL + "/auth/register";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", newUserData.getFirstName());
        jsonObject.put("lastName", newUserData.getLastName());
        jsonObject.put("email", newUserData.getEmail());
        jsonObject.put("password", newUserData.getPassword());
        jsonObject.put("macAddress", newUserData.getMacAddress());
        String role = (newUserData.getRole() == null || newUserData.getRole().isEmpty()) ? "ADMIN" : newUserData.getRole();
        jsonObject.put("role", role);

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
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println("Response Code: " + response.code());
            System.out.println("Response Body: " + responseBody);
            System.out.println("Id je "+newUserData.getId());

            if (!response.isSuccessful()) {
                System.out.println("Error Headers: " + response.headers());
                return false;
            }

            return responseBody.contains("User successfully created");
        }
    }

    public static boolean deleteUser(Person user) throws IOException {
        if (user == null || user.getId() == 0) {
            throw new IllegalArgumentException("Invalid user for deletion");
        }

        String url = BASE_URL + "/" + user.getId();
        System.out.println("Attempting to delete user at URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 403) {
                System.err.println("Permission denied. Please check your authentication token.");
                return false;
            }
            if (response.code() == 404) {
                System.err.println("User not found: " + user.getId());
                return false;
            }
            if (!response.isSuccessful()) {
                System.err.println("Error deleting user: " + response.code() + " - " + response.message());
                return false;
            }
            return true;
        }
    }

    public static boolean editUser(NewUserDTO userDTO) throws IOException {
        if (userDTO == null || userDTO.getId() == 0) {
            throw new IllegalArgumentException("Invalid user for update");
        }
        String url = BASE_URL + "/" + userDTO.getId();


        String jsonBody = new JSONObject()
                .put("id", userDTO.getId())
                .put("firstName", userDTO.getFirstName())
                .put("lastName", userDTO.getLastName())
                .put("username", userDTO.getUsername())
                .put("email", userDTO.getEmail())
                //.put("role", new JSONArray(Arrays.asList(userDTO.getRole().split(","))))
                .toString();
        System.out.println(jsonBody);

        RequestBody requestBody = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        System.out.println(requestBody);


        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();
        System.out.println(request.toString());

        try (Response response = client.newCall(request).execute()) {
            int responseCode = response.code();
            String responseBody = response.body() != null ? response.body().string() : "";

            System.out.println("Response code: " + responseCode);
            System.out.println("Response body: " + responseBody);

            if (response.code() == 403) {
                System.err.println("Permission denied. Please check your authentication token.");
                return false;
            }
            if (response.code() == 404) {
                System.err.println("User not found: " + userDTO.getId());
                return false;
            }
            if (!response.isSuccessful()) {
                System.err.println("Error updating user: " + response.code() + " - " + response.message());
                return false;
            }
            return true;
        }
    }

    public static boolean deleteRoleFromUser(int userId, String role) throws IOException {
        if (userId == 0 || role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID or role for deletion");
        }

        String url = BASE_URL + "/" + userId + "/removeRole/" + role;

        Request request = new Request.Builder()
                .url(url)
                .patch(RequestBody.create(null, new byte[0]))
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();
        System.out.println(request.body() +  " " + request.toString());



        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response Code: " + response.code());
            System.out.println("Response Body: " + response.body().string());
            if (response.code() == 403) {
                System.err.println("Permission denied. Please check your authentication token.");
                return false;
            }
            if (response.code() == 404) {
                System.err.println("User or role not found: " + userId + " or " + role);
                return false;
            }
            if (!response.isSuccessful()) {
                System.err.println("Error deleting role: " + response.code() + " - " + response.message());
                return false;
            }
            return true;

        }

    }
    public static boolean addRoleFromUser(int userId, String role) throws IOException {
        if (userId == 0 || role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID or role for addition");
        }
        String url = BASE_URL + "/" + userId + "/addRole/" + role;

        Request request = new Request.Builder()
                .url(url)
                .patch(RequestBody.create(null, new byte[0]))
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 403) {
                System.err.println("Permission denied. Please check your authentication token.");
                return false;
            }
            if (response.code() == 404) {
                System.err.println("User or role not found: " + userId + " or " + role);
                return false;
            }
            if (!response.isSuccessful()) {
                System.err.println("Error adding role: " + response.code() + " - " + response.message());
                return false;
            }
            return true;
        }
    }




}


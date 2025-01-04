package com.example.demo3.Controller;
import com.example.demo3.Model.NewUserDTO;
import com.example.demo3.Model.Person;
import com.example.demo3.repository.MainRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ApiClientUser {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://192.168.124.28:8080/api/users";
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

    public static boolean addUser(NewUserDTO newUserData) throws IOException {
        String url = "http://192.168.124.28:8080/api/users/auth/register";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", newUserData.getFirstName());
        jsonObject.put("lastName", newUserData.getLastName());
        jsonObject.put("email", newUserData.getEmail());
        jsonObject.put("password", newUserData.getPassword());
        jsonObject.put("macAddress", newUserData.getMacAddress());
        jsonObject.put("role", "ADMIN");

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

            if (!response.isSuccessful()) {
                System.out.println("Error Headers: " + response.headers());
                return false;
            }

            // Check for success message in response
            return responseBody.contains("User successfully created");
        }
    }

    public static boolean updateUser(Person user) throws IOException {
        if (user == null || user.getId() == 0) {
            throw new IllegalArgumentException("Invalid user for update");
        }

        String url = BASE_URL + "/" + user.getId();
        System.out.println("Attempting to update user at URL: " + url);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", user.getFirstName());
        jsonObject.put("lastName", user.getLastName());
        jsonObject.put("username", user.getUsername());
        jsonObject.put("email", user.getEmail());
        jsonObject.put("role", user.getRole());

        String json = jsonObject.toString();
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Authorization", "Bearer " + AuthClient.getToken())
                .addHeader("Content-Type", "application/json")
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
                System.err.println("Error updating user: " + response.code() + " - " + response.message());
                return false;
            }
            return true;
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

    public static boolean createUser(String firstName, String lastName, String username,
                                     String email, String role, String mac) throws IOException {
        URL url = new URL("http://192.168.124.28:8080/api/users/auth/register");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Match the CreateUserDTO format from API docs
        String jsonInputString = String.format(
                "{\"firstName\": \"%s\", \"lastName\": \"%s\", \"email\": \"%s\", " +
                        "\"password\": \"%s\", \"macAddress\": \"%s\", \"role\": \"%s\"}",
                firstName, lastName, email, username, mac, role.toUpperCase());

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED;
    }

}


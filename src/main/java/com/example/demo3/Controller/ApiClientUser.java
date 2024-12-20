package com.example.demo3.Controller;
import com.example.demo3.Model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;
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

    public static boolean addUser(int userId, Object newUserData) throws IOException {

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

    public static boolean deleteUserById(int userId) throws IOException {

        String url = BASE_URL + "/" + userId;

        Request request = new Request.Builder()
                .url(url)
                .delete()  // DELETE metoda
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful(); // Vraća true ako je brisanje uspešno
        }
    }

    public static boolean updateUserById(int id, Object updatedUserData) throws IOException {

        String url = BASE_URL + "/" + id;


        String json = objectMapper.writeValueAsString(updatedUserData);


        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));


        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

}


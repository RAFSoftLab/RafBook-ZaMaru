package com.example.demo3;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;

public class ApiClientAddUser {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8080/user"; // Endpoint za korisnike
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Metoda za dodavanje korisnika po ID-u
    public static boolean addUser(int userId, Object newUserData) throws IOException {
        // URL sa ID-em
        String url = BASE_URL + "/" + userId;

        // Kreiranje JSON stringa od objekta newUserData
        String json = objectMapper.writeValueAsString(newUserData);

        // Kreiranje tela zahteva
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        // Kreiranje POST zahteva
        Request request = new Request.Builder()
                .url(url)
                .post(body)  // POST metoda
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful(); // Vraća true ako je dodavanje uspešno
        }
    }
}


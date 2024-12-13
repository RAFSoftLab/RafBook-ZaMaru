package com.example.demo3;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;

public class ApiClientUpdateUser {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:8080/user"; // Endpoint za korisnike
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Metoda za ažuriranje korisnika po ID-u
    public static boolean updateUserById(int id, Object updatedUserData) throws IOException {
        // URL sa ID-em
        String url = BASE_URL + "/" + id;

        // Kreiranje JSON stringa od objekta updatedUserData
        String json = objectMapper.writeValueAsString(updatedUserData);

        // Kreiranje tela zahteva
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        // Kreiranje PUT zahteva
        Request request = new Request.Builder()
                .url(url)
                .put(body)  // PUT metoda
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful(); // Vraća true ako je ažuriranje uspešno
        }
    }
}


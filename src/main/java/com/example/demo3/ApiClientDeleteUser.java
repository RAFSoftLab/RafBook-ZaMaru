package com.example.demo3;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;


public class ApiClientDeleteUser {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://192.168.124.28:8080/api/user"; // Endpoint za korisnike
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Metoda za brisanje korisnika po ID-u
    public static boolean deleteUserById(int userId) throws IOException {
        // URL sa ID-em
        String url = BASE_URL + "/" + userId;

        Request request = new Request.Builder()
                .url(url)
                .delete()  // DELETE metoda
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful(); // Vraća true ako je brisanje uspešno
        }
    }
}

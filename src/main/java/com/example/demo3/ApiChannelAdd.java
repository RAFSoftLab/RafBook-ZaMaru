package com.example.demo3;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;

public class ApiChannelAdd {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://192.168.124.28:8080/text-channel"; // Endpoint za kanale
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Metoda za dodavanje kanala po ID-u
    public static boolean addChannelById(int channelId, Object newChannelData) throws IOException {
        // URL sa ID-em
        String url = BASE_URL + "/" + channelId;

        // Kreiranje JSON stringa od objekta newChannelData
        String json = objectMapper.writeValueAsString(newChannelData);

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
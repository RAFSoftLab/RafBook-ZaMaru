package com.example.demo3.Controller;

import com.example.demo3.Model.NewUserDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonParser {

    public static void loadAndRegisterUsers(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(new File(filePath));

            for (JsonNode node : root) {
                JsonNode teacherNode = node.get("teacher");
                if (teacherNode != null) {
                    NewUserDTO newUser = new NewUserDTO();
                    newUser.setFirstName(teacherNode.get("firstName").asText());
                    newUser.setLastName(teacherNode.get("lastName").asText());
                    newUser.setEmail(teacherNode.get("email").asText());
                    newUser.setPassword("defaultPassword123");
                    newUser.setMacAddress("00:1A:2B:3C:4D:5E");
                    newUser.setRole("ADMIN");

                    boolean success = ApiClientUser.addUser(newUser);

                    if (success) {
                        System.out.println("Korisnik uspešno dodat: " + newUser.getEmail());
                    } else {
                        System.out.println("Dodavanje korisnika neuspešno: " + newUser.getEmail());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //JsonParser.loadAndRegisterUsers("putanja/do/tvog.json");
    }
}

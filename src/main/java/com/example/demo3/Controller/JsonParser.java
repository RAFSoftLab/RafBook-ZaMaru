package com.example.demo3.Controller;

import com.example.demo3.Model.NewUserDTO;
import com.example.demo3.Model.NewCategoryDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonParser {

    public static void loadData(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(new File(filePath));

            for (JsonNode node : root) {
                JsonNode teacherNode = node.get("teacher");
                JsonNode subjectNode = node.get("subject");

                if (teacherNode != null) {
                    NewUserDTO newUser = new NewUserDTO();
                    newUser.setFirstName(teacherNode.get("firstName").asText());
                    newUser.setLastName(teacherNode.get("lastName").asText());
                    newUser.setEmail(teacherNode.get("email").asText());
                    String generatedPassword = teacherNode.get("firstName").asText().toLowerCase() + "123";
                    newUser.setPassword(generatedPassword);
                    newUser.setMacAddress("");
                    String roleUpperCase = teacherNode.get("title").asText().toUpperCase();
                    newUser.setRole(roleUpperCase);

                    boolean success = ApiClientUser.addUser(newUser);

                    if (success) {
                        System.out.println("Korisnik uspešno dodat: " + newUser.getEmail());
                    } else {
                        System.out.println("Dodavanje korisnika neuspešno: " + newUser.getEmail());
                    }
                }

                if (subjectNode != null) {
                    NewCategoryDTO newCategory = new NewCategoryDTO();
                    newCategory.setName(subjectNode.get("name").asText());
                    newCategory.setDescription(subjectNode.get("name").asText() + " drzi");

                    System.out.println("Kategorija kreirana: " + newCategory.getName() + " - " + newCategory.getDescription());

                    boolean success2=ApiClientCategory.addCategory(newCategory);
                    if(success2){
                        System.out.println("Uspesno dodata kategorija");
                    }
                    else{System.out.println("Neuspesno dodata kategorija");}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

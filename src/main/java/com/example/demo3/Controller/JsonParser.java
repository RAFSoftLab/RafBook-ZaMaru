package com.example.demo3.Controller;

import com.example.demo3.Model.NewUserDTO;
import com.example.demo3.Model.NewCategoryDTO;
import com.example.demo3.Model.Person;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                    String title = teacherNode.get("title").asText();
                    String role;

                    if ("nastavnik".equalsIgnoreCase(title)) {
                        role = "PROFESSOR";
                    } else if ("saradnik".equalsIgnoreCase(title)) {
                        role = "TEACHING_ASSOCIATE";
                    } else {
                        role = title;
                    }

                    newUser.setRole(role);

                    boolean success = ApiClientUser.addUser(newUser);

//                    Person newUser1=ApiClientUser.getUserById(newUser.getId());
//
//                    int newUserId=newUser1.getId();


                    if (success) {
                        System.out.println("Korisnik uspešno dodat: " + newUser.getEmail());
//                        if (subjectNode != null) {
//                            String subjectName = subjectNode.get("name").asText();
//                            String roleForSubject = subjectName.toUpperCase();
//
//                            boolean roleAdded = ApiClientUser.addRoleFromUser(newUserId, roleForSubject);
//
//                            if (roleAdded) {
//                                System.out.println("Uloga za predmet '" + subjectName + "' dodeljena korisniku: " + newUser.getEmail());
//                            } else {
//                                System.out.println("Greška prilikom dodele uloge za predmet '" + subjectName + "' korisniku: " + newUser.getEmail());
//                            }
//                        }

                    } else {
                        System.out.println("Dodavanje korisnika neuspešno: " + newUser.getId());

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

package com.example.demo3.Controller;

import com.example.demo3.Model.NewChannelDTO;
import com.example.demo3.Model.NewUserDTO;
import com.example.demo3.Model.NewCategoryDTO;
import com.example.demo3.Model.Person;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
                    String categoryName = subjectNode.get("name").asText();
                    String categoryDescription = "Kategorija"+categoryName;

                    System.out.println("Kategorija kreirana: " + categoryName + " - " + categoryDescription);

                    boolean success2 = ApiClientCategory.addCategory(categoryName, categoryDescription);
                    if (success2) {
                        System.out.println("Uspesno dodata kategorija");

                        String[] channelNames = {"Archive", "General", "Obavestenja"};
                        String[] channelDescriptions = {
                                "Archive channel for " + categoryName,
                                "General channel for " + categoryName,
                                "Obavestenja channel for " + categoryName
                        };

                        for (int i = 0; i < channelNames.length; i++) {
                            NewChannelDTO newChannel = new NewChannelDTO();
                            newChannel.setName(channelNames[i]);
                            newChannel.setDescription(channelDescriptions[i]);
                            newChannel.setCategory(categoryName);
                            //newChannel.setRoles(Collections.singletonList(categoryName));
                            List<String> roles = new ArrayList<>();
                            roles.add(categoryName);
                            newChannel.setRoles(roles);

                            boolean successChannel = ApiChannel.addChannel(newChannel);
                            if (successChannel) {
                                System.out.println("Kanal " + newChannel.getName() + " uspesno dodat u kategoriju " + categoryName);
                            } else {
                                System.out.println("Neuspesno dodavanje kanala " + newChannel.getName() + " u kategoriju " + categoryName);
                            }
                        }
                    } else {
                        System.out.println("Neuspesno dodata kategorija");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

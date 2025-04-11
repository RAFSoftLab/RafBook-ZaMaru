package com.example.demo3.Controller;

import com.example.demo3.Model.*;
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

            String[] programNames = {"RI", "RN", "S", "SI"};
            List<StudyProgramDTO> studyProgramList = new ArrayList<>();

            for (String programName : programNames) {
                StudyProgramDTO studyProgramDTO = new StudyProgramDTO();
                studyProgramDTO.setName(programName);
                studyProgramDTO.setDescription(programName + " racunarskog fakulteta");
                studyProgramDTO.setCategories(new ArrayList<>());

                try {
                    String response = ApiStudyProgram.addStudyProgram(studyProgramDTO);
                    System.out.println("Studijski program kreiran: " + response);
                    studyProgramList.add(studyProgramDTO);
                } catch (IOException e) {
                    System.out.println("Greška pri kreiranju studijskog programa " + programName + ": " + e.getMessage());
                }
            }

            StudiesDTO studiesDTO = new StudiesDTO();
            studiesDTO.setName("Osnovne akademske studije 2025");
            studiesDTO.setDescription("Osnovne akademske studije 2025 na Racunarskom fakultetu");
            studiesDTO.setStudyPrograms(studyProgramList);

            try {
                String response = ApiStudies.addStudies(studiesDTO);
                System.out.println("Studije kreirane: " + response);
            } catch (IOException e) {
                System.out.println("Greška pri kreiranju studija: " + e.getMessage());
            }

            JsonNode root = objectMapper.readTree(new File(filePath));
            List<NewUserDTO> addedUsers = new ArrayList<>();

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
                    String role = title.equalsIgnoreCase("nastavnik") ? "PROFESSOR"
                            : title.equalsIgnoreCase("saradnik") ? "TEACHING_ASSOCIATE" : title;

                    newUser.setRole(role);

                    boolean success = ApiClientUser.addUser(newUser);
                    if (success) {
                        addedUsers.add(newUser);
                        System.out.println("Korisnik uspešno dodat: " + newUser.getEmail());
                    } else {
                        System.out.println("Dodavanje korisnika neuspešno: " + newUser.getEmail());
                    }
                }

                if (subjectNode != null) {
                    String categoryName = subjectNode.get("name").asText();
                    String categoryDescription = "Kategorija " + categoryName;
                    String studyProgram=subjectNode.get("studyProgram").asText();
                    String studies="OSNOVNE AKADEMSKE STUDIJE";

                    System.out.println("Kategorija kreirana: " + categoryName + " - " + categoryDescription);
                    boolean successCategory = ApiClientCategory.addCategory(categoryName, categoryDescription,studyProgram,studies);

                    if (successCategory) {
                        System.out.println("Uspešno dodata kategorija");

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
                            newChannel.setCategoryName(categoryName);
                            List<String> roles = new ArrayList<>();
                            roles.add(categoryName);
                            newChannel.setRoles(roles);

                            boolean successChannel = ApiChannel.addChannel(newChannel);
                            if (successChannel) {
                                System.out.println("Kanal " + newChannel.getName() + " uspešno dodat u kategoriju " + categoryName);
                            } else {
                                System.out.println("Neuspešno dodavanje kanala " + newChannel.getName() + " u kategoriju " + categoryName);
                            }
                        }
                        NewVoiceChannelDTO newVoiceChannel = new NewVoiceChannelDTO();
                        newVoiceChannel.setName("General voice channel for " + categoryName);
                        newVoiceChannel.setDescription("General voice channel for " + categoryName);
                        newVoiceChannel.setCategoryName(categoryName);
                        newVoiceChannel.setStudiesName(studies);
                        newVoiceChannel.setStudyProgramName(studyProgram);

                        boolean successVoiceChannel = ApiVoiceChannel.addVoiceChannel(newVoiceChannel);
                        if (successVoiceChannel) {
                            System.out.println("Voice kanal " + newVoiceChannel.getName() + " uspešno dodat za kategoriju " + categoryName);
                        } else {
                            System.out.println("Neuspešno dodavanje voice kanala " + newVoiceChannel.getName() + " za kategoriju " + categoryName);
                        }
                    } else {
                        System.out.println("Neuspešno dodata kategorija");
                    }
                }
            }

            List<Person> allUsers = ApiClientUser.getUsers();
            for (NewUserDTO user : addedUsers) {
                for (Person existingUser : allUsers) {
                    if (existingUser.getEmail().equals(user.getEmail())) {
                        JsonNode correspondingNode = findNodeByEmail(root, user.getEmail());
                        if (correspondingNode != null) {
                            JsonNode subjectNode = correspondingNode.get("subject");
                            String roleName = subjectNode != null ? subjectNode.get("name").asText() : "DefaultRole";
                            ApiClientUser.addRoleFromUser(existingUser.getId(), roleName);
                            System.out.println("Dodata rola " + roleName + " korisniku " + user.getEmail());
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonNode findNodeByEmail(JsonNode root, String email) {
        for (JsonNode node : root) {
            JsonNode teacherNode = node.get("teacher");
            if (teacherNode != null && teacherNode.get("email").asText().equals(email)) {
                return node;
            }
        }
        return null;
    }
}

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

            StudiesDTO studiesDTO = new StudiesDTO();
            studiesDTO.setName("Osnovne akademske studije 2025");
            studiesDTO.setDescription("Osnovne akademske studije 2025 na Racunarskom fakultetu");
            studiesDTO.setStudyPrograms(new ArrayList<>());

            try {
                String response = ApiStudies.addStudies(studiesDTO);
                System.out.println("Studije kreirane: " + response);
                createStudyPrograms(studiesDTO);
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
                    String studyProgramRaw = subjectNode.get("studyProgram").asText();
                    String studies = "Osnovne akademske studije 2025";

                    List<String> validPrograms = List.of("RN", "RI", "S", "SI");
                    String[] programNames = studyProgramRaw.split("\\s+");
                    for (String program : programNames) {
                        if (!validPrograms.contains(program)) {
                            System.out.println("Nepoznat study program: " + program + " - preskačem dodavanje kategorije.");
                            continue;
                        }

                        System.out.println("Dodavanje kategorije: " + categoryName + " u studije " + studies + ", program: " + program);
                        boolean successCategory = ApiClientCategory.addCategory(categoryName, categoryDescription, program, studies);

                        if (successCategory) {
                            System.out.println("Uspešno dodata kategorija za program " + program);
                            String[] channelNames = {"Archive", "General", "Obavestenja"};
                            for (String channelName : channelNames) {
                                NewChannelDTO newChannel = new NewChannelDTO();
                                newChannel.setName(channelName);
                                newChannel.setDescription(channelName + " for " + categoryName);
                                newChannel.setCategoryName(categoryName);
                                newChannel.setStudiesName(studies);
                                newChannel.setStudyProgramName(program);
                                newChannel.setFolderId("");
                                List<String> roles = new ArrayList<>();
                                roles.add(categoryName);
                                newChannel.setRoles(roles);

                                try {
                                    boolean successChannel = ApiChannel.addChannel(newChannel);
                                    if (successChannel) {
                                        System.out.println("Kanal \"" + channelName + "\" uspešno dodat.");
                                    } else {
                                        System.out.println("Neuspešno dodavanje kanala \"" + channelName + "\".");
                                    }
                                } catch (IOException e) {
                                    System.out.println("Greška pri dodavanju kanala \"" + channelName + "\": " + e.getMessage());
                                }
                            }

                            NewVoiceChannelDTO newVoiceChannel = new NewVoiceChannelDTO();
                            newVoiceChannel.setName("General Voice Channel");
                            newVoiceChannel.setDescription("General Voice Channel for " + categoryName);
                            newVoiceChannel.setCategoryName(categoryName);
                            newVoiceChannel.setStudiesName(studies);
                            newVoiceChannel.setStudyProgramName(program);
                            newVoiceChannel.setRoles(null);

                            try {
                                boolean successVoiceChannel = ApiVoiceChannel.addVoiceChannel(newVoiceChannel);
                                if (successVoiceChannel) {
                                    System.out.println("Voice kanal \"General Voice Channel for " + categoryName + "\" uspešno dodat.");
                                } else {
                                    System.out.println("Neuspešno dodavanje voice kanala.");
                                }
                            } catch (IOException e) {
                                System.out.println("Greška pri dodavanju voice kanala: " + e.getMessage());
                            }

                        } else {
                            System.out.println("Neuspešno dodata kategorija za program " + program);
                        }
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

    private static void createStudyPrograms(StudiesDTO studiesDTO) {
        String[] programNames = {"RN", "RI", "S", "SI"};
        for (String programName : programNames) {
            StudyProgramDTO studyProgramDTO = new StudyProgramDTO();
            studyProgramDTO.setName(programName);
            studyProgramDTO.setDescription(programName + " na rafu");
            studyProgramDTO.setStudies(studiesDTO.getName());

            try {
                String response = ApiStudyProgram.addStudyProgram(studyProgramDTO);
                System.out.println("Studijski program " + programName + " kreiran: " + response);
            } catch (IOException e) {
                System.out.println("Greška pri kreiranju studijskog programa " + programName + ": " + e.getMessage());
            }
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

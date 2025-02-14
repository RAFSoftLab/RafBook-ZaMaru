package com.example.demo3.Controller;

import com.example.demo3.Model.Person;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public static List<Person> loadPersonsFromJson(String filePath) {
        List<Person> persons = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(new File(filePath));

            for (JsonNode node : root) {
                JsonNode teacherNode = node.get("teacher");
                if (teacherNode != null) {
                    String firstName = teacherNode.get("firstName").asText();
                    String lastName = teacherNode.get("lastName").asText();
                    String email = teacherNode.get("email").asText();
                    String username = "N/A";

                    List<String> roles = new ArrayList<>();
                    if (teacherNode.has("title")) {
                        roles.add(teacherNode.get("title").asText());
                    }

                    Person teacher = new Person(firstName, lastName, username, email, roles);

                    persons.add(teacher);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return persons;
    }
}

package com.example.demo3.Model;
import java.util.List;

public class NewVoiceChannelDTO {
    private String name;
    private String description;
    private List<String> roles;
    private String categoryName;
    private String studiesName;
    private String studyProgramName;

    public NewVoiceChannelDTO(String name, String description, List<String> roles,
                              String categoryName, String studiesName, String studyProgramName) {
        this.name = name;
        this.description = description;
        this.roles = roles;
        this.categoryName = categoryName;
        this.studiesName = studiesName;
        this.studyProgramName = studyProgramName;
    }

    public NewVoiceChannelDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStudiesName() {
        return studiesName;
    }

    public void setStudiesName(String studiesName) {
        this.studiesName = studiesName;
    }

    public String getStudyProgramName() {
        return studyProgramName;
    }

    public void setStudyProgramName(String studyProgramName) {
        this.studyProgramName = studyProgramName;
    }

}

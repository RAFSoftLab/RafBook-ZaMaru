package com.example.demo3.Model;

import java.util.List;

public class NewChannelDTO {
    private String name;
    private String description;
    private String category;
    private List<String> roles;
    private String studiesName;
    private String studyProgramName;




    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
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

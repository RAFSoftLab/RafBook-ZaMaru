package com.example.demo3.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


public class NewChannelDTO {
    private String name;
    private String description;
    private List<String> roles;
    private String categoryName;
    private String studiesName;
    private String studyProgramName;
    private String folderId;




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
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String category) {
        this.categoryName = category;
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

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }




}

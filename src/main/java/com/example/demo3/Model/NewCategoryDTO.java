package com.example.demo3.Model;

public class NewCategoryDTO {

    private String name;
    private String description;
    private String studies;
    private String studyProgram;

    public NewCategoryDTO(String name,String description,String studies,String studyProgram){
        this.name=name;
        this.description=description;
    }

    public NewCategoryDTO(){}


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudies() {
        return studies;
    }

    public void setStudies(String studies) {
        this.studies = studies;
    }

    public String getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(String studyProgram) {
        this.studyProgram = studyProgram;
    }

}

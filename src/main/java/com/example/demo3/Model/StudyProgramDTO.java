package com.example.demo3.Model;

import java.util.List;

public class StudyProgramDTO {
    private int id;
    private String name;
    private String description;
    private List<NewCategoryDTO> studyPrograms;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public List<NewCategoryDTO> getCategories() {
        return studyPrograms;
    }

    public void setCategories(List<NewCategoryDTO> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }

    @Override
    public String toString() {
        return getName(); // Pretpostavljam da metoda getName() postoji i vraća ime studijskog programa
    }
}

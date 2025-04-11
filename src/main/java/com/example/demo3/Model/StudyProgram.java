package com.example.demo3.Model;

import java.util.List;

public class StudyProgram {
    private int id;
    private String name;
    private String description;
    private List<NewCategoryDTO> categories;

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
        return categories;
    }

    public void setCategories(List<NewCategoryDTO> studyPrograms) {
        this.categories = studyPrograms;
    }

    @Override
    public String toString() {
        return getName();
}}

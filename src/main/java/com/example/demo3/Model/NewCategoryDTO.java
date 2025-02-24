package com.example.demo3.Model;

public class NewCategoryDTO {

    private String name;
    private String description;

    public NewCategoryDTO(String name,String description){
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

}

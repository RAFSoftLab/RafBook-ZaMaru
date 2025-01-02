package com.example.demo3.Model;

public class Channel {
    private int id;
    private String name;
    private String description;

    public Channel() {};

    public Channel(String name, String description) {

        this.name = name;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id=id;
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

}

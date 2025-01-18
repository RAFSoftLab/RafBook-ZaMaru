package com.example.demo3.Model;

import java.util.List;

public class Channel {
    private int id;
    private String name;
    private String description;
    private List<String> messageDTOList;
    private boolean canWrite;

    public Channel(){}

    public Channel(String name, String description, List<String> messageDTOList, boolean canWrite) {
        this.name = name;
        this.description = description;
        this.messageDTOList = messageDTOList;
        this.canWrite = canWrite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    // Getters and Setters for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters and Setters for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getters and Setters for messageDTOList
    public List<String> getMessageDTOList() {
        return messageDTOList;
    }

    public void setMessageDTOList(List<String> messageDTOList) {
        this.messageDTOList = messageDTOList;
    }

    // Getters and Setters for canWrite
    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

}

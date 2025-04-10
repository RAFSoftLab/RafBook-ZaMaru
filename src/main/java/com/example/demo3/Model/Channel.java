package com.example.demo3.Model;

import java.util.List;

public class Channel {
    private int id;
    private String name;
    private String description;
    private List<String> messageDTOList;
    private List<RolePermissionDTO> rolePermissionDTOList;
    private boolean canWrite;
    private String folderId;

    public Channel(){}

    public Channel(String name, String description, List<String> messageDTOList, boolean canWrite,String folderId) {
        this.name = name;
        this.description = description;
        this.messageDTOList = messageDTOList;
        this.canWrite = canWrite;
        this.folderId=folderId;
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


    public List<String> getMessageDTOList() {
        return messageDTOList;
    }

    public void setMessageDTOList(List<String> messageDTOList) {
        this.messageDTOList = messageDTOList;
    }


    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public List<RolePermissionDTO> getRolePermissionDTOList() {
        return rolePermissionDTOList;
    }

    public void setRolePermissionDTOList(List<RolePermissionDTO> rolePermissionDTOList) {
        this.rolePermissionDTOList = rolePermissionDTOList;
    }
    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

}

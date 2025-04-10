package com.example.demo3.Model;

import java.util.List;

public class StudiesDTO {
    private int id;
    private String name;
    private String description;
    private List<StudyProgramDTO> studyPrograms;



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

    public List<StudyProgramDTO> getStudyPrograms() {
        return studyPrograms;
    }

    public void setStudyPrograms(List<StudyProgramDTO> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }
}

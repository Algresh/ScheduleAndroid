package com.example.alex.scheduleandroid.dto;

/**
 * Created by alex on 02.03.16.
 */
public class Group {

    private String titleGrp;

    private int versionGrp;

    private int idGrp;

    private int numberMessages;

    private int course;

    public Group(String titleGrp, int versionGrp, int idGrp, int numberMessages , int course) {
        this.titleGrp = titleGrp;
        this.versionGrp = versionGrp;
        this.idGrp = idGrp;
        this.numberMessages = numberMessages;
        this.course = course;
    }

    public String getTitleGrp() {
        return titleGrp;
    }

    public void setTitleGrp(String titleGrp) {
        this.titleGrp = titleGrp;
    }

    public int getVersionGrp() {
        return versionGrp;
    }

    public void setVersionGrp(int versionGrp) {
        this.versionGrp = versionGrp;
    }

    public int getIdGrp() {
        return idGrp;
    }

    public void setIdGrp(int idGrp) {
        this.idGrp = idGrp;
    }

    public int getNumberMessages() {
        return numberMessages;
    }

    public void setNumberMessages(int numberMessages) {
        this.numberMessages = numberMessages;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }
}

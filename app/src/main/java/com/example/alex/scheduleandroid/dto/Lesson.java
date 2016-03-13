package com.example.alex.scheduleandroid.dto;

/**
 * Created by alex on 06.03.16.
 */
public class Lesson {

    private String titleOfSubject;

    private int numberOfLesson;

    private String classRoom;

    private String typeLesson;

    private String teacher;

    private int sunGroup;

    private String adress;

    private String[] dateOfLesson;

    public Lesson(String titleOfSubject, int numberOfLesson, String classRoom, String typeLesson, String teacher, int sunGroup, String adress, String[] dateOfLesson) {
        this.titleOfSubject = titleOfSubject;
        this.numberOfLesson = numberOfLesson;
        this.classRoom = classRoom;
        this.typeLesson = typeLesson;
        this.teacher = teacher;
        this.sunGroup = sunGroup;
        this.adress = adress;
        this.dateOfLesson = dateOfLesson;
    }

    public String getTitleOfSubject() {
        return titleOfSubject;
    }

    public void setTitleOfSubject(String titleOfSubject) {
        this.titleOfSubject = titleOfSubject;
    }

    public int getNumberOfLesson() {
        return numberOfLesson;
    }

    public void setNumberOfLesson(int numberOfLesson) {
        this.numberOfLesson = numberOfLesson;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getTypeLesson() {
        return typeLesson;
    }

    public void setTypeLesson(String typeLesson) {
        this.typeLesson = typeLesson;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getSunGroup() {
        return sunGroup;
    }

    public void setSunGroup(int sunGroup) {
        this.sunGroup = sunGroup;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String[] getDateOfLesson() {
        return dateOfLesson;
    }

    public void setDateOfLesson(String[] dateOfLesson) {
        this.dateOfLesson = dateOfLesson;
    }
}

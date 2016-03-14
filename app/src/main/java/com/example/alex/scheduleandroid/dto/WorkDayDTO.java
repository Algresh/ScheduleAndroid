package com.example.alex.scheduleandroid.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 06.03.16.
 */
public class WorkDayDTO {
    private String[] dateOfWorkDay;

    private List<Lesson> lessons;

    public WorkDayDTO(String[] dateOfWorkDay) {
        this.dateOfWorkDay = dateOfWorkDay;

        lessons = new ArrayList<Lesson>();
    }

    public String[] getDateOfWorkDay() {
        return dateOfWorkDay;
    }

    public String getDateOfWorkDay(int position) {
        return dateOfWorkDay[position];
    }

    public void setDateOfWorkDay(String[] dateOfWorkDay) {
        this.dateOfWorkDay = dateOfWorkDay;
    }

    public void setLesson(Lesson grp) {
        this.lessons.add(grp);
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Lesson getLesson(int position) {
        return lessons.get(position);
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public int getNumberOfLessons() {
        return lessons.size();
    }

}

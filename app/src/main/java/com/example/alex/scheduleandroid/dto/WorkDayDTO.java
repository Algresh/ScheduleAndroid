package com.example.alex.scheduleandroid.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 06.03.16.
 */
public class WorkDayDTO {
    private List<String> dateOfWorkDay;

    private List<Lesson> lessons;

    public WorkDayDTO(List dateOfWorkDay) {
        this.dateOfWorkDay = dateOfWorkDay;

        lessons = new ArrayList<Lesson>();
    }

    public List getDateOfWorkDay() {
        return dateOfWorkDay;
    }

    public String getDateOfWorkDay(int position) {
        return dateOfWorkDay.get(position);
    }

    public void setDateOfWorkDay(List dateOfWorkDay) {
        this.dateOfWorkDay = dateOfWorkDay;
    }

    public void setLesson(Lesson grp) {
        this.lessons.add(grp);
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void addNewLessons(List<Lesson> lessons){
        this.lessons.addAll(lessons);
    }

    public void addNewDateOfWorkDay(List<String> dateOfWorkDay){
        this.dateOfWorkDay.addAll(dateOfWorkDay);
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

    public int getNumberOfDateOfWorkDay() {
        return dateOfWorkDay.size();
    }

    @Override
    public String toString() {
        return "WorkDayDTO{" +
                "dateOfWorkDay=" + dateOfWorkDay +
                ", lessons=" + lessons +
                '}';
    }
}

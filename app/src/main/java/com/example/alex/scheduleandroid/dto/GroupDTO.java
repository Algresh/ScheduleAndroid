package com.example.alex.scheduleandroid.dto;



import com.example.alex.scheduleandroid.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by algresh on 14.02.16.
 */
public class GroupDTO {
    private String titleFaculty;

    private List<Group> groups;

    public GroupDTO(String title) {
        this.titleFaculty = title;

        groups = new ArrayList<Group>();
    }

    public String getTitle() {
        return titleFaculty;
    }

    public void setTitle(String title) {
        this.titleFaculty = title;
    }

    public void setGroup(Group grp) {
        this.groups.add(grp);
    }

    public void setGroups(List<Group> grps) {
        this.groups = grps;
    }

    public Group getGroup(int position) {
        return groups.get(position);
    }

    public List<Group> getGroups() {
        return groups;
    }

    public String[] getNameOfGroups(int numCourse) {
        int numberGrps = this.numberGroupsByCourse(numCourse);
        String[] nameGroups;
        nameGroups  = new String[numberGrps];

        int i = 0;
        for (Group item : groups) {
            if(item.getCourse() == numCourse){
                nameGroups[i] = item.getTitleGrp();
                i++;
            }
        }

        return nameGroups;
    }

    private int numberGroupsByCourse(int course) {
        int numberRightGrps = 0;
        for (Group item : groups) {
            if (item.getCourse() == course) {
                numberRightGrps++;
            }
        }

        return  numberRightGrps;
    }



}

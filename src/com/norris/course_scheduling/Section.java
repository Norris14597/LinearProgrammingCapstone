package com.norris.course_scheduling;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private char sectionId;
    private int professorIdAssigned;

    private List<Room> roomsAssigned;

    private List<Day> dayList; //holds M-F from 8-5pm


    public Section(char id, int pid) {
        this.sectionId = id;
        this.professorIdAssigned = pid;

        dayList = new ArrayList<Day>();
        dayList.add(new Day("Monday"));
        dayList.add(new Day("Tuesday"));
        dayList.add(new Day("Wednesday"));
        dayList.add(new Day("Thursday"));
        dayList.add(new Day("Friday"));
    }

    public char getSectionId() {
        return this.sectionId;
    }

    public void setSectionId(char id) {
        this.sectionId = id;
    }

    public int getProfessorIdAssigned() {
        return professorIdAssigned;
    }

    public void setProfessorIdAssigned(int professorIdAssigned) {
        this.professorIdAssigned = professorIdAssigned;
    }

    public String toString () {
        return " Section ID: "+Character.toString(this.sectionId)+" Professor ID: "+this.professorIdAssigned;
    }

}

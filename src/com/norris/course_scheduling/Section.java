package com.norris.course_scheduling;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private char section; // section character
    private String sectionID; //both course and section name
    private Professor professorAssigned;

    private int hours;
    private Room roomAssigned; //one room per section

    private List<DayTimes> dayTimeAssigned; //holds value of time decision for each day


    public Section(char id, String sec, Professor p) {
        this.section = id;
        this.sectionID = sec;
        this.professorAssigned = p;

        dayTimeAssigned = new ArrayList<DayTimes>();

        dayTimeAssigned.add(new DayTimes("Monday"));
        dayTimeAssigned.add(new DayTimes("Tuesday"));
        dayTimeAssigned.add(new DayTimes("Wednesday"));
        dayTimeAssigned.add(new DayTimes("Thursday"));
        dayTimeAssigned.add(new DayTimes("Friday"));

    }

    public char getSection() {
        return this.section;
    }

    public void setSection(char id) {
        this.section = id;
    }

    public Professor getProfessorAssigned() {
        return professorAssigned;
    }

    public void setProfessorAssigned(Professor professorAssigned) {
        this.professorAssigned = professorAssigned;
    }

    public String getSectionID() {
        return sectionID;
    }

    public void setSectionID(String sectionID) {
        this.sectionID = sectionID;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public Room getRoomAssigned() {
        return roomAssigned;
    }

    public void setRoomAssigned(Room roomAssigned) {
        this.roomAssigned = roomAssigned;
    }

    public List<DayTimes> getDayTimeAssigned() {
        return dayTimeAssigned;
    }

    public void setDayTimeAssigned(List<DayTimes> dayTimeAssigned) {
        this.dayTimeAssigned = dayTimeAssigned;
    }

    public String toString () {
        return "\n\t"+" SECTION: "+Character.toString(section)+"   |   ID: "+sectionID+"   |   Professor: "+professorAssigned.toString();
    }

}

package com.norris.course_scheduling;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private char section;
    private String sectionID;
    private Professor professorAssigned;

    private int hours;
    private Room roomAssigned; //one room per section

    private List<SectionSlot> dayTimeAssigned; //holds value of time decision for each day


    public Section(char id, String sec, Professor p) {
        this.section = id;
        this.sectionID = sec;
        this.professorAssigned = p;

        dayTimeAssigned = new ArrayList<SectionSlot>();

    }

    public char getSection() {
        return this.section;
    }

    public void setSection(char id) {
        this.section = id;
    }

    public Professor getProfessorIdAssigned() {
        return professorAssigned;
    }

    public void setProfessorIdAssigned(Professor professorAssigned) {
        this.professorAssigned = professorAssigned;
    }

    public String toString () {
        return "\n\t"+" SECTION: "+Character.toString(section)+" ID: "+sectionID+" Professor: "+professorAssigned.toString();
    }

}

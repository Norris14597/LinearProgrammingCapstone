package com.norris.course_scheduling;

import java.util.List;

public class Section {
    private char sectionId;
    private int professorIdAssigned;

    private List<Room> roomsAssigned;

//    private double startTime;
//    private double duration; //minutes

    /*

    private TimesHeld timesheld;

    monday at 2:00pm for 1 hour in J100 and wednesday at 1:00pm for 1:30 hours in B200
     */


    public Section(char id, int pid) {
        this.sectionId = id;
        this.professorIdAssigned = pid;
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

}

package com.norris.course_scheduling;


import java.util.ArrayList;
import java.util.List;

public class Professor {

//    Anthony Corso
//    Larry Clement
//    Creed Jones
//    Mi Kyung Han
//    Kyungsoo Im
//    Michael Kolta
//    Arlene Louise Perkins

    private String professorName;
    private String[] availableDaysNames;
    private List<DayTimes> availableDayTimes;
    private List<Section> sectionsTaught;

    public Professor(String n, String[] days) {
        this.professorName = n;
        this.availableDaysNames = days;
        this.availableDayTimes = new ArrayList<>();
        for (int i = 0; i < days.length; i++) {
            this.availableDayTimes.add(new DayTimes(days[i]));
        }

        sectionsTaught = new ArrayList<Section>();
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }


    public String[] getAvailableDaysNames() {
        return availableDaysNames;
    }

    public void setAvailableDaysNames(String[] availableDaysNames) {
        this.availableDaysNames = availableDaysNames;
    }

    public List<DayTimes> getAvailableDayTimes() {
        return availableDayTimes;
    }

    public void setAvailableDayTimes(List<DayTimes> availableDayTimes) {
        this.availableDayTimes = availableDayTimes;
    }

    public List<Section> getSectionsTaught() {
        return sectionsTaught;
    }

    public void setSectionsTaught(List<Section> sectionsTaught) {
        this.sectionsTaught = sectionsTaught;
    }

    public String toString() {
        return professorName;
    }
}

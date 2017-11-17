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
    private List<DayTimes> availableTimes;
    //private List<Section> sectionsTaught;

    public Professor(String n, String[] days) {
        this.professorName = n;
        this.availableTimes = new ArrayList<>();
        for (int i = 0; i < days.length; i++) {
            this.availableTimes.add(new DayTimes(days[i]));
        }

        //sectionsTaught = s;
    }

    public String toString() {
        return professorName;
    }
}

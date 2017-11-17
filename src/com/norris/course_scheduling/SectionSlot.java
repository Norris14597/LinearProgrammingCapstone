package com.norris.course_scheduling;


import java.util.ArrayList;

public class SectionSlot {

    private String day;
    private ArrayList<TimeLength> classTimes;
    private int dayMinutes;

    public SectionSlot(String d) {
        this.day = d;
        classTimes = new ArrayList<TimeLength>();
    }

    public void fillClass(String y, int x){
        if (day.equals(y)) {
            classTimes.get(x).setTrue(1);
        }
    }

    public String toString() {
        return "Class Time: "+day+classTimes.toString();
    }

}

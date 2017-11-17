package com.norris.course_scheduling;


import java.util.ArrayList;

public class DayTimes {

    private String day;
    private ArrayList<TimeLength> dayTimes;

    public DayTimes(String d) {
        this.day = d;

        dayTimes = new ArrayList<>();
        for (int i = 8; i < 17; i++) {
            for (int j = 0; j <= 45; j+=15) {
                if (j == 45) {
                    dayTimes.add(new TimeLength(i, j, i + 1, 0));
                }
                else {
                    dayTimes.add(new TimeLength(i, j, i, j + 15));
                }
            }
        }
    }

    public void fillDay(String y, int x){
        if (day.equals(y)) {
            dayTimes.get(x).setTrue(1);
        }
    }

    public String toString() {
        return "\n\tDAY: "+day+"\n\t"+dayTimes.toString();
    }

}

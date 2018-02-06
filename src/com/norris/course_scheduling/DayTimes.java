package com.norris.course_scheduling;


import java.util.ArrayList;

public class DayTimes {

    private String day;
    private ArrayList<TimeLength> dayTimes;

    public DayTimes(String d) {
        this.day = d;

        dayTimes = new ArrayList<>();
        //8am to 9pm for i
        //15 minute slots for each hour
        for (int i = 8; i < 21; i++) {
            for (int j = 0; j <= 45; j+=15) {
                if (j == 45) {
                    dayTimes.add(new TimeLength("", i, j, i + 1, 0));
                }
                else {
                    dayTimes.add(new TimeLength("", i, j, i, j + 15));
                }
            }
        }
    }

    public void fillDay(String y, int x){
        if (day.equals(y)) {
            dayTimes.get(x).setTrue(1);
        }
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<TimeLength> getDayTimes() {
        return dayTimes;
    }

    public void setDayTimes(ArrayList<TimeLength> dayTimes) {
        this.dayTimes = dayTimes;
    }

    public String toString() {
        return "\n\tDAY: "+day+"\n\t"+dayTimes.toString();
    }

}

package com.norris.course_scheduling;

/**
 * Created by jennasowers on 11/8/17.
 */
public class TimeLength {

    private int startTimeHour;
    private int startTimeMinute;
    private int endTimeHour;
    private int endTimeMinute;

    private boolean isTimeFilled;

    public TimeLength(int sh, int sm, int eh, int em) {
        this.startTimeHour = sh;
        this.startTimeMinute = sm;
        this.endTimeHour = eh;
        this.endTimeMinute = em;
        this.isTimeFilled = false;
    }

    public String toString() {
        return "starttime: "+startTimeHour+":"+startTimeMinute+" endtime: "+endTimeHour+":"+endTimeMinute+isTimeFilled;
    }
}
